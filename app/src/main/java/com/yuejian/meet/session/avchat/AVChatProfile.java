package com.yuejian.meet.session.avchat;

import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.yuejian.meet.session.DemoCache;
import com.yuejian.meet.session.avchat.activity.AVChatActivity;
import com.yuejian.meet.session.common.infra.Handlers;

/**
 * Created by huangjun on 2015/5/12.
 */
public class AVChatProfile {

    private final String TAG = "AVChatProfile";

    private boolean isAVChatting = false; // 是否正在音视频通话

    public static AVChatProfile getInstance() {
        return InstanceHolder.instance;
    }

    public boolean isAVChatting() {
        return isAVChatting;
    }

    public void setAVChatting(boolean chating) {
        isAVChatting = chating;
    }

    private static class InstanceHolder {
        public final static AVChatProfile instance = new AVChatProfile();
    }

    public void launchActivity(final AVChatData data, final int source) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 启动，如果 task正在启动，则稍等一下
                if (!DemoCache.isMainTaskLaunching()) {
                    AVChatActivity.launch(DemoCache.getContext(), data, source);
                } else {
                    launchActivity(data, source);
                }
            }
        };
        Handlers.sharedHandler(DemoCache.getContext()).postDelayed(runnable, 200);
    }
}