package com.yuejian.meet.activities.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.session.config.preference.UserPreferences;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.ViewInject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新消息提醒
 * Created by zh02 on 2017/8/8.
 */

public class MessageSettingActivity extends BaseActivity {
    @Bind(R.id.new_message_notify_switch)
    CheckBox notifySwitch;
    @Bind(R.id.new_message_content_switch)
    CheckBox contentSwitch;
    @Bind(R.id.new_message_voice_switch)
    CheckBox voiceSwitch;
    @Bind(R.id.new_message_vibration_switch)
    CheckBox vibrationSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        setTitleText(getString(R.string.New_message_alert));
        notifySwitch.setChecked(UserPreferences.getNotificationToggle());
        contentSwitch.setChecked(UserPreferences.getNoticeContentToggle());
        voiceSwitch.setChecked(UserPreferences.getRingToggle());
        vibrationSwitch.setChecked(PreferencesUtil.readBoolean(getApplicationContext(), PreferencesUtil.KEY_VIBRATE_SWITCH, true));

    }


    @OnClick({R.id.new_message_notify_switch,R.id.new_message_voice_switch})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_message_notify_switch://新消息提醒通知
                setMessageNotify(notifySwitch.isChecked());
                break;
            case R.id.new_message_content_switch://通知显示
//                UserPreferences.setNotificationToggle(contentSwitch.isChecked());
                UserPreferences.setNoticeContentToggle(contentSwitch.isChecked());
                StatusBarNotificationConfig config2 = UserPreferences.getStatusConfig();
                config2.titleOnlyShowAppName = contentSwitch.isChecked();
                UserPreferences.setStatusConfig(config2);
                NIMClient.updateStatusBarNotificationConfig(config2);
                break;
            case R.id.new_message_voice_switch://声音
                UserPreferences.setRingToggle(voiceSwitch.isChecked());
                StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
                config.ring = voiceSwitch.isChecked();
                UserPreferences.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
//                setMessageNotify(notifySwitch.isChecked());
                break;
            case R.id.new_message_vibration_switch://振动
//                setMessageNotify(notifySwitch.isChecked());
                break;
        }
    }
    private void setMessageNotify(final boolean checkState) {
        // 如果接入第三方推送（小米），则同样应该设置开、关推送提醒
        // 如果关闭消息提醒，则第三方推送消息提醒也应该关闭。
        // 如果打开消息提醒，则同时打开第三方推送消息提醒。
        NIMClient.getService(MixPushService.class).enable(checkState).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
//                Toast.makeText(SettingsActivity.this, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
                setToggleNotification(checkState);
            }
            @Override
            public void onFailed(int code) {
                // 这种情况是客户端不支持第三方推送
                if (code == ResponseCode.RES_UNSUPPORT) {
                    notifySwitch.setChecked(checkState);
                    setToggleNotification(checkState);
                } else if (code == ResponseCode.RES_EFREQUENTLY){
                    notifySwitch.setChecked(!checkState);
                    ViewInject.shortToast(mContext,"操作太上频繁");
                } else {
                    notifySwitch.setChecked(!checkState);
                    ViewInject.shortToast(mContext,"设置失败,请重试");
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
    private void setToggleNotification(boolean checkState) {
        try {
            setNotificationToggle(checkState);
            NIMClient.toggleNotification(checkState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setNotificationToggle(boolean on) {
        UserPreferences.setNotificationToggle(on);
    }
    @Override
    protected void onDestroy() {
//        PreferencesUtil.write(getApplicationContext(), Constants.NEW_MESSAGE_NOTIFY_SWITCH, notifySwitch.isChecked());
//        PreferencesUtil.write(getApplicationContext(), Constants.NEW_MESSAGE_CONTENT_SWITCH, contentSwitch.isChecked());
//        PreferencesUtil.write(getApplicationContext(), PreferencesUtil.KEY_MUSIC_SWITCH, voiceSwitch.isChecked());
        PreferencesUtil.write(getApplicationContext(), PreferencesUtil.KEY_VIBRATE_SWITCH, vibrationSwitch.isChecked());
        super.onDestroy();
    }
}
