package com.yuejian.meet.activities.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.PreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 设置
 * Created by zh02 on 2017/8/7.
 */

public class SettingActivity extends BaseActivity {

    private ApiImp apiImp = new ApiImp();
    private Mine mine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        setTitleText(getString(R.string.mine_txt_setting));
        mine = (Mine) getIntent().getSerializableExtra("mine");
        user = AppConfig.userEntity;
        if (DadanPreference.getInstance(this).getBoolean("isLogin")) {
            findViewById(R.id.modify_phone).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_account).setVisibility(View.VISIBLE);
            findViewById(R.id.privacy).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.modify_phone).setVisibility(View.GONE);
            findViewById(R.id.logout_account).setVisibility(View.GONE);
            findViewById(R.id.privacy).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            findMyInfo(user.customer_id);
        }
    }

    private void findMyInfo(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        new ApiImp().findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Log.d("data", data);
                mine = JSON.parseObject(data, Mine.class);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.chat_setting, R.id.message_setting, R.id.modify_phone, R.id.about, R.id.logout_account, R.id.privacy,R.id.langage, R.id.renzheng_setting})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chat_setting:
                Intent intent = new Intent(this, ChatSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.message_setting:
                intent = new Intent(this, MessageSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_phone:
                if (user == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    AppManager.finishAllActivity();
                } else {
                    intent = new Intent(this, ModifyPhoneActivity.class);
                    intent.putExtra("mine", mine);
                    startActivity(intent);
                }
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_account:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage(R.string.out_login);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logoutAccount();
                    }
                });
                builder.setNegativeButton(R.string.cancel,null);
                builder.show();
                break;
            case R.id.privacy:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.langage:
                startActivity(new Intent(this, LanguageSettingActivity.class));
                break;
            case R.id.renzheng_setting:
                if (user == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    AppManager.finishAllActivity();
                } else {
                    startActivity(new Intent(this, VerifyCenterActivity.class));
                }
                break;
        }
    }

    private void logoutAccount() {
        if (user == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        apiImp.logout(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                NIMClient.getService(AuthService.class).logout();
                AppConfig.userEntity = null;
                PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, "");  //存储个人信息数据
                AppConfig.userEntity = null;
                AppConfig.CustomerId = "";
                AppConfig.UserSex = null;
                AppConfig.Token = null;
                BusCallEntity bus = new BusCallEntity();
                bus.setCallType(BusEnum.LOGOUT);
                Bus.getDefault().post(bus);
                startActivity(new Intent(getBaseContext(),LoginActivity.class));
                AppManager.finishAllActivity();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
