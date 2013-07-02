
package com.uwetrottmann.shopr.settings;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Quick access to some app settings.
 */
public class AppSettings {

    private static final String KEY_MAX_RECOMMENDATIONS = "com.uwetrottmann.shopr.maxrecommendations";
    private static final String KEY_FAKE_LOCATION = "com.uwetrottmann.shopr.fakelocation";

    public static int getMaxRecommendations(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                KEY_MAX_RECOMMENDATIONS, 9);
    }

    public static boolean isUsingFakeLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_FAKE_LOCATION,
                false);
    }
}
