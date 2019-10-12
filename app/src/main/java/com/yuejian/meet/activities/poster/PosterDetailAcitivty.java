package com.yuejian.meet.activities.poster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.family.ActivityLabActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.PosterDetailEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.zhy.view.flowlayout.FlowLayout;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class PosterDetailAcitivty extends BaseActivity {
    @Bind(R.id.head_back)
    View back;
    @Bind(R.id.head_share)
    TextView shard;
    @Bind(R.id.head_check)
    ImageView check;
    @Bind(R.id.activity_poster_pic)
    ImageView poster_img;
    @Bind(R.id.activity_poster_title)
    TextView poster_title;
    @Bind(R.id.activity_poster_discount_price)
    TextView poster_discount_price;
    @Bind(R.id.activity_poster_price)
    TextView poster_price;
    @Bind(R.id.activity_poster_content)
    TextView poster_content;
    @Bind(R.id.activity_poster_flowlayout)
    FlowLayout flowLayout;
    @Bind(R.id.activity_poster_cardview)
    CardView cardView;
    @Bind(R.id.activity_poster_open_vip)
    View openVip;

    private PosterDetailEntity posterInfo;

    private Bitmap share_icon;

    private static String POSTER_DETAIL_ID = "POSTER_DETAIL_ID";

    private static String CUSTOMER_ID = "customerId";

    private static String POSTERS_TITTLE = "postersTitle";

    private static String LABEL_ID = "labelId";

    private static String POSTERS_JSON = "postersJson";

    private int id = -1;

    private String customerId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_detail);
        init();
        if (!getData()) return;
        getDataFromNet();
    }

    private void init() {

        ViewGroup.LayoutParams lp = cardView.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth(this) * 0.584);
        lp.height = lp.width / 9 * 16;
        cardView.setLayoutParams(lp);

        ViewGroup.LayoutParams contentLp = poster_content.getLayoutParams();
        contentLp.width = (int) (ScreenUtils.getScreenWidth(this) * 0.64);
        poster_content.setLayoutParams(contentLp);

        poster_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        poster_price.getPaint().setAntiAlias(true);
    }


    @OnClick({R.id.head_back, R.id.activity_poster_experience, R.id.head_share, R.id.head_check, R.id.activity_poster_open_vip})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.activity_poster_experience:
                Intent publishIntent = new Intent(this, WebActivity.class);

                String url = "http://app2.yuejianchina.com/yuejian-app/canvas_haibao/daiyan.html?"
                        + "userPosterType=" + false + "&"
                        + "id=" + id + "&"
                        + "customerId=" + customerId;
