
package com.uwetrottmann.shopr.importer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.uwetrottmann.shopr.R;

/**
 * Allows importing of a data set of items and shops from CSV files into the
 * database.
 */
public class ImporterActivity extends Activity {

    private static final int CODE_IMPORT_SHOPS = 100;
    private static final int CODE_IMPORT_ITEMS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer);

        setupActionBar();
        setupViews();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupViews() {
        View buttonImportShops = findViewById(R.id.buttonImporterShops);
        buttonImportShops.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisplayChooser(CODE_IMPORT_SHOPS);
            }
        });
        View buttonImportItems = findViewById(R.id.buttonImporterItems);
        buttonImportItems.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisplayChooser(CODE_IMPORT_ITEMS);
            }
        });
    }

    private void onDisplayChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/csv");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.import_a_csv_file)), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.import_no_file_manager,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_IMPORT_ITEMS || requestCode == CODE_IMPORT_SHOPS) {
                if (requestCode == CODE_IMPORT_SHOPS) {
                    onImportShops(data.getData());
                } else {
                    onImportItems(data.getData());
                }
            }
        }
    }

    protected void onImportShops(Uri uri) {
        CsvImporter.importShopsCsvToDatabase(this, uri);
    }

    protected void onImportItems(Uri uri) {
        CsvImporter.importItemsCsvToDatabase(this, uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.importer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
