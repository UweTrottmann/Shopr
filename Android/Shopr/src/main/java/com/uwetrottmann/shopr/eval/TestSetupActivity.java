
package com.uwetrottmann.shopr.eval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.settings.AppSettings;
import com.uwetrottmann.shopr.ui.MainActivity;
import com.uwetrottmann.shopr.ui.SettingsActivity;

import java.util.Random;

public class TestSetupActivity extends Activity {

    private static final String TAG = "Test Setup";

    private EditText mNameEditText;
    private CheckBox mDiversityCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_setup);

        setupViews();
    }

    private void setupViews() {
        View startButton = findViewById(R.id.buttonTestSetupStart);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onStartTest();
            }
        });

        mNameEditText = (EditText) findViewById(R.id.editTextTestSetupName);
        String prevUserName = Statistics.get().getUserName();
        mNameEditText.setText(TextUtils.isEmpty(prevUserName) ? "thisis"
                + new Random().nextInt(999999) : prevUserName);

        mDiversityCheckBox = (CheckBox) findViewById(R.id.checkBoxTestSetupDiversity);
        mDiversityCheckBox.setChecked(AppSettings.isUsingDiversity(this));
    }

    protected void onStartTest() {
        if (TextUtils.isEmpty(mNameEditText.getText())) {
            Toast.makeText(this, "Please supply a name or pseudonym.", Toast.LENGTH_LONG).show();
            return;
        }

        // set diversity on or off
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(AppSettings.KEY_USING_DIVERSITY, mDiversityCheckBox.isChecked())
                .commit();
        Log.d(TAG, "Setting diversity to : " + (mDiversityCheckBox.isChecked() ? "ON" : "OFF"));

        // record name, time and type, start task
        Statistics.get().startTask(mNameEditText.getText().toString(),
                mDiversityCheckBox.isChecked());

        // start the task
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
