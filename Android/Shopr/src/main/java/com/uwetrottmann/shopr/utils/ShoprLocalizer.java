
package com.uwetrottmann.shopr.utils;

import android.content.Context;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.LocalizationModule;

public class ShoprLocalizer implements LocalizationModule {

    private Context mContext;

    public ShoprLocalizer(Context context) {
        mContext = context;
    }

    @Override
    public String getLocalizedValueDescriptor(String value) {
        return ValueConverter.getLocalizedStringForValue(mContext, value);
    }

    @Override
    public String getOnlyString() {
        return mContext.getString(R.string.only);
    }

    @Override
    public String getAvoidString() {
        return mContext.getString(R.string.avoid);
    }

    @Override
    public String getPreferablyString() {
        return mContext.getString(R.string.preferably);
    }

}
