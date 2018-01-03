package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodBasket.R;
import com.foodBasket.core.main.adapter.DiscountsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 特惠首页
 * price_tex.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);  加横线
 */

public class DiscountsFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.discounts_tv)
    TextView mDiscountsTv;
    @BindView(R.id.discounts_vv)
    View mDiscountsVv;
    @BindView(R.id.recommend_tv)
    TextView mRecommendTv;
    @BindView(R.id.recommend_vv)
    View mRecommendVv;

    private DiscountsAdapter mAdapter;
    private int mPage = 1;
    private int mTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discounts, null);
        unbinder = ButterKnife.bind(this, view);
        initialUI();
        return view;
    }


    public void initialUI() {
        //设置布局管理器为2列，纵向
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new DiscountsAdapter(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), true));
        mRefreshLayout.beginRefreshing();
        mAdapter.setOnItemClickListener(new DiscountsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
//        try {
//            SecondaryGoodsAction action = new SecondaryGoodsAction();
//            mRequest.page = mPage++;
//            mRequest.rows = 10;
//
//            action.getCategoryList(mRequest, new MyStringCallBack() {
//                @Override
//                public void onResult(ResponseBean result) {
//                    mRefreshLayout.endRefreshing();
//                    mRefreshLayout.endLoadingMore();
//                    if (result != null && "1".equals(result.getErrorcode())) {
//                        Object data = result.getData();
//                        Gson gson = new GsonBuilder().serializeNulls().create();
//                        String json = gson.toJson(data);
//                        mResponse = gson.fromJson(json, SecondaryGoodsCategoryResponseModel.class);
//                        mAdapter.addList(mResponse.filter_item.rows);
//                        mTypeList = mResponse.filter_data.item_category;
//                        mPriceList = mResponse.filter_data.item_price;
//                        mTotal = Integer.parseInt(mResponse.filter_item.total);
//                    } else {
//                        ToastUtil.showMessage(getString(R.string.error));
//                    }
//                }
//            });
//        } catch (Exception e) {
//            ToastUtil.showMessage(getString(R.string.error));
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
//        }

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.removeAll();
        mPage = 1;
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (mAdapter.mData.size() < mTotal) {
            getData();
        } else {
            mRefreshLayout.endLoadingMore();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.discounts_ll, R.id.recommend_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.discounts_ll:
                mRecommendTv.setTextColor(getResources().getColor(R.color.text_color, null));
                mDiscountsTv.setTextColor(getResources().getColor(R.color.black, null));
                mDiscountsVv.setVisibility(View.VISIBLE);
                mRecommendVv.setVisibility(View.GONE);
                break;
            case R.id.recommend_ll:
                mRecommendTv.setTextColor(getResources().getColor(R.color.black, null));
                mDiscountsTv.setTextColor(getResources().getColor(R.color.text_color, null));
                mDiscountsVv.setVisibility(View.GONE);
                mRecommendVv.setVisibility(View.VISIBLE);
                break;
        }
    }
}
