package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.adapter.OrderWaitReceivingListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 待收货列表
 */

public class OrderWaitReceivingListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    OrderWaitReceivingListAdapter mAdapter;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, OrderWaitReceivingListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_wait_receiving_list);
        ButterKnife.bind(this);
        initTopbar("待收货");
        initUI();
    }

    public void initUI() {
        mAdapter = new OrderWaitReceivingListAdapter(mContext);
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

}
