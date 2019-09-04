package com.yuejian.meet.session.avchat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.widgets.GiftDialogFragment;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.yuejian.meet.R;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.session.avchat.activity.AVChatExitCode;
import com.yuejian.meet.session.avchat.constant.CallStateEnum;
import com.yuejian.meet.session.avchat.widgets.ToggleListener;
import com.yuejian.meet.session.avchat.widgets.ToggleState;
import com.yuejian.meet.session.avchat.widgets.ToggleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 视频管理器， 视频界面初始化和相关管理
 * Created by hzxuwen on 2015/5/5.
 */
public class AVChatVideo implements View.OnClickListener, ToggleListener {

    // data
    private Context context;
    private View root;
    private AVChatUI manager;
    //顶部控制按钮
    private View topRoot;
    private View switchAudio;
    private Chronometer time;
    private TextView netUnstableTV;
    //中间控制按钮
    private View middleRoot;
    private HeadImageView headImg;
    private  TextView nickNameTV;
    private  TextView notifyTV;
    private View refuse_receive;
    private TextView refuseTV;
    private TextView receiveTV;
    //底部控制按钮
    private View bottomRoot;
    ToggleView switchCameraToggle;
    ToggleView closeCameraToggle;
    ToggleView muteToggle;
    ImageView recordToggle;
    ImageView hangUpImg;
    ImageView avchat_close_camera;
    TextView call_refuse;


    private Button avchat_distinct;//切换清晰度
    private TextView avchat_price;///视频价格  女方的

    //摄像头权限提示显示
    private View permissionRoot;

    private static sendGiftEntity gift;//接口

    //record
    private View recordView;
    private View recordTip;
    private View recordWarning;

    private int topRootHeight = 0;
    private int bottomRootHeight = 0;

    private AVChatUIListener listener;

    // state
    private boolean init = false;
    private boolean shouldEnableToggle = false;
    private boolean isInSwitch = false;
    public AVChatVideo(){}
    public AVChatVideo(Context context, View root, AVChatUIListener listener, AVChatUI manager) {
        this.context = context;
        this.root = root;
        this.listener = listener;
        this.manager = manager;
    }

    private void findViews() {
        if(init || root == null )
            return;
        topRoot = root.findViewById(R.id.avchat_video_top_control);
        switchAudio = topRoot.findViewById(R.id.avchat_video_switch_audio);
        switchAudio.setOnClickListener(this);

        avchat_distinct= (Button) topRoot.findViewById(R.id.avchat_distinct);
        avchat_distinct.setOnClickListener(this);

        time = (Chronometer) topRoot.findViewById(R.id.avchat_video_time);
        netUnstableTV = (TextView) topRoot.findViewById(R.id.avchat_video_netunstable);

        middleRoot = root.findViewById(R.id.avchat_video_middle_control);
        headImg = (HeadImageView) middleRoot.findViewById(R.id.avchat_video_head);
        nickNameTV = (TextView) middleRoot.findViewById(R.id.avchat_video_nickname);
        notifyTV = (TextView) middleRoot.findViewById(R.id.avchat_video_notify);

        refuse_receive = middleRoot.findViewById(R.id.avchat_video_refuse_receive);
        refuseTV = (TextView) refuse_receive.findViewById(R.id.refuse);
        receiveTV = (TextView) refuse_receive.findViewById(R.id.receive);
        refuseTV.setOnClickListener(this);
        receiveTV.setOnClickListener(this);

        recordView = root.findViewById(R.id.avchat_record_layout);
        recordTip = recordView.findViewById(R.id.avchat_record_tip);
        recordWarning = recordView.findViewById(R.id.avchat_record_warning);

        bottomRoot = root.findViewById(R.id.avchat_video_bottom_control);
        switchCameraToggle = new ToggleView(bottomRoot.findViewById(R.id.avchat_switch_camera), ToggleState.DISABLE, this);
        closeCameraToggle = new ToggleView(bottomRoot.findViewById(R.id.avchat_close_camera), ToggleState.DISABLE, this);
        muteToggle = new ToggleView(bottomRoot.findViewById(R.id.avchat_video_mute), ToggleState.DISABLE, this);
        recordToggle = (ImageView) bottomRoot.findViewById(R.id.avchat_video_record);
        avchat_close_camera= (ImageView) bottomRoot.findViewById(R.id.avchat_close_camera);
        recordToggle.setEnabled(false);
        recordToggle.setOnClickListener(this);
        hangUpImg = (ImageView) bottomRoot.findViewById(R.id.avchat_video_logout);
        hangUpImg.setOnClickListener(this);
        call_refuse= (TextView) root.findViewById(R.id.call_refuse);
        call_refuse.setOnClickListener(this);

        permissionRoot = root.findViewById(R.id.avchat_video_permission_control);
        avchat_price = (TextView) root.findViewById(R.id.avchat_price);
        if (AppConfig.avChatPrice>0){
            avchat_price.setText(AppConfig.avChatPrice+"金币/分钟");
        }else {
            avchat_price.setVisibility(View.GONE);
            avchat_close_camera.setVisibility(View.GONE);
        }
        init = true;
    }

