
package com.uwetrottmann.shopr.utils;

import android.content.Context;

import com.uwetrottmann.shopr.R;

import java.util.HashMap;

public class ValueConverter {

    static HashMap<String, Integer> sDescriptorMap;

    static {
        sDescriptorMap = Maps.newHashMap();
        sDescriptorMap.put("Swim suit", R.string.swim_suit);
        sDescriptorMap.put("Trunks", R.string.trunks);
        sDescriptorMap.put("Blouse", R.string.blouse);
        sDescriptorMap.put("Shirt", R.string.shirt);
        sDescriptorMap.put("Trousers", R.string.trousers);
        sDescriptorMap.put("Jeans", R.string.jeans);
        sDescriptorMap.put("Dress", R.string.dress);
        sDescriptorMap.put("Poloshirt", R.string.poloshirt);
        sDescriptorMap.put("Sweater", R.string.sweater);
        sDescriptorMap.put("Skirt", R.string.skirt);
        sDescriptorMap.put("Shorts", R.string.shorts);
        sDescriptorMap.put("Cardigan", R.string.cardigan);
        sDescriptorMap.put("Top/T-Shirt", R.string.top_t_shirt);
    }

    public static String getLocalizedStringForValue(Context context, String descriptor) {
        Integer integer = sDescriptorMap.get(descriptor);
        if (integer != null) {
            return context.getString(integer);
        } else {
            return "";
        }
    }
}
