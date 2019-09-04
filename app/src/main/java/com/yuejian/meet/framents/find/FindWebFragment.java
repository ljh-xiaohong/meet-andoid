package com.yuejian.meet.framents.find;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.session.constant.Extras;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.find.SurnameWikiActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/12/29/029.
 */

public class FindWebFragment extends BaseFragment {
    private FrameLayout layout = null;
    private WebView webView;
    private View noNetWorkView;

    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private final static int SCANNER = 1212;
    private String orginalUrl = "";
    ShareAction shareAction = null;
    private boolean isShareSuccess;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        layout = new FrameLayout(getActivity());
        orginalUrl = Constants.FIND_URL + (StringUtils.isNotEmpty(AppConfig.CustomerId) ? "?customer_id=" + AppConfig.CustomerId : "");
        orginalUrl = orginalUrl.replace("https:", "http:");
        FrameLayout parentView = new FrameLayout(getActivity());
        parentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentView.addView(layout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        noNetWorkView = inflater.inflate(R.layout.no_network_layout, null);
        noNetWorkView.findViewById(R.id.tap_reconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWeb(orginalUrl);
            }
        });
        parentView.addView(noNetWorkView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWeb(orginalUrl);
        return parentView;
    }

    private void initWeb(String url) {
        layout.removeAllViews();
        webView = new WebView(getActivity());
        layout.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);
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

    private WebChromeClient webChromeClient = new WebChromeClient() {
        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUM = uploadMsg;
            showPhotoSheet();
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
            showPhotoSheet();
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            showPhotoSheet();
        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            showPhotoSheet();
            return true;
        }
    };

    private boolean isError = false;

    private void showPhotoSheet() {
        Utils.pickPhotos(getActivity(), FCR, false, 0, 0, dismissListener);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isError = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            noNetWorkView.setVisibility(isError ? View.VISIBLE : View.GONE);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            noNetWorkView.setVisibility(View.VISIBLE);
            isError = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (url.contains("xingshibaike.html")) {
                startActivity(new Intent(getActivity(), SurnameWikiActivity.class));
            } else if (url.contains("scanQrCode.html")) {
                if (StringUtils.isEmpty(AppConfig.CustomerId)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivityForResult(new Intent(getActivity(), ScannerActivity.class), SCANNER);
                    String link = Constants.FIND_URL + (StringUtils.isNotEmpty(AppConfig.CustomerId) ? "?customer_id=" + AppConfig.CustomerId : "");
                    link = link.replace("https:", "http:");
                    webView.loadUrl(link);
                }
            } else if (url.contains("zhongshare.html")) {
                try {
                    final String title = URLDecoder.decode(Utils.getValueByName(url, "title"), "UTF-8");
                    final String desc = URLDecoder.decode(Utils.getValueByName(url, "desc"), "UTF-8");
                    String img = Utils.getValueByName(url, "img");
                    final String shareCustomerId = Utils.getValueByName(url, "customer_id");
                    final String shareUrl = Utils.getValueByName(url, "url");
                    Glide.with(getActivity()).load(img).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            shareXiuXing(resource, shareCustomerId, shareUrl, title, desc);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            shareXiuXing(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), shareCustomerId, shareUrl, title, desc);
                        }
                    });
                    return true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(Constants.URL, url);
                intent.putExtra(Constants.NO_TITLE_BAR, true);
                if (url.contains("xiuxing.html")) {
                    if (AppConfig.userEntity == null) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                    }
                }
                startActivity(intent);
            }
            return true;
        }
    };

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
            } else if (SCANNER == requestCode && data != null) {
                String result = data.getStringExtra("scanner_result");
                Log.d("scanner", webView.getUrl());
                webView.loadUrl("javascript:getResult('" + result + "')");
                Log.d("scannerResult", result);
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
    public void onResume() {
        mIsFirstResume = true;
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.LOGIN_UPDATE || event.getCallType() == BusEnum.LOGOUT) {
            String url = Constants.FIND_URL + (StringUtils.isNotEmpty(AppConfig.CustomerId) ? "?customer_id=" + AppConfig.CustomerId : "");
            url = url.replace("https:", "http:");
            webView.loadUrl(url);
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        String url = Constants.FIND_URL + (StringUtils.isNotEmpty(AppConfig.CustomerId) ? "?customer_id=" + AppConfig.CustomerId : "");
        url = url.replace("https:", "http:");
        if (Utils.isNetLink()) {
            webView.loadUrl(url);
        } else {
            noNetWorkView.setVisibility(View.VISIBLE);
        }
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
        web.setThumb(new UMImage(getActivity(), resource));  //缩略图
        web.setDescription(desc);//描述
        shareAction = new ShareAction(getActivity())
                .withMedia(web)
                .setCallback(umShareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
        shareAction.open();
    }
}
