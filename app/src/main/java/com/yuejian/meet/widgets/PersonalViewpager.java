package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author : ljh
 * @time : 2019/10/11 09:36
 * @desc :
 */
public class PersonalViewpager extends ViewPager {

    private boolean isFrist = true;
    private int postion = 0;

    public PersonalViewpager(Context context) {
        super(context);
    }

    public PersonalViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (getChildCount()>0){
            if (postion==1){
                View child;
                if (isFrist){
                    child = getChildAt(0);
                }else {
                    child = getChildAt(1);
                }
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
                Log.e("asdasd","postion="+postion+"height"+height);
            }else if (postion==0){
                View child;
                if (isFrist){
                    child = getChildAt(1);
                }else {
                    child = getChildAt(0);
                }
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
                Log.e("asdasd","postion="+postion+"height"+height);
            }
        }
//        for (int i = 0; i < getChildCount(); i++) {
//            if (postion==1){
//                View child = getChildAt(i);
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                int h = child.getMeasuredHeight();
//                if (h > height) height = h;
//                Log.e("asdasd","postion="+postion+"height"+height);
//            }else if (postion==0){
//                View child = getChildAt(0);
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                int h = child.getMeasuredHeight();
//                if (h > height) height = h;
//                Log.e("asdasd","postion="+postion+"height"+height);
//            }
//        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setIsFrist(boolean isFrist) {
        this.isFrist = isFrist;
    }
    public void setPostion(int postion) {
        this.postion = postion;
    }
}
