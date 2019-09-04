package com.netease.nim.uikit.api.utils;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.netease.nim.uikit.app.AppConfig;

/**
 * Created by zh03 on 2017/7/10.
 */

public class CountDownTimerUtils extends CountDownTimer {
    private TimeRockon time;
    public CountDownTimerUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        AppConfig.mAvchattime+=1;
        if ((millisUntilFinished/1000)==180){
            time.TimeHintCall();
        }
        if ((millisUntilFinished/1000)==160||(millisUntilFinished/1000)==150){
            try {
                Toast.makeText(AppConfig.context,"您的余额不足三分钟，请及时充值",Toast.LENGTH_SHORT).show();
            }catch (Exception e){}
        }
        if ((millisUntilFinished/1000)==10){
            time.TimeCall(true);
        }
        Log.d("===================time:",millisUntilFinished+"");
    }

    @Override
    public void onFinish() {
        time.TimeCall(true);
    }
    public void setTimeCall(TimeRockon timCall){
        this.time=timCall;
    }
    public interface TimeRockon{
        void TimeCall(Boolean isFinishCall);
        void TimeHintCall();
    }
}
