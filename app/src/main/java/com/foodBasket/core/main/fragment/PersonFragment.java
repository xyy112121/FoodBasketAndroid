package com.foodBasket.core.main.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MainActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.RequestPermissionCallBack;
import com.foodBasket.core.main.model.UserResModel;
import com.foodBasket.core.main.model.WaitingReceiveResModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.ui.AboutActivity;
import com.foodBasket.core.person.ui.AddressListActivity;
import com.foodBasket.core.person.ui.CouponListActivity;
import com.foodBasket.core.person.ui.MerchantAddActivity;
import com.foodBasket.core.person.ui.OrderListActivity;
import com.foodBasket.core.person.ui.OrderListDeliveryManActivity;
import com.foodBasket.core.person.ui.PersonInfoActivity;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.foodBasket.util.loader.LatteLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mic.etoast2.Toast;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import q.rorbin.badgeview.QBadgeView;

/**
 * 我的
 */

public class PersonFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.img_user_avatar)
    RoundedImageView mIvAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvName;
    @BindView(R.id.tv_user_name2)
    Button mTvName2;
    @BindView(R.id.ll_wait_receive)
    LinearLayout llWaitReceive;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.ll_received)
    LinearLayout llReceived;
    @BindView(R.id.ll_receive)
    LinearLayout llReceive;
    @BindView(R.id.person_add_lv)
    LinearLayout personAddLv;
    @BindView(R.id.ll_gift)
    LinearLayout llGift;
    @BindView(R.id.person_add_view)
    View mAddView;
    @BindView(R.id.view_gift)
    View mViewGift;
    @BindView(R.id.iv_receive)
    ImageView mIvReceive;

    private String mRealName;//真实姓名
    private String mMerchantName;//餐馆名称

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, null);
        unbinder = ButterKnife.bind(this, view);

        int userType = ShareConfig.getConfigInt(getActivity(), Constants.USERTYPE, 0);
        if (userType == 0) {
            llWaitReceive.setVisibility(View.GONE);
        } else {
            llPay.setVisibility(View.GONE);
            llReceived.setVisibility(View.INVISIBLE);
            llReceive.setVisibility(View.INVISIBLE);
            personAddLv.setVisibility(View.GONE);
            llGift.setVisibility(View.GONE);
            mAddView.setVisibility(View.GONE);
            mViewGift.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        HomeAction action = new HomeAction();
        LatteLoader.showLoading(getActivity());
        try {
            action.user(getActivity(), new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    UserResModel model = JSON.parseObject(result, UserResModel.class);
                    if (model != null) {
                        if (model.getSuccess()) {
                            UserResModel.User user = model.user;
                            String url = MyApplication.getApplication().mImageUrl + user.avatar;
                            Glide.with(getActivity())
                                    .load(url)
                                    .apply(MyApplication.getOptions())
                                    .into(mIvAvatar);
                            mTvName.setText(user.userLogin);
                            mTvName2.setText(user.realName);

                            mRealName = user.realName;
                            mMerchantName = user.merchantName;


                            ShareConfig.setConfig(getActivity(), Constants.USERTYPE, user.userType);
                            if (model.waitingReceive > 0) {
                                new QBadgeView(getActivity()).bindTarget(mIvReceive).setBadgeNumber(model.waitingReceive).setBadgeGravity(Gravity.END | Gravity.TOP).setGravityOffset(2, -3, true);
                            }
                        } else {
                            Toast.makeText(getActivity(), model.getResultInfo(), android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (Exception e) {
            LatteLoader.stopLoading();
        }
    }

//    private void getData() {
//        String avatar = ShareConfig.getConfigString(getActivity(), Constants.AVATER, "");
//        String name = ShareConfig.getConfigString(getActivity(), Constants.NAME, "");
//        String niceName = ShareConfig.getConfigString(getActivity(), Constants.NICENAME, "");
//        String url = MyApplication.getApplication().mImageUrl + avatar;
//        Glide.with(getActivity())
//                .load(url)
//                .apply(MyApplication.getOptions())
//                .into(mIvAvatar);
//        mTvName.setText(niceName);
//        mTvName2.setText(name);
//
//        getDebitAndWaiting();
//    }

    /**
     * 获取待收货数量
     */
    private void getDebitAndWaiting() {
        LatteLoader.showLoading(getActivity());
        HomeAction action = new HomeAction();
        try {
            action.getDebitAndWaiting(getActivity(), new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    WaitingReceiveResModel model = JSON.parseObject(result, WaitingReceiveResModel.class);
                    if (model != null && model.getSuccess()) {
                        if (model.waitingReceive > 0) {
                            new QBadgeView(getActivity()).bindTarget(mIvReceive).setBadgeNumber(model.waitingReceive).setBadgeGravity(Gravity.END | Gravity.TOP).setGravityOffset(2, -3, true);
                        }


                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.top_set, R.id.img_user_avatar, R.id.ll_wait_receive,
            R.id.person_all_order_ll, R.id.ll_receive, R.id.ll_received
            , R.id.person_add_lv, R.id.ll_pay, R.id.ll_phone, R.id.ll_about, R.id.merchant_add_lv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_set:
                PersonInfoActivity.openActivity(getActivity());
                break;
            case R.id.img_user_avatar:
                break;
            case R.id.ll_wait_receive:
                OrderListDeliveryManActivity.openActivity(getActivity());//送货单
                break;
            case R.id.person_all_order_ll:
                OrderListActivity.openActivity(getActivity(), "");
                break;
            case R.id.ll_receive:
                OrderListActivity.openActivity(getActivity(), "待收货");
                break;
            case R.id.ll_received:
                OrderListActivity.openActivity(getActivity(), "已收货");
                break;
            case R.id.person_add_lv:
                AddressListActivity.openActivity(getActivity(), false, 0);
                break;
            case R.id.ll_pay:
                CouponListActivity.openActivity(getActivity());
                break;
            case R.id.ll_phone:
                phone();
                break;
            case R.id.ll_about:
                AboutActivity.openActivity(getActivity());
                break;
            case R.id.merchant_add_lv:
                MerchantAddActivity.openActivity(getActivity(), mRealName, mMerchantName);
                break;
        }
    }

    //联系客服
    private void phone() {
        final String[] items = {"客服A", "客服B"};
        new CircleDialog.Builder((FragmentActivity) getActivity())
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = R.style.PopWindowAnimationFade;
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            call("15812002697");
                        } else {
                            call("15398679337");
                        }

                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                    }
                })
                .show();
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(final String phone) {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        final MainActivity activity = (MainActivity) getActivity();
        activity.requestPermissions(getActivity(), permissions, new RequestPermissionCallBack() {
            @Override
            public void granted() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void denied() {
                Toast.makeText(getActivity(), "请开启拨打电话权限！", android.widget.Toast.LENGTH_SHORT).show();

            }
        });

    }


}
