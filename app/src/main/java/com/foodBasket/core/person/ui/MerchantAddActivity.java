package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.TextRowView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我是商户
 */

public class MerchantAddActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.merchant_add_realname_tv)
    TextRowView mRealNameTv;
    @BindView(R.id.merchant_add_name_tv)
    TextRowView mMerchantNameTv;


    public static void openActivity(Activity activity, String realName, String merchantName) {
        Intent intent = new Intent(activity, MerchantAddActivity.class);
        intent.putExtra("realName", realName);
        intent.putExtra("merchantName", merchantName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add);
        ButterKnife.bind(this);
        initTopbar("添加商户信息", "保存", this);

        String realName = getIntent().getStringExtra("realName");
        String merchantName = getIntent().getStringExtra("merchantName");

        mRealNameTv.setValueText(realName);
        mMerchantNameTv.setValueText(merchantName);
    }


    @Override
    public void onClick(View view) {
        String realName = mRealNameTv.getValueText();
        String merchantName = mMerchantNameTv.getValueText();

        if ("".equals(realName) || realName == null) {
            showMessage("请输入真实姓名");
            return;
        }

        if ("".equals(merchantName) || merchantName == null) {
            showMessage("请输入餐馆名称");
            return;
        }

        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.merchantAdd(mContext, realName, merchantName, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                    if (model != null && model.getSuccess()) {
                        showMessage(model.getResultInfo());
                    } else {
                        showMessage("操作失败！");
                    }

                }
            });
        } catch (Exception e) {
            showMessage("操作失败！");
            LatteLoader.stopLoading();
        }
    }
}
