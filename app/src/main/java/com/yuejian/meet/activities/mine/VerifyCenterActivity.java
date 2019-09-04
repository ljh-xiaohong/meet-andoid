package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Mine;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 认证中心
 * Created by zh02 on 2017/8/9.
 */

public class VerifyCenterActivity extends BaseActivity {

    @Bind(R.id.phone_verify_status)
    TextView phoneVerifyStatus;
    @Bind(R.id.wx_verify_status)
    TextView wxVerifyStatus;
    @Bind(R.id.id_card_verify_status)
    TextView idCardVerifyStatus;
    @Bind(R.id.business_verify_status)
    TextView businessVerifyStatus;

    private ApiImp apiImp = new ApiImp();
    private Mine mine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_center);
        setTitleText(getString(R.string.mine_authentication_center));
    }

    @Override
    protected void onResume() {
        super.onResume();
        findMyInfo();
    }

    private void findMyInfo() {
        if (user == null) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        apiImp.findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mine = JSON.parseObject(data, Mine.class);
                if (mine != null) {
                    updateVerifyStatus(mine);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void updateVerifyStatus(Mine mine) {
//      ( -1 未认证 , 0:待审核,1:审核通过,2,审核不通过)
        if ("1".equals(mine.is_mobile_certified)) {
            phoneVerifyStatus.setText(mine.mobile);
            phoneVerifyStatus.setTextColor(Color.parseColor("#999999"));
        } else if ("-1".equals(mine.is_mobile_certified)) {
            phoneVerifyStatus.setText(R.string.verify_center_text1);
            phoneVerifyStatus.setTextColor(Color.parseColor("#2a80ba"));
        } else if ("0".equals(mine.is_mobile_certified)) {
            phoneVerifyStatus.setText(R.string.verify_center_text);
            phoneVerifyStatus.setTextColor(Color.parseColor("#7bbc7a"));
        } else if ("2".equals(mine.is_mobile_certified)) {
            phoneVerifyStatus.setText(R.string.verify_center_text3);
            phoneVerifyStatus.setTextColor(Color.parseColor("#f54d4d"));
        }


        if ("1".equals(mine.is_weixin_certified)) {
            wxVerifyStatus.setText(R.string.verify_center_text2);
            wxVerifyStatus.setTextColor(Color.parseColor("#999999"));
        } else if ("-1".equals(mine.is_weixin_certified)) {
            wxVerifyStatus.setText(R.string.verify_center_text1);
            wxVerifyStatus.setTextColor(Color.parseColor("#2a80ba"));
        } else if ("0".equals(mine.is_weixin_certified)) {
            wxVerifyStatus.setText(R.string.verify_center_text);
            wxVerifyStatus.setTextColor(Color.parseColor("#7bbc7a"));
        } else if ("2".equals(mine.is_weixin_certified)) {
            wxVerifyStatus.setText(R.string.verify_center_text3);
            wxVerifyStatus.setTextColor(Color.parseColor("#f54d4d"));
        }


        if ("1".equals(mine.is_idcard_certified)) {
            idCardVerifyStatus.setText(R.string.verify_center_text2);
            idCardVerifyStatus.setTextColor(Color.parseColor("#999999"));

            findViewById(R.id.business_verify).setVisibility(View.VISIBLE);
            if ("1".equals(mine.is_business_license_certified)) {
                businessVerifyStatus.setText(R.string.verify_center_text2);
                businessVerifyStatus.setTextColor(Color.parseColor("#999999"));
            } else if ("-1".equals(mine.is_business_license_certified)) {
                businessVerifyStatus.setText(R.string.verify_center_text1);
                businessVerifyStatus.setTextColor(Color.parseColor("#2a80ba"));
            } else if ("0".equals(mine.is_business_license_certified)) {
                businessVerifyStatus.setText(R.string.verify_center_text);
                businessVerifyStatus.setTextColor(Color.parseColor("#7bbc7a"));
            } else if ("2".equals(mine.is_business_license_certified)) {
                businessVerifyStatus.setText(R.string.verify_center_text3);
                businessVerifyStatus.setTextColor(Color.parseColor("#f54d4d"));
            }

        } else if ("-1".equals(mine.is_idcard_certified)) {
            idCardVerifyStatus.setText(R.string.verify_center_text1);
            idCardVerifyStatus.setTextColor(Color.parseColor("#2a80ba"));
        } else if ("0".equals(mine.is_idcard_certified)) {
            idCardVerifyStatus.setText(R.string.verify_center_text);
            idCardVerifyStatus.setTextColor(Color.parseColor("#7bbc7a"));
        } else if ("2".equals(mine.is_idcard_certified)) {
            idCardVerifyStatus.setText(R.string.verify_center_text3);
            idCardVerifyStatus.setTextColor(Color.parseColor("#f54d4d"));
        }
    }

    @OnClick({R.id.phone_verify, R.id.id_verify, R.id.wx_verify, R.id.business_verify})
    @Override
    public void onClick(View v) {
        if (mine == null) return;
        switch (v.getId()) {
            case R.id.phone_verify:
                if ("1".equals(mine.is_mobile_certified) || "0".equals(mine.is_mobile_certified)) {
                    return;
                }
                startActivity(new Intent(this, VerifyPhoneActivity.class));
                break;
            case R.id.id_verify:
                if ("1".equals(mine.is_idcard_certified) || "0".equals(mine.is_idcard_certified)) {
                    return;
                }
                startActivity(new Intent(this, VerifyIdCardActivity.class));
                break;
            case R.id.wx_verify:
                if ("1".equals(mine.is_weixin_certified) || "0".equals(mine.is_weixin_certified)) {
                    return;
                }
                startActivity(new Intent(this, VerifyWxActivity.class));
                break;
            case R.id.business_verify:
                if ("1".equals(mine.is_business_license_certified) || "0".equals(mine.is_business_license_certified)) {
                    return;
                }
                startActivity(new Intent(this, VerifyBusinessActivity.class));
                break;

        }
    }
}
