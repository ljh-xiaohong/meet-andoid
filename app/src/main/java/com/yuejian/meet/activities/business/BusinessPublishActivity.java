package com.yuejian.meet.activities.business;

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
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.find.SurnameWikiActivity;
import com.yuejian.meet.activities.home.ActionInfoActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.mine.SelectGoodsActivity;
import com.yuejian.meet.activities.mine.VerifyBusinessActivity;
import com.yuejian.meet.framents.find.RechargeMeritsActivity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/6/21 16:50
 * @desc : 商圈 - 发布界面（WebView）
 */
public class BusinessPublishActivity extends BaseActivity {

    @Bind(R.id.business_publish_title)
    TextView webTitle;
    @Bind(R.id.business_publish_back)
    ImageButton mBack;
    @Bind(R.id.business_publish_web_content)
    WebView webView;
    @Bind(R.id.business_publish_action_bar)
    RelativeLayout mActionBar;
    @Bind(R.id.business_publish_no_network_layout)
    LinearLayout mNoNetworkLayout;
    @Bind(R.id.business_publish_tap_reconnect)
    LinearLayout mTapReconnect;
    @Bind(R.id.business_publish_submit)
    TextView mSubmitBtn;

    private boolean isApplyFamilyMaster = false;
    ShareAction shareAction = null;

    private String orginalUrl = "";
    private String title = "发布";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_publish);
        ACTIVITY_NAME = "浏览H5页面";

        orginalUrl = getIntent().getStringExtra("url");
        orginalUrl = orginalUrl.replace("https://", "http://");

        if (StringUtils.isEmpty(orginalUrl))
            finish();

        mTapReconnect.setOnClickListener(v -> loadUrl(orginalUrl));

        initWidget();
    }

    protected void initWidget() {
        if ((getIntent().getBooleanExtra("No_Title", false)) || (this.orginalUrl.contains("person_card/index.html"))) {
            mActionBar.setVisibility(View.GONE);
        }
        mSubmitBtn.setVisibility(View.INVISIBLE);
        webTitle.setVisibility(View.VISIBLE);
        webTitle.setText(title);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setTextZoom(100);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if(Build.VERSION.SDK_INT >=21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if(Build.VERSION.SDK_INT >=19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if(Build.VERSION.SDK_INT< 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(webViewClient);// 新建浏览器客户端，不调用系统浏览器
        webView.setWebChromeClient(webChromeClient);

        loadUrl(orginalUrl);
    }

    private void loadUrl(String url) {
        if (Utils.isNetLink()) {
            webView.loadUrl(url);
            mNoNetworkLayout.setVisibility(View.GONE);
        } else {
            mNoNetworkLayout.setVisibility(View.VISIBLE);
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Utils.isNetLink()) {
                mNoNetworkLayout.setVisibility(View.GONE);
                return blockUrl(url) ? true : super.shouldOverrideUrlLoading(view, url);
            } else {
                mNoNetworkLayout.setVisibility(View.VISIBLE);
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mNoNetworkLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        // 显示网页加载进度
        @Override
        public void onReceivedTitle(WebView view, final String title) {
        }

        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUM = uploadMsg;
            Utils.pickPhotos(BusinessPublishActivity.this, FCR, false, 0, 0, dismissListener);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
            Utils.pickPhotos(BusinessPublishActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            Utils.pickPhotos(BusinessPublishActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Utils.pickPhotos(BusinessPublishActivity.this, FCR, false, 0, 0, dismissListener);
            return true;
        }
    };

    private String shareCustomerId = "";

    /**
     * 拦截Url，判断是否阻止网页跳转
     * @param url
     * @return 是否阻止网页跳转
     */
    private boolean blockUrl(String url) {
        Log.d("pay", url);
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
                        Toast.makeText(this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        } else if (url.contains("/pay/aliPayResult?") && isApplyFamilyMaster) {
            startActivity(new Intent(getApplication(), SelectGoodsActivity.class));
            finish();
            return true;
        } else if (url.contains("release/activity") || url.contains("release/demand")) {
            //显示发布按钮
            mSubmitBtn.setVisibility(View.VISIBLE);
            mSubmitBtn.setOnClickListener(v -> webView.loadUrl("javascript:toSava()"));
            return false;
        } else if (!url.contains("release/activity") && !url.contains("release/demand")) {
            //不显示发布按钮
            mSubmitBtn.setVisibility(View.INVISIBLE);
            return false;
        }
        return false;
    }

    private void shareXiuXing(Bitmap resource, final String customerId, String shareUrl, String title, String desc) {
        UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                isShareSuccess = false;
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                webView.loadUrl("javascript:shareBack(" + customerId + ")");
                isShareSuccess = true;
                shareAction.close();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                shareAction.close();
                isShareSuccess = false;
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                isShareSuccess = false;
            }
        };

        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(BusinessPublishActivity.this, resource));  //缩略图
        web.setDescription(desc);//描述
        shareAction = new ShareAction(BusinessPublishActivity.this)
                .withMedia(web)
                .setCallback(umShareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
        shareAction.open();
    }

    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 11111;

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


    @OnClick({R.id.business_publish_back})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.business_publish_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    webView.freeMemory();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            webView.freeMemory();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            try {
                webView.stopLoading();
                webView.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isShareSuccess = false;

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        } else if (requestCode == 10103) {
            webView.loadUrl("javascript:shareBack(" + shareCustomerId + ")");
            isShareSuccess = true;
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
    public void onClick(View v) {

    }

    @BusReceiver
    public void onStringEvent(String event) {
        if ("wx_share_success".equals(event)) {
            if (webView != null) {
                webView.loadUrl("javascript:shareBack(" + shareCustomerId + ")");
                isShareSuccess = true;
            }
        }
    }


}
