package com.yuejian.meet.activities.creation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.common.utils.DensityUtil;
import com.aliyun.svideo.editor.publish.CoverEditActivity;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunIThumbnailFetcher;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunThumbnailFetcherFactory;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.ShopEntity;
import com.yuejian.meet.bean.TypeEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.TagFlowLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class PulishActivity extends BaseActivity {

    @Bind(R.id.activity_publish_taglayout)
    TagFlowLayout mTagFlowLayout;

    @Bind(R.id.edit_label_init)
    CheckBox initTag;

    @Bind(R.id.activity_agree)
    CheckBox agree;

    @Bind(R.id.activity_publish_shop_select_layout)
    View shopLayout;

    @Bind(R.id.activity_publish_img_layout)
    View imgLayout;

    @Bind(R.id.activity_publish_img)
    ImageView img;

    @Bind(R.id.activity_publish_edittext)
    EditText mEditText;

    @Bind(R.id.activity_article_good_cancel)
    View cancel;

    @Bind(R.id.activity_publish_good_layout)
    View goodLayout;

    @Bind(R.id.activity_publish_shop_img)
    ImageView goodImg;

    @Bind(R.id.activity_publish_shop_name)
    TextView goodName;

    @Bind(R.id.activity_publish_shop_disct)
    TextView goodDisct;

    @Bind(R.id.activity_publish_shop_price_discount)
    TextView goodDiscount;

    @Bind(R.id.activity_publish_shop_price_full)
    TextView goodPrice;

    @Bind(R.id.activity_publish_publish_btn)
    View publish;

    @Bind(R.id.activity_publish_privacy)
    View privary;

    @Bind(R.id.activity_publish_protocol)
    View protocol;

    private LoadingDialogFragment mLoadingDialog;

    private ShopEntity good;

    private static final int SHOP_GOOD = 100;
    private static final int CROP_TITLE_PIC = 200;

    private String thumPic;

    private Intent intent;

    private String mOutputPath;

    /**
     * 视频缩略图截取，不同于MediaMetadataRetriever，可精准获取视频非关键帧图片
     */
    private AliyunIThumbnailFetcher aliyunIThumbnailFetcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_publish);
        getDataFromNet();
        initListener();
        mLoadingDialog = LoadingDialogFragment.newInstance("正在上传...");

        initData();
