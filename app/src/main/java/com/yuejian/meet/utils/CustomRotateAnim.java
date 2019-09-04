package com.yuejian.meet.utils;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CustomRotateAnim
  extends Animation
{
  private static CustomRotateAnim rotateAnim;
  private int mHeight;
  private int mWidth;
  
  public static CustomRotateAnim getCustomRotateAnim()
  {
    if (rotateAnim == null) {
      rotateAnim = new CustomRotateAnim();
    }
    return rotateAnim;
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    paramTransformation.getMatrix().setRotate((float)(Math.sin(paramFloat * 3.141592653589793D * 2.0D) * 20.0D), this.mWidth / 2, this.mHeight / 2);
    super.applyTransformation(paramFloat, paramTransformation);
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
  }
}
