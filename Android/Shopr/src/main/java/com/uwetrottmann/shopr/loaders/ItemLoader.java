
package com.uwetrottmann.shopr.loaders;

import android.content.Context;
import android.location.Location;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns a list of items based on the current user model.
 */
public class ItemLoader extends GenericSimpleLoader<List<Item>> {

    private Location mLocation;

    public ItemLoader(Context context, Location location) {
        super(context);
        mLocation = location;
    }

    @Override
    public List<Item> loadInBackground() {
        if (mLocation == null) {
            return new ArrayList<Item>();
        }

        AdaptiveSelection manager = AdaptiveSelection.get();
        List<Item> recommendations = manager.getRecommendations();

        return recommendations;
    }

}
