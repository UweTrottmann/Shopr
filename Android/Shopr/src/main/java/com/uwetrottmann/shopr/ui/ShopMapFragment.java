
package com.uwetrottmann.shopr.ui;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.utils.ShopUtils;

import java.util.List;

public class ShopMapFragment extends SupportMapFragment {

    private static final int RADIUS_METERS = 2000;
    private static final int ZOOM_LEVEL_INITIAL = 13;
    public static final String TAG = "Shops Map";

    public static ShopMapFragment newInstance() {
        return new ShopMapFragment();
    }

    private List<Shop> mShops;
    private Location mLocation;
    private boolean mIsInitialized;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsInitialized = false;

        // enable my location feature
        getMap().setMyLocationEnabled(true);

        mShops = ShopUtils.getShopsSamples();

    }

    @Override
    public void onStart() {
        super.onStart();

        onInitializeMap();

        onDisplayShops();
    }

    private void onInitializeMap() {
        if (!mIsInitialized) {
            mLocation = ((MainActivity) getActivity()).getLastLocation();
            if (mLocation == null) {
                return;
            }

            LatLng userPosition = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            // move camera to current position
            getMap().moveCamera(
                    CameraUpdateFactory.newLatLngZoom(userPosition, ZOOM_LEVEL_INITIAL));
            // draw a circle around it
            getMap().addCircle(new CircleOptions()
                    .center(userPosition)
                    .radius(RADIUS_METERS)
                    .strokeColor(getResources().getColor(R.color.lilac))
                    .strokeWidth(4)
                    .fillColor(getResources().getColor(R.color.lilac_transparent)));

            mIsInitialized = true;
        }
    }

    private void onDisplayShops() {
        for (Shop shop : mShops) {
            getMap().addMarker(new MarkerOptions().position(shop.location()).title(shop.name()));
        }
    }

}
