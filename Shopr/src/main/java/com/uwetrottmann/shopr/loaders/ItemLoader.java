
package com.uwetrottmann.shopr.loaders;

import android.content.Context;

import com.uwetrottmann.shopr.model.Item;
import com.uwetrottmann.shopr.utils.Lists;

import java.math.BigDecimal;
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

        for (int i = 0; i < 10; i++) {
            Item item = new Item().id(i).name("Sample Item " + i).picture("sample.jpg").shopId(42);
            double random = Math.random() * 200;
            item.price(new BigDecimal(random));
            items.add(item);
        }

        return items;
    }

}
