package com.yuejian.meet.activities.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.PicAdapter;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.GifActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.DynamicPrivatePicBean;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.ImageUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class ReleaseActivity extends BaseActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.content)
    EmojiconEditText content;
    @Bind(R.id.pic_list)
    RecyclerView picList;
    @Bind(R.id.emoji)
    ImageView emoji;
    @Bind(R.id.pic)
    ImageView pic;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.tv_counts)
    TextView tvCounts;
    @Bind(R.id.count_lay)
    LinearLayout countLay;
    @Bind(R.id.emojicons)
    FrameLayout emojicons;
    @Bind(R.id.post)
    TextView post;
    private boolean hasClick;
    private PicAdapter mAdapter;
    private String photoAndVideoUrl="";
    private LoadingDialogFragment dialog;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                    crLatitude= aMapLocation.getLatitude();//获取纬度
                    crLongitude=aMapLocation.getLongitude();//获取经度
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    private double crLongitude=0.0;
    private double crLatitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_activity);
        ButterKnife.bind(this);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        Image pb = new Image();
        pb.setBitmap(null);
        pb.setThumbPath("");
        pb.setUrl("");
        pb.setPath("");
        mSelectImages.add(pb);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        picList.setLayoutManager(layoutManager);
        picList.setAdapter(mAdapter = new PicAdapter(ReleaseActivity.this, mSelectImages, true));
        mAdapter.setClickPic(new PicAdapter.OnClickPic() {
            @Override
            public void clickPic(int position) {
                if (CommonUtil.isNull(mSelectImages.get(position).getPath())) {
                    try {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ReleaseActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                    }
                    initPopwindow();
                } else {
                    List<DynamicPrivatePicBean> actionPhoto = new ArrayList<>();
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < mSelectImages.size(); i++) {
                        actionPhoto.add(new DynamicPrivatePicBean(mSelectImages.get(i).getPath(), "1"));
                        list.add(mSelectImages.get(i).getPath());
                    }
                    Utils.displayImages(ReleaseActivity.this, list, position, null);
                }
            }

            @Override
            public void delectPic(int position) {
                mSelectImages.remove(position);
                if (!CommonUtil.isNull(mSelectImages.get(mSelectImages.size() - 1).getPath()) && mSelectImages.size() == 5) {
                    Image pb = new Image();
                    pb.setBitmap(null);
                    pb.setUrl("");
                    pb.setPath("");
                    mSelectImages.add(mSelectImages.size(), pb);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        backBtn.setOnClickListener(v -> finish());
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCounts.setText(s.length() + "");
                if (s.length() > 200) {
                    tvCounts.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    tvCounts.setTextColor(getResources().getColor(R.color.black9));
                }
            }
        });
        setEmojiconFragment(false);
    }

    private View popupView;
    private PopupWindow window;

    private void initPopwindow() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        popupView = inflater.inflate(R.layout.popupwindow, null);
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        stateWindow.getBackground().setAlpha(10);
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
//        //添加pop窗口关闭事件
        window.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        popupView.findViewById(R.id.bg).setOnClickListener(v -> window.dismiss());
        popupView.findViewById(R.id.cancel).setOnClickListener(v -> window.dismiss());
        TextView photo = popupView.findViewById(R.id.report);
        photo.setText("拍照");
        photo.setVisibility(View.VISIBLE);
        photo.setOnClickListener(v -> {
            int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (isPermission == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        });
        TextView selectImage = popupView.findViewById(R.id.delect);
        selectImage.setText("相册");
        selectImage.setVisibility(View.VISIBLE);
        selectImage.setOnClickListener(v -> selectImage());
    }

    private static final int TAKE_PHOTO = 99;
    /**
     * 当前选择的图片的路径
     */
    public String mImagePath;

    /**
     * 检查相机权限，如果不能打开相机则抛出异常
     */
    public static void checkCameraPermissions() throws IOException {
        try {
            Camera camera = Camera.open();
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            throw new IOException();
        }
    }

    private void takePhoto() {
        //检查相机权限
        try {
            checkCameraPermissions();
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("照相权限被禁止，无法使用该功能!");
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();

        }
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            /**
             * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
             */
            //获取与应用相关联的路径
            String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            //根据当前时间生成图片的名称
            String timestamp = "/" + formatter.format(new Date()) + ".jpg";
            File imageFile = new File(imageFilePath, timestamp);// 通过路径创建保存文件
            mImagePath = imageFile.getAbsolutePath();
            Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
                Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int SELECT_IMAGE_REQUEST = 0x0011;
    private ArrayList<Image> mSelectImages = new ArrayList<>();

    private void selectImage() {
        int isPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int isPermission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (isPermission1 == PackageManager.PERMISSION_GRANTED && isPermission2 == PackageManager.PERMISSION_GRANTED) {
            startActivity();
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void startActivity() {
        Intent intent = new Intent(this, SelectImageActivity.class);
        intent.putParcelableArrayListExtra("selected_images", mSelectImages);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            window.dismiss();
            if (requestCode == SELECT_IMAGE_REQUEST && data != null) {
                ArrayList<Image> selectImages = data.getParcelableArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                mSelectImages.clear();
                mSelectImages.addAll(selectImages);
                if (selectImages.size() < 6) {
                    Image pb = new Image();
                    pb.setBitmap(null);
                    pb.setThumbPath("");
                    pb.setUrl("");
                    pb.setPath("");
                    mSelectImages.add(pb);
                }
                mAdapter.notifyDataSetChanged();
            } else if (requestCode == TAKE_PHOTO) {
                String imagePath = "";
                Uri uri = null;
                if (data != null && data.getData() != null) {// 有数据返回直接使用返回的图片地址
                    uri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null,
                            null, null);
                    if (cursor == null) {
                        uri = ImageUtils.getUri(this, data);
                    }
                    imagePath = ImageUtils.getFilePathByFileUri(this, uri);
                } else {// 无数据使用指定的图片路径
                    imagePath = mImagePath;
                }
                if (mSelectImages.size() < 6) {
                    Image pb = new Image();
                    pb.setBitmap(null);
                    pb.setUrl("");
                    pb.setSelect(true);
                    pb.setTake(true);
                    pb.setPath(imagePath);
                    mSelectImages.add(mSelectImages.size() - 1, pb);
                } else {
                    Image pb = new Image();
                    pb.setBitmap(null);
                    pb.setUrl("");
                    pb.setSelect(true);
                    pb.setTake(true);
                    pb.setPath(imagePath);
                    mSelectImages.add(mSelectImages.size() - 1, pb);
                    mSelectImages.remove(mSelectImages.size() - 1);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    public ApiImp apiImp = new ApiImp();
    @OnClick({R.id.emoji, R.id.pic, R.id.content,R.id.post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.emoji:
                if (hasClick) {
                    emojicons.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(content, 0); //强制隐藏键盘
                    hasClick = false;
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(content.getWindowToken(), 0); //强制隐藏键盘
                    emojicons.setVisibility(View.VISIBLE);
                    hasClick = true;
                }
                break;
            case R.id.post:
                if (dialog != null)
                    dialog.show(getFragmentManager(), "");

                //说明：customerId：用户id，type：类型动态固定为1，title：动态标题，
                // content：动态内容，labelId：标签id（动态为空串），photoAndVideoUrl：
                // 动态图片资源地址（多个用逗号分隔），crLongitude：经度，crLatitude：纬度 crdId：动态固定给空串
                if (CommonUtil.isNull(content.getText().toString())){
                    ViewInject.shortToast(ReleaseActivity.this,R.string.content_tips);
                    return;
                }
                if (CommonUtil.isNull(mSelectImages.get(0).getPath())){
                    commit();
                    return;
                }
                //上传图片
                for (int i=0;i<mSelectImages.size();i++){
                    if (!CommonUtil.isNull(mSelectImages.get(i).getPath())){
                        mSelectImagesSize++;
                        uploadUserImg(mSelectImages.get(i).getPath());
                    }
                }
                break;
            case R.id.pic:
                initPopwindow();
                break;
            case R.id.content:
                emojicons.setVisibility(View.GONE);
                hasClick = false;
                break;
        }
    }

    String photo  = "";
    int uploadCount=0;
    int mSelectImagesSize=0;
    /**
     * 上传图片到oss 完成
     */
    private void uploadUserImg(String params) {
        //如果存在头像，先上存到oss
        String updtaImgName = OssUtils.getTimeNmaeJpg();
        new FeedsApiImpl().upLoadImageFileToOSS(params, updtaImgName, this, new DataCallback<FeedsResourceBean>() {
            @Override
            public void onSuccess(FeedsResourceBean data) {
                uploadCount++;
                photo = data.imageUrl;
                photoAndVideoUrl=photoAndVideoUrl+photo+",";
                if (uploadCount==mSelectImagesSize) {
                    commit();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (dialog != null)
                    dialog.dismiss();
                ViewInject.shortToast(getApplication(), errMsg);
            }
        });


    }

    private void commit() {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("type", 1);
        map.put("title", "");
        map.put("content", content.getText().toString());
        map.put("labelId", "");
        if (!CommonUtil.isNull(photoAndVideoUrl)) {
            map.put("photoAndVideoUrl", photoAndVideoUrl.substring(0, photoAndVideoUrl.length() - 1));
        } else {
            map.put("photoAndVideoUrl", photoAndVideoUrl);
        }
        map.put("crLongitude", crLongitude);
        map.put("crLatitude", crLatitude);
        map.put("crdId", "");
        apiImp.postCreateAction(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                ResultBean resultBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(ReleaseActivity.this, resultBean.getMessage());
                if (resultBean.getCode() == 0) {
                    Intent i = new Intent();
                    setResult(7, i);
                    finish();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(content, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(content);
    }

    @Override
    public void onBackPressed() {
        if (hasClick) {
            emojicons.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(content, 0); //强制隐藏键盘
            hasClick = false;
        } else {
            super.onBackPressed();
        }
    }
}
