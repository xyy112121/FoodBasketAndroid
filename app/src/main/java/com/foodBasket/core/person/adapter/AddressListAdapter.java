package com.foodBasket.core.person.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.foodBasket.R;
import com.foodBasket.core.person.model.AddressResModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地址
 */

public class AddressListAdapter extends ArrayAdapter<AddressResModel> {
    private int mResource;
    private Context mContext;

    public AddressListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mResource = resource;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_shipping_address_list_item, null);
        ViewHolder holder = new ViewHolder(convertView);
        holder.mPersonTv.setText(getItem(position).userName);
        holder.mMobileTv.setText(getItem(position).mobile);
        String addr = getItem(position).province + getItem(position).city + getItem(position).county + getItem(position).address;
        holder.mAddressTv.setText(addr);
        if (getItem(position).isDefault == true) {
            holder.mck.setChecked(true);
        } else {
            holder.mck.setChecked(false);
        }
//        convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_shipping_address_list_item, null);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressResModel model = getItem(position);
                Intent intent = new Intent();
                String json = JSON.toJSONString(model);
                intent.putExtra("addr", json);
                Activity activity = (Activity) mContext;
                activity.setResult(activity.RESULT_OK, intent);
                activity.finish();
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.addr_item_contactperson)
        TextView mPersonTv;
        @BindView(R.id.addr_item_contactmobile)
        TextView mMobileTv;
        @BindView(R.id.addr_item_address)
        TextView mAddressTv;
        @BindView(R.id.check_box)
        CheckBox mck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
