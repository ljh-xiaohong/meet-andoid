package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyInnerViewPager extends ViewPager {
    public MyInnerViewPager(@NonNull Context context) {
        super(context);
    }

    public MyInnerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        super.onInterceptTouchEvent(ev);
//        return true;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        super.onTouchEvent(ev);
//
//        //判断子item总个数不大于1，TouchEvent返回默认值，继续消费。
//        if (getChildCount() <= 1) {
//            super.onTouchEvent(ev);
//        }
//        //获取事件
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN://按下
//                if (getParent() != null) {
//
//                    getParent().requestDisallowInterceptTouchEvent(true);   //让事件不再分发
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE://移动
//                if (getParent() != null) {
//                    getParent().requestDisallowInterceptTouchEvent(true);   //让事件不再分发
//                }
//                break;
//
//            case MotionEvent.ACTION_CANCEL://非人工操作
//                if (getParent() != null) {
//                    getParent().requestDisallowInterceptTouchEvent(true);   //让事件不再分发
//                }
//                break;
//
//            case MotionEvent.ACTION_UP://抬起
//                if (getParent() != null) {
//                    getParent().requestDisallowInterceptTouchEvent(true);   //让事件不再分发
//                }
//                break;
//        }
//
//        return true;        //让事件不再分发
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                //让子View竟可能的大
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) {
                    height = h;
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
