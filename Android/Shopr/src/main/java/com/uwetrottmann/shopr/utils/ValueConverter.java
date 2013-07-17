
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

        sDescriptorMap.put("Female", R.string.female);
        sDescriptorMap.put("Male", R.string.male);
        sDescriptorMap.put("Unisex", R.string.unisex);

        sDescriptorMap.put("Blue", R.string.blue);
        sDescriptorMap.put("Red", R.string.red);
        sDescriptorMap.put("Pink", R.string.pink);
        sDescriptorMap.put("Purple", R.string.purple);
        sDescriptorMap.put("Yellow", R.string.yellow);
        sDescriptorMap.put("Brown", R.string.brown);
        sDescriptorMap.put("Colored", R.string.colored);
        sDescriptorMap.put("Mixed", R.string.mixed);
        sDescriptorMap.put("Grey", R.string.grey);
        sDescriptorMap.put("Green", R.string.green);
        sDescriptorMap.put("Orange", R.string.orange);
        sDescriptorMap.put("Black", R.string.black);
        sDescriptorMap.put("Turquoise", R.string.turquoise);
        sDescriptorMap.put("White", R.string.white);
        sDescriptorMap.put("Beige", R.string.beige);

        sDescriptorMap.put("less than 25 €", R.string.less_than_25);
        sDescriptorMap.put("25 to 50 €", R.string._25_to_50);
        sDescriptorMap.put("50 to 75 €", R.string._50_to_75);
        sDescriptorMap.put("75 to 100 €", R.string._75_to_100);
        sDescriptorMap.put("100 to 150 €", R.string._100_to_150);
        sDescriptorMap.put("150 to 200 €", R.string._150_to_200);
        sDescriptorMap.put("200 € or more", R.string._200_or_more);
    }

    public static String getLocalizedStringForValue(Context context, String descriptor) {
        Integer integer = sDescriptorMap.get(descriptor);
        if (integer != null) {
            return context.getString(integer);
        } else {
            return descriptor;
        }
    }
}
