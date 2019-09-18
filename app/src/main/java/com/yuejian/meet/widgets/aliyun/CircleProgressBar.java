package com.yuejian.meet.widgets.aliyun;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.yuejian.meet.R;

public class CircleProgressBar extends View implements View.OnClickListener{
    String TAG = getClass().toString();
    Paint mPaintBase;
    Paint mPaintLoading;
    Paint mPaintBorder;
    Paint mPaintShape;
    int seconds = 30;
    long indexSeconds = 0l;
    float degrees = 0;

    int baseColor = Color.parseColor("#ffffff");
    int loadColor = Color.parseColor("#F93C54");
    int bottomColor = Color.parseColor("#99ffffff");
    float border = 10;

    //true：说明处于正在运行状态，false：说明处于非工作状态；
    boolean hasWindowFocus;
    //true:说明正在绘制
    boolean isLoading;

    OnLoadingListener listener;

    ValueAnimator valueAnimator;

    AnimationController animationController;


    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        baseColor = array.getColor(R.styleable.CircleProgressBar_baseColor, baseColor);
        loadColor = array.getColor(R.styleable.CircleProgressBar_loadColor, loadColor);
        border = array.getDimension(R.styleable.CircleProgressBar_broder, border);
        seconds = array.getInteger(R.styleable.CircleProgressBar_progress_seconds, seconds);
        array.recycle();
        init();

    }

    private void init() {
        setBasePaint();
        setBorderPaint();
        setLoadingPaint();
        //使用该空间时，屏幕为一直亮着的状态
        ((Activity) this.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setOnClickListener(this);
        animationController = new AnimationController();
        valueAnimator = ValueAnimator.ofFloat(0, 360f);
        valueAnimator.setDuration(seconds * 1000);
        valueAnimator.addUpdateListener(animationController);
        valueAnimator.addListener(animationController);
    }


    /**
     * 轨迹画笔
     */
    private void setBorderPaint() {
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStrokeWidth(border);
        mPaintBorder.setColor(baseColor);
        mPaintBorder.setStyle(Paint.Style.STROKE);
    }

    /**
     * 图形画笔
     */
    private void setShapePaint() {
        mPaintShape = new Paint();
        mPaintShape.setAntiAlias(true);
        mPaintShape.setStrokeWidth(border);
        mPaintShape.setColor(baseColor);
        mPaintShape.setStyle(Paint.Style.FILL);
    }

    /**
     * 进度画笔
     */
    private void setLoadingPaint() {
        mPaintLoading = new Paint();
        mPaintLoading.setAntiAlias(true);
        mPaintLoading.setStrokeWidth(border);
        mPaintLoading.setColor(loadColor);
        mPaintLoading.setStrokeCap(Paint.Cap.ROUND);
        mPaintLoading.setStyle(Paint.Style.STROKE);
    }

    /**
     * 底图画笔
     */
    private void setBasePaint() {
        mPaintBase = new Paint();
        mPaintBase.setAntiAlias(true);
        mPaintBase.setStrokeWidth(border);
        mPaintBase.setColor(bottomColor);
        mPaintBase.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //取最小值，让控件呈现一个正方形；
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //正方形
        RectF rectangle = new RectF(width * 0.25f + border * 1.2f, width * 0.25f + border * 1.2f, width * 0.75f - border / 1.2f, width * 0.75f - border / 1.2f);
        //圆形
        RectF circle = new RectF(0 + border, 0 + border, width - border, height - border);


        //使用后释放资源 同时也减少不必要的重绘
        if (mPaintBase != null) {
            canvas.drawArc(circle, 0, 360, true, mPaintBase);
//            mPaintBase = null;
        }

        if (mPaintBorder != null) {
            canvas.drawArc(circle, 0, 360, false, mPaintBorder);

//            mPaintBorder = null;
        }

        if (mPaintShape != null) {
            canvas.drawRoundRect(rectangle, 5, 5, mPaintShape);
        }

        if (mPaintLoading != null) {

            canvas.drawArc(circle, 270, degrees, false, mPaintLoading);

        }


        // canvas.drawArc(circle, 270, 0, false, mPaintloading);

    }

    /**
     * 暂定录制的状态
     */
    private void PauseStatus() {
        mPaintShape = null;
//        mPaintLoading = null;
        invalidate();
    }

    public void setBaseStatus() {
        mPaintShape = null;
        mPaintLoading = null;
        invalidate();
    }

    /**
     * 当用时返回true，当处于后台或被遮盖时返回false；
     * 由于onWindowFocusChanged()在屏护时不一定能及时返回，所以在此空间内设置了“屏幕常亮功能”
     *
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasWindowFocus = hasWindowFocus;

        if (!hasWindowFocus) {
            setBorderPaint();
//            setLoadingPaint();
            setBasePaint();

        }

    }

    @Override
    public void onClick(View view) {

        if (isLoading) {
            stopLoading();
        } else {
            startLoading();
        }

    }

    /**
     * 开始录制
     */
    public void startLoading() {
        isLoading = true;
        setShapePaint();
        setLoadingPaint();
        valueAnimator.start();

    }

    /**
     * 停止录制
     */
    public void stopLoading() {
        isLoading = false;
        mPaintShape = null;
        valueAnimator.cancel();
    }


    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }


    public interface OnLoadingListener {
        void getStatus(int seconds, boolean isFinish);

        void startLoading();

        void stopLoading(long playTime);
    }

    public void setLoadingTime(int seconds) {
        this.seconds = seconds;

        valueAnimator.setDuration(this.seconds * 1000);
    }

    class AnimationController extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            invalidate();
            indexSeconds = valueAnimator.getCurrentPlayTime();
            if (null != listener) {
                listener.stopLoading(indexSeconds);
                PauseStatus();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            indexSeconds = valueAnimator.getCurrentPlayTime();
            if (null != listener) {
                listener.stopLoading(indexSeconds);
                PauseStatus();
            }
        }

        @Override
        public void onAnimationPause(Animator animation) {
            super.onAnimationPause(animation);

        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            if (null != listener) {
                listener.startLoading();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            degrees = (float) valueAnimator.getAnimatedValue();
            invalidate();

        }
    }

}

