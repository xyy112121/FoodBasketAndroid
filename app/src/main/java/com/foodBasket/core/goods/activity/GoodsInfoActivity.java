package com.foodBasket.core.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.view.banner.HolderCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品详情
 */

public class GoodsInfoActivity extends BaseActivity {
    private static final String GOODID = "goodId";
    @BindView(R.id.goods_info_price_tv)
    TextView mPriceTv;
    @BindView(R.id.detail_banner)
    ConvenientBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        initTopbar("产品详情");
    }

    public static void openActivity(Activity activity, @NonNull String goodId) {
        Intent intent = new Intent(activity, GoodsInfoActivity.class);
        intent.putExtra(GOODID, goodId);
        activity.startActivity(intent);
    }

    private void initBanner(JSONObject data) {
        final JSONArray array = data.getJSONArray("banners");
        final List<String> images = new ArrayList<>();
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            images.add(array.getString(i));
        }
        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }
}
