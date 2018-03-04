package com.foodBasket.core.person.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foodBasket.R;
import com.foodBasket.core.person.model.MerchantRowModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 餐馆列表
 */

public class MerchantListAdapter extends ArrayAdapter<MerchantRowModel> {
    private Context mContext;

    public MerchantListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_merchant_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleTv.setText(getItem(position).name);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.merchant_item_title_tv)
        TextView mTitleTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
