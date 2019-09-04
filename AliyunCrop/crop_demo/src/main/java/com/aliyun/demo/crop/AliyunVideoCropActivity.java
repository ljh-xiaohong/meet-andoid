/*
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 */

package com.aliyun.demo.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.global.Version;
import com.aliyun.common.utils.DensityUtil;
import com.aliyun.common.utils.FileUtils;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.crop.AliyunCropCreator;
import com.aliyun.crop.struct.CropParam;
import com.aliyun.crop.supply.AliyunICrop;
import com.aliyun.crop.supply.CropCallback;
import com.aliyun.demo.crop.media.FrameExtractor10;
import com.aliyun.demo.crop.media.VideoTrimAdapter;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.svideo.base.ActionInfo;
import com.aliyun.svideo.base.AlivcSvideoEditParam;
import com.aliyun.svideo.base.AliyunSvideoActionConfig;
import com.aliyun.svideo.base.MediaInfo;
import com.aliyun.svideo.base.widget.FanProgressBar;
import com.aliyun.svideo.base.widget.HorizontalListView;
import com.aliyun.svideo.base.widget.SizeChangedNotifier;
import com.aliyun.svideo.base.widget.VideoSliceSeekBar;
import com.aliyun.svideo.base.widget.VideoTrimFrameLayout;
import com.aliyun.svideo.player.AliyunISVideoPlayer;
import com.aliyun.svideo.player.AliyunSVideoPlayerCreator;
import com.aliyun.svideo.player.PlayerCallback;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.aliyun.video.common.utils.DateTimeUtils;
import com.duanqu.transcode.NativeParser;

import java.io.File;


/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 * <p>
 * Created by Administrator on 2017/1/16.
 */

