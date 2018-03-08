package com.foodBasket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.foodBasket.core.main.fragment.CartFragment;
import com.foodBasket.core.main.fragment.HomeFragment;
import com.foodBasket.core.main.fragment.PersonFragment;
import com.foodBasket.core.main.fragment.TypeFragment;
import com.foodBasket.core.person.model.VersionResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.AppUpdateUtils;
import com.foodBasket.util.AppVersion;
import com.foodBasket.util.loader.LatteLoader;
import com.foodBasket.view.MainNavigateTabBar;
import com.mic.etoast2.Toast;

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
        getUpdateInfo();
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
