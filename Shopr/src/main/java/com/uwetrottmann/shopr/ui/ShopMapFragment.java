
package com.uwetrottmann.shopr.ui;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

public class ShopMapFragment extends SupportMapFragment {

    public static ShopMapFragment newInstance() {
        return new ShopMapFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // enable my location feature
        getMap().setMyLocationEnabled(true);
    }
}
