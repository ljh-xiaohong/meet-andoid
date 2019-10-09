package com.yuejian.meet.activities.creation;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.CommonUtil;
import com.aliyun.common.utils.MySystemParams;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.demo.recorder.util.Common;
import com.aliyun.demo.recorder.util.FixedToastUtils;
import com.aliyun.demo.recorder.util.OrientationDetector;
import com.aliyun.demo.recorder.util.SharedPreferenceUtils;
import com.aliyun.demo.recorder.view.BaseScrollPickerView;
import com.aliyun.demo.recorder.view.StringScrollPicker;
import com.aliyun.demo.recorder.view.dialog.DialogVisibleListener;
import com.aliyun.demo.recorder.view.dialog.FilterEffectChooser;
import com.aliyun.demo.recorder.view.dialog.GIfEffectChooser;
import com.aliyun.demo.recorder.view.effects.filter.EffectInfo;
import com.aliyun.demo.recorder.view.effects.filter.interfaces.OnFilterItemClickListener;
import com.aliyun.demo.recorder.view.effects.paster.PasterSelectListener;
import com.aliyun.downloader.zipprocessor.DownloadFileUtils;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.svideo.base.ActionInfo;
import com.aliyun.svideo.base.AlivcSvideoEditParam;
import com.aliyun.svideo.base.AliyunSvideoActionConfig;
import com.aliyun.svideo.base.UIConfigManager;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyFaceItemSeletedListener;
import com.aliyun.svideo.editor.MediaActivity;
import com.aliyun.svideo.sdk.external.struct.common.AliyunDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBean;
import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;
import com.aliyun.svideo.sdk.external.struct.effect.EffectPaster;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.form.PreviewPasterForm;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraParam;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
import com.aliyun.svideo.sdk.external.struct.recorder.MediaInfo;
import com.aliyun.video.common.utils.PermissionUtils;
import com.aliyun.video.common.utils.ScreenUtils;
import com.aliyun.video.common.utils.ThreadUtils;
import com.qu.preview.callback.OnTextureIdCallBack;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.common.SDCardConstants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.dialogs.TipsDialog;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.aliyun.CircleProgressBar;
import com.yuejian.meet.widgets.aliyun.CountDownTextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoLoadActivity extends FragmentActivity implements ScaleGestureDetector.OnScaleGestureListener, View.OnClickListener {

    private String TAG = VideoLoadActivity.this.getClass().toString();

    private static final String TAG_GIF_CHOOSER = "gif";

    private MediaInfo mOutputInfo;

    private AliyunIRecorder mRecorder;
    private AliyunIClipManager clipManager;
    private OrientationDetector orientationDetector;
    private TipsDialog mTipsDialog;

    //速度
    private float rate = 1.0f;

    @Bind(R.id.sv_view_video_load)
    SurfaceView mRecorderSurfaceView;
    //录制按钮及录制时的进度条
    @Bind(R.id.zdy_circleProgressBar)
    CircleProgressBar zdy_loadBtn;
    //镜头翻转
    @Bind(R.id.tv_activity_video_load_return)
    View v_CameraReturn;
    //滤镜
    @Bind(R.id.tv_lvjing_btn)
    View v_lvjingBtn;
    //贴纸
    @Bind(R.id.tv_paster_btn)
    View v_PasterBtn;
    //倒数录制
    @Bind(R.id.tv_activity_video_load_countdown)
    View v_CountdownBtn;
    //返回
    @Bind(R.id.iv_activity_video_load_back)
    View v_backBtn;
    //音乐
    @Bind(R.id.tv_music_btn)
    View v_musicBtn;
    //上传
    @Bind(R.id.tv_upload_btn)
    View v_uploadBtn;
    //回删
//    private View v_huishan;
    //下一步
    @Bind(R.id.tv_next_btn)
    View v_nextBtn;
    //快慢速
    @Bind(R.id.tv_activity_video_load_speed)
    View v_speed;
    //显示倒数时的view
    @Bind(R.id.tv_activity_video_load_countdown_text)
    CountDownTextView tv_countTimeShow;

    @Bind(R.id.alivc_video_picker_view)
    StringScrollPicker mPicker;

    @Bind(R.id.rg_video_fast_low)
    RadioGroup rg_speed;

    /**
     * 滤镜及美颜dialog
     */
    FilterEffectChooser filterEffectChooser;


//    MusicDialog musicChooser;

    AliyunVideoParam mVideoParam;

    /**
     * 裁剪参数
     */
    private AlivcSvideoEditParam mAlivcSvideoEditParam;

    /**
     * 记录美颜选中的索引, 默认为3
     */
    private int currentBeautyFacePosition = 0;


    ////////////////////////////////////////////////////////////////
    /**
     * 人脸贴纸dialog
     */
    private GIfEffectChooser gifEffectChooser;
    //当前所选中的人脸贴纸路径
    private String currPasterPath;
    //选中的贴图效果
    private EffectPaster effectPaster;
    ////////////////////////////////////////////////////////////////

    private AsyncTask<Void, Void, Void> initAssetPath;
    private AsyncTask<Void, Void, Void> copyAssetsTask;

    private LoadingDialogFragment mLoadingDialog;

    private String videoPath = "";


    private static final String TAG_FILTER_CHOOSER = "filter";
    /**
     * 是否正在录制
     */
    private boolean mIsBackground = false;

    /**
     * 记录filter选中的item索引
     */
    private int currentFilterPosition;

    private LinkedHashMap<Integer, Object> mConflictEffects = new LinkedHashMap<>();

    public static final int TYPE_FILTER = 1;

    private String[] mEffDirs;

    private int width = 720;
    private int height = width * 16 / 9;

    //最小录制时长
    private int minRecordTime = 2000;
    //最大录制时长
    private int maxRecordTime = 15 * 1000;
    //传感器角度值
    private int rotation;
    //当前使用的摄像头位置
    private CameraType cameraType = CameraType.FRONT;
    /**
     * 底层在onPause时会回收资源, 此处选择的滤镜的资源路径, 用于恢复状态
     */
    private String filterSourcePath;


    public static final int PERMISSION_REQUEST_CODE = 1000;

    /**
     * 相机的原始NV21数据
     */
    private byte[] frameBytes;
    private byte[] mFuImgNV21Bytes;

    /**
     * 原始数据宽
     */
    private int frameWidth;
    /**
     * 原始数据高
     */
    private int frameHeight;

    //1.判断是否已经获取照相机的权限  完成 PermissionUtils.checkPermissionsGroup(this, permission)
    //2.判断储存空间是否足够
    //3.保持屏幕常亮  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    //4.设置好输出地址
    //5.设置好开始时的参数
    //6.初始化控件

    private String pathComplete;

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
        setContentView(R.layout.activity_video_load);
        ButterKnife.bind(this);
        MySystemParams.getInstance().init(this);
        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission,
                    PERMISSION_REQUEST_CODE);
            return;
        }
        init();
        if (mLoadingDialog == null) mLoadingDialog = LoadingDialogFragment.newInstance("正在制作中...");

    }

    /**
     * 初始化控件（自身activity的控件）
     */
    private void initViews() {
//        mRecorderSurfaceView = findViewById(R.id.sv_view_video_load);
//        zdy_loadBtn = findViewById(R.id.zdy_circleProgressBar);
//        v_CameraReturn = findViewById(R.id.tv_activity_video_load_return);
//        v_lvjingBtn = findViewById(R.id.tv_lvjing_btn);
//        v_PasterBtn = findViewById(R.id.tv_paster_btn);
//        v_CountdownBtn = findViewById(R.id.tv_activity_video_load_countdown);
//        tv_countTimeShow = findViewById(R.id.tv_activity_video_load_countdown_text);
//        v_backBtn = findViewById(R.id.iv_activity_video_load_back);
//        v_musicBtn = findViewById(R.id.tv_music_btn);
//        v_uploadBtn = findViewById(R.id.tv_upload_btn);
////        v_huishan = findViewById(R.id.tv_huishan_btn);
//        v_nextBtn = findViewById(R.id.tv_next_btn);
//        v_speed = findViewById(R.id.tv_activity_video_load_speed);
////        mRecorderSurfaceView.setLayoutParams(getLayoutParams());

    }


    /**
     * 倒计时
     */
    private void countTime() {
        tv_countTimeShow.startCount(3 * 1000, 1000, new CountDownTextView.OnCountDownFinishedListener() {
            @Override
            public void finished() {
                //todo 开始录制逻辑
                zdy_loadBtn.startLoading();
                Toast.makeText(VideoLoadActivity.this, "开始录制", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 显示滤镜选择的控件
     */
    private void showFilterEffectView() {

        if (filterEffectChooser == null) {
            filterEffectChooser = new FilterEffectChooser();
        }
        if (filterEffectChooser.isAdded()) {
            return;
        }
        // 滤镜改变listener
        filterEffectChooser.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(EffectInfo effectInfo, int index) {
                if (effectInfo != null) {
                    filterSourcePath = effectInfo.getPath();
                    if (index == 0) {
                        filterSourcePath = null;
                    }
                    EffectFilter filterEffect = new EffectFilter(filterSourcePath);
                    mRecorder.applyFilter(filterEffect);
                    mConflictEffects.put(TYPE_FILTER, filterEffect);
                }
                currentFilterPosition = index;
            }
        });
        filterEffectChooser.setFilterPosition(currentFilterPosition);
        filterEffectChooser.setDismissListener(new DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {

            }

            @Override
            public void onDialogShow() {

            }
        });
        //美颜改变listener
        filterEffectChooser.setOnBeautyFaceItemSeletedListener(new OnBeautyFaceItemSeletedListener() {
            @Override
            public void onNormalSelected(int postion, BeautyLevel beautyLevel) {
//                currentBeautyFaceNormalPosition = postion;


            }

            @Override
            public void onAdvancedSelected(int position, BeautyLevel beautyLevel) {
                currentBeautyFacePosition = position;
                SharedPreferenceUtils.setBeautyFaceLevel(VideoLoadActivity.this, position);
                // 普通美颜
                mRecorder.setBeautyLevel(beautyLevel.getValue());
            }
        });
        filterEffectChooser.show(getSupportFragmentManager(), TAG_FILTER_CHOOSER);
    }


//    /**
//     * 录制时，控件的状态
//     */
//    private void setLoadingStatus(int visibility) {
//        setVisibility(visibility, v_backBtn, v_CameraReturn, v_CountdownBtn, v_speed, v_uploadBtn, v_musicBtn, v_PasterBtn, v_lvjingBtn);
//    }

    /**
     * 控制View 在特定时的状态
     *
     * @param visibility
     * @param views
     */
    private void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }


    /**
     * 初始化控件事件（自身activity的控件）
     */
    private void initListener() {
//        v_CameraReturn.setOnClickListener(this);
//        v_lvjingBtn.setOnClickListener(this);
//        v_PasterBtn.setOnClickListener(this);
//        v_CountdownBtn.setOnClickListener(this);
//        v_backBtn.setOnClickListener(this);
//        v_musicBtn.setOnClickListener(this);
        zdy_loadBtn.setOnLoadingListener(new CircleProgressBar.OnLoadingListener() {
            @Override
            public void getStatus(int seconds, boolean isFinish) {

            }

            @Override
            public void startLoading() {

                startRecord();
            }

            @Override
            public void stopLoading(long playTime) {
                if (mLoadingDialog != null && !mLoadingDialog.isShowing) {
                    mLoadingDialog.show(getFragmentManager(), "show");
                }
                stopRecord();
//                Toast.makeText(VideoLoadActivity.this, "完成录制", Toast.LENGTH_LONG).show();
            }
        });

        mPicker.setOnSelectedListener(new BaseScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(BaseScrollPickerView baseScrollPickerView, int position) {

                switch (position) {
                    case 0:
                        maxRecordTime = 15 * 1000;
                        break;
                    case 1:
                        maxRecordTime = 60 * 1000;
                        break;
                }
            }
        });
        rg_speed.setOnCheckedChangeListener((radioGroup, viewId) -> {

            switch (viewId) {
                case R.id.rb_video_low:
                    //慢
                    rate = 0.75f;
                    break;

                case R.id.rb_video_nor:
                    //标准
                    rate = 1f;
                    break;

                case R.id.rb_video_fast:
                    //快
                    rate = 1.5f;
                    break;

            }
            mRecorder.setRate(rate);

        });
    }

    /**
     * 镜头切换功能,人脸识别功能需要cameraType的状态来设置传感值，务必要记录下来镜头的状态
     * AliyunIRecord.switchCamera() / AliyunIRecord.setCamera()
     */
    private void changeCamera() {

        if (mRecorder != null) {
            int cameraId = mRecorder.switchCamera();
            for (CameraType type : CameraType.values()) {

                if (type.getType() == cameraId) {
                    cameraType = type;
                }

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        startOrStopPreview(false);
        stopRecord();


        if (orientationDetector != null) {
            orientationDetector.disable();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecorder != null) {
            mRecorder.startPreview();
        }

        startOrStopPreview(true);


    }

    @Override
    protected void onStop() {
        super.onStop();

        startOrStopPreview(false);
        stopRecord();

        if (orientationDetector != null) {
            orientationDetector.disable();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopRecord();
        ButterKnife.unbind(this);
        mRecorder.destroy();
    }

    /**
     * 设置是否浏览
     *
     * @param isPreview 浏览为true；取消浏览为false；
     */
    private void startOrStopPreview(boolean isPreview) {
        if (mRecorder != null) {
            if (isPreview) {
                mRecorder.startPreview();
            } else {
                mRecorder.stopPreview();
            }
        }
    }


    /**
     * 权限申请 镜头，音像，输入，输出权限
     */
    public String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 设置资源的路径，如滤镜
     */
    private void setAssetPath() {
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator + Common.QU_NAME
                + File.separator;
        File filter = new File(new File(path), "filter");
        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        mEffDirs = new String[list.length + 1];
        mEffDirs[0] = null;
        int length = list.length;
        for (int i = 0; i < length; i++) {
            mEffDirs[i + 1] = filter.getPath() + File.separator + list[i];
        }
    }

    private void finishRecording() {

    }

    private void initAssetPath() {
        initAssetPath = new AssetPathInitTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class AssetPathInitTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<VideoLoadActivity> weakReference;

        AssetPathInitTask(VideoLoadActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VideoLoadActivity activity = weakReference.get();
            if (activity != null) {
                activity.setAssetPath();
            }
            return null;
        }
    }

    /**
     * 从asssets获取资源，再把assets的压缩包加压到SDcard上，注意aliyun的SDK不支持使用assets流，务必要把问价解压到本地
     */
    private void copyAssets() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                copyAssetsTask = new CopyAssetsTask(VideoLoadActivity.this).executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 700);

    }

    public static class CopyAssetsTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<VideoLoadActivity> weakReference;
        ProgressDialog progressBar;

        CopyAssetsTask(VideoLoadActivity activity) {

            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            VideoLoadActivity activity = weakReference.get();
            progressBar = new ProgressDialog(activity);
            progressBar.setMessage("资源拷贝中....");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VideoLoadActivity activity = weakReference.get();
            if (activity != null) {
                Common.copyAll(activity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.dismiss();
            VideoLoadActivity activity = weakReference.get();
            if (activity != null) {
                //资源复制完成之后设置一下人脸追踪，防止第一次人脸动图应用失败的问题

                if (activity.mRecorder != null) {
                    activity.setFaceTrackModePath();
                }

            }

        }
    }

    /**
     * 动态Gif dialog
     */
    private void showGifEffectView() {
        if (gifEffectChooser == null) {
            gifEffectChooser = new GIfEffectChooser();
            gifEffectChooser.setPasterSelectListener(new PasterSelectListener() {
                @Override
                public void onPasterSelected(PreviewPasterForm imvForm) {
                    String path;
                    if (imvForm.getId() == 150) {
                        //id=150的动图为自带动图
                        path = imvForm.getPath();
                    } else {
                        path = DownloadFileUtils.getAssetPackageDir(VideoLoadActivity.this,
                                imvForm.getName(), imvForm.getId()).getAbsolutePath();
                    }
                    currPasterPath = path;
                    addEffectToRecord(path);

                }

                @Override
                public void onSelectPasterDownloadFinish(String path) {
                    // 所选的paster下载完成后, 记录该paster 的path
                    currPasterPath = path;
                }
            });

            gifEffectChooser.setDismissListener(new DialogVisibleListener() {
                @Override
                public void onDialogDismiss() {

                }

                @Override
                public void onDialogShow() {
                    if (!TextUtils.isEmpty(currPasterPath)) {
                        // dialog显示后,如果记录的paster不为空, 使用该paster
                        addEffectToRecord(currPasterPath);
                    }
                }
            });
        }
        gifEffectChooser.show(getSupportFragmentManager(), TAG_GIF_CHOOSER);
    }

    /**
     * 获取到动态gif路径后，加入mRecorder当中，显示人脸贴纸
     *
     * @param path 动态gif路径
     */
    private void addEffectToRecord(String path) {

        if (effectPaster != null) {

            mRecorder.removePaster(effectPaster);
        }

        effectPaster = new EffectPaster(path);

        mRecorder.addPaster(effectPaster);

    }

    /**
     * 设置人脸识别的模型文件，从assets解压到本地，设置路径即可；
     */
    public void setFaceTrackModePath() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                String path = StorageUtils.getCacheDirectory(VideoLoadActivity.this).getAbsolutePath() + File.separator + Common.QU_NAME + File.separator;
                if (mRecorder != null) {
                    mRecorder.needFaceTrackInternal(true);
                    mRecorder.setFaceTrackInternalModelPath(path + "/model");
                }
            }
        });

    }

    /**
     * 进入界面
     *
     * @param context  Context
     * @param entrance 模块入口方式
     */
    public static void startRecord(Context context, String entrance) {

        Intent intent = new Intent(context, VideoLoadActivity.class);
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, param.getResolutionMode());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO, param.getRatioMode());
//        intent.putExtra(AliyunSnapVideoParam.RECORD_MODE, param.getRecordMode());
//        intent.putExtra(AliyunSnapVideoParam.FILTER_LIST, param.getFilterList());
//        intent.putExtra(AliyunSnapVideoParam.BEAUTY_LEVEL, param.getBeautyLevel());
//        intent.putExtra(AliyunSnapVideoParam.BEAUTY_STATUS, param.getBeautyStatus());
//        intent.putExtra(AliyunSnapVideoParam.CAMERA_TYPE, param.getCameraType());
//        intent.putExtra(AliyunSnapVideoParam.FLASH_TYPE, param.getFlashType());
//        intent.putExtra(AliyunSnapVideoParam.NEED_CLIP, param.isNeedClip());
//        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION, param.getMaxDuration());
//        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION, param.getMinDuration());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY, param.getVideoQuality());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP, param.getGop());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_BITRATE, param.getVideoBitrate());
//        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, param.getVideoCodec());
//        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE, param.getFrameRate());
//        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
//        intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION, param.getMinCropDuration());
//        intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION, param.getMinVideoDuration());
//        intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION, param.getMaxVideoDuration());
//        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
//        intent.putExtra(INTENT_PARAM_KEY_ENTRANCE, entrance);
        context.startActivity(intent);
    }


