
package com.uwetrottmann.shopr.model;

import com.google.android.gms.maps.model.LatLng;

public class Shop {

    private int id;
    private String name;
    private LatLng location;

    public int id() {
        return id;
    }

    public Shop id(int id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Shop name(String name) {
        this.name = name;
        return this;
    }

    public LatLng location() {
        return location;
    }

    public Shop location(LatLng location) {
        this.location = location;
        return this;
    }

}
