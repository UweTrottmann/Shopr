
package com.uwetrottmann.shopr.importer;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import au.com.bytecode.opencsv.CSVReader;

import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvImporter {

    private static final String TAG = "Importer";

    public static void importShopsCsvToDatabase(Context context, Uri uri) {
        // get input stream
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not open file. " + e.getMessage());
            return;
        }

        CSVReader reader = new CSVReader(new InputStreamReader(in));

        // read shops line by line
        ArrayList<ContentValues> newValues = Lists.newArrayList();
        try {
            reader.readNext(); // skip first line

            String[] line;
            while ((line = reader.readNext()) != null) {
                // add values for one shop
                ContentValues values = new ContentValues();
                values.put(Shops.NAME, line[0]);
                values.put(Shops.OPENING_HOURS, line[1]);
                values.put(Shops.LAT, line[4]);
                values.put(Shops.LONG, line[5]);
                newValues.add(values);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not read file. " + e.getMessage());
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close file. " + e.getMessage());
            }
        }

        // clear existing table
        context.getContentResolver().delete(Shops.CONTENT_URI, null, null);

        // insert into database in one transaction
        ContentValues[] valuesArray = new ContentValues[newValues.size()];
        valuesArray = newValues.toArray(valuesArray);
        context.getContentResolver().bulkInsert(Shops.CONTENT_URI, valuesArray);
    }

}
