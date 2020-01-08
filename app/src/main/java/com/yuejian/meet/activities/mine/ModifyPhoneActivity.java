package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.StringUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 更换手机号
 * Created by zh02 on 2017/8/8.
 */

public class ModifyPhoneActivity extends BaseActivity {

    @Bind(R.id.sure_modify_phone)
    Button sureModifyPhone;
    @Bind(R.id.modify_phone_edit)
    EditText modifyPhoneEdit;
    @Bind(R.id.verify_code_edit)
    EditText verifyCodeEdit;
    @Bind(R.id.gain_verify_code)
    TextView gainVerifyCode;
    @Bind(R.id.phone_modify_tips)
    TextView phoneModifyTips;
    @Bind(R.id.modify_nation_code)
    TextView nationCode;
    @Bind(R.id.modify_nation_name)
    TextView nationAreaName;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            AppManager.finishAllActivity();
        }
        findMyInfo(user.customer_id);
        setTitleText(getString(R.string.change_mobile_phone_number2));
    }

    private void findMyInfo(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        new ApiImp().findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Log.d("data", data);
                Mine mine = JSON.parseObject(data, Mine.class);
                if (mine != null && StringUtils.isNotEmpty(mine.mobile)) {
                    String tips = phoneModifyTips.getText().toString().trim();
                    tips = tips.replaceAll("#phone", mine.mobile);
                    phoneModifyTips.setText(tips);
                }

                modifyPhoneEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        verifyCodeEdit.postDelayed(verifyRunnable, 500);
                    }
                });

                verifyCodeEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        verifyCodeEdit.postDelayed(verifyRunnable, 500);
                    }
                });
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Runnable verifyRunnable = new Runnable() {
        @Override
        public void run() {
            String phone = modifyPhoneEdit.getText().toString().trim();
            if (StringUtils.isNotEmpty(phone)) {
                sureModifyPhone.setEnabled(true);
                sureModifyPhone.setSelected(true);
            } else {
                sureModifyPhone.setEnabled(false);
                sureModifyPhone.setSelected(false);
            }
        }
    };

    @OnClick({R.id.sure_modify_phone, R.id.gain_verify_code,R.id.modify_nation_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_modify_phone:
                if (user == null) {
                    return;
                }
                String mobile = modifyPhoneEdit.getText().toString();
                if (StringUtils.isEmpty(mobile)) {
                    Toast.makeText(mContext, R.string.please_enter_your_cell_phone_number_new, Toast.LENGTH_SHORT).show();
                    return;
                } else if (nationCode.getText().toString().equals("+86") && !StringUtils.isPhone(mobile)) {
                    Toast.makeText(mContext, R.string.please_enter_the_correct_mobile_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                intent=new Intent(getApplication(),PhoneCodeVerificationActivity.class);
                intent.putExtra("typeModeify",1);
                intent.putExtra("mobile",mobile);
                intent.putExtra("areaCode",nationCode.getText().toString());
                startActivity(intent);
                break;
            case R.id.modify_nation_layout://选择国家地区
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
