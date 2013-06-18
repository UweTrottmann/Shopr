
package com.uwetrottmann.shopr.utils;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.model.Shop;

import java.util.List;

public class ShopUtils {

    public static List<Shop> getShopsSamples() {
        List<Shop> shops = Lists.newArrayList();

        Shop shop = new Shop();
        shop.id(1).name("Sport u. Mode Soller").location(new LatLng(48.249246, 11.64988));
        shops.add(shop);

        return shops;
    }

}
