package com.yuejian.meet.activities.clan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ReleaseActionAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanPhotoReleaseActivity extends BaseActivity implements OnItemClickListener {
    private static final int OPENCAM = 12;
    private static final int OPENPIC = 11;
    String clanId = "";
    LoadingDialogFragment dialog;
    Dialog dialogs;
    @Bind(R.id.clan_photo_release_gridveiw)
    GridView gridView;
    String imgUrlArr = "";
    ReleaseActionAdapter2 mAdpter2;
    String outputPath = "";
    List<PhotoInfo> photosList = new ArrayList();
    int pudateCnt = 0;
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    View viewDailog;

    public void delPhotos(int paramInt) {
        this.photosList.remove(paramInt);
        this.mAdpter2.refresh(this.photosList);
    }

    public void initData() {
        setTitleText("上传到相册");
        this.txt_titlebar_release.setVisibility(View.VISIBLE);
        this.txt_titlebar_release.setTextColor(Color.parseColor("#ffffff"));
        this.txt_titlebar_release.setText("确定");
        this.mAdpter2 = new ReleaseActionAdapter2(this, this.photosList);
        this.gridView.setAdapter(this.mAdpter2);
        this.gridView.setOnItemClickListener(this);
    }

    protected void onActivityResult(int paramInt1, int resultCode, Intent data) {
        super.onActivityResult(paramInt1, resultCode, data);
        switch (paramInt1) {
            case OPENPIC://相册选择照片返回
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            photosList.addAll(photosInfo);
                            mAdpter2.notifyDataSetChanged();
                        }
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
                    return;
                }
                break;
        }
    }

    @OnClick({R.id.txt_titlebar_release, R.id.txt_action_dialog_select_img, R.id.txt_action_dialog_photograph_img})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.txt_titlebar_release:
                if (this.photosList.size() == 0) {
                    ViewInject.shortToast(getApplicationContext(), "至少选择一张图片");
                    return;
                }
                this.imgUrlArr = "";
                this.pudateCnt = 0;
                if (this.dialog != null) {
                    this.dialog.show(getFragmentManager(), "");
                }
                int i = 0;
                while (i < this.photosList.size()) {
                    updateUrl(((PhotoInfo) this.photosList.get(i)).getAbsolutePath());
                    i += 1;
                }
            case R.id.txt_action_dialog_select_img:
                showSelector(true);
                this.dialogs.dismiss();
                return;
            case R.id.txt_action_dialog_photograph_img:
                showSelector(false);
                this.dialogs.dismiss();
                break;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_photo_release);
        this.clanId = getIntent().getStringExtra("clanId");
        initData();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (paramInt == this.photosList.size()) {
            showDialog();
        }
    }

    public void posetRequst() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("photos", this.imgUrlArr);
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.clanUpLoadingPhoto(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                ViewInject.toast(ClanPhotoReleaseActivity.this.getApplication(), "上传失败");
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ClanPhotoReleaseActivity.this.setResult(RESULT_OK, new Intent());
                ViewInject.toast(ClanPhotoReleaseActivity.this.getApplication(), "上传成功");
                ClanPhotoReleaseActivity.this.finish();
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        this.viewDailog = View.inflate(this, R.layout.dialog_action_issue_layout, null);
        this.viewDailog.findViewById(R.id.txt_action_dialog_issue).setVisibility(View.GONE);
        this.viewDailog.findViewById(R.id.txt_action_dialog_video).setVisibility(View.GONE);
        this.viewDailog.findViewById(R.id.txt_action_dialog_photograph_img).setOnClickListener(this);
        this.viewDailog.findViewById(R.id.txt_action_dialog_select_img).setOnClickListener(this);
        localBuilder.setView(this.viewDailog);
        this.dialogs = localBuilder.show();
        this.dialogs.show();
    }

    public void showSelector(Boolean paramBoolean) {
        if (this.photosList.size() >= 9) {
            ViewInject.shortToast(getApplication(), "图片最多能选9张");
            return;
        }
        this.outputPath = ImgUtils.imgTempFile();
        if (paramBoolean.booleanValue()) {
            PickImageActivity.start(this, 11, 1, this.outputPath, true, 9 - this.photosList.size(), true, false, 0, 0);
            return;
        }
        PickImageActivity.start(this, 12, 2, this.outputPath, false, 1, true, false, 0, 0);
    }

    public void updateUrl(String paramString) {
        String updateUrl;
        String bitmap2File=paramString;
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

        updateUserImg(updateUrl, bitmap2File);
    }

    public void updateUserImg(String paramString1, String paramString2) {
        if (!paramString1.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(paramString1, paramString2, this, new DataCallback<FeedsResourceBean>() {
                public void onFailed(String paramAnonymousString1, String paramAnonymousString2) {
                    if (ClanPhotoReleaseActivity.this.dialog != null) {
                        ClanPhotoReleaseActivity.this.dialog.dismiss();
                    }
                    ViewInject.shortToast(ClanPhotoReleaseActivity.this.getApplication(), paramAnonymousString2);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }

                public void onSuccess(FeedsResourceBean paramAnonymousFeedsResourceBean) {
                    if (ClanPhotoReleaseActivity.this.photosList.size() > 0) {
//                        paramAnonymousFeedsResourceBean = ClanPhotoReleaseActivity.this;
                        pudateCnt += 1;
                        if (ClanPhotoReleaseActivity.this.photosList.size() == pudateCnt) {
                            ClanPhotoReleaseActivity.this.posetRequst();
                        }
                    }
                }

                @Override
                public void onSuccess(FeedsResourceBean data, int id) {

                }
            });
        }
    }
}
