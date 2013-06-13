
package com.uwetrottmann.shopr;

import android.app.Application;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Utils;

public class ShoprApp extends Application {

    private static final int MAX_RECOMMENDATIONS = 8;

    @Override
    public void onCreate() {
        super.onCreate();

        AdaptiveSelection.get().setInitialCaseBase(Utils.getLimitedCaseBase());
        AdaptiveSelection.get().setMaxRecommendations(MAX_RECOMMENDATIONS);
    }

}
