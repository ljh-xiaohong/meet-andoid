package com.yuejian.meet.bean.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.LinearInterpolator;
import com.yuejian.meet.R;
import com.yuejian.meet.bsr.BSRGiftLayout;
import com.yuejian.meet.bsr.BSRGiftView;
import com.yuejian.meet.bsr.BSRPathBase;
import com.yuejian.meet.bsr.BSRPathPoint;
import com.yuejian.meet.bsr.BSRPathView;
import com.yuejian.meet.bsr.OnAnmEndListener;
import java.io.Serializable;
import java.util.Timer;


/**
 * Created by yan on 2016/12/14.
 */

public class GiftAnmManager {

    private BSRGiftLayout giftLayout;
    private Context context;
    private int curIndex;
    private Timer timer;
    private Bitmap res;
    int[] car1Ids = new int[]{
            R.mipmap.app_logo
    };

    public GiftAnmManager(BSRGiftLayout giftLayout, Context context) {
        this.context = context;
        this.giftLayout = giftLayout;
    }

    public void setBitmapRes(Bitmap bitmapRes){
        this.res = bitmapRes;
    }

    public void showCarOne() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 150;
        BSRPathPoint carOne = new BSRPathPoint();
        carOne.setDuring(during);
        carOne.setInterpolator(new LinearInterpolator());
        carOne.setRes(context,car1Ids[0]);
        carOne.adjustScaleInScreen(0.8f);
        carOne.setAntiAlias(true);
        bsrGiftView.addBSRPathPointAndDraw(carOne);
        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.positionInScreen();// 设置位置为相对控件的位置（比如0.5是控件的中心点）
        bsrPathView.addPositionControlPoint(-1f, 0.0f);// 恒定bsr的位置
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(0.06f, 0.2f);
        bsrPathView.addPositionControlPoint(1f, 0.3f);
        bsrPathView.setXPercent(0f);
        bsrPathView.setYPercent(0f);
        bsrPathView.setDuring(4000);// 设置动画执行时间
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {

            }
        });
        giftLayout.addChild(bsrPathView);
    }

    public void onDestroy() {

    }

    public static class Action implements Serializable {
        public AnimationUserDataBean userBean;
        public AnimationInfoBean.DataBean dataBean;
        public int times = 1;

        public Action(AnimationUserDataBean userBean, AnimationInfoBean.DataBean dataBean) {
            this.userBean = userBean;
            this.dataBean = dataBean;
        }
    }

}
