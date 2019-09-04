package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.PrivacySettings;
import com.yuejian.meet.utils.StringUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 隐私
 * Created by zh02 on 2017/8/9.
 */

public class PrivacyActivity extends BaseActivity {
    @Bind(R.id.action_user_permission_tips)
    TextView actionUserPermissionTips;
    @Bind(R.id.privacy_range)
    TextView privacyRange;

    private String permissionType = "0";
    private String range = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setTitleText(getString(R.string.privacy));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            getPrivacy(AppConfig.CustomerId);
        }
    }

    private void getPrivacy(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        apiImp.getPrivacy(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PrivacySettings settings = JSON.parseObject(data, PrivacySettings.class);
                if (settings != null) {
                    initActionBrowsPermission(settings.see_person);
                    initActionBrowsRange(settings.see_range);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private void initActionBrowsPermission(String permissionType) {
        this.permissionType = permissionType;
        if ("0".equals(permissionType)) {
            actionUserPermissionTips.setText(R.string.owner);
            findViewById(R.id.action_brows_rank).setVisibility(View.VISIBLE);
        } else if ("1".equals(permissionType)) {
            actionUserPermissionTips.setText(R.string.owner2);
            findViewById(R.id.action_brows_rank).setVisibility(View.GONE);
        } else if ("2".equals(permissionType)) {
            actionUserPermissionTips.setText(R.string.friend);
            findViewById(R.id.action_brows_rank).setVisibility(View.VISIBLE);
        }
    }

    //     <!--可以查看的范围:0.全部,1:近7天 2.近30天,3.近半年-->
    private void initActionBrowsRange(String range) {
        this.range = range;
        if ("0".equals(range)) {
            privacyRange.setText(R.string.home_all_name);
        } else if ("1".equals(range)) {
            privacyRange.setText(R.string.Only_7_days);
        } else if ("2".equals(range)) {
            privacyRange.setText(R.string.Only_30_days);
        } else if ("3".equals(range)) {
            privacyRange.setText(R.string.Only_days);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 2000) {
                permissionType = data.getStringExtra("permissionType");
                initActionBrowsPermission(permissionType);
            }
        }
    }

    @OnClick({R.id.black_list, R.id.action_user_permission, R.id.action_brows_rank})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.black_list:
                startActivity(new Intent(this, BlackListActivity.class));
                break;
            case R.id.action_user_permission:
                Intent intent = new Intent(this, ActionUserPermissionActivity.class);
                intent.putExtra("permissionType", permissionType);
                startActivityForResult(intent, 2000);
                break;
            case R.id.action_brows_rank:
                intent = new Intent(this, PrivacyRangeActivity.class);
                intent.putExtra("range", range);
                startActivity(intent);
                break;
        }
    }
}
