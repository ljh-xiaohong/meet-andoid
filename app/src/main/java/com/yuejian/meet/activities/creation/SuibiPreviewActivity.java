package com.yuejian.meet.activities.creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.youth.banner.Banner;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.ui.GlideImageLoader;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/5/27 17:47
 * @desc : 预览随笔Activity
 */
public class SuibiPreviewActivity extends BaseActivity {

    @Bind(R.id.iv_preview_suibi_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.tv_preview_suibi_submit)
    TextView mReleaseBtn;
    @Bind(R.id.banner_suibi_preview)
    Banner mBannerView;
    @Bind(R.id.tv_suibi_preview_title)
    TextView mTitleView;
    @Bind(R.id.tv_suibi_preview_content)
    TextView mContentView;
    @Bind(R.id.tv_suibi_preview_tag)
    TextView mTagView;

    private LoadingDialogFragment mLoadingDialog;

    private JSONArray imgUrlJsonArray;
    private int pudateCnt = 0;

    private String mTitle;
    private String mContent;
    private String mLabelId;
    private List<PhotoInfo> mPhotoInfoList;
    private String mIsReprint;
    private String mIsComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suibi_preview);

        Intent intent = getIntent();
        initData(intent);
    }

    private void initData(Intent intent) {
        mLoadingDialog = LoadingDialogFragment.newInstance(getString(R.string.Is_released));

        if (intent != null) {
            mTitle = intent.getStringExtra("title");
            mContent = intent.getStringExtra("content");
            mLabelId = intent.getStringExtra("label_id");
            String labelText = intent.getStringExtra("label_text");
            mPhotoInfoList = (List<PhotoInfo>) intent.getSerializableExtra("photo_list");
            mIsReprint = intent.getStringExtra("is_reprint");
            mIsComment = intent.getStringExtra("is_comment");

            mTitleView.setText(mTitle);
            mContentView.setText(mContent);
            mTagView.setText(String.format("#%s", labelText));
            //
            mBannerView.setImageLoader(new GlideImageLoader());
            mBannerView.setImages(mPhotoInfoList);
            mBannerView.start();
        }
    }

    @OnClick({R.id.iv_preview_suibi_back_btn, R.id.tv_preview_suibi_submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.iv_preview_suibi_back_btn:
                finish();
                break;
            case R.id.tv_preview_suibi_submit:
                imgUrlJsonArray = new JSONArray();
                if (mLoadingDialog != null)
                    mLoadingDialog.show(getFragmentManager(), "");
                if (mPhotoInfoList.size() > 0) { //上传图片
                    for (int i = 0; i < mPhotoInfoList.size(); i++) {
                        updateUrl(i, mPhotoInfoList.get(i).getAbsolutePath());
                    }
                } else {
                    releaseSuibi();
                }
                break;
        }
    }

    /**
     * 更新已上传图片url串
     * @param index 顺序
     * @param url 本地图片路径
     */
    private void updateUrl(int index, String url) {
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

        JSONObject object = new JSONObject();
        object.put("index", index);
        object.put("url", OssUtils.getOssUploadingUrl(updateUrl));
        imgUrlJsonArray.add(object);
        updateUserImg(bitmap2File, updateUrl);
    }

    /**
     * 上传视频跟图片到oss
     */
    private void updateUserImg(String path, String ossUpdateUrl) {
        if (!path.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(path, ossUpdateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (mPhotoInfoList.size() > 0) {
                        pudateCnt += 1;
                        if (mPhotoInfoList.size() == pudateCnt) {
                            releaseSuibi();
                        }
                    }
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

    private void releaseSuibi() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("title", mTitle);
        params.put("content", mContent);
        params.put("photo_and_video_url", imgUrlJsonArray.toJSONString());
        params.put("label_id", mLabelId);
        params.put("is_reprint", mIsReprint);
        params.put("is_comment", mIsComment);

        apiImp.insertEssay(params, this, new DataIdCallback<String>() {
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
}
