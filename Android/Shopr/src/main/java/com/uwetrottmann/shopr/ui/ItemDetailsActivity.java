
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.uwetrottmann.shopr.R;

public class ItemDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_details, menu);
        return true;
    }

}
