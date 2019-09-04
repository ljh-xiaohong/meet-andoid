package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.TeamAllMemberAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 指定类型的所有群成员
 */
public class GroupAllMemberActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.member_grid)
    GridView member_grid;
    List<MembersEntity> listData = new ArrayList<>();
    TeamAllMemberAdapter teamFamilyAdapter;
    Boolean isMember = false;
    String t_id;
    ApiImp api = new ApiImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_all_member);
        initData();
    }

    public void initData() {
        isMember = getIntent().getBooleanExtra("isMember", false);
        t_id = getIntent().getStringExtra("t_id");
        setTitleText(isMember ? "成员" : "访客");
        teamFamilyAdapter = new TeamAllMemberAdapter(member_grid, listData, R.layout.item_group_family_member_layout);
        member_grid.setAdapter(teamFamilyAdapter);
        teamFamilyAdapter.notifyDataSetChanged();
        member_grid.setOnItemClickListener(this);
        requstData();
    }

    public void requstData() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", isMember ? "1" : "0");
        params.put("t_id", t_id);
        api.getAllGroupMember(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData = JSON.parseArray(data, MembersEntity.class);
                teamFamilyAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppUitls.goToPersonHome(mContext, listData.get(position).getCustomer_id());
    }
}