//   录制视频的重要参数：

//    视频比例：
//             AliyunSnapVideoParam.RATIO_MODE_1_1  /AliyunSnapVideoParam.RATIO_MODE_9_16  /AliyunSnapVideoParam.RATIO_MODE_3_4
//    视频质量：
//             VideoQuality.SSD  /VideoQuality.HD  /VideoQuality.SD  /VideoQuality.LD
//    视频编码：
//             VideoCodecs.H264_SOFT_FFMPEG  /  VideoCodecs.H264_SOFT_OPENH264  /  VideoCodecs.H264_HARDWARE
//    视频分辨率：
//             AliyunSnapVideoParam.RESOLUTION_360P  /  AliyunSnapVideoParam.RESOLUTION_480P  /  AliyunSnapVideoParam.RESOLUTION_540P  /  AliyunSnapVideoParam.RESOLUTION_720P
//
//     关键帧：
//     码率：

    /**
     * 初始化引用的总和方法，请注意初始化的顺序；
     */
    private void init() {
        List<String> strings = new ArrayList<>(2);
        strings.add("拍15s");
        strings.add("拍60s");
        mPicker.setData(strings);
        //向上的三角形对应的图片
        mPicker.setCenterItemBackground(UIConfigManager.getDrawableResources(this, R.attr.triangleImage, R.drawable.back_tip));
        mPicker.setSelectedPosition(0);
//        initViews();
        initListener();
        initMediaInfo();
        initRecorder();
        initSurfaceView();
        initOritationDetector();
        initAssetPath();
        copyAssets();
        setFaceTrackModePath();

        startOrStopPreview(true);
        if (orientationDetector != null && orientationDetector.canDetectOrientation()) {
            orientationDetector.enable();
        }

    }

    /**
     * 初始化mRecorder.MediaInfo的参数，如视频的宽高，分辨率，帧率，码率，编码方式等
     */
    private void initMediaInfo() {
        mOutputInfo = new MediaInfo();
        //帧率（越高越流畅 建议25-30）
        mOutputInfo.setFps(30);
        //分辨率 16/9
        mOutputInfo.setVideoWidth(ScreenUtils.getWidth(this));
        mOutputInfo.setVideoHeight(ScreenUtils.getWidth(this) / 9 * 16);
        mOutputInfo.setVideoCodec(VideoCodecs.H264_SOFT_FFMPEG);
//        mOutputInfo.setCrf(0);

        mVideoParam = new AliyunVideoParam.Builder()
                .gop(30)
                .bitrate(0)
//                .crf(0)
                .frameRate(30)
                .outputWidth(ScreenUtils.getWidth(this))
                .outputHeight(ScreenUtils.getWidth(this) / 9 * 16)
                .videoQuality(VideoQuality.SSD)
                .build();

        mAlivcSvideoEditParam = new AlivcSvideoEditParam.Build()
                .setRatio(RATIO_ORIGINAL)
                .setResolutionMode(AlivcSvideoEditParam.RESOLUTION_720P)
                .setHasTailAnimation(false)
                .setEntrance("svideo")
                .setCropMode(VideoDisplayMode.FILL)
                .setFrameRate(30)
                .setGop(30)
                .setBitrate(0)
                .setVideoQuality(VideoQuality.SSD)
                .setVideoCodec(VideoCodecs.H264_HARDWARE)
                .build();
        mAlivcSvideoEditParam.setCropAction(CropKey.ACTION_TRANSCODE);
    }

    private void startCrop(String path) {
        AliyunIImport mImport = AliyunImportCreator.getImportInstance(VideoLoadActivity.this);
        mImport.setVideoParam(mVideoParam);
        mImport.addMediaClip(new AliyunVideoClip.Builder()
                .source(path)
                .startTime(0)
                .endTime(clipManager.getDuration())
                .displayMode(AliyunDisplayMode.DEFAULT)
                .build());

//                        String projectJsonPath = mImport.generateProjectConfigure();
//                        Intent intent = new Intent();
//                        ActionInfo action = AliyunSvideoActionConfig.getInstance().getAction();
//                        //获取录制完成的配置页面
//                        String tagClassName = action.getTagClassName(ActionInfo.SVideoAction.RECORD_TARGET_CLASSNAME);
//
//                        intent.setClassName(VideoLoadActivity.this, tagClassName);
//                        if (tagClassName.equals(ActionInfo.getDefaultTargetConfig(ActionInfo.SVideoAction.RECORD_TARGET_CLASSNAME))) {
////                            intent.putExtra("isReplaceMusic", isUseMusic);
//                        }
//                        intent.putExtra("video_param", mVideoParam);
//                        intent.putExtra("project_json_path", projectJsonPath);
////                        intent.putExtra(INTENT_PARAM_KEY_ENTRANCE, mRecordEntrance);
////                        intent.putExtra(INTENT_PARAM_KEY_HAS_MUSIC, videoRecordView.isHasMusic());
//                        startActivity(intent);
//                        mImport.release();
        String projectJsonPath = mImport.generateProjectConfigure();
        com.aliyun.svideo.base.MediaInfo mediaInfo = new com.aliyun.svideo.base.MediaInfo();
        mediaInfo.filePath = clipManager.getVideoPathList().get(0);
        mediaInfo.duration = clipManager.getDuration();
        mAlivcSvideoEditParam.setMediaInfo(mediaInfo);
        VideoCropActivity.startCropForResult(VideoLoadActivity.this, mAlivcSvideoEditParam, projectJsonPath);
        mImport.release();
        finish();
    }


    /**
     * 初始化mRecorder的部分参数
     */
    private void initRecorder() {

        mRecorder = AliyunRecorderCreator.getRecorderInstance(this);
        mRecorder.setMediaInfo(mOutputInfo);
        mRecorder.setVideoQuality(VideoQuality.SSD);
        mRecorder.setVideoBitrate(0);
        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        mRecorder.setFaceTrackInternalMaxFaceCount(2);
        mRecorder.setMute(false);
        mRecorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(boolean b, long l) {
                //录制完成时回调
                Log.e(TAG, "onComplete");
                ThreadUtils.runOnUiThread(() -> {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing)
                        mLoadingDialog.dismiss();
                    setVisibility(View.VISIBLE, v_backBtn, v_nextBtn);
                    if (clipManager.getDuration() - minRecordTime < 0) {
                        clipManager.deletePart();
                        Toast.makeText(VideoLoadActivity.this, "录制视频，不可少于2秒", Toast.LENGTH_LONG).show();
                    } else {
                        mRecorder.finishRecording();
                    }
                });


            }

            @Override
            public void onFinish(final String s) {
                ThreadUtils.runOnUiThread(() -> {
                    pathComplete = s;
                    if (mLoadingDialog != null && mLoadingDialog.isShowing)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(VideoLoadActivity.this, "完成录制");
                    Log.e(TAG, "onFinish");
                });


            }

            @Override
            public void onProgress(long l) {
                //录制时回调
                Log.e(TAG, "onProgress");
            }

            @Override
            public void onMaxDuration() {
                Log.e(TAG, "onMaxDuration");
            }

            @Override
            public void onError(int i) {
                ThreadUtils.runOnUiThread(() -> {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(VideoLoadActivity.this, "录制失败");
                    Log.e(TAG, "onError");
                });

            }

            @Override
            public void onInitReady() {
                //初始化回调
                Log.e(TAG, "onInitReady");
            }

            @Override
            public void onDrawReady() {

            }


            @Override
            public void onPictureBack(Bitmap bitmap) {

            }

            @Override
            public void onPictureDataBack(byte[] bytes) {

            }
        });
        mRecorder.setOnTextureIdCallback(new OnTextureIdCallBack() {
            @Override
            public int onTextureIdBack(int i, int i1, int i2, float[] floats) {
                return 0;
            }

            @Override
            public int onScaledIdBack(int i, int i1, int i2, float[] floats) {
                return i;
            }

            @Override
            public void onTextureDestroyed() {

            }
        });
        mRecorder.setBeautyStatus(true);
        mRecorder.setBeautyLevel(BeautyLevel.BEAUTY_LEVEL_ZERO.getValue());
        mRecorder.setGop(30);
        cameraType = mRecorder.getCameraCount() == 1 ? CameraType.BACK : cameraType;
        mRecorder.setCamera(cameraType);
        clipManager = mRecorder.getClipManager();

    }

    /**
     * 设置surfaceview的参数；
     */
    private void initSurfaceView() {

        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, this);
        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        float x = e.getX() / mRecorderSurfaceView.getWidth();
                        float y = e.getY() / mRecorderSurfaceView.getHeight();
                        mRecorder.setFocus(x, y);

