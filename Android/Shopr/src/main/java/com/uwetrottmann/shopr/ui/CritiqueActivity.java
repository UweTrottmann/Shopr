
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Critique;
import com.uwetrottmann.shopr.algorithm.Feedback;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.List;

public class CritiqueActivity extends Activity {

    private TextView mQuestion;
    private ListView mListView;
    private Button mButtonUpdate;

    private ItemFeatureAdapter mAdapter;

    private Item mItem;
    private boolean mIsPositiveCritique;
    private int mCritiquedAttributeId;

    public interface InitBundle {
        String ITEM_ID = "item_id";
        String IS_POSITIVE_CRITIQUE = "is_positive";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critique);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        int itemId = extras.getInt(InitBundle.ITEM_ID);
        List<Item> currentCaseBase = AdaptiveSelection.get().getCurrentRecommendations();
        for (Item item : currentCaseBase) {
            if (item.id() == itemId) {
                mItem = item;
                break;
            }
        }
        mIsPositiveCritique = extras.getBoolean(InitBundle.IS_POSITIVE_CRITIQUE);

        // Show the Up button in the action bar.
        setupActionBar();
        setupViews();
        setupAdapter();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setBackgroundDrawable(null);
    }

    private void setupViews() {
        mQuestion = (TextView) findViewById(R.id.textViewCritiqueQuestion);
        mQuestion.setText(getString(mIsPositiveCritique ? R.string.detail_like
                : R.string.detail_dislike, mItem.name()));

        mListView = (ListView) findViewById(R.id.listViewCritique);

        mButtonUpdate = (Button) findViewById(R.id.buttonRecommend);
        mButtonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateQuery();
            }

        });
    }

    private void setupAdapter() {
        mAdapter = new ItemFeatureAdapter(this, mItem);
        mListView.setAdapter(mAdapter);
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

    private void onUpdateQuery() {
        // Get selected attribute
        Feedback feedback = new Feedback();

        SparseBooleanArray checkedPositions = mAdapter.getCheckedPositions();
        for (int i = 0; i < 4; i++) {
            if (checkedPositions.get(i)) {
                mCritiquedAttributeId = i;
                break;
            }
        }

        feedback.attribute(mCritiquedAttributeId);
        feedback.isPositiveFeedback(mIsPositiveCritique);

        Critique critique = new Critique();
        critique.item(mItem);
        critique.feedback(feedback);

        // Submit to algorithm backend
        AdaptiveSelection.get().submitCritique(critique);
        setResult(RESULT_OK);
        finish();
    }

    public class ItemFeatureAdapter extends BaseAdapter {

        private static final int LAYOUT = R.layout.feature_row;
        private Item mItem;
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private SparseBooleanArray mCheckedPositions = new SparseBooleanArray();

        public ItemFeatureAdapter(Context context, Item item) {
            mContext = context;
            mItem = item;
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 3;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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

            holder.title.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCheckedPositions.put(position, isChecked);
                }
            });

            String title = "";
            String value = "";
            switch (position) {
                case 0:
                    title = mContext.getString(R.string.color);
                    value = item.attributes().color().currentValue().descriptor();
                    break;
                case 1:
                    title = mContext.getString(R.string.type);
                    value = item.attributes().type().currentValue().descriptor();
                    break;
                case 2:
                    title = mContext.getString(R.string.label);
                    value = item.attributes().label().currentValue().descriptor();
                    break;
            }

            holder.title.setText(title);
            holder.value.setText(value);

            return convertView;
        }

        public SparseBooleanArray getCheckedPositions() {
            return mCheckedPositions;
        }

    }

    static class ViewHolder {
        CheckBox title;
        TextView value;
    }

}