//        initVideo();
    }

    private void initData() {
        mOutputPath = getIntent().getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
    }

    private void initVideo() {
        aliyunIThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{mOutputPath}, new String[]{"video/mp4"}, null);
        aliyunIThumbnailFetcher.addVideoSource(mOutputPath, 0, Integer.MAX_VALUE, 0);
        aliyunIThumbnailFetcher.requestThumbnailImage(new long[]{0}, new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
            private int vecIndex = 1;
            private int mInterval = 100;

            @Override
            public void onThumbnailReady(Bitmap bitmap, long l) {
                if (bitmap != null && !bitmap.isRecycled()) {

                }
            }

            @Override
            public void onError(int i) {

            }
        });
    }

    @OnClick({R.id.activity_publish_shop_select_layout, R.id.edit_label_init, R.id.activity_article_good_cancel, R.id.activity_publish_img_layout, R.id.activity_publish_publish_btn, R.id.activity_publish_privacy, R.id.activity_publish_protocol})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_publish_shop_select_layout:
                ShopActivity.startActivityForResult(this, SHOP_GOOD);
                break;

            case R.id.edit_label_init:
                mTagFlowLayout.clearClick(v.getId());
                break;

            case R.id.activity_article_good_cancel:
                goodLayout.setVisibility(View.GONE);
                break;

            case R.id.activity_publish_img_layout:
                intent = new Intent(this, CoverEditActivity.class);
                intent.putExtra(CoverEditActivity.KEY_PARAM_VIDEO, mOutputPath);
                startActivityForResult(intent, CROP_TITLE_PIC);
                break;

            case R.id.activity_publish_publish_btn:
                publish();
                break;
            case R.id.activity_publish_privacy:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.PRIVACY);
                startActivity(intent);
                break;

            case R.id.activity_publish_protocol:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.USERGUIDE);
                startActivity(intent);
                break;
        }
    }

    /**
     * 发布视频
     */
    private void publish() {

        if (TextUtils.isEmpty(thumPic)) {
            ViewInject.shortToast(mContext, "请选择您的视频界面");
            return;
        }

        if (TextUtils.isEmpty(mEditText.getText().toString())) {
            ViewInject.shortToast(mContext, "请介绍您的视频");
            return;
        }

        if (!agree.isChecked()) {
            ViewInject.shortToast(mContext, "请阅读并同意《用户协议》《隐私政策》");
            return;
        }

        if (mLoadingDialog != null && !mLoadingDialog.isShowing) {
            mLoadingDialog.show(getFragmentManager(), "");

        }

        uploadAliyun();

    }

    public void initListener() {
        mTagFlowLayout.setOnItemClickListener(view -> {
            if (initTag.isChecked()) initTag.setChecked(false);
        });
    }

    /**
     * 设置商品
     */
    private void setGood() {
        if (good == null) return;
        goodLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(good.getGPhoto()).into(goodImg);
        goodName.setText(good.getGName());
        goodDiscount.setText(String.format("￥%s", good.getGPriceVip()));
        goodPrice.setText(String.format("￥%s", good.getGPriceOriginal()));
        goodDisct.setText(good.getGDes());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SHOP_GOOD://商品返回
                if (resultCode == RESULT_OK) {
                    good = (ShopEntity) data.getSerializableExtra(ShopActivity.RESULT_ENETIY);
                    setGood();
                }
                break;

            case CROP_TITLE_PIC:
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = null;
                    String imgUrl = data.getStringExtra(CoverEditActivity.KEY_PARAM_RESULT);
                    if (TextUtils.isEmpty(imgUrl)) return;
                    thumPic = imgUrl;
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgUrl, opt);
                    opt.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeFile(imgUrl, opt);
                    if (bmp == null) return;
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img.setImageBitmap(bmp);
                }
                break;

        }
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

                if (checkIsLife()) return;
                if (data != null) {
                    JSONObject jo = (JSONObject) JSON.parse(data);
                    String code = jo.getString("code");
                    if (code != null && code.equalsIgnoreCase("0")) {
                        typeEntities = JSON.parseArray(jo.getString("data"), TypeEntity.class);
                        if (typeEntities != null && typeEntities.size() > 0) {

                            for (TypeEntity label : typeEntities) {
                                CheckBox checkBox = (CheckBox) LayoutInflater.from(mContext).inflate(R.layout.radiobutton_label, null);
                                checkBox.setText(label.getTitle());
                                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                ;
                                lp.leftMargin = 10;
                                lp.topMargin = DensityUtil.dip2px(10);
                                checkBox.setLayoutParams(lp);
                                checkBox.setTag(label.getId());
                                mTagFlowLayout.addView(checkBox);
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
     * 阿里云接口（图片及视频）
     */
    private void uploadAliyun() {
        String ossVideoUrl = OssUtils.getTimeNmaeVideo();
        //先上传视频
        new FeedsApiImpl().upLoadImageFileToOSS(mOutputPath, ossVideoUrl, mContext, new DataCallback<FeedsResourceBean>() {
            @Override
            public void onSuccess(FeedsResourceBean data) {
                if (checkIsLife()) return;
                //再上传图片
                String ossImageUrl = OssUtils.getTimeNmaeJpg();
                new FeedsApiImpl().upLoadImageFileToOSS(thumPic, ossImageUrl, mContext, new DataCallback<FeedsResourceBean>() {
                    @Override
                    public void onSuccess(FeedsResourceBean data) {
                        if (checkIsLife()) return;
                        //上传自身服务
                        uploadData(OssUtils.getOssUploadingUrl(ossImageUrl), OssUtils.getOssUploadingUrl(ossVideoUrl));
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg) {
                        if (checkIsLife()) return;
                        ViewInject.shortToast(mContext, errMsg);
                        if (mLoadingDialog != null && mLoadingDialog.isShowing) {
                            mLoadingDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (checkIsLife()) return;
                ViewInject.shortToast(mContext, errMsg);
                if (mLoadingDialog != null && mLoadingDialog.isShowing) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    /**
     * 上传发布内容
     *
     * @param titlePic
     * @param VideoUrl
     */
    private void uploadData(String titlePic, String VideoUrl) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("type", 4);
        params.put("title", mEditText.getText().toString());
        params.put("content", VideoUrl);
        params.put("labelId", mTagFlowLayout.getLabId());
        params.put("photoAndVideoUrl", titlePic);
        params.put("crLongitude", AppConfig.slongitude);
        params.put("crLatitude", AppConfig.slatitude);
        params.put("crdId", "");
        params.put("vipDeployId", goodLayout.getVisibility() == View.VISIBLE ? good.getGid() : "");
        apiImp.publishedArticlesNew(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                JSONObject jo = JSON.parseObject(data);
                if (jo == null) return;
                if (jo.getString("code").equals("0")) {
                    ViewInject.shortToast(getApplicationContext(), R.string.release_success);
                    Bus.getDefault().getDefault().post(new ShopEntity());
                    finish();
                } else {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing) {
                        mLoadingDialog.dismiss();
                    }
                    ViewInject.shortToast(mContext, jo.getString("message"));
                }


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (checkIsLife()) return;
                if (mLoadingDialog != null && mLoadingDialog.isShowing) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(mContext, "发布失败");
            }
        });
    }


    public static void startPulishActivity(Context context, String outputPath, long duration, String filePath, String projectJsonPath) {
        Intent intent = new Intent(context, PulishActivity.class);
        intent.putExtra(CropKey.RESULT_KEY_CROP_PATH, outputPath);
        intent.putExtra(CropKey.RESULT_KEY_DURATION, duration);
        intent.putExtra(CropKey.RESULT_KEY_FILE_PATH, filePath);
        intent.putExtra("project_json_path", projectJsonPath);
        context.startActivity(intent);
    }

}