public class AliyunVideoCropActivity extends Activity implements TextureView.SurfaceTextureListener, HorizontalListView.OnScrollCallBack, SizeChangedNotifier.Listener,
    MediaPlayer.OnVideoSizeChangedListener, VideoTrimFrameLayout.OnVideoScrollCallBack, View.OnClickListener, CropCallback, Handler.Callback {

    public static final VideoDisplayMode SCALE_CROP = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_FILL = VideoDisplayMode.FILL;

    private static final int PLAY_VIDEO = 1000;
    private static final int PAUSE_VIDEO = 1001;
    private static final int END_VIDEO = 1003;

    private int playState = END_VIDEO;

    private AliyunICrop crop;

    private HorizontalListView listView;
    private VideoTrimFrameLayout frame;
    private TextureView textureview;
    private Surface mSurface;

    /**
     * sdk提供的播放器，支持非关键帧的实时预览
     */
    private AliyunISVideoPlayer mPlayer;
    private ImageView cancelBtn, nextBtn, transFormBtn;
    private TextView dirationTxt;

    private VideoSliceSeekBar seekBar;

    private FanProgressBar mCropProgress;
    private FrameLayout mCropProgressBg;

    private String path;

    /**
     * 裁剪文件的输出目录
     */
    private final static String OUTPUT_PATH_DIR = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;
    /**
     * 裁剪文件后缀
     */
    private final static String OUTPUT_PATH_SUFFIX = "-crop.mp4";
    private String outputPath;
    private long duration;
    private int resolutionMode;
    private int ratioMode;
    private VideoQuality quality = VideoQuality.HD;
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;
    private int frameRate;
    private int gop;
    private int mBitrate;

    private int screenWidth;
    private int frameWidth;
    private int frameHeight;
    private int mScrollX;
    private int mScrollY;
    private int videoWidth;
    private int videoHeight;
    private int cropDuration = 2000;

    private VideoDisplayMode cropMode = VideoDisplayMode.SCALE;

    private long mStartTime;
    private long mEndTime;

    private final static int MAX_DURATION = Integer.MAX_VALUE;

    private Handler playHandler = new Handler(this);

    private boolean isPause = false;
    private boolean isCropping = false;
    /**
     * 每次修改裁剪结束位置时需要重新播放视频
     */
    private boolean needPlayStart = false;
    private boolean isUseGPU = false;

    private int mAction = CropKey.ACTION_TRANSCODE;

    /**
     * 原比例
     */
    private static final int RATIO_ORIGINAL = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.aliyun_svideo_activity_crop);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        crop = AliyunCropCreator.createCropInstance(this);
        crop.setCropCallback(this);
        getData();
        initView();
        initSurface();
    }

    private void getData() {
        mAction = getIntent().getIntExtra(CropKey.ACTION, CropKey.ACTION_SELECT_TIME);
        path = getIntent().getStringExtra(CropKey.VIDEO_PATH);
        try {
            duration = crop.getVideoDuration(path) / 1000;
        } catch (Exception e) {
            ToastUtil.showToast(this, R.string.alivc_crop_error);
        }//获取精确的视频时间
        resolutionMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, AliyunSnapVideoParam.RESOLUTION_720P);
        cropMode = (VideoDisplayMode) getIntent().getSerializableExtra(AliyunSnapVideoParam.CROP_MODE);
        if (cropMode == null) {
            cropMode = VideoDisplayMode.SCALE;
        }
        quality = (VideoQuality) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_QUALITY);
        if (quality == null) {
            quality = VideoQuality.HD;
        }
        gop = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_GOP, 250);
        mBitrate = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_BITRATE, 0);
        frameRate = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, 30);
        ratioMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RATIO, AliyunSnapVideoParam.RATIO_MODE_9_16);
        cropDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MIN_CROP_DURATION, 2000);
        isUseGPU = getIntent().getBooleanExtra(AliyunSnapVideoParam.CROP_USE_GPU, false);
        mVideoCodec = (VideoCodecs) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_CODEC);
    }


    public static String getVersion() {
        return Version.VERSION;
    }

    private void initView() {
        int mOutStrokeWidth = DensityUtil.dip2px(this, 5);
        FrameExtractor10 kFrame = new FrameExtractor10();
        kFrame.setDataSource(path);
        seekBar = (VideoSliceSeekBar) findViewById(R.id.aliyun_seek_bar);
        seekBar.setSeekBarChangeListener(mSeekBarListener);
        int minDiff = (int) (cropDuration / (float) duration * 100) + 1;
        seekBar.setProgressMinDiff(minDiff > 100 ? 100 : minDiff);
        listView = (HorizontalListView) findViewById(R.id.aliyun_video_tailor_image_list);
        listView.setOnScrollCallBack(this);
        VideoTrimAdapter adapter = new VideoTrimAdapter(this, duration, MAX_DURATION, kFrame, seekBar);
        listView.setAdapter(adapter);
        transFormBtn = (ImageView) findViewById(R.id.aliyun_transform);
        transFormBtn.setOnClickListener(this);
        nextBtn = (ImageView) findViewById(R.id.aliyun_next);
        nextBtn.setOnClickListener(this);
        cancelBtn = (ImageView) findViewById(R.id.aliyun_back);
        cancelBtn.setOnClickListener(this);
        dirationTxt = (TextView) findViewById(R.id.aliyun_duration_txt);
        dirationTxt.setText((float) duration / 1000 + "");
        mCropProgressBg = (FrameLayout) findViewById(R.id.aliyun_crop_progress_bg);
        mCropProgressBg.setVisibility(View.GONE);
        mCropProgress = (FanProgressBar) findViewById(R.id.aliyun_crop_progress);
        mCropProgress.setOutRadius(DensityUtil.dip2px(this, 40) / 2 - mOutStrokeWidth / 2);
        mCropProgress.setOffset(mOutStrokeWidth / 2, mOutStrokeWidth / 2);
        mCropProgress.setOutStrokeWidth(mOutStrokeWidth);
        setListViewHeight();
    }

    private void setListViewHeight() {
        LayoutParams layoutParams = (LayoutParams) listView.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        listView.setLayoutParams(layoutParams);
        seekBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, screenWidth / 8));
    }

    public void initSurface() {
        frame = (VideoTrimFrameLayout) findViewById(R.id.aliyun_video_surfaceLayout);
        frame.setOnSizeChangedListener(this);
        frame.setOnScrollCallBack(this);
        textureview = (TextureView) findViewById(R.id.aliyun_video_textureview);
        resizeFrame();
        textureview.setSurfaceTextureListener(this);
    }

    private void resizeFrame() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frame.getLayoutParams();
        switch (ratioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * 4 / 3;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * 16 / 9;
            break;
        default:
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * 16 / 9;
            break;
        }
        frame.setLayoutParams(layoutParams);
        NativeParser parser = new NativeParser();
        parser.init(path);
        int videoWidth = Integer.parseInt(parser.getValue(NativeParser.VIDEO_WIDTH));
        int videoHeight = Integer.parseInt(parser.getValue(NativeParser.VIDEO_HEIGHT));
        parser.release();
        parser.dispose();
        frameWidth = layoutParams.width;
        frameHeight = layoutParams.height;
        if (cropMode == SCALE_CROP) {
            scaleCrop(videoWidth, videoHeight);
        } else if (cropMode == SCALE_FILL) {
            scaleFill(videoWidth, videoHeight);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mPlayer == null) {
            mSurface = new Surface(surface);
            mPlayer = AliyunSVideoPlayerCreator.createPlayer();
            mPlayer.init(this);

            mPlayer.setPlayerCallback(new PlayerCallback() {
                @Override
                public void onPlayComplete() {

                }

                @Override
                public void onDataSize(int dataWidth, int dataHeight) {
                    frameWidth = frame.getWidth();
                    frameHeight = frame.getHeight();
                    videoWidth = dataWidth;
                    videoHeight = dataHeight;
                    if (crop != null && mEndTime == 0) {
                        try {
                            mEndTime = (long) (crop.getVideoDuration(path) * 1.0f / 1000);
                        } catch (Exception e) {
                            ToastUtil.showToast(AliyunVideoCropActivity.this, R.string.alivc_crop_error);
                        }
                    }
                    if (cropMode == SCALE_CROP) {
                        scaleCrop(dataWidth, dataHeight);
                    } else if (cropMode == SCALE_FILL) {
                        scaleFill(dataWidth, dataHeight);
                    }
                    mPlayer.setDisplaySize(textureview.getLayoutParams().width, textureview.getLayoutParams().height);
                    playVideo();

                }

                @Override
                public void onError(int i) {
                    Log.e(TAG, "错误码 : " + i);
                }
            });
            mPlayer.setDisplay(mSurface);
            mPlayer.setSource(path);

        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mPlayer.setDisplaySize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            playState = END_VIDEO;
            mPlayer = null;
            mSurface = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private VideoSliceSeekBar.SeekBarChangeListener mSeekBarListener = new VideoSliceSeekBar.SeekBarChangeListener() {
        @Override
        public void seekBarValueChanged(float leftThumb, float rightThumb, int whitchSide) {
            long seekPos = 0;
            if (whitchSide == 0) {
                seekPos = (long) (duration * leftThumb / 100);
                mStartTime = seekPos;
            } else if (whitchSide == 1) {
                seekPos = (long) (duration * rightThumb / 100);
                mEndTime = seekPos;
            }
            dirationTxt.setText((float) (mEndTime - mStartTime) / 1000 + "");
            if (mPlayer != null) {
                mPlayer.seek((int) seekPos);
            }
            Log.e(TAG, "mStartTime" + mStartTime);
        }

        @Override
        public void onSeekStart() {
            pauseVideo();
        }

        @Override
        public void onSeekEnd() {
            needPlayStart = true;
            if (playState == PAUSE_VIDEO) {
                playVideo();
            }
        }
    };

    private void resetScroll() {
        mScrollX = 0;
        mScrollY = 0;
    }

    @Override
    public void onScrollDistance(Long count, int distanceX) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            playVideo();
        }
    }

    @Override
    protected void onPause() {
        if (playState == PLAY_VIDEO) {
            pauseVideo();
        }
        isPause = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (crop != null) {
            crop.dispose();
            crop = null;
        }
    }

    private void scaleFill(int videoWidth, int videoHeight) {
        LayoutParams layoutParams = (LayoutParams) textureview.getLayoutParams();
        int s = Math.min(videoWidth, videoHeight);
        int b = Math.max(videoWidth, videoHeight);
        float videoRatio = (float) b / s;
        float ratio = 1f;
        switch (ratioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            ratio = 1f;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            ratio = (float) 4 / 3;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            ratio = (float) 16 / 9;
            break;
        default:
            ratio = (float) 16 / 9;
            break;
        }
        if (videoWidth > videoHeight) {
            layoutParams.width = frameWidth;
            layoutParams.height = frameWidth * videoHeight / videoWidth;
        } else {
            if (videoRatio >= ratio) {
                layoutParams.height = frameHeight;
                layoutParams.width = frameHeight * videoWidth / videoHeight;
            } else {
                layoutParams.width = frameWidth;
                layoutParams.height = frameWidth * videoHeight / videoWidth;
            }
        }
        layoutParams.setMargins(0, 0, 0, 0);
        textureview.setLayoutParams(layoutParams);
        cropMode = SCALE_FILL;
        transFormBtn.setActivated(false);
        resetScroll();
    }

    private void scaleCrop(int videoWidth, int videoHeight) {
        LayoutParams layoutParams = (LayoutParams) textureview.getLayoutParams();
        int s = Math.min(videoWidth, videoHeight);
        int b = Math.max(videoWidth, videoHeight);
        float videoRatio = (float) b / s;
        float ratio;
        switch (ratioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            ratio = 1f;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            ratio = (float) 4 / 3;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            ratio = (float) 16 / 9;
            break;
        default:
            ratio = (float) 16 / 9;
            break;
        }
        if (ratioMode == AlivcSvideoEditParam.RATIO_MODE_ORIGINAL) {
            //原比例显示逻辑和填充模式一致
            if (videoWidth > videoHeight) {
                layoutParams.width = frameWidth;
                layoutParams.height = frameWidth * videoHeight / videoWidth;
            } else {
                if (videoRatio >= ratio) {
                    layoutParams.height = frameHeight;
                    layoutParams.width = frameHeight * videoWidth / videoHeight;
                } else {
                    layoutParams.width = frameWidth;
                    layoutParams.height = frameWidth * videoHeight / videoWidth;
                }
            }
        } else {
            if (videoWidth > videoHeight) {
                layoutParams.height = frameHeight;
                layoutParams.width = frameHeight * videoWidth / videoHeight;
            } else {
                if (videoRatio >= ratio) {
                    layoutParams.width = frameWidth;
                    layoutParams.height = frameWidth * videoHeight / videoWidth;
                } else {
                    layoutParams.height = frameHeight;
                    layoutParams.width = frameHeight * videoWidth / videoHeight;

                }
            }

        }

        layoutParams.setMargins(0, 0, 0, 0);
        textureview.setLayoutParams(layoutParams);
        cropMode = SCALE_CROP;
        transFormBtn.setActivated(true);
        resetScroll();
    }


    private void scanFile() {
        MediaScannerConnection.scanFile(getApplicationContext(),
                                        new String[] {outputPath}, new String[] {"video/mp4"}, null);
    }

    private void playVideo() {
        if (isCropping) {
            //裁剪过程中点击无效
            return;
        }
        if (mPlayer == null) {
            return;
        }
        mPlayer.seek((int) mStartTime);
        mPlayer.resume();
        playState = PLAY_VIDEO;
        long videoPos = mStartTime;
        playHandler.sendEmptyMessage(PLAY_VIDEO);
        //重新播放之后修改为false，防止暂停、播放的时候重新开始播放
        needPlayStart = false;
    }

    private void pauseVideo() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.pause();
        playState = PAUSE_VIDEO;
        playHandler.removeMessages(PLAY_VIDEO);
        seekBar.showFrameProgress(false);
        seekBar.invalidate();
    }

    private void resumeVideo() {
        if (mPlayer == null) {
            return;
        }
        if (needPlayStart) {
            playVideo();
            needPlayStart = false;
            return;
        }
        mPlayer.resume();
        playState = PLAY_VIDEO;
        playHandler.sendEmptyMessage(PLAY_VIDEO);
    }

    @Override
    public void onBackPressed() {
        if (isCropping) {
            crop.cancel();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        frameWidth = frame.getWidth();
        frameHeight = frame.getHeight();
        videoWidth = width;
        videoHeight = height;
        mStartTime = 0;
        if (crop != null) {
            try {
                mEndTime = (long) (crop.getVideoDuration(path) * 1.0f / 1000);
            } catch (Exception e) {
                ToastUtil.showToast(this, R.string.alivc_crop_error);
            }
        } else {
            mEndTime = Integer.MAX_VALUE;
        }
        if (cropMode == SCALE_CROP) {
            scaleCrop(width, height);
        } else if (cropMode == SCALE_FILL) {
            scaleFill(width, height);
        }

    }

    @Override
    public void onSizeChanged(View view, int w, int h, int oldw, int oldh) {

    }

    @Override
    public void onVideoScroll(float distanceX, float distanceY) {
        if (isCropping) {
            //裁剪中无法操作
            return;
        }
        LayoutParams lp = (LayoutParams) textureview.getLayoutParams();
        int width = lp.width;
        int height = lp.height;

        if (width > frameWidth || height > frameHeight) {
            int maxHorizontalScroll = width - frameWidth;
            int maxVerticalScroll = height - frameHeight;
            if (maxHorizontalScroll > 0) {
                maxHorizontalScroll = maxHorizontalScroll / 2;
                mScrollX += distanceX;
                if (mScrollX > maxHorizontalScroll) {
                    mScrollX = maxHorizontalScroll;
                }
                if (mScrollX < -maxHorizontalScroll) {
                    mScrollX = -maxHorizontalScroll;
                }
            }
            if (maxVerticalScroll > 0) {
                maxVerticalScroll = maxVerticalScroll / 2;
                mScrollY += distanceY;
                if (mScrollY > maxVerticalScroll) {
                    mScrollY = maxVerticalScroll;
                }
                if (mScrollY < -maxVerticalScroll) {
                    mScrollY = -maxVerticalScroll;
                }
            }
            lp.setMargins(0, 0, mScrollX, mScrollY);
        }

        textureview.setLayoutParams(lp);
    }

    @Override
    public void onVideoSingleTapUp() {
        if (isCropping) {
            //裁剪过程中点击无效
            return;
        }
        if (playState == END_VIDEO) {
            playVideo();
        } else if (playState == PLAY_VIDEO) {
            pauseVideo();
        } else if (playState == PAUSE_VIDEO) {
            resumeVideo();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == transFormBtn) {
            if (isCropping) {
                return;
            }
            mAction = CropKey.ACTION_TRANSCODE;
            if (cropMode == SCALE_FILL) {
                scaleCrop(videoWidth, videoHeight);
            } else if (cropMode == SCALE_CROP) {
                scaleFill(videoWidth, videoHeight);
            }
        } else if (v == nextBtn) {
            switch (mAction) {
            case CropKey.ACTION_TRANSCODE:
                startCrop();
                break;
            case CropKey.ACTION_SELECT_TIME:
                Intent intent = getIntent();
                intent.putExtra(CropKey.RESULT_KEY_CROP_PATH, path);
                intent.putExtra(CropKey.RESULT_KEY_DURATION, mEndTime - mStartTime);
                intent.putExtra(CropKey.RESULT_KEY_START_TIME, mStartTime);
                //裁剪之后的跳转
                String tagClassName = AliyunSvideoActionConfig.getInstance().getAction().getTagClassName(ActionInfo.SVideoAction.CROP_TARGET_CLASSNAME);
                if (tagClassName == null) {
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    intent.setClassName(this, tagClassName);
                    startActivity(intent);
                }
                break;
            default:
                break;
            }
        } else if (v == cancelBtn) {
            onBackPressed();
        }
    }

    private void startCrop() {
        if (frameWidth == 0 || frameHeight == 0) {
            ToastUtil.showToast(this, R.string.alivc_crop_error);
            isCropping = false;
            return;
        }
        if (isCropping) {
            return;
        }
        //开始裁剪时，暂停视频的播放,提高裁剪效率
        pauseVideo();
        final LayoutParams lp = (LayoutParams) textureview.getLayoutParams();
        int posX;
        int posY;
        int outputWidth = 0;
        int outputHeight = 0;
        int cropWidth;
        int cropHeight;
        outputPath = OUTPUT_PATH_DIR + DateTimeUtils.getDateTimeFromMillisecond(System.currentTimeMillis()) + OUTPUT_PATH_SUFFIX;
        float videoRatio = (float) videoHeight / videoWidth;
        float outputRatio = 1f;
        switch (ratioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            outputRatio = 1f;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            outputRatio = (float) 4 / 3;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            outputRatio = (float) 16 / 9;
            break;
        case RATIO_ORIGINAL:
            outputRatio = videoRatio;
            break;
        default:
            outputRatio = (float) 16 / 9;
            break;
        }
        if (videoRatio > outputRatio) {
            posX = 0;
            posY = ((lp.height - frameHeight) / 2 + mScrollY) * videoWidth / frameWidth;
            while (posY % 4 != 0) {
                posY++;
            }
            switch (resolutionMode) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                outputWidth = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                outputWidth = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                outputWidth = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                outputWidth = 720;
                break;
            default:
                outputWidth = 720;
                break;
            }
            cropWidth = videoWidth;
            cropHeight = 0;
            switch (ratioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                cropHeight = videoWidth;
                outputHeight = outputWidth;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                cropHeight = videoWidth * 4 / 3;
                outputHeight = outputWidth * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                cropHeight = videoWidth * 16 / 9;
                outputHeight = outputWidth * 16 / 9;
                break;
            default:
                break;
            }
        } else if (videoRatio > outputRatio) {

            posX = ((lp.width - frameWidth) / 2 + mScrollX) * videoHeight / frameHeight;
            posY = 0;
            while (posX % 4 != 0) {
                posX++;
            }
            switch (resolutionMode) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                outputWidth = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                outputWidth = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                outputWidth = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                outputWidth = 720;
                break;
            default:
                outputWidth = 720;
                break;
            }
            cropHeight = videoHeight;
            switch (ratioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                cropWidth = videoHeight;
                outputHeight = outputWidth;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                cropWidth = videoHeight * 3 / 4;
                outputHeight = outputWidth * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                cropWidth = videoHeight * 9 / 16;
                outputHeight = outputWidth * 16 / 9;
                break;
            case RATIO_ORIGINAL:
                cropWidth = (int)(videoHeight / videoRatio);
                outputHeight = (int)(outputWidth * videoRatio);
                break;
            default:
                cropWidth = videoHeight * 9 / 16;
                outputHeight = outputWidth * 16 / 9;
                break;
            }
        } else {
            // 原比例或videoRatio = outputRatio执行else

            posX = 0;
            posY = 0;

            switch (resolutionMode) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                outputWidth = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                outputWidth = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                outputWidth = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                outputWidth = 720;
                break;
            default:
                outputWidth = 720;
                break;
            }
            cropHeight = videoHeight;
            switch (ratioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                cropWidth = videoHeight;
                outputHeight = outputWidth;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                cropWidth = videoHeight * 3 / 4;
                outputHeight = outputWidth * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                cropWidth = videoHeight * 9 / 16;
                outputHeight = outputWidth * 16 / 9;
                break;
            case RATIO_ORIGINAL:
                cropWidth = (int)(videoHeight / videoRatio);
                outputHeight = (int)(outputWidth * videoRatio);
                break;
            default:
                cropWidth = videoHeight * 9 / 16;
                outputHeight = outputWidth * 16 / 9;
                break;
            }
        }
        CropParam cropParam = new CropParam();
        cropParam.setOutputPath(outputPath);
        cropParam.setInputPath(path);
        cropParam.setOutputWidth(outputWidth);
        cropParam.setOutputHeight(outputHeight);
        Rect cropRect = new Rect(posX, posY, posX + cropWidth, posY + cropHeight);
        cropParam.setCropRect(cropRect);
        cropParam.setStartTime(mStartTime * 1000);
        cropParam.setEndTime(mEndTime * 1000);
        cropParam.setScaleMode(cropMode);
        cropParam.setFrameRate(frameRate);
        cropParam.setGop(gop);
        cropParam.setVideoBitrate(mBitrate);
        cropParam.setQuality(quality);
        cropParam.setVideoCodec(mVideoCodec);
        cropParam.setFillColor(Color.BLACK);
        cropParam.setCrf(0);
