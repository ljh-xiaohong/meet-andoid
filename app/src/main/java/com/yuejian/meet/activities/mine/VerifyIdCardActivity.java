package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.session.constant.Extras;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 身份认证
 * Created by zh02 on 2017/8/11.
 */

public class VerifyIdCardActivity extends BaseActivity {

    @Bind(R.id.upload_id_card_and_head_img)
    ImageView uploadIdCardHeadImg;
    @Bind(R.id.upload_id_card_img)
    ImageView uploadIdCardImg;

    private static final int TAKE_HEAD_WITH_ID_CARD_PHOTO = 1000;
    private static final int TAKE_ID_CARD_PHOTO = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_id_card);
        setTitleText(getString(R.string.verifyld_title));
    }

    @OnClick({R.id.upload_id_card_and_head_img, R.id.upload_id_card_img, R.id.sure_verify_id_card, R.id.see_the_examples})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_id_card_and_head_img:
                takeHeadWithIdCardPhoto();
                break;
            case R.id.upload_id_card_img:
                takeIdCardPhoto();
                break;
            case R.id.sure_verify_id_card:
                verifyIdCard();
                break;
            case R.id.see_the_examples:
                showExamples();
                break;
        }
    }

    private PopupWindow popupWindow = null;

    private void showExamples() {
        if (popupWindow == null) {
            View contentView = View.inflate(this, R.layout.layout_examples_id_card, null);
            ImageView close = (ImageView) contentView.findViewById(R.id.close_examples_window);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            contentView.setFitsSystemWindows(true);
            popupWindow = new PopupWindow(contentView, DensityUtils.getScreenW(this), DensityUtils.getScreenH(this) - DensityUtils.dip2px(this, 50));
            popupWindow.setBackgroundDrawable(null);
        }
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.LEFT, 0, 0);
        setBackgroundAlpha(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(false);
            }
        });

    }

    private void setBackgroundAlpha(boolean isAlpha) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (isAlpha) {
            attributes.alpha = 0.3f;
        } else {
            attributes.alpha = 1f;
        }
        getWindow().setAttributes(attributes);
    }

    private String headWithIdCardPath = "";

    private void takeHeadWithIdCardPhoto() {
//        PickImageActivity.start(this, TAKE_HEAD_WITH_ID_CARD_PHOTO, PickImageActivity.FROM_CAMERA, headWithIdCardPath, false, 1, true, false, 0, 0);
        Utils.pickPhotos(this, TAKE_HEAD_WITH_ID_CARD_PHOTO, false, 0, 0);
    }

    private String idCardPath = "";

    private void takeIdCardPhoto() {
//        PickImageActivity.start(this, TAKE_ID_CARD_PHOTO, PickImageActivity.FROM_CAMERA, idCardPath, false, 1, true, false, 0, 0);
        Utils.pickPhotos(this, TAKE_ID_CARD_PHOTO, false, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (TAKE_HEAD_WITH_ID_CARD_PHOTO == requestCode) {
                headWithIdCardPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                Glide.with(mContext).load(new File(headWithIdCardPath)).asBitmap().placeholder(R.mipmap.ic_default).into(uploadIdCardHeadImg);
            } else if (TAKE_ID_CARD_PHOTO == requestCode) {
                idCardPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                Glide.with(mContext).load(new File(idCardPath)).asBitmap().placeholder(R.mipmap.ic_default).into(uploadIdCardImg);
            }
        }
    }

    private FeedsApiImpl feedsApi = new FeedsApiImpl();
    private String idCard1 = "";
    private String idCard2 = "";
    private LoadingDialogFragment dialog = null;
    private int uploadIndex = 0;
    private ApiImp apiImp = new ApiImp();
    private ArrayList<String> uploadPaths;

    private void verifyIdCard() {
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        uploadIndex = 0;
        uploadPaths = new ArrayList<>();
        if (StringUtils.isNotEmpty(headWithIdCardPath)) {
            uploadPaths.add(headWithIdCardPath);
        } else {
            Toast.makeText(mContext, R.string.verifyld_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNotEmpty(idCardPath)) {
            uploadPaths.add(idCardPath);
        } else {
            Toast.makeText(mContext, R.string.verifyld_toast1, Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setOnDismissListener(new LoadingDialogFragment.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (StringUtils.isEmpty(idCard1)) {
                    Toast.makeText(mContext, R.string.verifyld_toast2, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtils.isEmpty(idCard2)) {
                    Toast.makeText(mContext, R.string.verifyld_toast3, Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> params = new HashMap<>();
                params.put("customer_id", user.customer_id);
                params.put("id_card_1", idCard1);
                params.put("id_card_2", idCard2);
                apiImp.verifyIdCard(params, VerifyIdCardActivity.this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        uploadIdCardPhotos();
    }

    private void uploadIdCardPhotos() {
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "");
        for (final String path : uploadPaths) {
            feedsApi.upLoadImageFileToOSS(path, OssUtils.getTimeNmaeJpg(), this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (path.equals(headWithIdCardPath)) {
                        idCard1 = data.imageUrl;
                    } else if (path.equals(idCardPath)) {
                        idCard2 = data.imageUrl;
                    }
                    uploadIndex++;
                    if (uploadIndex == uploadPaths.size()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(FeedsResourceBean data, int id) {

                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    uploadIndex++;
                    if (uploadIndex == uploadPaths.size()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
