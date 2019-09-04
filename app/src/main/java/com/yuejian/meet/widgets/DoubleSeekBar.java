package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.utils.DensityUtils;

public class DoubleSeekBar extends View {
    private int preX;
    private int startMark, endMark;
    private Bitmap thumb;
    private Paint paint;
    private int mScrollBarWidth, mScrollBarHeight;  //控件宽度=滑动条宽度+滑动块宽度
    private int offset;//控件的偏移量
    private int progressLow, progressHigh;
    private int mCurrentStartX = -1;
    private int mCurrentEndX = -1;
    private float r, d;
    private OnSeekBarChangeListener mBarChangeListener;

    public DoubleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);

        r = DensityUtils.dip2px(getContext(), 10);
        d = DensityUtils.dip2px(getContext(), 20);

        startMark = 1;
        endMark = 3;
        progressLow = -1;
        progressHigh = progressLow;
        thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_seek_icon);
    }

    //默认执行，计算view的宽高,在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        mScrollBarWidth = width;
        mScrollBarHeight = DensityUtils.dip2px(getContext(), 10);
        offset = (int) ((width - getPaddingLeft() - getPaddingRight() - 2 * r) / 5);
        if (mCurrentStartX == -1 && mCurrentEndX == mCurrentStartX) {
            mCurrentStartX = startMark * offset + getPaddingLeft();
            mCurrentEndX = endMark * offset + getPaddingLeft();
        }
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specSize = MeasureSpec.getSize(measureSpec);
        return specSize;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int defaultHeight = 80;
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
            defaultHeight = specSize;
        }
        return defaultHeight;
    }

    //处理手势
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                preX = (int) event.getX() - getPaddingLeft();
                if (preX < mCurrentStartX) {
                    if (preX < 1) {
                        preX = 1;
                    }
                    mCurrentStartX = preX;
                } else if (preX < mCurrentEndX && preX > mCurrentStartX) {
                    int distance = mCurrentEndX - mCurrentStartX;
                    if ((preX - mCurrentStartX) < distance / 2) {
                        mCurrentStartX = preX;
                    } else if ((preX - mCurrentStartX) >= distance / 2) {
                        mCurrentEndX = preX;
                    }
                } else if (preX >= mCurrentEndX) {
                    if (preX >= getWidth() - getPaddingRight() - 2 * r) {
                        preX = (int) (getWidth() - getPaddingRight() - 2 * r);
                        if( mCurrentStartX >= mCurrentEndX){
                            mCurrentStartX = preX;
                        }
                    }
                    mCurrentEndX = preX;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景
        RectF rectF = new RectF(getPaddingLeft() + r, getHeight() / 2 - mScrollBarHeight / 2, getWidth() - getPaddingRight() - 2 * r, getHeight() / 2 + mScrollBarHeight / 2);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#E3E3E3"));
        canvas.drawRoundRect(rectF, mScrollBarHeight / 2, mScrollBarHeight / 2, paint);

        float start = mCurrentStartX;
        start = start <= 0 ? 1 : start;

        float end = mCurrentEndX + r;
        end = end >= (getWidth() - getPaddingRight() - 2 * r) ? getWidth() - getPaddingRight() - 2 * r : end;
        if (end <= 1) {
            end = 1;
        }

        float fullProgressLength = getWidth() - getPaddingLeft() - getPaddingRight() - 2 * r;
        progressLow = (int) ((start - getPaddingLeft()) * 100 / fullProgressLength);
        progressHigh = (int) ((end - getPaddingLeft()) * 100 / fullProgressLength);

        progressLow = progressLow <= 1 ? 1 : progressLow;

        //绘制进度
        rectF = new RectF(start + r, getHeight() / 2 - mScrollBarHeight / 2, end + r / 2, getHeight() / 2 + mScrollBarHeight / 2);
        paint.setColor(getContext().getResources().getColor(R.color.txt_color_yellow));
        canvas.drawRect(rectF, paint);

        //绘制第一个游标
        rectF = new RectF(start, getHeight() / 2 - r, start + 2 * r, getHeight() / 2 + r);
        canvas.drawBitmap(thumb, null, rectF, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#A3A3A3"));
        paint.setTextSize(DensityUtils.dip2px(getContext(), 12));
        String startAge = String.valueOf(progressLow > 0 ? progressLow : 1);
        canvas.drawText(startAge, 0, startAge.length(), start + r, getHeight() / 2 + d, paint);
        if(end - start- 2*r>0){

        }
        //绘制第二个游标
        rectF = new RectF(end - r, getHeight() / 2 - r, end + r, getHeight() / 2 + r);
        canvas.drawBitmap(thumb, null, rectF, paint);

        String endAge = String.valueOf(progressHigh);
        canvas.drawText(endAge, 0, endAge.length(), end, getHeight() / 2 + d, paint);


        if (mBarChangeListener != null) {
            mBarChangeListener.onProgressChanged(DoubleSeekBar.this, progressLow, progressHigh);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    public void initProgress(int low, int high) {
        progressLow = low;
        progressHigh = high;
    }

    //回调函数，在滑动时实时调用，改变输入框的值
    public interface OnSeekBarChangeListener {
        //滑动时
        void onProgressChanged(DoubleSeekBar seekBar, int progressLow, int progressHigh);

        //滑动后
        void onProgressAfter();
    }
}