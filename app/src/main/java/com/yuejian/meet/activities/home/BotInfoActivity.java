package com.yuejian.meet.activities.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.EnjoyEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目详情
 */
public class BotInfoActivity extends BaseActivity {
    @Bind(R.id.holp)
    ImageView holp;
    @Bind(R.id.bot_info_logo)
    ImageView bot_info_logo;
    @Bind(R.id.bot_info_title)
    TextView bot_info_title;
    @Bind(R.id.bot_info_job)
    TextView bot_info_job;
    @Bind(R.id.ch_1)
    TextView ch_1;
    @Bind(R.id.ch_2)
    TextView ch_2;
    @Bind(R.id.ch_3)
    TextView ch_3;
    @Bind(R.id.bot_info_intro)
    TextView bot_info_intro;
    @Bind(R.id.tet_site)
    TextView tet_site;

    @Bind(R.id.user_photo)
    ImageView user_photo;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.bot_info_time)
    TextView bot_info_time;
    @Bind(R.id.bot_info_attention)
    Button bu_chat;
    @Bind(R.id.bot_info_bot_layout)
    LinearLayout bot_info_bot_layout;
    @Bind(R.id.bot_info_ta_layout)
    LinearLayout bot_info_ta_layout;
    BotEntity entity;
    Intent intent;

    String customer_id;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_info);
        initView();
        initData();
    }
    public void initView(){
        holp.setVisibility(View.VISIBLE);
        intent=getIntent();
        customer_id=intent.getStringExtra("customer_id");
        id=intent.getStringExtra("id");
    }
    public void initData(){
        setTitleText(getString(R.string.bot_txt_1));
        getRequestDataTa();
        if (customer_id.equals(user.getCustomer_id())){
            bot_info_bot_layout.setVisibility(View.VISIBLE);
            bot_info_ta_layout.setVisibility(View.GONE);
//            getRequestData();
        }else {
            bot_info_bot_layout.setVisibility(View.GONE);
            bot_info_ta_layout.setVisibility(View.VISIBLE);
        }
    }
    public void getRequestDataTa(){
        Map<String,Object> params=new HashMap<>();
        params.put("id",id);
        params.put("op_customer_id",customer_id);
        params.put("customer_id",user.getCustomer_id());
        apiImp.getBotMy(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity= JSON.parseObject(data, BotEntity.class);
                initLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void initLayout(){
        Glide.with(this).load(entity.getLogo()).into(bot_info_logo);
        bot_info_title.setText(entity.getTitle());
        bot_info_job.setText(entity.getIndustry());
        String[] array=entity.getFacing_problems().split(",");
        if (array.length>=1){
            ch_1.setVisibility(View.VISIBLE);
            ch_1.setText(array[0]);
        }
        if (array.length>=2){
            ch_2.setVisibility(View.VISIBLE);
            ch_2.setText(array[1]);
        }
        if (array.length>=3){
            ch_3.setVisibility(View.VISIBLE);
            ch_3.setText(array[2]);
        }
        bot_info_intro.setText(entity.getIntro());
        tet_site.setText(entity.getCompany_address());
        if (!customer_id.equals(user.getCustomer_id())){
            Glide.with(this).load(entity.getPhoto()).error(R.mipmap.ic_default).into(user_photo);
            user_name.setText(entity.getSurname()+entity.getName());
            bot_info_time.setText(TimeUtils.getBugTimeTwo(Long.parseLong(entity.getGmt_create())));
            if (entity.getIs_attention() != 0){
                bu_chat.setSelected(true);
                bu_chat.setText(R.string.attention2);
            }
        }

    }
    public void del(){
        Map<String,Object> params=new HashMap<>();
        params.put("id",id);
        apiImp.delBot(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.holp,R.id.bu_chat,R.id.edit_but,R.id.del_but,R.id.bot_info_attention})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.holp:
                intent= new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.getWebUrl()+"clanEnjoyAssociationMeeting/help.html");
                startActivity(intent);
                break;
            case R.id.bu_chat:
                ImUtils.toP2PCaht(this,customer_id);
                break;
            case R.id.edit_but:
                setResult(RESULT_OK);
                finish();
                intent=new Intent(this,CreateBotActivity.class);
                intent.putExtra("id",entity.getId());
                startActivity(intent);
                break;
            case R.id.del_but:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle(R.string.hint);
                builder.setMessage(R.string.bot_txt_2);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        del();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
                break;
            case R.id.bot_info_attention:
//                if (!bu_chat.getText().toString().equals(getString(R.string.attention2)))
                    focusCustomer(user.getCustomer_id(),entity.getCustomer_id());
                break;

        }
    }
    private void focusCustomer(String customerId, String opCustomerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("bind_type", bu_chat.getText().toString().equals(getString(R.string.attention2))?String.valueOf(2):"1");
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (bu_chat.getText().toString().equals(getString(R.string.attention2))){
                    bu_chat.setText(R.string.bot_txt_4);
                    bu_chat.setSelected(false);
                }else {
                    bu_chat.setText(R.string.attention2);
                    bu_chat.setSelected(true);
                }
            }


            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
}

