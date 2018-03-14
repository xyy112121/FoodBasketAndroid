package com.foodBasket.core.person.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.foodBasket.BaseTwoActivity;
import com.foodBasket.MainActivity;
import com.foodBasket.R;
import com.foodBasket.RequestPermissionCallBack;
import com.foodBasket.core.person.model.LoginResponseModel;
import com.foodBasket.core.person.model.VersionResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;
import com.mic.etoast2.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.UpdateAppUtils;

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
    @BindView(R.id.login_user_ll)
    LinearLayout mUserLl;
    @BindView(R.id.login_deliveryman_phone)
    EditText mDeliverymanPhone;
    @BindView(R.id.login_deliveryman_code)
    EditText mDeliverymanCode;
    @BindView(R.id.login_deliveryman_ll)
    LinearLayout mDeliverymanLl;
    @BindView(R.id.login_switch)
    TextView mLoginSwitch;
    @BindView(R.id.login_deliveryman_btn)
    Button mDeliverymanBtn;

    private MyCount mc;

    private boolean mFlag = true;//判断当前是用户登录还是送货员登录  true是用户

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (ShareConfig.getConfigBoolean(mContext, Constants.ONLINE, false)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(mContext, permissions, new RequestPermissionCallBack() {
                @Override
                public void granted() {
                    getUpdateInfo();
                }

                @Override
                public void denied() {
                    Toast.makeText(mContext, "请开启权限，否则可能导致不能更新！", android.widget.Toast.LENGTH_SHORT).show();
                }
            });

        }

        mDeliverymanPhone.addTextChangedListener(new DeliverymanTextWatcher());
        mDeliverymanCode.addTextChangedListener(new DeliverymanTextWatcher());

        mCodeEt.addTextChangedListener(new UserTextWatcher());
        mPhoneEt.addTextChangedListener(new UserTextWatcher());
    }

    private class UserTextWatcher implements TextWatcher {

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
            if (getCurrentFocus().getId() != R.id.login_code) {
                if (!"".equals(phone)) {
                    mSendBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                } else {
                    mSendBtn.setBackgroundResource(R.drawable.btn_bg_gray);
                }

            }
            if (!"".equals(code) && !"".equals(phone)) {
                mLoginBtn.setBackgroundResource(R.drawable.btn_bg_blue);
            } else {
                mLoginBtn.setBackgroundResource(R.drawable.btn_bg_gray);
            }
        }
    }

    private class DeliverymanTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String code = mDeliverymanCode.getText() + "";
            String phone = mDeliverymanPhone.getText() + "";
            if (!"".equals(code) && !"".equals(phone)) {
                mDeliverymanBtn.setBackgroundResource(R.drawable.btn_bg_blue);
            } else {
                mDeliverymanBtn.setBackgroundResource(R.drawable.btn_bg_gray);
            }
        }
    }


    @OnClick({R.id.login_send_code_btn, R.id.login_btn, R.id.login_switch, R.id.login_deliveryman_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_send_code_btn:
                sendCode();
                break;
            case R.id.login_btn:
                userlogin();
                break;
            case R.id.login_switch:
                if (mFlag) {//当前是用户切换到送货员
                    mLoginSwitch.setText("切换用户登录");
                    mFlag = false;
                    mUserLl.setVisibility(View.GONE);
                    mDeliverymanLl.setVisibility(View.VISIBLE);
                    mLoginBtn.setVisibility(View.GONE);
                    mDeliverymanBtn.setVisibility(View.VISIBLE);
                } else {
                    mLoginSwitch.setText("切换送货员登录");
                    mFlag = true;
                    mUserLl.setVisibility(View.VISIBLE);
                    mDeliverymanLl.setVisibility(View.GONE);
                    mLoginBtn.setVisibility(View.VISIBLE);
                    mDeliverymanBtn.setVisibility(View.GONE);
                }
                break;
            case R.id.login_deliveryman_btn:
                deliverymanlogin();
                break;
        }
    }

    private void sendCode() {
        mSendBtn.setBackgroundResource(R.drawable.btn_bg_gray);
        final String mobile = mPhoneEt.getText() + "";
        if (isMobileNO(mobile) == false) {
            showMessage("请输入正确的手机号码");
            return;
        }
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
            mSendBtn.setBackgroundResource(R.drawable.btn_bg_blue);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSendBtn.setText(millisUntilFinished / 1000 + "秒");

        }
    }

    private void userlogin() {
        final String mobile = mPhoneEt.getText() + "";
        String code = mCodeEt.getText() + "";
        if (isMobileNO(mobile) == false) {
            showMessage("请输入正确的手机号码！");
            return;
        }
        if ("".equals(code)) {
            showMessage("验证码不能为空！");
            return;
        }

        login(mobile, code);
    }

    private void deliverymanlogin() {
        final String mobile = mDeliverymanPhone.getText() + "";
        String code = mDeliverymanCode.getText() + "";
        if ("".equals(mobile)) {
            showMessage("请输入手机号码！");
            return;
        }
        if ("".equals(code)) {
            showMessage("请输入密码！");
            return;
        }
        String codeMd5 = EncryptUtils.encryptMD5ToString(code);
        login(mobile, codeMd5);

    }

    private void login(String mobile, String code) {
        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.login(mobile, code, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    LoginResponseModel model = JSON.parseObject(result, LoginResponseModel.class);
                    if (model != null) {
                        if (model.getSuccess()) {
                            ShareConfig.setConfig(LoginActivity.this, Constants.ONLINE, true);
                            ShareConfig.setConfig(LoginActivity.this, Constants.USERID, model.user.id);
                            ShareConfig.setConfig(LoginActivity.this, Constants.USERTYPE, model.user.userType);
                            ShareConfig.setConfig(LoginActivity.this, Constants.NICENAME, model.user.userLogin);
                            ShareConfig.setConfig(LoginActivity.this, Constants.NAME, model.user.realName);
                            ShareConfig.setConfig(LoginActivity.this, Constants.AVATER, model.user.avatar);
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(mContext, model.getResultInfo(), android.widget.Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            });
        } catch (Exception e) {
            showMessage("登录失败");
            LatteLoader.stopLoading();
        }
    }

    //检测更新
    private void getUpdateInfo() {

        LatteLoader.showLoading(mContext);
        try {
            PersonAction action = new PersonAction();
            action.getVersion(new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    VersionResModel model = JSON.parseObject(result, VersionResModel.class);
                    if (model != null) {
                        if (model.getSuccess() && model.version != null) {
                            VersionResModel.Version info = model.version;
                            if(info.filePath != null && !"".equals(info.filePath)){
                                UpdateAppUtils.from(mContext)
                                        .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                                        .serverVersionCode(1)
                                        .serverVersionName(info.version)
                                        .apkPath(info.filePath)
                                        .updateInfo(info.memo)  //更新日志信息 String
                                        .update();
                            }

                        } else {
                            Toast.makeText(mContext, model.getResultInfo(), android.widget.Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            LatteLoader.stopLoading();
        }


    }

}
