
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
import android.widget.Toast;

import com.uwetrottmann.shopr.R;
import com.uwetrottmann.shopr.model.Item;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {

    private static final int LAYOUT = R.layout.item_layout;

    private LayoutInflater mInflater;

    public ItemAdapter(Context context) {
        super(context, LAYOUT);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT, null);

            holder = new ViewHolder();
            holder.picture = (ImageView) convertView.findViewById(R.id.imageViewItemPicture);
            holder.name = (TextView) convertView.findViewById(R.id.textViewItemName);
            holder.price = (TextView) convertView.findViewById(R.id.textViewItemPrice);
            holder.buttonLike = (ImageButton) convertView.findViewById(R.id.imageButtonItemLike);
            holder.buttonDislike = (ImageButton) convertView
                    .findViewById(R.id.imageButtonItemDislike);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);
        holder.name.setText(item.name());
        holder.price.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(
                item.price().doubleValue()));
        holder.buttonLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Liking this item", Toast.LENGTH_SHORT).show();
            }
        });
        holder.buttonDislike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Not liking this item", Toast.LENGTH_SHORT).show();
            }
        });

        holder.picture.setImageResource(R.drawable.armani);

        return convertView;
    }

    static class ViewHolder {
        ImageView picture;
        TextView name;
        TextView price;
        ImageButton buttonLike;
        ImageButton buttonDislike;
    }

}
