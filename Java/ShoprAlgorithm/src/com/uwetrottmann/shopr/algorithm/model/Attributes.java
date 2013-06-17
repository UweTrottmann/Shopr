
package com.uwetrottmann.shopr.algorithm.model;

/**
 * Holds a list of possible attributes of an item.
 */
public class Attributes {

    public interface Attribute {
        public String id();

        public AttributeValue currentValue();

        public double[] getValueWeights();

        public String getValueWeightsString();

        public String getReasonString();
    }

    public interface AttributeValue {

        /**
         * Returns the index of this value in the weights vector of the
         * attribute.
         */
        public int index();

        /**
         * Returns a {@link String} representation suitable for end-user
         * representation.
         */
        public String descriptor();
    }

    private ClothingType type;

    private Color color;

    private Label label;

    public Attribute[] getAllAttributes() {
        Attribute[] attrs = new Attribute[3];

        attrs[0] = type();
        attrs[1] = color();
        attrs[2] = label();

        return attrs;
    }

    public String getAllAttributesString() {
        StringBuilder attrsStr = new StringBuilder("");

        Attribute[] allAttributes = getAllAttributes();
        for (int i = 0; i < allAttributes.length; i++) {
            if (allAttributes[i] != null) {
                attrsStr.append(allAttributes[i].getValueWeightsString());
            } else {
                attrsStr.append("[not set]");
            }
            if (i != allAttributes.length - 1) {
                attrsStr.append(" ");
            }
        }

        return attrsStr.toString();
    }

    /**
     * Returns a string describing this query for an end-user, e.g.
     * "no Red, mainly Blue, no Armani".
     */
    public String getReasonString() {
        StringBuilder reason = new StringBuilder();

        Attribute[] allAttributes = getAllAttributes();
        for (int i = 0; i < allAttributes.length; i++) {
            if (allAttributes[i] == null) {
                continue;
            }

            Attribute attribute = allAttributes[i];
            if (reason.length() != 0) {
                reason.append(", ");
            }
            reason.append(attribute.getReasonString());
        }

        return reason.toString();
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

    public Label label() {
        return label;
    }

    public Attributes label(Label label) {
        this.label = label;
        return this;
    }

}
