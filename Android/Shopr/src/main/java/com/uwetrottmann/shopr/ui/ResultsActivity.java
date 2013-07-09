
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;

/**
 * Displays the given stats and allows to start a new task.
 */
public class ResultsActivity extends Activity {

    public interface InitBundle {
        String STATS_ID = "stats_id";
    }

    private int mStatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // extract stat id
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        mStatId = extras.getInt(InitBundle.STATS_ID);

        setupViews();
    }

    private void setupViews() {
        TextView textViewUserName = (TextView) findViewById(R.id.textViewResultsUsername);
        TextView textViewTaskType = (TextView) findViewById(R.id.textViewResultsTaskType);
        TextView textViewDuration = (TextView) findViewById(R.id.textViewResultsDuration);
        TextView textViewCycles = (TextView) findViewById(R.id.textViewResultsCycles);

        final Cursor query = getContentResolver().query(Stats.buildStatUri(mStatId), new String[] {
                Stats._ID, Stats.USERNAME, Stats.TASK_TYPE, Stats.DURATION, Stats.CYCLE_COUNT
        }, null, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                textViewUserName.setText(query.getString(1));
                textViewTaskType.setText(query.getString(2));
                textViewDuration.setText(query.getString(3));
                textViewCycles.setText(query.getString(4));
            }
            query.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.results, menu);
        return true;
    }

}
