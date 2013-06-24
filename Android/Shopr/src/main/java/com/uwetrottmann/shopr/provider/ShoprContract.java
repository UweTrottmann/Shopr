package com.uwetrottmann.shopr.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShoprContract {

	interface ItemsColumns {

	}

	interface ShopsColumns {

	}

	private static final String CONTENT_AUTHORITY = "com.uwetrottmann.shopr.provider";

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

		public static Uri buildShopUri(int shopId) {
			return CONTENT_URI.buildUpon().appendPath(String.valueOf(shopId))
					.build();
		}
	}
}
