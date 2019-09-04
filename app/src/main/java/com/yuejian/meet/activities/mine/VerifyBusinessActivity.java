package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
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
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 营业执照认证
 * Created by zh02 on 2017/8/11.
 */

public class VerifyBusinessActivity extends BaseActivity {

    private static int TAKE_ID_CARD_PHOTO = 1000;

    @Bind(R.id.upload_business_certification_img)
    ImageView uploadImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_business);
        setTitleText(getString(R.string.verify_business_title));
        dialog = LoadingDialogFragment.newInstance(getString(R.string.verify_business_toast1));
    }

    @OnClick({R.id.upload_business_certification_img, R.id.sure_verify_business})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_business_certification_img:
                takePhoto();
                break;
            case R.id.sure_verify_business:
                verifyBusiness();
                break;
        }
    }

    private void verifyBusiness() {
        dialog.setOnDismissListener(new LoadingDialogFragment.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (StringUtils.isEmpty(licenceUrl)) {
                    return;
                }
                HashMap<String, Object> params = new HashMap<>();
                params.put("customer_id", user.customer_id);
                params.put("business_licence", licenceUrl);
                new ApiImp().businessCertification(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Toast.makeText(mContext, R.string.verify_business_toast, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
//                        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show(getFragmentManager(), "");
        if (StringUtils.isNotEmpty(licenceUrl)) {
            dialog.dismiss();
        } else {
            uploadBusinessPhoto();
        }
    }

    private FeedsApiImpl feedsApi = new FeedsApiImpl();
       private LoadingDialogFragment dialog ;
    private String licenceUrl = "";

    private void uploadBusinessPhoto() {
        licenceUrl = "";
        feedsApi.upLoadImageFileToOSS(businessCertificatesPath, OssUtils.getTimeNmaeJpg(), this, new DataCallback<FeedsResourceBean>() {
            @Override
            public void onSuccess(FeedsResourceBean data) {
                licenceUrl = data.imageUrl;
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String businessCertificatesPath = "";

    private void takePhoto() {
//        PickImageActivity.start(this, TAKE_ID_CARD_PHOTO, PickImageActivity.FROM_CAMERA, businessCertificatesPath, false, 1, true, false, 0, 0);
        Utils.pickPhotos(this, TAKE_ID_CARD_PHOTO, true, Constants.PORTRAIT_IMAGE_WIDTH, Constants.PORTRAIT_IMAGE_WIDTH * 5 / 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (TAKE_ID_CARD_PHOTO == requestCode) {
                businessCertificatesPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                Glide.with(mContext).load(new File(businessCertificatesPath)).asBitmap().placeholder(R.mipmap.ic_default).into(uploadImg);
            }
        }
    }
}
