package com.yuejian.meet.activities.creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.TypeEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.MediaItemView;


import org.json.JSONArray;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/5/27 11:44
 * @desc : 编辑文章
 */
public class ArticleEditActivity extends BaseActivity implements MediaItemView.OnViewClickListener {

    @Bind(R.id.iv_edit_article_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.tv_edit_article_submit)
    TextView mSubmitBtn;
    @Bind(R.id.ll_edit_article_content)
    LinearLayout mContentView;
    @Bind(R.id.ll_add_image_edit_article)
    LinearLayout mAddImageBtn;
    @Bind(R.id.ll_add_text_edit_article)
    LinearLayout mAddTextBtn;
    @Bind(R.id.ll_add_video_edit_article)
    LinearLayout mAddVideoBtn;
    @Bind(R.id.ll_add_music_edit_article)
    LinearLayout mAddMusicBtn;
    @Bind(R.id.ll_preview_edit_article)
    LinearLayout mPreviewBtn;
    @Bind(R.id.et_article_edit_title)
    EditText mTitleView;
    @Bind(R.id.iv_add_article_cover)
    ImageView mAddArticleCoverBtn;
    @Bind(R.id.edit_article_layout_label)
    RadioGroup mRadioGroupLabel;
    private static final int OPENPIC = 100;
    private static final int OPENCAM = 200;
    private static final int OPENVIDEO = 300;
    private static final int OPENMUSIC = 400;
    private static final int REPLACEVIEW_PIC = 500;
    private static final int REPLACEVIEW_VIDEO = 600;

    private LoadingDialogFragment mLoadingDialog;
    private volatile int pudateCnt = 0;
    private String lab = "";

    //作为替换时的锚 处理结束时，必须设置为null；
    private MediaItemView replaceObject = null;

    private Map<Integer, MediaItemView> itemViews = new HashMap<>();
    private Map<Integer, String> mMediaUrlMap = new HashMap<>();
    private Map<Integer, String> mMediaUrlMapImg = new HashMap<>();
    private int mItemId = 0;
    private String coverImageUrl = "";

