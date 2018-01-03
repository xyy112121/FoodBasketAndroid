package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.ButterKnife;

/**
 * 送货单
 */

public class OrderInfoActivity extends BaseActivity {


    public static void openActivity(Activity activity, String orderId) {
        Intent intent = new Intent(activity, OrderInfoActivity.class);
        intent.putExtra("orderId", orderId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.bind(this);
        initTopbar("送货单");
    }
}
