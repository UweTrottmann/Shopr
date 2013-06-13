
package com.uwetrottmann.shopr.loaders;

import android.content.Context;

import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.model.Item;
import com.uwetrottmann.shopr.utils.Lists;

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
        List<Item> items = Lists.newArrayList();

        AdaptiveSelection manager = AdaptiveSelection.get();
        List<com.uwetrottmann.shopr.algorithm.model.Item> recommendations = manager
                .getRecommendations();

        // Until we have real data transfer the model of item used by algorithm
        // to the app model
        int count = 0;
        for (com.uwetrottmann.shopr.algorithm.model.Item item : recommendations) {
            Attributes attrs = item.attributes();
            String label = attrs.label().currentValue().toString();
            String type = attrs.type().currentValue().toString();

            Item expandedItem = new Item().id(item.id());
            expandedItem.name(type + " " + label + count);
            expandedItem.color(attrs.color().currentValue().toString());
            expandedItem.label(label);
            expandedItem.type(type);
            expandedItem.price(item.price());

            items.add(expandedItem);
        }

        return items;
    }

}
