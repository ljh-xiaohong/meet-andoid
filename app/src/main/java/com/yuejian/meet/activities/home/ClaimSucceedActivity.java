package com.yuejian.meet.activities.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 认领成功
 */
public class ClaimSucceedActivity extends BaseActivity {

    @Bind(R.id.determine)
    Button determine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_succeed);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.To_claim_success));
        determine.setSelected(true);
    }

    @OnClick({R.id.determine})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.determine:
                finish();
                break;
        }
    }
}
