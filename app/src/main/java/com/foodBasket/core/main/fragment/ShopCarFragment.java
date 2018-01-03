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

import com.foodBasket.R;
import com.foodBasket.core.main.adapter.ShopCarAdapter;
import com.foodBasket.core.main.model.ProductInfo;
import com.foodBasket.core.person.ui.OrderConfirmActivity;
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

public class ShopCarFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
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
        View view = inflater.inflate(R.layout.fragment_shop_car, null);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        listener();
        getData();
        return view;
    }

    public void initUI() {
        mAdapter = new ShopCarAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), true));
//        mRefreshLayout.beginRefreshing();
    }

    private void getData() {
        List<ProductInfo> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ProductInfo obj = new ProductInfo();
            obj.setOrderdetailid("1");
            obj.setCommodityid("1");
            obj.setCommodityname("蔬菜" + i);
            obj.setOrderprice(25);
            obj.setOrdernumber(12);
            list.add(obj);
        }
        mAdapter.addAll(list);
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
                if ("编辑".equals(mRightTv.getText())) {
                    mRightTv.setText("完成");
                    mPayLl.startAnimation(hideAnim);
                    mPayLl.setVisibility(View.GONE);
                    mDelectLl.startAnimation(showAnim);
                    mDelectLl.setVisibility(View.VISIBLE);
                    mAdapter.showCk(true);//显示多选框
                } else {
                    mRightTv.setText("编辑");
                    mPayLl.startAnimation(showAnim);
                    mPayLl.setVisibility(View.VISIBLE);
                    mDelectLl.startAnimation(hideAnim);
                    mDelectLl.setVisibility(View.GONE);
                    mAdapter.showCk(false);//不显示多选框
                }

                break;
            case R.id.tv_go_to_pay://提交订单
                OrderConfirmActivity.openActivity(getActivity());
                break;
            case R.id.delect_tv_delete:
                delete();
                break;
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
