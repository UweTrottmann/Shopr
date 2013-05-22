
package com.uwetrottmann.shopr.ui;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShopMapFragment extends SupportMapFragment {

    public static ShopMapFragment newInstance() {
        return new ShopMapFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // enable my location feature
        getMap().setMyLocationEnabled(true);
        getMap().setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                // Add marker at tap position
                getMap().addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));
            }
        });

    }
}
