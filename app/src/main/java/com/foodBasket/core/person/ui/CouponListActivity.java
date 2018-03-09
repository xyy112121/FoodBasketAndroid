package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.person.model.OrderListResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 我的钱包
 */

public class CouponListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    MyAdapter mAdapter;
    @BindView(R.id.wallet_list_total_tv)
    TextView mTotalTv;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, CouponListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        ButterKnife.bind(this);
        initTopbar("我的钱包");
        initUI();
    }

    public void initUI() {
        mAdapter = new MyAdapter(mContext, 0);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, false));


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                MerchantRowModel model = mAdapter.getItem(i);
//                MerchantEditActivity.openActivity(mContext, model.id);
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
            action.couponList(mContext, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    OrderListResModel model = JSON.parseObject(result, OrderListResModel.class);
                    if (model != null && model.getSuccess()) {
                        mAdapter.addAll(model.rows);
                        mTotalTv.setText("￥" + model.wallet + "元");
                    } else {
                        com.mic.etoast2.Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.endLoadingMore();
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

    public class MyAdapter extends ArrayAdapter<OrderListResModel.Rows> {

        public MyAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.fragment_order_list_item, null);
            ViewHolder holder = new ViewHolder(view);
            final OrderListResModel.Rows obj = getItem(i);
            holder.mOrderNoTv.setText("订单编号:" + obj.orderNo);
            holder.mStateTv.setText(obj.disDeliveryState);
            holder.mDisplayIsPayIv.setVisibility(View.GONE);
            holder.mDisplayIsPayTv.setText("优惠：￥" + obj.couponPay + "元");
            for (OrderListResModel.Rows.Products item : obj.products
                    ) {
                FrameLayout layout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_order_list_item_iv, null);
                ImageView imageView = layout.findViewById(R.id.iv);
                String url = MyApplication.getApplication().mImageUrl + item.productBasic_headPicture;
                Glide.with(mContext)
                        .load(url)
                        .apply(MyApplication.getOptions())
                        .into(imageView);
                holder.mShopLl.addView(layout);
            }
            holder.mProductCountTv.setText("共" + obj.productCount + "件商品 合计：");
            holder.mPriceTv.setText("￥" + obj.realPay + "元");


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    OrderInfoActivity.openActivity(mContext, obj.id);
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
            @BindView(R.id.order_item_displayIsPay_tv)
            TextView mDisplayIsPayTv;
            @BindView(R.id.order_item_displayIsPay_iv)
            ImageView mDisplayIsPayIv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
