package com.yuejian.meet.activities.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.helper.MessageListPanelHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.TeamFamilyAdapter;
import com.yuejian.meet.adapters.TeamTempyAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.AdvancedTeamInfoEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class AdvancedTeamInfoActivity extends BaseActivity {

    @Bind(R.id.family_grid)
    InnerGridView family_grid;
    @Bind(R.id.temp_grid)
    GridView temp_grid;
    @Bind(R.id.img_group_user_picture)
    ImageView img_group_user_picture;
    @Bind(R.id.img_grouop_user_sponsor)
    ImageView img_grouop_user_sponsor;
    @Bind(R.id.group_message_hint)
    CheckBox group_message_hint;
    String t_id;
    @Bind(R.id.group_notice)
    TextView group_notice;
    @Bind(R.id.txe_group_user_name)
    TextView txe_group_user_name;
    @Bind(R.id.group_temp)
    TextView group_temp;
    @Bind(R.id.group_family)
    TextView group_family;
    @Bind(R.id.group_temp_layout)
    LinearLayout group_temp_layout;
    AdvancedTeamInfoEntity entity = new AdvancedTeamInfoEntity();
    List<MembersEntity> familyList = new ArrayList<>();
    List<MembersEntity> tempList = new ArrayList<>();
    TeamTempyAdapter teamTempyAdapter;
    TeamFamilyAdapter teamFamilyAdapter;
    public ApiImp api = new ApiImp();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_team_info);
        t_id = getIntent().getStringExtra("t_id");
        initData();
    }

    public void initData() {
        setTitleText("群聊信息");
        temp_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUitls.goToPersonHome(mContext, tempList.get(position).getCustomer_id());
            }
        });
        family_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (familyList.size() == position) {
                    intent = new Intent(getApplication(), InviteJoinGroupActivity.class);
                    intent.putExtra("tid", t_id);
                    startActivity(intent);
                } else {
                    AppUitls.goToPersonHome(mContext, familyList.get(position).getCustomer_id());
                }
            }
        });
        requstData();
    }

    public void requstData() {
        Map<String, Object> params = new HashMap<>();
        params.put("t_id", t_id);
        api.getGroupUserInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity = JSON.parseObject(data, AdvancedTeamInfoEntity.class);
                familyList = JSON.parseArray(entity.getFamilyMemberList(), MembersEntity.class);
                tempList = JSON.parseArray(entity.getTempMemberList(), MembersEntity.class);
                if (tempList.size() > 0) {

                    teamTempyAdapter = new TeamTempyAdapter(temp_grid, tempList, R.layout.item_action_info_header_reward_img_layout);
                    temp_grid.setAdapter(teamTempyAdapter);
                    teamTempyAdapter.refresh(tempList);
                } else {
                    group_temp_layout.setVisibility(View.GONE);
                }
                teamFamilyAdapter = new TeamFamilyAdapter(mContext, familyList);
                family_grid.setAdapter(teamFamilyAdapter);
                teamFamilyAdapter.notifyDataSetChanged();
                initViewData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    ///设置数据
    public void initViewData() {
        group_temp.setText("查看全部访客(" + entity.getTemp_member_cnt() + ")");
        group_family.setText("查看全部群成员(" + entity.getGroup_member_cnt() + ")");
        txe_group_user_name.setText(StringUtil.isEmpty(entity.getCustomer_id()) ? "去认领" : entity.getSurname() + entity.getName());
        group_message_hint.setChecked(entity.getNotification().equals("1") ? true : false);
        group_notice.setText(entity.getGroup_notice());
        if (entity.getIs_super() > 0) {
            Glide.with(getApplication()).load(entity.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : entity.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(img_grouop_user_sponsor);
        }
        Glide.with(this).load(entity.getPhoto()).error(R.mipmap.ic_default).into(img_group_user_picture);
    }

    //设置消息提示
    public void setSwitch() {
        Map<String, Object> params = new HashMap<>();
        params.put("t_id", t_id);
        params.put("on_off", entity.getNotification().equals("1") ? "0" : "1");
        api.setSwitcNotification(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity.setNotification(entity.getNotification().equals("0") ? "1" : "0");
                group_message_hint.setChecked(entity.getNotification().equals("1") ? true : false);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            group_notice.setText(data.getStringExtra("content"));
            entity.setGroup_notice(data.getStringExtra("content"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            group_notice.setText(data.getStringExtra("content"));
            entity.setGroup_notice(data.getStringExtra("content"));
        }
    }

    @OnClick({R.id.message_chat_clear, R.id.img_group_user_picture, R.id.group_message_hint, R.id.group_notice_layout, R.id.group_temp, R.id.group_family,
            R.id.img_group_temp})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_chat_clear://清除数据
                // 删除与某个聊天对象的全部消息记录
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("是否要清除群信息");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NIMClient.getService(MsgService.class).clearChattingHistory(t_id, SessionTypeEnum.Team);
                        MessageListPanelHelper.getInstance().notifyClearMessages(t_id);
                    }
                });
                builder.show();
                break;
            case R.id.img_group_user_picture://点头像
                if (StringUtil.isEmpty(entity.getCustomer_id())) {
                    AppConfig.family_id = entity.family_id;
                    AppConfig.AreaName = entity.getArea_name();
                    intent = new Intent(this, ClaimCitySponsorActivity.class);
//                        intent.putExtra("t_id",t_id);
                    startActivity(intent);
                } else {
                    AppUitls.goToPersonHome(mContext, entity.getCustomer_id());
                }
                break;
            case R.id.group_message_hint://消息提示设置
                setSwitch();
                break;
            case R.id.group_notice_layout://公告设置
                intent = new Intent(this, GroupNoticeActivity.class);
                intent.putExtra("content", entity.getGroup_notice());
                intent.putExtra("t_id", t_id);
                intent.putExtra("isOwner", entity.getCustomer_id().equals(AppConfig.CustomerId) ? true : false);
                startActivityIfNeeded(intent, 1);
                break;
            case R.id.group_temp://查看全部访客
                intent = new Intent(this, GroupAllMemberActivity.class);
                intent.putExtra("isMember", false);
                intent.putExtra("t_id", t_id);
                startActivity(intent);
                break;
            case R.id.img_group_temp://查看全部访客
                intent = new Intent(this, GroupAllMemberActivity.class);
                intent.putExtra("isMember", false);
                intent.putExtra("t_id", t_id);
                startActivity(intent);
                break;
            case R.id.group_family://查看全部成员
                intent = new Intent(this, GroupAllMemberActivity.class);
                intent.putExtra("isMember", true);
                intent.putExtra("t_id", t_id);
                startActivity(intent);
                break;
        }
    }
}
