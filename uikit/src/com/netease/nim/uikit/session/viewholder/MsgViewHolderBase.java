package com.netease.nim.uikit.session.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.ChatFeeType;
import com.netease.nim.uikit.app.SendMessageEnum;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.MessageExtendEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.http.HttpClientWrapper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.custom.DefaultUserInfoProvider;
import com.netease.nim.uikit.session.module.list.MsgAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话窗口消息列表项的ViewHolder基类，负责每个消息项的外层框架，包括头像，昵称，发送/接收进度条，重发按钮等。<br>
 * 具体的消息展示项可继承该基类，然后完成具体消息内容展示即可。
 */
public abstract class MsgViewHolderBase extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, IMMessage> {

    public MsgViewHolderBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        this.adapter = adapter;
    }

    // basic
    protected View view;
    protected Context context;
    protected BaseMultiItemFetchLoadAdapter adapter;

    // data
    protected IMMessage message;

    // view
    protected View alertButton;
    protected TextView timeTextView;
    protected ProgressBar progressBar;
    protected TextView nameTextView;
    protected TextView cityTextView;
    protected FrameLayout contentContainer;
    protected LinearLayout nameContainer;
    protected TextView readReceiptTextView;

    private ImageView avatarLeft;
    private ImageView avatarRight;
    private RelativeLayout message_item_left_layout;
    private RelativeLayout message_item_right_layout;
    private ImageView message_item_left_fqr_image,message_item_ritht_fqr_image;

    public ImageView nameIconView;

    // contentContainerView的默认长按事件。如果子类需要不同的处理，可覆盖onItemLongClick方法
    // 但如果某些子控件会拦截触摸消息，导致contentContainer收不到长按事件，子控件也可在inflate时重新设置
    protected View.OnLongClickListener longClickListener;

    /// -- 以下接口可由子类覆盖或实现
    // 返回具体消息类型内容展示区域的layout res id
    abstract protected int getContentResId();

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    // 内容区域长按事件响应处理。该接口的优先级比adapter中有长按事件的处理监听高，当该接口返回为true时，adapter的长按事件监听不会被调用到。
    protected boolean onItemLongClick() {
        return false;
    }

    // 当是接收到的消息时，内容区域背景的drawable id
    protected int leftBackground() {
        return R.drawable.nim_message_item_left_selector;
    }

    // 当是发送出去的消息时，内容区域背景的drawable id
    protected int rightBackground() {
        return R.drawable.nim_message_item_right_selector;
    }

    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return false;
    }

    // 是否显示头像，默认为显示
    protected boolean isShowHeadImage() {
        return true;
    }

    // 是否显示气泡背景，默认为显示
    protected boolean isShowBubble() {
        return true;
    }
    // 是显示红包背景图上
    protected boolean isShowGiftBg() {
        return false;
    }

    /// -- 以下接口可由子类调用
    protected final MsgAdapter getMsgAdapter() {
        return (MsgAdapter) adapter;
    }

    /**
     * 下载附件/缩略图
     */
    protected void downloadAttachment() {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }

    // 根据layout id查找对应的控件
    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }

    // 判断消息方向，是否是接收到的消息
    protected boolean isReceivedMessage() {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    /// -- 以下是基类实现代码
    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        view = holder.getConvertView();
        context = holder.getContext();
        message = data;

        if(message.getMsgType() == MsgTypeEnum.audio) {
            Log.i("huangjun", "convert msg=" + message.getUuid());
        }

        inflate();
        refresh();
    }

    protected final void inflate() {
        timeTextView = findViewById(R.id.message_item_time);
        avatarLeft = findViewById(R.id.message_item_portrait_left);
        avatarRight = findViewById(R.id.message_item_portrait_right);
        message_item_left_layout=findViewById(R.id.message_item_left_layout);
        message_item_right_layout=findViewById(R.id.message_item_right_layout);
        message_item_ritht_fqr_image=findViewById(R.id.message_item_ritht_fqr_image);
        message_item_left_fqr_image=findViewById(R.id.message_item_left_fqr_image);
        alertButton = findViewById(R.id.message_item_alert);
        progressBar = findViewById(R.id.message_item_progress);
        nameTextView = findViewById(R.id.message_item_nickname);
        cityTextView = findViewById(R.id.message_item_city);
        contentContainer = findViewById(R.id.message_item_content);
        nameIconView = findViewById(R.id.message_item_name_icon);
        nameContainer = findViewById(R.id.message_item_name_layout);
        readReceiptTextView = findViewById(R.id.textViewAlreadyRead);
        // 这里只要inflate出来后加入一次即可
        if(contentContainer.getChildCount() == 0) {
            View.inflate(view.getContext(), getContentResId(), contentContainer);
        }
        inflateContentView();
    }

    protected final void refresh() {
        contentContainer.setVisibility(View.VISIBLE);
//        if (message.getMsgType().getValue()==0 && (message.getContent()==null || message.getContent().equals(""))){
//            ///解锁之后发空消息过来只显示扣费
//            contentContainer.setVisibility(View.GONE);
//            avatarLeft.setVisibility(View.GONE);
//            avatarRight.setVisibility(View.GONE);
//            readReceiptTextView.setVisibility(View.GONE);
//        }else{;

            setHeadImageView();
            setNameTextView();
            setTimeTextView();
            setStatus();
            setOnClickListener();
            setLongClickListener();
            setContent();
            setReadReceipt();

            bindContentView();
//        }
    }


    public void refreshCurrentItem() {
        if (message != null) {
            refresh();
        }
    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (!isShowTime())return;
        if (getMsgAdapter().needShowTime(message)) {
            timeTextView.setVisibility(View.VISIBLE);
        } else {
            timeTextView.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(message.getTime(), false);
        timeTextView.setText(text);
    }
    //是否显示时间
    public Boolean isShowTime(){
        return true;
    }

    /**
     * 设置消息发送状态
     */
    private void setStatus() {
        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.VISIBLE);
                break;
            case sending:
                progressBar.setVisibility(View.VISIBLE);
                alertButton.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
                break;
        }
    }

    private void setHeadImageView() {
        ImageView show = isReceivedMessage() ? avatarLeft : avatarRight;
        ImageView hide = isReceivedMessage() ? avatarRight : avatarLeft;
        ImageView fqeshow = isReceivedMessage() ? message_item_left_fqr_image : message_item_ritht_fqr_image;
        ImageView fqrhide = isReceivedMessage() ? message_item_ritht_fqr_image : message_item_left_fqr_image;
        RelativeLayout showLayout = isReceivedMessage() ? message_item_left_layout : message_item_right_layout;
        RelativeLayout hideLayout = isReceivedMessage() ? message_item_right_layout : message_item_left_layout;
        hideLayout.setVisibility(View.GONE);
        showLayout.setVisibility(View.GONE);
        hide.setVisibility(View.GONE);
        if (!isShowHeadImage()) {
            show.setVisibility(View.GONE);
            showLayout.setVisibility(View.GONE);
            return;
        }
        if (isMiddleItem()) {
            showLayout.setVisibility(View.GONE);
            show.setVisibility(View.GONE);
        } else {
            if (message != null && AppConfig.sponsorId.equals(message.getFromAccount()) &&message.getSessionType()==SessionTypeEnum.Team){
                showLayout.setVisibility(View.VISIBLE);
            }
            show.setVisibility(View.VISIBLE);
            final NimUserInfo userInfo = getUserInfo();
            if (userInfo==null){
                new DefaultUserInfoProvider(context).getUserInfo(message.getFromAccount());
            }else if (message != null && message.getSessionType()==SessionTypeEnum.Team){
                MessageExtendEntity extendEntity= JSON.parseObject(userInfo.getExtension(),MessageExtendEntity.class);
                if (extendEntity.getIs_super()>0){
                    showLayout.setVisibility(View.VISIBLE);
                    Glide.with(context).load(extendEntity.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:extendEntity.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(fqeshow);
                }
            }
//                getUserInfo(message.getFromAccount());
            Glide.with(context).load(userInfo!=null?userInfo.getAvatar():"").error(R.mipmap.ic_default).into(show);
//            show.loadBuddyAvatar(message.getFromAccount());
        }

    }
    //获取用户资料
    public NimUserInfo getUserInfo(){
//        return new DefaultUserInfoProvider(context).getUserInfo(message.getFromAccount());
        return NimUserInfoCache.getInstance().getUserInfo(message.getFromAccount());
    }

    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getMsgAdapter().getEventListener() != null) {
            alertButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getMsgAdapter().getEventListener().onFailedBtnClick(message);
                }
            });
        }

        // 内容区域点击事件响应， 相当于点击了整项
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///判断是否是小视频并且是收到的视频为男性用户
//                if (message.getDirect()== MsgDirectionEnum.In&& AppConfig.UserSex.equals("1")&&message.getMsgType()==MsgTypeEnum.video&&message.getStatus()!=MsgStatusEnum.read){
//                    showVideoDailog();
//                }else{
                    onItemClick();
