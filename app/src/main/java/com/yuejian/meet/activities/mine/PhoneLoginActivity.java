package com.yuejian.meet.activities.mine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.MD5Util;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.NewUserEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.gile.BangDingPhoneActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.QqLoginBean;
import com.yuejian.meet.bean.WxLoginBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StatusBarUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;

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
 * @author :
 * @time : 2018/11/16 11:31
 * @desc : 验证手机号
 * @version: V1.0
 * @update : 2018/11/16 11:31
 */

public class PhoneLoginActivity extends BaseActivity {

    //电话输入栏
    @Bind(R.id.txt_content)
    CleanableEditText txt_content;

    //获取验证码
    @Bind(R.id.activity_phone_login_getcode)
    TextView phoneCode;

    //下一步
    @Bind(R.id.activity_phone_login_phone_next_btn)
    Button btn_Next;

    //电话代码 +86
    @Bind(R.id.activity_phone_login_phone_nation_code)
    TextView nation_code;

    //验证码输入EditText
    @Bind(R.id.activity_phone_login_checkcode)
    EditText et_check_code;
    @Bind(R.id.look_check)
    CheckBox lookCheck;
    @Bind(R.id.login_privacy)
    TextView loginPrivacy;
    @Bind(R.id.login_protocol)
    TextView loginProtocol;
    @Bind(R.id.login_tips_tv)
    TextView loginTipsTv;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.login_wx)
    ImageView loginWx;

    private Intent intent;
    private String code = "+86";
    private LoadingDialogFragment dialog;

    private String phoneVersion;
    private String phoneModel;
    private String phoneImei;
    private UMShareAPI umShareAPI;
    private UMShareConfig config;
    private WxLoginBean wxLoginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Log.e("asd", MD5Util.getMD5("{\"clId\":15,\"customerId\":500102,\"pageIndex\":1,\"pageItemCount\":\"土狗\"}f6f86703467b7f7709fe02301c180f8a"));
        Log.e("asd", CommonUtil.encrypt32("{\"clId\":15,\"customerId\":500102,\"pageIndex\":1,\"pageItemCount\":\"土狗\"}f6f86703467b7f7709fe02301c180f8a"));
        //去除标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setContentView(R.layout.activity_phone_login);
        setContentView(R.layout.activity_phone_login_new);
        ButterKnife.bind(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtils.setYtfLightStatusBar(this);
        registerReceiver(verifyCodeReceiver, new IntentFilter(SMS_RECEIVED_ACTION));
        initView();
        listener();
    }

    public void initView() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        //  txt_content.addTextChangedListener(new TextWatcherImpl());
        et_check_code.addTextChangedListener(new TextWatcherImpl());
//        txt_code.addTextChangedListener(new TextWatcherImpl());
        umShareAPI = UMShareAPI.get(this);
    }

    //输入监听
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (et_check_code.getText().toString().length() >= 1) {
                btn_Next.setSelected(true);
                btn_Next.setEnabled(true);
                btn_Next.setBackgroundResource(R.drawable.bg_resg_grey_press_btn);
            } else {
                btn_Next.setSelected(false);
                btn_Next.setEnabled(false);
                btn_Next.setBackgroundResource(R.drawable.bg_resg_grey_unpress_btn);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2001:
                    finish();
                    break;
                case 12:
                    code = "+" + data.getStringExtra("nationCode");
                    nation_code.setText(code);
                    break;
            }
        }
    }

    /**
     * 监听
     */
    public void listener() {
        txt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check(0);
            }
        });
        et_check_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check(1);
            }
        });
    }


    private void check(int i) {
        if (!CommonUtil.isNull(txt_content.getText().toString())) {
            if (i==0){
                phoneCode.setEnabled(true);
                phoneCode.setBackgroundResource(R.drawable.bg_resg_grey_press_btn);
            }
            if (!CommonUtil.isNull(et_check_code.getText().toString())) {
                btn_Next.setBackgroundResource(R.drawable.bg_resg_grey_press_btn);
                btn_Next.setEnabled(true);
            } else {
                btn_Next.setBackgroundResource(R.drawable.bg_resg_grey_unpress_btn);
                btn_Next.setEnabled(false);
            }
        } else {
            phoneCode.setEnabled(false);
            phoneCode.setBackgroundResource(R.drawable.bg_resg_grey_unpress_btn);
            btn_Next.setBackgroundResource(R.drawable.bg_resg_grey_unpress_btn);
            btn_Next.setEnabled(false);
        }
    }

    @OnClick({R.id.login_protocol, R.id.login_privacy, R.id.back, R.id.login_wx
            , R.id.activity_phone_login_getcode, R.id.login_tips_tv
            , R.id.activity_phone_login_phone_next_btn, R.id.activity_phone_login_phone_nation_code})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_protocol:///约见用户使用协议
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.USERGUIDE);
                startActivity(intent);
                break;
            case R.id.login_privacy:///私隐政策
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.PRIVACY);
                startActivity(intent);
                break;
            case R.id.activity_phone_login_getcode://获取验证码
                getCode();
                break;
            case R.id.activity_phone_login_phone_next_btn://下一步
                loginType = 0;
                login();
