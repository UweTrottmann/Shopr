
package com.uwetrottmann.shopr.ui;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.utils.ShopUtils;

import java.util.List;

public class ShopMapFragment extends SupportMapFragment {

    public static ShopMapFragment newInstance() {
        return new ShopMapFragment();
    }

    private List<Shop> mShops;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // enable my location feature
        getMap().setMyLocationEnabled(true);

        mShops = ShopUtils.getShopsSamples();

        onDisplayShops();

    }

    private void onDisplayShops() {
        for (Shop shop : mShops) {
            getMap().addMarker(new MarkerOptions().position(shop.location()).title(shop.name()));
        }
    }
}
