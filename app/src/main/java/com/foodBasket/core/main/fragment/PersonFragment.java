package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.main.model.UserResModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.core.person.ui.AddressListActivity;
import com.foodBasket.core.person.ui.OrderListActivity;
import com.foodBasket.core.person.ui.OrderListDeliveryManActivity;
import com.foodBasket.core.person.ui.PersonInfoActivity;
import com.foodBasket.net.MyStringCallBack;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, null);
        unbinder = ButterKnife.bind(this, view);
        getData();
        return view;
    }

    private void getData() {
        HomeAction action = new HomeAction();
        try {
            //特惠
            action.user(getActivity(), new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    UserResModel model = JSON.parseObject(result, UserResModel.class);
                    if (model != null && model.getSuccess()) {
                        String url = MyApplication.getApplication().mImageUrl + model.user.avatar;
                        Glide.with(getActivity())
                                .load(url)
                                .apply(MyApplication.getOptions())
                                .into(mIvAvatar);
                        mTvName.setText(model.user.niceName);
                        mTvName2.setText(model.user.niceName);
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
            , R.id.person_add_lv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_set:
                PersonInfoActivity.openActivity(getActivity());
                break;
            case R.id.img_user_avatar:
                break;
            case R.id.ll_wait_receive:
                OrderListDeliveryManActivity.openActivity(getActivity());//送货单
//                OrderWaitReceivingListActivity.openActivity(getActivity());
                break;
            case R.id.person_all_order_ll:
                OrderListActivity.openActivity(getActivity(),"");
                break;
            case R.id.ll_receive:
                OrderListActivity.openActivity(getActivity(),"待收货");
                break;
            case R.id.ll_received:
                OrderListActivity.openActivity(getActivity(),"已收货");
                break;
            case R.id.person_add_lv:
                AddressListActivity.openActivity(getActivity());
                break;
        }
    }
}
