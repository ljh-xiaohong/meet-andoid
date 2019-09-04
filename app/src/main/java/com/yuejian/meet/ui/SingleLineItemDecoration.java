package com.yuejian.meet.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author : g000gle
 * @time : 2019/5/18 15:27
 * @desc : 单列RecyclerView间距设置
 */
public class SingleLineItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SingleLineItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.top = space;
    }
}
