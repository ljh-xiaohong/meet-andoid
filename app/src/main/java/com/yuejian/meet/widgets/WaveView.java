package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaveView
  extends View
{
  private List<Circle> mCircleList = new ArrayList();
  private Runnable mCreateCircle = new Runnable()
  {
    public void run()
    {
      if (WaveView.this.mIsRunning)
      {
        WaveView.this.newCircle();
        WaveView.this.postDelayed(WaveView.this.mCreateCircle, WaveView.this.mSpeed);
      }
    }
  };
  private long mDuration = 2000L;
  private float mInitialRadius;
  private Interpolator mInterpolator = new LinearInterpolator();
  private boolean mIsRunning;
  private long mLastCreateTime;
  private float mMaxRadius;
  private float mMaxRadiusRate = 0.85F;
  private boolean mMaxRadiusSet;
  private Paint mPaint = new Paint(1);
  private int mSpeed = 500;
  
  public WaveView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public WaveView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mPaint.setColor(-1);
    this.mPaint.setStrokeWidth(2.0F);
    setStyle(Paint.Style.STROKE);
  }
  
  private void newCircle()
  {
    long l = System.currentTimeMillis();
    if (l - this.mLastCreateTime < this.mSpeed) {
      return;
    }
    Circle localCircle = new Circle();
    this.mCircleList.add(localCircle);
    invalidate();
    this.mLastCreateTime = l;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    Iterator localIterator = this.mCircleList.iterator();
    while (localIterator.hasNext())
    {
      Circle localCircle = (Circle)localIterator.next();
      if (System.currentTimeMillis() - localCircle.mCreateTime < this.mDuration)
      {
        this.mPaint.setAlpha(localCircle.getAlpha());
        paramCanvas.drawCircle(getWidth() / 2, getHeight() / 2, localCircle.getCurrentRadius(), this.mPaint);
      }
      else
      {
        localIterator.remove();
      }
    }
    if (this.mCircleList.size() > 0) {
      postInvalidateDelayed(10L);
    }
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!this.mMaxRadiusSet) {
      this.mMaxRadius = (Math.min(paramInt1, paramInt2) * this.mMaxRadiusRate / 2.0F);
    }
  }
  
  public void setColor(int paramInt)
  {
    this.mPaint.setColor(paramInt);
  }
  
  public void setDuration(long paramLong)
  {
    this.mDuration = paramLong;
  }
  
  public void setInitialRadius(float paramFloat)
  {
    this.mInitialRadius = paramFloat;
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    this.mInterpolator = paramInterpolator;
    if (this.mInterpolator == null) {
      this.mInterpolator = new LinearInterpolator();
    }
  }
  
  public void setMaxRadius(float paramFloat)
  {
    this.mMaxRadius = paramFloat;
    this.mMaxRadiusSet = true;
  }
  
  public void setMaxRadiusRate(float paramFloat)
  {
    this.mMaxRadiusRate = paramFloat;
  }
  
  public void setSpeed(int paramInt)
  {
    this.mSpeed = paramInt;
  }
  
  public void setStyle(Paint.Style paramStyle)
  {
    this.mPaint.setStyle(paramStyle);
  }
  
  public void start()
  {
    if (!this.mIsRunning)
    {
      this.mIsRunning = true;
      this.mCreateCircle.run();
    }
  }
  
  public void stop()
  {
    this.mIsRunning = false;
  }
  
  private class Circle
  {
    private long mCreateTime = System.currentTimeMillis();
    
    public Circle() {}
    
    public int getAlpha()
    {
      float f = (float)(System.currentTimeMillis() - this.mCreateTime) * 1.0F / (float)WaveView.this.mDuration;
      return (int)((1.0F - WaveView.this.mInterpolator.getInterpolation(f)) * 255.0F);
    }
    
    public float getCurrentRadius()
    {
      float f = (float)(System.currentTimeMillis() - this.mCreateTime) * 1.0F / (float)WaveView.this.mDuration;
      return WaveView.this.mInitialRadius + WaveView.this.mInterpolator.getInterpolation(f) * (WaveView.this.mMaxRadius - WaveView.this.mInitialRadius);
    }
  }
}
