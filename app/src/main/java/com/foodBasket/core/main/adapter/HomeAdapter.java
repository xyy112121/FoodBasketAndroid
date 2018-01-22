package com.foodBasket.core.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodBasket.R;

/**
 * Created by programmer on 2018/1/22.
 */

public class HomeAdapter extends BaseRecyclerAdapter<String> {
    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item, parent, false);
//        LinearLayout parentLayout = layout.findViewById(R.id.home_item_parent_lv);
//        LinearLayout.LayoutParams params = parentLayout.getLayoutParams();
//        params.setMargins();
        return new MyHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, String data) {
        if (viewHolder instanceof MyHolder) {
            MyHolder holder = (MyHolder) viewHolder;
            holder.text.setText(data);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.parentLayout.getLayoutParams();
            if (RealPosition % 2 == 1) {  //是偶数
                params.setMargins(0, 0, 16, 16);
            } else {
                params.setMargins(16, 0, 16, 16);
            }
            if(RealPosition == 0){
                params.setMargins(16, 0, 16, 16);
            }
            holder.parentLayout.setLayoutParams(params);
        }
    }

    class MyHolder extends BaseRecyclerAdapter.Holder {
        TextView text;
        LinearLayout parentLayout;

        public MyHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.item_name_tv);
            parentLayout = itemView.findViewById(R.id.home_item_parent_lv);
        }
    }
}
