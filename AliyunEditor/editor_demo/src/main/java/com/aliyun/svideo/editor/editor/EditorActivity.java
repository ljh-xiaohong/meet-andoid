/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.aliyun.svideo.editor.editor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.aliyun.apsaravideo.music.utils.NotchScreenUtil;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.svideo.base.AlivcEditorRoute;
import com.aliyun.svideo.base.AlivcSvideoEditParam;
import com.aliyun.svideo.base.MediaInfo;
import com.aliyun.svideo.editor.R;
import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.view.AlivcEditView;
import com.aliyun.svideo.sdk.external.struct.common.AliyunImageClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频编辑界面, 主要负责显示AlivcEditView
 */
public class EditorActivity extends FragmentActivity {
    private static final String TAG = "EditorActivity";
    public static final String KEY_VIDEO_PARAM = "video_param";
    public static final String KEY_PROJECT_JSON_PATH = "project_json_path";
    public static final String KEY_TEMP_FILE_LIST = "temp_file_list";
    private Uri mUri;
    private AliyunVideoParam mVideoParam;
    private ArrayList<String> mTempFilePaths = null;
    private AlivcEditView editView;
    /**
     * 是否替换原视频中音乐
     */
    private boolean isReplaceMusic;

    /**
     *   上个界面通过intent的传递参数的key,
     *   对应的value主要作用是
     *      判断是编辑模块进入还是通过社区模块的编辑功能进入
     *      svideo: 短视频
     *      community: 社区
     */
    private static final String INTENT_PARAM_KEY_ENTRANCE = "entrance";

    /**
     * 判断是否为录制有音乐, 如果有音乐, 编辑界面不能使用音效
     */
    public static final String INTENT_PARAM_KEY_HAS_RECORD_MUSIC = "hasRecordMusic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检测是否是全面屏手机, 如果不是, 设置FullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!NotchScreenUtil.checkNotchScreen(this)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_test);
        // 判断是编辑模块进入还是通过社区模块的编辑功能进入
        //  svideo: 短视频
        //  community: 社区
        String entrance = getIntent().getStringExtra(INTENT_PARAM_KEY_ENTRANCE);
        editView = findViewById(R.id.alivc_edit_view);
        Intent intent = getIntent();
        // TODO 调用 EditView 的 AliyunIEditor.applyMV(EffectBean effect) 添加MV效果
        EffectInfo effectInfo = (EffectInfo) intent.getSerializableExtra("effect_info");
        editView.setMv(effectInfo);
        mVideoParam = (AliyunVideoParam) intent.getSerializableExtra(KEY_VIDEO_PARAM);
        boolean hasTailAnimation = getIntent().getBooleanExtra("hasTailAnimation", false);
        isReplaceMusic = intent.getBooleanExtra("isReplaceMusic", false);
        String jsonExtra = intent.getStringExtra(KEY_PROJECT_JSON_PATH);
        if (jsonExtra != null) {
            mUri = Uri.fromFile(new File(jsonExtra));
        } else {
            List<MediaInfo> mediaInfos = intent.getParcelableArrayListExtra(AlivcEditorRoute.KEY_INTENT_MEDIA_INFO);
            mUri = Uri.fromFile(new File(getProjectJsonPath(mediaInfos)));
        }
        if ("svideo".equals(entrance)) {
            editView.setParam(mVideoParam, mUri, hasTailAnimation, true);
        } else {
            editView.setParam(mVideoParam, mUri, hasTailAnimation, false);
        }
        editView.setReplaceMusic(isReplaceMusic);
        mTempFilePaths = intent.getStringArrayListExtra(KEY_TEMP_FILE_LIST);

        boolean mHasRecordMusic = intent.getBooleanExtra(INTENT_PARAM_KEY_HAS_RECORD_MUSIC, false);
        editView.setHasRecordMusic(mHasRecordMusic);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (editView != null) {
            editView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (editView != null) {
            editView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (editView != null) {
            editView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (editView != null) {
            editView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (editView != null) {
            editView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (editView != null) {
            editView.onStart();
        }
    }
    @Override
    public void onBackPressed() {
        boolean isConsume = false;
        if (editView != null) {
            isConsume = editView.onBackPressed();
        }
        if (!isConsume) {
            super.onBackPressed();
            finish();
        }
    }

    /**
     * 通过MediaInfo生成ProjectJson
     * @param mediaInfos List<MediaInfo>
     * @return jsonPath
     */
    private String getProjectJsonPath(List<MediaInfo> mediaInfos) {

        AliyunIImport mImport = AliyunImportCreator.getImportInstance(this);
        mImport.setVideoParam(mVideoParam);
        int size = mediaInfos.size();
        for (int i = 0; i < size; i++) {
            MediaInfo mediaInfo = mediaInfos.get(i);
            if (mediaInfo.mimeType.startsWith("video")) {
                mImport.addMediaClip(new AliyunVideoClip.Builder()
                                     .source(mediaInfo.filePath)
                                     .startTime(mediaInfo.startTime)
                                     .endTime(mediaInfo.startTime + mediaInfo.duration)
                                     .build());
            } else if (mediaInfo.mimeType.startsWith("image")) {
                mImport.addMediaClip(new AliyunImageClip.Builder()
                                     .source(mediaInfo.filePath)
                                     .duration(mediaInfo.duration)
                                     .build());
            }
        }
        String projectConfigure = mImport.generateProjectConfigure();
        mImport.release();

        return projectConfigure;
    }

    public static void startEdit(Context context, AlivcSvideoEditParam param, List<MediaInfo> mediaList) {
        if (context == null || param == null || mediaList == null || mediaList.size() == 0) {
            return;
        }
        AliyunIImport mImport = AliyunImportCreator.getImportInstance(context);
        param.setMediaInfo(mediaList.get(0));
        AliyunVideoParam mVideoParam = param.generateVideoParam();
        mImport.setVideoParam(mVideoParam);
        int size = mediaList.size();
        for (int i = 0; i < size; i++) {
            MediaInfo mediaInfo = mediaList.get(i);
            if (mediaInfo.mimeType.startsWith("video")) {
                mImport.addMediaClip(new AliyunVideoClip.Builder()
                                     .source(mediaInfo.filePath)
                                     .startTime(mediaInfo.startTime)
                                     .endTime(mediaInfo.startTime + mediaInfo.duration)
                                     .build());
            } else if (mediaInfo.mimeType.startsWith("image")) {
                mImport.addMediaClip(new AliyunImageClip.Builder()
                                     .source(mediaInfo.filePath)
                                     .duration(mediaInfo.duration)
                                     .build());
            }
        }
        String projectJsonPath = mImport.generateProjectConfigure();
        if (projectJsonPath != null) {
            Intent intent = new Intent(context, EditorActivity.class);
            intent.putExtra("video_param", mVideoParam);
            intent.putExtra("project_json_path", projectJsonPath);
            intent.putExtra("hasTailAnimation", param.isHasTailAnimation());
            intent.putExtra(INTENT_PARAM_KEY_ENTRANCE, param.getEntrance());
            context.startActivity(intent);
        }
    }

}
