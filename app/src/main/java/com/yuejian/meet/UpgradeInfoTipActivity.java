package com.yuejian.meet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.CustomerInfo;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27/027.
 */

public class UpgradeInfoTipActivity extends BaseActivity {
    private TextView tips = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_info_tips);
        setTitleText("完善资料");
        tips = (TextView) findViewById(R.id.tips);
        int par = (int) ((1 + Math.random()) * 30);
        String string = tips.getText().toString();
        String s = string.replaceAll("xx%", par + "%");
        tips.setText(s);
        findViewById(R.id.titlebar_imgBtn_back).setVisibility(View.GONE);
    }

    @OnClick({R.id.gotoupgrade, R.id.finish})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gotoupgrade:
                findCustomerInfo();
                break;
            case R.id.finish:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void findCustomerInfo() {
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("customer_id", AppConfig.CustomerId);
            params.put("latitude", String.valueOf(AppConfig.slatitude));
            params.put("longitude", String.valueOf(AppConfig.slongitude));
            if (user != null) {
                params.put("my_customer_id", user.customer_id);
            }
            apiImp.findCustomerInfo(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    CustomerInfo customerInfo = JSON.parseObject(data, CustomerInfo.class);
                    if (customerInfo == null) {
                        Toast.makeText(UpgradeInfoTipActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                    }
                    String string = tips.getText().toString();
                    String s = string.replaceAll("xx%", customerInfo.perfect_ratio + "%");
                    tips.setText(s);
//                    Intent intent = new Intent(UpgradeInfoTipActivity.this, PersonInfoEditActivity.class);
//                    intent.putExtra(Constants.KEY_CUSTOMER_INFO, customerInfo);
//                    startActivity(intent);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {
                    if (!Utils.isNetLink()) {
                        Toast.makeText(UpgradeInfoTipActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
