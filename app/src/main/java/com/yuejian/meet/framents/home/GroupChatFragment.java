package com.yuejian.meet.framents.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netease.nim.uikit.UIKitLogTag;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.team.TeamServiceObserver;
import com.netease.nimlib.sdk.team.model.Team;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.activities.home.GroupFootprintActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.GroupChatListAdapter;
import com.yuejian.meet.adapters.GroupListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ChatGroupEntity;
import com.yuejian.meet.bean.GroupAllEntity;
import com.yuejian.meet.bean.GroupChatEntity;
import com.yuejian.meet.bean.GroupEntity;
import com.yuejian.meet.bean.GroupSeedEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.ImMesssageRedDot;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerListView;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群聊
 */
public class GroupChatFragment extends BaseFragment implements SpringView.OnFreshListener,ImMesssageRedDot.groupRedDotCount  {

    @Bind(R.id.group_spring)
    SpringView springView;
    @Bind(R.id.group_list)
    ListView listView;
    @Bind(R.id.group_chat_login_layout)
    View group_chat_login_layout;
    View viewHeader;
    GroupChatListAdapter mAdapter;
    List<GroupChatEntity> groupChatEntityList=new ArrayList<>();
    List<GroupEntity> listData = new ArrayList<>();
    List<GroupEntity> listDataHeab = new ArrayList<>();
    GroupEntity myGroupEntity = new GroupEntity();
    GroupEntity familyGroup = new GroupEntity();
    GroupAllEntity groupAllEntity = new GroupAllEntity();
    ChatGroupEntity chatGroupEntity;
    ApiImp api = new ApiImp();
    Intent intent;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    protected void onFirstUserVisible() {
        requstData();
        super.onFirstUserVisible();
    }

