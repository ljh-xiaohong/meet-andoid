package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频评价---页面
 */
public class AvChatEvaluateActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.rg_evaluate_all)
    RadioGroup rg_evaluate_all;
    @Bind(R.id.rg_publicity)
    RadioGroup rg_publicity;
    @Bind(R.id.txt_evaluate_content)
    TextView content;
    @Bind(R.id.user_icon)
    ImageView user_icon;
    public String opCustomerId;
    public String video_id;
    ApiImp api=new ApiImp();
    Intent intent;
    public String isOpen="0";
    public String evaluate="1";
    private Mine myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_av_chat_evaluate);
        intent=getIntent();
        opCustomerId=intent.getStringExtra("opCustomerId");
        video_id=intent.getStringExtra("video_id");
        rg_evaluate_all.setOnCheckedChangeListener(new RadioGroupListener());
        rg_publicity.setOnCheckedChangeListener(new RadioGroupListener());
        initData();
    }
    public void initData(){
        back.setVisibility(View.VISIBLE);
        title.setText("评价");
        txt_titlebar_release.setVisibility(View.VISIBLE);
        txt_titlebar_release.setText("提交");
        getUsreInfo();
    }
    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==R.id.rb_open){//公开
                isOpen="0";
            }else if (checkedId==R.id.rb_hide){//匿名
                isOpen="1";
            }else if (checkedId==R.id.rb_good){//好评
                evaluate="1";
            }else if (checkedId==R.id.rb_medium){//中评
                evaluate="2";
            }else if (checkedId==R.id.rb_bad){//差评
                evaluate="3";
            }
        }
    }

//    FindMyInfoEntity myInfo;
    /**
     * 获取用户信息
     */
    public void getUsreInfo(){
        Map<String, Object> param = new HashMap<>();
        param.put("customer_id", opCustomerId);
        new ApiImp().findMyInfo(param, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                myInfo = JSON.parseObject(data, Mine.class);
                Glide.with(mContext).load(myInfo.photo).asBitmap().placeholder(R.mipmap.ic_default).into(user_icon);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    @OnClick({R.id.titlebar_imgBtn_back,R.id.btn_submit,R.id.txt_titlebar_release})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
            case R.id.txt_titlebar_release:
                Map<String ,Object > params=new HashMap<>();
                params.put("customer_id", AppConfig.CustomerId);
                params.put("op_customer_id",opCustomerId);
                params.put("judge_level",evaluate);
                params.put("video_id",video_id);
                params.put("description",content.getText()==null?"":content.getText().toString());
                api.postJudce(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        ViewInject.shortToast(getApplication(),"评价提交成功");
                        finish();
                    }
                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
                finish();
                break;
            case R.id.btn_submit:
//                Map<String ,String > params=new HashMap<>();
//                params.put("customer_id", AppConfig.CustomerId);
//                params.put("op_customer_id",opCustomerId);
//                params.put("judge_level",evaluate);
//                params.put("video_id",video_id);
//                params.put("description",content.getText()==null?"":content.getText().toString());
//                api.postJudce(params, this, new DataIdCallback<String>() {
//                    @Override
//                    public void onSuccess(String data, int id) {
//                        ViewInject.shortToast(getApplication(),"评价提交成功");
//                        finish();
//                    }
//                    @Override
//                    public void onFailed(String errCode, String errMsg, int id) {
//                    }
//                });
                finish();
                break;
        }
    }
}
