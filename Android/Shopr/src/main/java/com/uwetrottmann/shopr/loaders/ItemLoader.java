
package com.uwetrottmann.shopr.loaders;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.settings.AppSettings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns a list of items based on the current user model.
 */
public class ItemLoader extends GenericSimpleLoader<List<Item>> {

    private static final String TAG = "ItemLoader";
    private Location mLocation;
    private boolean mIsInit;

    public ItemLoader(Context context, Location location, boolean isInit) {
        super(context);
        mLocation = location;
        mIsInit = isInit;
    }

    @Override
    public List<Item> loadInBackground() {
        if (mLocation == null) {
            return new ArrayList<Item>();
        }

        AdaptiveSelection manager = AdaptiveSelection.get();

        // get initial case base
        if (mIsInit) {
            Log.d(TAG, "Initializing case base.");
            List<Item> caseBase = getInitialCaseBase();
            manager.setInitialCaseBase(caseBase);

            int maxRecommendations = AppSettings.getMaxRecommendations(getContext());
            AdaptiveSelection.get().setMaxRecommendations(maxRecommendations);
        }

        Log.d(TAG, "Fetching recommendations.");
        List<Item> recommendations = manager.getRecommendations();

        return recommendations;
    }

    private List<Item> getInitialCaseBase() {
        // TODO Implement all attributes
        List<Item> caseBase = Lists.newArrayList();

        Cursor query = getContext().getContentResolver().query(Items.CONTENT_URI, new String[] {
                Items._ID, Items.CLOTHING_TYPE, Items.BRAND, Items.PRICE, Items.IMAGE_URL
        }, null, null, null);

        if (query != null) {
            while (query.moveToNext()) {
                Item item = new Item();

                item.id(query.getInt(0));
                item.image(query.getString(4));
                item.shopId(1);
                // name
                String type = query.getString(1);
                String brand = query.getString(2);
                item.name(type + " " + brand);
                // price
                BigDecimal price = new BigDecimal(query.getDouble(3));
                item.price(price);
                // critiquable attributes
                item.attributes(new Attributes()
                        .putAttribute(new Price(price)));

                caseBase.add(item);
            }

            query.close();
        }

        return caseBase;
    }

}
