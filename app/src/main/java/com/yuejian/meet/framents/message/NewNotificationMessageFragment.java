package com.yuejian.meet.framents.message;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.custom.DefaultUserInfoProvider;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.CommentZanActivity;
import com.yuejian.meet.activities.message.ContactActivity;
import com.yuejian.meet.activities.message.NotificationActivity;
import com.yuejian.meet.activities.message.ServerCenterActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.CustomerServiceAdapter;
import com.yuejian.meet.adapters.MessagePrivateChatAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.AllPeopleBean;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 系统消息
 *
 * @author : ljh
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class NewNotificationMessageFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.new_firent)
    TextView newFirent;
    @Bind(R.id.comment_and_zan)
    TextView commentAndZan;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    @Bind(R.id.notifi)
    TextView notifi;
    @Bind(R.id.lv_message_private_chat_list)
    ListView listView;
    private CustomerServiceAdapter adapter;
    public ApiImp apiImp = new ApiImp();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.new_notification_message_fragment, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
    }
    View viewHeader, viewMessage;
    TextView txt_message_private_chat_content;
    List<RecentContact> mListDatan = new ArrayList<>();
    List<AllPeopleBean.DataBean> mData = new ArrayList<>();
    public DefaultUserInfoProvider defaultUser;
    private MessagePrivateChatAdapter mAdapter;
    private TextView serverUnReadBadge = null;
    @Override
    protected void initData() {
        newFirent.setOnClickListener(v -> startActivity(new Intent(getActivity(), ContactActivity.class)));
        notifi.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationActivity.class)));
        commentAndZan.setOnClickListener(v -> startActivity(new Intent(getActivity(), CommentZanActivity.class)));
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        viewMessage = View.inflate(getContext(), R.layout.item_system_message_layout, null);
        viewMessage.setVisibility(View.GONE);
        viewHeader = View.inflate(getContext(), R.layout.layout_message_private_chat_live_header_item, null);
        viewHeader.setVisibility(View.GONE);
        serverUnReadBadge = (TextView) viewHeader.findViewById(R.id.server_unread_badge);
        txt_message_private_chat_content = (TextView) viewHeader.findViewById(R.id.txt_message_private_chat_content);
//        listView.addHeaderView(viewMessage);
//        listView.addHeaderView(viewHeader);

        defaultUser = new DefaultUserInfoProvider(getContext());
        mAdapter = new MessagePrivateChatAdapter(listView, mListDatan, R.layout.layout_message_private_chat_live_item, this,mData);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        //注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);
        try{
            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                    new Observer<StatusCode>() {
                        public void onEvent(StatusCode status) {
                            if (status.wontAutoLogin()) {
                                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
//                            Toast.makeText(LoginActivity.this,
//                                    "Customer——————"+AppConfig.userEntity.getCustomer_id()+"Token——————"+ AppConfig.userEntity.getToken(),Toast.LENGTH_LONG).show();
                                Dialog dialog = DialogUtils.createTwoBtnDialog(getActivity(), "提示", "IM登录异常，如需使用IM功能，请重新登录！","取消","确定", false);
                                dialog.show();
                                DialogUtils.setOnTitleViewClickListener(new DialogUtils.OnTitleViewClickListener() {
                                    @Override
                                    public void onTitleViewClick() {
                                        ImUtils.isLoginIm=false;
                                        DadanPreference.getInstance(getActivity()).setBoolean("isLogin",false);
                                        DadanPreference.getInstance(getActivity()).setString("CustomerId","");
                                        DadanPreference.getInstance(getActivity()).setString("photo","");
                                        DadanPreference.getInstance(getActivity()).setString("surname","");
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        AppManager.finishAllActivity();
                                    }
                                });
                            }
                        }
                    }, true);
            loadRecentMessage();
        }catch (Exception e){

        }
    }
    @Override
    public void onUserVisible() {
        super.onUserVisible();
        getrequstCount();
//        mineFragmentBC.getUnReadMsgCount();
        actionReadNum();
        loadRecentMessage();
    }

    public void actionReadNum() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.getCustomer_id());
        apiImp.getActionReadNum(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    private boolean msgLoaded = false;
    private List<RecentContact> recentContacts = new ArrayList<>();
    private String allID="";
    //加载最近会话
    private void loadRecentMessage() {
        if (msgLoaded) {
            return;
        }
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> recents, Throwable e) {
                mListDatan.clear();
                totalMessageCount=0;
                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                    mAdapter.refresh(mListDatan);
                    if (AppConfig.userEntity == null) {
                        return;
                    }
                    LoginInfo info = new LoginInfo(AppConfig.CustomerId, AppConfig.userEntity.getToken());
                    NIMClient.getService(AuthService.class).login(info).setCallback(new RequestCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            for (int i = 0; i < recents.size(); i++) {
//                    if (!(recents.get(i).getSessionType() == SessionTypeEnum.Team)) {
                                RecentContact contact = recents.get(i);
                                totalMessageCount += contact.getUnreadCount();
                                recentContacts.add(contact);
                                allID=allID+contact.getContactId()+",";
//                    }
                            }
                            BusCallEntity entity = new BusCallEntity();
                            entity.setData(String.valueOf(totalMessageCount));
                            entity.setCallType(BusEnum.Message_RECEIVER);
                            Bus.getDefault().post(entity);
                            msgLoaded = true;
                            getServerList(allID);
                        }

                        @Override
                        public void onFailed(int i) {
                            if (!ImUtils.isLoginIm){
                                Dialog dialog = DialogUtils.createTwoBtnDialog(getActivity(), "提示", "IM登录异常，如需使用IM功能，请重新登录！","取消","确定", false);
                                dialog.show();
                                DialogUtils.setOnTitleViewClickListener(new DialogUtils.OnTitleViewClickListener() {
                                    @Override
                                    public void onTitleViewClick() {
                                        ImUtils.isLoginIm=false;
                                        DadanPreference.getInstance(getActivity()).setBoolean("isLogin",false);
                                        DadanPreference.getInstance(getActivity()).setString("CustomerId","");
                                        DadanPreference.getInstance(getActivity()).setString("photo","");
                                        DadanPreference.getInstance(getActivity()).setString("surname","");
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        AppManager.finishAllActivity();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onException(Throwable throwable) {

                        }
                    });
                }else {
                    for (int i = 0; i < recents.size(); i++) {
//                    if (!(recents.get(i).getSessionType() == SessionTypeEnum.Team)) {
                        RecentContact contact = recents.get(i);
                        totalMessageCount += contact.getUnreadCount();
                        recentContacts.add(contact);
                        allID=allID+contact.getContactId()+",";
//                    }
                    }
                    BusCallEntity entity = new BusCallEntity();
                    entity.setData(String.valueOf(totalMessageCount));
                    entity.setCallType(BusEnum.Message_RECEIVER);
                    Bus.getDefault().post(entity);
                    msgLoaded = true;
                    getServerList(allID);
                }

            }
        });

    }
    int totalMessageCount = 0;
    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            totalMessageCount=0;
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
                allID=contact.getContactId()+","+allID;
            }

            BusCallEntity entity = new BusCallEntity();
            entity.setData(String.valueOf(totalMessageCount));
            entity.setCallType(BusEnum.Message_RECEIVER);
            Bus.getDefault().post(entity);
            Collections.sort(recentContacts, comp);//排序
            getServerList(allID);
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
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag
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
    ///删除最近联系人
    public void deleteMessage(RecentContact recent) {
        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), SessionTypeEnum.P2P);
        mListDatan.remove(recent);
        this.mAdapter.refresh(this.mListDatan);
        BusCallEntity entity = new BusCallEntity();
        entity.setData(String.valueOf(totalMessageCount-recent.getUnreadCount()));
        entity.setCallType(BusEnum.Message_RECEIVER);
        Bus.getDefault().post(entity);
    }
