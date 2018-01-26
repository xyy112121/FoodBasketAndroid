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
 * Created by programmer on 2018/1/24.
 */

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    XTabLayout mTabLayout;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, OrderListActivity.class);
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
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("待发货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("待收货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已收货"));

        swithFragment();

        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //选中了tab的逻辑
                swithFragment();
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

    private void swithFragment() {
        OrderListFragment f = new OrderListFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }


}
