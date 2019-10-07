package com.yuejian.meet.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nimlib.sdk.NimIntent;
import com.yuejian.meet.MainActivity;
//import com.yuejian.meet.NewMainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.session.avchat.activity.AVChatActivity;
import com.yuejian.meet.session.main.Extras;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.Utils;

/**
 * Created by zh02 on 2017/9/3.
 */

public class StartUpActivity extends Activity implements AppWakeUpListener {
    public Boolean isLogin = false;
    Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Utils.setlangage(this);
        isLogin = false;
        context = this;
        // 已经登录过了，处理过来的请求
        final Intent intent2 = getIntent();
        if (intent2 != null) {
            if (intent2.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                isLogin = true;
            } else if (intent2.hasExtra(Extras.EXTRA_JUMP_P2P) || intent2.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
                isLogin = true;
            }
        }
        boolean hasFirstUp = PreferencesUtil.readBoolean(this, Constants.HAVE_START_UP);
        if (!hasFirstUp) {
            setContentView(R.layout.activity_start_up);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getBaseContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();
                    context.finish();
                }
            }, 1000);
        } else {
            if (Utils.isClsRunning("com.yuejian.meet","com.yuejian.meet.MainActivity",context)) {
                if (isLogin) {
                    BusCallEntity entity = new BusCallEntity();
                    entity.setCallType(BusEnum.INTEN_CHAT);
                    entity.setIntent(intent2);
                    Bus.getDefault().post(entity);
                }
                this.finish();
            } else {
                setContentView(R.layout.activity_start_up);
                OpenInstall.getWakeUp(getIntent(), this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=null;
                        if (!DadanPreference.getInstance(StartUpActivity.this).getBoolean("isLogin")){
                            intent= new Intent(getBaseContext(), LoginActivity.class);
                        }else {
                            intent= new Intent(getBaseContext(), MainActivity.class);
                        }
                        //游客模式
//                        intent= new Intent(getBaseContext(), MainActivity.class);
                        if (isLogin) {
                            intent.putExtras(intent2);
                        }
                        startActivity(intent);
                        context.finish();
                    }
                }, 1000);

            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OpenInstall.getWakeUp(intent, this);
    }

    @Override
    public void onWakeUpFinish(AppData appData, Error error) {

    }
}
