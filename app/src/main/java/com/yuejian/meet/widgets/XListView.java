package com.yuejian.meet.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.mcxiaoke.bus.Bus;
import com.yuejian.meet.utils.DensityUtils;

/**
 * Created by zh02 on 2017/8/27.
 */

public class XListView extends ListView implements AbsListView.OnScrollListener {

    public XListView(Context context) {
        super(context);
        initView();
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public XListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setOnScrollListener(this);
        DISTANCE = DensityUtils.dip2px(getContext(), 80);
    }

    private boolean isTop = false;
    private boolean isBottom = false;
    private float downY = 0;
    private int DISTANCE;
    public boolean isLoading = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        isTop = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int distance = (int) (ev.getRawY() - downY);
                if (listener != null) {
                    if (Math.abs(distance) >= 30) {
                        if (distance < 0) {
                            if(getChildAt(0) != null){
                                if (isTop) {
                                    Toast.makeText(getContext(), "refresh", Toast.LENGTH_SHORT).show();
                                    listener.onRefresh();
                                }
                            }
                        } else if (distance > 0) {
                            int y1 = getHeight() + getScrollY();
                            int y2 = getChildAt(getChildCount() - 1).getBottom();
                            isBottom = y1 >= y2;
                            if (isBottom && !isLoading) {
                                if (listener != null) {
                                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                                    listener.onLoadMore();
                                }
                            }
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (SCROLL_STATE_FLING == scrollState) {
            Bus.getDefault().post("list_scrolling");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getFirstVisiblePosition() == 0) {
            Bus.getDefault().post("list_to_top");
        } else {
            Bus.getDefault().post("list_scrolling");
        }
    }

    private DataLoadListener listener = null;

    public void setDataLoadListener(DataLoadListener listener) {
        this.listener = listener;
    }

    public interface DataLoadListener {
        void onRefresh();

        void onLoadMore();
    }
}
