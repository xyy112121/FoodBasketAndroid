package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;

/**
 * 搜索
 */

public class HistorySearchActivity extends BaseActivity {

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, HistorySearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