//                        mFocusView.showView();
//                        mFocusView.setLocation(e.getRawX(), e.getRawY());
                        return true;
                    }
                });
        mRecorderSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() >= 2) {
                    scaleGestureDetector.onTouchEvent(event);
                } else if (event.getPointerCount() == 1) {
                    gestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });
        //添加录制surFaceView
        mRecorder.setDisplayView(mRecorderSurfaceView);
//        mRecorder.resizePreviewSize(getLayoutParams().width, getLayoutParams().height);
//        mRecorder.resizePreviewSize(ScreenUtils.getWidth(this), ScreenUtils.getHeight(this));
//        mRecorder.setFaceDetectRotation(100);
    }

    /**
     * ※※※设置传感，此值用于人脸识别功能，非常重要
     */
    private void initOritationDetector() {
        orientationDetector = new OrientationDetector(this.getApplicationContext());
        orientationDetector.setOrientationChangedListener(new OrientationDetector.OrientationChangedListener() {
            @Override
            public void onOrientationChanged() {
                rotation = getPictureRotation();
                mRecorder.setRotation(rotation);

            }
        });
    }

    /**
     * ※※※设置传感度的计算方式，非常重要
     */
    private int getPictureRotation() {
//        Log.e("switchCamera", cameraType + ":");
        int orientation = orientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }
        if (cameraType == CameraType.FRONT) {

            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }


    /**
     * 开始录制；
     */
    private void startRecord() {
        //设置录制时长
        zdy_loadBtn.setLoadingTime(maxRecordTime / 1000);
        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission,
                    PERMISSION_REQUEST_CODE);
            return;
        }

        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            FixedToastUtils.show(this, getResources().getString(com.aliyun.demo.R.string.aliyun_no_free_memory));
            return;
        }

        if (mRecorder != null && !mIsBackground) {
            videoPath = SDCardConstants.OUTPUT_PATH_DIR + File.separator + System.currentTimeMillis() + "-record.mp4";
            mRecorder.setOutputPath(videoPath);
            mRecorder.startRecording();
            setVisibility(View.GONE, v_backBtn, v_CameraReturn, v_CountdownBtn, v_speed, v_uploadBtn, v_musicBtn, v_PasterBtn, v_lvjingBtn, mPicker, rg_speed);
            mIsBackground = true;

        }

    }

    /**
     * 停止录制；注意无论是 mRecorder.stopRecording()还是mRecorder.finishRecording();都是先把每段的视频录制到本地以后，再进行剪接的
     */
    private void stopRecord() {
        if (mRecorder != null && mIsBackground) {
            mRecorder.stopRecording();

            mIsBackground = false;
            zdy_loadBtn.setClickable(false);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了
                //Toast.makeText(this, "get All Permisison", Toast.LENGTH_SHORT).show();
                init();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                showPermissionDialog();
            }
        }
    }

    private float lastScaleFactor;
    private float scaleFactor;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float factorOffset = detector.getScaleFactor() - lastScaleFactor;
        scaleFactor += factorOffset;
        lastScaleFactor = detector.getScaleFactor();
        if (scaleFactor < 0) {
            scaleFactor = 0;
        }
        if (scaleFactor > 1) {
            scaleFactor = 1;
        }
        mRecorder.setZoom(scaleFactor);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }

    @OnClick({R.id.tv_activity_video_load_return, R.id.tv_lvjing_btn, R.id.tv_paster_btn, R.id.tv_activity_video_load_countdown, R.id.iv_activity_video_load_back, R.id.tv_music_btn, R.id.tv_next_btn, R.id.tv_activity_video_load_speed, R.id.tv_upload_btn})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //镜头转换
            case R.id.tv_activity_video_load_return:
                changeCamera();
                break;

            case R.id.tv_lvjing_btn:
                showFilterEffectView();
                break;

            case R.id.tv_paster_btn:
                showGifEffectView();
                break;

            case R.id.tv_activity_video_load_countdown:
                countTime();
                break;

            case R.id.iv_activity_video_load_back:
                isfinish();
                break;

            case R.id.tv_music_btn:
