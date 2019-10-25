package com.yuejian.meet.activities.home;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.ImageAdapter;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.recyclerview.SpaceGridItemDecoration;
import com.yuejian.meet.utils.TDevice;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectImageActivity extends BaseActivity {
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final int MAX_SIZE = 9;
    private static final int PERMISSION_REQUEST_CODE = 88;

    @Bind(R.id.tv_back)
    ImageView mTvBack;
    @Bind(R.id.tv_ok)
    TextView mTvSelectCount;
    @Bind(R.id.rv)
    RecyclerView mRvImage;
    private boolean mHasCamera = false;
    //被选中图片的集合
    private List<Image> mSelectedImages = new ArrayList<>();
    private List<Image> mImages = new ArrayList<>();
    private ImageAdapter mImageAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        ButterKnife.bind(this);
        setupSelectedImages();
        mRvImage.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false));
        mRvImage.addItemDecoration(new SpaceGridItemDecoration((int) TDevice.dipToPx(getResources(), 1)));
        //异步加载图片
        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
    }

    private void setupSelectedImages() {
        Intent intent = getIntent();
        ArrayList<Image> selectImages = intent.getParcelableArrayListExtra("selected_images");
        mSelectedImages.addAll(selectImages);
    }


    @OnClick({R.id.tv_back, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_ok:
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(EXTRA_RESULT, (ArrayList<? extends Parcelable>) mSelectedImages);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }





    /*************************************异步加载相册图片************************************************/

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        //创建一个CursorLoader，去异步加载相册的图片
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                ArrayList<Image> images = new ArrayList<>();
                //是否显示照相图片
                if (mHasCamera) {
                    //添加到第一个的位置（默认）
                    images.add(new Image());
                }
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);
                        images.add(image);
                        //如果是被选中的图片
                        if (mSelectedImages.size() > 0) {
                            for (Image i : mSelectedImages) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }
                        //设置图片分类的文件夹
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                    } while (data.moveToNext());
                }
                addImagesToAdapter(images);
                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedImages.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImages) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImages.removeAll(rs);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    private void addImagesToAdapter(ArrayList<Image> images) {
        mImages.clear();
        mImages.addAll(images);
        if (mImageAdapter == null) {
            mImageAdapter = new ImageAdapter(this, mImages, mSelectedImages);
            mRvImage.setAdapter(mImageAdapter);
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
        mImageAdapter.setSelectImageCountListener(mOnSelectImageCountListener);
    }
    private int max=0;
    /*************************************已选择的图片回调的方法************************************************/
    private ImageAdapter.onSelectImageCountListener mOnSelectImageCountListener = new ImageAdapter.onSelectImageCountListener() {
        @Override
        public void onSelectImageCount(int count) {
        }

        @Override
        public void onSelectImageList(List<Image> images) {
            max=0;
            mSelectedImages = images;
            for (int i=0;i<mSelectedImages.size();i++){
                if (mSelectedImages.get(i).isTake()){
                    max=max+1;
                }
            }
            if (mSelectedImages.size()>max) {
                if (max==0) {
                    mTvSelectCount.setText("完成(" + (mSelectedImages.size()) + "/" + (6 - max) + ")");
                }else {
                    mTvSelectCount.setText("完成(" + (mSelectedImages.size() - max) + "/" + (6 - max) + ")");
                }
            }else {
                mTvSelectCount.setText("完成");
            }
        }
    };

}
