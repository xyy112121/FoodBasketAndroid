package com.foodBasket.core.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
import com.foodBasket.core.goods.view.CartAddPopWin;
import com.foodBasket.core.main.model.CategoryResModel;
import com.foodBasket.core.main.model.CategoryRowsModel;
import com.foodBasket.core.main.model.ProductDiscountModelRes;
import com.foodBasket.core.main.model.ProductListModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.grantland.widget.AutofitTextView;

/**
 * 首页
 */

public class TypeListFragment extends Fragment {
    LayoutInflater mInflater;
    @BindView(R.id.home_list_type_lv)
    ListView mTypeLv;
    @BindView(R.id.home_list_value_lv)
    ListView mValueLv;

    @BindView(R.id.main_view)
    LinearLayout mainView;
    WindowManager.LayoutParams params;

    Unbinder unbinder;

    TypeAdapter mTypeAdapter;
    ValueAdapter mValueAdapter;

    public String mTypeId;//大分类选中的
    private String mSeleteId = "";//小分类选中的

    private int last_index;
    private int total_index;

    private int mPgae = 1;
    private int mTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View mView = mInflater.inflate(R.layout.fragment_home_list, null);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        getTypeData();
        return mView;
    }

    private void getTypeData() {
        //获取小分类
        HomeAction action = new HomeAction();
        try {
            action.categorySmallList(mTypeId, 1, 40, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    CategoryResModel model = JSON.parseObject(result, CategoryResModel.class);
                    if (model != null && model.getSuccess()) {
                        if (model.rows.size() > 0) {
                            mSeleteId = model.rows.get(0).id;
                            getValueData();
                        }
                        mTypeAdapter.addAll(model.rows);
                    }
                }
            });
        } catch (Exception e) {
            LatteLoader.stopLoading();
        }
    }

    private void initView() {
        mTypeAdapter = new TypeAdapter(getActivity(), R.layout.item_type_list_type_item);
        mTypeLv.setAdapter(mTypeAdapter);
        mValueAdapter = new ValueAdapter(getActivity(), R.layout.fragment_home_list_value_item);
        mValueLv.setAdapter(mValueAdapter);

        mValueLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (mTotal == mValueAdapter.getCount() && last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
                    LatteLoader.showLoading(getActivity());
                    getValueData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                last_index = firstVisibleItem + visibleItemCount;
                total_index = totalItemCount;
            }
        });


    }

    public void getValueData() {
        HomeAction action = new HomeAction();
        try {
            action.productBasicList(mSeleteId, mPgae++, 25, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    ProductDiscountModelRes model = JSON.parseObject(result, ProductDiscountModelRes.class);
                    mTotal = model.total;
                    mValueAdapter.addAll(model.rows);
                    LatteLoader.stopLoading();
                }
            });
        } catch (Exception e) {
            LatteLoader.stopLoading();
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class TypeAdapter extends ArrayAdapter<CategoryRowsModel> {

        public TypeAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            convertView = mInflater.inflate(R.layout.item_type_list_type_item, null);
            holder = new ViewHolder(convertView);
            holder.mTypeCk.setText(getItem(position).name);
            holder.mTypeCk.setTag(getItem(position).id);
            if (getItem(position).id.equals(mSeleteId)) {
                holder.mTypeCk.setChecked(true);
            } else {
                holder.mTypeCk.setChecked(false);
            }
            holder.mTypeCk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSeleteId = view.getTag() + "";
                    notifyDataSetChanged();
                    LatteLoader.showLoading(getActivity());
                    mValueAdapter.clear();
                    mPgae = 1;
                    getValueData();
                }
            });
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.home_list_type_ck)
            CheckBox mTypeCk;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public class ValueAdapter extends ArrayAdapter<ProductListModel> {

        public ValueAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_type_list_value_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProductListModel model = getItem(position);
            holder.mNameTv.setText(model.name);
            int userType = ShareConfig.getConfigInt(getActivity(), Constants.USERTYPE, 0);
            int price = model.salePrice;
            if (userType == 2) {
                price = model.merchantPrice;
            }
            holder.mPriceTv.setText("￥" + price + "元/" + model.displayUnit);
            String url = MyApplication.getApplication().mImageUrl + model.headPicture;
            Glide.with(getActivity())
                    .load(url)
                    .apply(MyApplication.getOptions())
                    .into(holder.mPictrueIv);
            holder.mSummaryTv.setText(model.summary);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductInfoActivity.openActivity(getActivity(), model.id);
                }
            });

            if (userType == 1) {
                holder.mAddIv.setVisibility(View.GONE);
            }

            holder.mAddIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopFormBottom(model);
                }
            });
            return convertView;
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

        class ViewHolder {
            @BindView(R.id.type_value_pictrue_iv)
            ImageView mPictrueIv;
            @BindView(R.id.type_value_name_tv)
            AutofitTextView mNameTv;
            @BindView(R.id.type_value_summary_tv)
            TextView mSummaryTv;
            @BindView(R.id.type_value_price_tv)
            TextView mPriceTv;
            @BindView(R.id.type_value_add_iv)
            ImageView mAddIv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
