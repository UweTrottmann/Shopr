
package com.uwetrottmann.shopr.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShoprContract {

    public static final String CONTENT_AUTHORITY = "com.uwetrottmann.shopr.provider";

    interface ItemsColumns {

    }

    interface ShopsColumns {
        /** NOT in this table! Used to reference ID from other tables. */
        String REF_SHOP_ID = "shop_id";
    }

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"
            + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    public static final String PATH_SHOPS = "shops";

    /**
     * Represents clothing items.
     */
    public static class Items implements ItemsColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ITEMS).build();

        /** Use if multiple items get returned */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shopr.item";

        /** Use if a single item is returned */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shopr.item";

        public static Uri buildItemUri(int itemId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(itemId))
                    .build();
        }
    }

    /**
     * Represents shops where clothing items are available for purchase.
     */
    public static class Shops implements ShopsColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SHOPS).build();

        /** Use if multiple items get returned */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shopr.shop";

        /** Use if a single item is returned */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shopr.shop";

        public static Uri buildShopUri(int shopId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(shopId))
                    .build();
        }
    }
}
