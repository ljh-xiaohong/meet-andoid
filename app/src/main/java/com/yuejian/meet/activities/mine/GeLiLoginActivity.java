package com.yuejian.meet.activities.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.umeng.socialize.utils.Log;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.gile.GeLiUserNameSelectActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.QqLoginBean;
import com.yuejian.meet.bean.WxLoginBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static android.provider.Settings.Global.putInt;


/**
 * 登录
 */
public class GeLiLoginActivity extends BaseActivity {
    @Bind(R.id.txt_content)
    EditText txt_content;
    @Bind(R.id.txt_code)
    EditText code;
    @Bind(R.id.bu_next)
    Button bu_next;
    @Bind(R.id.bt_code)
    TextView bt_code;
    Intent intent;
    @Bind(R.id.login_qq_wx_layout)
    LinearLayout login_qq_wx_layout;
    @Bind(R.id.geli_nation_code)
    TextView nationCode;
    @Bind(R.id.geli_nation_area_name)
    TextView nationAreaName;

    private String phoneVersion;
    private String phoneModel;
    private String phoneImei;

    private ApiImp api = new ApiImp();
    private UMShareAPI umShareAPI;
    private UMShareConfig config;
    private LoadingDialogFragment dialog;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    public SMSReceiver smsReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_geli_login);
        AppConfig.isGeLiGuide=true;
        Utils.settingPutInt(this);
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(Integer.MAX_VALUE);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver=new SMSReceiver();
        registerReceiver(smsReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try{
            unregisterReceiver(smsReceiver);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void initView() {
//        setTitleText("注册约见万家帐号");
        initBackButton(false);
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        findViewById(R.id.titlebar_imgBtn_back).setVisibility(View.GONE);
        dialog = LoadingDialogFragment.newInstance("正在请求");
        umShareAPI = UMShareAPI.get(this);
        login_qq_wx_layout.setVisibility(View.GONE);

        txt_content.addTextChangedListener(new TextWatcherImpl());
        code.addTextChangedListener(new TextWatcherImpl());
    }

    //输入监听
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (code.getText().toString().length() >= 1 && txt_content.getText().toString().length() >= 1) {
                bu_next.setSelected(true);
                bu_next.setEnabled(true);
            } else {
                bu_next.setSelected(false);
                bu_next.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    }

    @OnClick({R.id.Look_at_it, R.id.bu_next, R.id.img_login_wx, R.id.img_login_qq, R.id.bt_code, R.id.login_protocol, R.id.login_privacy,R.id.geli_area_code_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Look_at_it:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.bu_next:///下一步
                login();
                break;
            case R.id.img_login_wx:///微信
                if (!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    ViewInject.shortToast(this, "未装微信，请安装再试");
                    return;
                }
                config = new UMShareConfig();
                config.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(this).setShareConfig(config);
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.img_login_qq:///qq
                if (!umShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                    ViewInject.shortToast(this, "未装QQ，请安装再试");
                    return;
                }
                config = new UMShareConfig();
                config.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(this).setShareConfig(config);
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.bt_code:///获取验证码
                getCode();
                break;
            case R.id.login_protocol:///约见用户使用协议
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.USERGUIDE);
                startActivity(intent);
                break;
            case R.id.login_privacy:///约见用户使用协议
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.PRIVACY);
                startActivity(intent);
                break;
            case R.id.geli_area_code_layout:///选择国家区号
                intent = new Intent(getBaseContext(), AreaCodeActivity.class);
                startActivityForResult(intent,12);
                break;
        }
    }

    ///获取验证码
    public void getCode() {
        if (!Utils.isMobileNO(txt_content.getText().toString())) {
            ViewInject.shortToast(this, "手机号不正确");
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        api.getCode("+86",txt_content.getText().toString(), this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                Utils.countDown(bt_code);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getApplication(), errMsg);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    //登录
    public void login() {
        if (!Utils.isMobileNO(txt_content.getText().toString())) {
            ViewInject.shortToast(this, "手机号不正确");
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", txt_content.getText().toString());
        params.put("code", code.getText().toString());
        params.put("phone_imei", phoneImei);///手机设备唯一deviceid
        params.put("phone_type", "1");//设备类型:0为IOS,1为android
        params.put("phone_version", phoneVersion);
        params.put("phone_model", phoneModel);
        params.put("registration_id", JPushInterface.getRegistrationID(this));//JPushInterface.getRegistrationID(this)极光
        api.login(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (id == -5) {
                    intent = new Intent(getApplication(), GeLiUserNameSelectActivity.class);
                    intent.putExtra("mobile", txt_content.getText().toString());
                    startActivity(intent);
                } else {
                    upadate(data);
                }
                finish();
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
                org.json.JSONObject object = new org.json.JSONObject(loginData);
                if (platform == SHARE_MEDIA.QQ) {
                    QqLoginBean qqLoginBean = gson.fromJson(object.toString(), QqLoginBean.class);
                    isExistence(qqLoginBean.screen_name, qqLoginBean.openid, qqLoginBean.profile_image_url, "", 2);

                } else if (platform == SHARE_MEDIA.WEIXIN) {//微信
                    WxLoginBean wxLoginBean = gson.fromJson(object.toString(), WxLoginBean.class);
                    isExistence(wxLoginBean.screen_name, wxLoginBean.unionid, wxLoginBean.profile_image_url, wxLoginBean.unionid, 1);
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
                    Intent intent = new Intent(getApplicationContext(), GeLiUserNameSelectActivity.class);
                    intent.putExtra("openId", openID);
                    intent.putExtra("flag", flag + "");
                    intent.putExtra("photo", photo);
                    startActivity(intent);
                    finish();
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
                    intent = new Intent(getApplication(), GeLiUserNameSelectActivity.class);
                    intent.putExtra("mobile", txt_content.getText().toString());
                    intent.putExtra("nationCode",nationCode.getText().toString());
                    startActivity(intent);
                } else {
                    upadate(data);
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
        UserEntity userBean = JSON.parseObject(data, UserEntity.class);
        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
        AppConfig.userEntity = userBean;
        AppConfig.CustomerId = userBean.getCustomer_id();
        AppConfig.UserSex = userBean.getSex();
        AppConfig.Token = userBean.getToken();
        if (AppConfig.isGeLiGuide) {
            Utils.settingOpenPutInt(this);
            return;
        }
        intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.LOGIN_UPDATE);
                Bus.getDefault().post(entity);
            }
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 2001) {
                finish();
            }else if(requestCode==12){
                if (data.hasExtra("nationCode")){
                    nationCode.setText("+"+data.getStringExtra("nationCode"));
                    nationAreaName.setText(data.getStringExtra("nationAreaName"));
                }
            }
        }
    }
    private String patternCoder = "(?<!--\\d)\\d{6}(?!\\d)";
    class SMSReceiver extends BroadcastReceiver {
        private String verifyCode = "";
        public static final String TAG = "SMSReceiver";
        public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
                try{
                    SmsMessage[] messages = getMessagesFromIntent(intent);
                    for (SmsMessage message : messages) {
                        Log.i(TAG, message.getOriginatingAddress() + " : " +
                                message.getDisplayOriginatingAddress() + " : " +
                                message.getDisplayMessageBody() + " : " +
                                message.getTimestampMillis());
                        String smsContent = message.getDisplayMessageBody();
                        code.setText(patternCode(smsContent));//将短信内容写入SD卡
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        /**
         * 匹配短信中间的6个数字（验证码等）
         *
         * @param patternContent
         * @return
         */
        private String patternCode(String patternContent) {
            if (TextUtils.isEmpty(patternContent)) {
                return null;
            }
            Pattern p = Pattern.compile(patternCoder);
            Matcher matcher = p.matcher(patternContent);
            if (matcher.find()) {
                return matcher.group();
            }
            return null;
        }
        public final SmsMessage[] getMessagesFromIntent(Intent intent){
            Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
            byte[][] pduObjs = new byte[messages.length][];
            for (int i = 0; i < messages.length; i++)
            {
                pduObjs[i] = (byte[]) messages[i];
            }
            byte[][] pdus = new byte[pduObjs.length][];
            int pduCount = pdus.length;
            SmsMessage[] msgs = new SmsMessage[pduCount];
            for (int i = 0; i < pduCount; i++)        {
                pdus[i] = pduObjs[i];
                msgs[i] = SmsMessage.createFromPdu(pdus[i]);
            }
            return msgs;
        }

    }
}
