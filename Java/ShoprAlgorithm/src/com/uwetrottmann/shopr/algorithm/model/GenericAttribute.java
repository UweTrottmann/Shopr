
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public abstract class GenericAttribute implements Attribute {

    public static final String ID = null;

    private AttributeValue currentValue;

    double[] mValueWeights;

    @Override
    public String id() {
        return ID;
    }

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
                reason.append("no ").append(values[i].descriptor());
            }
            if (mValueWeights[maxIndex] > mValueWeights[i]) {
                maxIndex = i;
            }
        }

        // e.g. ", mainly Red"
        if (reason.length() != 0) {
            reason.append(", ");
        }
        reason.append("mainly ").append(values[maxIndex].descriptor());

        return reason.toString();
    }

}
