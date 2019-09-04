package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.WxLoginBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 注册完成之后提示是否要绑定微信
 */
public class BangDingWeChatActivity extends BaseActivity {
    LoadingDialogFragment dialog;
    String area_code;
    String mobile;
    private String phoneImei;
    private String phoneVersion;
    private String phoneModel;
    private UMShareAPI umShareAPI;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_ding_we_chat);
        area_code=getIntent().getStringExtra("areaCode");
        mobile=getIntent().getStringExtra("mobile");
        dialog=LoadingDialogFragment.newInstance("");
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        umShareAPI = UMShareAPI.get(this);
    }

    @OnClick({R.id.nubengding,R.id.bengding_weichat})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bengding_weichat:
                goToVerifyWx();

                break;
            case R.id.nubengding:
                intent = new Intent(getApplication(), UserNameSelectActivity.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("areaCode", area_code);


//                intent.putExtra("openId", openID);
//                intent.putExtra("flag", flag + "");
//                intent.putExtra("photo", photo);
                startActivity(intent);
                this.finish();
//                startActivity(new Intent(this, MainActivity.class));
//                BangDingWeChatActivity.this.finish();
                break;
        }
    }
    private void goToVerifyWx() {

        if (!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
            ViewInject.shortToast(this, R.string.not_install_weChat);
            return;
        }
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
            try {
                String loginData = Utils.map2Json(data);
                Gson gson = new Gson();
                org.json.JSONObject object = new org.json.JSONObject(loginData);
                if (platform == SHARE_MEDIA.WEIXIN) {
                    WxLoginBean wxLoginBean = gson.fromJson(object.toString(), WxLoginBean.class);
                    isBangdingWeixi(wxLoginBean.unionid, wxLoginBean.profile_image_url);
//                    verifyWx(wxLoginBean.unionid, wxLoginBean.iconurl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ViewInject.toast(getApplicationContext(), R.string.authorization_failure);
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
            ViewInject.toast(getApplicationContext(), "取消成功");
            if (dialog != null)
                dialog.dismiss();
        }
    };

    /**
     * 判断微信是否注册
     * @param openID
     * @param photoUrl
     */
    public void isBangdingWeixi(final String openID, final String photoUrl){
        Map<String,Object> params=new HashMap<>();
        params.put("openId",openID);
        apiImp.getWeixiCheckOpenid(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (data.equals("0")){
                    intent = new Intent(getApplication(), UserNameSelectActivity.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("areaCode", area_code);
                    intent.putExtra("openId", openID);
                    intent.putExtra("flag", "1");
                    intent.putExtra("photo", photoUrl);
                    startActivity(intent);
                    finish();
                }else {
                    bangdinweixi(data, openID,  "1");
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    /**
     * 绑定手机号
     * @param customer_id
     * @param openID
     * @param flag
     */
    public void bangdinweixi(String customer_id, final String openID, String flag){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",customer_id);
        params.put("mobile",mobile);
        params.put("area_code",area_code);
        params.put("add_gold","1");
        apiImp.bangdingMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                loginother(openID, openID,  "1");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
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
        apiImp.loginother(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                upadate(data);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
                if (dialog!=null)
                    dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private void verifyWx(String openID, String wxUrl) {
        if (user == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("weixin", openID);
        params.put("wx_url", wxUrl);
        params.put("add_gold", "1");
        new ApiImp().wxCertification(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Toast.makeText(mContext, R.string.WeChat_certification_is_successful, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