    @Override
    protected void initData() {
        ImMesssageRedDot.registerGroup(this);
        viewHeader=View.inflate(getContext(),R.layout.group_chat_header_layout,null);
        ((RelativeLayout) viewHeader.findViewById(R.id.group_feet)).setOnClickListener(this);
        ((RelativeLayout) viewHeader.findViewById(R.id.group_fonud)).setOnClickListener(this);
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setListener(this);
        mAdapter = new GroupChatListAdapter(listView, groupChatEntityList, R.layout.item_group_chat_list_parent_layout);
        listView.setAdapter(mAdapter);
        listView.addHeaderView(viewHeader);
//        mAdapter.notifyDataSetChanged();
        if(group_chat_login_layout != null) {
            if (user == null) {
                group_chat_login_layout.setVisibility(View.VISIBLE);
            } else {
                group_chat_login_layout.setVisibility(View.GONE);
            }
        }

        NIMClient.getService(TeamServiceObserver.class).observeTeamUpdate(teamUpdateObserver, true);
    }
    public Boolean isUpdateGroupName=false;
    private Long tiem=0l;
    // 群资料变动观察者通知。新建群和群更新的通知都通过该接口传递
    private Observer<List<Team>> teamUpdateObserver = new Observer<List<Team>>() {
        @Override
        public void onEvent(final List<Team> teams) {
            if (teams==null && teams.size()==0){
                return;
            }
            isUpdateGroupName=false;
            if (groupChat.size()>0 && teams.size()==1){
                for (int i=0;i<groupChat.size();i++){
                    if (teams.get(0).getId()==groupChat.get(i).getT_id() && teams.get(0).isMyTeam()){
                        groupChat.get(i).setGroup_name(teams.get(0).getName());
                        isUpdateGroupName=true;
                        break;
                    }
                }
            }
            if (groupChatTop.size()>0 && teams.size()==1){
                for (int n=0;n<groupChatTop.size();n++){
                    if (teams.get(0).getId()==groupChatTop.get(n).getT_id() && teams.get(0).isMyTeam()){
                        groupChat.get(n).setGroup_name(teams.get(0).getName());
                        isUpdateGroupName=true;
                        break;
                    }
                }
            }
            if ((System.currentTimeMillis()-tiem)<2000){
                return;
            }
            tiem=System.currentTimeMillis();
            if (teams.size()>1){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadChatGroup();
                    }
                },2000);
            }else {
                if (!isUpdateGroupName){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadChatGroup();
                        }
                    },2000);
                }else {
                    callGroupRedDot();
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    public void requstData() {
        if (user == null) return;
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.getCustomer_id());
        api.getRoupAllName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData.clear();
                groupChatEntityList.clear();
                groupAllEntity = JSON.parseObject(data, GroupAllEntity.class);
                listData = JSON.parseArray(groupAllEntity.getCity_list(), GroupEntity.class);
                myGroupEntity = JSON.parseObject(groupAllEntity.getMy_group(), GroupEntity.class);
                myGroupEntity.setOpen(true);
                familyGroup = JSON.parseObject(groupAllEntity.getFamily_group(), GroupEntity.class);
                familyGroup.setOpen(true);
//                GroupEntity footprint = new GroupEntity();
//                footprint.setOpen(false);
//                footprint.setChat_group_list("[]");
//                footprint.setList("[]");
//                footprint.setFamily_name("群聊足迹");
//                footprint.setProvince("群聊足迹");
//                listData.add(0, footprint);
                if (familyGroup.getList().length() > 4)
                    listData.add(0, familyGroup);
                listData.add(0, myGroupEntity);
//                mAdapter.refresh(listData);
                GroupChatEntity groupChatEntity=new GroupChatEntity();
                groupChatEntity.setGroupName("家族群");
                groupChatEntity.setGroupList(listData);
                groupChatEntity.setOpen(true);
                if(groupChatEntityList.size()>0){
                    groupChatEntityList.add(1,groupChatEntity);
                }else {
                    groupChatEntityList.add(groupChatEntity);
                }

                mAdapter.refresh(groupChatEntityList);
                loadChatGroup();
                if (springView != null)
                    springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null)
                    springView.onFinishFreshAndLoad();
            }
        });
    }
    Long mTime=0l;
    public void loadChatGroup(){
//        if ((System.currentTimeMillis()-mTime)<2000){
//            return;
//        }
//        Log.d("com.xiaomi.mipushmichat","自建群");
        mTime=System.currentTimeMillis();
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        api.getChatGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (data.length()>40){
                    chatGroupEntity=JSON.parseObject(data,ChatGroupEntity.class);
//                    GroupEntity groupEntity=new GroupEntity();
                    groupChatTop=JSON.parseArray(chatGroupEntity.getTop_list(),GroupSeedEntity.class);
                    groupChat=JSON.parseArray(chatGroupEntity.getNot_top_list(),GroupSeedEntity.class);
//                    List<GroupSeedEntity> listSeed=new ArrayList<GroupSeedEntity>();
//                    listSeed.addAll(groupChatTop);
//                    listSeed.addAll(groupChat);
//                    groupEntity.setProvince("好友群聊("+listSeed.size()+")");
//                    JSONArray ja = JSONArray.parseArray(JSON.toJSONString(listSeed));
//                    groupEntity.setCity_family_list(ja.toJSONString());
//                    groupEntity.setOpen(true);
//                    listData.add(0,groupEntity);
//                    mAdapter.refresh(listData);
                }else {
                    groupChatTop.clear();
                    groupChat.clear();
                }
                callGroupRedDot();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @OnClick({R.id.search_layout, R.id.et_group_search, R.id.chat_group_login_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_layout:
                startActivity(new Intent(getContext(), GroupChatSearchActivity.class));
                break;
            case R.id.et_group_search:
                startActivity(new Intent(getContext(), GroupChatSearchActivity.class));
                break;
            case R.id.chat_group_login_btn:
                ImUtils.isLoginIm=false;
                startActivity(new Intent(getContext(), LoginActivity.class));
                AppManager.finishAllActivity();
                break;
            case R.id.group_feet:
                startActivity(new Intent(getContext(), GroupFootprintActivity.class));
                break;
            case R.id.group_fonud:
                intent=new Intent(getActivity(), SelectContactActivity.class);
                startActivityForResult(intent,66);
                break;
        }
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        if (event.getCallType() == BusEnum.LOGIN_UPDATE) {
            if(group_chat_login_layout != null) {
                if (user == null) {
                    group_chat_login_layout.setVisibility(View.VISIBLE);
                } else {
                    group_chat_login_layout.setVisibility(View.GONE);
                }
            }
            requstData();
        } else if (event.getCallType() == BusEnum.LOGOUT) {
            if(group_chat_login_layout != null) {
                if (user == null) {
                    group_chat_login_layout.setVisibility(View.VISIBLE);
                } else {
                    group_chat_login_layout.setVisibility(View.GONE);
                }
            }
            listData.clear();
            mAdapter.refresh(groupChatEntityList);
        }else if (event.getCallType()==BusEnum.GROUP_UPDATE){
            loadChatGroup();
        }else if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
            if (listData.size()==0){
                requstData();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==getActivity().RESULT_OK){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        requstData();
    }

    @Override
    public void onLoadmore() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImMesssageRedDot.registerGroup(null);
        NIMClient.getService(TeamServiceObserver.class).observeTeamUpdate(teamUpdateObserver, false);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if(group_chat_login_layout != null) {
            if (user == null) {
                group_chat_login_layout.setVisibility(View.VISIBLE);
            } else {
                group_chat_login_layout.setVisibility(View.GONE);
            }
        }
    }

    GroupChatEntity groupChatHeadEntity=new GroupChatEntity();
    List<GroupSeedEntity> groupChatTop=new ArrayList<>();
    List<GroupSeedEntity> groupChat=new ArrayList<>();
    /**
     * 有消息回调
     */
    @Override
    public void callGroupRedDot() {
        if (chatGroupEntity!=null){
            GroupEntity groupEntity=new GroupEntity();
            List<GroupSeedEntity> listSeed=new ArrayList<GroupSeedEntity>();
            if (groupChatTop.size()>0){
                for (int i=0;i<ImMesssageRedDot.mListDatan.size();i++){
                    GroupSeedEntity top;
                    for (int t=0;t<groupChatTop.size();t++){
                        if (ImMesssageRedDot.mListDatan.get(i).getContactId().equals(groupChatTop.get(t).getT_id())){
                            top=groupChatTop.get(t);
                            groupChatTop.remove(t);
                            groupChatTop.add(0,top);
                            break;
                        }
                    }
                }
            }
            if (groupChat.size()>0){
                for (int i=0;i<ImMesssageRedDot.mListDatan.size();i++){
                    GroupSeedEntity seed;
                    for (int t=0;t<groupChat.size();t++){
                        if (ImMesssageRedDot.mListDatan.get(i).getContactId().equals(groupChat.get(t).getT_id())){
                            seed=groupChat.get(t);
                            groupChat.remove(t);
                            groupChat.add(0,seed);
                            break;
                        }
                    }
                }
            }

            listSeed.addAll(groupChatTop);
            listSeed.addAll(groupChat);
            groupEntity.setProvince("好友群");
            groupEntity.setCount(listSeed.size());
            JSONArray ja = JSONArray.parseArray(JSON.toJSONString(listSeed));
            groupEntity.setCity_family_list(ja.toJSONString());
            groupEntity.setOpen(true);
            if (listDataHeab.size()>0){
                listDataHeab.set(0,groupEntity);
            }else {
                listDataHeab.add(0,groupEntity);
            }
            groupChatHeadEntity.setGroupList(listDataHeab);
            groupChatHeadEntity.setNumbers(listSeed.size());
            groupChatHeadEntity.setGroupName("好友群");
            if (groupChatEntityList.size()<2){
                if (listSeed.size()==0)return;
                if (groupChatEntityList.size()==1){
                    if (groupChatEntityList.get(0).getGroupName().equals("好友群")){
                        groupChatEntityList.set(0,groupChatHeadEntity);
                    }else{
                        groupChatEntityList.add(0,groupChatHeadEntity);
                    }
                }else {
                    groupChatEntityList.add(groupChatHeadEntity);
                }
            }else {
                if (listSeed.size()==0){
                    groupChatEntityList.remove(0);
                }else {
                    groupChatEntityList.set(0,groupChatHeadEntity);
                }
            }
//            if (null==listData.get(0).getProvince() || !listData.get(0).getProvince().contains("好友群")){
//                listData.add(0,groupEntity);
//            }else {
//                listData.set(0,groupEntity);
//            }
        }
        if (groupChatEntityList.size()>0)
            mAdapter.refresh(groupChatEntityList);
    }
}
