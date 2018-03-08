package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.model.AddressResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.ClearEditText;
import com.foodBasket.view.SwitchButton;
import com.foodBasket.view.TextRowView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 收货地址编辑
 */

public class AddressEditActivity extends BaseActivity {

    @BindView(R.id.addr_edit_userName_tv)
    TextRowView mNameTv;
    @BindView(R.id.addr_edit_mobile_tv)
    TextRowView mMobileTv;
    @BindView(R.id.addr_edit_city_tv)
    TextRowView mCityTv;
    @BindView(R.id.addr_edit_address_tv)
    ClearEditText mAddressTv;
    @BindView(R.id.switch1)
    SwitchButton mSwitch;

    private String mProvince;
    private String mCity;
    private String mCounty;

    AddressResModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_edit);
        ButterKnife.bind(this);
        initTopbar("添加地址");
        getData();
    }


    public static void openActivity(Activity activity,String json) {
        Intent intent = new Intent(activity, AddressEditActivity.class);
        intent.putExtra("obj",json);
        activity.startActivity(intent);
    }

    private void getData(){
        String json = getIntent().getStringExtra("obj");
        if(!"".equals(json) || json != null){
            model = JSON.parseObject(json,AddressResModel.class);
            if(model!= null){
                mNameTv.setValueText(model.userName);
                mMobileTv.setValueText(model.mobile);
                mCityTv.setValueText(model.province + model.city + model.county);
                mAddressTv.setText(model.address);

                mProvince = model.province;
                mCity = model.city;
                mCounty = model.county;
                if (model.isDefault){
                    mSwitch.setCurrentStatus(SwitchButton.CLOSE);
                }else {
                    mSwitch.setCurrentStatus(SwitchButton.OPEN);
                }
            }
        }
    }

    @OnClick({R.id.addr_edit_city_tv, R.id.shipping_address_list_add_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addr_edit_city_tv:
                try {
                    ArrayList<Province> data = new ArrayList<>();
                    String json = ConvertUtils.toString(getAssets().open("city.json"));
                    data.addAll(JSON.parseArray(json, Province.class));
                    AddressPicker picker = new AddressPicker(this, data);
                    picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                        @Override
                        public void onAddressPicked(Province province, City city, County county) {
                            mProvince = province.getAreaName();
                            mCity = city.getAreaName();
                            mCounty = county.getAreaName();

                            mCityTv.setValueText(mProvince + mCity + mCounty);

                        }
                    });
                    picker.show();
                } catch (Exception e) {
                }
                break;
            case R.id.shipping_address_list_add_ll:
                String address = mAddressTv.getText() + "";//详细地址
                String mobile = mMobileTv.getValueText() + "";//电话号码
                String userName = mNameTv.getValueText() + "";//用户名
                int defaultAdr = mSwitch.getCurrentStatus();
                try {
                PersonAction action = new PersonAction();
                Map<String, String> params = new HashMap<>();
                String userId = ShareConfig.getConfigString(mContext, Constants.USERID, "");
                params.put("userId", userId);
                params.put("province", mProvince);
                params.put("city", mCity);
                params.put("county", mCounty);
                params.put("address", address);
                params.put("mobile", mobile);
                params.put("userName", userName);
                params.put("defaultAdr", defaultAdr + "");//是否是默认地址  1表示默认  0表示不是

                    LatteLoader.showLoading(mContext);
                if(model != null){
                    params.put("objectID", model.id);

                    action.userAddrEdit(params, new MyStringCallBack() {
                        @Override
                        public void onResult(String result) {
                            LatteLoader.stopLoading();
                            ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                            if (model != null && model.getSuccess()) {
                                showMessage("操作成功");
                                finish();
                            }
                        }
                    });
                }else {
                    action.userAddrAdd(params, new MyStringCallBack() {
                        @Override
                        public void onResult(String result) {
                            LatteLoader.stopLoading();
                            ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                            if (model != null && model.getSuccess()) {
                                showMessage("操作成功");
                                finish();
                            }
                        }
                    });
                }
                } catch (Exception e) {
                    LatteLoader.stopLoading();
                    showMessage("操作失败");
                }
                break;
        }
    }
}
