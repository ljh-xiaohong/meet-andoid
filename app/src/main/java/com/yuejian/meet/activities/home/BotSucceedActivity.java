package com.yuejian.meet.activities.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.OnClick;

public class BotSucceedActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_succeed);
        setTitleText("发布成功");
    }

    @OnClick({R.id.bu_chat})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bu_chat:
                finish();
                break;
        }
    }
}
