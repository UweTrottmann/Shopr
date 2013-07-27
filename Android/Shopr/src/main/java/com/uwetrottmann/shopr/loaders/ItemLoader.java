
package com.uwetrottmann.shopr.loaders;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Price;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.utils.ShoprLocalizer;
import com.uwetrottmann.shopr.utils.ValueConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns a list of items based on the current user model.
 */
public class ItemLoader extends GenericSimpleLoader<List<Item>> {

    private static final String TAG = "ItemLoader";
    private LatLng mLocation;
    private boolean mIsInit;

    public ItemLoader(Context context, LatLng location, boolean isInit) {
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
            manager.setLocalizationModule(new ShoprLocalizer(getContext()));

            Log.d(TAG, "Initializing case base.");
            List<Item> caseBase = getInitialCaseBase();
            manager.setInitialCaseBase(caseBase, AppSettings.isUsingDiversity(getContext()));

            int maxRecommendations = AppSettings.getMaxRecommendations(getContext());
            AdaptiveSelection.get().setMaxRecommendations(maxRecommendations);
        }

        Log.d(TAG, "Fetching recommendations.");
        List<Item> recommendations = manager.getRecommendations();

        return recommendations;
    }

    private List<Item> getInitialCaseBase() {
        List<Item> caseBase = Lists.newArrayList();

        Cursor query = getContext().getContentResolver().query(
                Items.CONTENT_URI,
                new String[] {
                        Items._ID, Items.CLOTHING_TYPE, Items.BRAND, Items.PRICE, Items.IMAGE_URL,
                        Items.COLOR, Items.SEX, Shops.REF_SHOP_ID
                }, null, null, null);

        if (query != null) {
            while (query.moveToNext()) {
                Item item = new Item();

                item.id(query.getInt(0));
                item.image(query.getString(4));
                item.shopId(query.getInt(7));
                // name
                ClothingType type = new ClothingType(query.getString(1));
                String brand = query.getString(2);
                item.name(ValueConverter.getLocalizedStringForValue(getContext(), type
                        .currentValue().descriptor())
                        + " " + brand);
                // price
                BigDecimal price = new BigDecimal(query.getDouble(3));
                item.price(price);
                // critiquable attributes
                item.attributes(new Attributes()
                        .putAttribute(type)
                        .putAttribute(new Color(query.getString(5)))
                        .putAttribute(new Price(price))
                        .putAttribute(new Sex(query.getString(6))));

                caseBase.add(item);
            }

            query.close();
        }

        return caseBase;
    }

}
