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
 * 钱包帮助中心
 * Created by zh02 on 2017/9/1.
 */

public class WalletHelperActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_helper);
        setTitleText(getString(R.string.wallet_helper_title));
    }

    @OnClick({
            R.id.wallet_bill_question,
            R.id.pay_password_question,
            R.id.out_in_cash_rule,
            R.id.out_in_fail_question,})
    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.wallet_bill_question:
                url = UrlConstant.ExplainURL.BILL_QUESTION;
                break;
            case R.id.pay_password_question:
                url = UrlConstant.ExplainURL.PAY_PASSWORD_QUESTION;
                break;
            case R.id.out_in_cash_rule:
                startActivity(new Intent(this, WalletCashRuleActivity.class));
                break;
            case R.id.out_in_fail_question:
                url = UrlConstant.ExplainURL.INCASH_OUTCASH_QUESTION;
                break;
        }

        if (StringUtils.isNotEmpty(url)) {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(Constants.URL, url);
            startActivity(intent);
        }
    }
}
