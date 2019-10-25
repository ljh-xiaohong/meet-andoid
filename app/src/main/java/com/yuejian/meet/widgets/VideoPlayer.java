package com.yuejian.meet.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.ScreenUtils;
import com.zhy.view.flowlayout.FlowLayout;

import org.apache.lucene.util.fst.BytesRefFSTEnum;
import org.w3c.dom.Text;

public class VideoPlayer extends StandardGSYVideoPlayer {

    private View back;

    private View start;

    private GestureDetector mGestureDetector;

    private OnSlideListener slideListener;


    /**
     * 主要控制暂停及播放，上下滚动刷新视频操作
     */
    private View dispatch;

    public enum MODEL {
        NORMAL, MEDITATION
    }

    public VideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public VideoPlayer(Context context) {
        super(context);
        mDismissControlTime = 0;
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        //改变进度条位置
//        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics()));
//        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        mBottomProgressBar.setMax(100);
//        mBottomProgressBar.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.progressbar_bg));
//        mBottomProgressBar.setLayoutParams(layoutParams1);
        mDismissControlTime = 0;

        back = this.findViewById(R.id.back_btn);
        back.setOnClickListener(view -> {
            if (this.cancelListener != null) {
                this.cancelListener.finish();
                return;
            }
            ((Activity) getContext()).finish();
        });
        start = this.findViewById(R.id.start_2);
        setOnClickListener(this);

        dispatch = this.findViewById(R.id.dispatch_layout);
        dispatch.setOnTouchListener((view, motionEvent) -> {

            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        });
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {

            int FLIP_DISTANCE = ScreenUtils.getScreenHeight(getContext()) / 6;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {

                startOrPause();

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                //向上滑...
                if (motionEvent.getY() - motionEvent1.getY() > FLIP_DISTANCE) {
                    if (slideListener != null) {
                        slideListener.slideUp();
                    }
//                    Log.e("touch_event", "向上滑");
                    return false;
                }
                //向下滑...
                if (motionEvent1.getY() - motionEvent.getY() > FLIP_DISTANCE) {
                    if (slideListener != null) {
                        slideListener.slideDown();
                    }
//                    Log.e("touch_event", "向下滑");
                    return false;
                }

                return false;
            }
        });
    }

    public interface OnSlideListener {

        void slideUp();

        void slideDown();

    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.slideListener = listener;
    }

    OnCancelListener cancelListener;

    public interface OnCancelListener {
        void finish();
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
    }


    public void setModel(MODEL model) {
        switch (model) {

            case NORMAL:
                setChildVisibility(VISIBLE, getNormalLayout());
                break;

            case MEDITATION:

                setChildVisibility(GONE, getMoreButton(), getNormalLayout());
                setChildVisibility(VISIBLE, getMeditation());

                break;

            default:
                setChildVisibility(VISIBLE, getNormalLayout());
                break;
        }
    }

    private void setChildVisibility(int VISIBILITY, View... views) {
        for (View view : views) {
            view.setVisibility(VISIBILITY);
        }
    }


    @Override
    protected void init(Context context) {
        super.init(context);
        initGestureDetector();

    }

    public ImageView getHeadImagView() {
        return this.findViewById(R.id.circleImageView);
    }

    public View getMoreButton() {
        return this.findViewById(R.id.video_more);
    }

    public TextView getGoodsButton() {
        return this.findViewById(R.id.video_goods);
    }

    public TextView getDiscussButton() {
        return this.findViewById(R.id.video_discuss);
    }

    public TextView getLikeButton() {
        return this.findViewById(R.id.video_like);
    }

    public ImageView getFirstBG() {
        return this.findViewById(R.id.video_first_pic);
    }

    public RelativeLayout getBackGround() {
        return this.findViewById(R.id.dispatch_layout);
    }

    public TextView getShareButton() {
        return this.findViewById(R.id.video_share);
    }

    public View getMeditation() {
        return this.findViewById(R.id.video_layout_meditation);
    }

    public View getNormalLayout() {
        return this.findViewById(R.id.video_layout_normal);
    }

    public TextView getDiscussEdittext() {
        return this.findViewById(R.id.video_discuss_edittext);
    }

    public TextView getNameText() {
        return this.findViewById(R.id.video_name);
    }

    public TextView getFollowText() {
        return this.findViewById(R.id.video_follow);
    }

    public TextView getContenText() {
        return this.findViewById(R.id.video_content);
    }

    public void setTagItem(String[] tab, String[] id, View.OnClickListener listener) {
        if (tab != null && id != null && tab.length == id.length) {
            FlowLayout flowLayout = this.findViewById(R.id.video_tag_layout);
            for (int i = 0; i < tab.length; i++) {
                TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_video_tag, null);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
                lp.leftMargin = 10;
                item.setText(tab[i]);
                item.setTag(id[i]);
                item.setOnClickListener(listener);
                flowLayout.addView(item, lp);
            }
        }
    }

    public void startOrPause() {
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                mCurrentState = CURRENT_STATE_PLAYING;
                getGSYVideoManager().start();
                setViewShowState(mBottomProgressBar, VISIBLE);
                setViewShowState(start, INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mCurrentState == CURRENT_STATE_PLAYING) {
            try {
                onVideoPause();
                setViewShowState(mBottomProgressBar, VISIBLE);
                setViewShowState(start, VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void startPlayLogic() {
        super.startPlayLogic();
        setViewShowState(start, INVISIBLE);
    }

    public void startOrPause(boolean isPlay) {
        if (isPlay) {
            try {
                mCurrentState = CURRENT_STATE_PLAYING;
                getGSYVideoManager().start();
                setViewShowState(mBottomProgressBar, VISIBLE);
                setViewShowState(start, INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                mCurrentState = CURRENT_STATE_PAUSE;
                onVideoPause();
                setViewShowState(mBottomProgressBar, VISIBLE);
                setViewShowState(start, VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
//        super.onClick(v);

//            if (mCurrentState == CURRENT_STATE_PAUSE) {
//                try {
//                    mCurrentState = CURRENT_STATE_PLAYING;
//                    getGSYVideoManager().start();
//                    setViewShowState(mBottomProgressBar, VISIBLE);
//                    setViewShowState(start, INVISIBLE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (mCurrentState == CURRENT_STATE_PLAYING) {
//                try {
//                    onVideoPause();
//                    setViewShowState(mBottomProgressBar, VISIBLE);
//                    setViewShowState(start, VISIBLE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
    }

    public void setLike(boolean isLike, String count) {
        getLikeButton().setText(CommonUtil.changeNum(count));
        getLikeButton().setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(isLike ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_video_zan_nor),
                null,
                null);
    }


    @Override
    public int getLayoutId() {
        return R.layout.widget_video_player;
    }

//    @Override
//    public void startPlayLogic() {
////        setSeekOnStart(0);
//        if (mVideoAllCallBack != null) {
//            Debuger.printfLog("onClickStartThumb");
//            mVideoAllCallBack.onClickStartThumb(mOriginUrl, mTitle, VideoPlayer.this);
//        }
//        prepareVideo();
//        startDismissControlViewTimer();
//    }


    /**
     * 隐藏的控件
     */
    @Override
    protected void hideAllWidget() {
//        super.hideAllWidget();
        setViewShowState(mTitleTextView, GONE);
        setViewShowState(mBackButton, GONE);
        setViewShowState(mLoadingProgressBar, GONE);
        setViewShowState(mCurrentTimeTextView, GONE);
        setViewShowState(mTotalTimeTextView, GONE);
        setViewShowState(mProgressBar, GONE);
        setViewShowState(mFullscreenButton, GONE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mBottomContainer, GONE);
    }

    /**
     * 正常时的状态
     */
    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mBottomContainer, GONE);

    }


    /**
     * 准备时的状态
     */
    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mBottomContainer, GONE);
    }

    /**
     * 开始时的状态
     */
    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mBottomContainer, GONE);
    }

    /**
     * 暂停时的状态
     */
    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mBottomContainer, GONE);
        setViewShowState(mStartButton, VISIBLE);
    }

    /**
     * 错误时的状态
     */
    @Override
    protected void changeUiToError() {

    }

    /**
     * 完成时的状态
     */
    @Override
    protected void changeUiToCompleteShow() {

    }

    /**
     * 获取数据时的状态
     */
    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        mCurrentPosition = 0;
        resetProgressAndTime();

    }

    /**
     * 网络WIFI
     */
    @Override
    protected void showWifiDialog() {

    }
