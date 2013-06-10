
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.Arrays;

public class ClothingType extends GenericAttribute implements Attribute {

    public enum Value {
        DRESS,
        SHIRT,
        TROUSER,
        JEANS,
        SHORTS
    }

    public ClothingType() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public ClothingType(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
    }

}
