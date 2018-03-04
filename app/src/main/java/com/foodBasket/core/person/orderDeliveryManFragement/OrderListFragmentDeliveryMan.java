package com.foodBasket.core.person.orderDeliveryManFragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.foodBasket.R;
import com.foodBasket.core.person.model.DeliveryListResModel;
import com.foodBasket.core.person.net.OrderAction;
import com.foodBasket.core.person.ui.OrderInfoActivity;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.loader.LatteLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 送货单fragment（送货人看到）
 */

public class OrderListFragmentDeliveryMan extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.listview)
    ListView mListView;
    Unbinder unbinder;

    private MyAdapter mAdapter;

    public String mDeliveryState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list_deliveryman, null);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    public void initUI() {
        mAdapter = new MyAdapter(getActivity(), 0);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), false));
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.beginRefreshing();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getData() {
        LatteLoader.showLoading(getActivity());
        OrderAction action = new OrderAction();
        try {
            //购物车
            action.deliveryList(getActivity(), mDeliveryState, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    DeliveryListResModel model = JSON.parseObject(result, DeliveryListResModel.class);
                    if (model != null && model.getSuccess()) {
                        mAdapter.addAll(model.rows);
                    } else {
                        com.mic.etoast2.Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                    }
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.endLoadingMore();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public class MyAdapter extends ArrayAdapter<DeliveryListResModel.Rows> {

        public MyAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.fragment_order_list_item_deliveryman, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final DeliveryListResModel.Rows obj = getItem(i);
            holder.mOrderNoTv.setText("订单编号：" + obj.order_orderNo);
            holder.mDeliveryStateTv.setText(obj.order_disDeliveryStateForDeliveryUser);
            holder.mContactpersonNameTv.setText(obj.order_userAddress_userName);
            holder.mMobileTv.setText(obj.order_userAddress_mobile);
            String addr = obj.order_userAddress_province +
                    obj.order_userAddress_city +
                    obj.order_userAddress_county + obj.order_userAddress_address;
            holder.mAddrTv.setText(addr);
            holder.mTimeTv.setText(obj.deliveryTime);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderInfoActivity.openActivity(getActivity(), obj.order_id);
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.order_item_orderNo_tv)
            TextView mOrderNoTv;
            @BindView(R.id.order_item_deliveryState_tv)
            TextView mDeliveryStateTv;
            @BindView(R.id.order_item_contactperson_name_tv)
            TextView mContactpersonNameTv;
            @BindView(R.id.order_item_mobile_tv)
            TextView mMobileTv;
            @BindView(R.id.order_item_addr_tv)
            TextView mAddrTv;
            @BindView(R.id.order_item_time_tv)
            TextView mTimeTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
