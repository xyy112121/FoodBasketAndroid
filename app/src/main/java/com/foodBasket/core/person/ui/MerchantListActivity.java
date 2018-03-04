package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.adapter.MerchantListAdapter;
import com.foodBasket.core.person.model.MerchantListResModel;
import com.foodBasket.core.person.model.MerchantRowModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 餐馆列表
 */
public class MerchantListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    MerchantListAdapter mAdapter;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, MerchantListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);
        ButterKnife.bind(this);
        initTopbar("餐馆列表");
        initUI();
    }

    public void initUI() {
        mAdapter = new MerchantListAdapter(mContext, 0);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, false));


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MerchantRowModel model = mAdapter.getItem(i);
                MerchantEditActivity.openActivity(mContext, model.id);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.beginRefreshing();
    }

    private void getData() {
        PersonAction action = new PersonAction();
        try {
            action.merchantList(mContext, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    MerchantListResModel model = JSON.parseObject(result, MerchantListResModel.class);
                    if (model != null && model.getSuccess()) {
                        mAdapter.addAll(model.rows);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.clear();
        getData();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @OnClick(R.id.shipping_address_list_add_ll)
    public void onViewClicked() {
        MerchantEditActivity.openActivity(mContext, "");
    }
}
