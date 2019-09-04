package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class XWebView extends WebView {
    public Context context;

    public interface PlayFinish{
        void After();
    }
    PlayFinish df;
    public void setDf(PlayFinish playFinish) {
        this.df = playFinish;
    }
    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        setBackgroundColor(0);
        setVerticalScrollBarEnabled(false);

    }
    public XWebView(Context context) {
        super(context);
        this.context=context;
    }
    //onDraw表示显示完毕
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        df.After();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    /**
     * 设置不能点击
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }


}