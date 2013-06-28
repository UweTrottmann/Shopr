
package com.uwetrottmann.shopr.settings;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Quick access to some app settings.
 */
public class AppSettings {

    private static final String KEY_MAX_RECOMMENDATIONS = "com.uwetrottmann.shopr.maxrecommendations";

    public static int getMaxRecommendations(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                KEY_MAX_RECOMMENDATIONS, 9);
    }
}
