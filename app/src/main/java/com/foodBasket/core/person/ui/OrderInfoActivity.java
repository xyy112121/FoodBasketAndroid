package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.person.model.AddressResModel;
import com.foodBasket.core.person.model.OrderInfoResModel;
import com.foodBasket.core.person.net.OrderAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.mylhyl.circledialog.CircleDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 送货单
 */

public class OrderInfoActivity extends BaseActivity {


    @BindView(R.id.order_comfirm_product_info_ll)
    LinearLayout mProductInfoLl;
    @BindView(R.id.order_comfirm_payType_rg)
    RadioGroup mPayTypeRg;
    @BindView(R.id.order_info_confirm_ll)
    LinearLayout mConfirmLl;

    public static void openActivity(Activity activity, String orderId) {
        Intent intent = new Intent(activity, OrderInfoActivity.class);
        intent.putExtra("orderId", orderId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.bind(this);
        initTopbar("订单详情");
        getData();
    }

    private void getData() {
        LatteLoader.showLoading(mContext);
        OrderAction action = new OrderAction();
        try {
            //购物车
            String id = getIntent().getStringExtra("orderId");
            action.orderInfo(id, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    OrderInfoResModel model = JSON.parseObject(result, OrderInfoResModel.class);
                    if (model != null && model.getSuccess()) {
                        //地址部分
                        AddressResModel address = model.address;
                        if (address != null) {
                            setTextViewValue(R.id.addr_item_contactperson, address.userName);
                            setTextViewValue(R.id.addr_item_contactmobile, address.mobile);
                            setTextViewValue(R.id.addr_item_address, address.province + address.city + address.county + address.address);
                        }

                        setTextViewValue(R.id.order_info_deliveryTime_tv, model.deliveryTime);

                        //商品部分
                        for (OrderInfoResModel.Products item : model.products
                                ) {
                            FrameLayout layout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_order_confirm_item, null);
                            OrderConfirmActivity.ViewHolder holder = new OrderConfirmActivity.ViewHolder(layout);
                            String url = MyApplication.getApplication().mImageUrl + item.headPicture;
                            Glide.with(mContext)
                                    .load(url)
                                    .apply(MyApplication.getOptions())
                                    .into(holder.mPictureIv);
                            holder.mNameTv.setText(item.name);
                            holder.mPriceTv.setText("￥" + item.sumPrice);
                            holder.mCountTv.setText("单价：￥" + item.salePrice + "  数量：" + item.count);
                            mProductInfoLl.addView(layout);
                        }

                        //0："货到付款"; 1： "到店取货"; 2： "欠账订单";
                        if (model.payChannel == 0) {
                            RadioButton btn = (RadioButton) mPayTypeRg.getChildAt(0);
                            btn.setChecked(true);
                        }
                        if (model.payChannel == 1) {
                            RadioButton btn = (RadioButton) mPayTypeRg.getChildAt(2);
                            btn.setChecked(true);
                        }
                        if (model.payChannel == 2) {
                            RadioButton btn = (RadioButton) mPayTypeRg.getChildAt(1);
                            btn.setChecked(true);
                        }

                        setTextViewValue(R.id.order_info_totalPrice_tv, "￥" + model.totalPrice);
                        setTextViewValue(R.id.order_info_delivery_tv, "￥" + model.deliveryPrice);
                        setTextViewValue(R.id.order_info_couponPay_tv, "￥" + model.couponPayPrice);

                        setTextViewValue(R.id.order_info_displayIsPay, model.displayIsPay);
                        setTextViewValue(R.id.order_info_productCount, "共计:" + model.productCount + "款产品");
                        setTextViewValue(R.id.order_info_realPay_price, "￥" + model.realPayPrice);

                        if ("待收货".equals(model.disDeliveryState)) {
                            mConfirmLl.setVisibility(View.VISIBLE);
                        }

                    } else {
                        com.mic.etoast2.Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.order_info_confirm_ll)
    public void onViewClicked() {
        new CircleDialog.Builder((FragmentActivity) mContext)
                .setTitle("提示")
                .setText("请您在确认收到货的时候点击确认收货，请问是否确认收货？")
                .setNegative("取消", null)
                .setPositive("确认收货", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderAction action = new OrderAction();
                        try {
                            String id = getIntent().getStringExtra("orderId");
                            action.confirmReceipt(id, new MyStringCallBack() {
                                @Override
                                public void onResult(String result) {
                                    ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                                    if (model != null && model.getSuccess()) {
                                        showMessage("收货成功！");
                                        finish();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();
    }
}
