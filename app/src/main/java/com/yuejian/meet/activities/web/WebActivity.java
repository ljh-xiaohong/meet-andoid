package com.yuejian.meet.activities.web;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
import com.yuejian.meet.activities.mine.BuyAllVipPermissionActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.mine.SelectGoodsActivity;
import com.yuejian.meet.activities.mine.VerifyBusinessActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.find.RechargeMeritsActivity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.PaymentBottomDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

import static com.yuejian.meet.api.http.UrlConstant.youPinGou;


/**
 * @author :
 * @time : 2018/11/11 14:42
 * @desc : 显示网页H5信息
 * @version: V1.0
 * @update : 2018/11/11 14:42
 */
@SuppressWarnings("ALL")
public class WebActivity extends BaseActivity {

    @Bind(R.id.web_title)
    TextView webTitle;

    @Bind(R.id.webview_titlebar_imgBtn_back)
    ImageButton mBack;

    @Bind(R.id.webview_titlebar_imgBtn_finish)
    ImageButton mBackFinish;

    @Bind(R.id.web_content)
    WebView webView;

    private boolean isApplyFamilyMaster = false;
    ShareAction shareAction = null;

    private String orginalUrl = "";
    private View noNetWorkView = null;
    private String tag;
    private String title = "约见优选商城";
    private IWXAPI mIwxapi;
    private static final int VIDEOS_REQUEST = 315;
    private boolean isTitle = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "浏览H5页面";
        if (AppConfig.isGeLiGuide) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        isApplyFamilyMaster = getIntent().getBooleanExtra("isUpVip", false);
        orginalUrl = getIntent().getStringExtra(Constants.URL);

        // TODO: 2018/11/26   区别壹克优选商城 title写死
        tag = getIntent().getStringExtra("tag");

        orginalUrl = orginalUrl.replace("https:", "http:");