    private static final int offset_dp = 12;
    private int offset_px;
    private int videoViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);

        offset_px = Utils.dp2px(getApplicationContext(), offset_dp);
        videoViewHeight = Utils.dp2px(getApplicationContext(), 200);
        mLoadingDialog = LoadingDialogFragment.newInstance("正在上传...");
        getDataFromNet();
        setListener();
    }

    @OnClick({R.id.iv_edit_article_back_btn, R.id.ll_add_image_edit_article, R.id.ll_add_text_edit_article,
            R.id.ll_add_video_edit_article, R.id.ll_add_music_edit_article, R.id.ll_preview_edit_article,
            R.id.tv_edit_article_submit, R.id.iv_add_article_cover})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_edit_article_back_btn:
                finish();
                break;
            case R.id.ll_add_image_edit_article:
                showSelector(true, OPENPIC);
                break;
            case R.id.ll_add_text_edit_article:
                MediaItemView textItemView = new MediaItemView(this, MediaItemView.MEDIA_TEXT, mItemId, this);
                LinearLayout.LayoutParams textItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textItemViewParams.setMargins(offset_px, offset_px * 2, offset_px, offset_px);
                itemViews.put(mItemId++, textItemView);
                mContentView.addView(textItemView, textItemViewParams);
                break;
            case R.id.ll_add_video_edit_article:
                PictureSelector.create(ArticleEditActivity.this).openGallery(PictureMimeType.ofVideo()).maxSelectNum(1).isCamera(false).forResult(OPENVIDEO);
                break;
            case R.id.ll_add_music_edit_article:
                PictureSelector.create(ArticleEditActivity.this).openGallery(PictureMimeType.ofAudio()).maxSelectNum(1).forResult(OPENMUSIC);
                break;
            case R.id.ll_preview_edit_article:
                break;
            case R.id.tv_edit_article_submit:
                if (!TextUtils.isEmpty(mTitleView.getText().toString()) && itemViews.size() > 0) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.show(getFragmentManager(), "");
                    convertArticleContent();
                } else {
                    ViewInject.shortToast(this, "请填写内容");
                }
                break;
            case R.id.iv_add_article_cover:
                String outputPath = ImgUtils.imgTempFile();
                int from = PickImageActivity.FROM_LOCAL;
                PickImageActivity.start(this, 404, from, outputPath, true, 1,
                        true, false, 0, 0);
                break;
            default:
                break;
        }
    }

    private void setListener() {
        mRadioGroupLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (R.id.edit_label_init == checkedId) {
                    lab = "";
                    return;
                }
                RadioButton rb = radioGroup.findViewById(checkedId);
                lab = ((TypeEntity) rb.getTag()).getId() + "";

            }
        });
    }

    /**
     * 添加标签
     */
    private void getDataFromNet() {

        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);

        apiImp.getContentLabel(params, this, new DataIdCallback<String>() {

            List<TypeEntity> typeEntities;

            @Override
            public void onSuccess(String data, int id) {


                if (data != null) {
                    JSONObject jo = (JSONObject) JSON.parse(data);
                    String code = jo.getString("code");
                    if (code != null && code.equalsIgnoreCase("0")) {
                        typeEntities = JSON.parseArray(jo.getString("data"), TypeEntity.class);
                        if (typeEntities != null && typeEntities.size() > 0) {

                            for (TypeEntity label : typeEntities) {
                                RadioButton radioButton = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.radiobutton_label, null);
                                radioButton.setText(label.getTitle());
                                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                lp.leftMargin = 10;
                                radioButton.setLayoutParams(lp);
                                radioButton.setTag(label);
                                mRadioGroupLabel.addView(radioButton);
                            }


                        }


                    }
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {


            }
        });
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen, int replace) {
        String outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, replace, from, outputPath, true, 1,
                    true, false, 0, 0);
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, replace, from, outputPath, false, 1,
                    true, false, 0, 0);
        }
    }

    private void savePic(Intent intent) {
        List<PhotoInfo> photosInfos = PickerContract.getPhotos(intent);
        if (photosInfos.size() > 0) {
            PhotoInfo photo = photosInfos.get(0);
            MediaItemView imageItemView = new MediaItemView(this, MediaItemView.MEDIA_IMAGE, mItemId, this);
            LinearLayout.LayoutParams imageItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth(this) / 5 * 3);
            imageItemViewParams.setMargins(offset_px, offset_px + 5, offset_px, offset_px / 3);
            imageItemView.setImageContentView(photo.getAbsolutePath());
            // 上传图片
            if (mLoadingDialog != null)
                mLoadingDialog.show(getFragmentManager(), "");
            updateImageUrl(mItemId, photo.getAbsolutePath());

            // 加入界面
            itemViews.put(mItemId++, imageItemView);
            mContentView.addView(imageItemView, imageItemViewParams);
        }
    }

    private void replacePic(Intent intent, MediaItemView imageItemView) {
        List<PhotoInfo> photosInfos = PickerContract.getPhotos(intent);
        if (photosInfos.size() > 0) {
            PhotoInfo photo = photosInfos.get(0);
            imageItemView.setImageContentView(photo.getAbsolutePath());
            // 上传图片
            if (mLoadingDialog != null)
                mLoadingDialog.show(getFragmentManager(), "");
            //更新item显示，和及时更新url map中的地址
            updateImageUrl(imageItemView.getmItemId(), photo.getAbsolutePath());
            imageItemView.invalidate();
        }
    }

    private void saveVideo(Intent data) {
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (selectList != null && selectList.size() > 0) {
            System.out.println("-----------------------------");
            System.out.println(selectList.get(0).getPath());

            MediaItemView videoItemView = new MediaItemView(this, MediaItemView.MEDIA_VIDEO, mItemId, this);
            LinearLayout.LayoutParams videoItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoViewHeight);
            videoItemViewParams.setMargins(offset_px, offset_px + 5, offset_px, offset_px / 3);
            videoItemView.setVideoContentView(selectList.get(0).getPath());
            // 上传视频
            if (mLoadingDialog != null)
                mLoadingDialog.show(getFragmentManager(), "");
            updateVideoUrl(mItemId, selectList.get(0).getPath());

            String imgSize = "";

            System.out.println("{" + selectList.get(0).getWidth() + "," + selectList.get(0).getHeight() + "}");
            imgSize = "{" + selectList.get(0).getWidth() + "," + selectList.get(0).getHeight() + "}";

            mMediaUrlMapImg.put(mItemId, imgSize);

            // 加入界面
            itemViews.put(mItemId, videoItemView);
            mItemId = mItemId + 1;
            mContentView.addView(videoItemView, videoItemViewParams);
        }
    }

    private void replaceVideo(Intent data, MediaItemView videoItemView) {
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (selectList != null && selectList.size() > 0) {
            System.out.println("-----------------------------");
            System.out.println(selectList.get(0).getPath());


            videoItemView.setVideoContentView(selectList.get(0).getPath());
            // 上传视频
            if (mLoadingDialog != null)
                mLoadingDialog.show(getFragmentManager(), "");
            updateVideoUrl(videoItemView.getmItemId(), selectList.get(0).getPath());

            String imgSize = "";

            System.out.println("{" + selectList.get(0).getWidth() + "," + selectList.get(0).getHeight() + "}");
            imgSize = "{" + selectList.get(0).getWidth() + "," + selectList.get(0).getHeight() + "}";

            mMediaUrlMapImg.put(videoItemView.getmItemId(), imgSize);

            // 更新界面
            videoItemView.invalidate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPENPIC: // 相册选择照片返回
                if (resultCode == RESULT_OK) {
                    try {
                        savePic(data);
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }

                }
                break;

            case REPLACEVIEW_PIC://图片转换
                if (resultCode == RESULT_OK && null != replaceObject) {
                    replacePic(data, replaceObject);

                }
                replaceObject = null;
                break;

            case REPLACEVIEW_VIDEO:
                if (resultCode == RESULT_OK && null != replaceObject) {
                    replaceVideo(data, replaceObject);

                }
                replaceObject = null;
                break;

            case OPENCAM: // 相机拍照返回
                break;

            case OPENVIDEO:
                if (resultCode == RESULT_OK) {
                    saveVideo(data);
                }
                break;
            case 404: //选择封面
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfos = PickerContract.getPhotos(data);
                        if (photosInfos.size() > 0) {
                            PhotoInfo photo = photosInfos.get(0);
                            // 上传图片
                            if (mLoadingDialog != null)
                                mLoadingDialog.show(getFragmentManager(), "");
                            updateCoverImageUrl(photo.getAbsolutePath());

                            Glide.with(mContext).load(photo.getAbsolutePath()).into(mAddArticleCoverBtn);
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }

                }
                break;

        }
    }


    /**
     * 更新封面
     *
     * @param absolutePath
     */
    private void updateCoverImageUrl(String absolutePath) {
        String updateUrl = "";
        String bitmap2File = absolutePath;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }

        coverImageUrl = OssUtils.getOssUploadingUrl(updateUrl);
        if (!bitmap2File.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(bitmap2File, updateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), "上传完成");
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        }
    }

    /**
     * 更新已上传图片url串
     *
     * @param index 顺序
     * @param url   本地图片路径
     */
    public void updateImageUrl(int index, String url) {
        String updateUrl = "";
        String bitmap2File = url;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }

        System.out.println("image:" + url);
        String imgSize = "";
        try {
            FileInputStream fis = new FileInputStream(url);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            System.out.println("image:" + url + "-{" + bitmap.getWidth() + "." + bitmap.getHeight() + "}");
            imgSize = "{" + bitmap.getWidth() + "," + bitmap.getHeight() + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaUrlMapImg.put(index, imgSize);
        mMediaUrlMap.put(index, OssUtils.getOssUploadingUrl(updateUrl));
        updateUserImg(bitmap2File, updateUrl);
    }

    public void updateVideoUrl(int index, String url) {
        String updateUrl = OssUtils.getTimeNmaeVideo();

        mMediaUrlMap.put(index, OssUtils.getOssUploadingUrl(updateUrl));
        updateUserImg(url, updateUrl);
    }

    /**
     * 上传视频跟图片到oss
     */
    public void updateUserImg(String path, String ossUpdateUrl) {
        if (!path.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(path, ossUpdateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), "上传完成");
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        }
    }

    private void convertArticleContent() {
        try {
            JSONArray data = new JSONArray();

//            for (int i = 0; i < itemViews.size(); i++) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("index", i);
//                String mediaType = itemViews.get(i).getMediaType() == 1 ? "text" : itemViews.get(i).getMediaType() == 2 ? "image" : "video";
//                jsonObject.put("type", mediaType);
//                switch (mediaType) {
//                    case "text":
//                        jsonObject.put("content", itemViews.get(i).getEditText());
//                        break;
//                    case "image":
//                        jsonObject.put("content", mMediaUrlMap.get(i));
//                        jsonObject.put("imgSize", mMediaUrlMapImg.get(i));
//                        break;
//                    case "video":
//                        jsonObject.put("content", mMediaUrlMap.get(i));
//                        jsonObject.put("imgSize", mMediaUrlMapImg.get(i));
//                        break;
//                }
//                data.put(jsonObject);
//            }

            List<Map.Entry<Integer, MediaItemView>> list = new ArrayList<Map.Entry<Integer, MediaItemView>>(itemViews.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Integer, MediaItemView>>() {
                @Override
                public int compare(Map.Entry<Integer, MediaItemView> t1, Map.Entry<Integer, MediaItemView> t2) {
                    return t1.getKey().compareTo(t2.getKey());
                }

            });

            int i = 0;
            for (Map.Entry<Integer, MediaItemView> ma : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("index", i++);
//                Log.e("cchekc", ma.getKey() + "");
                String mediaType = ma.getValue().getMediaType() == 1 ? "text" : ma.getValue().getMediaType() == 2 ? "image" : "video";
                jsonObject.put("type", mediaType);
                jsonObject.put("type", mediaType);
                switch (mediaType) {
                    case "text":
                        jsonObject.put("content", ma.getValue().getEditText());
                        break;
                    case "image":
                        jsonObject.put("content", mMediaUrlMap.get(ma.getKey()));
                        jsonObject.put("imgSize", mMediaUrlMapImg.get(ma.getKey()));
                        break;
                    case "video":
                        jsonObject.put("content", mMediaUrlMap.get(ma.getKey()));
                        jsonObject.put("imgSize", mMediaUrlMapImg.get(ma.getKey()));
                        break;
                }
                data.put(jsonObject);
            }
//            Log.e("cchekc", data.toString());
//            releaseArticle(data.toString());
            releaseArticleNew(data.toString());
        } catch (Exception e) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            ViewInject.shortToast(getApplication(), "Error");
            e.printStackTrace();
        }
    }

    private void releaseArticleNew(String contentJson) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("type", 2);
        params.put("title", mTitleView.getText().toString());
        params.put("content", contentJson);
        params.put("labelId", lab);
        params.put("photoAndVideoUrl", coverImageUrl);
        params.put("crLongitude", AppConfig.slongitude);
        params.put("crLatitude", AppConfig.slatitude);
        params.put("crdId", "");
        apiImp.publishedArticlesNew(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), R.string.release_success);
                finish();

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), "发布失败");
            }
        });
    }

    private void releaseArticle(String contentJson) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("title", mTitleView.getText().toString());
        params.put("content", contentJson);
        params.put("label_id", "0");
        params.put("photo_and_video_url", coverImageUrl);
        params.put("is_reprint", "0");
        params.put("is_comment", "0");
        params.put("music_url", "");
        params.put("template_id", "");

        apiImp.publishedArticles(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), R.string.release_success);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), "发布失败");
            }
        });
    }

    @Override
    public void onRemoveViewClick(View v) {

        if (v instanceof MediaItemView) {
            MediaItemView mv = (MediaItemView) v;
            mContentView.removeView(mv);
            itemViews.remove(mv.getmItemId());
            mMediaUrlMap.remove(mv.getmItemId());
            switch (mv.getMediaType()) {
                //图片
                case MediaItemView.MEDIA_IMAGE:

                    break;

                //视频
                case MediaItemView.MEDIA_VIDEO:

                    break;
            }

        }


    }

    @Override
    public void onReplaceViewClick(View v) {

        if (v instanceof MediaItemView) {
            MediaItemView mv = (MediaItemView) v;
            switch (mv.getMediaType()) {
                //图片
                case MediaItemView.MEDIA_IMAGE:
                    replaceObject = mv;
                    showSelector(true, REPLACEVIEW_PIC);
                    break;
                //文字
                case MediaItemView.MEDIA_TEXT:


                    break;
                //视频
                case MediaItemView.MEDIA_VIDEO:
                    replaceObject = mv;
                    PictureSelector.create(ArticleEditActivity.this).openGallery(PictureMimeType.ofVideo()).maxSelectNum(1).isCamera(false).forResult(REPLACEVIEW_VIDEO);
                    break;
            }
        }
    }
}
