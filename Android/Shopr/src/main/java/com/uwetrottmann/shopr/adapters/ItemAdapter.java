
package com.uwetrottmann.shopr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.algorithm.AdaptiveSelection;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {

    private static final int LAYOUT = R.layout.item_layout;

    private LayoutInflater mInflater;

    private OnItemCritiqueListener mCritiqueListener;

    private OnItemDisplayListener mItemListener;

    public interface OnItemCritiqueListener {
        public void onItemCritique(Item item, boolean isLike);
    }

    public interface OnItemDisplayListener {
        public void onItemDisplay(Item item);
    }

    public ItemAdapter(Context context, OnItemCritiqueListener critiqueListener,
            OnItemDisplayListener itemListener) {
        super(context, LAYOUT);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCritiqueListener = critiqueListener;
        mItemListener = itemListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT, null);

            holder = new ViewHolder();
            holder.picture = (ImageView) convertView.findViewById(R.id.imageViewItemPicture);
            holder.name = (TextView) convertView.findViewById(R.id.textViewItemName);
            holder.label = (TextView) convertView.findViewById(R.id.textViewItemLabel);
            holder.price = (TextView) convertView.findViewById(R.id.textViewItemPrice);
            holder.buttonLike = (ImageButton) convertView.findViewById(R.id.imageButtonItemLike);
            holder.buttonDislike = (ImageButton) convertView
                    .findViewById(R.id.imageButtonItemDislike);
            holder.lastCritiqueTag = convertView.findViewById(R.id.textViewItemLastCritiqueLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Item item = getItem(position);
        holder.name.setText(item.name());
        holder.label.setText(item.attributes().getAttributeById(Color.ID).currentValue()
                .descriptor());
        holder.price.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
                item.price().doubleValue()));
        holder.buttonLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCritiqueListener != null) {
                    mCritiqueListener.onItemCritique(item, true);
                }
            }
        });
        holder.buttonDislike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCritiqueListener != null) {
                    mCritiqueListener.onItemCritique(item, false);
                }
            }
        });

        // last critique tag
        int lastCritiquedId = AdaptiveSelection.get().getLastCritiquedItem() != null ?
                AdaptiveSelection.get().getLastCritiquedItem().id()
                : -1;
        holder.lastCritiqueTag.setVisibility(item.id() == lastCritiquedId ? View.VISIBLE
                : View.GONE);

        holder.picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.onItemDisplay(item);
                }
            }
        });
        // load picture
        Picasso.with(getContext())
                .load(item.image())
                .placeholder(null)
                .error(R.drawable.ic_action_tshirt)
                .resizeDimen(R.dimen.default_image_size, R.dimen.default_image_size)
                .centerCrop()
                .into(holder.picture);

        return convertView;
    }

    static class ViewHolder {
        ImageView picture;
        TextView name;
        TextView label;
        TextView price;
        ImageButton buttonLike;
        ImageButton buttonDislike;
        View lastCritiqueTag;
    }

}
