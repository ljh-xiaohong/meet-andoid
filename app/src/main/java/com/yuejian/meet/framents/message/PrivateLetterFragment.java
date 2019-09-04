package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.custom.DefaultUserInfoProvider;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.ServerCenterActivity;
import com.yuejian.meet.adapters.MessagePrivateChatAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.ImUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 私信
 */
public class PrivateLetterFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_message_private_chat_list)
    ListView listView;
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag
    private boolean msgLoaded = false;
    private MessagePrivateChatAdapter mAdapter;
    List<RecentContact> mListDatan = new ArrayList<>();
    public DefaultUserInfoProvider defaultUser;
    View viewHeader;
    private View serverUnReadBadge = null;
    private List<RecentContact> recentContacts = new ArrayList<>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_private_letter, container, false);
    }

    @Override
    protected void initData() {
        viewHeader = View.inflate(getContext(), R.layout.layout_message_private_chat_live_header_item, null);
        serverUnReadBadge = viewHeader.findViewById(R.id.server_unread_badge);
        listView.addHeaderView(viewHeader);
        defaultUser = new DefaultUserInfoProvider(getContext());
//        mAdapter = new MessagePrivateChatAdapter(listView, mListDatan, R.layout.layout_message_private_chat_live_item);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        loadRecentMessage();
        //注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);
    }

    //加载最近会话
    private void loadRecentMessage() {
        if (msgLoaded) {
            return;
        }
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> recents, Throwable e) {
                mListDatan.clear();
                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                    mAdapter.refresh(mListDatan);
                    return;
                }
                int totalMessageCount = 0;
                for (int i = 0; i < recents.size(); i++) {
//                    if (!(recents.get(i).getSessionType() == SessionTypeEnum.Team)) {
                        RecentContact contact = recents.get(i);
                        totalMessageCount += contact.getUnreadCount();
                        recentContacts.add(contact);
//                    }
                }
                BusCallEntity entity = new BusCallEntity();
                entity.setData(String.valueOf(totalMessageCount));
                entity.setCallType(BusEnum.Message_RECEIVER);
                Bus.getDefault().post(entity);
                msgLoaded = true;
                getServerList();
            }
        });

    }

    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            int totalMessageCount = 0;
            for (int i = 0; i < messages.size(); i++) {
                RecentContact contact = messages.get(i);
//                if (contact.getSessionType() == SessionTypeEnum.Team) {
//                    continue;
//                }
                int index = -1;
                for (int n = 0; n < recentContacts.size(); n++) {
                    if (recentContacts.get(n).getContactId().equals(contact.getContactId())) {
                        index = n;
                        break;
                    }
                }
                if (index >= 0) {
                    recentContacts.remove(index);
                } else {
                    List<String> infoUser = new ArrayList<>();
                    //从网易云服务下载用户信息
                    for (RecentContact info : messages) {
                        infoUser.add(info.getContactId());
                    }
                    getUserInfo(infoUser);
                    defaultUser.getUserInfo(contact.getContactId());
                }
                totalMessageCount += contact.getUnreadCount();
                recentContacts.add(contact);
            }
            BusCallEntity entity = new BusCallEntity();
            entity.setData(String.valueOf(totalMessageCount));
            entity.setCallType(BusEnum.Message_RECEIVER);
            Bus.getDefault().post(entity);
            Collections.sort(recentContacts, comp);//排序
            getServerList();
        }
    };

    ///从网易云拿用户数据
    public void getUserInfo(List<String> accounts) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {

                    @Override
                    public void onSuccess(List<NimUserInfo> param) {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.refresh(mListDatan);
                            }
                        });
                    }

                    @Override
                    public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                });
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    @BusReceiver
    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.LOGOUT) {
            mListDatan.clear();
            mAdapter.refresh(mListDatan);
        } else if (event.getCallType() == BusEnum.IM_LOGIN) {
            loadRecentMessage();
        } else if (event.getCallType() == BusEnum.OPEN_compile) {//编辑
            if (event.getId().equals("0")) {
                isCompile(event.getUsable());
            }
        } else if (event.getCallType() == BusEnum.IS_SELECTLAA) {//是否全选
            if (event.getId().equals("0")) {
                isSelectAll(event.getUsable());
            }
        } else if (event.getCallType() == BusEnum.MESSGE_DEL) {//删除
            if (!event.getId().equals("0")) {
                return;
            }
            int delTotalMessageCnt = 0;
            for (int i = 0; i < mListDatan.size(); ) {
                if (mListDatan.get(i).getTag() == 1) {
                    RecentContact contact = mListDatan.get(i);
                    deleteMessage(contact);
                    delTotalMessageCnt += contact.getUnreadCount();
                } else {
                    i++;
                }
            }
            BusCallEntity entity = new BusCallEntity();
            entity.setCallType(BusEnum.MESSAGE_RECEIVER_DELETE);
            entity.setData(String.valueOf(delTotalMessageCnt));
            Bus.getDefault().post(entity);
            mAdapter.refresh(mListDatan);
            isAllDel();
        } else if (event.getCallType() == BusEnum.IM_USERCHAT_UPDATE) {///刷新消息
            mAdapter.refresh(mListDatan);
        }
    }

    ///是否已全部删完
    public void isAllDel() {
        if (mListDatan.size() == 0) {
            BusCallEntity entity = new BusCallEntity();
            entity.setCallType(BusEnum.IS_DELETE_ALL);
            Bus.getDefault().post(entity);
        }
    }

    //是否编辑
    public void isCompile(Boolean isOpen) {
        for (int i = 0; i < mListDatan.size(); i++) {
            mListDatan.get(i).setTag(0);
        }
        mAdapter.setIsEdit(isOpen);
        mAdapter.refresh(mListDatan);
    }

    //是否全选
    public void isSelectAll(Boolean isSelect) {
        for (int i = 0; i < mListDatan.size(); i++) {
            mListDatan.get(i).setTag(isSelect ? 1 : 0);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            try {
                Intent intent = new Intent(getActivity(), ServerCenterActivity.class);
                Collections.sort(serverList, new Comparator<Server>() {
                    @Override
                    public int compare(Server o1, Server o2) {
                        if ("1".equals(o1.is_online)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                intent.putExtra("server_list", serverList);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        position -= 1;
        if (mListDatan.get(position).getSessionType() == SessionTypeEnum.P2P)
            ImUtils.toP2PCaht(getContext(), mListDatan.get(position).getContactId());
        else if (mListDatan.get(position).getSessionType() == SessionTypeEnum.Team){
            ImUtils.toTeamSession(getContext(),mListDatan.get(position).getContactId(),"0");
        }
    }

    ///删除最近联系人
    public void deleteMessage(RecentContact recent) {
        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), SessionTypeEnum.P2P);
        mListDatan.remove(recent);
    }

    @Override
    public void onResume() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        super.onResume();
    }

    private ArrayList<Server> serverList = new ArrayList<>();

    private void getServerList() {
        HashMap<String, Object> params = new HashMap<>();
        apiImp.getKfList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(final String data, int id) {
                List<Server> servers = JSON.parseArray(data, Server.class);
                if (serverList.isEmpty()) {
                    serverList.addAll(servers);
                }

                for (Server server : serverList) {
                    boolean isExist = true;
                    for (Server s : servers) {
                        if (server.customer_id.equals(s.customer_id)) {
                            isExist = true;
                            break;
                        } else {
                            isExist = false;
                        }
                    }
                    if (!isExist) {
                        serverList.remove(server);
                    }
                }

                boolean hasUnReadMsg = false;
                List<RecentContact> tmp = new ArrayList<>();
                tmp.addAll(recentContacts);
                for (Server server : serverList) {
                    if (recentContacts != null) {
                        for (RecentContact contact : tmp) {
                            if (server.getCustomer_id().equals(contact.getContactId())) {
                                server.recentContent = contact.getContent().trim();
                                server.recentTime = contact.getTime();
                                server.unreadMsgCnt = contact.getUnreadCount();
                                recentContacts.remove(contact);
                            }
                        }
                    }

                    if (server.unreadMsgCnt > 0) {
                        hasUnReadMsg = true;
                    }
                }
                serverUnReadBadge.setVisibility(hasUnReadMsg ? View.VISIBLE : View.GONE);
                mListDatan.clear();
                mListDatan.addAll(recentContacts);
                mAdapter.notifyDataSetChanged();
                tmp.clear();
                tmp = null;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                mAdapter.refresh(mListDatan);
            }
        });
    }

    @Override
    public void onSomeEvent(BusCallEntity event) {
        if (event.getCallType().equals(BusEnum.IM_LOGIN)) {
            loadRecentMessage();
        }
    }
}