    /**
     * 音视频状态变化及界面刷新
     * @param state
     */
    public void onCallStateChange(CallStateEnum state) {
        if(CallStateEnum.isVideoMode(state))
            findViews();
        switch (state){
            case OUTGOING_VIDEO_CALLING:
                showProfile();//对方的详细信息
                showNotify(R.string.avchat_wait_recieve);
                setRefuseReceive(false);
                shouldEnableToggle = true;
                enableCameraToggle();   //使用音视频预览时这里可以开启切换摄像头按钮
                setTopRoot(false);
                setMiddleRoot(true);
                setBottomRoot(true);
                break;
            case INCOMING_VIDEO_CALLING:
                showProfile();//对方的详细信息
                showNotify(R.string.avchat_video_call_request);
                setRefuseReceive(true);
                receiveTV.setText(R.string.avchat_pickup);
                setTopRoot(false);
                setMiddleRoot(true);
                setBottomRoot(false);
                break;
            case VIDEO:
                isInSwitch = false;
                enableToggle();
                setTime(true);
                setTopRoot(true);
                setMiddleRoot(false);
                setBottomRoot(true);
                showNoneCameraPermissionView(false);
                break;
            case VIDEO_CONNECTING:
                showNotify(R.string.avchat_connecting);
                shouldEnableToggle = true;
                break;
            case OUTGOING_AUDIO_TO_VIDEO:
                isInSwitch = true;
                setTime(true);
                setTopRoot(true);
                setMiddleRoot(false);
                setBottomRoot(true);
                break;
            default:
                break;
        }
        setRoot(CallStateEnum.isVideoMode(state));
    }

    /********************** 界面显示 **********************************/

    /**
     * 显示个人信息
     */
    private void showProfile(){
        String account = manager.getAccount();
        headImg.loadBuddyAvatar(account);
        nickNameTV.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
    }

    /**
     * 显示通知
     * @param resId
     */
    private void showNotify(int resId){
        notifyTV.setText(resId);
        notifyTV.setVisibility(View.VISIBLE);
    }

    /************************ 布局显隐设置 ****************************/

