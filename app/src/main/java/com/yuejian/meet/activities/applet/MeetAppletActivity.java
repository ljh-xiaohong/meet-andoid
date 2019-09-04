package com.yuejian.meet.activities.applet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 约见小程序
 */
public class MeetAppletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_applet);
        setTitleText(getString(R.string.applet_name12));
    }

    @OnClick({R.id.applet_but,R.id.applet_server,R.id.meet_applet_pal})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.applet_but:
//                getJson();
                    startActivity(new Intent(this,ExclusiveAppletActivity.class));
                break;
            case R.id.meet_applet_pal:
//                getJson();
                    startActivity(new Intent(this,ExclusiveAppletActivity.class));
                break;
            case R.id.applet_server:
                    startActivity(new Intent(this,AppletSeverActivity.class));
                break;
        }
    }
    public void getJson(){
        apiImp.getMiniJson(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
