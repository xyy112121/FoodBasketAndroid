package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.androidkun.xtablayout.XTabLayout;
import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.core.main.model.CategoryResModel;
import com.foodBasket.core.main.model.CategoryRowsModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.ui.HistorySearchActivity;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.loader.LatteLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 分类
 */

public class TypeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    XTabLayout mTabLayout;
    @BindView(R.id.type_more_iv)
    ImageView mMoreIv;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_type, null);
        unbinder = ButterKnife.bind(this, mView);
        getTopData();
        initTop();
        return mView;
    }


    private void getTopData() {
        HomeAction action = new HomeAction();
        LatteLoader.showLoading(getActivity());
        try {
            action.categoryList(1, 25, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
//                    LatteLoader.stopLoading();
                    CategoryResModel model = JSON.parseObject(result, CategoryResModel.class);
                    if (model != null && model.getSuccess()) {
                        if (model.rows.size() > 4) {
                            mMoreIv.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < model.rows.size(); i++) {
                            if (i <= 3) {
                                CategoryRowsModel item = model.rows.get(i);
                                mTabLayout.addTab(mTabLayout.newTab().setText(item.name).setTag(item.id));
                                if (i == 0) {
                                    swithFragment(item.id);
                                }
                            }

                        }
                    }
                }
            });
        } catch (Exception e) {
            LatteLoader.stopLoading();
            e.printStackTrace();
        }
    }

    private void initTop() {
        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //选中了tab的逻辑
                swithFragment(tab.getTag() + "");
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


    private void swithFragment(String typeId) {
        TypeListFragment f = new TypeListFragment();
        f.mTypeId = typeId;
        FragmentTransaction t = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        t.replace(R.id.type_content_frame, f);
        t.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.expert_search_text)
    public void onViewClicked() {
        HistorySearchActivity.openActivity(getActivity());
    }
}
