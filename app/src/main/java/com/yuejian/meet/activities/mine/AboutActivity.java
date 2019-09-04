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
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.VersionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于
 * Created by zh02 on 2017/8/8.
 */

public class AboutActivity extends BaseActivity {

    @Bind(R.id.text_version_name)
    TextView versionNameText;
    @Bind(R.id.check_version_tips)
    TextView checkTips;

    private VersionEntity version = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitleText(getString(R.string.in_regard_to));
        String versionName = Utils.getVersionName(this);
        versionNameText.setText(getString(R.string.Appointment)+" V" + versionName);
        checkVersion();
        Utils.getDeviceInfo(this);
    }

    private void checkVersion() {
        apiImp.getVersion(null, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<VersionEntity> list = JSON.parseArray(data, VersionEntity.class);
                if (list.size() > 1) {
                    if (Utils.versionComparison(list.get(0).version_name, Utils.getVersionName(AboutActivity.this)) == 1) {//非强制更新
                        version = list.get(0);
                        checkTips.setText(R.string.It_s_the_latest_version_2);
                        checkTips.setTextColor(Color.parseColor("#F14B62"));
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @OnClick({R.id.check_version, R.id.user_guide})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_version:
                if (version == null) return;
                Utils.download(AboutActivity.this, version, false);
                break;
            case R.id.user_guide:
                Intent intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.USERGUIDE);
                startActivity(intent);
                break;
        }
    }
}
