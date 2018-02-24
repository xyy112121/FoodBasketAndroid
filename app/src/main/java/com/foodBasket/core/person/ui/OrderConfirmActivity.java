package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.person.model.AddressResModel;
import com.foodBasket.core.person.model.OrderConfirmResModel;
import com.foodBasket.core.person.net.OrderAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.mylhyl.circledialog.CircleDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * 确认订单
 */

public class OrderConfirmActivity extends BaseActivity {
    OrderConfirmResModel mObj;
    @BindView(R.id.order_comfirm_addr_tv)
    TextView mAddrTitleTv;
    @BindView(R.id.order_comfirm_addr_ll)
    LinearLayout mAddrLl;
    @BindView(R.id.addr_item_contactperson)
    TextView mContactPersonTv;
    @BindView(R.id.addr_item_contactmobile)
    TextView mContactMobileTv;
    @BindView(R.id.addr_item_address)
    TextView mAddressTv;

    @BindView(R.id.order_comfirm_product_info_ll)
    LinearLayout mProductInfoLl;
    @BindView(R.id.order_comfirm_totalPrice_tv)
    TextView mTotalPriceTv;
    @BindView(R.id.order_comfirm_delivery_tv)
    TextView mDeliveryTv;
    @BindView(R.id.order_comfirm_couponPay_tv)
    TextView mCouponPayTv;
    @BindView(R.id.order_comfirm_realPay_price)
    TextView mRealPayPriceTv;
    @BindView(R.id.order_comfirm_productCount)
    TextView mCountTv;
    @BindView(R.id.order_comfirm_deliveryTime_tv)
    TextView mDeliveryTimeTv;


    @BindView(R.id.tv_go_to_pay)
    TextView tvGoToPay;
    @BindView(R.id.tv_go_to_pay_layout)
    LinearLayout tvGoToPayLayout;
    @BindView(R.id.order_comfirm_payType_rg)
    RadioGroup mPayTypeRg;

    //提交的值
    //付款方式 0："货到付款"; 1： "到店取货"; 2： "欠账订单";
    String payChannel = "0";
    String mTime;
    String mAddressId;


    public static void openActivity(Activity activity, String info, String products) {
        Intent intent = new Intent(activity, OrderConfirmActivity.class);
        intent.putExtra("info", info);
        intent.putExtra("products", products);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        initTopbar("确定订单");
        String info = getIntent().getStringExtra("info");
        mObj = JSON.parseObject(info, OrderConfirmResModel.class);

        if (mObj.address == null) {
            mAddrTitleTv.setVisibility(View.VISIBLE);
            mAddrLl.setVisibility(View.GONE);
        } else {
            mAddrLl.setVisibility(View.VISIBLE);
            mAddrTitleTv.setVisibility(View.GONE);
            mContactMobileTv.setText(mObj.address.mobile);
            mContactPersonTv.setText(mObj.address.userName);
            mAddressTv.setText(mObj.address.province + mObj.address.city + mObj.address.county + mObj.address.address);
            mAddressId = mObj.address.id;
        }

        mTotalPriceTv.setText("￥" + mObj.totalPrice);
        mDeliveryTv.setText("￥" + mObj.deliveryPrice);
        mCouponPayTv.setText("￥" + mObj.couponPayPrice);
        mRealPayPriceTv.setText("￥" + mObj.realPayPrice);

        mCountTv.setText("共计:" + mObj.products.size() + "款产品");


        for (OrderConfirmResModel.Products item : mObj.products
                ) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_order_confirm_item, null);
            ViewHolder holder = new ViewHolder(layout);
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

        mPayTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioGroupID1:
                        payChannel = "0";
                        break;
                    case R.id.radioGroupID2:
                        payChannel = "2";
                        break;
                    case R.id.radioGroupID3:
                        payChannel = "1";
                        break;
                }

            }
        });

    }

    @OnClick({R.id.order_comfirm_parent_rl, R.id.tv_go_to_pay, R.id.tv_go_to_pay_layout, R.id.order_comfirm_deliveryTime_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_comfirm_parent_rl:
                Intent intent = new Intent(mContext, AddressListActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.tv_go_to_pay:
                if ("".equals(mTime)) {
                    showMessage("请选择送达时间！");
                    return;
                }
                new CircleDialog.Builder((FragmentActivity) mContext)
                        .setTitle("提示")
                        .setText("您实际需要支付 ￥" + mObj.realPayPrice + "元,确定订单吗？")
                        .setNegative("取消", null)
                        .setPositive("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderAction action = new OrderAction();
                                try {
                                    String products = getIntent().getStringExtra("products");
                                    action.confirmOrder(mContext, products, mAddressId, payChannel, mTime, new MyStringCallBack() {
                                        @Override
                                        public void onResult(String result) {
                                            ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                                            if (model != null && model.getSuccess()) {
                                                showMessage("下单成功！");
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

                break;
            case R.id.order_comfirm_deliveryTime_tv:
                DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
                Calendar a = Calendar.getInstance();
                picker.setDateRangeStart(a.get(Calendar.YEAR), 1, 1);
                picker.setDateRangeEnd(a.get(Calendar.YEAR) + 5, 11, 11);
                picker.setTimeRangeStart(9, 0);
                picker.setTimeRangeEnd(20, 30);
                picker.setTopLineColor(0x99FF0000);
                picker.setDividerColor(0xFFFF0000);
                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        mDeliveryTimeTv.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                        mTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    }
                });
                picker.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            mAddrLl.setVisibility(View.VISIBLE);
            mAddrTitleTv.setVisibility(View.GONE);
            String addr = data.getStringExtra("addr");
            AddressResModel model = JSON.parseObject(addr, AddressResModel.class);
            mAddressId = model.id;
            mContactMobileTv.setText(model.mobile);
            mContactPersonTv.setText(model.userName);
            mAddressTv.setText(model.province + model.city + model.county + model.address);
        }
    }

    static class ViewHolder {
        @BindView(R.id.product_picture_iv)
        ImageView mPictureIv;
        @BindView(R.id.product_name_tv)
        TextView mNameTv;
        @BindView(R.id.product_price_tv)
        TextView mPriceTv;
        @BindView(R.id.product_count_tv)
        TextView mCountTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
