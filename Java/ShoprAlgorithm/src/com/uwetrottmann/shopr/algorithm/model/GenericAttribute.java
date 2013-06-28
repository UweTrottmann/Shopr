
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public abstract class GenericAttribute implements Attribute {

    private AttributeValue currentValue;

    double[] mValueWeights;

    public AttributeValue currentValue() {
        return currentValue;
    }

    public GenericAttribute currentValue(AttributeValue currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public double[] getValueWeights() {
        return mValueWeights;
    }

    public abstract AttributeValue[] getValueSymbols();

    @Override
    public String getValueWeightsString() {
        StringBuilder builder = new StringBuilder();
        AttributeValue[] values = getValueSymbols();

        builder.append("[");
        for (int i = 0; i < mValueWeights.length; i++) {
            if (mValueWeights[i] == 1) {
                return "[" + values[i].descriptor() + ":" + mValueWeights[i] + "]";
            }
            builder.append(values[i].descriptor()).append(":").append(mValueWeights[i]).append(" ");
        }
        builder.append("]");

        return builder.toString();
    }

    @Override
    public String getReasonString() {
        StringBuilder reason = new StringBuilder();
        AttributeValue[] values = getValueSymbols();

        int maxIndex = 0; // assuming there is always a maximum
        for (int i = 0; i < mValueWeights.length; i++) {
            if (mValueWeights[i] == 1) {
                // e.g. "only Red"
                return "only " + values[i].descriptor();
            }
            if (mValueWeights[i] == 0) {
                // e.g. ", no Red"
                if (reason.length() != 0) {
                    reason.append(", ");
                }
                reason.append("less ").append(values[i].descriptor());
            } else if (mValueWeights[i] > mValueWeights[maxIndex]) {
                maxIndex = i;
            }
        }

        // check for global maximum
        boolean hasGlobalMaximum = true;
        for (int i = 0; i < mValueWeights.length; i++) {
            if (i == maxIndex) {
                continue;
            }
            if (mValueWeights[i] == mValueWeights[maxIndex]) {
                hasGlobalMaximum = false;
                break;
            }
        }

        // e.g. ", mainly Red"
        if (hasGlobalMaximum) {
            if (reason.length() != 0) {
                reason.append(", ");
            }
            reason.append("mainly ").append(values[maxIndex].descriptor());
        }

        return reason.toString();
    }

    /**
     * Updates the given {@link Query}s {@link Attributes} given the positive or
     * negative critique based on the {@link #currentValue()} of this
     * {@link Attribute}.
     */
    public void critiqueQuery(Query query, boolean isPositive) {
        // get value weight index, current weights
        Attribute queryAttr = query.attributes().getAttributeById(id());
        if (queryAttr == null) {
            query.attributes().initializeAttribute(this);
        }

        int valueIndex = currentValue().index();
        double[] weights = query.attributes().getAttributeById(id()).getValueWeights();

        // calculate new weights
        if (isPositive) {
            likeValue(valueIndex, weights);
        } else {
            dislikeValue(valueIndex, weights);
        }
    }

    /**
     * Increases the weight of the liked value by {@code 1 / weights.length},
     * clamps to 1.0. Subtracts the difference evenly from other weights, clamps
     * them to zero if liked value weight is 1.0.
     */
    public void likeValue(int valueIndex, double[] weights) {
        // increase by the average weight
        double likedWeight = 1.0 / weights.length;

        weights[valueIndex] += likedWeight;

        // sum can not exceed 1.0
        if (weights[valueIndex] > 1.0) {
            Arrays.fill(weights, 0.0);
            weights[valueIndex] = 1.0;
            return;
        }

        // subtract average from other weights
        double redistributed = likedWeight / (weights.length - 1);
        for (int i = 0; i < weights.length; i++) {
            if (i != valueIndex) {
                weights[i] -= redistributed;
                // floor at 0.0
                if (weights[i] < 0) {
                    weights[i] = 0.0;
                }
            }
        }
    }

    /**
     * Sets the disliked values weight to zero, distributes its ex-weight evenly
     * to other weights.
     */
    public void dislikeValue(int valueIndex, double[] weights) {
        double dislikedWeight = weights[valueIndex];

        weights[valueIndex] = 0.0;

        int nonZeroCount = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                nonZeroCount++;
            }
        }

        double redistributed = dislikedWeight / nonZeroCount;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                weights[i] += redistributed;
            }
        }
    }
}
