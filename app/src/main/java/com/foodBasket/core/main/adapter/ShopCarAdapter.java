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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
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
    private List<OrderDetailid> mSelectIds = new ArrayList<>();
    private List<ProductInfo> mList = new ArrayList<>();
    private boolean mIsShow = false;//不显示多选框

    public ShopCarAdapter(@NonNull Context context) {
        mContext = context;
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
                obj.setChoosed(((CheckBox) v).isChecked());
                cholder.cb_check.setChecked(((CheckBox) v).isChecked());

//                String price = ((TextView) findViewById(R.id.tv_total_price)).getText() + "";
//                Double price2 = Double.valueOf(price);
//                if (((CheckBox) v).isChecked()) {
//                    OrderDetailid orderDetailid = new OrderDetailid(obj.getOrderdetailid(), obj.getOrdernumber(), obj.getCommoditypicture());
//                    mSelectIds.add(orderDetailid);
//                    price2 = price2 + (obj.getOrdernumber() * obj.getOrderprice());
//                } else {
//                    for (int i = 0; i < mSelectIds.size(); i++) {
//                        if (mSelectIds.get(i).getOrderdetailid().equals(obj.getOrderdetailid())) {
////                              list.add(i);
//                            mSelectIds.remove(i);
//                            i--;
//                        }
//                    }
//                    price2 = price2 - (obj.getOrdernumber() * obj.getOrderprice());
//                }
//
////                    for (int i = 0; i < mSelectIds.size(); i++) {
////                        price = price + (mSelectIds.get(i).getOrdernumber() * obj.getOrderprice());
////                    }
//                setPayCount(mSelectIds.size(), price2);
            }
        });
        cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = obj.getOrdernumber();
                cholder.tv_count.setText(++count + "");
                obj.setOrdernumber(count);
                if (obj.isChoosed()) {
                    Double price = 0.0;
                    for (int i = 0; i < mSelectIds.size(); i++) {
                        price = price + (obj.getOrdernumber() * obj.getOrderprice());
                    }
//                    setPayCount(mSelectIds.size(), price);
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
                    if (obj.isChoosed()) {
                        Double price = 0.0;
                        for (int i = 0; i < mSelectIds.size(); i++) {
                            price = price + (obj.getOrdernumber() * obj.getOrderprice());
                        }
//                        setPayCount(mSelectIds.size(), price);
                    }
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
//            setPayCount(mSelectIds.size(), 0.0);
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
        EditText tv_count;
        @BindView(R.id.tv_add)
        ImageView iv_increase;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
