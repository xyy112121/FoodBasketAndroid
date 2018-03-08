package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.main.model.UserResModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.model.VersionResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.AppUpdateUtils;
import com.foodBasket.util.AppVersion;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.TextRowView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mic.etoast2.Toast;
import com.mylhyl.circledialog.CircleDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人信息
 * /
 */

public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.person_info_avatar)
    RoundedImageView mAvatarIv;
    @BindView(R.id.person_info_name)
    TextRowView mNameTv;
    @BindView(R.id.person_info_mobile)
    TextRowView mMobileTv;
    @BindView(R.id.person_info_waitingReceive)
    TextRowView mWaitingReceiveTv;
    @BindView(R.id.person_info_parent_ll)
    LinearLayout mParentLl;

    private List<LocalMedia> mSelectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initTopbar("基本信息设置");
        int userType = ShareConfig.getConfigInt(mContext, Constants.USERTYPE, 0);
        if (userType == 1) {
            mParentLl.setVisibility(View.GONE);
        }
        getData();
    }

    private void getData() {
        HomeAction action = new HomeAction();
        try {
            action.user(mContext, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    UserResModel model = JSON.parseObject(result, UserResModel.class);
                    if (model != null && model.getSuccess()) {
                        if (model.user != null) {
                            String url = MyApplication.getApplication().mImageUrl + model.user.avatar;
                            Glide.with(mContext)
                                    .load(url)
                                    .apply(MyApplication.getOptions())
                                    .into(mAvatarIv);
                            mNameTv.setValueText(model.user.realName);
                            mMobileTv.setValueText(model.user.mobile);
                            mWaitingReceiveTv.setValueText(model.waitingReceive + "");
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, PersonInfoActivity.class);
        activity.startActivity(intent);
    }


    @OnClick({R.id.person_info_avatar_rl, R.id.person_info_out, R.id.person_info_waitingReceive, R.id.set_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_info_avatar_rl://头像
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                        .compress(true)
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .selectionMedia(mSelectList)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.person_info_out://退出
                new CircleDialog.Builder((FragmentActivity) mContext)
                        .setTitle("提示")
                        .setText("确定退出登录么？")
                        .setNegative("取消", null)
                        .setPositive("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShareConfig.setConfig(mContext, Constants.ONLINE, false);
                                startActivity(new Intent(mContext, LoginActivity.class));
                                finish();
                            }
                        })
                        .show();

                break;
            case R.id.person_info_waitingReceive:
                MerchantListActivity.openActivity(mContext);
                break;
            case R.id.set_update:
                getUpdateInfo();
                break;

        }
    }

    //检测更新
    private void getUpdateInfo() {

        LatteLoader.showLoading(mContext);
        try {
            PersonAction action = new PersonAction();
            //购物车
            action.getVersion(new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    VersionResModel model = JSON.parseObject(result, VersionResModel.class);
                    if (model != null) {
                        if (model.getSuccess() && model.version != null) {
                            VersionResModel.Version info = model.version;
                            if(info.filePath != null && !"".equals(info.filePath)){
                                AppVersion av = new AppVersion();
                                av.setApkName("foodBasket.apk");
                                av.setUrl(info.filePath);
                                av.setContent(info.memo);
//                            av.setVerCode(info.version);
                                av.setVersionName(info.version);
                                //不强制升级
                                AppUpdateUtils.init(mContext, av, true, false, true);
                                AppUpdateUtils.upDate();
                            }else {
                                Toast.makeText(mContext, "当前版本已经是最新版本！", android.widget.Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    mSelectList = PictureSelector.obtainMultipleResult(data);
                    String url = "file://" + mSelectList.get(0).getCompressPath();
                    Glide.with(mContext).load(url).apply(MyApplication.getOptions())
                            .into(mAvatarIv);

                    LatteLoader.showLoading(mContext);
                    PersonAction action = new PersonAction();
                    try {
                        File file = new File(mSelectList.get(0).getCompressPath());
                        action.uploadAvatar(mContext, file, new MyStringCallBack() {
                            @Override
                            public void onResult(String result) {
                                LatteLoader.stopLoading();
                            }
                        });
                    } catch (Exception e) {
                        showMessage("添加失败！");
                        LatteLoader.stopLoading();
                    }
                    break;

            }
        }
    }
}
