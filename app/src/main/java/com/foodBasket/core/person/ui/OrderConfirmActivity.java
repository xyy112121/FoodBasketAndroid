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
import com.foodBasket.util.NumberUtil;
import com.mylhyl.circledialog.CircleDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String payChannel = "-1";
    private String mTime;
    private String mAddressId;
    private String mCounty;


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

        if (mObj.address != null && mObj.address.province != null) {
            mAddrLl.setVisibility(View.VISIBLE);
            mAddrTitleTv.setVisibility(View.GONE);
            mContactMobileTv.setText(mObj.address.mobile);
            mContactPersonTv.setText(mObj.address.userName);
            mAddressTv.setText(mObj.address.province + mObj.address.city + mObj.address.county + mObj.address.address);
            mAddressId = mObj.address.id;
            mCounty = mObj.address.county;
        } else {
            mAddrTitleTv.setVisibility(View.VISIBLE);
            mAddrLl.setVisibility(View.GONE);
        }

        mTotalPriceTv.setText("￥" + NumberUtil.decimalFormat(mObj.totalPrice));
        mDeliveryTv.setText("￥" + NumberUtil.decimalFormat(mObj.deliveryPrice));
        mCouponPayTv.setText("￥" + NumberUtil.decimalFormat(mObj.couponPayPrice));
        mRealPayPriceTv.setText("￥" + NumberUtil.decimalFormat(mObj.realPayPrice));

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
            holder.mPriceTv.setText("￥" + NumberUtil.decimalFormat(item.sumPrice));
            holder.mCountTv.setText("单价：￥" + NumberUtil.decimalFormat(item.salePrice) + "  数量：" + item.count);
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
                AddressListActivity.openActivity(mContext, true, 1000);
                break;
            case R.id.tv_go_to_pay:

                if ("-1".equals(payChannel)) {
                    showMessage("请选择付款方式！");
                    return;
                }
                if ("".equals(mTime) || mTime == null) {
                    showMessage("请选择送达时间！");
                    return;
                }

                if (mAddressId == null || "".equals(mAddressId)) {
                    showMessage("请选择收货地址！");
                    return;
                }

                if (!"西山区".equals(mCounty) && !"高新区".equals(mCounty)) {
                    if (mObj.totalPrice < 500) {
                        showDialog("对不起，我们当前区的配送设施正在紧张的建设之中，暂时不能下单，给您带来不便，敬请谅解！");
                    }
                    return;
                }

                new CircleDialog.Builder((FragmentActivity) mContext)
                        .setTitle("提示")
                        .setText("您实际需要支付 ￥" + mObj.realPayPrice + "元,\n        确定提交订单吗？")
                        .setTextColor(getResources().getColor(R.color.black))
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
                                            if (model != null) {
                                                showMessage(model.getResultInfo());
                                                if (model.getSuccess()) {
                                                    finish();
                                                }

                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    showMessage("确认订单失败，请稍后重试！");
                                }
                            }
                        })
                        .show();

                break;
            case R.id.order_comfirm_deliveryTime_tv:
                DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
                Calendar a = Calendar.getInstance();
                picker.setDateRangeStart(a.get(Calendar.YEAR), (a.get(Calendar.MONTH)) + 1, a.get(Calendar.DAY_OF_MONTH));
                picker.setTimeRangeStart(a.get(Calendar.HOUR_OF_DAY), a.get(Calendar.MINUTE));
                picker.setTopLineColor(0x99FF0000);
                picker.setDividerColor(0xFFFF0000);

                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        isTime(time);
                    }
                });
                picker.show();
                break;
        }
    }

    private void showDialog(String text) {
        new CircleDialog.Builder((FragmentActivity) mContext)
                .setTitle("提示")
                .setText(text)
                .setTextColor(getResources().getColor(R.color.black))
                .setPositive("确定", null)
                .show();
    }

    public void isTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            startTime = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        long between = (startTime.getTime() - curDate.getTime()) / 3600;
        if (between < 1) {//时间小于一小时
            showDialog("对不起，由于我们需要备货，为您送达货物时间必须大于一小时，为您带来不便，敬请谅解！");
        } else {
            mTime = time;
            mDeliveryTimeTv.setText(mTime);
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
            mCounty = model.county;
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
