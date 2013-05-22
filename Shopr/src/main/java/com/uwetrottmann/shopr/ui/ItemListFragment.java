
package com.uwetrottmann.shopr.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwetrottmann.shopr.R;

/**
 * Shows a list of clothing items the user can critique by tapping an up or down
 * vote button.
 */
public class ItemListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        return v;
    }
}
