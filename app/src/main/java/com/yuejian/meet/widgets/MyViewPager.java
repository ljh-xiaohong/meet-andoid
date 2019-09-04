package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyViewPager extends ViewPager {
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {


        if (v != this && v instanceof ViewPager) {
            ViewPager innerViewPager = (ViewPager) v;
            Log.e("canscroll", "innerViewPager.getAdapter().getCount():" + innerViewPager.getAdapter().getCount() + "->" + "innerViewPager.getCurrentItem():" + innerViewPager.getCurrentItem());
            //如果子Viewpager是最后一页,且向右滑动
            if ((innerViewPager.getAdapter().getCount() - 1) == innerViewPager.getCurrentItem() && dx < 0) {
                return false;
            }
            //如果子Viewpager是第一页,且向左滑动
            if (innerViewPager.getCurrentItem() == 0 && dx > 0) {
                return false;
            }


            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }


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
