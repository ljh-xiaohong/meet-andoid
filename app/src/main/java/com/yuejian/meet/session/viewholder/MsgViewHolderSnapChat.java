package com.yuejian.meet.session.viewholder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.NetWorkUtils;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.ChatFeeType;
import com.netease.nim.uikit.app.SendMessageEnum;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.session.activity.WatchSnapChatPictureActivity;
import com.yuejian.meet.session.extension.SnapChatAttachment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSnapChat extends MsgViewHolderBase {

    private ImageView thumbnailImageView;

    protected View progressCover;
    private TextView progressLabel;
    private TextView hint_consume;///阅后即焚提示收费
    private boolean isLongClick = false;
    private NetApi api=new NetApi();

    public MsgViewHolderSnapChat(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_snapchat;
    }

    @Override
    protected void inflateContentView() {
        thumbnailImageView = (ImageView) view.findViewById(R.id.message_item_snap_chat_image);
        hint_consume= (TextView) view.findViewById(R.id.hint_consume);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
        progressLabel = (TextView) view.findViewById(R.id.message_item_thumb_progress_text);
    }

    @Override
    protected void bindContentView() {
        contentContainer.setOnTouchListener(onTouchListener);

        layoutByDirection();

        refreshStatus();
        hintConsumeType();
    }

    private void refreshStatus() {
        thumbnailImageView.setBackgroundResource(isReceivedMessage() ? R.drawable.message_view_holder_left_snapchat : R.drawable.message_view_holder_right_snapchat);

        if (message.getStatus() == MsgStatusEnum.sending || message.getAttachStatus() == AttachStatusEnum.transferring) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressCover.setVisibility(View.GONE);
        }

        progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
        readReceiptTextView.setVisibility(View.GONE);
    }
    //设置是否显示收费提示字眼
    private void hintConsumeType(){
        hint_consume.setVisibility(View.GONE);
        if (message.getDirect()== MsgDirectionEnum.In&&message.getMsgType().getValue()==100&& AppConfig.UserSex.equals("1")&&message.getStatus()!=MsgStatusEnum.read){
            hint_consume.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onItemClick() {
        if (message.getDirect()== MsgDirectionEnum.In&&message.getMsgType().getValue()==100&& AppConfig.UserSex.equals("1")&&message.getStatus()!=MsgStatusEnum.read){
            showDailog();
        }else{
            Toast.makeText(context,"长按查看",Toast.LENGTH_SHORT).show();
        }

    }

    ///提示框
    public void showDailog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("收费提示");
        dialog.setMessage("查看即焚50金币");
        dialog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requstMoney();///请求服务扣费
            }
        });
        dialog.setNegativeButton("取消",null);
        dialog.show();
