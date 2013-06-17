
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public class ClothingType extends GenericAttribute {

    public static final String ID = "clothing-type";

    public enum Value implements AttributeValue {
        DRESS("Dress"),
        SHIRT("Shirt"),
        TROUSER("Trouser"),
        JEANS("Jeans"),
        SHORTS("Shorts");

        String mDescriptor;

        Value(String descriptor) {
            mDescriptor = descriptor;
        }

        @Override
        public String descriptor() {
            return mDescriptor;
        }

        @Override
        public int index() {
            return ordinal();
        }
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
        currentValue(value);
    }

    @Override
    public Value[] getValueSymbols() {
        return Value.values();
    }

}
