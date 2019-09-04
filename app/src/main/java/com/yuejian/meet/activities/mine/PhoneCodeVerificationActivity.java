package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 手机验证码  填写验证码
 */
public class PhoneCodeVerificationActivity extends BaseActivity {
    @Bind(R.id.verify_code_edit)
    EditText gainVerifyCode;
    @Bind(R.id.nation_code)
    TextView code;
    @Bind(R.id.phone_number)
    TextView phone_number;
    @Bind(R.id.submit_phone)
    Button submitPhone;
    @Bind(R.id.phone_modify_tips)
    TextView phone_modify_tips;
    @Bind(R.id.gain_verify_code)
    TextView gain_verify_code;
    int typeModeify=0;
    Intent intent;
    public LoadingDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_code_verification);
        setTitleText(getString(R.string.phone_code_text_2));
        dialog=LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        initDate();
//        startDownCount();
        gainVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                gainVerifyCode.postDelayed(postRunnable, 800);
            }
        });
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone_number.postDelayed(postRunnable, 800);
            }
        });
    }
    public void initDate(){
        intent=getIntent();
        typeModeify=intent.getIntExtra("typeModeify",1);
        submitPhone.setText(typeModeify==1?getString(R.string.phone_code_text_5):getString(R.string.phone_code_text_6));
        phone_number.setText(intent.getStringExtra("mobile"));
        code.setText(intent.getStringExtra("areaCode"));
    }
    private Runnable postRunnable = new Runnable() {
        @Override
        public void run() {
            String code = gainVerifyCode.getText().toString();
            String phone = phone_number.getText().toString();
            if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {
                submitPhone.setEnabled(true);
                submitPhone.setSelected(true);
            } else {
                submitPhone.setEnabled(false);
                submitPhone.setSelected(false);
            }
        }
    };

    @OnClick({R.id.submit_phone,R.id.gain_verify_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_phone:
                modifyPhone(gainVerifyCode.getText().toString());
                break;
            case R.id.gain_verify_code:
                sendSmsCode(phone_number.getText().toString());
                break;
        }
    }
    private void sendSmsCode(final String mobile) {
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("mobile", mobile);
        params.put("area_code",code.getText().toString());
        apiImp.sendSmsCodeToBindMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                startDownCount();
                if (dialog!=null)
                    dialog.dismiss();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });

    }
    private Timer timer = null;
    private int downCount = 120;

    private void startDownCount() {
        if (timer != null) {
            timer.cancel();
        }
        phone_modify_tips.setVisibility(View.VISIBLE);
        downCount=120;
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gain_verify_code.post(new Runnable() {
                    @Override
                    public void run() {
                        if (downCount == 0) {
                            gain_verify_code.setTextColor(Color.parseColor("#ba945a"));
                            gain_verify_code.setBackgroundColor(Color.WHITE);
                            gain_verify_code.setText(R.string.reg_text_verify_code_time);
                            gain_verify_code.setEnabled(true);
                            timer.cancel();
                        } else {
                            gain_verify_code.setText(getString(R.string.phone_verify_toast2) + downCount + "s");
                            downCount--;
                            gain_verify_code.setEnabled(false);
                            gain_verify_code.setTextColor(getResources().getColor(R.color.color_radio_sex_text));
                            gain_verify_code.setBackgroundColor(getResources().getColor(R.color.black_trans_30));
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    /**
     * 更换手机号
     * @param code
     */
    private void modifyPhone(String code) {
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("code", code);
        apiImp.bindMobile(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Toast.makeText(mContext, typeModeify==1?getString(R.string.phone_code_text_3):getString(R.string.phone_code_text_4), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }
}
