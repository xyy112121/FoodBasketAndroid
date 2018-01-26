package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.mylhyl.circledialog.CircleDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人信息
 * /
 */

public class PersonInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initTopbar("基本设置");
    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, PersonInfoActivity.class);
        activity.startActivity(intent);
    }


    @OnClick({R.id.person_info_header_layout, R.id.person_info_addr_tv, R.id.person_info_out, R.id.person_info_identification_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_info_header_layout://头像
                break;
            case R.id.person_info_addr_tv://地址
                AddressListActivity.openActivity(mContext);
                break;
            case R.id.person_info_out://退出
                new CircleDialog.Builder((FragmentActivity) mContext)
                        .setTitle("提示")
                        .setText("确定退出登录么？")
                        .setNegative("取消", null)
                        .setPositive("确定", null)
                        .show();

                break;
            case R.id.person_info_identification_tv:
                IdentificationActivity.openActivity(mContext);
                break;
        }
    }
}
