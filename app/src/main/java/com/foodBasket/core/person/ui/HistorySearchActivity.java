package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.foodBasket.BaseActivity;
import com.foodBasket.BaseTwoActivity;
import com.foodBasket.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索历史记录
 */
public class HistorySearchActivity extends BaseTwoActivity {
    @BindView(R.id.search_et)
    EditText mSearchEt;
    @BindView(R.id.search_history_list)
    ListView mListView;
    @BindView(R.id.search_del)
    TextView mSearchDel;
    private MyAdapter mAdapter;
    private String mHistoryKey = "foodbasket";
    private String mSharedKey = "foodbasket_search";

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, HistorySearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_search);
        ButterKnife.bind(this);
        mAdapter = new MyAdapter(mContext, 0);
        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences(mSharedKey, 0);
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
            } else {
                mSearchDel.setVisibility(View.GONE);

            }
            // 设置适配器
            mListView.setAdapter(mAdapter);
            if (list != null && list.size() > 0) {
                mAdapter.addAll(list);
                mSearchDel.setVisibility(View.VISIBLE);
            }
        } else {
            mSearchDel.setVisibility(View.GONE);
        }
        listener();
    }

    public void listener() {

        mSearchEt.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = mSearchEt.getText().toString().trim();
                    if (!"".equals(text) && text.length() >= 2) {
                        save(text);
                        search(text);
                    } else {
                        showMessage("搜索内容至少需要两个字符！");
                    }
                }
                return false;
            }
        });


        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                save(mAdapter.getItem(position));
                search(mAdapter.getItem(position));
            }
        });
    }

    public void search(String keyword) {
        Intent intent = new Intent(HistorySearchActivity.this, HistorySearchListActivity.class);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
    }

    @OnClick({R.id.search_cancel, R.id.search_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_cancel:
                finish();
                break;
            case R.id.search_del:
                SharedPreferences mysp = getSharedPreferences(mSharedKey, 0);
                SharedPreferences.Editor myeditor = mysp.edit();
                myeditor.putString(mHistoryKey, "").commit();
                mAdapter.clear();
                break;
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_history_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mText.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.expert_search_item_text)
            TextView mText;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    public void save(String text) {
        if (!"".equals(text)) {
            mSearchDel.setVisibility(View.VISIBLE);
            // 获取搜索框信息
            SharedPreferences mysp = getSharedPreferences(mSharedKey, 0);
            String old_text = mysp.getString(mHistoryKey, "");

            // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
            StringBuilder builder = new StringBuilder(old_text);
            builder.append(text + ",");

            // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
            if (!old_text.contains(text + ",")) {
                SharedPreferences.Editor myeditor = mysp.edit();
                myeditor.putString(mHistoryKey, builder.toString());
                myeditor.commit();
                mAdapter.add(text);
            }
        }
    }
}
