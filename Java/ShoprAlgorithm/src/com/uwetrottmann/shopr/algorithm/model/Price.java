
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Price extends GenericAttribute {

    private static UndirectedGraph<Price.Value, DefaultEdge> sSimilarValues;

    static {
        sSimilarValues = new SimpleGraph<Price.Value, DefaultEdge>(DefaultEdge.class);

        Value[] values = Value.values();
        for (Value value : values) {
            sSimilarValues.addVertex(value);
        }

        /**
         * Stores similar price values in an undirected graph. Price regions
         * right above or below are similar.
         */
        sSimilarValues.addEdge(Value.SUB_25, Value.BETWEEN_25_50);
        sSimilarValues.addEdge(Value.BETWEEN_25_50, Value.BETWEEN_50_75);
        sSimilarValues.addEdge(Value.BETWEEN_50_75, Value.BETWEEN_75_100);
        sSimilarValues.addEdge(Value.BETWEEN_75_100, Value.BETWEEN_100_150);
        sSimilarValues.addEdge(Value.BETWEEN_100_150, Value.BETWEEN_150_200);
        sSimilarValues.addEdge(Value.BETWEEN_150_200, Value.ABOVE_200);
    }

    public static final String ID = "price";

    public enum Value implements AttributeValue {
        SUB_25("less than 25 €"),
        BETWEEN_25_50("25 to 50 €"),
        BETWEEN_50_75("50 to 75 €"),
        BETWEEN_75_100("75 to 100 €"),
        BETWEEN_100_150("100 to 150 €"),
        BETWEEN_150_200("150 to 200 €"),
        ABOVE_200("200 € or more");

        private String mDescriptor;

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

    public Price() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Price(BigDecimal price) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);

        // determine price range
        Value value;
        if (price.doubleValue() < 25.0) {
            value = Value.SUB_25;
        } else if (price.doubleValue() < 50.0) {
            value = Value.BETWEEN_25_50;
        } else if (price.doubleValue() < 75.0) {
            value = Value.BETWEEN_50_75;
        } else if (price.doubleValue() < 100.0) {
            value = Value.BETWEEN_75_100;
        } else if (price.doubleValue() < 150.0) {
            value = Value.BETWEEN_100_150;
        } else if (price.doubleValue() < 200.0) {
            value = Value.BETWEEN_150_200;
        } else {
            value = Value.ABOVE_200;
        }

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

    /**
     * Changes the behavior to also increase the weight of neighboring weights.
     * E.g. a user might like items priced in a little more expensive or a
     * little cheaper based on the price range he liked.
     */
    @Override
    public void likeValue(int valueIndex, double[] weights) {
        Value[] values = Value.values();
        Value likedPriceRegion = values[valueIndex];
        List<Value> similarPriceRegions = Graphs.neighborListOf(sSimilarValues, likedPriceRegion);

        if (similarPriceRegions.isEmpty()) {
            // treat like a regular like
            super.likeValue(valueIndex, weights);
        } else {
            // do not touch similar price weights

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
             * Subtract added weight evenly from other price regions, BUT only
             * from non-similar price regions and those with non-zero weight.
             */
            // get number of non-zero weights
            int count = 0;
            for (int i = 0; i < weights.length; i++) {
                if (weights[i] != 0 && !hasValueWithSameIndex(similarPriceRegions, i)) {
                    count++;
                }
            }
            // calculate share for each value
            double redistributed = weightIncrease / (count - 1);
            for (int i = 0; i < weights.length; i++) {
                if (i != valueIndex && !hasValueWithSameIndex(similarPriceRegions, i)) {
                    weights[i] -= redistributed;
                    // precaution: floor at 0.0
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
                double redistribute = (1 - weights[valueIndex]) / similarPriceRegions.size();
                for (int i = 0; i < weights.length; i++) {
                    if (i == valueIndex) {
                        // weight for liked price region already set
                        continue;
                    }
                    if (hasValueWithSameIndex(similarPriceRegions, i)) {
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
