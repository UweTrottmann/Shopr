
package com.uwetrottmann.shopr.algorithm.model;

/**
 * Represents a item (e.g. clothing item), or one case in the case-base.
 */
public class Item {

    private Attributes attrs;

    private double querySimilarity;

    private double quality;

    public Attributes attributes() {
        return attrs;
    }

    public Item attributes(Attributes attrs) {
        this.attrs = attrs;
        return this;
    }

    public double querySimilarity() {
        return querySimilarity;
    }

    public Item querySimilarity(double querySimilarity) {
        this.querySimilarity = querySimilarity;
        return this;
    }

    public double quality() {
        return quality;
    }

    public Item quality(double quality) {
        this.quality = quality;
        return this;
    }

}