//        FragmentActivity activity = null;
//        if(view.getContext() instanceof FragmentActivity){
//
//            activity = (FragmentActivity) view.getContext();
//        }
//        if(activity != null){
//            new CircleDialog.Builder(activity)
//                    .setTitle("标题")
//                    .setText("查看即焚50金币").setNegative("取消",null)
//                    .setPositive("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            requstMoney();///请求服务扣费
//
////                            PreferencesIm.write(view.getContext(),PreferencesIm.isShapCharge,true);
//                        }
//                    })
//                    .show();
//        }
    }
    ExpenseEntity expenseEntity=new ExpenseEntity();
    MonyEntity monyEntity=new MonyEntity();
    ////阅后即焚扣费请求
    public void requstMoney(){
        api.deductionMoney(AppConfig.CustomerId, message.getSessionId(), ChatFeeType.charPendImg, this,new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                message.setStatus(MsgStatusEnum.read);
                ///更改im数据状态  改为已读
                NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                adapter.notifyDataSetChanged();
                ////保存用户金额信息
                expenseEntity= JSON.parseObject(data,ExpenseEntity.class);
                monyEntity= JSON.parseObject(expenseEntity.getBal(),MonyEntity.class);
                UtilsIm.setMyMoney(context,monyEntity);
                ///发送一条收费消息
                IMMessage newMessage= MessageBuilder.createTextMessage(message.getSessionId(), SessionTypeEnum.P2P,"");
                newMessage.setPushPayload(getPayLoad(SendMessageEnum.MessageSnapRead,expenseEntity.getCost()));
                NIMClient.getService(MsgService.class).sendMessage(newMessage,false);
                adapter.appendData(newMessage);
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                Toast.makeText(view.getContext(),errMsg+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    /**
     * 扣费完成之后进行设置消息
     * @param msgType   消息类型  0
     * @param cost     扣的费用
     * @return
     */
    protected Map<String,Object> getPayLoad(String msgType, String cost){
        Map<String,Object> params=new HashMap<>();
        params.put("MsgType",msgType);
        params.put("cost",cost);
        params.put("MsgId",message.getUuid());
        org.json.JSONObject mapJson=new org.json.JSONObject();
        try {
            ///对方的
            org.json.JSONObject jsonPo=new org.json.JSONObject();
            jsonPo.put("cost",(Double.parseDouble(cost)*0.7));
            jsonPo.put("custmerId",message.getSessionId());
            ///自己的
            org.json.JSONObject jsonSender=new org.json.JSONObject();
            jsonSender.put("cost","-"+cost);
            jsonSender.put("custmerId", AppConfig.CustomerId);

            params.put("op",jsonPo);
            params.put("sender",jsonSender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
    protected View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.getParent().requestDisallowInterceptTouchEvent(false);

                    WatchSnapChatPictureActivity.destroy();

                    // 删除这条消息，当然你也可以将其标记为已读，同时删除附件内容，然后不让再查看
                    if (isLongClick && message.getAttachStatus() == AttachStatusEnum.transferred) {
                        // 物理删除
                        NIMClient.getService(MsgService.class).deleteChattingHistory(message);
                        AttachmentStore.delete(((SnapChatAttachment) message.getAttachment()).getPath());
                        AttachmentStore.delete(((SnapChatAttachment) message.getAttachment()).getThumbPath());

                        getMsgAdapter().deleteItem(message, true);
                        isLongClick = false;
                    }
                    break;
            }

            return false;
        }
    };

    @Override
    protected boolean onItemLongClick() {
        if (AppConfig.UserSex.equals("1")&&message.getDirect()== MsgDirectionEnum.In){
            if (message.getStatus()!=MsgStatusEnum.read){
                Toast.makeText(context,"请单击付费",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

//        if (message.getStatus() == MsgStatusEnum.success) {
//            PreferencesIm.write(view.getContext(),PreferencesIm.isShapCharge,true);
            if (!NetWorkUtils.isNetworkConnected(view.getContext())){
                Toast.makeText(view.getContext(),"网络不可用",Toast.LENGTH_LONG).show();
                return false;
            }
//            requstMoney();
            WatchSnapChatPictureActivity.start(context, message);
            isLongClick = true;
            return true;
//    }
//        return false;
    }


    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    private void layoutByDirection() {
        View body = findViewById(R.id.message_item_snap_chat_body);
        View tipsLayout = findViewById(R.id.message_item_tips_layout);
        View tips = findViewById(R.id.message_item_snap_chat_tips_label);
        View readed = findViewById(R.id.message_item_snap_chat_readed);
        ViewGroup container = (ViewGroup) body.getParent();
        container.removeView(tipsLayout);
        if (isReceivedMessage()) {
            container.addView(tipsLayout, 1);
        } else {
            container.addView(tipsLayout, 0);
        }
        if (message.getStatus() == MsgStatusEnum.success) {
            tips.setVisibility(View.VISIBLE);
        } else {
            tips.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getMsgAdapter().getUuid()) && message.getUuid().equals(getMsgAdapter().getUuid())) {
            readed.setVisibility(View.VISIBLE);
        } else {
            readed.setVisibility(View.GONE);
        }
    }
}
