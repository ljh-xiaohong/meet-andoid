package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.adapters.ReleaseActionAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发布动态
 */
/**
 * @author :
 * @time   : 2018/11/27 16:56
 * @desc   : 发布动态
 * @version: V1.0
 * @update : 2018/11/27 16:56
 */

public class ReleaseActionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    @Bind(R.id.txt_release_content)
    TextView txt_release_content;
    @Bind(R.id.txt_release_location)
    TextView txt_release_location;
    @Bind(R.id.release_gridveiw)
    GridView release_gridveiw;
    @Bind(R.id.release_emo)
    ImageView release_emo;
    @Bind(R.id.release_photograph)
    ImageView release_photograph;
    @Bind(R.id.release_img)
    ImageView release_img;
    @Bind(R.id.release_location)
    ImageView release_location;
    @Bind(R.id.rl_release_video_layout)
    RelativeLayout video_layout;
    @Bind(R.id.video_img)
    ImageView video_img;
    @Bind(R.id.location_layout)
    RelativeLayout location_layout;
    ImageView img_imgadd_item;
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    View viewFooter;
    List<PhotoInfo> photosList = new ArrayList<>();
    ReleaseActionAdapter2 mAdpter2;
    private LoadingDialogFragment dialog;
    ApiImp api = new ApiImp();
    Bitmap bitmap;
    String videoUrl = "";
    String AdName = "", ProvinceName = "", CityName = "", Snippet = "", LatLonPoint = "", locationTitle = "";
    Intent intent;
    private Boolean isCompile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_action);
        initData();
    }

    public void initData() {
        dialog = LoadingDialogFragment.newInstance(getString(R.string.Is_released));
        txt_titlebar_release.setVisibility(View.VISIBLE);
        setTitleText(getString(R.string.title_send_dynamic));
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isCompile = true;
            if (bundle.containsKey("videoUrl")) {
                videoUrl = bundle.getString("videoUrl");
                video_layout.setVisibility(View.VISIBLE);
                release_gridveiw.setVisibility(View.GONE);
                bitmap = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Video.Thumbnails.MINI_KIND);
                File file = Utils.saveBitmap(bitmap, System.currentTimeMillis() + "");
                video_img.setImageBitmap(Utils.loadBitmap(file.getAbsolutePath(), true));
                release_photograph.setVisibility(View.GONE);
                release_img.setVisibility(View.GONE);
            }
            if (bundle.containsKey("photosList")) {
                photosList = (List<PhotoInfo>) bundle.getSerializable("photosList");
            }
            if (bundle.containsKey("photo")) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setFilePath(bundle.getString("photo"));
                photoInfo.setAbsolutePath(bundle.getString("photo"));
                photosList.add(photoInfo);
            }
        }
        viewFooter = View.inflate(this, R.layout.item_addimg_release_finally, null);
        img_imgadd_item = (ImageView) viewFooter.findViewById(R.id.img_imgadd_item);
        img_imgadd_item.setOnClickListener(this);
        mAdpter2 = new ReleaseActionAdapter2(this, photosList);
        release_gridveiw.setAdapter(mAdpter2);
