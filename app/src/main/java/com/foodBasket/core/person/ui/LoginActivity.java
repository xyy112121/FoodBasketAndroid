package com.foodBasket.core.person.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.foodBasket.BaseTwoActivity;
import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.model.LoginResponseModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 */

public class LoginActivity extends BaseTwoActivity {


    @BindView(R.id.login_phone)
    EditText mPhoneEt;
    @BindView(R.id.login_code)
    EditText mCodeEt;
    @BindView(R.id.login_send_code_btn)
    Button mSendBtn;
    @BindView(R.id.login_btn)
    Button mLoginBtn;

    private MyCount mc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (ShareConfig.getConfigBoolean(mContext, Constants.ONLINE, false)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

        }

        mCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String code = mCodeEt.getText() + "";
                String phone = mPhoneEt.getText() + "";
                if (!"".equals(code) && !"".equals(phone)) {
                    mLoginBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                } else {
                    mLoginBtn.setBackgroundResource(R.drawable.btn_bg_gray);
                }

            }
        });

        mPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String code = mCodeEt.getText() + "";
                String phone = mPhoneEt.getText() + "";
                if (!"".equals(phone)) {
                    mSendBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                } else {
                    mSendBtn.setBackgroundResource(R.drawable.btn_bg_gray);
                }

                if (!"".equals(code) && !"".equals(phone)) {
                    mLoginBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                } else {
                    mLoginBtn.setBackgroundResource(R.drawable.btn_bg_gray);
                }

            }
        });
    }


    @OnClick({R.id.top_left, R.id.login_send_code_btn, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.login_send_code_btn:
                sendCode();
                break;
            case R.id.login_btn:
                login();
                break;
        }
    }

    private void sendCode() {
        mSendBtn.setBackgroundResource(R.drawable.btn_bg_gray);
        final String mobile = mPhoneEt.getText() + "";
//        if (isMobileNO(mobile) == false) {
//            showMessage("请输入正确的手机号码");
//            return;
//        }
        mc = new MyCount(60000, 1000);//倒计时60秒
        mc.start();

        PersonAction action = new PersonAction();
        try {
            action.sendCode(mobile, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    ResponseBean bean = JSON.parseObject(result, ResponseBean.class);
                    if (bean.getSuccess() == false) {
                        mc.cancel();
                        mSendBtn.setText("获取验证码");
                        mSendBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                    }
                }
            });
        } catch (Exception e) {
            showMessage("获取验证码失败");
            mc.cancel();
            mSendBtn.setText("获取验证码");
            mSendBtn.setBackgroundResource(R.drawable.btn_bg_blue);

        }
    }

    /**
     * 验证手机格式 false不正确
     */
    public boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][73458]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (mobiles.equals("")) return false;
        else return mobiles.matches(telRegex);
    }

    private class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mSendBtn.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSendBtn.setText(millisUntilFinished / 1000 + "秒");

        }
    }

    public void login() {
        final String mobile = mPhoneEt.getText() + "";
        String code = mCodeEt.getText() + "";
//        if (isMobileNO(phone) == false) {
//            showMessage("请输入正确的手机号码！");
//            return;re
//        }
//        if ("".equals(code)) {
//            showMessage("验证码不能为空！");
//            return;
//        }

        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.login(mobile, code, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    LoginResponseModel model = JSON.parseObject(result, LoginResponseModel.class);
                    if (model != null && model.getSuccess()) {
                        ShareConfig.setConfig(LoginActivity.this, Constants.ONLINE, true);
                        ShareConfig.setConfig(LoginActivity.this, Constants.USERID, model.user.id);
                        ShareConfig.setConfig(LoginActivity.this, Constants.USERTYPE, model.user.userType);
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            showMessage("登录失败");

        }

    }

}
