package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.androidkun.xtablayout.XTabLayout;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.orderDeliveryManFragement.OrderListFragmentDeliveryMan;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 送货单（送货人看到了）
 */

public class OrderListDeliveryManActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    XTabLayout mTabLayout;

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
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("未送货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已送货"));


        String sta = "";
        swithFragment(sta);

        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //选中了tab的逻辑
                if ("未送货".equals(tab.getText())) {
                    swithFragment("2");
                } else if ("已送货".equals(tab.getText())) {
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
        OrderListFragmentDeliveryMan f = new OrderListFragmentDeliveryMan();
        f.mDeliveryState = state;
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }
}
