
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Color.Value;

public abstract class GenericAttribute {

    double[] mValueWeights;

    public double[] getValueWeights() {
        return mValueWeights;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Value[] values = Value.values();

        builder.append("[");
        for (int i = 0; i < mValueWeights.length; i++) {
            if (mValueWeights[i] != 0) {
                builder.append(values[i]).append(":").append(mValueWeights[i]).append(" ");
            }
        }
        builder.append("]");

        return builder.toString();
    }

}
