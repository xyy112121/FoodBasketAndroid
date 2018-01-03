package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.adapter.ShippingAddressListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 收货地址列表
 */

public class ShippingAddressListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    ShippingAddressListAdapter mAdapter;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, ShippingAddressListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_list);
        ButterKnife.bind(this);
        initTopbar("收货地址");
        initUI();
    }

    public void initUI() {
        mAdapter = new ShippingAddressListAdapter(mContext);
//        mAdapter = new ShippingAddressListAdapter(mContext, R.layout.activity_shipping_address_list_item);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
//        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @OnClick(R.id.shipping_address_list_add_ll)
    public void onViewClicked() {
        ShippingAddressEditActivity.openActivity(mContext);
    }
}
