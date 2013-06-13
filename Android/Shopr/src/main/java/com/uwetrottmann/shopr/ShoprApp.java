
package com.uwetrottmann.shopr;

import android.app.Application;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Utils;

public class ShoprApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AdaptiveSelection.getInstance().setInitialCaseBase(Utils.getLimitedCaseBase());
    }

}
