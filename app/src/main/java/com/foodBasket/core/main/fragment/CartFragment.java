package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.foodBasket.R;
import com.foodBasket.core.main.adapter.ShopCarAdapter;
import com.foodBasket.core.main.model.ProductInfo;
import com.foodBasket.core.main.model.ShoppingCartResModel;
import com.foodBasket.core.main.model.ShoppingCartRowsModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.net.OrderAction;
import com.foodBasket.core.person.ui.OrderConfirmActivity;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.mic.etoast2.Toast;
import com.mylhyl.circledialog.CircleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 购物车
 */

public class CartFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.top_right_text)
    TextView mRightTv;
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.all_chekbox)
    CheckBox mAllCk;
    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTv;
    @BindView(R.id.tv_go_to_pay_layout)
    LinearLayout mPayLl;
    @BindView(R.id.delect_all_chekbox)
    CheckBox mDelectAllCk;
    @BindView(R.id.delect_tv_delete)
    TextView mDeleteTv;
    @BindView(R.id.shop_car_delect_layout)
    LinearLayout mDelectLl;
    Unbinder unbinder;

    private ShopCarAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        listener();
        return view;
    }

    public void initUI() {
        mAdapter = new ShopCarAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), false));

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            mRefreshLayout.beginRefreshing();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.beginRefreshing();
    }

    private void getData() {
        HomeAction action = new HomeAction();
        try {
            //购物车
            action.shoppingCartList(getActivity(), new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    ShoppingCartResModel model = JSON.parseObject(result, ShoppingCartResModel.class);

                    if (model != null && model.getSuccess()) {
                        mTotalPriceTv.setText("￥" + model.totalPrice);
                        List<ProductInfo> list = new ArrayList<>();
                        for (int i = 0; i < model.rows.size(); i++) {
                            ShoppingCartRowsModel item = model.rows.get(i);
                            ProductInfo obj = new ProductInfo();
                            obj.setOrderdetailid(item.id);
                            obj.setCommodityid(item.id);
                            obj.setCommodityname(item.name);
                            obj.setOrderprice(item.salePrice);
                            obj.setOrdernumber(item.productNumber);
                            obj.setCommoditypicture(item.headPicture);
                            list.add(obj);
                        }
                        mAdapter.addAll(list);
                    }
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.endLoadingMore();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listener() {
        mAllCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mAdapter.selectAll();
                    mDelectAllCk.setChecked(true);
                    mAllCk.setText("全不选");
                    mDelectAllCk.setText("全不选");
                } else {
                    mAdapter.selectAllNo();
                    mDelectAllCk.setChecked(false);
                    mAllCk.setText("全选");
                    mDelectAllCk.setText("全选");
                }
            }
        });

        mDelectAllCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mAdapter.selectAll();
                    mAllCk.setChecked(true);
                    mAllCk.setText("全不选");
                    mDelectAllCk.setText("全不选");

                } else {
                    mAdapter.selectAllNo();
                    mAllCk.setChecked(false);
                    mAllCk.setText("全选");
                    mDelectAllCk.setText("全选");
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.removeAll();
        getData();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @OnClick({R.id.top_right_text, R.id.tv_go_to_pay, R.id.delect_tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_right_text:
                Animation hideAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.out_toptobottom);
                Animation showAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.in_bottomtotop);
                if ("删除".equals(mRightTv.getText())) {
                    mRightTv.setText("完成");
                    mPayLl.startAnimation(hideAnim);
                    mPayLl.setVisibility(View.GONE);
                    mDelectLl.startAnimation(showAnim);
                    mDelectLl.setVisibility(View.VISIBLE);
                    mAdapter.showCk(true);//显示多选框
                } else {
                    mRightTv.setText("删除");
                    mPayLl.startAnimation(showAnim);
                    mPayLl.setVisibility(View.VISIBLE);
                    mDelectLl.startAnimation(hideAnim);
                    mDelectLl.setVisibility(View.GONE);
                    mAdapter.showCk(false);//不显示多选框
                }
                break;
            case R.id.tv_go_to_pay://提交订单
                orderFrom();
                break;
            case R.id.delect_tv_delete:
                delete();
                break;
        }
    }

    /**
     * 去结算
     */
    private void orderFrom() {
        LatteLoader.showLoading(getActivity());
        try {
            if (mAdapter.getAll() != null) {
                List<Order> list = new ArrayList<>();
                for (ProductInfo item : mAdapter.getAll()) {
                    Order order = new Order(item.getCommodityid(), item.getOrdernumber());
                    list.add(order);
                }
                OrderAction action = new OrderAction();
                final String products = JSON.toJSONString(list);

                //购物车
                action.orderFrom(getActivity(), products, new MyStringCallBack() {
                    @Override
                    public void onResult(String result) {
                        LatteLoader.stopLoading();
                        ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                        if (model != null) {
                            if (model.getSuccess()) {
                                OrderConfirmActivity.openActivity(getActivity(), result, products);
                            } else {
                                Toast.makeText(getActivity(), model.getResultInfo(), android.widget.Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class Order {
        public String productId;
        public int count;

        public Order(String id, int count) {
            this.productId = id;
            this.count = count;
        }
    }


    //删除
    private void delete() {
        new CircleDialog.Builder((FragmentActivity) getActivity())
                .setTitle("提示")
                .setText("确定删除么？")
                .setNegative("取消", null)
                .setPositive("确定", null)
                .show();

    }

}
