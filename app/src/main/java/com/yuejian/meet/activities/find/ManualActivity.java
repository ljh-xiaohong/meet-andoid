package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.HistoryFeedbackActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.mine.UserFeedbackActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 帮助与反馈
 * Created by zh02 on 2017/8/21.
 */

public class ManualActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        setTitleText(getString(R.string.mine_Help_and_feedback));
    }

    @OnClick({R.id.city_originator_manual,
            R.id.group_chat_manual,
            R.id.in_cash_rule,
            R.id.out_cash_rule,R.id.feedback_layout,R.id.manual_submit})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        String url = "";
        switch (v.getId()) {
            case R.id.city_originator_manual:
                url = UrlConstant.ExplainURL.FAMILYMASTER;
                break;
            case R.id.group_chat_manual:
                url = UrlConstant.ExplainURL.CHATGROUP;
                break;
            case R.id.in_cash_rule:
                url = UrlConstant.ExplainURL.INCASH;
                break;
            case R.id.out_cash_rule:
                url = UrlConstant.ExplainURL.OUTCASH;
                break;
            case R.id.feedback_layout:
                if (StringUtil.isEmpty(AppConfig.CustomerId)){
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, HistoryFeedbackActivity.class));
                break;
            case R.id.manual_submit:
                if (StringUtil.isEmpty(AppConfig.CustomerId)){
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, UserFeedbackActivity.class));
                break;
        }
        if (StringUtils.isEmpty(url))
            return;
        intent.putExtra(Constants.URL, url);
        startActivity(intent);
    }
}
