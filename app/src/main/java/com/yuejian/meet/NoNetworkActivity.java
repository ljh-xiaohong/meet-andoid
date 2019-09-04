package com.yuejian.meet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import butterknife.OnClick;

public class NoNetworkActivity extends BaseActivity {
    public static long timeClose=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }
    public static void starter(Context context){
        if (System.currentTimeMillis()-timeClose>6000){
            timeClose= System.currentTimeMillis();
            Intent intent=new Intent(context, NoNetworkActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @OnClick({R.id.txt_nwtwork})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_nwtwork:
                if (Utils.isNetLink()){
                    BusCallEntity busCallEntity=new BusCallEntity();
                    busCallEntity.setCallType(BusEnum.NETWOR_CONNECTION);
                    Bus.getDefault().post(busCallEntity);
                    timeClose= System.currentTimeMillis();
                    finish();
                }else {
                    try {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ViewInject.shortToast(AppConfig.context,"网络错误，请设置网络");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        timeClose= System.currentTimeMillis();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
            timeClose= System.currentTimeMillis();
            this.finish();
        }
    }
}
