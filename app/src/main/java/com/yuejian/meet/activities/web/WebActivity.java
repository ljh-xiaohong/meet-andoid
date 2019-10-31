package com.yuejian.meet.activities.web;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
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
import com.yuejian.meet.BuildConfig;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.family.ArticleActivity;
import com.yuejian.meet.activities.family.VideoActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.ShopEntity;
import com.yuejian.meet.bean.UpdateBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.DownLoadUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.PaymentBottomDialog;
import com.yuejian.meet.widgets.VideoPlayer;

import org.json.JSONException;

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

    public static final String PAY_FOR_POSTER = "PAY_FOR_POSTER";

    public static final String PAY_FOR_VIP = "PAY_FOR_VIP";

    public static final String PAY_FOR_ORDER = "PAY_FOR_ORDER";

    public String LoadingUrl = "";


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
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
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
            Log.e("ansen", "拦截url:" + url);
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
            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
//            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
//            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
//            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Utils.pickPhotos(WebActivity.this, FCR, false, 0, 0, dismissListener);
//            Utils.pickInfor(WebActivity.this, FCR, false, 0, 0, dismissListener);
            return true;
        }
    };

    private String shareCustomerId = "";
    private boolean isVIP = false;
    private String backType="";
    private String updateType="";
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
                        if (checkIsLife()) return;
                        Long payId = JSON.parseObject(data).getLong("id");
                        showPaymentDialog(payId);
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
                return true;
            } else if (uri.getAuthority().equals("poster_share")) {
                try {
                    final String shareUrl = Utils.getValueByName(url, "previewUrl");
                    final String title = URLDecoder.decode(Utils.getValueByName(url, "postersTitle"), "UTF-8");
                    Glide.with(mContext).load(shareUrl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            Utils.umengShareByList(WebActivity.this, bitmap, title, " ", String.format("http://app2.yuejianchina.com/yuejian-app/canvas_haibao/poster_share.html?previewUrl=%s&postersTitle=%s", shareUrl, title));
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            } else if (uri.getAuthority().equals("poster_money")) {
                //海报购买
                LoadingUrl = url;
                buyPosterTemplate(url);
                return true;
            } else if (uri.getAuthority().equals("createShopOrderPay")) {
                String[] s=url.split("&");
                String oid=s[0].split("=")[1];
                String payType=s[1].split("=")[1];
                if (s.length>2) {
                    backType = s[2].split("=")[1];
                }
                isVIP=false;
                //商品购买
                createShopOrderPay(url);
                return true;
            } else if (uri.getAuthority().equals("upgradeVip")) {
                //VIP购买
                upgradeVip(url);
                return true;
            } else if (uri.getAuthority().equals("toBack")) {
                webView.freeMemory();
                Intent i = new Intent();
                setResult(2, i);
                finish();
                return true;
            } else if (uri.getAuthority().equals("meditaVideo")) {
                //冥想寻根
                meditation(url);

                return true;
            } else if (uri.getAuthority().equals("posterSave")) {
                //海报保存
                savePoster(url);
    return true;
            } else if (uri.getAuthority().contains("toBackName")) {
                String[] s=url.split("=");
                DadanPreference.getInstance(this).setString("websurname",url.split("=")[1]);
                BusCallEntity busCallEntity = new BusCallEntity();
                busCallEntity.setCallType(BusEnum.toback);
                Bus.getDefault().post(busCallEntity);
                onBackPressed();
                return true;
            } else if (uri.getAuthority().contains("userPay_inContribution")) {//充值贡献值
                String[] s = url.split("&");
                String customerId = s[0].split("=")[1];
                String silverIngot = s[1].split("=")[1];
                String amount = s[2].split("=")[1];
                String payType = s[3].split("=")[1];
                isVIP = false;
                doInCash(customerId, silverIngot, amount, payType);
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().equals("user_info")) {
                AppUitls.goToPersonHome(this, getIntent().getStringExtra("customer_id"));
                return true;
            } else if (uri.getAuthority().equals("articleVideo")) {
                String id = Utils.getValueByName(url, "id");
                String type = Utils.getValueByName(url, "type");
                String coverSizetype = Utils.getValueByName(url, "coverSizetype");
                String customerID = Utils.getValueByName(orginalUrl, "opCustomerId");
                switch (type) {
                    //文章
                    case "1":
                        ArticleActivity.startActivity(mContext, id, customerID);
                        break;

                    //视频
                    case "2":
                        VideoActivity.startActivity(mContext, id, customerID, coverSizetype.equals("0"));
                        break;
                }
//                Toast.makeText(mContext, id + ":" + type, Toast.LENGTH_SHORT).show();

                return true;
            } else if (uri.getAuthority().contains("updateApp")) {
                initCheck();
                return true;
            } else if (uri.getAuthority().equals("continueWebapp")) {
                return true;
            } else if (uri.getAuthority().contains("gaodeMap")) {
                //yuejian://createShopOrderPay?oid=28&payType=3
                if (CommonUtil.isInstalled(this, "com.autonavi.minimap")) {
                    String[] s = url.split("&");
                    String end = s[1].split("=")[1];
                    String dlat = end.split(",")[0];
                    String dlon = end.split(",")[1];
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?sid=&slat=&slon=&sname=&did=&dlat="
                            + dlat + "&dlon=" + dlon + "&dname=&dev=0&t=0&sourceApplication=" + this.getPackageName()));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.casht_text10, Toast.LENGTH_SHORT).show();
                }
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().contains("baiduMap")) {
                //yuejian://createShopOrderPay?oid=28&payType=3
                if (CommonUtil.isInstalled(this, "com.baidu.BaiduMap")) {
                    String[] s = url.split("&");
                    String str = s[0].split("=")[1];
                    String end = s[1].split("=")[1];
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("baidumap://map/direction?destination=name=|latlng:"
                            + end + "&coord_type=gcj02" + "&src=" + this.getPackageName()));
                    startActivity(intent); // 启动调用
                } else {
                    Toast.makeText(this, R.string.casht_text11, Toast.LENGTH_SHORT).show();
                }
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().contains("updateImage")) {//图片上传
                String[] s = url.split("=");
                updateType=s[1];
                showBottomPopupWindow();
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().contains("tel")) {//打电话
                String[] s = url.split("=");
                CommonUtil.call(this, s[1]);
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().contains("sharaTui")) {//分享
                //'yuejian://sharaTui?url=http://app2.yuejianchina.com/yuejian-app/shara_register.html'+'&type='+type+'&referralMobile='+referralMobile+'&name='+name
                String[] s = url.split("&");
                String shareUrl = s[0].split("url=")[1];
                String type = s[1].split("=")[1];
                String name = s[2].split("=")[1];
                name = URLDecoder.decode(name);
                if (type.equals("1")) {
                    Utils.umengShareForPhatForm(SHARE_MEDIA.WEIXIN_CIRCLE, this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), name + "邀请您注册《百家姓氏》", " ", shareUrl);
                } else if (type.equals("2")) {
                    Utils.umengShareForPhatForm(SHARE_MEDIA.WEIXIN, this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), name + "邀请您注册《百家姓氏》", " ", shareUrl);
                } else if (type.equals("3")) {
//                    Utils.umengShareForPhatForm(SHARE_MEDIA.QQ, getActivity(), BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), name+"邀请您注册《百家姓氏》", " ", shareUrl);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, name + "邀请您注册《百家姓氏》" + "\n" + shareUrl);
                    sendIntent.setType("text/plain");
                    sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ好友或QQ群
                    startActivityForResult(sendIntent, 100);
                } else if (type.equals("4")) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(shareUrl);
                    Toast.makeText(this, "已复制", Toast.LENGTH_LONG).show();
                } else {
                    Utils.umengShareByList(this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), name + "邀请您注册《百家姓氏》", " ", shareUrl);
                }
                return true;//表示我已经处理过了
            } else if (uri.getAuthority().contains("toBackLoad")) {//退出
                Dialog dialog = DialogUtils.createTwoBtnDialog(WebActivity.this, "提示", "是否退出登录", "取消", "确定");
                dialog.show();
                DialogUtils.setOnTitleViewClickListener(new DialogUtils.OnTitleViewClickListener() {
                    @Override
                    public void onTitleViewClick() {
                        DadanPreference.getInstance(WebActivity.this).setBoolean("isLogin", false);
                        DadanPreference.getInstance(WebActivity.this).setString("CustomerId", "");
                        DadanPreference.getInstance(WebActivity.this).setString("photo", "");
                        DadanPreference.getInstance(WebActivity.this).setString("surname", "");
                        startActivity(new Intent(WebActivity.this, LoginActivity.class));
                        AppManager.finishAllActivity();
                    }
                });
                return true;//表示我已经处理过了
            }
        }
        if (url.contains("/pay/aliPayResult?") && isApplyFamilyMaster) {
//            startActivity(new Intent(getApplication(), SelectGoodsActivity.class));
//            finish();
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
//            String op_customer_id = Utils.getValueByName(url, "op_customer_id");
//            String userName = Utils.getValueByName(url, "surname") + Utils.getValueByName(url, "name");
//            Intent intent = new Intent(this, AddRelationActivity.class);
//            intent.putExtra("op_customer_id", op_customer_id);
//            intent.putExtra("userName", userName);
//            startActivity(intent);
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
//            String action_id = Utils.getValueByName(url, "action_id");
//            Intent intent = new Intent(this, ActionInfoActivity.class);
//            intent.putExtra("action_id", action_id);
//            startActivity(intent);
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
//            Intent intent = new Intent(WebActivity.this, VerifyBusinessActivity.class);
//            startActivity(intent);
            return true;
        } else if (url.contains("xingshibaike.html")) {
//            startActivity(new Intent(this, SurnameWikiActivity.class));
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
//            Intent intent = new Intent(this, RechargeMeritsActivity.class);
//            startActivity(intent);
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

    private void savePoster(final String url) {

        String state = Utils.getValueByName(url, "toBack");
        if (state.equalsIgnoreCase("true")) {
            Bus.getDefault().getDefault().post(new ShopEntity());
            onBackPressed();
        } else {
            Bus.getDefault().getDefault().post(new ShopEntity());
        }

    }

    /**
     * 冥想寻根
     */
    private void meditation(final String url) {
        ;
        VideoActivity.startActivity(mContext, Utils.getValueByName(url, "url"), VideoPlayer.MODEL.MEDITATION, false);
    }

    /**
     * 购买商城订单
     */
    private void createShopOrderPay(final String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("oid", Utils.getValueByName(url, "oid"));
        params.put("payType", Utils.getValueByName(url, "payType"));
        params.put("outCashPassword", "");
        apiImp.createShopOrderPay(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                if (data == null) return;
                JSONObject jo = JSON.parseObject(data);
                if (jo == null || !jo.getString("code").equals("0")) return;
                switch (Utils.getValueByName(url, "payType")) {
                    case "1":
                        //支付宝
                        new Thread(() -> {
                            if (checkIsLife())
                                return;
                            PayTask task = new PayTask(WebActivity.this);
                            Map<String, String> result = task.payV2(jo.getString("data"), true);
                            runOnUiThread(() -> {
                                if (checkIsLife()) return;
                                if (!isAliPayInstalled(mContext)) {
                                    ViewInject.shortToast(mContext, "请先安装支付宝，再进行支付");
                                    return;
                                }
                                PayResult payResult = new PayResult(result);
                                String resultStatus = payResult.getResultStatus();
                                if (TextUtils.equals(resultStatus, "9000")) {
                                    if (!CommonUtil.isNull(backType)) {
                                        webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType=" + backType + "&customerId=" + AppConfig.CustomerId);
                                    }else {
                                        webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?customerId=" + AppConfig.CustomerId);
                                    }
                                }
                            });
                        }).start();
                        break;

                    case "2":
                        //微信
                        if (!mIwxapi.isWXAppInstalled()) {
                            ViewInject.shortToast(mContext, "请先安装微信，再进行支付");
                            return;
                        }
                        final WxPayOrderInfo orderInfo = JSON.parseObject(jo.getString("data"), WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = Constants.WX_APP_ID;
                        request.partnerId = Constants.WX_PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        request.extData = PAY_FOR_ORDER;
                        mIwxapi.sendReq(request);
                        break;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    private void initCheck() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        apiImp.getLastVersionByType(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                UpdateBean loginBean=new Gson().fromJson(data, UpdateBean.class);
                if (loginBean.getData()==null) return;
                versions=loginBean.getData().getVersionName();
                isForcedUpdating=loginBean.getData().getIsForced()==1?true:false;
                versionsInfo=loginBean.getData().getContent();
                andriodDownloadURL=loginBean.getData().getAppUrl();
                checkUpdate();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    String versions;
    boolean  isForcedUpdating;
    String versionsInfo;
    String andriodDownloadURL;
    private void checkUpdate() {
        boolean isUpdate;
        if (versions.equals(BuildConfig.VERSION_NAME)){
            isUpdate = false;
        }else {
            isUpdate = true;
        }
        if (isUpdate) {
            if (isForcedUpdating) {
                showForcedUpdatingDialog();
            } else {
                showNoForcedUpdatingDialog();
            }
        }else {
            Toast.makeText(this,"已经是最新版本！",Toast.LENGTH_LONG).show();
        }
    }
    //强制更新
    private void showForcedUpdatingDialog() {
        LayoutInflater inflater = (LayoutInflater)this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_tips_layout_update, null);
        message = (TextView) layout.findViewById(R.id.message);
        positiveButton = (TextView) layout.findViewById(R.id.positiveButton);
        ImageView cancel_img = (ImageView) layout.findViewById(R.id.cancel_img);
        cancel_img.setVisibility(View.GONE);
        tv_download_progressBar = (ProgressBar) layout.findViewById(R.id.download_progressBar);
        positiveButton.setOnClickListener(v -> {
            int isPermission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (isPermission2 == PackageManager.PERMISSION_GRANTED) {
                download();
            } else {
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        });
        Dialog dialog = new Dialog(this);// 创建自定义样式dialog
        dialog.setCancelable(false);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.setContentView(layout);// 设置布局
        dialog.show();
    }
    private static final int PERMISSION_REQUEST_CODE = 0;
    //非强制更新
    private void showNoForcedUpdatingDialog() {
        LayoutInflater inflater = (LayoutInflater)this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_tips_layout_update, null);
        tv_download_progressBar = (ProgressBar) layout.findViewById(R.id.download_progressBar);
        message = (TextView) layout.findViewById(R.id.message);
        positiveButton = (TextView) layout.findViewById(R.id.positiveButton);
        ImageView cancel_img = (ImageView) layout.findViewById(R.id.cancel_img);
        cancel_img.setVisibility(View.VISIBLE);
        Dialog dialog = new Dialog(this);// 创建自定义样式dialog
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(true);//
        dialog.setContentView(layout);// 设置布局
        dialog.show();
        cancel_img.setOnClickListener(v -> {
            dialog.dismiss();
        });
        positiveButton.setOnClickListener(v -> {
            int isPermission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (isPermission2 == PackageManager.PERMISSION_GRANTED) {
                download();
            } else {
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        });
    }

    private ProgressBar tv_download_progressBar;
    private TextView message;
    private TextView positiveButton;
    //下载
    private void download() {
        String fileDownloadPath = "yuejian/";
        String fileName = "";//文件名
        String fileRootPath = Environment.getExternalStorageDirectory() + File.separator;
        /*文件名*/
        fileName = andriodDownloadURL.substring(andriodDownloadURL.lastIndexOf("/") + 1);
        /*下载目录*/
        File downloadfile = new File(fileRootPath + fileDownloadPath + fileName);
        tv_download_progressBar.setMax(100);
        if (downloadfile.exists()) {
            if (positiveButton != null) {
                if (message != null) {
                    message.setText("下载完成");
                }
                tv_download_progressBar.setProgress(100);
                tv_download_progressBar.setVisibility(View.VISIBLE);
                positiveButton.setEnabled(true);
                positiveButton.setText("点击安装");
            }
            DownLoadUtils.installApp(this, fileRootPath + fileDownloadPath + fileName);
        } else {
            positiveButton.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DownLoadUtils.DownloadFile(andriodDownloadURL, WebActivity.this, tv_download_progressBar, null, null, null, message, positiveButton);
                }
            }).start();
        }
    }

    /**
     * 购买VIP
     *
     * @param url
     */
    private void upgradeVip(final String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("payType", Utils.getValueByName(url, "payType"));
        params.put("outCashPassword", Utils.getValueByName(url, "outCashPassword"));
        apiImp.upgradeVip(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                if (data == null) return;
                JSONObject jo = JSON.parseObject(data);
                if (jo == null || !jo.getString("code").equals("0")) return;
                switch (Utils.getValueByName(url, "payType")) {
                    case "1":
                        //支付宝
                        new Thread(() -> {
                            PayTask task = new PayTask(WebActivity.this);
                            Map<String, String> result = task.payV2(jo.getString("data"), true);
                            runOnUiThread(() -> {
                                if (checkIsLife()) return;
                                if (!isAliPayInstalled(mContext)) {
                                    ViewInject.shortToast(mContext, "请先安装支付宝，再进行支付");
                                    return;
                                }
                                PayResult payResult = new PayResult(result);
                                String resultStatus = payResult.getResultStatus();
                                if (TextUtils.equals(resultStatus, "9000")) {
                                    webView.loadUrl("javascript:reloadHome()");
                                }
                            });
                        }).start();
                        break;

                    case "2":
                        //微信
                        if (!mIwxapi.isWXAppInstalled()) {
                            ViewInject.shortToast(mContext, "请先安装微信，再进行支付");
                            return;
                        }
                        final WxPayOrderInfo orderInfo = JSON.parseObject(jo.getString("data"), WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = Constants.WX_APP_ID;
                        request.partnerId = Constants.WX_PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        request.extData = PAY_FOR_VIP;
                        mIwxapi.sendReq(request);
                        break;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 购买海报
     */
    private void buyPosterTemplate(final String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);//用户id
        params.put("postersId", Utils.getValueByName(url, "postersId"));//用户海报模板制作记录id
        params.put("payType", Utils.getValueByName(url, "payType"));//充值方式（1支付宝2微信3ApplePay4贡献值）
        params.put("outCashPassword", "");//支付密码
        apiImp.buyPosterTemplate(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;

                if (data == null) return;
                JSONObject jo = JSON.parseObject(data);
                if (jo == null || !jo.getString("code").equals("0")) return;


                switch (Utils.getValueByName(url, "payType")) {
                    case "1":
                        //支付宝
                        new Thread(() -> {
                            PayTask task = new PayTask(WebActivity.this);
                            Map<String, String> result = task.payV2(jo.getString("data"), true);
                            runOnUiThread(() -> {
                                if (checkIsLife())
                                    return;
                                if (!isAliPayInstalled(mContext)) {
                                    ViewInject.shortToast(mContext, "请先安装支付宝，再进行支付");
                                    return;
                                }
                                PayResult payResult = new PayResult(result);
                                String resultStatus = payResult.getResultStatus();
                                if (TextUtils.equals(resultStatus, "9000")) {
                                    try {
                                        final String shareUrl = Utils.getValueByName(url, "previewUrl");
                                        final String title = URLDecoder.decode(Utils.getValueByName(url, "postersTitle"), "UTF-8");
                                        Glide.with(mContext).load(shareUrl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                Utils.umengShareByList(WebActivity.this, bitmap, title, " ", String.format("http://app2.yuejianchina.com/yuejian-app/canvas_haibao/poster_share.html?previewUrl=%s&postersTitle=%s", shareUrl, title));
                                            }
                                        });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }).start();
                        break;

                    case "2":
                        //微信

                        if (!mIwxapi.isWXAppInstalled()) {
                            ViewInject.shortToast(mContext, "请先安装微信，再进行支付");
                            return;
                        }
                        final WxPayOrderInfo orderInfo = JSON.parseObject(jo.getString("data"), WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = Constants.WX_APP_ID;
                        request.partnerId = Constants.WX_PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        request.extData = PAY_FOR_POSTER;
                        mIwxapi.sendReq(request);
                        break;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    //充值贡献值API payType 1:支付宝，2.微信，3.3ApplePay
    private void doInCash(String customerId, String silverIngot, String amount, String payType) {
        if (payType.equals("2")) {
            if (!Utils.isWeixinAvilible(this)) {
                Toast.makeText(this, R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (payType.equals("1")) {
            if (!Utils.isAliPayInstalled(this)) {
                Toast.makeText(this, R.string.casht_text9, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("silverIngot", silverIngot);
        params.put("amount", amount);
        params.put("payType", payType);
        apiImp.inContribution(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (payType.equals("1")) {
                    try {
                        org.json.JSONObject oo = new org.json.JSONObject(data);
                        final String orderInfo = oo.getString("data");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask task = new PayTask(WebActivity.this);
                                Map<String, String> result = task.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        }).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (payType.equals("2")) {
                    try {
                        org.json.JSONObject oo = new org.json.JSONObject(data);
                        if (oo == null) return;
                        final String data1 = oo.getString("data");
                        final WxPayOrderInfo orderInfo = JSON.parseObject(data1, WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = Constants.WX_APP_ID;
                        request.partnerId = Constants.WX_PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        mIwxapi.sendReq(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(WebActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
                    if (isVIP) {
                        reloadHome();
                    }
                    if (!CommonUtil.isNull(backType))
                        if (!CommonUtil.isNull(backType)) {
                            webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType=" + backType + "&customerId=" + AppConfig.CustomerId);
                        }else {
                            webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?customerId=" + AppConfig.CustomerId);
                        }
                }
            }
            return false;
        }
    });

    public void reloadHome() {
        try {
            webView.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    // 注意调用的JS方法名要对应上
                    // 调用javascript的callJS()方法
                    webView.evaluateJavascript("javascript:reloadHome()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.e("value", value);
                        }
                    });
                }
            });
        } catch (Exception e) {

        }
    }
    public void getImg() {
        try {
            webView.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    // 注意调用的JS方法名要对应上
                    // 调用javascript的callJS()方法
                    webView.evaluateJavascript("javascript:reloadHome()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.e("value", value);
                        }
                    });
                }
            });
        } catch (Exception e) {

        }
    }
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(this);
            mPoupView = mInflater.inflate(R.layout.dialog_edit_head_photo, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
            mPoupWindow.setTouchable(true);
            mPoupWindow.setFocusable(true);
            mPoupWindow.setOutsideTouchable(true);
            mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        mPoupWindow.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view) {
        TextView txtToPhoto = (TextView) view.findViewById(R.id.txt_dialog_photo);
        TextView txtToAlbum = (TextView) view.findViewById(R.id.txt_dialog_album);
        TextView txtSavePic = (TextView) view.findViewById(R.id.txt_dialog_save_pic);
        TextView txtCancel = (TextView) view.findViewById(R.id.txt_dialog_cancel);
        txtToPhoto.setOnClickListener(v -> {
            showSelector(false);
            mPoupWindow.dismiss();});
        txtSavePic.setOnClickListener(this);
        txtSavePic.setVisibility(View.GONE);
        txtToAlbum.setOnClickListener(v -> {
             showSelector(true);
             mPoupWindow.dismiss();});
        txtCancel.setOnClickListener(v -> mPoupWindow.dismiss());
    }
    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    /**
     * 打开图片/拍照选择器（完成）
     */
    private void showSelector(Boolean isOpen) {

        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, OPENPIC, from, outputPath, false, 1,
                    false, true, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_WIDTH);
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, OPENCAM, from, outputPath, false, 1,
                    false, true, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_WIDTH);
        }
    }

    private void doInCash(Long payId, int i) {
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("id", String.valueOf(payId));
        params.put("pay_type", String.valueOf(i));
        apiImp.doPayRankDo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
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
                onBackPressed();
                break;
            case R.id.webview_titlebar_imgBtn_finish:
                webView.freeMemory();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView==null)return;
        if (webView.getUrl().contains("suefulPayment")){
            webView.loadUrl("javascript:toBack()");
        }
        if (webView.canGoBack()) {
            webView.goBack();

            // mBackFinish.setVisibility(View.VISIBLE);
        } else {
            webView.freeMemory();
            Intent i = new Intent();
            setResult(2, i);
            finish();
        }
            if(mPoupWindow==null)return;
            if (mPoupWindow.isShowing()){
                mPoupWindow.dismiss();
                backgroundAlpha(1f);
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
        if (checkIsLife()) return;
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
            }else if (requestCode == OPENPIC) {
                if (resultCode == RESULT_OK) {
                    updateUserImg();
                    return;
                }
            }  else if (requestCode == OPENCAM) {
                if (resultCode == RESULT_OK) {//outputPath
                    updateUserImg();
                    return;
                }
            }
        } else if (requestCode == 10103) {
            webView.loadUrl("javascript:shareBack(" + shareCustomerId + ")");
            isShareSuccess = true;
        } else if (requestCode == 100) {

        }  else {
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
                mUMA = null;
            } else if (mUM != null) {
                mUM.onReceiveValue(null);
                mUM = null;
            }
        }
    }
    LoadingDialogFragment dialog;
    public void updateUserImg() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        if (!outputPath.equals("")) {
            String updtaImgName = OssUtils.getTimeNmaeJpg();
            new FeedsApiImpl().upLoadImageFileToOSS(outputPath, updtaImgName, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (dialog != null)
                    dialog.dismiss();
//                    Toast.makeText(WebActivity.this, data.imageUrl, Toast.LENGTH_LONG).show();
//                    getImg("");
                    Log.e("asfsaf",data.getImageUrl());
                    if (updateType.equals("1")){
                        try {
                            webView.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    // 注意调用的JS方法名要对应上
                                    // 调用javascript的callJS()方法
                                    webView.evaluateJavascript("javascript:updatePhoto('" + data.getImageUrl() + "')", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            //此处为 js 返回的结果
                                            Log.e("asfsaf",data.getImageUrl());
                                            Log.e("value", value);
                                        }
                                    });
                                }
                            });
                        }catch (Exception e){

                        }
                    }else if (updateType.equals("2")){
//                        webView.loadUrl("javascript:updateCover(" + data.getImageUrl() + ")");
                        try {
                            webView.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    // 注意调用的JS方法名要对应上
                                    // 调用javascript的callJS()方法
                                    webView.evaluateJavascript("javascript:updateCover('" + data.getImageUrl() + "')", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            //此处为 js 返回的结果
                                            Log.e("asfsaf",data.getImageUrl());
                                            Log.e("value", value);
                                        }
                                    });
                                }
                            });
                        }catch (Exception e){

                        }
                    }else if (updateType.equals("3")){
//                        webView.loadUrl("javascript:proNewImage(" +data.getImageUrl() + ")");
                        try {
                            webView.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    // 注意调用的JS方法名要对应上
                                    // 调用javascript的callJS()方法
                                    webView.evaluateJavascript("javascript:proNewImage('" + data.getImageUrl() + "')", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            //此处为 js 返回的结果
                                            Log.e("asfsaf",data.getImageUrl());
                                            Log.e("value", value);
                                        }
                                    });
                                }
                            });
                        }catch (Exception e){

                        }
                    }else if (updateType.equals("4")){
//                        webView.loadUrl("javascript:updateOldImage(" + data.getImageUrl() + ")");
                        try {
                            webView.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    // 注意调用的JS方法名要对应上
                                    // 调用javascript的callJS()方法
                                    webView.evaluateJavascript("javascript:updateOldImage('" + data.getImageUrl() + "')", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            //此处为 js 返回的结果
                                            Log.e("asfsaf",data.getImageUrl());
                                            Log.e("value", value);
                                        }
                                    });
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (dialog != null)
                        dialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        }
    }
    @Override
    public void onClick(View v) {

    }

    @BusReceiver
    public void onAfterPayEvent(BusCallEntity event) {
        if (checkIsLife()) return;
        if (event == null || TextUtils.isEmpty(event.getData())) return;
        if (event.getCallType() == BusEnum.payment_success) {
            switch (event.getData()) {
                //购买海报成功
                case PAY_FOR_POSTER:
                    if (webView != null) {
                        try {
                            webView.loadUrl("javascript:onload()");
                            final String shareUrl = Utils.getValueByName(LoadingUrl, "previewUrl");
                            final String title = URLDecoder.decode(Utils.getValueByName(LoadingUrl, "postersTitle"), "UTF-8");
                            Glide.with(mContext).load(shareUrl).asBitmap().error(R.mipmap.app_logo).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Utils.umengShareByList(WebActivity.this, bitmap, title, " ", String.format("http://app2.yuejianchina.com/yuejian-app/canvas_haibao/poster_share.html?previewUrl=%s&postersTitle=%s", shareUrl, title));
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                //购买VIP成功
                case PAY_FOR_VIP:
                    if (webView != null) {
                        webView.loadUrl("javascript:reloadHome()");
                    }
                    break;
                //购买商品成功
                case PAY_FOR_ORDER:
                    if (webView != null) {
                        if (!CommonUtil.isNull(backType)) {
                            webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType=" + backType + "&customerId=" + AppConfig.CustomerId);
                        }else {
                            webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?customerId=" + AppConfig.CustomerId);
                        }
                    }
                    break;
            }
        }
    }

    @BusReceiver
    public void onStringEvent(String event) {
        if (checkIsLife()) return;

        switch (event) {
            case "wx_share_success":
                if (webView != null) {
                    webView.loadUrl("javascript:shareBack(" + shareCustomerId + ")");
                    isShareSuccess = true;
                }
                break;
        }
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.payment_success){
            if (isVIP){
                reloadHome();
            }
            if (!CommonUtil.isNull(backType)) {
                webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType=" + backType + "&customerId=" + AppConfig.CustomerId);
            }else {
                webView.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?customerId=" + AppConfig.CustomerId);
            }
        }
    }
    @Override
    public void finish() {
        setResult(38);
        super.finish();
    }

    public boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


}
