
package com.uwetrottmann.shopr.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.adapters.ItemAdapter;
import com.uwetrottmann.shopr.loaders.ItemLoader;
import com.uwetrottmann.shopr.model.Item;

import java.util.List;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class ItemListFragment extends Fragment implements LoaderCallbacks<List<Item>> {

    // I = 9, T = 20
    private static final int LOADER_ID = 920;
    private GridView mGridView;
    private ItemAdapter mAdapter;

    public static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridViewItemList);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ItemAdapter(getActivity());

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
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        mAdapter.clear();
    }
}
