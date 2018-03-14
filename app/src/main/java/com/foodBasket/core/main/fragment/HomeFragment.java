package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
import com.foodBasket.core.goods.view.CartAddPopWin;
import com.foodBasket.core.main.adapter.BaseRecyclerAdapter;
import com.foodBasket.core.main.model.DiscoveryResModel;
import com.foodBasket.core.main.model.ProductDiscountModelRes;
import com.foodBasket.core.main.model.ProductListModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.ui.HistorySearchActivity;
import com.foodBasket.core.person.ui.WebActivity;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.dimen.DimenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import me.grantland.widget.AutofitTextView;

/**
 * 首页
 */

public class HomeFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.home_head_parent_layout)
    LinearLayout mParentLayout;
    @BindView(R.id.main_view)
    LinearLayout mainView;
    @BindView(R.id.scrollView)
    ScrollView mSlView;
    @BindView(R.id.search_history_me)
    ImageView mDiscoveryIv;

    private HomeAdapter mAdapter;
    private int mPage = 1;
    private int mTotal;

    private String mDiscoveryUrl;

    WindowManager.LayoutParams params;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        initialUI();
        getDiscovery();
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
//        mAdapter.addDatas(generateData());
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), false));
        mRefreshLayout.beginRefreshing();
//        setHeader(mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                ProductListModel model = (ProductListModel) data;
                ProductInfoActivity.openActivity(getActivity(), model.id);
            }
        });

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mSlView.fullScroll(ScrollView.FOCUS_UP);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden == false) {
//            mSlView.fullScroll(ScrollView.FOCUS_UP);
//        }
//    }

    private void getData() {
        HomeAction action = new HomeAction();
        try {
            //推荐
            action.recommendList(1, 25, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    ProductDiscountModelRes model = JSON.parseObject(result, ProductDiscountModelRes.class);
                    if (model != null && model.getSuccess()) {
                        if (model.rows.size() > 0) {
                            for (final ProductListModel item : model.rows) {
                                FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_head_item, null);
                                RelativeLayout layout1 = layout.findViewById(R.id.home_head_item_parent_layout);
                                int width = (int) ((DimenUtil.getScreenWidth()) * 0.8);
                                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout1.getLayoutParams();
                                lp.width = width;
                                layout1.setLayoutParams(lp);
                                ImageView pic = layout.findViewById(R.id.item_recommend_picture_iv);
                                String url = MyApplication.getApplication().mImageUrl + item.headPicture;
                                Glide.with(getActivity())
                                        .load(url)
                                        .apply(MyApplication.getOptions())
                                        .into(pic);
                                ((AutofitTextView) layout.findViewById(R.id.item_recommend_name_tv)).setText(item.name);
                                ((AutofitTextView) layout.findViewById(R.id.item_recommend_summary_tv)).setText(item.summary);
                                ((AutofitTextView) layout.findViewById(R.id.item_recommend_alias_tv)).setText(item.alias);
                                ((TextView) layout.findViewById(R.id.item_recommend_price_tv)).setText("￥" + item.salePrice + "元/" + item.displayUnit);
                                int userType = ShareConfig.getConfigInt(getActivity(), Constants.USERTYPE, 0);
                                if (userType == 1) {
                                    layout.findViewById(R.id.item_recommend_add_iv).setVisibility(View.GONE);
                                }
                                layout.findViewById(R.id.item_recommend_add_iv).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showPopFormBottom(item);

                                    }
                                });

                                layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ProductInfoActivity.openActivity(getActivity(), item.id);
                                    }
                                });
                                mParentLayout.addView(layout);
                            }
                        }
                    }
                }
            });

            //特惠
            action.discountList(1, 25, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    ProductDiscountModelRes model = JSON.parseObject(result, ProductDiscountModelRes.class);
                    if (model != null && model.getSuccess()) {
                        mAdapter.addDatas(model.rows);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();

    }

    private void getDiscovery() {
        HomeAction action = new HomeAction();
        try {
            //特惠
            action.getDiscovery(new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    DiscoveryResModel model = JSON.parseObject(result, DiscoveryResModel.class);
                    if (model != null && model.getSuccess()) {
                        if (model.discovery != null && model.discovery.equals("true")) {
                            mDiscoveryIv.setVisibility(View.VISIBLE);
                            mDiscoveryUrl = model.discovery.url;
                        } else {
                            mDiscoveryIv.setVisibility(View.GONE);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopFormBottom(ProductListModel model) {
        CartAddPopWin takePhotoPopWin = new CartAddPopWin(getActivity(), model);
//        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(mainView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getActivity().getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.removeAll();
        mParentLayout.removeAllViews();
        mPage = 1;
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.expert_search_text, R.id.search_history_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.expert_search_text:
                HistorySearchActivity.openActivity(getActivity());
                break;
            case R.id.search_history_me:
                String url;
                String userId = ShareConfig.getConfigString(getActivity(), Constants.USERID, "");
                if (mDiscoveryUrl.contains("?")) {
                    url = mDiscoveryUrl + "&userid=" + userId;
                } else {
                    url = mDiscoveryUrl + "?userid=" + userId;
                }
                WebActivity.openActivity(getActivity(), "发现", url);

                break;
        }
    }

    public class HomeAdapter extends BaseRecyclerAdapter<ProductListModel> {
        Context mContext;

        @Override
        public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item, parent, false);
            return new MyHolder(layout);
        }

        @Override
        public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, final ProductListModel data) {
            if (viewHolder instanceof MyHolder) {
                MyHolder holder = (MyHolder) viewHolder;
                holder.nameTv.setText(data.name);
                holder.priceTv.setText("￥" + data.salePrice + "元/" + data.displayUnit);
                String url = MyApplication.getApplication().mImageUrl + data.headPicture;
                Glide.with(mContext)
                        .load(url)
                        .apply(MyApplication.getOptions())
                        .into(holder.pictureIv);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.parentLayout.getLayoutParams();
                if (RealPosition % 2 == 1) {  //是偶数
                    params.setMargins(0, 0, 16, 16);
                } else {
                    params.setMargins(16, 0, 16, 16);
                }
                if (RealPosition == 0) {
                    params.setMargins(16, 0, 16, 16);
                }
                holder.parentLayout.setLayoutParams(params);

                int userType = ShareConfig.getConfigInt(getActivity(), Constants.USERTYPE, 0);
                if (userType == 1) {
                    holder.addIv.setVisibility(View.GONE);
                }

                holder.addIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopFormBottom(data);
                    }
                });
            }
        }

        class MyHolder extends Holder {
            AutofitTextView nameTv;
            TextView priceTv;
            ImageView pictureIv;
            ImageView addIv;
            LinearLayout parentLayout;

            public MyHolder(View itemView) {
                super(itemView);
                nameTv = itemView.findViewById(R.id.item_name_tv);
                priceTv = itemView.findViewById(R.id.item_price_tv);
                pictureIv = itemView.findViewById(R.id.item_picture_iv);
                addIv = itemView.findViewById(R.id.item_add_iv);
                parentLayout = itemView.findViewById(R.id.home_item_parent_lv);
            }
        }
    }

}
