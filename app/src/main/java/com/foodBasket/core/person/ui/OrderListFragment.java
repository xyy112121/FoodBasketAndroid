package com.foodBasket.core.person.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.person.model.OrderListResModel;
import com.foodBasket.core.person.net.OrderAction;
import com.foodBasket.net.MyStringCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by programmer on 2018/1/24.
 */

public class OrderListFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
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
        mAdapter = new MyAdapter(getActivity(), R.layout.fragment_order_list_item);
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
        OrderAction action = new OrderAction();
        try {
            //购物车
            action.orderList(getActivity(), mDeliveryState, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    OrderListResModel model = JSON.parseObject(result, OrderListResModel.class);
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

    public class MyAdapter extends ArrayAdapter<OrderListResModel.Rows> {

        public MyAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.fragment_order_list_item, null);
            ViewHolder holder = new ViewHolder(view);
            final OrderListResModel.Rows obj = getItem(i);
            holder.mOrderNoTv.setText(obj.orderNo);
            holder.mStateTv.setText(obj.disDeliveryState);
            for (OrderListResModel.Rows.Products item : obj.products
                    ) {
                FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_list_item_iv, null);
                ImageView imageView = layout.findViewById(R.id.iv);
                String url = MyApplication.getApplication().mImageUrl + item.productBasic_headPicture;
                Glide.with(getActivity())
                        .load(url)
                        .apply(MyApplication.getOptions())
                        .into(imageView);
                holder.mShopLl.addView(layout);
            }
            holder.mProductCountTv.setText("共" + obj.productCount + "件商品 合计：");
            holder.mPriceTv.setText("￥"+obj.totalPrice);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderInfoActivity.openActivity(getActivity(), obj.id);
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.order_item_orderNo_tv)
            TextView mOrderNoTv;
            @BindView(R.id.order_item_state_tv)
            TextView mStateTv;
            @BindView(R.id.order_item_shop_ll)
            LinearLayout mShopLl;
            @BindView(R.id.order_item_productCount_tv)
            TextView mProductCountTv;
            @BindView(R.id.order_item_price_tv)
            TextView mPriceTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}

