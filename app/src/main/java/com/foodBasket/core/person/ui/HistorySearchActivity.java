package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.foodBasket.BaseTwoActivity;
import com.foodBasket.R;
import com.foodBasket.view.WaroLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索
 */

public class HistorySearchActivity extends BaseTwoActivity {

    @BindView(R.id.search_et)
    EditText mSearchEt;
    @BindView(R.id.search_hot_tv)
    TextView mHotTv;
    @BindView(R.id.search_hot_parent_lv)
    WaroLinearLayout mHotParentLv;
    @BindView(R.id.search_history_tv)
    TextView mHistoryTv;
    @BindView(R.id.search_parent_lv)
    WaroLinearLayout mParentLv;
    @BindView(R.id.search_del)
    TextView mDelTv;


    private String mHistoryKey = "FoodBasketAndroid_historySearch";
    private String mSharedPreferencesName = "FoodBasketAndroid";

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, HistorySearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences(mSharedPreferencesName, 0);
        String history = sp.getString(mHistoryKey, "");
        if (!"".equals(history)) {
            // 用逗号分割内容返回数组
            String[] history_arr = history.split(",");
            List<String> list = new ArrayList<>();
            if (history_arr.length >= 1) {
                // 保留前50条数据
                if (history_arr.length > 50) {
                    String[] newArrays = new String[50];
                    // 实现数组之间的复制
                    System.arraycopy(history_arr, 0, newArrays, 0, 50);
                    Collections.addAll(list, newArrays);
                } else {
                    Collections.addAll(list, history_arr);
                }
            }
            // 设置适配器
            addAll(list);
        }

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = mSearchEt.getText().toString().trim();
                    if (!"".equals(text) && text.length() >= 2) {
                        save(text);
//                        search(text);
                    } else {
                        showMessage("搜索内容至少需要两个字符！");
                    }
                }
                return false;
            }
        });
    }

    private void addAll(List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.activity_search_item, null);
            TextView textView = view.findViewById(R.id.search_item_tv);
            textView.setText(items.get(i));
            mParentLv.addView(view);

        }
    }

    private void add(String item) {
        View view = getLayoutInflater().inflate(R.layout.activity_search_item, null);
        TextView textView = view.findViewById(R.id.search_item_tv);
        textView.setText(item);
        mParentLv.addView(view);
    }

    public void save(String text) {
        if (!"".equals(text)) {
            // 获取搜索框信息
            SharedPreferences mysp = getSharedPreferences(mSharedPreferencesName, 0);
            String old_text = mysp.getString(mHistoryKey, "");

            // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
            StringBuilder builder = new StringBuilder(old_text);
            builder.append(text + ",");

            // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
            if (!old_text.contains(text + ",")) {
                SharedPreferences.Editor myeditor = mysp.edit();
                myeditor.putString(mHistoryKey, builder.toString());
                myeditor.commit();
                add(text);
            }
        }
    }


    @OnClick({R.id.search_cancel, R.id.search_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_cancel:
                String text = mSearchEt.getText().toString().trim();
                if (!"".equals(text) && text.length() >= 2) {
                    save(text);
//                        search(text);
                } else {
                    showMessage("搜索内容至少需要两个字符！");
                }
                break;
            case R.id.search_del:
                SharedPreferences mysp = getSharedPreferences(mSharedPreferencesName, 0);
                SharedPreferences.Editor myeditor = mysp.edit();
                myeditor.putString(mHistoryKey, "").commit();
                mParentLv.removeAllViews();
                break;
        }
    }


}
