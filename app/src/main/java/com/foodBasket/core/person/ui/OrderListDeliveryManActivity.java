package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.orderDeliveryManFragement.OrderListFragmentDeliveryMan;
import com.foodBasket.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 送货单（送货人看到了）
 */

public class OrderListDeliveryManActivity extends BaseActivity {
    @BindView(R.id.home_top_parent_ll)
    LinearLayout mTopParentLl;


    private List<FrameLayout> mListLayout = new ArrayList<>();
    private int mIndex = 0;
    private int mIndex2 = -1;//前一次点击的下标

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, OrderListDeliveryManActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_deliveryman);
        ButterKnife.bind(this);
        initTopbar("送货单");
        initTop();

    }

    private void initTop() {
        List<String> topList = new ArrayList<>();
        topList.add("全部");
        topList.add("未送货");
        topList.add("已送货");
        for (int i = 0; i < topList.size(); i++) {
            final int index = i;
            final FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_order_list_top_item, null);
            TextView t = layout.findViewById(R.id.order_top_parent_item_tv);
            t.setText(topList.get(i));
            if (i == 0) {
                t.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                layout.findViewById(R.id.order_top_parent_item_line).setVisibility(View.VISIBLE);
                swithFragment();
            }
            int width = DimenUtil.getScreenWidth();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex2 = mIndex;
                    mIndex = index;
                    if (mIndex2 != -1 && mIndex != mIndex2) {
                        ((TextView) view.findViewById(R.id.order_top_parent_item_tv)).setTextColor(
                                ContextCompat.getColor(mContext, R.color.black));
                        (view.findViewById(R.id.order_top_parent_item_line)).setVisibility(View.VISIBLE);
                        setViewColor();
                    }
                    swithFragment();
                }
            });
            mListLayout.add(layout);
            mTopParentLl.addView(layout);
        }

    }

    public void setViewColor() {
        FrameLayout layout = mListLayout.get(mIndex2);
        ((TextView) layout.findViewById(R.id.order_top_parent_item_tv)).setTextColor(
                ContextCompat.getColor(mContext, R.color.bar_grey));
        (layout.findViewById(R.id.order_top_parent_item_line)).setVisibility(View.GONE);
    }

    private void swithFragment() {
        OrderListFragmentDeliveryMan f = new OrderListFragmentDeliveryMan();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }
}