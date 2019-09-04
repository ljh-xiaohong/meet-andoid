package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.ClaimPactActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 认领说明
 */
public class ClaimStatementActivity extends BaseActivity {

    @Bind(R.id.determine)
    Button determine;
    Intent intent;
    int isStatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_claim);
        isStatus=getIntent().getIntExtra("isStatus",-1);
        initData();
    }

    public void initData() {
        setTitleText(getString(R.string.To_claim_that));
        determine.setSelected(true);
    }

    @OnClick({R.id.determine, R.id.claim_the_request, R.id.claim_value, R.id.claim_privilege, R.id.claim_notice})
    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.determine:
                if (isStatus==1){
                    ViewInject.shortToast(getApplication(),R.string.Participate_in_the_claim3);
                    return;
                }else if (isStatus==0){
                    ViewInject.shortToast(getApplication(),R.string.Participate_in_the_claim4);
                    return;
                }
                intent=new Intent(this, ClaimPactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.claim_the_request://认领要求
                url = UrlConstant.ExplainURL.APPLY_REQUIRE;
                break;
            case R.id.claim_value://认领价值
                url = UrlConstant.ExplainURL.APPLY_WORTH;
                break;
            case R.id.claim_privilege://认领特权
                url = UrlConstant.ExplainURL.APPLY_POWER;
                break;
            case R.id.claim_notice://认领须知
                url = UrlConstant.ExplainURL.APPLY_NOTICE;
                break;
        }
        if (StringUtils.isNotEmpty(url)) {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(Constants.URL, url);
            startActivity(intent);
        }
    }
}