    private void setRoot(boolean visible) {
        root.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setRefuseReceive(boolean visible){
        refuse_receive.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setTopRoot(boolean visible){
        topRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(topRootHeight == 0){
            Rect rect = new Rect();
            topRoot.getGlobalVisibleRect(rect);
            topRootHeight = rect.bottom;
        }
    }

    private void setMiddleRoot(boolean visible){
        middleRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setBottomRoot(boolean visible){
        bottomRoot.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(bottomRootHeight == 0){
            bottomRootHeight = bottomRoot.getHeight();
        }
    }

    private void setTime(boolean visible){
        time.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(visible){
            time.setBase(manager.getTimeBase());
            time.start();
        }
    }

    /**
     * 底部控制开关可用
     */
    private void enableToggle() {
        if (shouldEnableToggle) {
            if (manager.canSwitchCamera() && AVChatCameraCapturer.hasMultipleCameras())
                switchCameraToggle.enable();
            if (!AppConfig.UserSex.equals("1"))
                closeCameraToggle.enable();
            muteToggle.enable();
            recordToggle.setEnabled(true);
            shouldEnableToggle = false;
        }
    }

    private void enableCameraToggle() {
        if (shouldEnableToggle) {
            if (manager.canSwitchCamera() && AVChatCameraCapturer.hasMultipleCameras())
                switchCameraToggle.enable();
        }
    }
    AlertDialog.Builder builder=null;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avchat_video_logout:
                builder=new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("是否要退出视频");
                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.onHangUp();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.call_refuse:
                builder=new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("是否要退出视频");
                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.onHangUp();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.refuse:
                listener.onRefuse();
                break;
            case R.id.receive:
                listener.onReceive();
                Bus.getDefault().post("connect_video");
                break;
            case R.id.avchat_video_mute:
                listener.toggleMute();
                break;
            case R.id.avchat_video_switch_audio:
                if(isInSwitch) {
                    Toast.makeText(context, R.string.avchat_in_switch, Toast.LENGTH_SHORT).show();
                } else {
                    listener.videoSwitchAudio();
                }
                break;
            case R.id.avchat_switch_camera:
                listener.switchCamera();
                break;
            case R.id.avchat_close_camera:
                listener.closeCamera();
                break;
            case R.id.avchat_distinct:///清晰度切换
                if (avchat_distinct.getText().toString().equals("清晰")){
                    avchat_distinct.setText("流畅");
                    setAvchatParameters(1);
                }else{
                    avchat_distinct.setText("清晰");
                    setAvchatParameters(3);
                }
                break;
            case R.id.avchat_video_record:
//                listener.toggleRecord();
//                android.support.v4.app.FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
//                GiftDialogFragment editNameDialog = new GiftDialogFragment();
//                editNameDialog.show(fm,"");
                sendGift();
                break;
            default:
                break;
        }

    }
    //设置清晰度
    public void setAvchatParameters(int type){
        AVChatParameters params = new AVChatParameters();
        params.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, type);
        AVChatManager.getInstance().setParameters(params);
    }
    GiftDialogFragment editNameDialog;
    //礼物发送回调
    private void sendGift(){
        android.support.v4.app.FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        if (editNameDialog==null)
            editNameDialog = new GiftDialogFragment();
//        editNameDialog.show(fm,"");
        final PopupWindow mPoupWindow = editNameDialog.showBottomPopupWindow((AppCompatActivity)context,root);
        editNameDialog.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
            @Override
            public void sendGift(GiftAllEntity giftBean) {
                if (mPoupWindow!=null)
                    mPoupWindow.dismiss();
                if (gift!=null)
                    gift.setGif(giftBean);
//                sendGiftInVideo(giftBean);
            }
        });
    }
    public void initgift(sendGiftEntity gift){
        this.gift=gift;
    }
    public interface sendGiftEntity{
        public void setGif(GiftAllEntity gift);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(MyApplication.context, R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.gif_dialog_layout);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        ButterKnife.bind(this, dialog); // Dialog即View


        return dialog;
    }

    public void showRecordView(boolean show, boolean warning) {
        if(show) {
            recordToggle.setEnabled(true);
            recordToggle.setSelected(true);
            recordView.setVisibility(View.VISIBLE);
            recordTip.setVisibility(View.VISIBLE);
            if(warning) {
                recordWarning.setVisibility(View.VISIBLE);
            } else {
                recordWarning.setVisibility(View.GONE);
            }
        } else {
            recordToggle.setSelected(false);
            recordView.setVisibility(View.INVISIBLE);
            recordTip.setVisibility(View.INVISIBLE);
            recordWarning.setVisibility(View.GONE);
        }
    }

    public void showNoneCameraPermissionView(boolean show) {
        permissionRoot.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 音频切换为视频, 界面控件是否开启显示
     * @param muteOn
     */
    public void onAudioToVideo(boolean muteOn, boolean recordOn, boolean recordWarning){
        muteToggle.toggle(muteOn ? ToggleState.ON : ToggleState.OFF);
        closeCameraToggle.toggle(ToggleState.OFF);
        if(manager.canSwitchCamera()){
            switchCameraToggle.off(false);
        }
        recordToggle.setEnabled(true);
        recordToggle.setSelected(recordOn);
        showRecordView(recordOn, recordWarning);

    }

    /******************************* toggle listener *************************/
    @Override
    public void toggleOn(View v) {
        onClick(v);
    }

    @Override
    public void toggleOff(View v) {
        onClick(v);
    }

    @Override
    public void toggleDisable(View v) {

    }

    public void closeSession(int exitCode){
        if(init){
            time.stop();
            switchCameraToggle.disable(false);
            muteToggle.disable(false);
            recordToggle.setEnabled(false);
            closeCameraToggle.disable(false);
            receiveTV.setEnabled(false);
            refuseTV.setEnabled(false);
            hangUpImg.setEnabled(false);
            this.gift=null;
            if (editNameDialog!=null){
//                editNameDialog.dismiss();
                editNameDialog=null;
            }
        }
    }
}
