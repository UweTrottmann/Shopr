
package com.uwetrottmann.shopr.ui;

import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.ui.MainActivity.ErrorDialogFragment;
import com.uwetrottmann.shopr.utils.ShopUtils;

import java.util.List;

public class ShopMapFragment extends SupportMapFragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String TAG = "Shops Map";

    /*
     * Define a request code to send to Google Play services. This code is
     * returned in Activity.onActivityResult.
     */
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

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

    @Override
    public void onConnected(Bundle dataBundle) {
        Toast.makeText(getActivity(), "Connected to location service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getActivity(), "Disconnected from location service, please reconnect.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the user with
             * the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Show a dialog returned by Google Play services for the connection error
     * code
     * 
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                getActivity(),
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getFragmentManager(), TAG);
        }
    }
}
