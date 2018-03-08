package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.top_view)
    View mTopView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initTopbar("关于我们");
        mTopView.setVisibility(View.GONE);
    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }
}
