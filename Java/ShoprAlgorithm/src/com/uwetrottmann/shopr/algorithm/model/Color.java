
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Arrays;
import java.util.List;

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
        UndirectedGraph<Color.Value, DefaultEdge> g =
                new SimpleGraph<Color.Value, DefaultEdge>(DefaultEdge.class);

        Value[] values = Value.values();
        for (Value value : values) {
            g.addVertex(value);
        }

        g.addEdge(Value.RED, Value.VIOLET);
        g.addEdge(Value.RED, Value.PINK);
        g.addEdge(Value.BLUE, Value.VIOLET);
        g.addEdge(Value.BLUE, Value.TURQUOISE);

        Value likedColor = values[valueIndex];
        List<Value> similarColors = Graphs.neighborListOf(g, likedColor);

        if (similarColors.isEmpty()) {
            // treat like a regular like
            super.likeValue(valueIndex, weights);
        } else {
            // do not touch similar colors weights

            // calculate average weight
            double weightIncrease = 1.0 / weights.length;

            /*
             * If the value was 0.0 (disliked) increase double so it will
             * definitely have highest weight.
             */
            if (weights[valueIndex] == 0.0) {
                weightIncrease *= 2;
            }

            // add weight to liked value
            weights[valueIndex] += weightIncrease;

            // if liked value weight exceeds 1.0, sum exceeds 1.0
            if (weights[valueIndex] > 1.0) {
                // all other weights have to be 0.0
                Arrays.fill(weights, 0.0);
                weights[valueIndex] = 1.0;
                return;
            }

            /*
             * Subtract added weight evenly from non-liked colors, BUT only from
             * non-similar colors.
             */
            double redistributed = weightIncrease
                    / (weights.length - 1 - similarColors.size());
            for (int i = 0; i < weights.length; i++) {
                if (i != valueIndex && !hasValueWithSameIndex(similarColors, i)) {
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
                // distribute remaining weight evenly over all similar values
                double redistribute = (1 - weights[valueIndex]) / similarColors.size();
                for (int i = 0; i < weights.length; i++) {
                    if (i == valueIndex) {
                        // weight for liked color already set
                        continue;
                    }
                    if (hasValueWithSameIndex(similarColors, i)) {
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
    private boolean hasValueWithSameIndex(List<Value> values, int index) {
        for (Value value : values) {
            if (value.index() == index) {
                return true;
            }
        }
        return false;
    }
}
