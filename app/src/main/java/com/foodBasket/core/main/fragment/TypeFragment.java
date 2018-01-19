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

import com.blankj.utilcode.util.SizeUtils;
import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 分类
 */

public class TypeFragment extends Fragment {
    Unbinder unbinder;
    private List<FrameLayout> mListLayout = new ArrayList<>();
    private int mIndex = 0;
    private int mIndex2 = -1;//前一次点击的下标
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_type, null);
        initTop();
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private void initTop() {
        LinearLayout parentLayout = mView.findViewById(R.id.home_top_parent_ll);
        String[] tops = new String[]{"干货", "蔬菜", "生鲜", "肉类"};
        for (int i = 0; i < tops.length; i++) {
            final int index = i;
//                Condition item = list.get(i);
            final FrameLayout layout = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.item_type_top_parent, null);
            TextView t = layout.findViewById(R.id.home_top_parent_item_tv);
            t.setText(tops[i]);
            if (i == 0) {
                t.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue));
                layout.findViewById(R.id.home_top_parent_item_line).setVisibility(View.VISIBLE);
                swithFragment();
            }
            int width = DimenUtil.getScreenWidth() - SizeUtils.dp2px(38);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / tops.length, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex2 = mIndex;
                    mIndex = index;
                    if (mIndex2 != -1 && mIndex != mIndex2) {
                        ((TextView) view.findViewById(R.id.home_top_parent_item_tv)).setTextColor(
                                ContextCompat.getColor(getActivity(), R.color.blue));
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
                ContextCompat.getColor(getActivity(), R.color.text_color));
        (layout.findViewById(R.id.home_top_parent_item_line)).setVisibility(View.GONE);
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
