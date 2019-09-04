package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 手机号认证
 * Created by zh02 on 2017/8/11.
 */

public class VerifyPhoneActivity extends BaseActivity {
    @Bind(R.id.verify_phone_edit)
    EditText phoneEdit;
    @Bind(R.id.verify_phone_code_edit)
    EditText codeEdit;
    @Bind(R.id.sure_verify_phone)
    Button sureButton;
    @Bind(R.id.gain_verify_code)
    TextView gainVerifyCode;
    @Bind(R.id.phone_nation_code)
    TextView nationCode;
    @Bind(R.id.phone_nation_area_name)
    TextView nationAreaName;
    Intent intent;


    private ApiImp api = new ApiImp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        setTitleText(getString(R.string.phone_verify_title));
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneEdit.postDelayed(postRunnable, 800);
            }
        });

    }

    private Runnable postRunnable = new Runnable() {
        @Override
        public void run() {
            String code = codeEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            if (StringUtils.isNotEmpty(phone) ) {
                sureButton.setEnabled(true);
                sureButton.setSelected(true);
            } else {
                sureButton.setEnabled(false);
                sureButton.setSelected(false);
            }
        }
    };

    @OnClick({R.id.sure_verify_phone, R.id.gain_verify_code,R.id.phone_nation_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_verify_phone:
                if (nationCode.getText().toString().equals("+86")&&!StringUtils.isPhone(phoneEdit.getText().toString().trim())) {
                    Toast.makeText(mContext, R.string.phone_verify_toast, Toast.LENGTH_SHORT).show();
                    return;
                }
                intent=new Intent(getApplication(),PhoneCodeVerificationActivity.class);
                intent.putExtra("typeModeify",0);
                intent.putExtra("mobile",phoneEdit.getText().toString().trim());
                intent.putExtra("areaCode",nationCode.getText().toString());
                startActivity(intent);
                finish();
//                getCode(phoneEdit.getText().toString().trim());


//                Utils.hideSystemKeyBoard(this, v);
//                verifyPhone(codeEdit.getText().toString().trim());
                break;
            case R.id.gain_verify_code:
                getCode(phoneEdit.getText().toString().trim());
                break;
            case R.id.phone_nation_layout:
                intent = new Intent(getBaseContext(), AreaCodeActivity.class);
                startActivityForResult(intent,12);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
           if(requestCode==12){
                if (data.hasExtra("nationCode")){
                    nationCode.setText("+"+data.getStringExtra("nationCode"));
                    nationAreaName.setText(data.getStringExtra("nationAreaName"));
                }
            }
        }
    }


    private void verifyPhone(String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("code", code);
        api.bindMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Toast.makeText(mContext, R.string.phone_verify_toast1, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCode(final String mobile) {
        if (nationCode.getText().toString().equals("+86")&&!StringUtils.isPhone(mobile)) {
            Toast.makeText(mContext, R.string.phone_verify_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        codeEdit.requestFocus();
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("mobile", mobile);
        params.put("area_code", nationCode.getText().toString());
        api.sendSmsCodeToBindMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                intent=new Intent(getApplication(),PhoneCodeVerificationActivity.class);
                intent.putExtra("typeModeify",0);
                intent.putExtra("mobile",mobile);
                intent.putExtra("areaCode",nationCode.getText().toString());
                startActivity(intent);
                finish();
//                startDownCount();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private Timer timer = null;
    private int downCount = 60;

    private void startDownCount() {
        if (timer != null) {
            timer.cancel();
        }
        downCount = 60;
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gainVerifyCode.post(new Runnable() {
                    @Override
                    public void run() {
                        if (downCount == 0) {
                            gainVerifyCode.setTextColor(Color.parseColor("#ba945a"));
                            gainVerifyCode.setBackgroundColor(Color.WHITE);
                            gainVerifyCode.setText(R.string.reg_text_verify_code_time);
                            gainVerifyCode.setEnabled(true);
                            timer.cancel();
                        } else {
                            gainVerifyCode.setText(getString(R.string.phone_verify_toast2) + downCount + "s");
                            downCount--;
                            gainVerifyCode.setEnabled(false);
                            gainVerifyCode.setTextColor(getResources().getColor(R.color.color_radio_sex_text));
                            gainVerifyCode.setBackgroundColor(getResources().getColor(R.color.black_trans_30));
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
