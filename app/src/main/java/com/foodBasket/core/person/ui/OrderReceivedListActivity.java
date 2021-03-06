package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已收货（用户看到的）
 */

public class OrderReceivedListActivity extends BaseActivity {
//    @BindView(R.id.home_top_parent_ll)
//    LinearLayout mTopParentLl;


    private List<FrameLayout> mListLayout = new ArrayList<>();
    private int mIndex = 0;
    private int mIndex2 = -1;//前一次点击的下标

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, OrderReceivedListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_deliveryman);
        ButterKnife.bind(this);
        initTopbar("已收货");
        initTop();
    }

    private void initTop() {
//        List<String> topList = new ArrayList<>();
//        topList.add("全款");
//        topList.add("现款");
//        topList.add("交易成功");
//        for (int i = 0; i < topList.size(); i++) {
//            final int index = i;
//            final FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_order_list_top_item, null);
//            TextView t = layout.findViewById(R.id.order_top_parent_item_tv);
//            t.setText(topList.get(i));
//            if (i == 0) {
//                t.setTextColor(ContextCompat.getColor(mContext, R.color.text_color1));
//                swithFragment();
//            }
//            int width = DimenUtil.getScreenWidth();
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layout.setLayoutParams(lp);
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mIndex2 = mIndex;
//                    mIndex = index;
//                    if (mIndex2 != -1 && mIndex != mIndex2) {
//                        ((TextView) view.findViewById(R.id.order_top_parent_item_tv)).setTextColor(
//                                ContextCompat.getColor(mContext, R.color.text_color1));
//                        setViewColor();
//                    }
//                    swithFragment();
//                }
//            });
//            mListLayout.add(layout);
//            mTopParentLl.addView(layout);
//        }

    }

    public void setViewColor() {
//        FrameLayout layout = mListLayout.get(mIndex2);
//        ((TextView) layout.findViewById(R.id.order_top_parent_item_tv)).setTextColor(
//                ContextCompat.getColor(mContext, R.color.bar_grey));
    }

    private void swithFragment() {
        OrderReceivedListFragment f = new OrderReceivedListFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, f);
        t.commit();
    }
}
