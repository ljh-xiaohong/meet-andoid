package com.yuejian.meet.activities.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.VideoAndContentEntiy;
import com.yuejian.meet.widgets.VideoPlayer;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class VideoActivity extends BaseActivity {

    private String contentId = null;
    private String customerId = null;
    private boolean full_screen;

    private VideoAndContentEntiy info;

    @Bind(R.id.activity_video_player)
    VideoPlayer player;

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
                player.setUp(info.getCrContent(), true, "");
                player.startPlayLogic();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
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
        info.setPhotoAndVideoUrl(in.getString("photoAndVideoUrl"));
        info.setUserPhoto(in.getString("userPhoto"));
        info.setCreateTime(in.getInteger("createTime"));
        info.setContentVipType(in.getInteger("contentVipType"));
        info.setCrContent(in.getString("crContent"));
        info.setCustomerId(in.getInteger("customerId"));
        info.setLabelTitle(in.getString("labelTitle"));
        info.setContentTitle(in.getString("contentTitle"));
        info.setVipDeployId(in.getInteger("vipDeployId"));
        info.setUserName(in.getString("userName"));
        info.setUserVipType(in.getInteger("userVipType"));
        info.setIsRelation(in.getInteger("isRelation"));
    }


    @Override
    protected void onPause() {
        super.onPause();
        player.onVideoPause();
    }
}
