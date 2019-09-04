package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;

import butterknife.OnClick;

/**
 * 充值/转出方法及规则
 * Created by zh02 on 2017/9/2.
 */

public class WalletCashRuleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_cash);
        setTitleText(getString(R.string.wallet_helper_text2));
    }

    @OnClick({R.id.in_cash_balance, R.id.out_cash_balance, R.id.about_rule})
    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.in_cash_balance:
                url = UrlConstant.ExplainURL.INCASH_RULE;
                break;
            case R.id.out_cash_balance:
                url = UrlConstant.ExplainURL.OUTCASH_RULE;
                break;
            case R.id.about_rule:
                url = UrlConstant.ExplainURL.WALLET_OUT_CASH_QUESTION;
                break;
        }

        if (StringUtils.isNotEmpty(url)) {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(Constants.URL, url);
            startActivity(intent);
        }
    }
}
