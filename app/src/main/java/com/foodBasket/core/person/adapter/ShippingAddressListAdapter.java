package com.foodBasket.core.person.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.foodBasket.R;

/**
 * Created by programmer on 2017/12/28.
 */

public class ShippingAddressListAdapter extends BaseAdapter {
    private int mResource;
    private Context mContext;

    public ShippingAddressListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_shipping_address_list_item, null);
        return convertView;
    }
}
