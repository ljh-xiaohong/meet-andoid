package com.yuejian.meet.activities.creation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.common.utils.DensityUtil;
import com.aliyun.common.utils.FileUtils;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.crop.AliyunCropCreator;
import com.aliyun.crop.struct.CropParam;
import com.aliyun.crop.supply.AliyunICrop;
import com.aliyun.crop.supply.CropCallback;
import com.aliyun.demo.crop.media.VideoTrimAdapterNew;
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
import com.aliyun.svideo.sdk.external.thumbnail.AliyunIThumbnailFetcher;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunThumbnailFetcherFactory;
import com.aliyun.video.common.utils.DateTimeUtils;
import com.aliyun.video.common.utils.ScreenUtils;
import com.aliyun.video.common.utils.ThreadUtils;
import com.duanqu.transcode.NativeParser;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.common.SDCardConstants;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoCropActivity extends AppCompatActivity
        implements View.OnClickListener, CropCallback, TextureView.SurfaceTextureListener,
        SizeChangedNotifier.Listener, VideoTrimFrameLayout.OnVideoScrollCallBack
        , VideoSliceSeekBar.SeekBarChangeListener, HorizontalListView.OnScrollCallBack
        , Handler.Callback {

    public static final VideoDisplayMode SCALE_CROP = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_FILL = VideoDisplayMode.FILL;
    public static final String TAG = VideoCropActivity.class.getSimpleName();

    private static final int PLAY_VIDEO = 1000;
    private static final int PAUSE_VIDEO = 1001;
    private static final int END_VIDEO = 1003;

    private Context mContext;

    private String outputPath;

    //获取屏幕宽度
    private int screenWidth;
    //帧数宽高
    private int frameWidth;
    private int frameHeight;
    //视频宽高
    private int videoWidth;
    private int videoHeight;

    private int mScrollX;
    private int mScrollY;

    private boolean isPause = false;
    private boolean isCropping = false;

    private int playState = END_VIDEO;

    //aliyun裁剪类
    private AliyunICrop crop;

    //获取视频帧数
    private AliyunIThumbnailFetcher mThumbnailFetcher;

    private int mAction = CropKey.ACTION_TRANSCODE;

    private String path;

    private long duration;

    private int resolutionMode;


    private VideoDisplayMode cropMode = VideoDisplayMode.SCALE;

    private VideoQuality quality = VideoQuality.HD;

    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;

    private VideoTrimAdapterNew adapter;

    private Handler playHandler = new Handler(this);

    private WeakReference<VideoCropActivity> reference;

    ////////////////////// 控件start ////////////////////////////////
    /**
     * 帧数拖动器
     */
    @Bind(R.id.aliyun_seek_bar)
    VideoSliceSeekBar seekBar;
    /**
     * 帧数显示器
     */
    @Bind(R.id.aliyun_video_tailor_image_list)
    HorizontalListView listView;

    @Bind(R.id.aliyun_crop_progress)
    FanProgressBar mCropProgress;

    @Bind(R.id.aliyun_crop_progress_bg)
    FrameLayout mCropProgressBg;
    /**
     *
     */
    @Bind(R.id.aliyun_video_surfaceLayout)
    VideoTrimFrameLayout frame;

    /**
     * 播放view
     */
    @Bind(R.id.aliyun_video_textureview)
    TextureView textureview;

    /**
     * 下一步
     */
    @Bind(R.id.aliyun_next)
    TextView nextBtn;

    /**
     * 取消编辑
     */
    @Bind(R.id.aliyun_back)
    ImageView cancelBtn;

    /**
     * 裁剪时段
     */
    @Bind(R.id.aliyun_duration_txt)
    TextView dirationTxt;

    private Surface mSurface;

    @Bind(R.id.aliyun_video_tailor_image_list_layout)
    RelativeLayout listViewLayout;

    ////////////////////// 控件end ////////////////////////////////

    private int frameRate;
    private int gop;
    private int mBitrate;

    private int ratioMode;
    private int cropDuration = 2000;
    private boolean isUseGPU = false;

    private long mStartTime;
    private long mEndTime;
    /**
     * 原比例
     */
    private static final int RATIO_ORIGINAL = 3;

    /**
     * 每次修改裁剪结束位置时需要重新播放视频
     */
    private boolean needPlayStart = false;

    /**
     * sdk提供的播放器，支持非关键帧的实时预览
     */
    private AliyunISVideoPlayer mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        reference = new WeakReference<>(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        crop = AliyunCropCreator.createCropInstance(this);
        crop.setCropCallback(this);
        getData();
        initViews();
        initSurface();
    }

    private boolean checkIsLife() {
        return reference == null || reference.get() == null || reference.get().isFinishing();
    }


    /**
     * 获取所有数据
     */
    private void getData() {
        mAction = getIntent().getIntExtra(CropKey.ACTION, CropKey.ACTION_TRANSCODE);
        path = getIntent().getStringExtra(CropKey.VIDEO_PATH);
        try {
            duration = crop.getVideoDuration(path) / 1000;
        } catch (Exception e) {
            ToastUtil.showToast(this, com.aliyun.demo.crop.R.string.alivc_crop_error);
        }//获取精确的视频时间
        resolutionMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, AliyunSnapVideoParam.RESOLUTION_720P);
        cropMode = (VideoDisplayMode) getIntent().getSerializableExtra(AliyunSnapVideoParam.CROP_MODE);
        if (cropMode == null) {
            cropMode = VideoDisplayMode.FILL;
        }
        quality = (VideoQuality) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_QUALITY);
        if (quality == null) {
            quality = VideoQuality.SSD;
        }
        gop = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_GOP, 5);
        mBitrate = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_BITRATE, 0);
        frameRate = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, 30);
        ratioMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RATIO, AliyunSnapVideoParam.RATIO_MODE_9_16);
        cropDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MIN_CROP_DURATION, 2000);
        isUseGPU = getIntent().getBooleanExtra(AliyunSnapVideoParam.CROP_USE_GPU, false);
        mVideoCodec = (VideoCodecs) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_CODEC);
    }

    private void initViews() {
        int mOutStrokeWidth = DensityUtil.dip2px(this, 5);
        seekBar.setSeekBarChangeListener(mSeekBarListener);
        int minDiff = (int) (cropDuration / (float) duration * 100) + 1;
        seekBar.setProgressMinDiff(minDiff > 100 ? 100 : minDiff);
        listView.setOnScrollCallBack(this);
        adapter = new VideoTrimAdapterNew(this, new ArrayList<SoftReference<Bitmap>>());
        listView.setAdapter(adapter);
//        nextBtn.setOnClickListener(this);
//        cancelBtn.setOnClickListener(this);
        dirationTxt.setText((float) duration / 1000 + "");
        mCropProgressBg.setVisibility(View.GONE);
        mCropProgress.setOutRadius(DensityUtil.dip2px(this, 40) / 2 - mOutStrokeWidth / 2);
        mCropProgress.setOffset(mOutStrokeWidth / 2, mOutStrokeWidth / 2);
        mCropProgress.setOutStrokeWidth(mOutStrokeWidth);
        setListViewHeight();
        requestThumbItemTime();
    }

    public void initSurface() {
        frame.setOnSizeChangedListener(this);
        frame.setOnScrollCallBack(this);
        resizeFrame();
        textureview.setSurfaceTextureListener(this);
    }


    /**
     * 设置帧数显示器高度 及 帧数拖动器宽高
     */
    private void setListViewHeight() {
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
//        layoutParams.height = screenWidth / 8;
//        listView.setLayoutParams(layoutParams);
//        seekBar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, screenWidth / 8));

        final ViewGroup.LayoutParams lp = seekBar.getLayoutParams();
        lp.height = BitmapFactory.decodeResource(getResources(), R.mipmap.creation_progress_left).getHeight();

        listView.getLayoutParams().height = lp.height;

        listView.post(new Runnable() {
            @Override
            public void run() {
                int itemPadding = (listView.getLayoutParams().height - listView.getItemContentHight()) / 2;
                listView.setPadding(2, itemPadding, 2, 0);

            }
        });


    }

    /**
     * 设置播放控件的宽高
     */
    private void resizeFrame() {

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) frame.getLayoutParams();
        int layoutWidth = (int) (screenWidth * 0.656);
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
//        if (cropMode == SCALE_CROP) {
//            scaleCrop(videoWidth, videoHeight);
//        } else if (cropMode == SCALE_FILL) {
//            scaleFill(videoWidth, videoHeight);
//        }
        setFrameSize(videoWidth, videoHeight);
    }

    /**
     * 设置播放layout的宽高
     *
     * @return
     */
    private void setFrameSize(int videoWidth, int videoHeight) {
        Log.e("adad", "videoWidth:" + videoWidth + ",videoHeight:" + videoHeight);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textureview.getLayoutParams();
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
        //rate = 0.656
        float rate = 1f;

//        if (videoWidth > videoHeight) {
//            //横视频
//            layoutParams.height = frameHeight;
//            layoutParams.width = frameHeight * videoWidth / videoHeight;
//        } else {
//            //竖视频
//            if (videoRatio >= ratio) {
//                layoutParams.width = frameWidth;
//                layoutParams.height = frameWidth * videoHeight / videoWidth;
//            } else {
//                layoutParams.height = frameHeight;
//                layoutParams.width = frameHeight * videoWidth / videoHeight;
//
//            }
//        }

        if (videoWidth > videoHeight) {
            layoutParams.width = frameWidth;
            layoutParams.height = frameWidth * videoHeight / videoWidth;
        } else {
            if (videoRatio >= ratio) {
                layoutParams.height = (int) (ScreenUtils.getHeight(mContext) * rate);
                layoutParams.width = (int) (ScreenUtils.getHeight(mContext) * rate) * videoWidth / videoHeight;
            } else {
                layoutParams.width = frameWidth;
                layoutParams.height = frameWidth * videoHeight / videoWidth;
            }
        }
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.gravity = Gravity.CENTER;
        textureview.setLayoutParams(layoutParams);
        resetScroll();
    }

    private void resetScroll() {
        mScrollX = 0;
        mScrollY = 0;
    }

    /**
     * 停止播放
     */
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

    /**
     * 开始播放
     */
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


    /**
     * 再次开始
     */
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

    private void scanFile() {
        MediaScannerConnection.scanFile(getApplicationContext(),
                new String[]{outputPath}, new String[]{"video/mp4"}, null);
    }

    /**
     * 删除文件
     */
    private void deleteFile() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                FileUtils.deleteFile(outputPath);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 开始录制
     */
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
        final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) textureview.getLayoutParams();
        int posX;
        int posY;
        int outputWidth = 0;
        int outputHeight = 0;
        int cropWidth;
        int cropHeight;
        outputPath = SDCardConstants.OUTPUT_PATH_DIR + DateTimeUtils.getDateTimeFromMillisecond(System.currentTimeMillis()) + SDCardConstants.CROP_SUFFIX;
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
        } else if (videoRatio < outputRatio) {

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
                    cropWidth = (int) (videoHeight / videoRatio);
                    outputHeight = (int) (outputWidth * videoRatio);
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
                    cropWidth = (int) (videoHeight / videoRatio);
                    outputHeight = (int) (outputWidth * videoRatio);
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
            ToastUtil.showToast(this, getString(com.aliyun.demo.crop.R.string.alivc_crop_error) + "错误码 ：" + ret);
            return;
        }
        startCropTimestamp = System.currentTimeMillis();
        Log.d("CROP_COST", "start : " + startCropTimestamp);
        isCropping = true;
        seekBar.setSliceBlocked(true);


    }

