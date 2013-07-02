
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.util.Arrays;

public class Color extends GenericAttribute {

    public static final String ID = "color";

    public enum Value implements AttributeValue {
        BLUE("Blue"),
        RED("Red"),
        PINK("Pink"), // rosa zu Deutsch
        VIOLET("Violet"),
        YELLOW("Yellow"),
        BROWN("Brown"),
        COLORED("Colored"),
        MIXED("Mixed"),
        GREY("Grey"),
        GREEN("Green"),
        ORANGE("Orange"),
        BLACK("Black"),
        TURQUOISE("Turquoise"),
        WHITE("White"),
        BEIGE("Beige");

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
        else if ("Beige".equals(value)) {
            setWeights(Color.Value.BEIGE);
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

    @Override
    public void likeValue(int valueIndex, double[] weights) {
        Value[][] similarity = new Value[Value.values().length][];
        similarity[Value.RED.index()] = new Value[] {
                Value.PINK, Value.VIOLET
        };
        similarity[Value.BLUE.index()] = new Value[] {
                Value.VIOLET, Value.TURQUOISE
        };
        similarity[Value.WHITE.index()] = new Value[] {
                Value.GREY, Value.BEIGE
        };
        similarity[Value.BLACK.index()] = new Value[] {
                Value.GREY, Value.BLUE, Value.BROWN
        };

        if (similarity[valueIndex] == null) {
            super.likeValue(valueIndex, weights);
        } else {
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

            // subtract average from other weights, BUT only from non-similar
            // values
            double redistributed = weightIncrease
                    / (weights.length - 1 - similarity[valueIndex].length);
            for (int i = 0; i < weights.length; i++) {
                if (i != valueIndex && !isSimilarValue(similarity[valueIndex], i)) {
                    weights[i] -= redistributed;
                    // floor at 0.0
                    if (weights[i] < 0) {
                        weights[i] = 0.0;
                    }
                }
            }

            // sum can not exceed 1.0
            double sum = 0;
            for (int i = 0; i < weights.length; i++) {
                sum += weights[i];
            }
            if (sum > 1.0) {
                // distribute remaining weight over all similar values
                double redistribute = 1 - weights[valueIndex];
                for (int i = 0; i < weights.length; i++) {
                    if (i != valueIndex && isSimilarValue(similarity[valueIndex], i)) {
                        weights[i] = redistribute;
                    } else {
                        weights[i] = 0.0;
                    }
                }
            }
        }
    }

    /**
     * Checks whether one of the values has the given index.
     */
    private boolean isSimilarValue(Value[] values, int index) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].index() == index) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dislikeValue(int valueIndex, double[] weights) {
        // TODO Auto-generated method stub
        super.dislikeValue(valueIndex, weights);
    }
}
