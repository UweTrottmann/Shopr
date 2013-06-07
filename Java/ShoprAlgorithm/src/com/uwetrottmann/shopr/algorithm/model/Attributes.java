
package com.uwetrottmann.shopr.algorithm.model;

/**
 * Holds a list of possible attributes of an item.
 */
public class Attributes {

    public interface Attribute {
        public double[] getValueWeights();
    }

    private ClothingType type;

    private Color color;

    public Attribute[] getAllAttributes() {
        Attribute[] attrs = new Attribute[2];

        attrs[0] = type();
        attrs[1] = color();

        return attrs;
    }

    public Color color() {
        return color;
    }

    public Attributes color(Color color) {
        this.color = color;
        return this;
    }

    public ClothingType type() {
        return type;
    }

    public Attributes type(ClothingType type) {
        this.type = type;
        return this;
    }

}