//    ///更新未读信息
//    public void updateMessage(RecentContact recent) {
//        BusCallEntity entity = new BusCallEntity();
//        entity.setData(String.valueOf(totalMessageCount-recent.getUnreadCount()));
//        entity.setCallType(BusEnum.Message_RECEIVER);
//        Bus.getDefault().post(entity);
//    }

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

    private void getServerList(String allID) {
        unreadCount = 0;
        HashMap<String, Object> params = new HashMap<>();
        apiImp.getKfList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(final String data, int id) {
//                List<Server> servers = JSON.parseArray(data, Server.class);
//                if (serverList.isEmpty()) {
//                    serverList.addAll(servers);
//                }
//
//                for (Server server : serverList) {
//                    boolean isExist = true;
//                    for (Server s : servers) {
//                        if (server.customer_id.equals(s.customer_id)) {
//                            isExist = true;
//                            break;
//                        } else {
//                            isExist = false;
//                        }
//                    }
//                    if (!isExist) {
//                        serverList.remove(server);
//                    }
//                }
//
//                boolean hasUnReadMsg = false;
//                List<RecentContact> tmp = new ArrayList<>();
//                tmp.addAll(recentContacts);
//                for (Server server : serverList) {
//                    if (recentContacts != null) {
//                        for (RecentContact contact : tmp) {
//                            if (server.getCustomer_id().equals(contact.getContactId())) {
//                                server.recentContent = contact.getContent().trim();
//                                server.recentTime = contact.getTime();
//                                server.unreadMsgCnt = contact.getUnreadCount();
//                                recentContacts.remove(contact);
//                            }
//                        }
//                    }
//
//                    if (server.unreadMsgCnt > 0) {
//                        unreadCount += server.unreadMsgCnt;
//                        hasUnReadMsg = true;
//                    }
//                }
                if (!CommonUtil.isNull(allID)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("batchCustomerIds",allID.substring(0,allID.length()-1));
                    apiImp.getBatchUsers(params, this, new DataIdCallback<String>() {
                        @Override
                        public void onSuccess(String data, int id) {
                            mData.clear();
                            AllPeopleBean allPeopleBean= new Gson().fromJson(data, AllPeopleBean.class);
                            mData.addAll(allPeopleBean.getData());
                            mListDatan.clear();
                            mListDatan.addAll(recentContacts);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String errCode, String errMsg, int id) {

                        }
                    });
                }else {
                    mListDatan.clear();
                    mListDatan.addAll(recentContacts);
                    mAdapter.notifyDataSetChanged();
                }
//                tmp.clear();
//                tmp = null;
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
    private void delect(int id) {
        Map<String, Object> params = new HashMap<>();
//        params.put("customerId", AppConfig.CustomerId);
        params.put("id", id);
        apiImp.getDelMessage(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MessageCommentBean bean = new Gson().fromJson(data, MessageCommentBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }






    public void update() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Intent intent = new Intent(getContext(), ContactActivity.class);
//            Intent intent = new Intent(getContext(), NotificationCenterActivity.class);
            startActivity(intent);
            return;
        } else if (position == 1) {
            startActivity(new Intent(getContext(), ContactActivity.class));
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
}
