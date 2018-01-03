package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.ui.HistorySearchActivity;
import com.foodBasket.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页
 */

public class HomeFragment extends Fragment {
    Unbinder unbinder;
    private List<FrameLayout> mListLayout = new ArrayList<>();
    private int mIndex = 0;
    private int mIndex2 = -1;//前一次点击的下标
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        initTop();
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private void initTop() {
        LinearLayout parentLayout = mView.findViewById(R.id.home_top_parent_ll);
        for (int i = 0; i < 3; i++) {
            final int index = i;
//                Condition item = list.get(i);
            final FrameLayout layout = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.home_top_parent_item, null);
            TextView t = layout.findViewById(R.id.home_top_parent_item_tv);
            t.setText("蔬菜");
            if (i == 0) {
                t.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                layout.findViewById(R.id.home_top_parent_item_line).setVisibility(View.VISIBLE);
                swithFragment();
            }
            int width = DimenUtil.getScreenWidth();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex2 = mIndex;
                    mIndex = index;
                    if (mIndex2 != -1 && mIndex != mIndex2) {
                        ((TextView) view.findViewById(R.id.home_top_parent_item_tv)).setTextColor(
                                ContextCompat.getColor(getActivity(), R.color.black));
                        (view.findViewById(R.id.home_top_parent_item_line)).setVisibility(View.VISIBLE);
                        setViewColor();
                    }
                    swithFragment();
                }
            });
            mListLayout.add(layout);
            parentLayout.addView(layout);
        }

    }

    public void setViewColor() {
        FrameLayout layout = mListLayout.get(mIndex2);
        ((TextView) layout.findViewById(R.id.home_top_parent_item_tv)).setTextColor(
                ContextCompat.getColor(getActivity(), R.color.bar_grey));
        (layout.findViewById(R.id.home_top_parent_item_line)).setVisibility(View.GONE);
    }

    private void swithFragment() {
        HomeListFragment f = new HomeListFragment();
        FragmentTransaction t = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.top_left, R.id.top_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_left:
                HistorySearchActivity.openActivity(getActivity());
                break;
            case R.id.top_right:
                break;
        }
    }
}