//        release_gridveiw.addFooterView(video_img);
        mAdpter2.notifyDataSetChanged();
        release_gridveiw.setOnItemClickListener(this);

    }

    /**
     * 删除选择图片
     *
     * @param index
     */
    public void delPhotos(int index) {
        photosList.remove(index);
        mAdpter2.refresh(photosList);
    }

    @OnClick({R.id.release_emo, R.id.release_photograph, R.id.release_img, R.id.release_location, R.id.txt_titlebar_release
            , R.id.del_location, R.id.video_img, R.id.titlebar_imgBtn_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_emo://表情

                break;
            case R.id.del_location:///删除位置
                location_layout.setVisibility(View.GONE);
                txt_release_location.setText(R.string.Google_Maps);
                LatLonPoint = "";
                break;
            case R.id.release_photograph://拍照
                showSelector(false);
                break;
            case R.id.release_img://选择图片
                showSelector(true);
                break;
            case R.id.img_imgadd_item://选择图片
                showSelector(true);
                break;
            case R.id.release_location://定位
                intent = new Intent(this, NearbyLocationActivity.class);
                startActivityIfNeeded(intent, 112);
                break;
            case R.id.txt_titlebar_release://发布
                imgUrlArr = "";
                if (txt_release_content.getText().toString().trim().equals("")) {
                    ViewInject.shortToast(getApplicationContext(), R.string.Content_cannot_be_empty);
                    return;
                }
                if (dialog != null)
                    dialog.show(getFragmentManager(), "");
                if (photosList.size() > 0) {//上传图片
                    for (int i = 0; i < photosList.size(); i++) {
                        updateUrl(photosList.get(i).getAbsolutePath());
                    }
                } else if (!videoUrl.equals("")) {///上传视频
                    updateVideo();
                } else {
                    posetRequstAction();
                }
                break;
            case R.id.video_img:
                Intent intent = new Intent(this, VideoViewActivity.class);
                intent.putExtra(Constants.VIDEO_URL, videoUrl);
                intent.putExtra("localVideoUrl", true);
                startActivity(intent);
                break;
            case R.id.titlebar_imgBtn_back:
                isFinish();
                break;
        }
    }

    String outputPath = "";

    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {
        if (photosList.size() >= 9) {
            ViewInject.shortToast(getApplication(), "最多只能选择9张图片");
            return;
        }
        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, OPENPIC, from, outputPath, true,
                    9 - photosList.size(), true, false, 0, 0);//不截图
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, OPENCAM, from, outputPath, false, 1,
                    true, false, 0, 0);//不截图
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPENPIC://相册选择照片返回
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            photosList.addAll(photosInfo);
                            mAdpter2.notifyDataSetChanged();
                        }
                        isCompile = true;
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }
                    return;
                }

                break;
            case OPENCAM://拍完照返回
                if (resultCode == RESULT_OK) {//outputPath
                    PhotoInfo entity = new PhotoInfo();
                    entity.setFilePath(outputPath);
                    entity.setAbsolutePath(outputPath);
                    photosList.add(entity);
                    mAdpter2.refresh(photosList);
                    isCompile = true;
                    return;
                }
                break;
            case CROP_PIC:
                if (resultCode != RESULT_OK) {
                    return;
                }
                break;
            case 112:
                if (resultCode == RESULT_OK) {
                    AdName = data.getStringExtra("AdName");
                    ProvinceName = data.getStringExtra("ProvinceName");
                    CityName = data.getStringExtra("CityName");
                    Snippet = data.getStringExtra("Snippet");
                    LatLonPoint = data.getStringExtra("LatLonPoint");
                    locationTitle = data.getStringExtra("locationTitle");
                    txt_release_location.setText(" " + locationTitle);
                    location_layout.setVisibility(View.VISIBLE);
                    isCompile = true;
                }
                break;
        }
    }

    String imgUrlArr = "";
    int pudateCnt = 0;
    int pudateVideoCnt = 0;

    //上传动态图片
    public void updateUrl(String rul) {
        String updateUrl = "";
        String bitmap2File = rul;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }
        if (!imgUrlArr.equals("")) {
            imgUrlArr = imgUrlArr + "," + OssUtils.getOssUploadingUrl(updateUrl);
        } else {
            imgUrlArr += OssUtils.getOssUploadingUrl(updateUrl);
        }
        updateUserImg(bitmap2File, updateUrl);
    }

    String videoImgUrl = "";

    /**
     * 上传视频
     */
    public void updateVideo() {
        String updateUrl = "";
        String bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmap, this);
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeVideoHorizontal();
            videoImgUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeVideo();
            videoImgUrl = OssUtils.getTimeNmaeJpg();
        }
        imgUrlArr += OssUtils.getOssUploadingUrl(updateUrl);
        updateUserImg(videoUrl, updateUrl);//视频
        updateUserImg(bitmap2File, videoImgUrl);//图片
        videoImgUrl = OssUtils.getOssUploadingUrl(videoImgUrl);
    }

    /**
     * 上传视频跟图片到oss
     */
    public void updateUserImg(String Path, String ossUpdateUrl) {
        if (!Path.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(Path, ossUpdateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (photosList.size() > 0) {
                        pudateCnt += 1;
                        if (photosList.size() == pudateCnt) {
                            posetRequstAction();
                        }
                    }
                    if (!videoUrl.equals("")) {
                        pudateVideoCnt += 1;
                        if (pudateVideoCnt == 2) {
                            posetRequstAction();
                        }
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
    }

    /**
     * 提交--
     */
    public void posetRequstAction() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.getCustomer_id());
        params.put("action_title", txt_release_content.getText().toString());
        params.put("action_url", imgUrlArr);//视频或图片连接
        params.put("url_type", videoUrl.equals("") ? "1" : "2");//0:没有链接, 1:图片, 2:视频
        params.put("video_first_url", videoImgUrl);//视频图片
        String[] coordinate = LatLonPoint.split(",");
        if (coordinate.length > 1) {
            params.put("longitude", coordinate[0]);//经度
            params.put("latitude", coordinate[1]);//纬度
            params.put("province", ProvinceName);//省
            params.put("city", CityName);//市
            params.put("area", locationTitle);//区
        }
        api.createAction(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(getApplicationContext(), R.string.release_success);
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == photosList.size()) {
            showSelector(true);
        }
    }


    @Override
    public void onBackPressed() {
        if (Utils.isAnyWindowsIsShowing()) {
            Utils.dismissAnyWindows();
        } else {
            isFinish();
        }
    }

    public void isFinish() {
        if (!StringUtil.isEmpty(txt_release_content.getText().toString()) || isCompile) {
            Utils.showNoTitleDialog(this, getString(R.string.Make_sure_to_abandon_the_editor), getString(R.string.confirm), getString(R.string.cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }, null);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void finish() {
        Intent intent  = new Intent();
        intent.putExtra("release_action", true);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
