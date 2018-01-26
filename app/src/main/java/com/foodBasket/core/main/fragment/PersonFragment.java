package com.foodBasket.core.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodBasket.R;
import com.foodBasket.core.person.ui.AddressListActivity;
import com.foodBasket.core.person.ui.OrderListActivity;
import com.foodBasket.core.person.ui.OrderListDeliveryManActivity;
import com.foodBasket.core.person.ui.OrderReceivedListActivity;
import com.foodBasket.core.person.ui.PersonInfoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 */

public class PersonFragment extends Fragment {
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.top_set, R.id.img_user_avatar, R.id.ll_wait_receive,
            R.id.person_all_order_ll, R.id.ll_receive,R.id.ll_received
            ,R.id.person_add_lv})
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
                OrderListActivity.openActivity(getActivity());
                break;
            case R.id.ll_receive:
            case R.id.ll_received:
                OrderListActivity.openActivity(getActivity());
//                OrderReceivedListActivity.openActivity(getActivity());
                break;
            case R.id.person_add_lv:
                AddressListActivity.openActivity(getActivity());
                break;
        }
    }
}