//        cropParam.setCrf(27);
//        if ((mEndTime - mStartTime) /  1000 / 60 >= 5) {
//            ToastUtil.showToast(this, R.string.aliyun_video_duration_5min_tip);
//            isCropping = false;
//            return;
//        }
        mCropProgressBg.setVisibility(View.VISIBLE);
        cropParam.setUseGPU(isUseGPU);
        crop.setCropParam(cropParam);
//        progressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.wait));
//        progressDialog.setCancelable(true);
//        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                crop.cancelCompose();
//                deleteFile();
//                setResult(Activity.RESULT_CANCELED, getIntent());
//            }
//        });


        int ret = crop.startCrop();
        if (ret < 0) {
            ToastUtil.showToast(this, getString(R.string.alivc_crop_error) + "错误码 ：" + ret);
            return;
        }
        startCropTimestamp = System.currentTimeMillis();
        Log.d("CROP_COST", "start : " + startCropTimestamp);
        isCropping = true;
        seekBar.setSliceBlocked(true);


    }

    long startCropTimestamp;

    private void deleteFile() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                FileUtils.deleteFile(outputPath);
                return null;
            }
        } .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onProgress(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCropProgress.setProgress(percent);
            }
        });
    }

    @Override
    public void onError(final int code) {
        Log.d(TAG, "crop failed : " + code);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
                switch (code) {
                case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                    ToastUtil.showToast(AliyunVideoCropActivity.this, R.string.alivc_crop_error);
                    break;
                case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                    ToastUtil.showToast(AliyunVideoCropActivity.this, R.string.alivc_not_supported_audio);
                    break;
                default:
                    break;
                }
//                progressDialog.dismiss();
                setResult(Activity.RESULT_CANCELED, getIntent());
            }
        });
        isCropping = false;

    }

    @Override
    public void onComplete(long duration) {
        long time = System.currentTimeMillis();
        Log.d(TAG, "completed : " + (time - startCropTimestamp));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCropProgress.setVisibility(View.GONE);
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
                scanFile();
                Intent intent = getIntent();
                intent.putExtra(CropKey.RESULT_KEY_CROP_PATH, outputPath);
                intent.putExtra(CropKey.RESULT_KEY_DURATION, mEndTime - mStartTime);
                intent.putExtra(CropKey.RESULT_KEY_FILE_PATH, path);
                //裁剪之后的跳转
                String tagClassName = AliyunSvideoActionConfig.getInstance().getAction().getTagClassName(ActionInfo.SVideoAction.CROP_TARGET_CLASSNAME);
                if (tagClassName == null) {
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    intent.setClassName(AliyunVideoCropActivity.this, tagClassName);
                    startActivity(intent);
                }
//                progressDialog.dismiss();
            }
        });
        isCropping = false;
    }

    @Override
    public void onCancelComplete() {
        //取消完成
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
            }
        });
        deleteFile();
        setResult(Activity.RESULT_CANCELED);
        finish();
        isCropping = false;
    }

    private final String TAG = "AliyunVideoCropActivity";

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case PAUSE_VIDEO:
            pauseVideo();
            break;
        case PLAY_VIDEO:
            if (mPlayer != null) {
                long currentPlayPos = mPlayer.getCurrentPosition() / 1000;
                Log.d(TAG, "currentPlayPos:" + currentPlayPos);
                if (currentPlayPos < mEndTime) {
                    seekBar.showFrameProgress(true);
                    seekBar.setFrameProgress(currentPlayPos / (float) duration);
                    playHandler.sendEmptyMessageDelayed(PLAY_VIDEO, 100);
                } else {
                    playVideo();
                }
            }
            break;
        default:
            break;
        }
        return false;
    }

    /**
     * 调转到该activity方法
     *
     * @param context
     * @param svideoParam 配置参数，需要包含需要剪切的MediaInfo信息
     * @param requestCode
     */
    public static void startCropForResult(Activity context, AlivcSvideoEditParam svideoParam, int requestCode) {
        MediaInfo mediaInfo = svideoParam.getMediaInfo();
        if (mediaInfo == null) {
            return;
        }
        Intent intent = new Intent(context, AliyunVideoCropActivity.class);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_PATH, mediaInfo.filePath);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_DURATION, mediaInfo.duration);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RATIO, svideoParam.getRatio());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CROP_MODE, svideoParam.getCropMode());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_QUALITY, svideoParam.getVideoQuality());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_GOP, svideoParam.getGop());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_BITRATE, svideoParam.getBitrate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_FRAMERATE, svideoParam.getFrameRate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RESOLUTION, svideoParam.getResolutionMode());
        intent.putExtra(AlivcSvideoEditParam.CROP_ACTION, svideoParam.getCropAction());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, svideoParam.getVideoCodec());
        context.startActivityForResult(intent, requestCode);
    }

    public static void startCropForResult(Activity activity, int requestCode, AliyunSnapVideoParam param) {
        Intent intent = new Intent(activity, MediaActivity.class);
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, param.getResolutionMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO, param.getRatioMode());
        intent.putExtra(AliyunSnapVideoParam.NEED_RECORD, param.isNeedRecord());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY, param.getVideoQuality());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, param.getVideoCodec());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP, param.getGop());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_BITRATE, param.getVideoBitrate());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, param.getFrameRate());
        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
        intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION, param.getMinCropDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION, param.getMinVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION, param.getMaxVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.RECORD_MODE, param.getRecordMode());
        intent.putExtra(AliyunSnapVideoParam.FILTER_LIST, param.getFilterList());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_LEVEL, param.getBeautyLevel());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_STATUS, param.getBeautyStatus());
        intent.putExtra(AliyunSnapVideoParam.CAMERA_TYPE, param.getCameraType());
        intent.putExtra(AliyunSnapVideoParam.FLASH_TYPE, param.getFlashType());
        intent.putExtra(AliyunSnapVideoParam.NEED_CLIP, param.isNeedClip());
        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION, param.getMaxDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION, param.getMinDuration());
        intent.putExtra(AliyunSnapVideoParam.CROP_USE_GPU, param.isCropUseGPU());
        activity.startActivityForResult(intent, requestCode);
    }


    public int getVideoWidth() {
        int width = 0;
        switch (resolutionMode) {
        case AliyunSnapVideoParam.RESOLUTION_360P:
            width = 360;
            break;
        case AliyunSnapVideoParam.RESOLUTION_480P:
            width = 480;
            break;
        case AliyunSnapVideoParam.RESOLUTION_540P:
            width = 540;
            break;
        case AliyunSnapVideoParam.RESOLUTION_720P:
            width = 720;
            break;
        default:
            width = 540;
            break;
        }
        return width;
    }


    public int getVideoHeight(MediaInfo mediaInfo) {
        int height = 0;
        int width = getVideoWidth();
        switch (ratioMode) {
        case AliyunSnapVideoParam.RATIO_MODE_1_1:
            height = width;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_3_4:
            height = width * 4 / 3;
            break;
        case AliyunSnapVideoParam.RATIO_MODE_9_16:
            height = width * 16 / 9;
            break;
        case RATIO_ORIGINAL:
            if (mediaInfo != null) {
                height = (int)(width / getMediaRatio(mediaInfo));
            } else {
                height = width * 16 / 9;
            }
            break;
        default:
            height = width * 16 / 9;
            break;
        }
        return height;
    }

    private float getMediaRatio(MediaInfo mediaInfo) {
        float videoWidth = 9;
        float videoHeight = 16;
        int videoRotation = 0;
        if (mediaInfo.mimeType.startsWith("video") || mediaInfo.filePath.endsWith("gif") || mediaInfo.filePath.endsWith("GIF")) {
            NativeParser parser = new NativeParser();
            parser.init(mediaInfo.filePath);
            try {
                videoWidth = Float.parseFloat(parser.getValue(NativeParser.VIDEO_WIDTH));
                videoHeight = Integer.parseInt(parser.getValue(NativeParser.VIDEO_HEIGHT));
                videoRotation = Integer.parseInt(parser.getValue(NativeParser.VIDEO_ROTATION));
            } catch (Exception e) {
                Log.e(AliyunTag.TAG, "parse rotation failed");
            }

        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mediaInfo.filePath, options);
            videoWidth = options.outWidth;
            videoHeight = options.outHeight;
        }

        float ratio = videoWidth / videoHeight;

        return videoRotation == 90 || videoRotation == 270 ? 1 / ratio : ratio;
    }
}
