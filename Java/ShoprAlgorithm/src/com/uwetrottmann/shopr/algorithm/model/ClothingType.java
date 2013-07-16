
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Arrays;
import java.util.List;

public class ClothingType extends GenericAttribute {

    private static UndirectedGraph<ClothingType.Value, DefaultEdge> sSimilarValues;

    static {
        sSimilarValues = new SimpleGraph<ClothingType.Value, DefaultEdge>(DefaultEdge.class);

        Value[] values = Value.values();
        for (Value value : values) {
            sSimilarValues.addVertex(value);
        }

        /**
         * Store similar clothing type values in an undirected graph.
         */
        sSimilarValues.addEdge(Value.SHIRT, Value.POLOSHIRT);
        sSimilarValues.addEdge(Value.SHIRT, Value.BLOUSE);
        sSimilarValues.addEdge(Value.TROUSERS, Value.JEANS);
        sSimilarValues.addEdge(Value.TROUSERS, Value.SHORTS);
        sSimilarValues.addEdge(Value.TROUSERS, Value.SKIRT);
        sSimilarValues.addEdge(Value.SKIRT, Value.SHORTS);
        sSimilarValues.addEdge(Value.CARDIGAN, Value.SWEATER);
        sSimilarValues.addEdge(Value.TOP, Value.SHIRT);
        sSimilarValues.addEdge(Value.TOP, Value.BLOUSE);
    }

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

    @Override
    public void likeValue(int valueIndex, double[] weights) {
        Value[] values = Value.values();
        Value likedColor = values[valueIndex];
        List<Value> similarTypes = Graphs.neighborListOf(sSimilarValues, likedColor);

        if (similarTypes.isEmpty()) {
            // treat like a regular like
            super.likeValue(valueIndex, weights);
        } else {
            // do not touch similar type weights

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
             * Subtract added weight evenly from other types, BUT only from
             * non-similar types and those with non-zero weights.
             */
            // get number of non-zero weights
            int count = 0;
            for (int i = 0; i < weights.length; i++) {
                if (weights[i] != 0 && !hasValueWithSameIndex(similarTypes, i)) {
                    count++;
                }
            }
            // calculate share for each value
            double redistributed = weightIncrease / (count - 1);
            for (int i = 0; i < weights.length; i++) {
                if (i != valueIndex && !hasValueWithSameIndex(similarTypes, i)) {
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
                double redistribute = (1 - weights[valueIndex]) / similarTypes.size();
                for (int i = 0; i < weights.length; i++) {
                    if (i == valueIndex) {
                        // weight for liked type already set
                        continue;
                    }
                    if (hasValueWithSameIndex(similarTypes, i)) {
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
