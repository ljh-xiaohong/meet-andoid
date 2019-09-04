package com.yuejian.meet.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.InviteOriginateActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupUserLimitsInfoEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 群管理权限
 * Created by zh03 on 2017/8/29.
 */

public class GroupUserLimits {
    ImageView group_user_close;
    ImageView headerImg;
    TextView txt_group_user_homepage,group_user_name,group_user_sex,group_user_job,txt_group_user_add_attention,txt_group_user_attention;
    ImageView img_user_phone,img_user_weixin,img_user_identity,img_member_enterprise,img_group_user_official;
    TextView group_user_chat,txt_group_user_distance,group_user_limits,txt_group_user_official;
    RelativeLayout group_user_redcard,group_user_yellowcard,group_user_warning,group_user_jurisdiction_layout;
    IMMessage message;
    GroupUserLimitsInfoEntity entity;
    Context context;
    ApiImp api=new ApiImp();
    Dialog dialog;
    public void showDialog(final Context activity,IMMessage message) {
        this.context=activity;
        this.message=message;
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        View view=View.inflate(activity, R.layout.dialog_group_message_user_info, null);
        initView(view);
        builder.setView(view);
        dialog=builder.show();
        dialog.show();
        initData();
    }
    public void initView(View view){
        group_user_close= (ImageView) view.findViewById(R.id.group_user_close);
        headerImg= (ImageView) view.findViewById(R.id.img_group_user_portrait);
        txt_group_user_homepage= (TextView) view.findViewById(R.id.txt_group_user_homepage);
        group_user_name= (TextView) view.findViewById(R.id.group_user_name);//用户名
        group_user_sex= (TextView) view.findViewById(R.id.group_user_sex);//用户性别
        group_user_job= (TextView) view.findViewById(R.id.group_user_job);//用户职业
        txt_group_user_add_attention= (TextView) view.findViewById(R.id.txt_group_user_add_attention);//用户关注
        txt_group_user_attention= (TextView) view.findViewById(R.id.txt_group_user_attention);//用户关注,被赞，粉丝
        img_user_phone= (ImageView) view.findViewById(R.id.img_user_phone);//手机
        img_user_weixin= (ImageView) view.findViewById(R.id.img_user_weixin);//微信
        img_user_identity= (ImageView) view.findViewById(R.id.img_user_identity);//身份
        img_member_enterprise= (ImageView) view.findViewById(R.id.img_member_enterprise);//企业
        img_group_user_official= (ImageView) view.findViewById(R.id.img_group_user_official);//官方log
        group_user_chat= (TextView) view.findViewById(R.id.group_user_chat);//私信
        txt_group_user_distance= (TextView) view.findViewById(R.id.txt_group_user_distance);//距离
        group_user_limits= (TextView) view.findViewById(R.id.group_user_limits);//设置为管理员
        txt_group_user_official= (TextView) view.findViewById(R.id.txt_group_user_official);//官方发起人
        group_user_warning= (RelativeLayout) view.findViewById(R.id.group_user_warning);//警告
        group_user_yellowcard= (RelativeLayout) view.findViewById(R.id.group_user_yellowcard);//黄牌
        group_user_redcard= (RelativeLayout) view.findViewById(R.id.group_user_redcard);//红牌
        group_user_jurisdiction_layout= (RelativeLayout) view.findViewById(R.id.group_user_jurisdiction_layout);//权限layout

        group_user_close.setOnClickListener(click);
        txt_group_user_homepage.setOnClickListener(click);
        group_user_warning.setOnClickListener(click);
        group_user_yellowcard.setOnClickListener(click);
        group_user_redcard.setOnClickListener(click);
        group_user_chat.setOnClickListener(click);
        txt_group_user_add_attention.setOnClickListener(click);
        group_user_limits.setOnClickListener(click);

    }
    public void setData(){
        group_user_jurisdiction_layout.setVisibility(View.GONE);
        group_user_name.setText(entity.getSurname()+entity.getName());
        group_user_sex.setSelected(entity.getSex().equals("0")?false:true);
        group_user_sex.setText(" "+entity.getAge());
        group_user_job.setText(entity.getJob()==null?"":entity.getJob());
        txt_group_user_add_attention.setSelected(entity.getWe_relation().equals("0")?false:true);
        txt_group_user_add_attention.setText(entity.getWe_relation().equals("0")?"加关注":"已关注");
        txt_group_user_attention.setText("关注"+entity.getAttention_cnt()+"|粉丝"+entity.getFans_cnt()+"|被赞"+entity.getFans_cnt());
        img_user_phone.setSelected(entity.getIs_mobile_certified().equals("0")?false:true);
        img_user_weixin.setSelected(entity.getIs_weixin_certified().equals("0")?false:true);
        img_user_identity.setSelected(entity.getIs_idcard_certified().equals("0")?false:true);
        img_member_enterprise.setSelected(entity.getIs_business_license_certified().equals("0")?false:true);
        txt_group_user_distance.setText(entity.getDistance()+"km");
        Glide.with(context).load(entity.getPhoto()).error(R.mipmap.ic_default).into(headerImg);
        img_group_user_official.setVisibility(View.GONE);
        txt_group_user_official.setVisibility(View.GONE);
//        if (entity.getIs_family_master()>0){
//            txt_group_user_official.setVisibility(View.VISIBLE);
//            img_group_user_official.setVisibility(View.VISIBLE);
//            txt_group_user_official.setText(entity.getMaster_area_name()+"官方发起人");
//        }
        if (entity.getIs_super()==FqrEnum.city.getValue()){
            txt_group_user_official.setVisibility(View.VISIBLE);
            txt_group_user_official.setText(entity.getMaster_area_name()+"姓氏文化传承使者");
        }
        if (entity.getIs_super()>0){
            img_group_user_official.setVisibility(View.VISIBLE);
            Glide.with(context).load(entity.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:entity.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(img_group_user_official);
        }
        //城市发起人或管理员可以禁言
        if (entity.getMy_relation_type().equals("2")||entity.getMy_relation_type().equals("3")){
            group_user_jurisdiction_layout.setVisibility(View.VISIBLE);
            if (entity.getMy_relation_type().equals("2")){
                group_user_redcard.setVisibility(View.GONE);
                group_user_yellowcard.setVisibility(View.GONE);
            }
        }
        if (!entity.getGroup_permit().equals("0")){
            group_user_redcard.setSelected(true);
            group_user_yellowcard.setSelected(true);
            group_user_warning.setSelected(true);
            return;
        }
        //城市发起人可以设置管理员
        if (entity.getMy_relation_type().equals("3")&&!entity.getRelation_type().equals("0")){
            group_user_limits.setVisibility(View.VISIBLE);
        }
        if (entity.getRelation_type().equals("2")){
            group_user_limits.setSelected(true);
            group_user_limits.setText("撒消管理员");
        }
    }
    public void initData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("op_customer_id",message.getFromAccount());
        params.put("t_id",message.getSessionId());
        params.put("longitude",AppConfig.slongitude);
        params.put("latitude",AppConfig.slatitude);
        api.getGroupMemberInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity= JSON.parseObject(data,GroupUserLimitsInfoEntity.class);
                setData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    View.OnClickListener click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.group_user_close:
                    if (dialog!=null)dialog.dismiss();
                    break;
                case R.id.group_user_chat:
                    ImUtils.toP2PCaht(context,message.getFromAccount());
                    break;
                case R.id.txt_group_user_homepage://个人主页
                    AppUitls.goToPersonHome(context, message.getFromAccount());
                    break;
                case R.id.group_user_redcard://红牌
                    setBanned(red);
                    break;
                case R.id.group_user_yellowcard://黄牌
                    setBanned(yellow);
                    break;
                case R.id.group_user_warning://警告
                    setBanned(caution);
                    break;
                case R.id.group_user_limits://设置管理员
                    if (entity.getRelation_type().equals("2")){
                        setUser(false);
                    }else {
                        setUser(true);
                    }
                    break;
                case R.id.txt_group_user_add_attention://关注
                    focusCustomer(AppConfig.CustomerId, message.getFromAccount(), Constants.ADD_FOCUS);
                    break;
            }
        }
    };
    //设置管理员
    public void setUser(final Boolean is_op_down){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("op_customer_id",message.getFromAccount());
        params.put("t_id",message.getSessionId());
        params.put("is_op_down",is_op_down?"1":"0");
        api.setGroupAdmin(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (is_op_down){
                    entity.setRelation_type("2");
                    group_user_limits.setSelected(true);
                    group_user_limits.setText("撒消管理员");
                }else {
                    entity.setRelation_type("1");
                    group_user_limits.setSelected(false);
                    group_user_limits.setText("设为管理员");
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    //设置禁言
    public void setBanned(String stype){
        if (!entity.getGroup_permit().equals("0")){
            ViewInject.shortToast(context,"已经禁言");
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("op_customer_id",message.getFromAccount());
        params.put("t_id",message.getSessionId());
        params.put("permit",stype);
        api.setSilenced(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                group_user_redcard.setSelected(true);
                group_user_yellowcard.setSelected(true);
                group_user_warning.setSelected(true);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    //设置关注
    private void focusCustomer(String customerId, String opCustomerId, int bindType) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("bind_type", String.valueOf(bindType));
        api.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                txt_group_user_add_attention.setSelected(true);
                txt_group_user_add_attention.setText("已关注");
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    static String red="3";//红牌
    static String yellow="2";//黄牌
    static String caution="1";//警告

}
