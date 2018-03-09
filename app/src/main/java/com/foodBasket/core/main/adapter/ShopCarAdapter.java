package com.foodBasket.core.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
import com.foodBasket.core.main.fragment.CartFragment;
import com.foodBasket.core.main.model.OrderDetailid;
import com.foodBasket.core.main.model.ProductInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车
 */

public class ShopCarAdapter extends BaseAdapter {
    private Context mContext;
    CartFragment mFragment;
    public List<OrderDetailid> mSelectIds = new ArrayList<>();
    private List<ProductInfo> mList = new ArrayList<>();
    private boolean mIsShow = false;//不显示多选框

    public ShopCarAdapter(@NonNull Context context, CartFragment fragment) {
        mContext = context;
        mFragment = fragment;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        final ViewHolder cholder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_cart_item, null);
            cholder = new ViewHolder(convertView);
            convertView.setTag(cholder);
        } else {
            cholder = (ViewHolder) convertView.getTag();
        }

        final ProductInfo obj = mList.get(i);
        cholder.mNameTv.setText(obj.getCommodityname());
        cholder.mPriceTv.setText("￥" + obj.getOrderprice() + "元");
        cholder.tv_count.setText(obj.getOrdernumber() + "");
        String url = MyApplication.getApplication().mImageUrl + obj.getCommoditypicture();
        Glide.with(mContext)
                .load(url)
                .apply(MyApplication.getOptions())
                .into(cholder.iv_product_picture);

        if (mIsShow) {
            cholder.cb_check.setVisibility(View.VISIBLE);
        } else {
            cholder.cb_check.setVisibility(View.GONE);
        }
        cholder.cb_check.setChecked(obj.isChoosed());
        cholder.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                obj.setChoosed(isChecked);
                cholder.cb_check.setChecked(isChecked);
            }
        });
        cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = obj.getOrdernumber();
                int c = ++count;
                if (c != 100) {
                    cholder.tv_count.setText(c + "");
                    obj.setOrdernumber(c);
                    Double price = 0.0;
                    for (int i = 0; i < getCount(); i++) {
                        ProductInfo item = mList.get(i);
                        price = price + (item.getOrdernumber() * item.getOrderprice());
                    }
                    mFragment.setPayCount(price);
                } else {
                    com.mic.etoast2.Toast.makeText(mContext, "最大数量为99", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cholder.tv_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!"".equals(s) && s != null) {
                        obj.setOrdernumber(Integer.parseInt(s + ""));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        cholder.iv_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = obj.getOrdernumber();
                if (count > 1) {
                    cholder.tv_count.setText(--count + "");
                    obj.setOrdernumber(count);

                    Double price = 0.0;
                    for (int i = 0; i < getCount(); i++) {
                        ProductInfo item = mList.get(i);
                        price = price + (item.getOrdernumber() * item.getOrderprice());
                    }

                    mFragment.setPayCount(price);

                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductInfoActivity.openActivity((Activity) mContext, obj.getCommodityid());
            }
        });

        return convertView;
    }

    public void addAll(List<ProductInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<ProductInfo> getAll() {
        return mList;
    }

    public void selectAll() {
        mSelectIds.clear();
        if (mList != null && mList.size() > 0) {
            Double price = 0.0;
            for (ProductInfo item : mList) {
                item.setChoosed(true);
                OrderDetailid orderDetailid = new OrderDetailid(item.getOrderdetailid(), item.getOrdernumber());
                mSelectIds.add(orderDetailid);
                int count = item.getOrdernumber();
                price = price + (count * item.getOrderprice()
                );
            }
//            setPayCount(mSelectIds.size(), price);
            notifyDataSetChanged();
        }
    }

    public void showCk(boolean isShow) {
        mIsShow = isShow;
        notifyDataSetChanged();
    }

    public void selectAllNo() {
        if (mList != null && mList.size() > 0) {
            for (ProductInfo item : mList) {
                item.setChoosed(false);
                mSelectIds.clear();
            }
            notifyDataSetChanged();
        }

    }

    public void removeAll() {
        mSelectIds.clear();
        mList.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @BindView(R.id.check_box)
        CheckBox cb_check;
        @BindView(R.id.shop_car_item_picture)
        ImageView iv_product_picture;
        @BindView(R.id.shop_car_item_name)
        TextView mNameTv;
        @BindView(R.id.shop_car_item_price)
        TextView mPriceTv;
        @BindView(R.id.tv_reduce)
        ImageView iv_decrease;
        @BindView(R.id.tv_num)
        TextView tv_count;
        @BindView(R.id.tv_add)
        ImageView iv_increase;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
