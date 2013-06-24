
package com.uwetrottmann.shopr.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.provider.ShoprDatabase.Tables;

public class ShoprProvider extends ContentProvider {

    private static UriMatcher sUriMatcher;

    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;

    private static final int SHOPS = 200;
    private static final int SHOP_ID = 201;

    private static final String TAG = "ShoprProvider";

    private static final boolean LOGV = false;

    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher(Context context) {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ShoprContract.CONTENT_AUTHORITY;

        // Items
        matcher.addURI(authority, ShoprContract.PATH_ITEMS, ITEMS);
        matcher.addURI(authority, ShoprContract.PATH_ITEMS + "/*", ITEM_ID);

        // Shops
        matcher.addURI(authority, ShoprContract.PATH_SHOPS, SHOPS);
        matcher.addURI(authority, ShoprContract.PATH_SHOPS + "/*", SHOP_ID);

        return matcher;
    }

    private ShoprDatabase mOpenHelper;

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        sUriMatcher = buildUriMatcher(context);
        mOpenHelper = new ShoprDatabase(context);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return Items.CONTENT_TYPE;
            case ITEM_ID:
                return Items.CONTENT_ITEM_TYPE;
            case SHOPS:
                return Shops.CONTENT_TYPE;
            case SHOP_ID:
                return Shops.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (LOGV)
            Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString()
                    + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS: {
                db.insertOrThrow(Tables.ITEMS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Items.buildItemUri(values.getAsInteger(Items._ID));
            }
            case SHOPS: {
                db.insertOrThrow(Tables.SHOPS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Shops.buildShopUri(values.getAsInteger(Shops._ID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