//                validateCode();
                //getCode();
                break;
            case R.id.activity_phone_login_phone_nation_code://电话代码 +86
//                intent = new Intent(getBaseContext(), AreaCodeActivity.class);
//                startActivityForResult(intent, 12);
                break;
            case R.id.login_tips_tv://电话代码 +86
                Dialog dialog = DialogUtils.createOneBtnDialog(PhoneLoginActivity.this, "联系客服", "400 000 000","呼叫");
                dialog.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.login_wx:
                if (!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    ViewInject.shortToast(this, R.string.not_install_weChat);
                    return;
                }
                config = new UMShareConfig();
                config.isNeedAuthOnGetUserInfo(true);
                umShareAPI.setShareConfig(config);
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
        }
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
                JSONObject object = new JSONObject(loginData);
                if (platform == SHARE_MEDIA.QQ) {
                    QqLoginBean qqLoginBean = gson.fromJson(object.toString(), QqLoginBean.class);
                    isExistence(qqLoginBean.screen_name, qqLoginBean.openid, qqLoginBean.profile_image_url, "", 2);
                } else if (platform == SHARE_MEDIA.WEIXIN) {//微信
                    wxLoginBean = gson.fromJson(object.toString(), WxLoginBean.class);
                    loginType = 1;
                    login();
//                    isExistence(wxLoginBean.screen_name, wxLoginBean.unionid, wxLoginBean.profile_image_url, wxLoginBean.unionid, 1);

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
    private ApiImp api = new ApiImp();

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
//                    upadate(data);
                }
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    int loginType = 0;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                    crLatitude= aMapLocation.getLatitude();//获取纬度
                    crLongitude=aMapLocation.getLongitude();//获取经度
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    private double crLongitude=0.0;
    private double crLatitude=0.0;
    //登录
    public void login() {
        if (loginType == 0) {
            if (code.equals("+86") && !Utils.isMobileNO(txt_content.getText().toString())) {
                ViewInject.shortToast(this, R.string.toast_sjh_not2);
                return;
            }
        }
        if (!lookCheck.isChecked()) {
            ViewInject.shortToast(this, R.string.toast_not_check);
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        //mobile:手机号,weixin:微信号,area_code:区号,note_code:短信验证码,imageCode:校验码,loginType:登录类型(0-手机登录，1-微信登录)
        Map<String, Object> params = new HashMap<>();
        params.put("areaCode", code);
        params.put("mobile", txt_content.getText().toString());
        params.put("noteCode", et_check_code.getText().toString());
        params.put("imageCode", "");
        if (loginType == 1) {
            params.put("weixin", wxLoginBean.openid);
        } else {
            params.put("weixin", "");
        }
        params.put("loginType", loginType);
        params.put("longitude", crLongitude);
        params.put("latitude", crLatitude);
        apiImp.login(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                NewUserEntity loginBean=new Gson().fromJson(data, NewUserEntity.class);
                if (loginBean.getData()!=null){
                    AppConfig.newUerEntity= loginBean;
                    upadate(loginBean.getData().toString());
                }
                if (dialog != null)
                    dialog.dismiss();
                //注册成功
                if (loginBean.getCode() == 19999) {
                    //手机跳转
                    if (loginType == 0) {
                        intent = new Intent(getApplication(), UserNameSelectActivity.class);
                        intent.putExtra("mobile", txt_content.getText().toString());
                        intent.putExtra("areaCode", code);
                        intent.putExtra("phone_imei", phoneImei);///手机设备唯一deviceid
                        intent.putExtra("phone_type", "1");//设备类型:0为IOS,1为android
                        intent.putExtra("phone_version", phoneVersion);
                        intent.putExtra("phone_model", phoneModel);
                    } else {
                        //微信跳转
                        intent = new Intent(getApplication(), BangDingPhoneActivity.class);
                    }
                    startActivity(intent);
                    //登录成功
                } else if (loginBean.getCode() == 19998) {
                    DadanPreference.getInstance(PhoneLoginActivity.this).setBoolean("isLogin",true);
                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    //登录成功但未绑定手机号
                } else if (loginBean.getCode() == 19997){
                    intent = new Intent(getApplication(), BangDingPhoneActivity.class);
                    startActivity(intent);
                    ////登录成功但未完善用户资料
                }else if (loginBean.getCode() == 19996){
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    startActivity(intent);
                }else {
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
     * 保存用户信息
     *
     * @param data
     */
    public void upadate(String data) {
        UserEntity entity =new Gson().fromJson(data, UserEntity.class);
//        UserEntity userBean = JSON.parseObject(data, UserEntity.class);
        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
        AppConfig.userEntity = entity;
        if (!entity.getCustomer_id().equals("0")){
            AppConfig.CustomerId = entity.getCustomer_id();
        }else {
            AppConfig.CustomerId = entity.getCustomerId();
        }
        DadanPreference.getInstance(this).setString("CustomerId",AppConfig.CustomerId);
        DadanPreference.getInstance(this).setString("photo",entity.getPhoto());
        DadanPreference.getInstance(this).setString("surname",entity.getSurname());
//        UserEntity userBean = JSON.parseObject(data, UserEntity.class);
//        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
//        AppConfig.userEntity = userBean;
//        DadanPreference.getInstance(this).setString("CustomerId",data);
//        CommonUtil.saveBean2Sp(this,AppConfig.newUerEntity,"userData","userBean");
//        AppConfig.CustomerId = data;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                BusCallEntity entity = new BusCallEntity();
//                entity.setCallType(BusEnum.LOGIN_UPDATE);
//                Bus.getDefault().post(entity);
//            }
//        }, 500);
    }

    ///获取验证码
    public void getCode() {
        if (code.equals("+86") && !Utils.isMobileNO(txt_content.getText().toString())) {
            ViewInject.shortToast(this, R.string.toast_sjh_not2);
            return;
        }
        if (StringUtil.isEmpty(txt_content.getText().toString())) {
            ViewInject.shortToast(this, R.string.toast_sjh_not);
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        apiImp.getCode(code, txt_content.getText().toString(), this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                Utils.countDown(90, phoneCode, null, R.drawable.bg_resg_grey_unpress_btn, R.drawable.bg_resg_grey_press_btn);

//                intent = new Intent(mContext, PhoneCodeActivity.class);
//                intent.putExtra("mobile", txt_content.getText().toString());
//                intent.putExtra("areaCode", code);
//                startActivity(intent);
//                finish();
//                Utils.countDown(bt_code);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getApplication(), errMsg);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }


    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";//只要注册声明权限即可收到、阻断
    private BroadcastReceiver verifyCodeReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
                //获得短信数据
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                //短信的类型, GSM与CDMA短信的解码方式不同
                String format = intent.getStringExtra("format");

                if (null != pdus) {
                    for (Object pdu : pdus) {
                        //23以上版本显示 createFromPdu过时，多加一个format参数即可
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                        //发送号码-可以过滤需要读取的短信的发送号码
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        //短信内容
                        String content = smsMessage.getDisplayMessageBody();
                        if (content.contains("约见科技")) {
                            autoFillCode(content);
                            abortBroadcast();//中断广播的继续传递,防止优先级低的获取到
                        }
                    }
                }

            }
        }
    };

    private void autoFillCode(String sms) {
        String numbers = getNumbers(sms);
        Log.d("auto get code", numbers);
        et_check_code.setText("");
        et_check_code.setText(numbers);
        et_check_code.setSelection(numbers.length());


    }

    //截取数字
    private String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(verifyCodeReceiver);
    }

}
