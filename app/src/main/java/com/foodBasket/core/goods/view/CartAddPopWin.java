package com.foodBasket.core.goods.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foodBasket.MainActivity;
import com.foodBasket.MyApplication;
import com.foodBasket.R;
import com.foodBasket.core.main.model.ProductListModel;
import com.foodBasket.core.main.net.HomeAction;
import com.foodBasket.net.MyStringCallBack;
import com.foodBasket.net.ResponseBean;
import com.foodBasket.util.loader.LatteLoader;
import com.mic.etoast2.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by programmer on 2018/2/24.
 */

public class CartAddPopWin extends PopupWindow {

    private Context mContext;

    private View view;

    private ImageView btn_cancel;
    private int mCount = 1;


    public CartAddPopWin(final Context mContext, final ProductListModel product) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.activity_add_cart, null);
        final ViewHolder cholder = new ViewHolder(view);
        cholder.mNameTv.setText(product.name);
        cholder.mPriceTv.setText("￥" + product.salePrice + "/" + product.displayUnit);
        String url = MyApplication.getApplication().mImageUrl + product.headPicture;
        Glide.with(mContext)
                .load(url)
                .apply(MyApplication.getOptions())
                .into(cholder.mPictrueIv);
        // 取消按钮
        cholder.ivCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        cholder.tvGoToCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                MainActivity.onTabIndex(2);
            }
        });

        cholder.tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                addCart(mContext, product.id, mCount + "");
            }
        });

        cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = ++mCount;

                if (c != 100) {
                    cholder.tv_count.setText(c + "");
                } else {
                    com.mic.etoast2.Toast.makeText(mContext, "最大数量为99", android.widget.Toast.LENGTH_SHORT).show();
                }

            }
        });

        cholder.tv_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!"".equals(s) && s != null) {
                        mCount = Integer.parseInt(s + "");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        cholder.iv_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCount > 1) {
                    cholder.tv_count.setText(--mCount + "");
                }
            }
        });


        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.cart_add_anim);

    }

    /**
     * 加入购物车
     * userId  productBasicId(产品ID) productNumber(数目)
     */
    private void addCart(Context mContext, String id, String count) {
        LatteLoader.showLoading(mContext);
        HomeAction action = new HomeAction();
        try {
            //购物车
            action.addShoppingCart(mContext, id, count, new MyStringCallBack() {
                @Override
                public void onResult(String result) {
                    LatteLoader.stopLoading();
                    ResponseBean model = JSON.parseObject(result, ResponseBean.class);
                    if (model == null) {
                        Toast.makeText(MyApplication.getApplication(), "添加购物车失败", android.widget.Toast.LENGTH_SHORT).show();
                    } else if (model.getSuccess()) {
                        Toast.makeText(MyApplication.getApplication(), "已添加购物车", android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyApplication.getApplication(), model.getResultInfo(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_cancel)
        ImageView ivCancel;
        @BindView(R.id.type_value_pictrue_iv)
        ImageView mPictrueIv;
        @BindView(R.id.type_value_price_tv)
        TextView mPriceTv;
        @BindView(R.id.type_value_name_tv)
        TextView mNameTv;
        @BindView(R.id.tv_reduce)
        ImageView iv_decrease;
        @BindView(R.id.tv_num)
        TextView tv_count;
        @BindView(R.id.tv_add)
        ImageView iv_increase;
        @BindView(R.id.tv_go_to_pay)
        TextView tvGoToPay;
        @BindView(R.id.tv_add_cart)
        TextView tvAddCart;
        @BindView(R.id.tv_go_to_pay_layout)
        LinearLayout tvGoToPayLayout;
        @BindView(R.id.pop_layout)
        FrameLayout popLayout;
        @BindView(R.id.tv_go_to_cart_ll)
        LinearLayout tvGoToCartLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
