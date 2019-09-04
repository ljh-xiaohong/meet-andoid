package com.yuejian.meet.session.viewholder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.activity.RedEnvelopeDetailsActivity;
import com.netease.nim.uikit.app.entity.IsRobGiftEntity;
import com.netease.nim.uikit.app.entity.RedEnvelopeDetailsEntity;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.app.widgets.SelectDialog;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 红包
 * Created by zh03 on 2017/8/21.
 */

public class MsgViewHlderRedEnvelope extends MsgViewHolderBase {

    TextView red_title_msg,red_title_right_msg;
    TextView red_get,red_get_right;
    LinearLayout nim_red_left_layout,nim_red_right_layout;
    LinearLayout nim_gift_item_layout;
    ProgressDialog dialog,dialog2;
    private IsRobGiftEntity isRobGiftEntity;
    public MsgViewHlderRedEnvelope(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_red_envelope_msg_view_layout;
    }

    @Override
    protected void inflateContentView() {
        red_title_msg=findViewById(R.id.red_title_msg);
        red_get=findViewById(R.id.red_get);
        red_get_right=findViewById(R.id.red_get_right);
        red_title_right_msg=findViewById(R.id.red_title_right_msg);
        nim_red_left_layout=findViewById(R.id.nim_red_left_layout);
        nim_red_right_layout=findViewById(R.id.nim_red_right_layout);
        nim_gift_item_layout=findViewById(R.id.nim_gift_item_layout);
    }

    @Override
    protected boolean isShowGiftBg() {
        return true;
    }

    @Override
    protected void bindContentView() {
        nim_red_left_layout.setVisibility(View.GONE);
        nim_red_right_layout.setVisibility(View.GONE);
        if (message.getDirect()== MsgDirectionEnum.In){//接收
            nim_red_left_layout.setVisibility(View.VISIBLE);
            nim_gift_item_layout.setBackgroundResource(R.drawable.nim_message_item_red_left_bg);
        }else {
            nim_gift_item_layout.setBackgroundResource(R.drawable.nim_message_item_red_right_bg);
            nim_red_right_layout.setVisibility(View.VISIBLE);
        }
        nim_gift_item_layout.setAlpha(1);
        RedEnvelopeAttachment envelopeAttachment= (RedEnvelopeAttachment) message.getAttachment();
        RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
        if (envelopeEntity.getType()!=null){
            if (envelopeEntity.getType().equals("3")){
                red_get.setText("已领取>>");
                red_get_right.setText("已领取>>");
                nim_gift_item_layout.setAlpha(0.7f);
            }else {
                red_get.setText("点击领取>>");
                red_get_right.setText("点击领取>>");
            }
        }else {
            red_get.setText("点击领取>>");
            red_get_right.setText("点击领取>>");
        }
        red_title_msg.setText(envelopeEntity.getContent());
        red_title_right_msg.setText(envelopeEntity.getContent());
    }

