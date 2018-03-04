package com.foodBasket.core.person.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
import com.foodBasket.core.goods.view.CartAddPopWin;
import com.foodBasket.core.main.model.ProductListModel;
import com.foodBasket.core.main.model.SearchResModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.net.MyStringCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 商品搜索结果
 */

public class HistorySearchListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    private MyAdapter mAdapter;
    private String mKeyword = "";
    private int mPage = 1;

    @BindView(R.id.main_view)
    LinearLayout mainView;

    WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_search_list);
        ButterKnife.bind(this);
        initTopbar("搜索结果");
        mKeyword = getIntent().getStringExtra("keyword");
        mAdapter = new MyAdapter(mContext, R.layout.activity_history_search_list_item);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(HistorySearchListActivity.this, true));
        mRefreshLayout.beginRefreshing();
    }

    /**
     * 获取数据
     */
    private void getData() {
        HomeAction action = new HomeAction();
        try {

            //特惠
            action.searchProductList(mKeyword, mPage, 14, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    SearchResModel model = JSON.parseObject(result, SearchResModel.class);
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
        //下拉刷新
        mAdapter.clear();
        mPage = 1;
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        //上拉加载更多
        getData();
        return false;
    }

    class MyAdapter extends ArrayAdapter<SearchResModel.Rows> {
        int resourceId;


        public MyAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            resourceId = resource;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resourceId, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SearchResModel.Rows obj = getItem(position);
            holder.mNameTv.setText(obj.name);
            holder.mSummaryTv.setText(obj.summary);
            holder.mPriceTv.setText("￥" + obj.salePrice + "元/" + obj.displayUnit);
            String url = MyApplication.getApplication().mImageUrl + obj.headPicture;
            Glide.with(mContext)
                    .load(url)
                    .apply(MyApplication.getOptions())
                    .into(holder.mPictureIv);
            holder.mAddIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductListModel product = new ProductListModel();
                    product.name = obj.name;
                    product.displayUnit = obj.displayUnit;
                    product.salePrice = obj.salePrice;
                    product.headPicture = obj.headPicture;
                    product.id = obj.id;
                    showPopFormBottom(product);

                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductInfoActivity.openActivity(mContext, obj.id);
                }
            });
            return convertView;
        }


        class ViewHolder {
            @BindView(R.id.item_recommend_picture_iv)
            ImageView mPictureIv;
            @BindView(R.id.item_recommend_name_tv)
            TextView mNameTv;
            @BindView(R.id.item_recommend_summary_tv)
            TextView mSummaryTv;
            @BindView(R.id.item_recommend_price_tv)
            TextView mPriceTv;
            @BindView(R.id.item_recommend_add_iv)
            ImageView mAddIv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }


    }

    public void showPopFormBottom(ProductListModel model) {
        CartAddPopWin takePhotoPopWin = new CartAddPopWin(mContext, model);
//        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(mainView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

    }


}
