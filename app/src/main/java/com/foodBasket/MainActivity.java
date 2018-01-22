package com.foodBasket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.foodBasket.core.main.fragment.CartFragment;
import com.foodBasket.core.main.fragment.HomeFragment;
import com.foodBasket.core.main.fragment.PersonFragment;
import com.foodBasket.core.main.fragment.TypeFragment;
import com.foodBasket.view.MainNavigateTabBar;

public class MainActivity extends AppCompatActivity {

    private MainNavigateTabBar mNavigateTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigateTabBar = findViewById(R.id.mainTabBar);

//        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_main_false, R.mipmap.icon_main_true, "首页"));
        mNavigateTabBar.addTab(TypeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_type_false, R.mipmap.icon_type_true, "分类"));
//        mNavigateTabBar.addTab(DiscountsFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "特惠"));
        mNavigateTabBar.addTab(CartFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_shapcar_false, R.mipmap.icon_shopcar_true, "购物车"));
        mNavigateTabBar.addTab(PersonFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_me_false, R.mipmap.icon_me_true, "我的"));
    }
}
