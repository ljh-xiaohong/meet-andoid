package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

public class ObservableScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private boolean isScrolledToBottom;
    private boolean isScrolledToTop;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isNeed ? false : super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
            int distance = 0;
            isScrolledToTop = false;
            isScrolledToBottom = false;
            if (y == 0) {
                isScrolledToTop = true;
                distance = 0;
            } else {
                distance = getHeight() + getScrollY();
                int d = getChildAt(0).getBottom();
                if (distance >= d) {
                    distance = d;
                    isScrolledToBottom = true;
                }
            }
            scrollViewListener.onScrollDistance(distance);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private float downY = 0;
    private float downX = 0;
    public boolean isRefreshing = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isRefreshing) return true;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                downX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = (int) (ev.getRawX() - downX);
                int distanceY = (int) (ev.getRawY() - downY);
                if (scrollingListener != null) {
                    scrollingListener.onScrolling(distanceX, distanceY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int distance = (int) (ev.getRawY() - downY);
                if (Math.abs(distance) > 30) {
                    if (distance > 0 && distance > 200) {
                        int scrollY = getScrollY();
                        if (scrollY == 0) {
                            if (scrollingListener != null) {
//                                scrollingListener.onArrivedTop();
                            }
                        }
                    } else if (distance < 0) {
                        int scrollY = getHeight() + getScrollY();
                        if (scrollY >= getChildAt(0).getBottom()) {
                            if (scrollingListener != null) {
                                scrollingListener.onArrivedBottom();
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isNeed = false;

    public void isNeedToIntercept(boolean isNeed) {
        this.isNeed = isNeed;
    }

    public int getTotalScrollDistance() {
        getChildAt(0).requestLayout();
        return getChildAt(0).getBottom();
    }

    public interface ScrollViewListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

        void onScrollDistance(int distance);
    }

    private ScrollingListener scrollingListener = null;

    public void setScrollingListener(ScrollingListener listener) {
        this.scrollingListener = listener;
    }

    public interface ScrollingListener {
        void onArrivedBottom();

        void onArrivedTop();

        void onScrolling(int scrollingX, int ScrollingY);
    }
}  