package com.yuejian.meet.activities.group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.session.helper.MessageListPanelHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.GroupNoticeActivity;
import com.yuejian.meet.adapters.GroupUserInfoAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ChatGroupOwnerEntity;
import com.yuejian.meet.bean.GroupAllInfoEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发起群聊用户详情页
 */
public class GroupUserinfo extends BaseActivity implements AdapterView.OnItemClickListener{
    InnerGridView gridView;
    @Bind(R.id.group_turn)
    LinearLayout groupRurn;
    @Bind(R.id.txt_group_notice)
    TextView groupNotice;
    @Bind(R.id.group_name)
    TextView groupName;
    @Bind(R.id.group_message_hint)
    CheckBox group_message_hint;
    @Bind(R.id.cb_chat_top)
    CheckBox cb_chat_top;
    @Bind(R.id.group_userinfo_number)
    TextView group_userinfo_number;
    String t_id;
    List<MembersEntity> listData=new ArrayList<>();
    GroupAllInfoEntity groupInfoEntity=new GroupAllInfoEntity();
    GroupUserInfoAdapter mAdapter;
    ChatGroupOwnerEntity chatGroupOwnerEntity;
    Intent intent;
    ApiImp api=new ApiImp();
    LoadingDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "发起群聊用户详情页";
        setContentView(R.layout.activity_group_userinfo);
        t_id=getIntent().getStringExtra("t_id");
        initView();
    }
    public void initView(){
        dialog=LoadingDialogFragment.newInstance("正在操作..");
        setTitleText("群聊信息");
        gridView= (InnerGridView) findViewById(R.id.group_grid);
        gridView.setOnItemClickListener(this);
        mAdapter=new GroupUserInfoAdapter(this,listData);
        gridView.setAdapter(mAdapter);

        loadData();

    }
    android.app.AlertDialog.Builder builder;
    @OnClick({R.id.bt_quit,R.id.group_message_chat_clear,R.id.cb_chat_top,R.id.group_message_hint,R.id.group_turn,R.id.group_notice_layout
    ,R.id.group_name_layout,R.id.group_userinfo_number})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_quit://退出群
                builder=new android.app.AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("你是否退出该群");
                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quitGroup();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.group_message_chat_clear://清除群消息
                // 删除与某个聊天对象的全部消息记录
                builder=new android.app.AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("是否要清除群信息");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NIMClient.getService(MsgService.class).clearChattingHistory(t_id, SessionTypeEnum.Team);
                        MessageListPanelHelper.getInstance().notifyClearMessages(t_id);
                    }
                });
                builder.show();
                break;
            case R.id.cb_chat_top://群消息置顶
                setGroupTop();
                break;
            case R.id.group_message_hint://群消息提示
                updateGroupNotifinaction();
                break;
            case R.id.group_turn://群主转让
                intent=new Intent(mContext,GroupMemberManagerActivity.class);
                intent.putExtra("tId",t_id);
                intent.putExtra("isDelect",false);
                startActivityIfNeeded(intent,6);
                break;
            case R.id.group_notice_layout://群公告
                intent=new Intent(this,GroupsNoticeActivity.class);
                intent.putExtra("content",chatGroupOwnerEntity.getGroup_notice());
                intent.putExtra("t_id",t_id);
                intent.putExtra("isOwner",chatGroupOwnerEntity.getGroup_master_id().equals(AppConfig.CustomerId)?true:false);
                startActivityIfNeeded(intent,1);
                break;
            case R.id.group_name_layout://群名
                intent=new Intent(this,GroupNameActivity.class);
                intent.putExtra("t_id",chatGroupOwnerEntity.getT_id());
                intent.putExtra("name",chatGroupOwnerEntity.getGroup_name());
                startActivityIfNeeded(intent,2);
                break;
            case R.id.group_userinfo_number://查看所有群成员
                intent=new Intent(this,GroupMemberActivity.class);
                intent.putExtra("t_id",chatGroupOwnerEntity.getT_id());
                startActivityIfNeeded(intent,2);
                break;
        }
    }

    /**
     * 加载群信息
     */
    public void loadData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("t_id",t_id);
        api.getGroupInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                groupInfoEntity= JSON.parseObject(data,GroupAllInfoEntity.class);
                group_message_hint.setChecked(groupInfoEntity.getNotification()==1?true:false);
                cb_chat_top.setChecked(groupInfoEntity.getSort()==0?false:true);
                group_userinfo_number.setVisibility(View.GONE);
                if (groupInfoEntity.getMember_count()>23){
                    group_userinfo_number.setVisibility(View.VISIBLE);
                    group_userinfo_number.setText("查看全部群成员("+groupInfoEntity.getMember_count()+")");
                }
                chatGroupOwnerEntity=JSON.parseObject(groupInfoEntity.getChat_group(),ChatGroupOwnerEntity.class);
                groupName.setText(chatGroupOwnerEntity.getGroup_name());
                groupNotice.setText(chatGroupOwnerEntity.getGroup_notice());
                groupRurn.setVisibility(chatGroupOwnerEntity.getGroup_master_id().equals(AppConfig.CustomerId)?View.VISIBLE:View.GONE);
                listData=JSON.parseArray(groupInfoEntity.getTop18_members(),MembersEntity.class);
                mAdapter.refresh(listData,chatGroupOwnerEntity.getGroup_master_id().equals(AppConfig.CustomerId));
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(mContext,"获取群信息失败");
                finish();
            }
        });
    }
    //退群
    public void quitGroup(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("t_id",t_id);
        api.outLietGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(mContext,"退群成功");
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    /**
     * 消息提醒设置
     */
    public void updateGroupNotifinaction(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("t_id",t_id);
        params.put("is_switch",group_message_hint.isChecked()?"1":"0");
        api.getUpdateGroupNotifinaction(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(mContext,"设置成功");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                group_message_hint.setChecked(!group_message_hint.isChecked());
            }
        });
    }

    /**
     * 群聊置顶
     */
    public void setGroupTop(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("t_id",t_id);
        params.put("top_down",cb_chat_top.isChecked()?"1":"0");
        api.setGroupTop(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(mContext,"设置成功");
                BusCallEntity busCallEntity=new BusCallEntity();
                busCallEntity.setCallType(BusEnum.GROUP_UPDATE);
                Bus.getDefault().post(busCallEntity);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                cb_chat_top.setChecked(!cb_chat_top.isChecked());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 1:
                    if (chatGroupOwnerEntity!=null)
                        chatGroupOwnerEntity.setGroup_notice(data.getStringExtra("content"));
                    groupNotice.setText(data.getStringExtra("content"));
                    break;
                case 2:
                    if (chatGroupOwnerEntity!=null)
                        chatGroupOwnerEntity.setGroup_name(data.getStringExtra("name"));
                    groupName.setText(data.getStringExtra("name"));
                    break;
                case 6:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    },1000);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position<listData.size()){
            AppUitls.goToPersonHome(mContext, listData.get(position).getCustomer_id());
        }else if (position==listData.size()){
            intent=new Intent(mContext,SelectContactActivity.class);
            intent.putExtra("tId",t_id);
            intent.putExtra("isFound",false);
            startActivityIfNeeded(intent,6);
        }else if (position==(listData.size()+1)){
            intent=new Intent(mContext,GroupMemberManagerActivity.class);
            intent.putExtra("tId",t_id);
            intent.putExtra("isDelect",true);
            startActivityIfNeeded(intent,6);
        }
    }

    @Override
    protected void onDestroy() {
//        BusCallEntity busCallEntity=new BusCallEntity();
//        busCallEntity.setCallType(BusEnum.GROUP_UPDATE);
//        Bus.getDefault().post(busCallEntity);
        super.onDestroy();
    }
}
