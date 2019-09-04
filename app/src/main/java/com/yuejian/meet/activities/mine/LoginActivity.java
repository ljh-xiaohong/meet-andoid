package com.yuejian.meet.activities.mine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.SplashAdapter;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.custom.scoller.ScollLinearLayoutManager;
import com.yuejian.meet.activities.gile.BangDingPhoneActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.EnjoyEntity;
import com.yuejian.meet.bean.LoginBean;
import com.yuejian.meet.bean.QqLoginBean;
import com.yuejian.meet.bean.WxLoginBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    Intent intent;
    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.exit_iv)
    ImageView exitIv;
    @Bind(R.id.phone_login)
    LinearLayout phoneLogin;
    @Bind(R.id.img_login_wx)
    LinearLayout imgLoginWx;
    @Bind(R.id.login_privacy)
    TextView loginPrivacy;

    private String phoneVersion;
    private String phoneModel;
    private String phoneImei;

    private ApiImp api = new ApiImp();
    private UMShareAPI umShareAPI;
    private UMShareConfig config;
    private LoadingDialogFragment dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "登录页面";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_start);
        initView();
        Utils.versionUpdate(this);
        AppManager.finishOtherActivitis(this);
    }

    public void initView() {
        mRecyclerView.setAdapter(new SplashAdapter(LoginActivity.this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(LoginActivity.this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(0);
        initBackButton(false);
        AppConfig.isGeLiGuide = false;
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        boolean mine_login = getIntent().getBooleanExtra("mine_login", true);
//        Look_at_it.setVisibility(mine_login ? View.GONE : View.VISIBLE);
        findViewById(R.id.titlebar_imgBtn_back).setVisibility(mine_login ? View.VISIBLE : View.GONE);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        umShareAPI = UMShareAPI.get(this);
    }


    @OnClick({R.id.img_login_wx,R.id.exit_iv,
            R.id.phone_login, R.id.login_privacy})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_login_wx:///微信
                if (!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    ViewInject.shortToast(this, R.string.not_install_weChat);
                    return;
                }
                config = new UMShareConfig();
                config.isNeedAuthOnGetUserInfo(true);
                umShareAPI.setShareConfig(config);
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.login_privacy:///约见用户使用协议
                Dialog dialog = DialogUtils.createOneBtnDialog(LoginActivity.this, "联系客服", "400 000 000");
                dialog.show();
                break;
            case R.id.phone_login:
                startActivity(new Intent(this, PhoneLoginActivity.class));
                break;
            case R.id.exit_iv:
                finish();
                break;
        }
    }

    WxLoginBean wxLoginBean;
    /**
     * 第三方授权回掉
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (dialog != null)
                dialog.dismiss();
            try {
                String loginData = Utils.map2Json(data);
                FCLoger.debug("loginData :" + loginData);
                Gson gson = new Gson();
                JSONObject object = new JSONObject(loginData);
                if (platform == SHARE_MEDIA.QQ) {
                    QqLoginBean qqLoginBean = gson.fromJson(object.toString(), QqLoginBean.class);
                    isExistence(qqLoginBean.screen_name, qqLoginBean.openid, qqLoginBean.profile_image_url, "", 2);
                } else if (platform == SHARE_MEDIA.WEIXIN) {//微信
                    wxLoginBean = gson.fromJson(object.toString(), WxLoginBean.class);
//                    isExistence(wxLoginBean.screen_name, wxLoginBean.unionid, wxLoginBean.profile_image_url, wxLoginBean.unionid, 1);
                    login();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ViewInject.toast(getApplicationContext(), "授权失败");
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (dialog != null)
                dialog.show(getFragmentManager(), "");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            if (dialog != null)
                dialog.dismiss();
        }
    };
    //登录
    public void login() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        //mobile:手机号,weixin:微信号,area_code:区号,note_code:短信验证码,imageCode:校验码,loginType:登录类型(0-手机登录，1-微信登录)
        Map<String, Object> params = new HashMap<>();
        params.put("areaCode","+86");
        params.put("mobile","");
        params.put("noteCode", "");
        params.put("imageCode", "");
        params.put("weixin", wxLoginBean.openid);
        params.put("loginType", 1);
        apiImp.login(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                LoginBean loginBean=new Gson().fromJson(data,LoginBean.class);
                if (loginBean.getData()!=null){
                    upadate(loginBean.getData().getCustomerId());
                }
                if (dialog != null)
                    dialog.dismiss();
                //注册成功或者登录成功但未绑定手机号
                if (loginBean.getCode() == 19999||loginBean.getCode()== 19997) {
                    intent = new Intent(getApplication(), BangDingPhoneActivity.class);
                    startActivity(intent);
                    //登录成功但未完善用户资料
                } else if (loginBean.getCode() == 19996){
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    startActivity(intent);
                    //登录成功
                }else if (loginBean.getCode() == 19998){
                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    //其他提示
                    ViewInject.shortToast(getApplication(), loginBean.getMessage());
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
                ViewInject.shortToast(getApplication(), errMsg);
            }
        });
    }
    /**
     * 第三方登录  已经注册成功，如果第一次注册跳转选取性别
     *
     * @param nickName
     * @param openID
     * @param flag     1：微信 2：qq 3: 微博
     */
    private void isExistence(final String nickName, final String openID, final String photo, final String unionid, final int flag) {
        Map<String, Object> params = new HashMap<>();
        params.put("openid", openID);
        params.put("flag", flag + "");
        api.isRegister(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (id == -1) {//注册
                    Intent intent;
                    if (flag == 1) {
                        intent = new Intent(getApplicationContext(), BangDingPhoneActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), UserNameSelectActivity.class);
                    }
                    intent.putExtra("openId", openID);
                    intent.putExtra("flag", flag + "");
                    intent.putExtra("photo", photo);
                    startActivity(intent);
//                    finish();
                } else {//登录
                    loginother(unionid, openID, flag + "");
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //第三方登录
    public void loginother(String unionid, String openID, String flag) {
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openID);
        params.put("flag", flag);
        params.put("phone_imei", phoneImei);///手机设备唯一deviceid
        params.put("phone_type", "1");//设备类型:0为IOS,1为android
        params.put("phone_version", phoneVersion);
        params.put("phone_model", phoneModel);
        params.put("registration_id", JPushInterface.getRegistrationID(this));//JPushInterface.getRegistrationID(this)极光
        api.loginother(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (id == -5) {
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param data
     */
    public void upadate(String data) {
//        UserEntity userBean = JSON.parseObject(data, UserEntity.class);
        DadanPreference.getInstance(this).setString("CustomerId",data);
//        AppConfig.userEntity = userBean;
        AppConfig.CustomerId = data;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                BusCallEntity entity = new BusCallEntity();
//                entity.setCallType(BusEnum.LOGIN_UPDATE);
//                Bus.getDefault().post(entity);
//            }
//        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 2001) {
                finish();
            } else if (requestCode == 12) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
