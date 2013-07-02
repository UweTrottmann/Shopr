
package com.uwetrottmann.shopr.loaders;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.model.Shop;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;

import java.util.List;

public class ShopLoader extends GenericSimpleLoader<List<Shop>> {

    public ShopLoader(Context context) {
        super(context);
    }

    @Override
    public List<Shop> loadInBackground() {
        final Cursor query = getContext().getContentResolver().query(Shops.CONTENT_URI,
                new String[] {
                        Shops._ID, Shops.NAME, Shops.LAT, Shops.LONG
                }, null, null,
                null);

        List<Shop> shops = Lists.newArrayList();

        if (query != null) {
            while (query.moveToNext()) {
                Shop shop = new Shop();
                shop.id(query.getInt(0));
                shop.name(query.getString(1));
                shop.location(new LatLng(query.getDouble(2), query.getDouble(3)));
                shops.add(shop);
            }

            query.close();
        }

        return shops;
    }

}
