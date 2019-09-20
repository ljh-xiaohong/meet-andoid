package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.AppConfig;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.PraiseEntity;
import com.yuejian.meet.bean.VideoAndContentEntiy;
import com.yuejian.meet.dialogs.MoreDialog;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.VideoPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class VideoActivity extends BaseActivity {

    private String contentId = null;
    private String customerId = null;
    private boolean full_screen;

    MoreDialog moreDialog;

    private VideoAndContentEntiy info;

    @Bind(R.id.activity_video_player)
    VideoPlayer player;

    private String[] labelName, labelId;

    private List<String> moreData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (!getData()) return;
        player.setLooping(true);
        getDataFromNet();
    }

    /**
     * @param context
     * @param contentId  内容ID
     * @param customerId 用户ID
     */
    public static void startActivity(Context context, String contentId, String customerId, boolean SCREEN_MATCH) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("VideoActivity.contentId", contentId);
        intent.putExtra("VideoActivity.customerId", customerId);
        intent.putExtra("VideoActivity.SCREEN_MATCH", SCREEN_MATCH);
        context.startActivity(intent);
    }

    private boolean getData() {
        contentId = getIntent().getStringExtra("VideoActivity.contentId");
        customerId = getIntent().getStringExtra("VideoActivity.customerId");
        full_screen = getIntent().getBooleanExtra("VideoActivity.SCREEN_MATCH", full_screen);
        GSYVideoType.setShowType(full_screen ? GSYVideoType.SCREEN_MATCH_FULL : GSYVideoType.SCREEN_TYPE_DEFAULT);
        return contentId != null && customerId != null;
    }


    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", contentId);
        params.put("customerId", customerId);
        apiImp.getContentDetails(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                parseJSON(data);
                if (info == null) return;
                initData();
                initListener();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void initData() {
        player.setUp(info.getCrContent(), true, "");
        player.startPlayLogic();
        Glide.with(mContext).load(info.getUserPhoto()).into(player.getHeadImagView());
        player.getNameText().setText(info.getUserName());
        player.getContenText().setText(info.getContentTitle());
        //关注
        player.getFollowText().setTextColor(Color.parseColor(info.getIsRelation() == 0 ? "#ffffffff" : "66ffffff"));
        player.getFollowText().setText(info.getIsRelation() == 0 ? "加关注" : "已关注");
        //点赞数
        //是否点赞
        player.setLike(info.getIsPraise() == 1 ? true : false, info.getFabulousNum());
        //评论数量
        player.getDiscussButton().setText(info.getCommentNum());
        player.getContenText().setText(info.getContentTitle());
        player.getGoodsButton().setText(info.getShopName());

        labelName = info.getLabelName().trim().substring(1, info.getLabelName().length()).split("#");
        labelId = info.getLabelId().split(",");

        player.setTagItem(labelName, labelId, view -> {
            AcitivityLabActivity.startActivity(mContext, (String) (view.getTag()), AppConfig.CustomerId);
        });

    }

    private void initListener() {
        player.getLikeButton().setOnClickListener(view -> {
            like();
        });
        player.getShareButton().setOnClickListener(view -> {
            share();
        });

        player.getFollowText().setOnClickListener(view -> {
            AddFollow();
        });
    }

    private void parseJSON(String data) {
        if (TextUtils.isEmpty(data)) return;
        JSONObject jo = JSON.parseObject(data);
        if (null == jo && !jo.getString("code").equals("0")) return;
        JSONArray ja = jo.getJSONArray("data");
        info = new VideoAndContentEntiy();
        JSONObject in = ja.getJSONObject(0);
        if (in == null && in.size() <= 0) return;
        try {
            info.setPhotoAndVideoUrl(in.getString("photoAndVideoUrl"));
            info.setUserPhoto(in.getString("userPhoto"));
            info.setCrContent(in.getString("crContent"));
            info.setShopName(in.getString("shopName"));
            info.setContentTitle(in.getString("contentTitle"));
            info.setUserName(in.getString("userName"));
            info.setUserVipType(in.getString("userVipType"));
            info.setIsRelation(in.getInteger("isRelation"));
            info.setCommentNum(in.getString("commentNum"));
            info.setLabelId(in.getString("labelId"));
            info.setCreateTime(in.getString("createTime"));
            info.setFabulousNum(in.getString("fabulousNum"));
            info.setCustomerId(in.getString("customerId"));
            info.setIsPraise(in.getInteger("isPraise"));
            info.setId(in.getInteger("id"));
            info.setShopId(in.getString("shopId"));
            info.setLabelName(in.getString("labelName"));
            info.setContentType(in.getString("contentType"));
        } catch (NullPointerException e) {

        }

    }

    /**
     * 点赞
     */
    //todo 点赞处理
    private void like() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("crId", contentId);
        apiImp.praiseContent(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PraiseEntity praise = JSON.parseObject(data, PraiseEntity.class);
                if (praise == null) return;
                switch (praise.getCode()) {
                    //操作成功
                    case 0:
                        int count = Integer.valueOf(info.getFabulousNum());
                        if (info.getIsPraise() == 1) {
                            info.setIsPraise(0);
                            count = count - 1;
                        } else {
                            info.setIsPraise(1);
                            count = count + 1;
                        }
                        info.setFabulousNum(count + "");
                        player.setLike(info.getIsPraise() == 1 ? true : false, count + "");

                        break;

                    //拉黑
                    case 10205:

                        break;

                    //系统异常
                    case 10001:

                        break;

                    //其他
                    case -1:

                        break;

                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 分享
     */
    private void share() {
        Glide.with(mContext).load(info.getPhotoAndVideoUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Utils.umengShareByList(
                        (Activity) mContext,
                        bitmap,
                        info.getContentTitle(),
                        mContext.getResources().getString(R.string.share_description),
                        String.format("http://app2.yuejianchina.com/yuejian-app/release/blank.html?type=%s&id=%s", info.getContentType(), info.getId()));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onVideoPause();
    }

    /**
     * 关注
     */
    public void AddFollow() {
        Map<String, Object> params = new HashMap<>();
        params.put("opCustomerId", info.getCustomerId());
        params.put("customerId", customerId);
        //2-取消关注 1-添加关注
        params.put("type", info.getIsRelation() == 0 ? 1 : 2);
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

                JSONObject jo = JSONObject.parseObject(data);
                if (jo == null) return;
                switch (jo.getInteger("code")) {

                    case 0:
                        info.setIsRelation(info.getIsRelation() == 0 ? 1 : 0);
                        player.getFollowText().setTextColor(Color.parseColor(info.getIsRelation() == 0 ? "#ffffffff" : "66ffffff"));
                        player.getFollowText().setText(info.getIsRelation() == 0 ? "加关注" : "已关注");
                        break;

                    case 19983:
                    case 19981:
                        ViewInject.shortToast(mContext, jo.getString("message"));
                        break;
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
