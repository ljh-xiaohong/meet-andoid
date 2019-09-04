package com.yuejian.meet.activities.gile;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.AreaCodeActivity;
import com.yuejian.meet.activities.mine.BangDingWeChatActivity;
import com.yuejian.meet.activities.mine.ModifyOutCashPWDActivity;
import com.yuejian.meet.activities.mine.UserNameSelectActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.PhoneCode;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 手机验证码
 */
public class PhoneCodeActivity extends BaseActivity {
    @Bind(R.id.phone_code)
    PhoneCode phoneCode;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.afresh_code)
    TextView afresh_code;
    Activity activity;
    String mobile,areaCode;
    Intent intent;
    private String phoneVersion;
    private String phoneModel;
    private String phoneImei;
    public String photo="",flag="",openId="",code="";
    private boolean phoneBangding=false;
    private LoadingDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_code);
        activity=this;
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        initData();
        Utils.countDown(afresh_code);
    }
    public void initData(){
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        mobile=getIntent().getStringExtra("mobile");
        areaCode=getIntent().getStringExtra("areaCode");
        phoneBangding=getIntent().getBooleanExtra("phoneBangding",false);
        if (phoneBangding){
            photo=getIntent().getStringExtra("photo");
            flag=getIntent().getStringExtra("flag");
            openId=getIntent().getStringExtra("openId");
        }
        phone.setText(areaCode+" "+mobile);
        phoneCode.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                PhoneCodeActivity.this.code=code;
                Utils.hintKbTwo(activity);
                if (phoneBangding){
                    validateCode();  //微信注册，绑定手机号
                }else {
                    login();  //手机号注册，或者已注册使用手机登陆
                }
            }

            @Override
            public void onInput() {

            }
        });
    }

    @OnClick({R.id.afresh_code})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.afresh_code:
                getCode();
                break;
        }
    }
    private void validateCode() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("area_code", areaCode);
        params.put("mobile", mobile);
        params.put("code", code);
        apiImp.validateCode(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent = new Intent(getBaseContext(), UserNameSelectActivity.class);
                intent.putExtra("openId", openId);
                intent.putExtra("flag", flag + "");
                intent.putExtra("photo", photo);
                intent.putExtra("mobile", mobile);
                intent.putExtra("areaCode", areaCode);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    ///获取验证码
    public void getCode() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        apiImp.getCode(areaCode,mobile, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                Utils.countDown(afresh_code);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getApplication(), errMsg);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    //手机号注册，或者已注册使用手机登陆
    public void login() {
        if (areaCode.equals("+86") && !Utils.isMobileNO(mobile)) {
            ViewInject.shortToast(this, R.string.toast_sjh_not2);
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        //mobile:手机号,weixin:微信号,area_code:区号,note_code:短信验证码,imageCode:校验码,loginType:登录类型(0-手机登录，1-微信登录)
        Map<String, Object> params = new HashMap<>();
        params.put("area_code",areaCode);
        params.put("mobile",mobile);
        params.put("note_code", code);
        params.put("imageCode", code);
        params.put("weixin", code);
        params.put("loginType", 0);

//        params.put("phone_imei", phoneImei);///手机设备唯一deviceid
//        params.put("phone_type", "1");//设备类型:0为IOS,1为android
//        params.put("phone_version", phoneVersion);
//        params.put("phone_model", phoneModel);
//        params.put("registration_id", JPushInterface.getRegistrationID(this));//JPushInterface.getRegistrationID(this)极光
        apiImp.login(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (id == -5) {
//                    intent = new Intent(getApplication(), BangDingWeChatActivity.class);
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("areaCode", areaCode);
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.LOGIN_UPDATE);
                Bus.getDefault().post(entity);
            }
        }, 500);
    }
}
