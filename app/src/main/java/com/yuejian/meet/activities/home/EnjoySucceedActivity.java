package com.yuejian.meet.activities.home;

import android.os.Bundle;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.OnClick;

/**
 * 宗享会创建成功
 */
public class EnjoySucceedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enjoy_succeed);
        setTitleText(getString(R.string.enjoy_succed_3));
    }

    @OnClick({R.id.bu_next})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bu_next:
                finish();
                break;
        }
    }
}
