package com.foodBasket.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.foodBasket.R;


/**
 * 发布的单行输入框
 */

public class TextRowView extends LinearLayout {
    private String mTitleText;
    private String mHiht;
    private int mTextSize;
    private int mTextColor;
    private int mRightTextColor;
    private boolean mIsTopLineShow;
    private boolean mIsBottomLineShow;
    private boolean mIsSelect;//true 是，
    private TextView mValueView;
    private ClearEditText mValueView2;
    private TextView mLeftView;
    private ImageView mRightView;
    View mBottomView;
    private int mWidth;
    private int mGravity;
    private int mInputType;
    private int mState;

    public TextRowView(Context context) {
        this(context, null);
    }

    public TextRowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextRowView, defStyleAttr, 0);
        mTitleText = a.getString(R.styleable.TextRowView_TextRowViewText);
        mHiht = a.getString(R.styleable.TextRowView_TextRowViewHiht);
        mIsBottomLineShow = a.getBoolean(R.styleable.TextRowView_TextRowViewIsBottomLineShow, true);
        mTextColor = a.getColor(R.styleable.TextRowView_TextRowViewTextColor, ContextCompat.getColor(context, R.color.tab_text_normal));
        mRightTextColor = a.getColor(R.styleable.TextRowView_TextRowViewRightTextColor, ContextCompat.getColor(context, R.color.tab_text_normal));
        mTextSize = a.getDimensionPixelSize(R.styleable.TextRowView_TextRowViewTextSize, ConvertUtils.dp2px(12));
        mIsSelect = a.getBoolean(R.styleable.TextRowView_TextRowViewIsSelect, false);
        mWidth = a.getDimensionPixelSize(R.styleable.TextRowView_TextRowViewLeftWidth, ConvertUtils.dp2px(140));
        mGravity = a.getInt(R.styleable.TextRowView_TextRowViewGravity, 0);
        mState = a.getInt(R.styleable.TextRowView_TextRowViewState, 1);
        mInputType = a.getInt(R.styleable.TextRowView_TextRowViewInputType, 2);
        initView(context);
    }

    public void setValueText(String text) {
        mValueView.setText(text);
        mValueView2.setText(text);
    }

    public void setValueHint(String text) {
        mValueView.setHint(text);
        mValueView2.setHint(text);
    }

    public String getValueText() {
        if (mValueView.getVisibility() == VISIBLE) {
            return mValueView.getText() + "";
        } else {
            return mValueView2.getText() + "";
        }
    }

    public void setLeftext(String text) {
        mLeftView.setText(text);
    }

    /**
     * 设置可编辑（可点击）
     *
     * @param isEditable
     */
    public void setEditable(boolean isEditable) {
        if (isEditable) {
            mRightView.setVisibility(VISIBLE);
        } else {
            mRightView.setVisibility(GONE);
        }
    }

    public void setImageDrawable(int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mValueView.setCompoundDrawables(null, null, drawable, null);
    }

    public void setBottonLineVisibility(int visibility) {
        mBottomView.setVisibility(visibility);
    }

    public void initView(Context context) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.view_pulish_text_row, null);
        mLeftView = view.findViewById(R.id.left);
        mValueView2 = view.findViewById(R.id.rowvalue);
        mValueView = view.findViewById(R.id.row_value);
        mBottomView = view.findViewById(R.id.bottom_line);
        mRightView = view.findViewById(R.id.right_image);
        mLeftView.setText(mTitleText);
        mValueView.setTextColor(mRightTextColor);
        mValueView2.setTextColor(mRightTextColor);
        mValueView2.setHint(mHiht);
        if (mInputType == 0) {//数字
            mValueView2.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (mInputType == 1) {//电话
            mValueView2.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (mInputType == 2) {//文本
            mValueView2.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (mInputType == 3) {//密码
            mValueView2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (mInputType == 4) {
            mValueView2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        mValueView.setHint(mHiht);
        if (mGravity == 0) {//左边
            mValueView.setGravity(Gravity.LEFT | Gravity.CENTER);
        } else if (mGravity == 1) {
            mValueView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        }
        mLeftView.setTextColor(mTextColor);
        LayoutParams params = new LayoutParams(mWidth, LayoutParams.MATCH_PARENT);
        mLeftView.setLayoutParams(params);

        if (mIsBottomLineShow == false) {
            mBottomView.setVisibility(INVISIBLE);
        }
        if (mIsSelect) {
            mValueView2.setVisibility(GONE);
            mValueView.setVisibility(VISIBLE);
            mRightView.setVisibility(VISIBLE);
        } else {
            mValueView2.setVisibility(VISIBLE);
            mValueView.setVisibility(GONE);
            mRightView.setVisibility(INVISIBLE);
        }

        if (mState == 1) {//可编辑
            mValueView2.setVisibility(VISIBLE);
            mValueView.setVisibility(GONE);
        } else {
            mValueView2.setVisibility(GONE);
            mValueView.setVisibility(VISIBLE);
        }
        addView(view);
    }

}
