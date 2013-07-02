
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

    /**
     * Returns string for the given attribute having weight 1.0, e.g. only items
     * that have this value are recommended.
     */
    public String getOnlyString(AttributeValue value) {
        return "only " + value.descriptor();
    }

    /**
     * Returns string for the given attribute having a 0.0 weight, e.g. it is
     * avoided to recommend items with this value unless there are no other
     * options.
     */
    public String getAvoidString(AttributeValue value) {
        return "avoid " + value.descriptor();
    }

    /**
     * Returns string for the given attribute having the highest weight of all,
     * e.g. recommended items are likely to have this value.
     */
    public String getPreferablyString(AttributeValue value) {
        return "preferably " + value.descriptor();
    }

    @Override
    public String getReasonString() {
        StringBuilder reason = new StringBuilder();
        AttributeValue[] values = getValueSymbols();

        int maxIndex = 0; // assuming there is always a maximum
        for (int i = 0; i < mValueWeights.length; i++) {
            if (mValueWeights[i] == 1) {
                // e.g. "only Red"
                return getOnlyString(values[i]);
            }
            if (mValueWeights[i] == 0) {
                // e.g. ", no Red"
                if (reason.length() != 0) {
                    reason.append(", ");
                }
                reason.append(getAvoidString(values[i]));
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
            reason.append(getPreferablyString(values[maxIndex]));
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
        double weightIncrease = 1.0 / weights.length;

        /*
         * If the value was 0.0 (disliked) increase double so it will have
         * definitely have highest weight.
         */
        if (weights[valueIndex] == 0.0) {
            weightIncrease *= 2;
        }

        weights[valueIndex] += weightIncrease;

        // sum can not exceed 1.0
        if (weights[valueIndex] > 1.0) {
            Arrays.fill(weights, 0.0);
            weights[valueIndex] = 1.0;
            return;
        }

        // subtract average from other weights
        double redistributed = weightIncrease / (weights.length - 1);
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
