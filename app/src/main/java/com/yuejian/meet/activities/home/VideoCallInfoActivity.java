package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.VideoCallInfoEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 通话详情
 */
public class VideoCallInfoActivity extends BaseActivity {
    @Bind(R.id.video_call_photo)
    ImageView video_call_photo;
    @Bind(R.id.video_call_name)
    TextView video_call_name;
    @Bind(R.id.video_call_go_time)
    TextView video_call_go_time;
    @Bind(R.id.video_call_money)
    TextView video_call_money;
    @Bind(R.id.video_call_price)
    TextView video_call_price;
    @Bind(R.id.video_call_me_content)
    TextView video_call_me_content;
    @Bind(R.id.video_call_content_he)
    TextView video_call_content_he;
    @Bind(R.id.video_call_duration)
    TextView video_call_duration;
    @Bind(R.id.video_call_sex)
    TextView video_call_sex;
    @Bind(R.id.video_call_me_evaluate)
    ImageView video_call_me_evaluate;
    @Bind(R.id.video_call_he_evaluate)
    ImageView video_call_he_evaluate;
    ApiImp api = new ApiImp();
    String video_id;
    VideoCallInfoEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_info);
        video_id = getIntent().getStringExtra("video_id");
        initData();
    }

    public void initData() {
        setTitleText("通话详情");
        requstData();
    }

    public void requstData() {
        if (StringUtils.isEmpty(video_id)) {
            Toast.makeText(getApplicationContext(), "video link is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("video_id", video_id);
        api.getVideoCallInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity = JSON.parseObject(data, VideoCallInfoEntity.class);
                loadViewData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                finish();
            }
        });
    }

    public void loadViewData() {
        video_call_me_content.setText(entity.getDescription());//我对他的评价
        video_call_content_he.setText(entity.getOp_description());//他对我的评价
        video_call_price.setText(entity.getVideo_price() + "金币/分钟");
        video_call_money.setText(entity.getCost() + "金币");
        video_call_go_time.setText((entity.getIs_send().equals("1") ? "去" : "来") + "电时间 " + StringUtils.friendlyTime(entity.getStart_time()));
        video_call_name.setText(entity.getSurname() + entity.getName());
        Glide.with(this).load(entity.getPhoto()).error(R.mipmap.ic_default).into(video_call_photo);
        video_call_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mContext, entity.customer_id);
            }
        });
        video_call_sex.setSelected(entity.getSex().equals("0") ? false : true);
        video_call_sex.setText(" " + entity.getAge());
        if (entity.getJudge_level().equals("1")) {//我
            video_call_me_evaluate.setImageResource(R.mipmap.ic_evaluate_good_on);
        } else if (entity.getJudge_level().equals("2") || entity.getJudge_level().equals("3")) {
            video_call_me_evaluate.setImageResource(R.mipmap.ic_evaluate_bad_on);
        }
        if (entity.getOp_judge_level().equals("1")) {//对方
            video_call_he_evaluate.setImageResource(R.mipmap.ic_evaluate_good_on);
        } else if (entity.getOp_judge_level().equals("2") || entity.getOp_judge_level().equals("3")) {
            video_call_he_evaluate.setImageResource(R.mipmap.ic_evaluate_bad_on);
        }

        try {
            int duration = Integer.valueOf(entity.duration);
            int hour = duration / 60;
            int min = duration - (hour * 60);
            String hourString = "";
            if (hour >= 10) {
                hourString = "" + hour;
            } else {
                hourString = "0" + hour;
            }
            String minString = "";
            if (min >= 10) {
                minString = "" + min;
            } else {
                minString = "0" + min;
            }
            String durationStr = hourString + ":" + minString + ":00";
            video_call_duration.setText((entity.getIs_send().equals("1") ? "去" : "来") + "电时长 " + durationStr);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

    }
}