//                showMusic();
                break;

            case R.id.tv_next_btn:
                if (!TextUtils.isEmpty(pathComplete) && !mLoadingDialog.isShowing) {
                    startCrop(pathComplete);
                }

                break;
            case R.id.tv_activity_video_load_speed:
                showSpeed();
                break;

            case R.id.tv_upload_btn:
                Intent intent = new Intent(this, MediaActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showSpeed() {
        rg_speed.setVisibility(rg_speed.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    //返回键的判断
    private void isfinish() {

        if (videoPath.isEmpty()) {
            //如果路径为空，退出界面
            finish();
        } else {
            //TODO 如果路径不为空，弹出提示框是否删除录制视频
            showDelectDialog();

        }

    }


//    private void showMusic() {
//        if (null == musicChooser) {
//            musicChooser = new MusicDialog();
//        }
//        musicChooser.setOnMusicListener(new MusicDialog.OnMusicListener() {
//            @Override
//            public void musicInfo(String path, int startTime, int duration) {
//                mRecorder.applyMv(new EffectBean());
//                mRecorder.setMusic(path, startTime, duration);
//            }
//        });
//        musicChooser.show(getSupportFragmentManager(), "musicChooser");
//    }

    /**
     * 回删逻辑
     * 删除录制的操作，及清空录制路径
     */

    private void showDelectDialog() {
        if (null == mTipsDialog) {
            mTipsDialog = new TipsDialog();
        }
//        mTipsDialog.setContents("是否删除这段视频？");
        mTipsDialog.setOnTipsDialogListener(new TipsDialog.OnTipsDialogListener() {
            @Override
            public void onDismiss(TipsDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(TipsDialog dialog) {
                cancelPath();
                dialog.dismiss();

            }

            @Override
            public void onCreatedView(TextView Contents) {
                Contents.setText("确认放弃本次录制？");
            }
        });
        mTipsDialog.show(getSupportFragmentManager(), "");
    }

    private void cancelPath() {
        clipManager.deleteAllPart();
        videoPath = "";
        //设置控件的状态
//        setLoadingStatus(View.VISIBLE);
        setVisibility(View.VISIBLE, v_backBtn, v_CameraReturn, v_CountdownBtn, v_speed, v_uploadBtn, v_lvjingBtn);
        setVisibility(View.GONE, v_nextBtn);
        zdy_loadBtn.setBaseStatus();
        zdy_loadBtn.setClickable(true);
    }

    //系统授权设置的弹框
    AlertDialog openAppDetDialog = null;

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(com.aliyun.demo.R.string.app_name) + "需要访问 \"相册\"、\"摄像头\" 和 \"外部存储器\",否则会影响绝大部分功能使用, 请到 \"应用信息 -> 权限\" 中设置！");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("暂不设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        if (null == openAppDetDialog) {
            openAppDetDialog = builder.create();
        }
        if (null != openAppDetDialog && !openAppDetDialog.isShowing()) {
            openAppDetDialog.show();
        }
    }


}

