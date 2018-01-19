package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.core.main.adapter.DiscountsAdapter;
import com.foodBasket.core.person.ui.HistorySearchActivity;
import com.foodBasket.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 首页
 */

public class HomeFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    private DiscountsAdapter mAdapter;
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
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new DiscountsAdapter(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), true));
//        mRefreshLayout.beginRefreshing();

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

}
