
package com.uwetrottmann.shopr.model;

import java.math.BigDecimal;

public class Item {

    private int id;
    private String name;
    private String picture;
    private BigDecimal price;
    private int shop_id;

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

    public String picture() {
        return picture;
    }

    public Item picture(String picture) {
        this.picture = picture;
        return this;
    }

    public int shopId() {
        return shop_id;
    }

    public Item shopId(int shop_id) {
        this.shop_id = shop_id;
        return this;
    }

    public BigDecimal price() {
        return price;
    }

    public Item price(BigDecimal price) {
        this.price = price;
        return this;
    }

}
