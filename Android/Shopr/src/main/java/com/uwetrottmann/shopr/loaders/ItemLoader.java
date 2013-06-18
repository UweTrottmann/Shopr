
package com.uwetrottmann.shopr.loaders;

import android.content.Context;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.List;

/**
 * Returns a list of items based on the current user model.
 */
public class ItemLoader extends GenericSimpleLoader<List<Item>> {

    public ItemLoader(Context context) {
        super(context);
    }

    @Override
    public List<Item> loadInBackground() {

        AdaptiveSelection manager = AdaptiveSelection.get();
        List<Item> recommendations = manager.getRecommendations();

        return recommendations;
    }

}
