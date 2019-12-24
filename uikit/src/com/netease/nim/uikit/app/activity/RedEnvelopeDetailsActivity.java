package com.netease.nim.uikit.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.Adapter.RedEnvelopeDetailsAdapter;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GroupGiftSendEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.entity.RedEnvelopeDetailsEntity;
import com.netease.nim.uikit.app.entity.RedEnvelopeDetailsSeedEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 抢到红包详情
 */
public class RedEnvelopeDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img_red_header;
    ImageButton titlebar_imgBtn_back;
    TextView txt_red_start_name,txt_start_content;
    ListView red_list;
    TextView red_envelope_count;
    String redData;
    List<RedEnvelopeDetailsSeedEntity> listData=new ArrayList<>();
    RedEnvelopeDetailsAdapter mAdapter;
    View viewHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelope_details);
        initData();
        getMiMoney();
    }
    public void getMiMoney(){
        new NetApi().getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                MonyEntity monyEntity= JSON.parseObject(data,MonyEntity.class);
                UtilsIm.setMyMoney(getApplication(),monyEntity);
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void initData(){
        viewHeader=View.inflate(this,R.layout.item_red_envelope_header,null);
        red_envelope_count=(TextView) viewHeader.findViewById(R.id.red_envelope_count);
        Intent intent=getIntent();
        redData=intent.getStringExtra("redData");
        img_red_header= (ImageView) findViewById(R.id.img_red_header);
        txt_red_start_name= (TextView) findViewById(R.id.txt_red_start_name);
        txt_start_content= (TextView) findViewById(R.id.txt_start_content);
        titlebar_imgBtn_back= (ImageButton) findViewById(R.id.titlebar_imgBtn_back);
        red_list= (ListView) findViewById(R.id.red_list);

        titlebar_imgBtn_back.setOnClickListener(this);
        RedEnvelopeDetailsEntity groupGiftSendEntity= JSON.parseObject(redData,RedEnvelopeDetailsEntity.class);
        listData=JSON.parseArray(groupGiftSendEntity.getCustomer_list(),RedEnvelopeDetailsSeedEntity.class);

        Glide.with(this).load(groupGiftSendEntity.getPhoto()).asBitmap().into(img_red_header);
        txt_red_start_name.setText(groupGiftSendEntity.getName());
        txt_start_content.setText(groupGiftSendEntity.getContent());

        red_list.addHeaderView(viewHeader);
        mAdapter=new RedEnvelopeDetailsAdapter(red_list,this,listData);
        red_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        red_envelope_count.setText(groupGiftSendEntity.getGift_cnt()+groupGiftSendEntity.getGift_name()+","+"剩余"+groupGiftSendEntity.getGift_remaining_cnt());

    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if (viewId==R.id.titlebar_imgBtn_back){
            finish();
        }
    }
}
