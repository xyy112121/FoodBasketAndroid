package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 认证
 */

public class IdentificationActivity extends BaseActivity {


    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, IdentificationActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        ButterKnife.bind(this);
        initTopbar("认证");
    }
}
