
package com.uwetrottmann.shopr.algorithm.model;

import java.math.BigDecimal;

/**
 * Represents a item (e.g. clothing item), or one case in the case-base.
 */
public class Item {

    private int id;

    private String name;

    private BigDecimal price;

    private String image_url;

    private int shop_id;

    private Attributes attrs;

    private double querySimilarity;

    private double quality;

    public int id() {
        return id;
    }

    public Item id(int id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal price() {
        return price;
    }

    public Item price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String image() {
        return image_url;
    }

    public Item image(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public int shopId() {
        return shop_id;
    }

    public Item shopId(int shop_id) {
        this.shop_id = shop_id;
        return this;
    }

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
