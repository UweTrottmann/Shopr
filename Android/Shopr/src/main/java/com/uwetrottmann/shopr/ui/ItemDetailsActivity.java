
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Sex;
import com.uwetrottmann.shopr.eval.ResultsActivity;
import com.uwetrottmann.shopr.eval.Statistics;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;
import com.uwetrottmann.shopr.utils.ValueConverter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemDetailsActivity extends Activity {

    public interface InitBundle {
        String ITEM_ID = "item_id";
    }

    private Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

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

        if (mItem == null) {
            finish();
            return;
        }

        setupViews();
    }

    private void setupViews() {
        ImageView image = (ImageView) findViewById(R.id.imageViewItemDetails);
        // load picture
        Picasso.with(this)
                .load(mItem.image())
                .placeholder(null)
                .error(R.drawable.ic_action_tshirt)
                .resizeDimen(R.dimen.default_image_width, R.dimen.default_image_height)
                .centerCrop()
                .into(image);

        findViewById(R.id.buttonItemDetailsFinish).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishTask();
            }
        });

        // title
        TextView itemTitle = (TextView) findViewById(R.id.textViewItemDetailsTitle);
        itemTitle.setText(getString(R.string.choice_confirmation, mItem.name()));

        // item attributes
        StringBuilder description = new StringBuilder();
        description
                .append(ValueConverter.getLocalizedStringForValue(this, mItem.attributes()
                        .getAttributeById(ClothingType.ID).currentValue().descriptor()))
                .append("\n")
                .append(ValueConverter.getLocalizedStringForValue(this, mItem.attributes()
                        .getAttributeById(Sex.ID).currentValue().descriptor()))
                .append("\n")
                .append(ValueConverter.getLocalizedStringForValue(this, mItem.attributes()
                        .getAttributeById(Color.ID).currentValue().descriptor()))
                .append("\n")
                .append(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
                        mItem.price().doubleValue()));
        TextView itemDescription = (TextView) findViewById(R.id.textViewItemDetailsAttributes);
        itemDescription.setText(description);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    protected void onFinishTask() {
        // finish task, store stats to database
        Uri statUri = Statistics.get().finishTask(this);
        if (statUri == null) {
            Toast.makeText(this, "Task was not started.", Toast.LENGTH_LONG).show();
            return;
        }

        // display results
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.InitBundle.STATS_ID,
                Integer.valueOf(Stats.getStatId(statUri)));
        intent.putExtra(ResultsActivity.InitBundle.ITEM_ID, mItem.id());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_details, menu);
        return true;
    }

}