//

    /**
     * 触摸进度
     */

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {

    }
//

    /**
     * 触摸进度
     */

    @Override
    protected void dismissProgressDialog() {

    }
//

    /**
     * 声量
     */
    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }
//

    /**
     * 声量
     */
    @Override
    protected void dismissVolumeDialog() {

    }
//

    /**
     * 亮度
     */

    @Override
    protected void showBrightnessDialog(float percent) {

    }


    /**
     * 拦截光亮度
     *
     * @param percent
     */
    @Override
    protected void onBrightnessSlide(float percent) {

    }


    /**
     * 亮度
     */

    @Override
    protected void dismissBrightnessDialog() {

    }
//


    @Override
    protected void touchDoubleUp() {
//        super.touchDoubleUp();
//        switch (mCurrentState) {
//            case CURRENT_STATE_PAUSE:
//                setViewShowState(mStartButton, VISIBLE);
//                Log.e("touch","CURRENT_STATE_PAUSE");
//
//                break;
//
//            case CURRENT_STATE_PLAYING:
//                setViewShowState(mStartButton, GONE);
//                //暂停播放
//                Log.e("touch","CURRENT_STATE_PLAYING");
//
//                break;
//        }
    }

    /**
     * 点击触摸显示和隐藏逻辑(只限界面显示逻辑)
     */
//    @Override
    protected void onClickUiToggle() {


        switch (mCurrentState) {
            case CURRENT_STATE_PAUSE:
                try {
                    mCurrentState = CURRENT_STATE_PLAYING;
                    getGSYVideoManager().start();
                    setViewShowState(mBottomProgressBar, VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CURRENT_STATE_PLAYING:

                try {
                    onVideoPause();
                    setViewShowState(mBottomProgressBar, VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                if (mCurrentState == CURRENT_STATE_PAUSE) {
//                    try {
//                        mCurrentState = CURRENT_STATE_PLAYING;
//                        getGSYVideoManager().start();
//                        setViewShowState(mBottomProgressBar, VISIBLE);
//                        setViewShowState(start, INVISIBLE);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if (mCurrentState == CURRENT_STATE_PLAYING) {
//                    try {
//                        onVideoPause();
//                        setViewShowState(mBottomProgressBar, VISIBLE);
//                        setViewShowState(start, VISIBLE);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                break;
//        }

        return false;
    }

    protected void updateStartImage() {


        if (mStartButton != null) {
            if (mStartButton instanceof ImageView) {
                switch (mCurrentState) {
                    case CURRENT_STATE_PLAYING:
                        setViewShowState(mStartButton, GONE);
                        break;

                    case CURRENT_STATE_PAUSE:
                        setViewShowState(mStartButton, VISIBLE);
                        break;

                }

            }
        }
    }
}