    @Override
    protected void onItemClick() {
        dialog = new ProgressDialog(context);
        //设置风格为圆形
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(null);
        dialog.setIcon(null);
        //设置提示信息
        dialog.setMessage("正在操作..");
        //设置是否可以通过返回键取消
        dialog.setCancelable(true);
        dialog.show();
        RedEnvelopeAttachment envelopeAttachment= (RedEnvelopeAttachment) message.getAttachment();
        RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
        if (envelopeEntity.getType()!=null){
            if (envelopeEntity.getType().equals("3")){
                isGroupCaht();
            }else {
                isChat();
            }
        }else {
            isChat();
        }

    }
    public void isChat(){
        if (AppConfig.chatEnum==ChatEnum.FoundGroup){
            isRobGift();
//            isGroupCaht();
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("t_id",message.getSessionId());
        new NetApi().isGroupCaht(params, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                isRobGift();
//                isGroupCaht();
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }
    //是否可以抢礼物
    public void isRobGift(){
        final RedEnvelopeAttachment envelopeAttachment= (RedEnvelopeAttachment) message.getAttachment();
        final RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
        Map<String,String> params=new HashMap<>();
        params.put("bag_id",envelopeEntity.getBag_id());
        new NetApi().getGiftCountBag(params, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                if (dialog!=null)dialog.dismiss();
                isRobGiftEntity=JSON.parseObject(data,IsRobGiftEntity.class);
                showRobGiftDialog();
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }
    //显示抢红包详情
    public void showRobGiftDialog(){
        final SelectDialog builder=new SelectDialog(context,  R.style.NoBackGroundDialog);
        View view=View.inflate(context, R.layout.rob_gift_dialog_layout, null);
        builder.setView(view);
        builder.show();
        Glide.with(context).load(isRobGiftEntity.getPhoto()).asBitmap().thumbnail(0.1f).into(((ImageView) view.findViewById(R.id.img_rob_gift_photo)));
        ImageView fqr= (ImageView) view.findViewById(R.id.img_rob_faqr);
        fqr.setVisibility(isRobGiftEntity.getIs_super()>0?View.VISIBLE:View.GONE);
        Glide.with(context).load(isRobGiftEntity.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:isRobGiftEntity.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(fqr);
        ((TextView)view.findViewById(R.id.txt_rob_name)).setText(isRobGiftEntity.getSurname()+isRobGiftEntity.getName());
        ((TextView)view.findViewById(R.id.txt_rob_gift_family_name)).setText("欢迎进入"+isRobGiftEntity.getSurname()+"氏家族");
        Glide.with(context).load(isRobGiftEntity.getGift_image()).asBitmap().thumbnail(0.1f).into(((ImageView)view.findViewById(R.id.img_gift_photo)));
        ((TextView)view.findViewById(R.id.txt_rob_gift_name)).setText(isRobGiftEntity.getGift_name());
        ((TextView)view.findViewById(R.id.txt_rob)).setText(isRobGiftEntity.getGift_remaining_cnt()>0?"抢礼物":"查看详情");
        view.findViewById(R.id.txt_rob).setSelected(isRobGiftEntity.getGift_remaining_cnt()>0?false:true);
        view.findViewById(R.id.txt_rob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                dialog.show();
                isGroupCaht();
            }
        });

    }
    public void isGroupCaht(){
//        Map<String,String> params=new HashMap<>();
//        params.put("t_id",message.getSessionId());
//        new NetApi().isGroupCaht(params, this, new DataCallback<String>() {
//            @Override
//            public void onSuccess(String data) {
                final RedEnvelopeAttachment envelopeAttachment= (RedEnvelopeAttachment) message.getAttachment();
                final RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
                Map<String,String> params=new HashMap<>();
                    params.put("bag_id",envelopeEntity.getBag_id());
                new NetApi().bagGroupRedPacket(params, this, new DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        RedEnvelopeDetailsEntity groupGiftSendEntity= JSON.parseObject(data,RedEnvelopeDetailsEntity.class);
                        if (groupGiftSendEntity.getIs_rob().equals("1")){
                            envelopeEntity.setType("3");
                            envelopeAttachment.setData((JSONObject) JSON.toJSON(envelopeEntity));
                            message.setAttachment(envelopeAttachment);
                            ///更改im数据状态  改为已读
                            NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                            red_get.setText("已领取>>");
                            red_get_right.setText("已领取>>");
                        }

                        Intent intent=new Intent(context, RedEnvelopeDetailsActivity.class);
                        intent.putExtra("redData",data);
                        context.startActivity(intent);
                        if (dialog!=null)dialog.dismiss();
                    }
                    @Override
                    public void onFailed(String errCode, String errMsg) {
                        if (dialog!=null)dialog.dismiss();
                    }
                });
//            }
//            @Override
//            public void onFailed(String errCode, String errMsg) {
//                if (dialog!=null)dialog.dismiss();
//            }
//        });
    }
}
