package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.luck.picture.lib.photoview.PhotoView;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.GlideUtils;


/**
 * 单张图片预览界面
 */
public class PicturePreviewActivity extends Activity {
    PhotoView photoIv;
    private String path;
    public static final String PATH = "path";

    public static Intent newIntent(Context context, String path) {
        Intent intent = new Intent(context, PicturePreviewActivity.class);
        intent.putExtra(PATH, path);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture_preview);
        initView();
    }

    protected void initView() {
        if (getIntent().getStringExtra(PATH) != null) {
            path = getIntent().getStringExtra(PATH);
        }
        setContentView(R.layout.activity_picture_preview);
        photoIv = (PhotoView) findViewById(R.id.photo_iv);
        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GlideUtils.loadBigPic(photoIv,path);
    }

}
