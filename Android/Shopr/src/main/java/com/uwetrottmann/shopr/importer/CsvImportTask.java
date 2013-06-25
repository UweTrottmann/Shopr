
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Imports items or shops from a CSV file into the database.
 */
public class CsvImportTask extends AsyncTask<Void, Integer, Integer> {

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
        // get input stream on main thread to avoid it being cleaned up
        Log.d(TAG, "Opening file.");
        try {
            mInputStream = mContext.getContentResolver().openInputStream(mUri);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not open file. " + e.getMessage());
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {
        if (mInputStream == null) {
            return -1;
        }

        CSVReader reader = new CSVReader(new InputStreamReader(mInputStream));

        // read shops line by line
        Log.d(TAG, "Reading values.");
        ArrayList<ContentValues> newValues = Lists.newArrayList();
        try {
            reader.readNext(); // skip first line

            String[] line;
            while ((line = reader.readNext()) != null) {
                ContentValues values = new ContentValues();

                switch (mType) {
                    case IMPORT_SHOPS:
                        // add values for one shop
                        values.put(Shops.NAME, line[0]);
                        values.put(Shops.OPENING_HOURS, line[1]);
                        values.put(Shops.LAT, line[4]);
                        values.put(Shops.LONG, line[5]);
                        break;
                    case IMPORT_ITEMS:
                        // add values for one item
                        break;
                }

                newValues.add(values);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not read file. " + e.getMessage());
            return -1;
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
                return -1;
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

        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        Toast.makeText(mContext, result == 0 ? R.string.import_successful : R.string.import_failed,
                Toast.LENGTH_SHORT).show();
    }

}
