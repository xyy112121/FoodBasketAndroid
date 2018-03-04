package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.androidkun.xtablayout.XTabLayout;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单
 */

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    XTabLayout mTabLayout;

    public static void openActivity(Activity activity, String state) {
        Intent intent = new Intent(activity, OrderListActivity.class);
        intent.putExtra("state", state);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_deliveryman);
        ButterKnife.bind(this);
        initTopbar("我的订单");
        initTop();

    }


    private void initTop() {
        String state = getIntent().getStringExtra("state");
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("待收货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已收货"));

        String sta = "";
        if ("待收货".equals(state)) {
            mTabLayout.getTabAt(1).select();
            sta = "2";
        } else if ("已收货".equals(state)) {
            mTabLayout.getTabAt(2).select();
            sta = "3";
        } else {
            mTabLayout.getTabAt(0).select();
        }

        swithFragment(sta);

        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //选中了tab的逻辑
                if ("待收货".equals(tab.getText())) {
                    swithFragment("2");
                } else if ("已收货".equals(tab.getText())) {
                    swithFragment("3");
                } else if ("全部".equals(tab.getText())) {
                    swithFragment("");
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
                //未选中tab的逻辑
            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                //再次选中tab的逻辑
            }
        });

    }

    private void swithFragment(String state) {
        OrderListFragment f = new OrderListFragment();
        f.mDeliveryState = state;
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }


}
