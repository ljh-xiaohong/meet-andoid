package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.app.AppConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 我的二维码
 * Created by Administrator on 2018/1/22/022.
 */

public class MyQrCodeActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        setTitleText(getString(R.string.scanner_content));
        ImageView qrCodeImg = (ImageView) findViewById(R.id.qr_code_img);
        if (StringUtils.isEmpty(AppConfig.CustomerId) || AppConfig.userEntity == null) {
            finish();
        }
        findViewById(R.id.select_more).setVisibility(View.VISIBLE);
        int size = DensityUtils.dip2px(this, 420);
        icon = (ImageView) findViewById(R.id.icon);
        Bitmap qrCode = Utils.generateBitmap(UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id=" + AppConfig.CustomerId + "&inviteCode=" + AppConfig.userEntity.invite_code, size, size);
        qrCodeImg.setImageBitmap(qrCode);
        findMyInfo(AppConfig.CustomerId);
        TextView id = (TextView) findViewById(R.id.customer_id);
        id.setText("约见号: " + AppConfig.CustomerId);
    }

    @OnClick({R.id.select_more})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_more:
//                final UMWeb web = new UMWeb(UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id=" + AppConfig.CustomerId + (AppConfig.userEntity != null ? "&inviteCode=" + AppConfig.userEntity.invite_code : ""));
//                web.setTitle("你的好友邀请你进入家族");//标题
//                web.setDescription("约见app是一款基于百家姓的APP，旨在凝聚家族力量，弘扬百家姓文化。带你追根溯源，找到家族宗亲");//描述
                View layout = findViewById(R.id.qr_code_layout);
                layout.setDrawingCacheEnabled(true);
                Bitmap bitmap = Utils.getViewBitmap(layout);
                layout.destroyDrawingCache();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                int marginTop = Utils.dp2px(getBaseContext(), 80);
                lp.topMargin = marginTop;
                layout.setLayoutParams(lp);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                }
                final UMImage image = new UMImage(this, bitmap);
                final Bitmap finalBitmap = bitmap;
                new ShareAction(this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .addButton("save_in_gallery", "save_in_gallery", "icon_baocun", "icon_baocun")
                        .addButton("go_back_home", "go_back_home", "icon_goback", "icon_goback")
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if ("save_in_gallery".equals(snsPlatform.mShowWord)) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.saveImageToGallery(MyQrCodeActivity.this, finalBitmap, String.valueOf(System.currentTimeMillis()));
                                        }
                                    }, 1000);
                                } else if ("go_back_home".equals(snsPlatform.mShowWord)) {
                                    Intent intent = new Intent();
                                    intent.putExtra("go_to_index", true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else if (share_media == SHARE_MEDIA.QZONE) {
                                    new ShareAction(MyQrCodeActivity.this).setPlatform(share_media)
                                            .withMedia(image)
                                            .share();
                                } else if (share_media == SHARE_MEDIA.QQ) {
                                    new ShareAction(MyQrCodeActivity.this).setPlatform(share_media)
                                            .withMedia(image)
                                            .share();
                                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                                    new ShareAction(MyQrCodeActivity.this).setPlatform(share_media)
                                            .withMedia(image)
                                            .share();
                                } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                                    new ShareAction(MyQrCodeActivity.this).setPlatform(share_media)
                                            .withMedia(image)
                                            .share();
                                }
                            }
                        }).open();

                break;
        }
    }

    private Mine mine = null;
    private Bitmap iconBitmap = null;
    private ImageView icon = null;

    private void findMyInfo(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        apiImp.findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mine = JSON.parseObject(data, Mine.class);
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(mine.surname + mine.name);
                Glide.with(getBaseContext()).load(mine.photo).asBitmap().into(icon);
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        icon.setImageBitmap(resource);
//                        iconBitmap = resource;
//                        return false;
//                    }
//                });
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (iconBitmap != null) {
            iconBitmap.recycle();
            iconBitmap = null;
        }
        super.onDestroy();
    }
}
