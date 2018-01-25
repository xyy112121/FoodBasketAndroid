package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.xtablayout.XTabLayout;
import com.foodBasket.MainActivity;
import com.foodBasket.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 分类
 */

public class TypeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    XTabLayout mTabLayout;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_type, null);
        unbinder = ButterKnife.bind(this, mView);
        initTop();
        return mView;
    }

    private void initTop() {
        mTabLayout.addTab(mTabLayout.newTab().setText("干货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("蔬菜"));
        mTabLayout.addTab(mTabLayout.newTab().setText("生鲜"));
        mTabLayout.addTab(mTabLayout.newTab().setText("肉类"));

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
        TypeListFragment f = new TypeListFragment();
        FragmentTransaction t = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        t.replace(R.id.type_content_frame, f);
        t.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
