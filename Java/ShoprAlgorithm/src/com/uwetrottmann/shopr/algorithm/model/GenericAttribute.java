
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public abstract class GenericAttribute implements Attribute {

    private AttributeValue currentValue;

    double[] mValueWeights;

    public double[] getValueWeights() {
        return mValueWeights;
    }

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

    public abstract AttributeValue[] getValueSymbols();

    public AttributeValue currentValue() {
        return currentValue;
    }

    public GenericAttribute currentValue(AttributeValue currentValue) {
        this.currentValue = currentValue;
        return this;
    }

}
