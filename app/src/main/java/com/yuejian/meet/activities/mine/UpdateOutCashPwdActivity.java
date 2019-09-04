package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.Mine;

import butterknife.OnClick;

/**
 * 提现密码
 * Created by zh02 on 2017/8/21.
 */

public class UpdateOutCashPwdActivity extends BaseActivity {
    private Mine mine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_out_cash_pwd);
        setTitleText(getString(R.string.withdrawal_password));
        mine = (Mine) getIntent().getSerializableExtra("mine");
    }

    @OnClick({R.id.modify_password, R.id.find_out_password})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_password:
                Intent intent = new Intent(this, ModifyOutCashPWDActivity.class);
                intent.putExtra("mine", mine);
                startActivity(intent);
                break;
            case R.id.find_out_password:
                intent = new Intent(this, ResetOutCashActivity.class);
                intent.putExtra("mine", mine);
                startActivity(intent);
                break;
        }
    }
}
