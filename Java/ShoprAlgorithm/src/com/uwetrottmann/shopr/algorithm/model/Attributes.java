
package com.uwetrottmann.shopr.algorithm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of possible attributes of an item.
 */
public class Attributes {

    public interface Attribute {
        public double[] getValueWeights();
    }

    public ClothingType type;

    public Color color;

    public List<double[]> getAllValueWeights() {
        List<double[]> weights = new ArrayList<double[]>();

        weights.add(type.getValueWeights());
        weights.add(color.getValueWeights());

        return weights;
    }

}
