
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.Item;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CritiqueActivity extends Activity {

    private TextView mQuestion;
    private ListView mListView;
    private Item mItem;
    private ItemFeatureAdapter mAdapter;
    private boolean mIsPositiveCritique;

    public interface InitBundle {
        String ITEM_ID = "item_id";
        String IS_POSITIVE_CRITIQUE = "is_positive";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critique);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        mItem = new Item().id(extras.getInt(InitBundle.ITEM_ID)).name("Some Item").picture("")
                .price(new BigDecimal(25.99));
        mIsPositiveCritique = extras.getBoolean(InitBundle.IS_POSITIVE_CRITIQUE);

        // Show the Up button in the action bar.
        setupActionBar();
        setupViews();
        setupAdapter();
    }

    private void setupAdapter() {
        mAdapter = new ItemFeatureAdapter(this, mItem);
        mListView.setAdapter(mAdapter);
    }

    private void setupViews() {
        mQuestion = (TextView) findViewById(R.id.textViewCritiqueQuestion);
        mQuestion.setText(getString(mIsPositiveCritique ? R.string.detail_like
                : R.string.detail_dislike, mItem.name()));
        mListView = (ListView) findViewById(R.id.listViewCritique);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.critique, menu);
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

    public class ItemFeatureAdapter extends BaseAdapter {

        private static final int LAYOUT = R.layout.feature_row;
        private Item mItem;
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public ItemFeatureAdapter(Context context, Item item) {
            mContext = context;
            mItem = item;
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return mItem;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(LAYOUT, parent, false);

                holder = new ViewHolder();
                holder.title = (CheckBox) convertView.findViewById(R.id.checkBoxFeatureTitle);
                holder.value = (TextView) convertView.findViewById(R.id.textViewFeatureValue);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Item item = (Item) getItem(position);
            holder.title.setText("Price");
            holder.value.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
                    item.price().doubleValue()));

            return convertView;
        }

    }

    static class ViewHolder {
        CheckBox title;
        TextView value;
    }

}