///////////////////////////////////////////////// 回调 start /////////////////////////////////////////////////////

    private VideoSliceSeekBar.SeekBarChangeListener mSeekBarListener = new VideoSliceSeekBar.SeekBarChangeListener() {
        @Override
        public void seekBarValueChanged(float leftThumb, float rightThumb, int whitchSide) {
            if(checkIsLife())return;
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
            if(checkIsLife())return;
            pauseVideo();
        }

        @Override
        public void onSeekEnd() {
            if(checkIsLife())return;
            needPlayStart = true;
            if (playState == PAUSE_VIDEO) {
                playVideo();
            }
        }
    };


    /**
     * 裁剪时返回进度，在子线程
     */

    @Override
    public void onProgress(final int percent) {
        //todo 进度演示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(checkIsLife())return;
                mCropProgress.setProgress(percent);
            }
        });
    }

    /**
     * 合成时错误提示，在子线程
     */

    @Override
    public void onError(final int code) {
        //TODO 错误提示
        // AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO  剪裁失败
        // AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO 音频格式不支持
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(checkIsLife())return;
                nextBtn.setClickable(true);
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
                switch (code) {
                    case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                        ToastUtil.showToast(mContext, com.aliyun.demo.crop.R.string.alivc_crop_error);
                        break;
                    case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                        ToastUtil.showToast(mContext, com.aliyun.demo.crop.R.string.alivc_not_supported_audio);
                        break;
                    default:
                        ToastUtil.showToast(mContext, com.aliyun.demo.crop.R.string.alivc_crop_error);
                        break;
                }
//                progressDialog.dismiss();
//                setResult(Activity.RESULT_CANCELED, getIntent());
            }
        });
        isCropping = false;

    }

    long startCropTimestamp;

    /**
     * 合成完成提示，在子线程
     */
    @Override
    public void onComplete(long l) {
        long time = System.currentTimeMillis();
        Log.d(TAG, "completed : " + (time - startCropTimestamp));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(checkIsLife())return;
                nextBtn.setClickable(true);
                mCropProgress.setVisibility(View.GONE);
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
                scanFile();
                Intent intent = getIntent();
                intent.putExtra(CropKey.RESULT_KEY_CROP_PATH, outputPath);
                intent.putExtra(CropKey.RESULT_KEY_DURATION, mEndTime - mStartTime);
                intent.putExtra(CropKey.RESULT_KEY_FILE_PATH, path);
                //裁剪之后的跳转
                PulishActivity.startPulishActivity(mContext, outputPath, mEndTime - mStartTime, path, getIntent().getStringExtra("project_json_path"));
//                finish();

//                String tagClassName = AliyunSvideoActionConfig.getInstance().getAction().getTagClassName(ActionInfo.SVideoAction.CROP_TARGET_CLASSNAME);
//                if (tagClassName == null) {
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
//                } else {
//                    intent.setClassName(mContext, tagClassName);
//                    startActivity(intent);
//                }
//                progressDialog.dismiss();
            }
        });
        isCropping = false;
    }

    /**
     * 合成取消完成时提示，子线程
     */
    @Override
    public void onCancelComplete() {
        if(checkIsLife())return;
        //取消完成
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(checkIsLife())return;
                nextBtn.setClickable(true);
                mCropProgressBg.setVisibility(View.GONE);
                seekBar.setSliceBlocked(false);
            }
        });
        deleteFile();
        setResult(Activity.RESULT_CANCELED);
        finish();
        isCropping = false;
    }


    @Override
    public void onSizeChanged(View view, int w, int h, int oldw, int oldh) {

    }

    @Override
    public void onVideoScroll(float distanceX, float distanceY) {
        if(checkIsLife())return;
        if (isCropping) {
            //裁剪中无法操作
            return;
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) textureview.getLayoutParams();
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
        if(checkIsLife())return;
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
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int i, int i1) {
        if(checkIsLife())return;
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
                    if(checkIsLife())return;
                    frameWidth = frame.getWidth();
                    frameHeight = frame.getHeight();
                    videoWidth = dataWidth;
                    videoHeight = dataHeight;
                    if (crop != null && mEndTime == 0) {
                        try {
                            mEndTime = (long) (crop.getVideoDuration(path) * 1.0f / 1000);
                        } catch (Exception e) {
                            ToastUtil.showToast(mContext, R.string.alivc_video_error);
                        }
                    }
//                        setFrameSize(dataWidth,dataHeight);
//                    if (cropMode == SCALE_CROP) {
//                        scaleCrop(dataWidth, dataHeight);
//                    } else if (cropMode == SCALE_FILL) {
//                        scaleFill(dataWidth, dataHeight);
//                    }

                    setFrameSize(dataWidth, dataHeight);
                    mPlayer.setDisplaySize(textureview.getLayoutParams().width, textureview.getLayoutParams().height);
                    playVideo();

                }

                @Override
                public void onError(int i) {
                    if(checkIsLife())return;
                    Log.e(TAG, "错误码 : " + i);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mContext, getString(com.aliyun.demo.crop.R.string.alivc_video_error));
                        }
                    });

                }
            });
            mPlayer.setDisplay(mSurface);
            mPlayer.setSource(path);

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        if(checkIsLife())return;
        mPlayer.setDisplaySize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            playState = END_VIDEO;
            mSurface.release();
            mPlayer = null;
            mSurface = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @OnClick({R.id.aliyun_next, R.id.aliyun_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aliyun_next:
                nextBtn.setClickable(false);
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
                            finish();
                        }
                        break;
                    default:
                        break;
                }
                break;

            case R.id.aliyun_back:
                finish();
                break;

        }
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
        ButterKnife.unbind(this);
        if (crop != null) {
            crop.dispose();
            crop = null;
        }
        if (mThumbnailFetcher != null) {
            mThumbnailFetcher.release();
            mThumbnailFetcher = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void seekBarValueChanged(float leftThumb, float rightThumb, int whitchSide) {

    }

    @Override
    public void onSeekStart() {

    }

    @Override
    public void onSeekEnd() {

    }

    @Override
    public void onScrollDistance(Long count, int distanceX) {

    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
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

    @Override
    public void onBackPressed() {
        if (isCropping) {
            crop.cancel();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {

        if (isCropping) {
            crop.cancel();
            crop.dispose();
            crop = null;
        }
        super.finish();
    }


    ///////////////////////////////////////////////// 回调 end /////////////////////////////////////////////////////

    /**
     * 获取每个item取帧的时间值
     **/
    private void requestThumbItemTime() {
        int itemWidth = screenWidth / 10;

        mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        mThumbnailFetcher.addVideoSource(path, 0, Integer.MAX_VALUE, 0);
        mThumbnailFetcher.setParameters(itemWidth, itemWidth, AliyunIThumbnailFetcher.CropMode.Mediate, VideoDisplayMode.FILL, 10);

        long duration = mThumbnailFetcher.getTotalDuration();
        long itemTime = duration / 10;
        for (int i = 1; i <= 10; i++) {
            requestFetchThumbnail(itemTime, i, 10);
        }

    }

    /**
     * 获取缩略图
     *
     * @param interval 取帧平均间隔
     * @param position 第几张
     * @param count    总共的张数
     */
    private void requestFetchThumbnail(final long interval, final int position, final int count) {
        long[] times = {(position - 1) * interval + interval / 2};

//        Log.d(TAG, "requestThumbnailImage() times :" + times[0] + " ,position = " + position);
        mThumbnailFetcher.requestThumbnailImage(times, new AliyunIThumbnailFetcher.OnThumbnailCompletion() {

            private int vecIndex = 1;

            @Override
            public void onThumbnailReady(Bitmap frameBitmap, long l) {
                if(checkIsLife())return;
                if (frameBitmap != null && !frameBitmap.isRecycled()) {
//                    Log.i(TAG, "onThumbnailReady  put: " + position + " ,l = " + l / 1000);

                    SoftReference<Bitmap> bitmapSoftReference = new SoftReference<Bitmap>(frameBitmap);

                    adapter.add(bitmapSoftReference);
                } else {
                    if (position == 0) {
                        vecIndex = 1;
                    } else if (position == count + 1) {
                        vecIndex = -1;
                    }
                    int np = position + vecIndex;
//                    Log.i(TAG, "requestThumbnailImage  failure: thisPosition = " + position + "newPosition = " + np);
                    requestFetchThumbnail(interval, np, count);
                }
            }

            @Override
            public void onError(int errorCode) {
//                Log.w(TAG, "requestThumbnailImage error msg: " + errorCode);
            }
        });
    }


    /**
     * 调转到该activity方法
     *
     * @param context
     * @param svideoParam 配置参数，需要包含需要剪切的MediaInfo信息
     */
    public static void startCropForResult(Activity context, AlivcSvideoEditParam svideoParam, String projectJsonPath) {
        MediaInfo mediaInfo = svideoParam.getMediaInfo();
        if (mediaInfo == null) {
            return;
        }
        Intent intent = new Intent(context, VideoCropActivity.class);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_PATH, mediaInfo.filePath);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_DURATION, mediaInfo.duration);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RATIO, svideoParam.getRatio());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CROP_MODE, svideoParam.getCropMode());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_QUALITY, svideoParam.getVideoQuality());
        //导入时的裁剪默认参数为：分辨率：小于1080, 1920，gop：5，fps：30，type： ffmpeg。
        intent.putExtra(AlivcSvideoEditParam.VIDEO_GOP, 5);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_FRAMERATE, 30);
        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, VideoCodecs.H264_SOFT_FFMPEG);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_BITRATE, svideoParam.getBitrate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RESOLUTION, svideoParam.getResolutionMode());
        intent.putExtra(AlivcSvideoEditParam.CROP_ACTION, svideoParam.getCropAction());
        intent.putExtra("project_json_path", projectJsonPath);
        context.startActivity(intent);
    }
}
