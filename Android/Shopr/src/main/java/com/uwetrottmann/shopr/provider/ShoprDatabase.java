
package com.uwetrottmann.shopr.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;

public class ShoprDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shopr.db";

    public static final int DBVER_INITIAL = 1;

    public static final int DBVER_ITEM_COLUMNS = 2;

    public static final int DBVER_STATS = 3;

    public static final int DATABASE_VERSION = DBVER_STATS;

    public interface Tables {
        String ITEMS = "items";

        String SHOPS = "shops";

        String STATS = "stats";
    }

    public interface References {
        String SHOP_ID = "REFERENCES " + Tables.SHOPS + "(" + Shops._ID + ")";
    }

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE "
            + Tables.ITEMS + " ("

            + Items._ID + " INTEGER PRIMARY KEY,"

            + Shops.REF_SHOP_ID + " INTEGER " + References.SHOP_ID + ","

            + Items.BRAND + " TEXT,"

            + Items.CLOTHING_TYPE + " TEXT,"

            + Items.COLOR + " TEXT,"

            + Items.PRICE + " REAL,"

            + Items.SEX + " TEXT,"

            + Items.IMAGE_URL + " TEXT"

            + ");";

    private static final String CREATE_SHOPS_TABLE = "CREATE TABLE "
            + Tables.SHOPS + " ("

            + Shops._ID + " INTEGER PRIMARY KEY,"

            + Shops.NAME + " TEXT NOT NULL,"

            + Shops.OPENING_HOURS + " TEXT,"

            + Shops.LAT + " REAL,"

            + Shops.LONG + " REAL"

            + ");";

    private static final String CREATE_STATS_TABLE = "CREATE TABLE "
            + Tables.STATS + " ("

            + Stats._ID + " INTEGER PRIMARY KEY,"

            + Stats.USERNAME + " TEXT,"

            + Stats.DURATION + " INTEGER,"

            + Stats.CYCLE_COUNT + " INTEGER,"

            + Stats.TASK_TYPE + " TEXT"

            + ");";

    private static final String TAG = "ShoprDatabase";

    public ShoprDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHOPS_TABLE);

        // items refs shop ids, so create shops table first
        db.execSQL(CREATE_ITEMS_TABLE);

        db.execSQL(CREATE_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // always start from scratch
        onResetDatabase(db);
    }

    /**
     * Drops all tables and creates an empty database.
     */
    private void onResetDatabase(SQLiteDatabase db) {
        Log.w(TAG, "Database has incompatible version, starting from scratch");

        db.execSQL("DROP TABLE IF EXISTS " + Tables.ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SHOPS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.STATS);

        onCreate(db);
    }

}
