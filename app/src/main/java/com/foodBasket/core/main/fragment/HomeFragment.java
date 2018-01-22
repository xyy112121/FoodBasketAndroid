package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodBasket.R;
import com.foodBasket.core.main.adapter.BaseRecyclerAdapter;
import com.foodBasket.core.main.adapter.GridItemDecoration;
import com.foodBasket.core.main.adapter.HomeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 首页
 */

public class HomeFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    private HomeAdapter mAdapter;
    private int mPage = 1;
    private int mTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        initialUI();
        return view;
    }

    public void initialUI() {
        //设置布局管理器为2列，纵向
//        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,
//                StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new HomeAdapter();
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new GridItemDecoration(getActivity(), true));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addDatas(generateData());
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), true));
//        mRefreshLayout.beginRefreshing();
        setHeader(mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String data) {
                Toast.makeText(getActivity(), position + "," + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> generateData() {
        ArrayList<String> data = new ArrayList<String>() {
            {
                for (int i = 0; i < 21; i++) add("数据" + i);
            }
        };
        return data;
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_head, view, false);
        mAdapter.setHeaderView(header);
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
//        mAdapter.removeAll();
//        mPage = 1;
//        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (mAdapter.getItemCount() < mTotal) {
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

}
