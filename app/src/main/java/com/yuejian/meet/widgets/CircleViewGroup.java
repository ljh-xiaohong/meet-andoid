package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CircleViewGroup extends FrameLayout {
    private float radio = 20f;

    public CircleViewGroup(Context context) {
        super(context);
    }

    public CircleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        Path path = new Path();
        path.addRoundRect(
                new RectF(0, 0, getWidth(), getHeight())
                , new float[]{radio, radio, radio, radio, radio, radio, radio, radio}
                , Path.Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

}
