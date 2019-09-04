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

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Mine;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/21.
 */

public class ResetOutCashActivity extends BaseActivity {
    @Bind(R.id.gain_verify_code)
    TextView gainVerifyCode;
    @Bind(R.id.code_edit)
    EditText codeEdit;
    @Bind(R.id.phone_tips)
    TextView phoneTips;
    @Bind(R.id.next_step)
    Button nextStep;

    private Mine mine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_out_cash_password);
        setTitleText("找回提现密码");
        mine = (Mine) getIntent().getSerializableExtra("mine");
        if (mine != null && "1".equals(mine.is_mobile_certified)) {
            String string = phoneTips.getText().toString();
            string = string.replaceAll("##", mine.mobile);
            phoneTips.setText(string);
        }

        codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (string.length() == 0) {
                    nextStep.setEnabled(false);
                    nextStep.setSelected(false);
                } else {
                    nextStep.setEnabled(true);
                    nextStep.setSelected(true);
                }
            }
        });
    }

    @OnClick({R.id.next_step, R.id.gain_verify_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step:
                validateCode(codeEdit.getText().toString().trim());
                break;
            case R.id.gain_verify_code:
                getCode();
                break;
        }
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
                            gainVerifyCode.setText("获取验证码");
                            gainVerifyCode.setEnabled(true);
                            timer.cancel();
                        } else {
                            gainVerifyCode.setText("重发" + downCount + "s");
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

    private void getCode() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        apiImp.sendSMSToResetOutCashPassword(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                startDownCount();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private void validateCode(String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("code", code);
        apiImp.validateSMSCode(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent = new Intent(getBaseContext(), ModifyOutCashPWDActivity.class);
                intent.putExtra("mine", mine);
                intent.putExtra("isReset", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
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
