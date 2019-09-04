package com.yuejian.meet.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.session.avchat.activity.AVChatActivity;
import com.yuejian.meet.session.main.Extras;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.List;

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
//                            intent= new Intent(getBaseContext(), LoginActivity.class);
                        }
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
