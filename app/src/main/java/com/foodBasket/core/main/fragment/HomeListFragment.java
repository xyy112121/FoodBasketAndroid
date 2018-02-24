package com.foodBasket.core.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.foodBasket.R;
import com.foodBasket.core.goods.activity.ProductInfoActivity;
import com.foodBasket.net.ResponseBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页
 */

public class HomeListFragment extends Fragment {
    LayoutInflater mInflater;
    @BindView(R.id.home_list_type_lv)
    ListView mTypeLv;
    @BindView(R.id.home_list_value_lv)
    ListView mValueLv;
    Unbinder unbinder;

    TypeAdapter mTypeAdapter;
    ValueAdapter mValueAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View mView = mInflater.inflate(R.layout.fragment_home_list, null);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        mTypeAdapter = new TypeAdapter(getActivity(), R.layout.item_type_list_type_item);
        mTypeLv.setAdapter(mTypeAdapter);
        mValueAdapter = new ValueAdapter(getActivity(), R.layout.fragment_home_list_value_item);
        mValueLv.setAdapter(mValueAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class TypeAdapter extends ArrayAdapter<ResponseBean> {

        public TypeAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return 15;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_type_list_type_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.mTypeCk.setOnCheckedChangeListener(new );


            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.home_list_type_ck)
            CheckBox mTypeCk;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public class ValueAdapter extends ArrayAdapter<ResponseBean> {

        public ValueAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fragment_home_list_value_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.mTypeCk.setOnCheckedChangeListener(new );

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductInfoActivity.openActivity(getActivity(), "");
                }
            });


            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.value_item_sales_tv)
            TextView mTypeCk;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