//                }
            }
        });


        // 头像点击事件响应
        if (NimUIKit.getSessionListener() != null) {
            View.OnClickListener portraitListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NimUIKit.getSessionListener().onAvatarClicked(context, message);
                }
            };
            avatarLeft.setOnClickListener(portraitListener);
            avatarRight.setOnClickListener(portraitListener);
        }
    }
    ///收费提示
    public void showVideoDailog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("收费提示");
        dialog.setMessage("查看小视频要收费100金币");
        dialog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requstVideoMoney();
            }
        });
        dialog.setNegativeButton("取消",null);




        dialog.show();
    }
    NetApi api=new NetApi();
    ExpenseEntity expenseEntity=new ExpenseEntity();
    MonyEntity monyEntity=new MonyEntity();
    ////视频扣费请求
    public void requstVideoMoney(){
        api.deductionMoney(AppConfig.CustomerId, message.getSessionId(), ChatFeeType.charVideo, this,new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                message.setStatus(MsgStatusEnum.read);
                ///更改im数据状态  改为已读
                NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                adapter.notifyDataSetChanged();
                ////保存用户金额信息
                expenseEntity= JSON.parseObject(data,ExpenseEntity.class);
                monyEntity=JSON.parseObject(expenseEntity.getBal(),MonyEntity.class);
                UtilsIm.setMyMoney(context,monyEntity);
                ///发送一条收费消息
                IMMessage newMessage= MessageBuilder.createTextMessage(message.getSessionId(), SessionTypeEnum.P2P,"");
                newMessage.setPushPayload(getPayLoad(SendMessageEnum.MessageVideoRead,expenseEntity.getCost()));
                NIMClient.getService(MsgService.class).sendMessage(newMessage,false);
                adapter.appendData(newMessage);
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                Toast.makeText(view.getContext(),errMsg+"",Toast.LENGTH_SHORT).show();
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
        params.put("isOpen",1);
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
            jsonSender.put("custmerId",AppConfig.CustomerId);

            params.put("op",jsonPo);
            params.put("sender",jsonSender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
    /**
     * item长按事件监听
     */
    private void setLongClickListener() {
        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 优先派发给自己处理，
                if (!onItemLongClick()) {
                    if (getMsgAdapter().getEventListener() != null) {
                        getMsgAdapter().getEventListener().onViewHolderLongClick(contentContainer, view, message);
                        return true;
                    }
                }
                return false;
            }
        };
        // 消息长按事件响应处理
        contentContainer.setOnLongClickListener(longClickListener);
//        message_item_left_layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (message.getSessionType() == SessionTypeEnum.Team){
//                    TeamMember teamMember= NIMClient.getService(TeamService.class).queryTeamMemberBlock(message.getSessionId(),message.getFromAccount());
//                    if (null==teamMember){
//                        NIMClient.getService(TeamService.class).queryTeamMember(message.getSessionId(),message.getFromAccount());
//                        teamMember= NIMClient.getService(TeamService.class).queryTeamMemberBlock(message.getSessionId(),message.getFromAccount());
//                    }
//                    if (teamMember!=null){
//                        getMsgAdapter().getEventListener().onViewUserPotoLoneClick(teamMember);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        // 头像长按事件响应处理
        if (NimUIKit.getSessionListener() != null) {
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NimUIKit.getSessionListener().onAvatarLongClicked(context, message);
                    return true;
                }
            };
            avatarRight.setOnLongClickListener(longClickListener);
            avatarLeft.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (message.getSessionType() == SessionTypeEnum.Team){
                        TeamMember teamMember= NIMClient.getService(TeamService.class).queryTeamMemberBlock(message.getSessionId(),message.getFromAccount());
                        if (null==teamMember){
                            NIMClient.getService(TeamService.class).queryTeamMember(message.getSessionId(),message.getFromAccount());
                            teamMember= NIMClient.getService(TeamService.class).queryTeamMemberBlock(message.getSessionId(),message.getFromAccount());
                        }
                        if (teamMember!=null){
                            getMsgAdapter().getEventListener().onViewUserPotoLoneClick(teamMember);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public void setNameTextView() {
        if (message.getSessionType() == SessionTypeEnum.Team && isReceivedMessage() && !isMiddleItem()) {
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(TeamDataCache.getInstance().getTeamMemberDisplayName(message.getSessionId(), message
                    .getFromAccount()));
            final NimUserInfo userInfo = getUserInfo();
            if (userInfo!=null){
                cityTextView.setVisibility(View.VISIBLE);
                MessageExtendEntity extendEntity= JSON.parseObject(userInfo.getExtension(),MessageExtendEntity.class);
                cityTextView.setText(extendEntity.getFamily_area()==null?"":extendEntity.getFamily_area());
            }else {
                cityTextView.setVisibility(View.GONE);
            }
        } else {
            nameTextView.setVisibility(View.GONE);
            cityTextView.setVisibility(View.GONE);
        }
    }

    private void setContent() {
        if (!isShowBubble() && !isMiddleItem()) {
            return;
        }

        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.message_item_body);

        // 调整container的位置
        int index = isReceivedMessage() ? 0 : 3;
        if (bodyContainer.getChildAt(index) != contentContainer) {
            bodyContainer.removeView(contentContainer);
            bodyContainer.addView(contentContainer, index);
        }

        if (isMiddleItem()) {
            setGravity(bodyContainer, Gravity.CENTER);
        } else {
            if (isReceivedMessage()) {
                setGravity(bodyContainer, Gravity.LEFT);
                if (isShowGiftBg()){
                    contentContainer.setBackgroundColor(Color.parseColor("#00000000"));
                }else {
                    contentContainer.setBackgroundResource(leftBackground());
                }
            } else {
                setGravity(bodyContainer, Gravity.RIGHT);
                if (isShowGiftBg()){
                    contentContainer.setBackgroundColor(Color.parseColor("#00000000"));
                }else {
                    contentContainer.setBackgroundResource(rightBackground());
                }
            }
        }
    }

    private void setReadReceipt() {
        if (!TextUtils.isEmpty(getMsgAdapter().getUuid()) && message.getUuid().equals(getMsgAdapter().getUuid())) {
            readReceiptTextView.setVisibility(View.VISIBLE);
        } else {
            readReceiptTextView.setVisibility(View.GONE);
        }
    }
}
