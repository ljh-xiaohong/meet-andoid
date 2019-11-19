package com.yuejian.meet.framents.business;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.amap.api.maps.model.LatLng;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.VideoActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.VideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ：
 * @time : 2018/11/17 16:59
 * @desc : 首页 我的
 * @version: V1.0
 * @update : 2018/11/17 16:59
 */

public class NewBusinessFragment extends BaseFragment {

    @Bind(R.id.wx_webview)
    WebView wxWebview;
    private WebSettings ws;


    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_new_mine, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        setws();
        initView();
        initWxPayApi();
    }
    public void update(){
        if (wxWebview==null) return;
        setws();
        initView();

    }
    private void initView() {
        if (!CommonUtil.isNull(AppConfig.city)){
            AppConfig.city="珠海市";
        }
        DadanPreference.getInstance(getActivity()).setString("webcity",AppConfig.city);
        DadanPreference.getInstance(getActivity()).setString("websurname",AppConfig.surname);
        wxWebview.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/family/index.html?customerId="+AppConfig.CustomerId+"&surname="+AppConfig.surname+"&city="+AppConfig.city);
        wxWebview.addJavascriptInterface(new JSInterface(), "webJs");//添加js监听 这样html就能调用客户端
        wxWebview.setWebChromeClient(webChromeClient);
        wxWebview.setWebViewClient(webViewClient);
    }
    @SuppressWarnings("unused")
    public class JSInterface extends Object {
        //微信分享
        @JavascriptInterface
        public void share(String url, String description) {
            if (!Utils.isWeixinAvilible(getActivity())) {
                Toast.makeText(getActivity(), R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }else {
//                initShareSelectPopupwindow(url, description);
            }
        }
    }
    private void setws() {
        // TODO Auto-generated method stub
        ws = wxWebview.getSettings();
        ws.setJavaScriptEnabled(true);//允许使用js
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        ws.setCacheMode(ws.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setSaveFormData(true);
        ws.setAppCacheEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setGeolocationEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDefaultTextEncodingName("utf-8");
        ws.setDisplayZoomControls(false);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(true);
    }
    private IWXAPI iwxapi;
    private final static String APP_ID = Constants.WX_APP_ID;
    private final static String PARTNER_ID = Constants.WX_PARTNER_ID;
    public void reloadHome() {
        try {
            wxWebview.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    // 注意调用的JS方法名要对应上
                    // 调用javascript的callJS()方法
                    wxWebview.evaluateJavascript("javascript:reloadHome()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.e("value", value);
                        }
                    });
                }
            });
        }catch (Exception e){

        }
    }
    private void initWxPayApi() {
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), APP_ID);
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(getActivity(), R.string.payment_success, Toast.LENGTH_SHORT).show();
                }
                if (isVIP){
                    reloadHome();
                }
                if (!CommonUtil.isNull(backType))
                    wxWebview.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType="+backType+"&customerId"+AppConfig.CustomerId);
            }
            return false;
        }
    });
    private String backType="";
    private boolean isVIP=false;
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("ansen", "拦截url:" + url);
          if (url.contains("yuejian://createShopOrderPay")){
               //yuejian://createShopOrderPay?oid=28&payType=3
               String[] s=url.split("&");
               String oid=s[0].split("=")[1];
               String payType=s[1].split("=")[1];
                if (s.length>2) {
                    backType = s[2].split("=")[1];
                }
                doInCash(oid,payType);
                return true;//表示我已经处理过了
            }else if (url.contains("yuejian://upgradeVip")){ //vip升级
                String[] s=url.split("&");
                String customerId=s[0].split("=")[1];
                String payType=s[1].split("=")[1];
                String outCashPassword="";
                if (s[2].split("=").length>1){
                    outCashPassword=s[2].split("=")[1];
                }
                isVIP=true;
                doInCashVip(customerId,payType,outCashPassword);
                return true;//表示我已经处理过了
            }else if (url.contains("yuejian://gaodeMap")){
                //yuejian://createShopOrderPay?oid=28&payType=3
                if (CommonUtil.isInstalled(getActivity(),"com.autonavi.minimap")){
                    String[] s=url.split("&");
                    String end=s[1].split("=")[1];
                    String dlat=end.split(",")[0];
                    String dlon=end.split(",")[1];
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?sid=&slat=&slon=&sname=&did=&dlat="
                            + dlat+"&dlon=" +dlon+ "&dname=&dev=0&t=0&sourceApplication="+ getActivity().getPackageName()));
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), R.string.casht_text10, Toast.LENGTH_SHORT).show();
                }
                return true;//表示我已经处理过了
            }else if (url.contains("yuejian://baiduMap")){
                //yuejian://createShopOrderPay?oid=28&payType=3
                if (CommonUtil.isInstalled(getActivity(),"com.baidu.BaiduMap")){
                    String[] s=url.split("&");
                    String str=s[0].split("=")[1];
                    String end=s[1].split("=")[1];
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("baidumap://map/direction?destination=name=|latlng:"
                            + end +"&coord_type=gcj02"+ "&src=" + getActivity().getPackageName()));
                    startActivity(intent); // 启动调用
                }else {
                    Toast.makeText(getActivity(), R.string.casht_text11, Toast.LENGTH_SHORT).show();
                }
                return true;//表示我已经处理过了
            }else if (url.contains("yuejian://tel")){//打电话
                    String[] s=url.split("=");
                    CommonUtil.call(getActivity(),s[1]);
                return true;//表示我已经处理过了
            } else if (url.contains("yuejian://meditaVideo")) {
                meditation(url);
                return true;//表示我已经处理过了
            } else if (!url.contains("index.html") && !url.contains("clan.html") && !url.contains("familyTab.html") && !url.contains("familyTradition.html")) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(Constants.URL, url + "&phone=true");
                intent.putExtra("No_Title", true);
                startActivity(intent);
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };


    private void doInCashVip(String customerId, String payType, String outCashPassword) {
        if (payType.equals("2")) {
            if (!Utils.isWeixinAvilible(getActivity())) {
                Toast.makeText(getActivity(), R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        }else if (payType.equals("1")){
            if (!Utils.isAliPayInstalled(getActivity())) {
                Toast.makeText(getActivity(), R.string.casht_text9, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customerId",customerId);
        params.put("payType", payType);
        params.put("outCashPassword", outCashPassword);
        apiImp.upgradeVip(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (payType.equals("1")) {
                    try {
                        JSONObject oo = new JSONObject(data);
                        final String orderInfo =oo.getString("data");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask task = new PayTask(getActivity());
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
                        JSONObject  oo = new JSONObject(data);
                        if (oo==null) return;
                        final String data1 =oo.getString("data");
                        final WxPayOrderInfo orderInfo = JSON.parseObject(data1, WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = APP_ID;
                        request.partnerId = PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        iwxapi.sendReq(request);
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
    //sourceType 1:支付宝，2.微信，3.银联方式
    private void doInCash(String oid,String payType) {
        if (payType.equals("2")) {
            if (!Utils.isWeixinAvilible(getActivity())) {
                Toast.makeText(getActivity(), R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        }else if (payType.equals("1")){
            if (!Utils.isAliPayInstalled(getActivity())) {
                Toast.makeText(getActivity(), R.string.casht_text9, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("oid",oid);
        params.put("payType", payType);
        params.put("outCashPassword", "");
        apiImp.createShopOrderPay(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (payType.equals("1")) {
                    try {
                        JSONObject oo = new JSONObject(data);
                        final String orderInfo =oo.getString("data");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask task = new PayTask(getActivity());
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
                        JSONObject  oo = new JSONObject(data);
                        final String data1 =oo.getString("data");
                        final WxPayOrderInfo orderInfo = JSON.parseObject(data1, WxPayOrderInfo.class);
                        PayReq request = new PayReq();
                        request.appId = APP_ID;
                        request.partnerId = PARTNER_ID;
                        request.prepayId = orderInfo.prepay_id;
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = orderInfo.nonceStr;
                        request.timeStamp = orderInfo.timeStamp;
                        request.sign = orderInfo.paySign;
                        iwxapi.sendReq(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    /**
     * 冥想寻根
     */
    private void meditation(final String url) {
        ;
        VideoActivity.startActivity(mContext, Utils.getValueByName(url, "url"), VideoPlayer.MODEL.MEDITATION, false);
    }

    private String url;
    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onBackPressed() {
        if (wxWebview.canGoBack()){
            wxWebview.goBack();
            return true;
        }
        return super.onBackPressed();
    }
    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.payment_success){
            if (isVIP){
                reloadHome();
            }
            if (!CommonUtil.isNull(backType))
                wxWebview.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/order/suefulPayment.html?backType="+backType+"&customerId="+AppConfig.CustomerId);
        }else if (event.getCallType() == BusEnum.toback){
            wxWebview.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/family/clan.html?customerId="+AppConfig.CustomerId+"&surnameList="
                    +DadanPreference.getInstance(getActivity()).getString("websurname")+"&surname="+AppConfig.surname+"&city="+DadanPreference.getInstance(getActivity()).getString("webcity"));
        }else if (event.getCallType() == BusEnum.toproject){
            wxWebview.loadUrl("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/family/familyTab.html?customerId="+AppConfig.CustomerId
                    +"&surname="+AppConfig.surname+"&industry="+DadanPreference.getInstance(getActivity()).getString("industry"));
        }
    }
}
