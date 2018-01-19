package com.foodBasket.core.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodBasket.R;
import com.foodBasket.net.ResponseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */

public class DiscountsAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    public List<ResponseBean> mData; //定义数据源
    private OnItemClickListener mOnItemClickListener = null;

    public DiscountsAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    //定义构造方法，默认传入上下文和数据源
    public DiscountsAdapter(Context context, List<ResponseBean> data) {
        mContext = context;
        mData = data;
    }

    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_discounts_item, null);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        MyViewHolder holder = (MyViewHolder) h;
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    public void addAll(List<ResponseBean> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        if (mData != null) {
//            return mData.size();
//        }
        return 20;
    }

    public ResponseBean getItem(int position) {
        return mData.get(position);
    }

    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.imageview_avatar);
            title = (TextView) itemView.findViewById(R.id.textview_title);
        }
    }

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
