package com.netease.nim.uikit.app.widgets;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.netease.nim.uikit.app.Adapter.ZoomOutPageTransformer;
import com.netease.nim.uikit.common.ui.imageview.MsgThumbImageView;

public class ChildViewPager extends ViewPager {
    Context mContext;
    private ViewGroup.LayoutParams mLayoutParams;
//    public ChildViewPager(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    public ChildViewPager(Context context) {
        super(context);
        this.mContext=context;
    }

    private void init() {
        int pagerWidth = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 5.0f / 5.0f);
        mLayoutParams = this.getLayoutParams();
        if (mLayoutParams == null) {
            mLayoutParams = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams.width = pagerWidth;
        }
        this.setLayoutParams(mLayoutParams);
        this.setPageMargin(-50);
        this.setPageTransformer(false, new ZoomOutPageTransformer());
        this.setClipChildren(false);
        this.setFitsSystemWindows(true);
        final ViewGroup parent = (ViewGroup) this.getParent();
        if (parent != null) {
            parent.setClipChildren(false);
            parent.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return dispatchTouchEvent(event);
                }
            });
        }

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < getChildCount(); i++){
                    View view = getChildAt(i);
                    if (view instanceof MyImageView) {
                        final MyImageView imageView = (MyImageView) view;
                        imageView.reset();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast,xDown, mLeft;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //把touch主动权交给子view处理，不拦截touch事件，也就是viewpager
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                xDown = ev.getX();
                mLeft = ev.getX();
                // 解决与侧边栏滑动冲突
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (mLeft < 100 || xDistance < yDistance) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (getCurrentItem() == 0) {
                        if (curX < xDown) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else if (getCurrentItem() == (getAdapter().getCount()-1)) {
                        if (curX > xDown) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof HorizontalScrollView) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

}
