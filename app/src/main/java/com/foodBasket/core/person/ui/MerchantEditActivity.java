package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.BaseActivity;
import com.foodBasket.MainActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.goods.model.GoodsInfoResModel;
import com.foodBasket.core.goods.net.ProductAction;
import com.foodBasket.core.person.model.LoginResponseModel;
import com.foodBasket.core.person.model.MerchantInfoResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.TextRowView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 餐馆编辑
 */

public class MerchantEditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.merchant_edit_name_tv)
    TextRowView mNameTv;
    @BindView(R.id.merchant_edit_addr_tv)
    TextRowView mAddrTv;
    @BindView(R.id.merchant_edit_file1)
    ImageView mFile1Iv;
    @BindView(R.id.merchant_edit_file2)
    ImageView mFile2Iv;
    @BindView(R.id.merchant_edit_file3)
    ImageView mFile3Iv;

    private List<LocalMedia> mSelectList1 = new ArrayList<>();
    private List<LocalMedia> mSelectList2 = new ArrayList<>();
    private List<LocalMedia> mSelectList3 = new ArrayList<>();

    private int mFileFlag = 1;

    private static final String OBJECTID = "objectId";


    public static void openActivity(Activity activity,String id) {
        Intent intent = new Intent(activity, MerchantEditActivity.class);
        intent.putExtra(OBJECTID,id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_edit);
        ButterKnife.bind(this);
        initTopbar("餐馆信息", "保存", this);
        String id = getIntent().getStringExtra(OBJECTID);
        if(id != null && !"".equals(id)){
            getData(id);
        }
    }

    private void getData(String id) {
        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.merchantInfo(id, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    MerchantInfoResModel model = JSON.parseObject(result,MerchantInfoResModel.class);
                   if(model != null && model.getSuccess()){
                       MerchantInfoResModel.Merchant merchant = model.merchant;
                       mNameTv.setValueText(merchant.name);
                       mAddrTv.setValueText(merchant.address);

                       String licenseUrl =MyApplication.getApplication().mImageUrl+merchant.license;
                       String cardReverseUrl =MyApplication.getApplication().mImageUrl+merchant.cardReverse;
                       String cardPositiveUrl =MyApplication.getApplication().mImageUrl+merchant.cardPositive;//正面

                       Glide.with(mContext).load(cardPositiveUrl).apply(MyApplication.getOptions()).into(mFile1Iv);
                       Glide.with(mContext).load(cardReverseUrl).apply(MyApplication.getOptions()).into(mFile2Iv);
                       Glide.with(mContext).load(licenseUrl).apply(MyApplication.getOptions()).into(mFile3Iv);

                   }else {
                       showMessage("获取失败！");
                   }
                }
            });
        } catch (Exception e) {
           showMessage("获取失败！");
        }
    }

    @Override
    public void onClick(View view) {
        String name = mNameTv.getValueText();
        String addr = mAddrTv.getValueText();
        List<File> files = new ArrayList<>();
        if (mSelectList1.size() > 0) {
            File file = new File(mSelectList1.get(0).getCompressPath());
            files.add(file);
        } else {
            showMessage("请选择身份证正反面图片");
            return;
        }

        if (mSelectList2.size() > 0) {
            File file = new File(mSelectList2.get(0).getCompressPath());
            files.add(file);
        } else {
            showMessage("请选择身份证正反面图片");
            return;
        }

        if (mSelectList3.size() > 0) {
            File file = new File(mSelectList3.get(0).getCompressPath());
            files.add(file);
        } else {
            showMessage("请选择营业执照图片");
            return;
        }

        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.merchantEdit(mContext, name, addr, files, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                    if (model != null && model.getSuccess()) {
                        finish();
                        showMessage("添加成功！");
                    }

                }
            });
        } catch (Exception e) {
            showMessage("添加失败！");
            LatteLoader.stopLoading();
        }
    }

    @OnClick({R.id.merchant_edit_file1, R.id.merchant_edit_file2, R.id.merchant_edit_file3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.merchant_edit_file1:
                mFileFlag = 1;
                openImage(mSelectList1);
                break;
            case R.id.merchant_edit_file2:
                mFileFlag = 2;
                openImage(mSelectList2);
                break;
            case R.id.merchant_edit_file3:
                mFileFlag = 3;
                openImage(mSelectList3);
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openImage(List<LocalMedia> selectList) {
        // 进入相册
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .compress(true)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    ImageView view;
                    String url;
                    // 图片选择结果回调
                    if (mFileFlag == 1) {
                        mSelectList1 = PictureSelector.obtainMultipleResult(data);
                        view = mFile1Iv;
                        url = "file://" + mSelectList1.get(0).getCompressPath();
                    } else if (mFileFlag == 2) {
                        mSelectList2 = PictureSelector.obtainMultipleResult(data);
                        view = mFile2Iv;
                        url = "file://" + mSelectList2.get(0).getCompressPath();
                    } else {
                        mSelectList3 = PictureSelector.obtainMultipleResult(data);
                        view = mFile3Iv;
                        url = "file://" + mSelectList3.get(0).getCompressPath();
                    }
                    Glide.with(mContext).load(url).apply(MyApplication.getOptions())
                            .into(view);
                    break;

            }
        }
    }

}