        if (StringUtils.isEmpty(orginalUrl)) finish();
        setRootView();
        noNetWorkView = findViewById(R.id.no_network_layout);
        noNetWorkView.findViewById(R.id.tap_reconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(orginalUrl);
            }
        });
        Utils.settingPutInt(this);
        initWidget();
        mIwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
    }

    public void setRootView() {
        setContentView(R.layout.activity_web);
    }

    protected void initWidget() {
        if ((getIntent().getBooleanExtra("No_Title", false)) || (this.orginalUrl.contains("person_card/index.html"))) {
            findViewById(R.id.topic_action_bar).setVisibility(View.GONE);
        }
        webTitle.setVisibility(View.VISIBLE);

        if (null == tag || tag.isEmpty()) {

        } else {
            webTitle.setText(title);
        }


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowContentAccess(true);
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

        loadUrl(orginalUrl);

    }

    private void loadUrl(String url) {
        if (Utils.isNetLink()) {
            webView.loadUrl(url);
            noNetWorkView.setVisibility(View.GONE);
        } else {
            noNetWorkView.setVisibility(View.VISIBLE);
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
                findViewById(R.id.no_network_layout).setVisibility(View.GONE);

                return blockUrl(url) ? true : super.shouldOverrideUrlLoading(view, url);
            } else {
                findViewById(R.id.no_network_layout).setVisibility(View.VISIBLE);
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            noNetWorkView.setVisibility(View.VISIBLE);
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
//            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
//            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
//            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
//            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
            return true;
        }
    };

    private String shareCustomerId = "";

    private boolean blockUrl(String url) {
        Log.d("pay", url);
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals("yuejian")) {
            if (uri.getAuthority().equals("webapp")) { //封面人物的支付接口
                HashMap<String, Object> params = new HashMap<>();
                Set<String> queryNames = uri.getQueryParameterNames();
                for (String queryName : queryNames) {
                    String value = uri.getQueryParameter(queryName);
                    params.put(queryName, value);
                }
                //申请成为封面人物
                params.put("customer_id", user.customer_id);
                apiImp.doRankedDo(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Long payId = JSON.parseObject(data).getLong("id");
                        showPaymentDialog(payId);
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
            } else if (uri.getAuthority().equals("toBack")) {
                finish();
            } else if (uri.getAuthority().equals("user_info")) {

                AppUitls.goToPersonHome(this, getIntent().getStringExtra("customer_id"));

            } else if (uri.getAuthority().equals("continueWebapp")) {

                //TODO


//                HashMap<String, String> params = new HashMap<>();
//                Set<String> queryNames = uri.getQueryParameterNames();
//                for (String queryName : queryNames) {
//                    String value = uri.getQueryParameter(queryName);
//                    params.put(queryName, value);
//                }
//                //申请成为封面人物
//                params.put("customer_id", user.customer_id);
//                apiImp.doRankedDoAgain(params, this, new DataIdCallback<String>() {
//                    @Override
//                    public void onSuccess(String data, int id) {
//                        Long payId = JSON.parseObject(data).getLong("id");
//                        showPaymentDialog(payId);
//                    }
//
//                    @Override
//                    public void onFailed(String errCode, String errMsg, int id) {
//                    }
//                });

            }
            return true;
        }
        if (url.contains("/pay/aliPayResult?") && isApplyFamilyMaster) {
            startActivity(new Intent(getApplication(), SelectGoodsActivity.class));
            finish();
            return true;
        } else if (url.contains("clanChatGroup.html?")) {
            String tId = Utils.getValueByName(url, "t_id");
            String customerId = Utils.getValueByName(url, "customer_id");
            ImUtils.toAssemblyHall(mContext, tId, ChatEnum.shop, customerId);
            return true;
        } else if (url.contains("close.html")) {
            finish();
            return true;
        } else if (url.contains("userinfo.html?")) {
            String customerId = Utils.getValueByName(url, "userId");
            AppUitls.goToPersonHome(mContext, customerId);
            return true;
        } else if (url.contains("addFriend.html?")) {
            String op_customer_id = Utils.getValueByName(url, "op_customer_id");
            String userName = Utils.getValueByName(url, "surname") + Utils.getValueByName(url, "name");
            Intent intent = new Intent(this, AddRelationActivity.class);
            intent.putExtra("op_customer_id", op_customer_id);
            intent.putExtra("userName", userName);
            startActivity(intent);
            return true;
        } else if (url.contains("web_share.html?")) {
            try {
                final String title = URLDecoder.decode(Utils.getValueByName(url, "title"), "UTF-8");
                final String imgurl = Utils.getValueByName(url, "imgurl");
                final String shareUrl = Utils.getValueByName(url, "url");
                final String desc = URLDecoder.decode(Utils.getValueByName(url, "desc"), "UTF-8");

                Glide.with(WebActivity.this).load(imgurl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Utils.umengShareByList(WebActivity.this, resource, title, desc, shareUrl);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Utils.umengShareByList(WebActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, desc, shareUrl);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (url.contains("web_action.html?action_id=")) {
            String action_id = Utils.getValueByName(url, "action_id");
            Intent intent = new Intent(this, ActionInfoActivity.class);
            intent.putExtra("action_id", action_id);
            startActivity(intent);
            return true;
        } else if (url.contains("clan_share.html?")) {
            try {
                final String title = URLDecoder.decode(Utils.getValueByName(url, "title"), "UTF-8");
                final String imgurl = Utils.getValueByName(url, "imgurl");
                final String desc = URLDecoder.decode(Utils.getValueByName(url, "desc"), "UTF-8");
                // title=百家姓宗亲会，期待你宝贵的一票
                // &imgurl=http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/system/share.png
                // &url=http://192.168.1.103:8080/yuejian-app/api/clanRelatives/vote_enrolling_voteing_share.html?objid=2478
                // &surname=孙&area_name=广东省%20东莞市&job_id=5
                // &desc=东莞市孙氏宗亲会理事职位选举，期待你投出宝贵的一票
                String shareUrl = Utils.getValueByName(url, "url");
                String surname = Utils.getValueByName(url, "surname");
                String area_name = Utils.getValueByName(url, "area_name");
                String job_id = Utils.getValueByName(url, "job_id");
                shareUrl = shareUrl + "&surname=" + surname + "&area_name=" + area_name + "&job_id=" + job_id;
                final String finalShareUrl = shareUrl;
                Glide.with(WebActivity.this).load(imgurl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Utils.umengShareByList(WebActivity.this, resource, title, desc, finalShareUrl);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Utils.umengShareByList(WebActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, desc, finalShareUrl);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (url.contains("to_app_business_license_certified.html")) {
            Intent intent = new Intent(WebActivity.this, VerifyBusinessActivity.class);
            startActivity(intent);
            return true;
        } else if (url.contains("xingshibaike.html")) {
            startActivity(new Intent(this, SurnameWikiActivity.class));
            return true;
        } else if (url.contains("web_chat.html?")) {
            ImUtils.toP2PCaht(this, Utils.getValueByName(url, "customer_id"));
            return true;
        } else if (url.contains("web_video.html?")) {
            ImUtils.startAudio(this, Utils.getValueByName(url, "customer_id"), AVChatType.VIDEO);
            return true;
        } else if (url.contains("zhongshare.html")) {
            if (isShareSuccess) {
                isShareSuccess = false;
                return true;
            }
            try {
                final String title = URLDecoder.decode(Utils.getValueByName(url, "title"), "UTF-8");
                final String desc = URLDecoder.decode(Utils.getValueByName(url, "desc"), "UTF-8");
                String img = Utils.getValueByName(url, "img");
                shareCustomerId = Utils.getValueByName(url, "customer_id");
                final String shareUrl = Utils.getValueByName(url, "url");
                Glide.with(this).load(img).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
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
        } else if (url.contains("faxian/game_zhuangzhong.html")) {
            isShareSuccess = false;
        } else if (url.contains("payment/chongzhi.html")) {
            Intent intent = new Intent(this, InCashActivity.class);
            startActivity(intent);
            return true;
        } else if (url.contains("duihuangongde.html")) {
            // 点灯和放生 都 拦截 duihuangongde.html  跳转到充值功德页面
            // TODO: 2018/11/21   徐

            Intent intent = new Intent(this, RechargeMeritsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void showPaymentDialog(Long payId) {
        View.OnClickListener zhifubaoPay = v -> doInCash(payId, 1);
        View.OnClickListener wechatPay = v -> doInCash(payId, 2);
        PaymentBottomDialog dialog = new PaymentBottomDialog(this, zhifubaoPay, wechatPay);
        dialog.show();
    }

    private void doInCash(Long payId, int i) {
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("id", String.valueOf(payId));
        params.put("pay_type", String.valueOf(i));
        apiImp.doPayRankDo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (i == 1) {  //支付宝支付
                    final String orderInfo = data;
                    new Thread(() -> {
                        PayTask task = new PayTask(WebActivity.this);
                        Map<String, String> result = task.payV2(orderInfo, true);
                        runOnUiThread(() -> {
                            PayResult payResult = new PayResult(result);
                            String resultStatus = payResult.getResultStatus();
                            if (TextUtils.equals(resultStatus, "9000")) {
                                webView.loadUrl("javascript:toSava()");
                            }
                        });
                    }).start();
                } else if (i == 2) {  //微信支付
                    Log.d("wxPay", data);
                    final WxPayOrderInfo orderInfo = JSON.parseObject(data, WxPayOrderInfo.class);
                    PayReq request = new PayReq();
                    request.appId = Constants.WX_APP_ID;
                    request.partnerId = Constants.WX_PARTNER_ID;
                    request.prepayId = orderInfo.prepay_id;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = orderInfo.nonceStr;
                    request.timeStamp = orderInfo.timeStamp;
                    request.sign = orderInfo.paySign;
                    mIwxapi.sendReq(request);
                }


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
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
        web.setThumb(new UMImage(WebActivity.this, resource));  //缩略图
        web.setDescription(desc);//描述
        shareAction = new ShareAction(WebActivity.this)
                .withMedia(web)
                .setCallback(umShareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        //   , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                );
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


    @OnClick({R.id.webview_titlebar_imgBtn_back, R.id.webview_titlebar_imgBtn_finish})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.webview_titlebar_imgBtn_back:
       /* if (webView.canGoBack()) {
          webView.goBack();
        } else {
          webView.freeMemory();
          finish();
        }*/
                if (youPinGou().contains(orginalUrl)) {
                    finish();
                    break;
                }


                if (webView.canGoBack()) {
                    webView.goBack();
                    //mBackFinish.setVisibility(View.VISIBLE);
                } else {
                    webView.freeMemory();
                    finish();
                }
                break;
            case R.id.webview_titlebar_imgBtn_finish:
                webView.freeMemory();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {


        if (webView.canGoBack()) {
            webView.goBack();

            // mBackFinish.setVisibility(View.VISIBLE);
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
                String filePath = "";
                Uri uri = null;
                if (data.hasExtra(Extras.EXTRA_FILE_PATH)) {
                    filePath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                    uri = Uri.fromFile(new File(filePath));
                } else {
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (null != selectList && selectList.size() > 0) {
                        String url = selectList.get(0).getPath();
                        uri = Uri.fromFile(new File(url));
                    }
//                     uri=data.getData();

                }

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

    @Override
    public void finish() {
        setResult(38);
        super.finish();
    }
}
