
package com.uwetrottmann.shopr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.ItemAdapter;
import com.uwetrottmann.shopr.adapters.ItemAdapter.OnItemCritiqueListener;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.loaders.ItemLoader;
import com.uwetrottmann.shopr.model.Item;

import java.util.List;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class ItemListFragment extends Fragment implements LoaderCallbacks<List<Item>>,
        OnItemCritiqueListener {

    // I = 9, T = 20
    private static final int LOADER_ID = 920;
    private static final int REQUEST_CODE = 12;
    private TextView mTextViewReason;
    private GridView mGridView;
    private ItemAdapter mAdapter;

    public static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        mTextViewReason = (TextView) v.findViewById(R.id.textViewItemListReason);
        mGridView = (GridView) v.findViewById(R.id.gridViewItemList);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ItemAdapter(getActivity(), this);

        mGridView.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int loaderId, Bundle args) {
        return new ItemLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        mAdapter.clear();
        mAdapter.addAll(data);
        onUpdateReason();
    }

    private void onUpdateReason() {
        Query currentQuery = AdaptiveSelection.get().getCurrentQuery();
        // Display current reason as explanatory text
        String reasonString = currentQuery.attributes().getReasonString();
        if (!TextUtils.isEmpty(reasonString)) {
            mTextViewReason.setText(reasonString);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onItemCritique(Item item, boolean isLike) {
        startActivityForResult(new Intent(getActivity(), CritiqueActivity.class).putExtra(
                CritiqueActivity.InitBundle.IS_POSITIVE_CRITIQUE, isLike)
                .putExtra(CritiqueActivity.InitBundle.ITEM_ID, item.id()), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }
}
