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
import com.foodBasket.core.person.adapter.AddressListAdapter;
import com.foodBasket.core.person.model.AddressListResModel;
import com.foodBasket.core.person.model.AddressResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 收货地址列表
 */

public class AddressListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    AddressListAdapter mAdapter;

    private int mPage = 1;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, AddressListActivity.class);
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
        mAdapter = new AddressListAdapter(mContext, 0);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressResModel model = mAdapter.getItem(i);
                Intent intent = new Intent();
                String json = JSON.toJSONString(model);
                intent.putExtra("addr", json);
                setResult(RESULT_OK, intent);
                finish();
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
            action.userAddrList(mContext, mPage, 10, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    AddressListResModel model = JSON.parseObject(result, AddressListResModel.class);
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
        mPage = 1;
        getData();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        getData();
        return false;
    }

    @OnClick(R.id.shipping_address_list_add_ll)
    public void onViewClicked() {
        AddressEditActivity.openActivity(mContext);
    }
}
