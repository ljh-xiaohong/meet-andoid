package com.yuejian.meet.activities.startactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.SplashActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.Utils;

/**
 * 信息启动页
 */
public class StartMessageActivity extends AppCompatActivity {

    String page="2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_family_activity);
        boolean hasFirstUp = PreferencesUtil.readBoolean(this, Constants.HAVE_START_UP);
        if (!hasFirstUp) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (!Utils.isClsRunning("com.yuejian.meet","com.yuejian.meet.MainActivity",this)){
                getWindow().setBackgroundDrawableResource(R.mipmap.qidongye);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getApplication(), MainActivity.class);
                        intent.putExtra("meetPatams",page);
                        startActivity(intent);
                        startPage();
                    }
                },2000);
            }else {
                startPage();
            }
        }
    }
    public void startPage(){
        finish();
        BusCallEntity entity=new BusCallEntity();
        entity.setCallType(BusEnum.START_PAGE);
        entity.setData(page);
        Bus.getDefault().post(entity);
    }
}
