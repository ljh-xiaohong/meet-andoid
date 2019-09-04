package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 本次视频通话时长记录
 * Created by zh03 on 2017/7/15.
 */

public class AvChatServiceInfoActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.txt_service_time)
    TextView service_time;
    @Bind(R.id.txt_service_consumption)
    TextView service_consumption;
    @Bind(R.id.txt_service_consumption_label)
    TextView txt_service_consumption_label;
    @Bind(R.id.txt_consumption_money_label)
    TextView txt_consumption_money_label;
    @Bind(R.id.txt_consumption_money)
    TextView txt_consumption_money;
    @Bind(R.id.btn_service_to_evaluate)
    Button btn_service_to_evaluate;

    public String duration;
    public String cost;
    public String opCustomerId;
    public String video_id;
    Boolean isMiStart=false;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);
        intent=getIntent();
        duration=intent.getStringExtra("duration");
        opCustomerId=intent.getStringExtra("opCustomerId");
        isMiStart=intent.getBooleanExtra("isMiStart",false);
        video_id=intent.getStringExtra("video_id");
        cost=intent.getStringExtra("cost");
        initData();
    }
    public void initData(){
        DecimalFormat df=new DecimalFormat("0.00");

        service_time.setText(duration+"分钟");
        service_consumption.setText(df.format(Double.parseDouble(cost))+"金币");
        if (!isMiStart){
            txt_consumption_money_label.setText("平台费");
            txt_service_consumption_label.setText("本次收益");
            service_consumption.setText(df.format((Double.parseDouble(cost)*0.7))+"金币");
            txt_consumption_money.setText((df.format(Double.parseDouble(cost)*0.3))+"金币");
        }
    }

    @OnClick({R.id.btn_service_to_evaluate,R.id.imgBtn_service_close})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_service_to_evaluate:////评价
                intent=new Intent(getApplication(),AvChatEvaluateActivity.class);
                intent.putExtra("opCustomerId",opCustomerId);
                intent.putExtra("video_id",video_id);
                startActivity(intent);
                finish();
                break;
            case R.id.imgBtn_service_close:////关闭
                finish();
                break;
        }
    }
}
