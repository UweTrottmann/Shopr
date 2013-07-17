
package com.uwetrottmann.shopr;

import android.app.Application;
import android.preference.PreferenceManager;

import com.google.analytics.tracking.android.EasyTracker;

public class ShoprApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        EasyTracker.getInstance().setContext(this);
    }

}
