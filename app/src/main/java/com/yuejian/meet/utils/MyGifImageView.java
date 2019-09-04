package com.yuejian.meet.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author : ljh
 * @time : 2019/8/19 16:22
 * @desc :
 */
public class MyGifImageView extends GifImageView {
    public MyGifImageView(Context context) {
        super(context);
    }

    public MyGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if(d!=null){
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }
}
