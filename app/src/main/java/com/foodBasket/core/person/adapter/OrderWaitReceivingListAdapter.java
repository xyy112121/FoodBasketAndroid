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
 * 待收货列表
 */

public class OrderWaitReceivingListAdapter extends BaseAdapter {
    private Context mContext;

    public OrderWaitReceivingListAdapter(Context mContext) {
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
        convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_order_wait_receiving_list_item, null);
        return convertView;
    }
}
