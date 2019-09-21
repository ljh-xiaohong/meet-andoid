package com.yuejian.meet.framents.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.app.AppConfig;
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
import com.yuejian.meet.activities.FamilyTree.TreeAddActivity;
import com.yuejian.meet.activities.find.ActionActivity;
import com.yuejian.meet.activities.find.NotificationCenterActivity;
import com.yuejian.meet.activities.message.GroupChatActivity;
import com.yuejian.meet.activities.message.ServerCenterActivity;
import com.yuejian.meet.activities.message.VideoBookActivity;
import com.yuejian.meet.activities.mine.ContactActivity;
import com.yuejian.meet.adapters.MessagePrivateChatAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.NotificationMsgBean;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.framents.mine.MineFragmentBC;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 信息
 */

/**
 * @author :
 * @time : 2018/11/29 16:29
 * @desc : 首页 消息
 * @version: V1.0
 * @update : 2018/11/29 16:29
 */


public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.red_rod_inform)
    TextView red_rod_inform;
    @Bind(R.id.lv_message_private_chat_list)
    ListView listView;
    TextView txt_message_private_chat_content;
    TextView message_dot, txt_message_content;
    TextView server_message_count;
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag
    private boolean msgLoaded = false;
    private MessagePrivateChatAdapter mAdapter;
    List<RecentContact> mListDatan = new ArrayList<>();
    public DefaultUserInfoProvider defaultUser;
    View viewHeader, viewHeader2, viewMessage;
    private TextView serverUnReadBadge = null;
    private List<RecentContact> recentContacts = new ArrayList<>();
    private NotificationMsgBean msgBean = new NotificationMsgBean();
    MineFragmentBC mineFragmentBC;
    public LoadingDialogFragment dialog;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    protected void initData() {
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        viewMessage = View.inflate(getContext(), R.layout.item_system_message_layout, null);
        message_dot = (TextView) viewMessage.findViewById(R.id.message_dot);
        txt_message_content = (TextView) viewMessage.findViewById(R.id.txt_message_content);
//        mineFragmentBC=new MineFragmentBC(txt_message_content,message_dot,msgBean);
        viewHeader = View.inflate(getContext(), R.layout.layout_message_private_chat_live_header_item, null);
        viewHeader2 = View.inflate(getContext(), R.layout.layout_message_new_action_header_item, null);
        serverUnReadBadge = (TextView) viewHeader.findViewById(R.id.server_unread_badge);
        server_message_count = (TextView) viewHeader2.findViewById(R.id.server_message_count);
        txt_message_private_chat_content = (TextView) viewHeader.findViewById(R.id.txt_message_private_chat_content);
        listView.addHeaderView(viewMessage);
        listView.addHeaderView(viewHeader);
        listView.addHeaderView(viewHeader2);

        defaultUser = new DefaultUserInfoProvider(getContext());
        mAdapter = new MessagePrivateChatAdapter(listView, mListDatan, R.layout.layout_message_private_chat_live_item, this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        loadRecentMessage();
        //注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        getrequstCount();
//        mineFragmentBC.getUnReadMsgCount();
        actionReadNum();
    }

    public void actionReadNum() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.getActionReadNum(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                message_dot.setVisibility(data.equals("0") ? View.GONE : View.VISIBLE);
                txt_message_content.setText(data.equals("0") ? "暂无消息" : "动态更新");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
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


    @OnClick({R.id.ll_book, R.id.ll_video_book, R.id.ll_group, R.id.ll_inform})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_book://通讯录
                startActivity(new Intent(getContext(), ContactActivity.class));
                break;
            case R.id.ll_video_book://视频记录
                startActivity(new Intent(getContext(), VideoBookActivity.class));
                break;
            case R.id.ll_group:
                if (PreferencesMessage.get(mContext, PreferencesMessage.family_id + AppConfig.CustomerId, "0").equals("0")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.Fill_in_the_location);
                    builder.setPositiveButton(R.string.To_fill_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            BusCallEntity busCallEntity = new BusCallEntity();
                            busCallEntity.setCallType(BusEnum.Bangding_Family);
                            Bus.getDefault().post(busCallEntity);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.show();
                    return;
                }
                startActivity(new Intent(getContext(), GroupChatActivity.class));
                break;
            case R.id.ll_inform:
                Intent intent = new Intent(getContext(), NotificationCenterActivity.class);
//                intent.putExtra("msgBean", this.msgBean);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Intent intent = new Intent(getContext(), ActionActivity.class);
//            Intent intent = new Intent(getContext(), NotificationCenterActivity.class);
            startActivity(intent);
            return;
        } else if (position == 1) {
            startActivity(new Intent(getContext(), TreeAddActivity.class));
//            startActivity(new Intent(getContext(), TreeTagActivity.class));
//            try {
//                Intent intent = new Intent(getActivity(), ServerCenterActivity.class);
//                Collections.sort(serverList, new Comparator<Server>() {
//                    @Override
//                    public int compare(Server o1, Server o2) {
//                        if ("1".equals(o1.is_online)) {
//                            return -1;
//                        } else {
//                            return 0;
//                        }
//                    }
//                });
//                intent.putExtra("server_list", serverList);
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return;
        } else if (position == 2) {
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
//            startActivity(new Intent(getContext(), NewActionActivity.class));
            return;
        }
        position -= 3;
        if (mListDatan.get(position).getSessionType() == SessionTypeEnum.P2P)
            ImUtils.toP2PCaht(getContext(), mListDatan.get(position).getContactId());
        else if (mListDatan.get(position).getSessionType() == SessionTypeEnum.Team) {
//            groupType(mListDatan.get(position).getContactId());
            ImUtils.toTeamSession(getContext(), mListDatan.get(position).getContactId(), "0");
        }
    }

    ///删除最近联系人
    public void deleteMessage(RecentContact recent) {
        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), SessionTypeEnum.P2P);
        mListDatan.remove(recent);
        this.mAdapter.refresh(this.mListDatan);
    }

    public void settinTop(int paramInt, RecentContact paramRecentContact) {
        ((MsgService) NIMClient.getService(MsgService.class)).updateRecent(paramRecentContact);
        Collections.sort(this.mListDatan, comp);
        this.mAdapter.refresh(this.mListDatan);
    }

    @Override
    public void onResume() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        getrequstCount();
        super.onResume();
    }

    int unreadCount = 0;
    private ArrayList<Server> serverList = new ArrayList<>();

    private void getServerList() {
        unreadCount = 0;
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
                        unreadCount += server.unreadMsgCnt;
                        hasUnReadMsg = true;
                    }
                }
                server_message_count.setVisibility(hasUnReadMsg ? View.VISIBLE : View.GONE);
                server_message_count.setText(unreadCount + "");

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

    public void getrequstCount() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.getRequstCount(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (StringUtils.isEmpty(PreferencesUtil.get(getContext(), user.getCustomer_id() + PreferencesUtil.Company_name, "")) && StringUtils.isEmpty(user.getCompany_name())) {
                    int num = Integer.parseInt(data) + 1;
                    data = num + "";
                }
                txt_message_private_chat_content.setText("您有" + data + getString(R.string.applet_name14));
                if (Integer.parseInt(data) > 0) {
                    serverUnReadBadge.setVisibility(View.VISIBLE);
                    serverUnReadBadge.setText(data);
                } else {
                    serverUnReadBadge.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
