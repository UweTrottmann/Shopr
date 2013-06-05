
package com.uwetrottmann.shopr.algorithm.model;

/**
 * Holds a list of possible attributes of an item.
 */
public class Attributes {

    public interface Attribute {
        public double[] getValueWeights();
    }

    public ClothingType type;

    public Color color;

    public Attribute[] getAllAttributes() {
        Attribute[] attrs = new Attribute[2];

        attrs[0] = type;
        attrs[1] = color;

        return attrs;
    }

}
