package com.yuejian.meet.widgets.aliyun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CountDownTextView extends TextView {

    private Timer timer;

    private AlphaAnimation alphaAnimation;

    private OnCountDownFinishedListener finishedListener;


    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        alphaAnimation = new AlphaAnimation(0f, 1f);
        this.setVisibility(GONE);
    }

    public void startCount(long millisInFuture, long countDownInterval, OnCountDownFinishedListener listener) {
        if (isClickable()) {
            this.finishedListener = listener;
            if (null == timer) {
                timer = new Timer(millisInFuture, countDownInterval);
            }
            this.setVisibility(VISIBLE);
            timer.start();
        }
    }


    class Timer extends CountDownTimer {
        int index;
        int countDownTotal;

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            alphaAnimation.setDuration(countDownInterval-80);
            index = (int) millisInFuture / 1000;
            countDownTotal = (int) millisInFuture / 1000;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setClickable(false);
            setText(index-- + "");
            startAnimation(alphaAnimation);
        }

        @Override
        public void onFinish() {
            setText("");
            index = countDownTotal;
            setClickable(true);
            setVisibility(GONE);
            if (finishedListener != null) {
                finishedListener.finished();
            }
        }
    }

    public interface OnCountDownFinishedListener {
        void finished();
    }
}
