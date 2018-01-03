package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.ButterKnife;

/**
 * 确认订单
 */

public class OrderConfirmActivity extends BaseActivity {


    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, OrderConfirmActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        initTopbar("确定订单");
    }
}
