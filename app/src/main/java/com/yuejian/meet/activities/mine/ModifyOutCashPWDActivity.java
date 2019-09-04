package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提现密码  找回密码 修改密码
 * Created by zh02 on 2017/8/21.
 */

public class ModifyOutCashPWDActivity extends BaseActivity {
    @Bind(R.id.pwd_edit)
    EditText pwdEdit;
    @Bind(R.id.password_panel)
    LinearLayout passwordPanel;
    @Bind(R.id.sure_out_cash)
    Button sureBtn;
    @Bind(R.id.out_cash_tips)
    TextView tips;
    @Bind(R.id.eye_check)
    CheckBox eyeCheck;

    private Mine mine;
    private boolean isReset;
    private boolean isOutCash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_out_cash_pwd);
        mine = (Mine) getIntent().getSerializableExtra("mine");
        if (mine == null) finish();
        if (!"1".equals(mine.is_mobile_certified)) {
            Toast.makeText(mContext, R.string.modify_out_toast, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        isOutCash = getIntent().getBooleanExtra("isOutCash", false);
        isReset = getIntent().getBooleanExtra("isReset", false);
        if (isOutCash) {
            setTitleText(getString(R.string.withdrawal_password));
            tips.setText(R.string.please_enter_the_withdrawal_password);
        } else if (isReset) {
            setTitleText(getString(R.string.update_psd2));
            tips.setText(R.string.please_enter_new_withdrawal_code);
        } else if ("0".equals(mine.is_out_cash_pw)) {
            setTitleText(getString(R.string.withdrawal_password));
            tips.setText(R.string.please_enter_the_withdrawal_password);
        } else if ("1".equals(mine.is_out_cash_pw)) {
            setTitleText(getString(R.string.update_psd));
            tips.setText(R.string.modify_out_text);
        }
        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwdEdit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pwdEdit.getText() != null) {
                            inputPassword(pwdEdit.getText().toString());
                        }
                    }
                }, 50);
            }
        });

        eyeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String number = pwdEdit.getText().toString();
                    for (int i = 0; i < passwordPanel.getChildCount(); i++) {
                        TextView child = (TextView) passwordPanel.getChildAt(i);
                        if (i < number.length()) {
                            child.setText(String.valueOf(number.charAt(i)));
                        } else {
                            child.setText("");
                        }
                    }
                } else {
                    String number = pwdEdit.getText().toString();
                    for (int i = 0; i < passwordPanel.getChildCount(); i++) {
                        TextView child = (TextView) passwordPanel.getChildAt(i);
                        if (i < number.length()) {
                            child.setText("*");
                        } else {
                            child.setText("");
                        }
                    }
                }
            }
        });
    }

    private void inputPassword(String input) {
        List<String> numbers = new ArrayList<String>();
        for (int i = 0; i < input.length(); i++) {
            String n = String.valueOf(input.charAt(i));
            if (StringUtils.isNotEmpty()) {
                numbers.add(n);
            }
        }

        for (int i = 0; i < passwordPanel.getChildCount(); i++) {
            TextView child = (TextView) passwordPanel.getChildAt(i);
            if (numbers.size() > i) {
                if (eyeCheck.isChecked()) {
                    child.setText(numbers.get(i));
                } else {
                    child.setText("*");
                }
            } else {
                child.setText("");
            }
        }

        if (numbers.size() >= 6) {
            sureBtn.setEnabled(true);
            sureBtn.setSelected(true);
        } else {
            sureBtn.setEnabled(false);
            sureBtn.setSelected(false);
        }
    }

    private void bindOutCashPassword(String password) {
        if(StringUtils.isNumber(password) && password.length() == 6){
            HashMap<String, Object> params = new HashMap<>();
            params.put("customer_id", mine.customer_id);
            params.put("password", Utils.md5(password));
            apiImp.bindOutCashPassword(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    Toast.makeText(mContext, R.string.modify_toast, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        } else {
            Toast.makeText(mContext, R.string.modify_toast1, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.sure_out_cash})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_out_cash:
                String password = pwdEdit.getText().toString();
                if (mine == null) return;
                if (isOutCash) {
                    Intent intent = new Intent();
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (isReset) {
                    bindOutCashPassword(password); //未绑定密码
                } else if (!"1".equals(mine.is_out_cash_pw) && !isValidated) {
                    bindOutCashPassword(password); //未绑定密码
                } else if ("1".equals(mine.is_out_cash_pw) && !isValidated) {
                    validatePassword(password); //修改密码，验证旧密码
                } else if ("1".equals(mine.is_out_cash_pw) && isValidated) {
                    bindOutCashPassword(password); //修改后绑定密码
                }
                break;
        }
    }

    private boolean isValidated = false;

    private void validatePassword(String oldPassword) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("oldPassword", Utils.md5(oldPassword));
        apiImp.updateOutCashPassword(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                tips.setText(R.string.modify_toast2);
                clearPasswordPanel();
                isValidated = true;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void clearPasswordPanel() {
        for (int i = 0; i < passwordPanel.getChildCount(); i++) {
            TextView child = (TextView) passwordPanel.getChildAt(i);
            child.setText("");
        }
        pwdEdit.setText("");
        sureBtn.setEnabled(false);
        sureBtn.setSelected(false);
    }
}
