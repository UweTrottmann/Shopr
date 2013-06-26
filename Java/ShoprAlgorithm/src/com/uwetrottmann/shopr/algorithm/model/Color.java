
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public class Color extends GenericAttribute {

    public static final String ID = "color";

    public enum Value implements AttributeValue {
        BLUE("Blue"),
        BROWN("Brown"),
        COLORED("Colored"),
        YELLOW("Yellow"),
        MIXED("Mixed"),
        GREY("Grey"),
        GREEN("Green"),
        ORANGE("Orange"),
        PINK("Pink"), // rosa zu Deutsch
        RED("Red"),
        BLACK("Black"),
        TURQUOISE("Turquoise"),
        VIOLET("Violet"),
        WHITE("White");

        String mDescriptor;

        Value(String name) {
            mDescriptor = name;
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

    public Color() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Color(Value value) {
        setWeights(value);
    }

    /**
     * Tries to match the given string with a {@link Color.Value}.
     */
    public Color(String value) {
        if ("Blau".equals(value)) {
            setWeights(Color.Value.BLUE);
        }
        else if ("Braun".equals(value)) {
            setWeights(Color.Value.BROWN);
        }
        else if ("Bunt".equals(value)) {
            setWeights(Color.Value.COLORED);
        }
        else if ("Gelb".equals(value)) {
            setWeights(Color.Value.YELLOW);
        }
        else if ("Gemischt".equals(value)) {
            setWeights(Color.Value.MIXED);
        }
        else if ("Grau".equals(value)) {
            setWeights(Color.Value.GREY);
        }
        else if ("Grün".equals(value)) {
            setWeights(Color.Value.GREEN);
        }
        else if ("Orange".equals(value)) {
            setWeights(Color.Value.ORANGE);
        }
        else if ("Rosa".equals(value)) {
            setWeights(Color.Value.PINK);
        }
        else if ("Rot".equals(value)) {
            setWeights(Color.Value.RED);
        }
        else if ("Schwarz".equals(value)) {
            setWeights(Color.Value.BLACK);
        }
        else if ("Türkis".equals(value)) {
            setWeights(Color.Value.TURQUOISE);
        }
        else if ("Violett".equals(value)) {
            setWeights(Color.Value.VIOLET);
        }
        else if ("Weiß".equals(value)) {
            setWeights(Color.Value.WHITE);
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
    public AttributeValue[] getValueSymbols() {
        return Value.values();
    }
}
