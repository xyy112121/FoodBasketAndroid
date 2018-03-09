package com.foodBasket.core.person.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foodBasket.BaseActivity;
import com.foodBasket.R;
import com.foodBasket.util.loader.LatteLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 打开网页
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initTopbar(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        showWebView(url);
    }


    public static void openActivity(Activity activity,String title,String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        activity.startActivity(intent);
    }

    private void showWebView(String url) {
        LatteLoader.showLoading(mContext);
        WebSettings settings = mWebView.getSettings();
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片加载不出来的问题
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);//允许DCOM

        mWebView.loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    LatteLoader.stopLoading();
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient());
    }
}
