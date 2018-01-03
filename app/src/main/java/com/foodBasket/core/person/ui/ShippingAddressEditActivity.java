package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.ButterKnife;

/**
 * 收货地址编辑
 */

public class ShippingAddressEditActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_edit);
        ButterKnife.bind(this);
        initTopbar("添加地址");
    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, ShippingAddressEditActivity.class);
        activity.startActivity(intent);
    }
}