//                        + "&"
//                        + "postersTitle=" + postersTitle + "&"
//                        + "labelId=" + labelId + "&"
//                        + "postersJson=" + postersJson;
                Log.e("URL", url);
                publishIntent.putExtra("url", url);
                publishIntent.putExtra("No_Title", true);
                startActivity(publishIntent);
                break;
            //分享
            case R.id.head_share:
                Utils.umengShareByList(
                        PosterDetailAcitivty.this,
                        share_icon,
                        posterInfo.getPostersTitle(),
                        " ",
                        String.format("http://app2.yuejianchina.com/yuejian-app/canvas_haibao/poster_share.html?previewUrl=%s&postersTitle=%s", posterInfo.getPreviewUrl(), posterInfo.getPostersTitle())
                );
                break;
            //收藏
            case R.id.head_check:
                if (posterInfo.isTemplateCollection()) return;
                collect();
                break;
            //开通VIP
            case R.id.activity_poster_open_vip:
                openVip();
                break;
        }
    }

    /**
     * 开通VIP
     */
    private void openVip() {
        String urlVip = "";
//        urlVip = String.format(UrlConstant.ExplainURL.OPEN_VIP + "?customerId=%s&phone=true", AppConfig.CustomerId);
        urlVip = String.format("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/pages/vip/vip.html?customerId=%s&phone=true", AppConfig.CustomerId);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.URL, urlVip);
        intent.putExtra("No_Title", true);
        startActivity(intent);
    }

    /**
     * 收藏
     */
    private void collect() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 10);
        params.put("relationId", posterInfo.getId());
        params.put("customerId", AppConfig.CustomerId);
        apiImp.doCollection(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                JSONObject jo = JSONObject.parseObject(data);
                if (jo != null && jo.getString("code").equals("0")) {
                    posterInfo.setTemplateCollection(true);
                    check.setImageResource(R.mipmap.icon_nav_collect_sel);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (checkIsLife()) return;
                ViewInject.shortToast(mContext, errMsg);
            }
        });
    }

    private boolean getData() {
        id = getIntent().getIntExtra(POSTER_DETAIL_ID, id);
        customerId = getIntent().getStringExtra(CUSTOMER_ID);
//        postersTitle = getIntent().getStringExtra(POSTERS_TITTLE);
//        labelId = getIntent().getStringExtra(LABEL_ID);
//        postersJson = getIntent().getStringExtra(POSTERS_JSON);
        return id != -1;
    }

    public static void startActivity(Context context, int id, String customerId) {
        Intent intent = new Intent(context, PosterDetailAcitivty.class);
        intent.putExtra(POSTER_DETAIL_ID, id);
        intent.putExtra(CUSTOMER_ID, customerId);
//        intent.putExtra(POSTERS_TITTLE, postersTitle);
//        intent.putExtra(LABEL_ID, labelId);
//        intent.putExtra(POSTERS_JSON, postersJson);
        context.startActivity(intent);
    }

    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("customerId", AppConfig.CustomerId);
        apiImp.findPostersModelById(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                if (data == null || data.length() < 0 || data.equalsIgnoreCase("null")) return;

                JSONObject jo = JSONObject.parseObject(data);
                if (null == jo) return;

                if (!jo.getString("code").equals("0")) return;

                parseJson(jo.getString("data"));

                if (null != posterInfo) {
                    Glide.with(mContext).load(posterInfo.getPreviewUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (checkIsLife()) return;
                            share_icon = bitmap;
                            poster_img.setImageBitmap(bitmap);
                        }
                    });
                    poster_title.setText(posterInfo.getPostersTitle());
                    poster_discount_price.setText(posterInfo.getDiscountPrice() + "");
                    poster_price.setText(String.format("%s贡献值", posterInfo.getPostersPrice() + ""));
                    poster_content.setText(posterInfo.getPostersDes());
                    check.setImageResource(posterInfo.isTemplateCollection() ? R.mipmap.icon_nav_collect_sel : R.mipmap.icon_nav_collect_nor);
//                    if (posterInfo.getContentLabelList() != null && posterInfo.getContentLabelList().size() > 0) {
//                        for (PosterDetailEntity.ContentLabelList labelList : posterInfo.getContentLabelList()) {
//                            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview, null);
//                            textView.setText(labelList.getTitle());
//                            flowLayout.addView(textView);
//                        }
//                    }

                    if (!TextUtils.isEmpty(posterInfo.getLabelId()) && !TextUtils.isEmpty(posterInfo.getLableName())) {
                        String labName = posterInfo.getLableName().replaceAll(" ", "");
                        labName = labName.substring(1, labName.length());
                        String[] labs = labName.split("#");
                        int i;
                        for (i = 0; i < labName.length(); i++) {
                            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview, null);
                            textView.setText(labs[i]);
                            flowLayout.addView(textView);

                        }
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void parseJson(String data) {
        JSONObject jo = JSON.parseObject(data);
        if (jo != null) {
            posterInfo = new PosterDetailEntity();
//            List<PosterDetailEntity.ContentLabelList> contentLabelList = JSON.parseArray(jo.getString("contentLabelList"), PosterDetailEntity.ContentLabelList.class);
//            if (null != contentLabelList) {
//                posterInfo.setContentLabelList(contentLabelList);
//            }
            posterInfo.setLableName(jo.getString("lableName"));
            posterInfo.setLabelId(jo.getString("labelId"));
            posterInfo.setCreateTime(jo.getInteger("createTime"));
            posterInfo.setDiscountPrice(jo.getDouble("discountPrice"));
            posterInfo.setId(jo.getInteger("id"));
            posterInfo.setTemplateCollection(jo.getBoolean("templateCollection"));
            posterInfo.setIsDelete(jo.getInteger("isDelete"));
            posterInfo.setLabelId(jo.getString("labelId"));
            posterInfo.setPostersJson(jo.getString("postersJson"));
            posterInfo.setPostersPrice(jo.getDouble("postersPrice"));
            posterInfo.setPostersDes(jo.getString("postersDes"));
            posterInfo.setPostersTitle(jo.getString("postersTitle"));
            posterInfo.setPreviewUrl(jo.getString("previewUrl"));
            posterInfo.setRecFlag(jo.getInteger("recFlag"));
            posterInfo.setUpdateTime(jo.getInteger("updateTime"));
            posterInfo.setUsenumFalse(jo.getInteger("usenumFalse"));
            posterInfo.setUsenumTrue(jo.getInteger("usenumTrue"));


        }

    }


}
