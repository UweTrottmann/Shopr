
package com.uwetrottmann.shopr.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShoprContract {

    public static final String CONTENT_AUTHORITY = "com.uwetrottmann.shopr.provider";

    interface ItemsColumns {
        String CLOTHING_TYPE = "item_clothing_type";

        String BRAND = "item_brand";

        String SEX = "item_sex";

        String COLOR = "item_color";

        String PRICE = "item_price";

        String IMAGE_URL = "item_image";
    }

    interface ShopsColumns {
        /** NOT in this table! Used to reference ID from other tables. */
        String REF_SHOP_ID = "shop_id";

        String NAME = "shop_name";

        String LAT = "shop_lat";

        String LONG = "shop_long";

        String OPENING_HOURS = "shop_opening_hours";
    }

    interface StatsColumns {
        String USERNAME = "stats_user";

        String DURATION = "stats_duration";

        String CYCLE_COUNT = "stats_cycles";

        String TASK_TYPE = "stats_tasktype";
    }

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"
            + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    public static final String PATH_SHOPS = "shops";

    public static final String PATH_STATS = "stats";

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

        public static String getItemId(Uri uri) {
            return uri.getLastPathSegment();
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

        public static String getShopId(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /**
     * Represents statistics items from one user task.
     */
    public static class Stats implements StatsColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STATS).build();

        /** Use if multiple items get returned */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shopr.stats";

        /** Use if a single item is returned */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shopr.stats";

        public static Uri buildStatUri(int statId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(statId))
                    .build();
        }

        public static String getStatId(Uri uri) {
            return uri.getLastPathSegment();
        }
    }
}
