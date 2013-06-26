
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public class ClothingType extends GenericAttribute {

    public static final String ID = "clothing-type";

    public enum Value implements AttributeValue {
        SWIMSUIT("Swim suit"),
        TRUNKS("Trunks"),
        BRA("Bra"),
        BLOUSE("Blouse"),
        SHIRT("Shirt"),
        TROUSERS("Trouser"),
        JACKET("Jacket"),
        JEANS("Jeans"),
        DRESS("Dress"),
        POLOSHIRT("Poloshirt"),
        SWEATER("Sweater"), // Pullover
        SKIRT("Skirt"),
        SLIP("Slip"),
        PANTS("Pants"),
        BRIEFS("Briefs"), // Slip
        THONG("Thong"), // String
        CARDIGAN("Cardigan"), // Strickjacke
        STOCKING("Stocking"), // Strumpf
        PANTYHOSE("Pantyhose"), // Strumpfhose
        TSHIRT("T-Shirt"),
        TOP("Top"),
        UNDERSHIRT("Undershirt"),
        UNDERPANTS("Underpants"),
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
        setWeights(value);
    }

    public ClothingType(String name) {
        if ("Badeanzug".equals(name)) {
            setWeights(Value.SWIMSUIT);
        }
        else if ("Badehose".equals(name)) {
            setWeights(Value.TRUNKS);
        }
        else if ("BH".equals(name)) {
            setWeights(Value.BRA);
        }
        else if ("Bluse".equals(name)) {
            setWeights(Value.BLOUSE);
        }
        else if ("Hemd".equals(name) || "Shirt".equals(name)) {
            setWeights(Value.SHIRT);
        }
        else if ("Hose".equals(name)) {
            setWeights(Value.TROUSERS);
        }
        else if ("Jacke".equals(name)) {
            setWeights(Value.JACKET);
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
        else if ("Slip".equals(name)) {
            setWeights(Value.BRIEFS);
        }
        else if ("Stoffhose".equals(name)) {
            setWeights(Value.PANTS);
        }
        else if ("Strickjacke".equals(name)) {
            setWeights(Value.CARDIGAN);
        }
        else if ("String".equals(name)) {
            setWeights(Value.THONG);
        }
        else if ("Strumpf".equals(name)) {
            setWeights(Value.STOCKING);
        }
        else if ("Strumpfhose".equals(name)) {
            setWeights(Value.PANTYHOSE);
        }
        else if ("T-Shirt".equals(name)) {
            setWeights(Value.TSHIRT);
        }
        else if ("Top".equals(name)) {
            setWeights(Value.TOP);
        }
        else if ("Unterhemd".equals(name)) {
            setWeights(Value.UNDERSHIRT);
        }
        else if ("Unterhose".equals(name)) {
            setWeights(Value.UNDERPANTS);
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
