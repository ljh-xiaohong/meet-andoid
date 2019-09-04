package com.yuejian.meet.widgets;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by idea on 2016/8/6.
 */
public class CustomToast {
    public static final int LENGTH_MAX = -1;
    private boolean mCanceled = true;
    private Handler mHandler;
    private Context mContext;
    private Toast mToast;

    public CustomToast(Context context) {
        this(context,new Handler());
    }


    public CustomToast(Context context,Handler h) {
        mContext = context;
        mHandler = h;
        mToast = Toast.makeText(mContext,"",Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    public void show(int resId,int duration) {
        mToast.setText(resId);
        if(duration != LENGTH_MAX) {
            mToast.setDuration(duration);
            mToast.show();
        } else if(mCanceled) {
            mToast.setDuration(Toast.LENGTH_LONG);
            mCanceled = false;
            showUntilCancel();
        }
    }

    /**
     * @param text 要显示的内容  
     * @param duration 显示的时间长  
     * 根据LENGTH_MAX进行判断  
     * 如果不匹配，进行系统显示  
     * 如果匹配，永久显示，直到调用hide()  
     */
    public void show(String text,int duration) {
        mToast.setText(text);
        if(duration != LENGTH_MAX) {
            mToast.setDuration(duration);
            mToast.show();
        } else {
            if(mCanceled) {
                mToast.setDuration(Toast.LENGTH_LONG);
                mCanceled = false;
                showUntilCancel();
            }
        }
    }

    /**
     * 隐藏Toast  
     */
    public void hide(){
        mToast.cancel();
        mCanceled = true;
    }

    public boolean isShowing() {
        return !mCanceled;
    }

    private void showUntilCancel() {
        if(mCanceled)
            return;
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                showUntilCancel();
            }
        },3000);
    }
}   