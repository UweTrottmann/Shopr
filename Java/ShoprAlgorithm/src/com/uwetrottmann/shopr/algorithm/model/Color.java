
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.Arrays;

public class Color implements Attribute {

    enum Value {
        RED,
        BLUE,
        GREEN,
        BLACK
    }

    double[] mValueWeights;

    public Color() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Color(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
    }

    public double[] getValueWeights() {
        return mValueWeights;
    }
}
