package com.yuejian.meet.widgets.aliyun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.aliyun.common.utils.DensityUtil;

import java.util.Random;

public class MusicWaveView extends View {
    private static final String TAG = "MusicWaveView";

    private int mDisplayTime;
    private int mTotalTime;
    private float[] mWaveArray;
    private int mStartOffset;
    private int mWaveHeight;
    private int mScreenWidth;
    private int mSelectBgWidth;

    //    private static final int WAVE_STEP = 1 * 1000;
    private static final int WAVE_WIDTH = 4;
    private static final int WAVE_OFFSET = 6;
    private static final float MIN_WAVE_RATE = 0.25f;

    private Paint mPaint = new Paint();
    private RectF mRect = new RectF();

    private int mWidth;
    private int mHeight;

    public MusicWaveView(Context context) {
        this(context, null);

    }

    public MusicWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //与选中的框的高宽一致
        mWaveHeight = DensityUtil.dip2px(context, 32);
        mSelectBgWidth = DensityUtil.dip2px(context, 180);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        //Waves开始绘制的值
        mStartOffset = (mScreenWidth - mSelectBgWidth) / 2;
        setWillNotDraw(false);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#4C5355"));
    }

    public void layout() {
        if (mDisplayTime != 0 && mTotalTime != 0) {
            //根据音乐的时长及需要的大小，计算自身的宽度
            //宽度=（音乐总长/需要的录制大小）*crop的宽+空白处的宽度
            int lWidth = (int) ((mTotalTime / (float) mDisplayTime) * mSelectBgWidth) + mStartOffset * 2;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
            params.width = lWidth;
            setLayoutParams(params);
            mWidth = lWidth;
            mHeight = params.height;
            generateWaveArray();
            invalidate();
        }

    }

    private void generateWaveArray() {
        //根据控件的宽度，获取条状的个数
        //自身控件的宽-空白处/每个条形的宽度+每个条形的间隔宽度；
        int count = (mWidth - (mStartOffset * 2)) / (WAVE_WIDTH + WAVE_OFFSET);
        mWaveArray = new float[count];
        //伪随机，获取每个条形的高的高度比例；
        Random random = new Random();
        random.setSeed(mTotalTime);
        for (int i = 0; i < count; i++) {
            mWaveArray[i] = random.nextFloat();
            if (mWaveArray[i] < MIN_WAVE_RATE) {
                mWaveArray[i] += MIN_WAVE_RATE;
            }
        }
    }

    public void setDisplayTime(int displayTime) {
        mDisplayTime = displayTime;
    }

    /**
     * 输入该音乐的总时长
     *
     * @param totalTime
     */
    public void setTotalTime(int totalTime) {
        mTotalTime = totalTime;
    }

    /**
     * 获取总音乐的选中框的长度
     *
     * @return
     */
    public int getMusicLayoutWidth() {
        return (mWidth - (mStartOffset * 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int right;
        if (getHeight() != 0 && mWaveArray != null) {
            for (int i = 0; i < mWaveArray.length; i++) {

                //获取每个条形的高度；
                int height = (int) (mWaveHeight * mWaveArray[i]);
                int left = i * (WAVE_OFFSET + WAVE_WIDTH + (getPaddingTop() + getPaddingBottom()) / 2) + mStartOffset + (i == 0 ? 4 : 0);
                right = left + WAVE_WIDTH;
                if (right >= getWidth() - mStartOffset) {
                    return;
                }
                int top = (getHeight() - height) / 2;
                int bottom = top + height;
                mRect.set(left, top, right, bottom);

                canvas.drawRoundRect(mRect, 20, 20, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = 0;
        int height = 0;
        //获得宽度MODE
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        //获得宽度的值
        if (modeW == MeasureSpec.AT_MOST) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (modeW == MeasureSpec.EXACTLY) {
            width = widthMeasureSpec;
        }
        if (modeW == MeasureSpec.UNSPECIFIED) {
            width = mWidth;
        }
        //获得高度MODE
        int modeH = MeasureSpec.getMode(height);
        //获得高度的值
        if (modeH == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        if (modeH == MeasureSpec.EXACTLY) {
            height = heightMeasureSpec;
        }
        if (modeH == MeasureSpec.UNSPECIFIED) {
            //ScrollView和HorizontalScrollView
            height = mHeight;
        }
        //设置宽度和高度
        setMeasuredDimension(width, height);
    }

}
