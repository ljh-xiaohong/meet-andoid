package com.yuejian.meet.activities.gile;

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

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.NewUserEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.GifActivity;
import com.yuejian.meet.activities.mine.UserNameSelectActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StatusBarUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author :
 * @time : 2018/11/16 11:31
 * @desc : 验证手机号
 * @version: V1.0
 * @update : 2018/11/16 11:31
 */

public class BangDingPhoneActivity extends BaseActivity {

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

    private Intent intent;
    private String code = "+86";
    private LoadingDialogFragment dialog;

    private String phoneVersion;
    private String phoneModel;
    private String phoneImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        Log.e("asd",MD5Util.getMD5("{\"clId\":15,\"customerId\":500102,\"pageIndex\":1,\"pageItemCount\":\"土狗\"}f6f86703467b7f7709fe02301c180f8a"));
//        Log.e("asd",CommonUtil.encrypt32("{\"clId\":15,\"customerId\":500102,\"pageIndex\":1,\"pageItemCount\":\"土狗\"}f6f86703467b7f7709fe02301c180f8a"));
        //去除标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setContentView(R.layout.activity_phone_login);
        setContentView(R.layout.activity_bangding_new);
        ButterKnife.bind(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtils.setYtfLightStatusBar(this);
        registerReceiver(verifyCodeReceiver, new IntentFilter(SMS_RECEIVED_ACTION));
        initView();
        listener();
    }

    public void initView() {
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

    @OnClick({R.id.login_protocol, R.id.login_privacy, R.id.back
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
                loginType=0;
                bangding();
                break;
            case R.id.activity_phone_login_phone_nation_code://电话代码 +86
//                intent = new Intent(getBaseContext(), AreaCodeActivity.class);
//                startActivityForResult(intent, 12);
                break;
            case R.id.login_tips_tv://电话代码 +86
                Dialog dialog = DialogUtils.createOneBtnDialog(BangDingPhoneActivity.this, "联系客服", "400 000 000","呼叫");
                dialog.show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private ApiImp api = new ApiImp();
    int loginType=0;
    //登录
    public void bangding() {
        if (loginType==0) {
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
        params.put("customerId", AppConfig.CustomerId);
        params.put("areaCode",code);
        params.put("mobile",txt_content.getText().toString());
        params.put("noteCode", et_check_code.getText().toString());
        params.put("imageCode", "");
        apiImp.bangdingMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                NewUserEntity loginBean=new Gson().fromJson(data, NewUserEntity.class);
                if (loginBean.getData()!=null){
                    AppConfig.newUerEntity= loginBean;
                    upadate(loginBean.getData().toString());
                }
                if (dialog != null)
                    dialog.dismiss();
                if (loginBean.getCode()==0||loginBean.getCode()==19996){
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    intent.putExtra("mobile", txt_content.getText().toString());
                    intent.putExtra("areaCode", code);
                    intent.putExtra("customer_id", AppConfig.CustomerId);
                    intent.putExtra("phone_imei", phoneImei);///手机设备唯一deviceid
                    intent.putExtra("phone_type", "1");//设备类型:0为IOS,1为android
                    intent.putExtra("phone_version", phoneVersion);
                    intent.putExtra("phone_model", phoneModel);
                    startActivity(intent);
               }else if (loginBean.getCode()==19998){
                    DadanPreference.getInstance(BangDingPhoneActivity.this).setBoolean("isLogin",true);
                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
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
        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
        AppConfig.userEntity = entity;
        if (!CommonUtil.isNull(entity.getCustomer_id())||!entity.getCustomer_id().equals("0")){
            AppConfig.CustomerId = entity.getCustomer_id();
        }else {
            AppConfig.CustomerId = entity.getCustomerId();
        }
        DadanPreference.getInstance(this).setString("CustomerId",AppConfig.CustomerId);
        DadanPreference.getInstance(this).setString("photo",entity.getPhoto());
        DadanPreference.getInstance(this).setString("surname",entity.getSurname());
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
//        txt_code.setText("");
//        txt_code.setText(numbers);
//        txt_code.setSelection(numbers.length());
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
