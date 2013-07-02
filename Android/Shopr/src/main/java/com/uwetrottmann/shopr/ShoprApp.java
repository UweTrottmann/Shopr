
package com.uwetrottmann.shopr;

import android.app.Application;
import android.preference.PreferenceManager;

public class ShoprApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
    }

}
