package com.yuejian.meet.activities.web;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.common.Constants;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用于显示网页h5信息
 */

public class WebWxPayActivity extends BaseActivity {

    @Bind(R.id.txt_titlebar_title)
    TextView mTxtTitleBarTitle;
    @Bind(R.id.webview_titlebar_imgBtn_back)
    ImageButton mBack;
    @Bind(R.id.webview_titlebar_imgBtn_finish)
    ImageButton mBackFinish;
    @Bind(R.id.web_content)
    WebView mWebView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootView();
        initWidget();
    }

    public void setRootView() {
        setContentView(R.layout.activity_web);
    }

    protected void initWidget() {
        url = getIntent().getStringExtra(Constants.URL);
        if (!url.contains("http")){
            url="https://"+url;
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);

        mWebView.setWebViewClient(mWebViewClient);// 新建浏览器客户端，不调用系统浏览器
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.loadUrl(url);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //用javascript隐藏系统定义的404页面信息
            String data = "Page NO FOUND！";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }else{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };


    private WebChromeClient mWebChromeClient = new WebChromeClient() {// 显示网页加载进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            try {
                mTxtTitleBarTitle.setText(title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @OnClick({R.id.webview_titlebar_imgBtn_back})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.webview_titlebar_imgBtn_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
//                    mBackFinish.setVisibility(View.VISIBLE);
                } else {
                    mWebView.freeMemory();
                    finish();
                }
                break;
            case R.id.webview_titlebar_imgBtn_finish:
                mWebView.freeMemory();
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            mBackFinish.setVisibility(View.VISIBLE);
        } else {
            mWebView.freeMemory();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            try {
                mWebView.stopLoading();
                mWebView.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.reload();
            mWebView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
