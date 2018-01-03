package com.foodBasket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.foodBasket.core.main.fragment.DiscountsFragment;
import com.foodBasket.core.main.fragment.HomeFragment;
import com.foodBasket.core.main.fragment.PersonFragment;
import com.foodBasket.core.main.fragment.ShopCarFragment;
import com.foodBasket.view.MainNavigateTabBar;

public class MainActivity extends AppCompatActivity {

    private MainNavigateTabBar mNavigateTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigateTabBar = findViewById(R.id.mainTabBar);

//        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "首页"));
        mNavigateTabBar.addTab(DiscountsFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "特惠"));
        mNavigateTabBar.addTab(ShopCarFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "购物车"));
        mNavigateTabBar.addTab(PersonFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "我的"));
    }
}
