package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.constant.Extras;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.mine.VerifyCenterActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.net.URLDecoder;

public class StoreWebActivity extends BaseActivity {

    private WebView webView;

    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private Activity activity = null;

    private ViewGroup titleLayout;
    private ProgressBar progressBar;
    private View noNetWorkView;

    private String orginalUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        ACTIVITY_NAME = "商域页面";
        setContentView(R.layout.activity_store_web);
        titleLayout = (ViewGroup) findViewById(R.id.title_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setTitleText("");
        orginalUrl = getIntent().getStringExtra(Constants.URL);
        if (StringUtil.isEmpty(orginalUrl)){
            orginalUrl= Constants.STORE_URL +"/index" + "?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app";
        }
        noNetWorkView = findViewById(R.id.no_network_layout);
        noNetWorkView.findViewById(R.id.tap_reconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(orginalUrl);
            }
        });
        initWeb(orginalUrl);
        setTitleText("优品购");
    }

    private void initWeb(String url) {
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setTextZoom(100);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(webViewClient);// 新建浏览器客户端，不调用系统浏览器
        webView.setWebChromeClient(webChromeClient);
        loadUrl(url);
    }

    private void loadUrl(String url) {
        if (Utils.isNetLink()) {
            noNetWorkView.setVisibility(View.GONE);
            webView.loadUrl(url);
        } else {
            noNetWorkView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUM = uploadMsg;
            Utils.pickPhotos(StoreWebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
            Utils.pickPhotos(StoreWebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            Utils.pickPhotos(StoreWebActivity.this, FCR, false, 0, 0, dismissListener);

        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Utils.pickPhotos(StoreWebActivity.this, FCR, false, 0, 0, dismissListener);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                if (view.getUrl().equals(orginalUrl)){
                    titleLayout.setVisibility(View.VISIBLE);
                }else {
                    titleLayout.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (!Utils.isSubmit) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                    mUMA = null;
                } else if (mUM != null) {
                    mUM.onReceiveValue(null);
                    mUM = null;
                }
            }
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (Utils.isNetLink()) {
                noNetWorkView.setVisibility(View.GONE);
                return blockUrl(url) ? true : super.shouldOverrideUrlLoading(view, url);
            } else {
                noNetWorkView.setVisibility(View.VISIBLE);
                return true;
            }
        }
    };

    private boolean blockUrl(String url) {
        if (url.contains("weixin://wap/pay") || url.contains("weixin")) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!Utils.isWeixinAvilible(getApplication())) {
                        Toast.makeText(activity, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        } else if (url.contains("app_share.html?")) {
            try {
                //title=&imgurl=&url=&desc=
                final String title = URLDecoder.decode(Utils.getValueByName(url, "title"), "UTF-8");
                final String imgurl = Utils.getValueByName(url, "imgurl");
                final String shareUrl = Utils.getValueByName(url, "url");
                final String desc = URLDecoder.decode(Utils.getValueByName(url, "desc"), "UTF-8");

                Glide.with(getActivity()).load(imgurl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Utils.umengShareByList(getActivity(), resource, title, desc, shareUrl);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Utils.umengShareByList(getActivity(), BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, desc, shareUrl);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (url.contains("userinfo.html?")) {
            String customerId = Utils.getValueByName(url, "userId");
            AppUitls.goToPersonHome(mContext, customerId);
            return true;
        } else if (url.contains("to_app_business_license_certified.html")) {
            Intent intent = new Intent(getActivity(), VerifyCenterActivity.class);
            startActivity(intent);
            return true;
        } else if (url.contains("/yuejian/h5/index") || url.contains("/yuejiantest/h5/index")) {
            loadUrl(orginalUrl);
//            getActivity().finish();
            return true;
        } else if (url.contains("changeName.html") || url.contains("changeCity.html")) {
            Bus.getDefault().post("web_store_reload");
            getActivity().finish();
            return true;
        } else if (url.contains("market_chat.html?")) {
            String customerId = Utils.getValueByName(url, "customer_id");
            ImUtils.toStoreP2PCaht(StoreWebActivity.this, customerId);
            return true;
        } else if (url.contains("login.html")) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (url.contains("close.html")) {
            finish();
            return true;
        } else if (url.contains("baijiaxin.html")) {
            setResult(RESULT_OK);
            finish();
            return true;
        } else if (url.contains("return_yuejian")) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (FCR == requestCode) {
                String filePath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                Uri uri = Uri.fromFile(new File(filePath));
                if (mUMA != null) {
                    mUMA.onReceiveValue(new Uri[]{uri});
                    mUMA = null;
                } else if (mUM != null) {
                    mUM.onReceiveValue(uri);
                    mUM = null;
                }
            }
        } else {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
                mUMA = null;
            } else if (mUM != null) {
                mUM.onReceiveValue(null);
                mUM = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStop() {
//        Bus.getDefault().post("reload");
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && webView != null) {
            String url = intent.getStringExtra(Constants.URL);
            if (StringUtils.isNotEmpty(url)) {
                webView.loadUrl(url);
            }
            String function = intent.getStringExtra("function");
            if (StringUtils.isNotEmpty(function)) {
                webView.loadUrl("javaScript:" + function);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.getSettings().setBuiltInZoomControls(false);
            webView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        webView.destroy();
                    }
                }
            }, timeout);
        }
        super.onDestroy();
    }

    private Activity getActivity() {
        return activity;
    }

}