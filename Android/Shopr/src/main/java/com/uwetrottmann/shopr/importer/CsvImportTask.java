
package com.uwetrottmann.shopr.importer;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import au.com.bytecode.opencsv.CSVReader;

import com.uwetrottmann.androidutils.Lists;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.provider.ShoprContract.Items;
import com.uwetrottmann.shopr.provider.ShoprContract.Shops;
import com.uwetrottmann.shopr.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Imports items or shops from a CSV file into the database.
 */
public class CsvImportTask extends AsyncTask<Void, Integer, String> {

    public enum Type {
        IMPORT_SHOPS,
        IMPORT_ITEMS;
    }

    private static final String TAG = "Importer";

    private Context mContext;
    private Uri mUri;
    private Type mType;

    private InputStream mInputStream;

    public CsvImportTask(Context context, Uri uri, CsvImportTask.Type type) {
        mContext = context;
        mUri = uri;
        mType = type;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mContext, R.string.action_import, Toast.LENGTH_SHORT).show();

        // get input stream on main thread to avoid it being cleaned up
        Log.d(TAG, "Opening file.");
        try {
            mInputStream = mContext.getContentResolver().openInputStream(mUri);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not open file. " + e.getMessage());
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        if (mInputStream == null) {
            return "Input stream is null.";
        }

        CSVReader reader = new CSVReader(new InputStreamReader(mInputStream));

        // read shops line by line
        Log.d(TAG, "Reading values.");
        ArrayList<ContentValues> newValues = Lists.newArrayList();
        try {
            String[] firstLine = reader.readNext(); // skip first line
            if (firstLine == null) {
                return "No data.";
            }
            if ((mType == Type.IMPORT_ITEMS && firstLine.length != 9) ||
                    mType == Type.IMPORT_SHOPS && firstLine.length != 9) {
                Log.d(TAG, "Invalid column count.");
                return "Invalid column count.";
            }

            Log.d(TAG, "Importing the following CSV schema: " + Arrays.toString(firstLine));

            String[] line;
            Random random = new Random(123456); // seed to get fixed
                                                // distribution
            while ((line = reader.readNext()) != null) {
                ContentValues values = new ContentValues();

                switch (mType) {
                    case IMPORT_SHOPS:
                        // add values for one shop
                        values.put(Shops._ID, line[0]);
                        values.put(Shops.NAME, line[1]);
                        values.put(Shops.OPENING_HOURS, line[2]);
                        values.put(Shops.LAT, line[5]);
                        values.put(Shops.LONG, line[6]);
                        break;
                    case IMPORT_ITEMS:
                        // add values for one item
                        values.put(Items._ID, line[0]);
                        values.put(Shops.REF_SHOP_ID, random.nextInt(129));
                        values.put(Items.CLOTHING_TYPE, line[1]);
                        values.put(Items.SEX, line[2]);
                        values.put(Items.COLOR, line[3]);
                        values.put(Items.BRAND, line[4]);
                        values.put(Items.PRICE, line[5]);
                        // extract first image
                        String imageUrl = line[6];
                        imageUrl = Utils.extractFirstUrl(imageUrl);
                        values.put(Items.IMAGE_URL, imageUrl);
                        break;
                }

                newValues.add(values);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not read file. " + e.getMessage());
            return "Could not read file. " + e.getMessage();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close file. " + e.getMessage());
            }
        }

        Uri uri;
        switch (mType) {
            case IMPORT_SHOPS:
                uri = Shops.CONTENT_URI;
                break;
            case IMPORT_ITEMS:
                uri = Items.CONTENT_URI;
                break;
            default:
                return "Invalid import type.";
        }

        // clear existing table
        Log.d(TAG, "Clearing existing data.");
        mContext.getContentResolver().delete(uri, null, null);

        // insert into database in one transaction
        Log.d(TAG, "Inserting new data...");
        ContentValues[] valuesArray = new ContentValues[newValues.size()];
        valuesArray = newValues.toArray(valuesArray);
        mContext.getContentResolver().bulkInsert(uri, valuesArray);
        Log.d(TAG, "Inserting new data...DONE");

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(
                mContext, result == null ?
                        mContext.getString(R.string.import_successful)
                        : mContext.getString(R.string.import_failed) + " " + result,
                Toast.LENGTH_SHORT).show();
    }

}
