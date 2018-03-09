package com.foodBasket;

import android.Manifest;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.foodBasket.core.main.fragment.CartFragment;
import com.foodBasket.core.main.fragment.HomeFragment;
import com.foodBasket.core.main.fragment.PersonFragment;
import com.foodBasket.core.main.fragment.TypeFragment;
import com.foodBasket.core.person.model.VersionResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.MainNavigateTabBar;
import com.mic.etoast2.Toast;

import util.UpdateAppUtils;

public class MainActivity extends BaseTwoActivity {

    private static MainNavigateTabBar mNavigateTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigateTabBar = findViewById(R.id.mainTabBar);

//        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_main_false, R.mipmap.icon_main_true, "首页"));
        mNavigateTabBar.addTab(TypeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_type_false, R.mipmap.icon_type_true, "分类"));
//        mNavigateTabBar.addTab(DiscountsFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.chats_selector_false, R.mipmap.chats_selector_false, "特惠"));
        mNavigateTabBar.addTab(CartFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_shapcar_false, R.mipmap.icon_shopcar_true, "购物车"));
        mNavigateTabBar.addTab(PersonFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.icon_me_false, R.mipmap.icon_me_true, "我的"));
        init();
    }

    private void init() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(mContext, permissions, new RequestPermissionCallBack() {
            @Override
            public void granted() {
                getUpdateInfo();
            }

            @Override
            public void denied() {
                Toast.makeText(mContext, "请开启权限，否则可能导致！", android.widget.Toast.LENGTH_SHORT).show();

            }
        });
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
                            if (info.filePath != null && !"".equals(info.filePath)) {
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

    public static void onTabIndex(int index) {
        mNavigateTabBar.setCurrentSelectedTab(index);
    }


}
