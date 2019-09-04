package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.R;
import com.yuejian.meet.utils.Utils;

public class MaskPierceView
        extends View {
    private static final String TAG = "MaskPierceView";
    private Bitmap mDstCircle;
    private int mPiercedRadius;
    private int mPiercedX;
    private int mPiercedY;
    private int mScreenHeight;
    private int mScreenWidth;
    private Bitmap mSrcRect;

    public MaskPierceView(Context paramContext) {
        this(paramContext, null);
    }

    public MaskPierceView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (this.mScreenWidth == 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            this.mScreenWidth = displayMetrics.widthPixels;
            this.mScreenHeight = displayMetrics.heightPixels;
        }
    }

    private Bitmap createPromptBitmap() {
        Bitmap localBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bj);
        float i = localBitmap.getWidth();
        float j = localBitmap.getHeight();
        int k = this.mScreenHeight;
        int m = Utils.dp2px(getContext(), 50.0F);
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(this.mScreenWidth / i, (k - m) / j * 0.96F);
        return Bitmap.createBitmap(localBitmap, 0, 0, (int)i, (int)j, localMatrix, true);
    }

    private Bitmap makeDstCircle() {
        Bitmap localBitmap = Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-1);
        localCanvas.drawCircle(getWidth() / 2, getHeight() / 2, Utils.dp2px(getContext(), 120.0F), localPaint);
        return localBitmap;
    }

    private Bitmap makeSrcRect() {
        Bitmap localBitmap = Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        localCanvas.drawRect(new RectF(0.0F, 0.0F, this.mScreenWidth, this.mScreenHeight), localPaint);
        return localBitmap;
    }

    protected void onDraw(Canvas paramCanvas) {
        this.mSrcRect = makeSrcRect();
        this.mDstCircle = makeDstCircle();
        Paint localPaint = new Paint();
        localPaint.setFilterBitmap(false);
        paramCanvas.saveLayer(0.0F, 0.0F, this.mScreenWidth, this.mScreenHeight, null, 31);
        paramCanvas.drawBitmap(this.mDstCircle, 0.0F, 0.0F, localPaint);
        localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        localPaint.setAlpha(255);
        paramCanvas.drawBitmap(createPromptBitmap(), 0.0F, 0.0F, localPaint);
        localPaint.setXfermode(null);
        paramCanvas.saveLayer(0.0F, 0.0F, this.mScreenWidth, this.mScreenHeight, null, 31);
    }

    public void setPiercePosition(int paramInt1, int paramInt2, int paramInt3) {
        this.mPiercedX = paramInt1;
        this.mPiercedY = paramInt2;
        this.mPiercedRadius = paramInt3;
        invalidate();
    }
}
