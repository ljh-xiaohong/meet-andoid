package com.yuejian.meet.utils;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 监听群聊信息
 * Created by zh03 on 2017/9/3.
 */

public class ImMesssageRedDot {
    public static List<RecentContact> mListDatan = new ArrayList<>();
    public static List<RecentContact> mChatList = new ArrayList<>();
    public static RedDotInterFace redDotInterFace;
    public static groupRedDotCount groupRedDotCount;

    public static void CallMessageRigister() {
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> recents, Throwable e) {
                mListDatan.clear();
                mChatList.clear();
                if (recents == null) return;
                for (int i = 0; i < recents.size(); i++) {
                    if ((recents.get(i).getSessionType() == SessionTypeEnum.Team))
                        mListDatan.add(recents.get(i));
                    else {
                        mChatList.add(recents.get(i));
                    }
                }
                redDot();
            }
        });
        MessageCallMonito();
    }

    public static void MessageCallMonito() {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> messages) {
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getSessionType() == SessionTypeEnum.Team) {
                        Boolean isGroupChat = false;
                        for (int n = 0; n < mListDatan.size(); n++) {
                            if (mListDatan.get(n).getContactId().equals(messages.get(i).getContactId())) {
                                mListDatan.remove(n);
                                break;
                            }
                        }
                        if (!isGroupChat) {
                            mListDatan.add(messages.get(i));
                            BusCallEntity busCallEntity = new BusCallEntity();
                            busCallEntity.setCallType(BusEnum.GROUP_UNREAD_COUNT);
                            Bus.getDefault().post(busCallEntity);
                        }
                    } else {
                        Boolean isAddChat = false;
                        for (int y = 0; y < mChatList.size(); y++) {
                            if (mChatList.get(y).getContactId().equals(messages.get(i).getContactId())) {
                                mChatList.set(y, messages.get(i));
                                isAddChat = true;
                                break;
                            }
                        }
                        if (!isAddChat) {
                            mChatList.add(messages.get(i));
                        }
                    }
                }
                redDot();
            }
        }, true);
    }

    //获取群聊未读消息数
    public static int getUnreadCount(String accounts) {
        int cont = 0;
        for (int i = 0; i < mListDatan.size(); i++) {
            if (mListDatan.get(i).getContactId().equals(accounts)) {
                cont = mListDatan.get(i).getUnreadCount();
                break;
            }
        }
        return cont;
    }

    public static int getGroupUnreadCount() {
        int cont = 0;
        for (int i = 0; i < mListDatan.size(); i++) {
            cont += mListDatan.get(i).getUnreadCount();
        }
        return cont;
    }

    public static void redDot() {
        int count = 0;
        for (int i = 0; i < mChatList.size(); i++) {
            count += mChatList.get(i).getUnreadCount();
        }
        if (redDotInterFace != null) {
            redDotInterFace.redDotCount(count + "");
        }

        if (mListDatan.size() > 0) {
            if(groupRedDotCount != null) {
                groupRedDotCount.callGroupRedDot();
            }
            BusCallEntity busCallEntity = new BusCallEntity();
            busCallEntity.setCallType(BusEnum.GROUP_UNREAD_COUNT);
            Bus.getDefault().post(busCallEntity);
        }
    }

    public static void register(RedDotInterFace rdf) {
        redDotInterFace = rdf;
    }

    public interface RedDotInterFace {
        public void redDotCount(String count);
    }

    public static void registerGroup(groupRedDotCount rdf) {
        groupRedDotCount = rdf;
    }

    public interface groupRedDotCount {
        public void callGroupRedDot();
    }
}
