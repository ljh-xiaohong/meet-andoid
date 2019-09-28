package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

public class MyNestedScrollView extends NestedScrollView {

    OnScrollViewListener listener;


    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.listener != null) this.listener.ScrollView(this,l, t, oldl, oldt);
    }

    public interface OnScrollViewListener {
        void ScrollView(NestedScrollView v ,int l, int t, int oldl, int oldt);
    }

    public void setonScrollChanged(OnScrollViewListener listener) {
        this.listener = listener;
    }
}
