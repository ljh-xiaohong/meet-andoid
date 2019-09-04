package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.QqLoginBean;
import com.yuejian.meet.bean.WxLoginBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 微信认证
 * Created by zh02 on 2017/8/11.
 */

public class VerifyWxActivity extends BaseActivity {

    private LoadingDialogFragment dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_wx);
        setTitleText(getString(R.string.verify_center_text4));
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
    }

    @OnClick({R.id.sure_verify_wx})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_verify_wx:
                goToVerifyWx();
                break;
        }
    }

    private void goToVerifyWx() {
        UMShareAPI umShareAPI = UMShareAPI.get(this);

        if (!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
            ViewInject.shortToast(this, R.string.not_install_weChat);
            return;
        }
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (dialog != null) {
                dialog.dismiss();
            }
            try {
                String loginData = Utils.map2Json(data);
                Gson gson = new Gson();
                org.json.JSONObject object = new org.json.JSONObject(loginData);
                if (platform == SHARE_MEDIA.WEIXIN) {
                    WxLoginBean wxLoginBean = gson.fromJson(object.toString(), WxLoginBean.class);
                    verifyWx(wxLoginBean.unionid, wxLoginBean.iconurl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ViewInject.toast(getApplicationContext(), R.string.authorization_failure);
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (dialog != null)
                dialog.show(getFragmentManager(), "");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ViewInject.toast(getApplicationContext(), "取消成功");
            if (dialog != null)
                dialog.dismiss();
        }
    };

    private void verifyWx(String openID, String wxUrl) {
        if (user == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("weixin", openID);
        params.put("wx_url", wxUrl);
        new ApiImp().wxCertification(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Toast.makeText(mContext, R.string.WeChat_certification_is_successful, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
