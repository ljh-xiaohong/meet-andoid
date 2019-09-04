package com.yuejian.meet.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author : g000gle
 * @time : 2019/5/23 17:47
 * @desc : 水平单行RecyclerView间距设置
 */
public class HorizontalLineItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public HorizontalLineItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
    }

}
