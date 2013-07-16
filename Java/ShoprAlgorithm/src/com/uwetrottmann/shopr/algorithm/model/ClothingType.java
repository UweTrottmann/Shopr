
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public class ClothingType extends GenericAttribute {

    public static final String ID = "clothing-type";

    public enum Value implements AttributeValue {
        SWIMSUIT("Swim suit"),
        TRUNKS("Trunks"),
        BLOUSE("Blouse"),
        SHIRT("Shirt"),
        TROUSERS("Trousers"),
        JEANS("Jeans"),
        DRESS("Dress"),
        POLOSHIRT("Poloshirt"),
        SWEATER("Sweater"), // Pullover
        SKIRT("Skirt"),
        SHORTS("Shorts"),
        CARDIGAN("Cardigan"), // Strickjacke
        TOP("Top/T-Shirt");

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
        setWeights(value);
    }

    public ClothingType(String name) {
        if ("Badeanzug".equals(name)) {
            setWeights(Value.SWIMSUIT);
        }
        else if ("Badehose".equals(name)) {
            setWeights(Value.TRUNKS);
        }
        else if ("Bluse".equals(name)) {
            setWeights(Value.BLOUSE);
        }
        else if ("Hemd".equals(name)) {
            setWeights(Value.SHIRT);
        }
        else if ("Hose".equals(name)) {
            setWeights(Value.TROUSERS);
        }
        else if ("Jeans".equals(name)) {
            setWeights(Value.JEANS);
        }
        else if ("Kleid".equals(name)) {
            setWeights(Value.DRESS);
        }
        else if ("Poloshirt".equals(name)) {
            setWeights(Value.POLOSHIRT);
        }
        else if ("Pullover".equals(name)) {
            setWeights(Value.SWEATER);
        }
        else if ("Rock".equals(name)) {
            setWeights(Value.SKIRT);
        }
        else if ("Strickjacke".equals(name)) {
            setWeights(Value.CARDIGAN);
        }
        else if ("Top".equals(name)) {
            setWeights(Value.TOP);
        }
        else if ("Shorts".equals(name)) {
            setWeights(Value.SHORTS);
        }
    }

    private void setWeights(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
        currentValue(value);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Value[] getValueSymbols() {
        return Value.values();
    }

}
