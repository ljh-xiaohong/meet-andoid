package com.yuejian.meet.session.util;

import android.os.Handler;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/8/25.
 */

public class ImUserInfoUtil {
    ///加载最近会话
    public static void loadRecentMessage() {
            NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                @Override
                public void onResult(int code, List<RecentContact> recents, Throwable e) {
                    if (code != ResponseCode.RES_SUCCESS || recents == null) {
                        return;
                    }
                    List<String> accounts = new ArrayList<String>();
                    ///从网易服务下载用户信息
                    for (RecentContact info : recents) {
                        accounts.add(info.getContactId());
                    }
                    int cnt=(accounts.size()+49)/50;
                    for (int i=1;i<=cnt;i++){
                        if (cnt==i)
                            getUserInfo(accounts.subList(50*(i-1),accounts.size()));
                        else {
                            getUserInfo(accounts.subList(50*(i-1),50*i));
                        }
                    }
                }
            });
    }
    ///从网易云拿用户数据
    public static void getUserInfo(List<String> accounts) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {

                    @Override
                    public void onSuccess(List<NimUserInfo> param) {
                        BusCallEntity busCallEntity=new BusCallEntity();
                        busCallEntity.setCallType(BusEnum.IM_USERCHAT_UPDATE);
                        Bus.getDefault().post(busCallEntity);
                    }

                    @Override
                    public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                });
    }
}
