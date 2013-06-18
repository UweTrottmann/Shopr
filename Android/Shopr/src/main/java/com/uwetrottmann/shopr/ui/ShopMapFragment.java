
package com.uwetrottmann.shopr.ui;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.ui.ItemListFragment.ShopUpdateEvent;
import com.uwetrottmann.shopr.ui.MainActivity.LocationUpdateEvent;
import com.uwetrottmann.shopr.utils.ShopUtils;

import de.greenrobot.event.EventBus;

import java.util.List;
import java.util.Map;

public class ShopMapFragment extends SupportMapFragment {

    private static final int RADIUS_METERS = 2000;
    private static final int ZOOM_LEVEL_INITIAL = 14;
    public static final String TAG = "Shops Map";

    public static ShopMapFragment newInstance() {
        return new ShopMapFragment();
    }

    private List<Marker> mShopMarkers;
    private Location mLocation;
    private boolean mIsInitialized;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsInitialized = false;

        // enable my location feature
        getMap().setMyLocationEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault()
                .registerSticky(this, LocationUpdateEvent.class, ShopUpdateEvent.class);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void onInitializeMap() {
        if (!mIsInitialized) {
            Log.d(TAG, "Initializing map.");

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

    private void onUpdateShops(Map<Integer, Integer> shopItemMap) {
        // remove existing markers
        if (mShopMarkers != null) {
            for (Marker marker : mShopMarkers) {
                marker.remove();
            }
        }
    
        // TODO replace with actual shop data
        List<Shop> shopsSamples = ShopUtils.getShopsSamples();
    
        List<Marker> shopMarkersNew = Lists.newArrayList();
    
        for (Shop shop : shopsSamples) {
            // determine color and recom. items in this shop
            float color;
            int itemCount;
            if (shopItemMap.containsKey(shop.id())) {
                itemCount = shopItemMap.get(shop.id());
                color = BitmapDescriptorFactory.HUE_VIOLET;
            } else {
                itemCount = 0;
                color = BitmapDescriptorFactory.HUE_AZURE;
            }
    
            // place marker
            Marker marker = getMap().addMarker(
                    new MarkerOptions()
                            .position(shop.location())
                            .title(shop.name())
                            .snippet(getString(R.string.has_x_recommendations, itemCount))
                            .icon(BitmapDescriptorFactory.defaultMarker(color)));
            shopMarkersNew.add(marker);
        }
    
        mShopMarkers = shopMarkersNew;
    }

    public void onEvent(LocationUpdateEvent event) {
        onInitializeMap();
    }

    public void onEvent(ShopUpdateEvent event) {
        onUpdateShops(event.shopMap);
    }

}
