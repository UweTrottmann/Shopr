
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
    public void likeValue(int indexLiked, double[] weights) {
        Value[] values = Value.values();
        Value valueLiked = values[indexLiked];
        List<Value> similarValues = Graphs.neighborListOf(sSimilarValues, valueLiked);

        // do regular like for liked value
        super.likeValue(indexLiked, weights);

        if (similarValues.isEmpty()) {
            // no similars: done!
            return;
        }

        // now do dampened like on similar values
        double increaseLiked = 1.0 / (weights.length - 1);
        double increaseSimilars = increaseLiked / 2;
        // per similar value increase
        double increaseSimilar = increaseSimilars / similarValues.size();
        // per non-similar and non-liked value decrease
        double decreaseOthers = increaseSimilars / (weights.length - similarValues.size() - 1);

        // actually add and subtract
        for (int i = 0; i < weights.length; i++) {
            if (i == indexLiked) {
                // skip liked value
                continue;
            }
            if (hasValueWithSameIndex(similarValues, i)) {
                // increase similar values
                weights[i] += increaseSimilar;
            } else {
                // decrease other values
                weights[i] -= decreaseOthers;
                // floor at 0.0
                if (weights[i] < 0) {
                    weights[i] = 0.0;
                }
            }
        }

        ensureSumBound(weights);
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
