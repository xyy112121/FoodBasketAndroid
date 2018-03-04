package com.foodBasket.core.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.model.GoodsInfoResModel;
import com.foodBasket.core.goods.net.ProductAction;
import com.foodBasket.core.goods.view.CartAddPopWin;
import com.foodBasket.core.main.model.ProductListModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.TextRowView;
import com.foodBasket.view.banner.HolderCreator;
import com.mic.etoast2.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情
 */

public class ProductInfoActivity extends BaseActivity {
    private static final String GOODID = "goodId";
    @BindView(R.id.goods_info_price_tv)
    TextView mPriceTv;
    @BindView(R.id.detail_banner)
    ConvenientBanner mBanner;
    @BindView(R.id.goods_info_attributes_ll)
    LinearLayout mAttributesLl;

    GoodsInfoResModel model;

    WindowManager.LayoutParams params;
    @BindView(R.id.main_view)
    RelativeLayout mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        initTopbar("产品详情");
        getData();
    }

    public static void openActivity(Activity activity, @NonNull String goodId) {
        Intent intent = new Intent(activity, ProductInfoActivity.class);
        intent.putExtra(GOODID, goodId);
        activity.startActivity(intent);
    }

    private void getData() {
        ProductAction action = new ProductAction();
        try {
            String id = getIntent().getStringExtra(GOODID);
            //特惠
            action.productInfo(id, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    model = JSON.parseObject(result, GoodsInfoResModel.class);
                    if (model != null && model.getSuccess()) {
                        initBanner(model.pictures);//广告条
                        GoodsInfoResModel.ProductBasic basic = model.productBasic;
                        if (basic != null) {
                            ((TextView) findViewById(R.id.goods_info_name_tv)).setText(basic.name);
                            ((TextView) findViewById(R.id.goods_info_detail_tv)).setText(basic.detail);
                            ((TextView) findViewById(R.id.goods_info_price_tv)).setText("￥" + basic.salePrice + "/" + basic.displayUnit);
                        }
                        if (model.attributes != null) {
                            for (GoodsInfoResModel.Attributes item : model.attributes) {
                                LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_goods_info_tv, null);
                                TextRowView tv = layout.findViewById(R.id.goods_info_text);
                                tv.setLeftext(item.productAttribute_name);
                                tv.setValueText(item.basicValue);
                                mAttributesLl.addView(layout);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBanner(List<GoodsInfoResModel.Pictures> list) {
//        final JSONArray array = data.getJSONArray("banners");
        final List<String> images = new ArrayList<>();
//        final int size = array.size();
        for (int i = 0; i < list.size(); i++) {
            String url = MyApplication.getApplication().mImageUrl + list.get(i).picture_pictureUrl;
            images.add(url);
        }
        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }

    @OnClick({R.id.tv_go_to_pay, R.id.tv_go_to_pay_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_go_to_pay:
                ProductListModel product = new ProductListModel();
                GoodsInfoResModel.ProductBasic basic = model.productBasic;
                product.name = basic.name;
                product.displayUnit = basic.displayUnit;
                product.salePrice = basic.salePrice;
                if(model.pictures != null && model.pictures.size() >0){
                    product.headPicture = model.pictures.get(0).picture_pictureUrl;
                }
                product.id = basic.id;


                showPopFormBottom(product);
                break;
            case R.id.tv_go_to_pay_layout:
                break;
        }
    }

    /**
     * 加入购物车
     */
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
