package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.core.person.model.AddressListResModel;
import com.foodBasket.core.person.model.AddressResModel;
import com.foodBasket.core.person.net.PersonAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.mylhyl.circledialog.CircleDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 收货地址列表
 */

public class AddressListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    AddressListAdapter mAdapter;

    private int mPage = 1;
    private int mTotal;

    //flag判断是否立即关闭当前页面,选择时使用
    public static void openActivity(Activity activity, boolean flag, int result) {
        Intent intent = new Intent(activity, AddressListActivity.class);
        intent.putExtra("flag", flag);
        activity.startActivityForResult(intent, result);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_list);
        ButterKnife.bind(this);
        initTopbar("收货地址");
        initUI();
    }

    public void initUI() {
        mAdapter = new AddressListAdapter(mContext, 0);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));


    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.beginRefreshing();
    }

    private void getData() {
        LatteLoader.showLoading(mContext);
        PersonAction action = new PersonAction();
        try {
            action.userAddrList(mContext, mPage, 10, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    AddressListResModel model = JSON.parseObject(result, AddressListResModel.class);
                    if (model != null && model.getSuccess()) {
                        mTotal = model.total;
                        mAdapter.addAll(model.rows);
                    }
                }
            });

        } catch (Exception e) {
            LatteLoader.stopLoading();
            e.printStackTrace();
        }
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.clear();
        mPage = 1;
        getData();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (mTotal > mAdapter.getCount()) {
            getData();
        } else {
            mRefreshLayout.endLoadingMore();
        }
        return false;
    }

    @OnClick(R.id.shipping_address_list_add_ll)
    public void onViewClicked() {
        AddressEditActivity.openActivity(mContext, "");
    }

    public class AddressListAdapter extends ArrayAdapter<AddressResModel> {
        private int mResource;
        private Context mContext;

        public AddressListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            mResource = resource;
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.activity_shipping_address_list_item, null);
            final ViewHolder holder = new ViewHolder(convertView);
            holder.mPersonTv.setText(getItem(position).userName);
            holder.mMobileTv.setText(getItem(position).mobile);
            String addr = getItem(position).province + getItem(position).city + getItem(position).county + getItem(position).address;
            holder.mAddressTv.setText(addr);
            if (getItem(position).isDefault == true) {
                holder.mck.setChecked(true);
            } else {
                holder.mck.setChecked(false);
            }

            holder.mck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mck.isChecked() == true) {
                        setDefault(getItem(position));
                    }
                }
            });

            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(getItem(position));
                }
            });

            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String json = JSON.toJSONString(getItem(position));
                    AddressEditActivity.openActivity((Activity) mContext, json);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddressResModel model = mAdapter.getItem(position);
                    boolean flag = getIntent().getBooleanExtra("flag", false);
                    if (flag) {
                        Intent intent = new Intent();
                        String json = JSON.toJSONString(model);
                        intent.putExtra("addr", json);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
            return convertView;
        }

        private void delete(final AddressResModel item) {
            new CircleDialog.Builder((FragmentActivity) mContext)
                    .setTitle("提示")
                    .setText("确定要删除选择的收货地址吗？")
                    .setTextColor(getResources().getColor(R.color.black))
                    .setNegative("取消", null)
                    .setPositive("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PersonAction action = new PersonAction();
                            try {
                                action.userAddrDelete(item.id, new MyStringCallBack() {
                                    @Override
                                    public void onResult(String result) {
                                        ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                                        if (model != null) {
                                            if (model.getSuccess()) {
                                                remove(item);
                                                notifyDataSetChanged();
                                            } else {
                                                showMessage(model.getResultInfo());
                                            }
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .show();
        }

        //设置默认地址
        private void setDefault(final AddressResModel item) {
            PersonAction action = new PersonAction();
            try {
                action.setDefault(item.id, new MyStringCallBack() {
                    @Override
                    public void onResult(String result) {
                        ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                        if (model != null) {
                            if (model.getSuccess()) {
                                mRefreshLayout.beginRefreshing();
                            } else {
                                showMessage(model.getResultInfo());
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        class ViewHolder {
            @BindView(R.id.addr_item_contactperson)
            TextView mPersonTv;
            @BindView(R.id.addr_item_contactmobile)
            TextView mMobileTv;
            @BindView(R.id.addr_item_address)
            TextView mAddressTv;
            @BindView(R.id.check_box)
            CheckBox mck;

            @BindView(R.id.addr_item_delete)
            TextView mDelete;
            @BindView(R.id.addr_item_edit)
            TextView mEdit;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}
