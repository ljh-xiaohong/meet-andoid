package com.yuejian.meet.bean;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ClaimSucceedActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.mine.VerifyIdCardActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 传承使者认领合同
 * 城市认领合同
 * */
public class ClaimPactActivity extends BaseActivity{
    @Bind(R.id.pact_web)
    WebView mWebView;
    @Bind(R.id.pact_button)
    Button button;
    @Bind(R.id.pact_cb)
    CheckBox pact_cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_pact);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.Participate_in_the_claim5));
        button.setSelected(true);
        button.setEnabled(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        mWebView.setWebViewClient(mWebViewClient);// 新建浏览器客户端，不调用系统浏览器
        mWebView.loadUrl(UrlConstant.ExplainURL.CONTRACT+"?customer_id="+AppConfig.CustomerId+"&token="+user.getToken());
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //用javascript隐藏系统定义的404页面信息
//            String data = "Page NO FOUND！";
//            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
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

    @Override
    protected void onResume() {
        super.onResume();
        requstData();
    }
    public void requstData(){
        final Map<String,Object> params=new HashMap<>();
        params.put("family_id",user.getFamily_id());
        new ApiImp().getFamilyMaster(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ClaimPactEntity entity= JSON.parseObject(data,ClaimPactEntity.class);
                if (entity.getApply_status()==0){//审核中
                    startActivity(new Intent(getApplication(),ClaimSucceedActivity.class));
                    finish();
                    return;
                }else if (entity.getApply_status()==1){
                    ViewInject.shortToast(getApplication(),R.string.Participate_in_the_claim6);
                    finish();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @OnClick({R.id.pact_button,R.id.pact_cb})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pact_button:
                getUserVerify();
                break;
            case R.id.pact_cb:
                if (!pact_cb.isChecked()){
                    button.setSelected(false);
                    button.setEnabled(false);
                }else {
                    button.setEnabled(true);
                    button.setSelected(true);
                }
                break;
        }
    }
    public void getUserVerify(){
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage(getString(R.string.in_operation));
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        new ApiImp().findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog!=null)dialog.dismiss();
                Mine mine = JSON.parseObject(data, Mine.class);
                if (mine.getIs_idcard_certified().equals("1")){
                    Intent intent=new Intent(mContext, InCashActivity.class);
                    intent.putExtra("isUpVip","1");
                    startActivity(intent);
                }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.hint);
                    builder.setMessage(R.string.cultural_inheritance3);
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ViewInject.shortToast(getApplication(),R.string.cultural_inheritance3);
                        }
                    });
                    builder.show();
//                    startActivity(new Intent(this, VerifyIdCardActivity.class));
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }
}
