package com.yuejian.meet.activities.clan;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ClanMasterMemberAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanFindApprovaEntity;
import com.yuejian.meet.bean.ClanMiAllEntity;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanMemberActivity extends BaseActivity implements SpringView.OnFreshListener, CleanableEditText.ContentClear, OnEditorActionListener {
    String clanId;
    String clanName;
    String customer_id;
    LoadingDialogFragment dialog;
    Intent intent;
    List<ClanFindApprovaEntity> listData = new ArrayList();
    List<ClanFindApprovaEntity> listDatas = new ArrayList();
    @Bind(R.id.clan_member_list)
    ListView listView;
    ClanMasterMemberAdapter mAdapter;
    int pageIndex = 1;
    CleanableEditText search;
    @Bind(R.id.clan_member_spring)
    SpringView springView;
    String tId;
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    int type = 2;

    public void ClearText() {
        this.pageIndex = 1;
        postSearch();
    }

    public void getClanId() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("wy_team_id", this.tId);
        this.apiImp.teaIdToClanId(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (StringUtil.isEmpty(paramAnonymousString)) {
                    ViewInject.toast(ClanMemberActivity.this.getApplication(), "获取群成功失败");
                    ClanMemberActivity.this.finish();
                    return;
                }
                MoreClanEntity moreClanEntity = (MoreClanEntity) JSON.parseObject(paramAnonymousString, MoreClanEntity.class);
                ClanMemberActivity.this.clanId = moreClanEntity.getAssociation_id();
                ClanMemberActivity.this.customer_id = moreClanEntity.getCustomer_id();
                ClanMemberActivity.this.clanName = moreClanEntity.getAssociation_name();
                ClanMemberActivity.this.requstData();
            }
        });
    }

    @SuppressLint({"ResourceAsColor"})
    public void initData() {
        this.dialog = LoadingDialogFragment.newInstance("正在操作..");
        this.intent = getIntent();
        if (this.intent.hasExtra("clanId")) {
            this.clanId = this.intent.getStringExtra("clanId");
        }
        if (this.intent.hasExtra("customer_id")) {
            this.customer_id = this.intent.getStringExtra("customer_id");
        }
        if (this.intent.hasExtra("clanName")) {
            this.clanName = this.intent.getStringExtra("clanName");
        }
        if (this.intent.hasExtra("type")) {
            this.type = this.intent.getIntExtra("type", 0);
        }
        if (this.intent.hasExtra("t_id")) {
            this.tId = this.intent.getStringExtra("t_id");
        }
        if (this.type != 0) {
            this.txt_titlebar_release.setVisibility(View.VISIBLE);
        }
        this.txt_titlebar_release.setTextColor(Color.parseColor("#ffffff"));
        this.txt_titlebar_release.setText("邀请好友");
        setTitleText("成员");
        this.search = ((CleanableEditText)findViewById(R.id.et_clan_member_search));
        this.search.setRegister(this);
        this.search.setOnEditorActionListener(this);
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setListener(this);
        this.mAdapter = new ClanMasterMemberAdapter(this.listView, this.listData, R.layout.item_clan_master_member_list_layout, this.type);
        this.listView.setAdapter(this.mAdapter);
        if (StringUtil.isEmpty(this.tId)) {
            requstData();
            return;
        }
        getClanId();
    }

    @OnClick({R.id.txt_titlebar_release})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.txt_titlebar_release:
                intent = new Intent(this, ClanInviteMemberActivity.class);
                intent.putExtra("clanId", this.clanId);
                intent.putExtra("clanName", this.clanName);
                startActivity(intent);
                return;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.activity_clan_member);
        initData();
        setResult(-1, new Intent());
    }

    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
        if ((paramInt == 4) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66))) {
            Utils.hintKbTwo(this);
            postSearch();
            return true;
        }
        return false;
    }

    public void onLoadmore()
    {
        this.pageIndex += 1;
    }

    public void onRefresh()
    {
        this.pageIndex = 1;
    }

    public void postSearch() {
        if (this.listData == null) {
            return;
        }
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        while (i < this.listData.size()) {
            if ((((ClanFindApprovaEntity)this.listData.get(i)).getSurname() + ((ClanFindApprovaEntity)this.listData.get(i)).getName()).contains(this.search.getText().toString())) {
                localArrayList.add(this.listData.get(i));
            }
            i += 1;
        }
        this.mAdapter.refresh(localArrayList);
    }

    public void remoeMember(final ClanFindApprovaEntity paramClanFindApprovaEntity) {
        Builder localBuilder = new Builder(this);
        localBuilder.setTitle("提示");
        localBuilder.setMessage("你确定要删除成员吗");
        localBuilder.setNegativeButton("确定", new OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                paramAnonymousDialogInterface.dismiss();
                Map<String,Object> params = new HashMap();
                params.put("member_id", paramClanFindApprovaEntity.getMember_id());
                ClanMemberActivity.this.apiImp.clanMemberRemoe(params, this, new DataIdCallback<String>()
                {
                    public void onFailed(String paramAnonymous2String1, String paramAnonymous2String2, int paramAnonymous2Int) {}

                    public void onSuccess(String paramAnonymous2String, int paramAnonymous2Int){
                        listData.remove(paramClanFindApprovaEntity);
                        ClanMemberActivity.this.postSearch();
                    }
                });
            }
        });
        localBuilder.setNeutralButton("取消", null);
        localBuilder.show();
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("customer_id", this.customer_id);
        this.apiImp.clanMember(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ClanMiAllEntity clanMiAllEntity = (ClanMiAllEntity) JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class);
                if (!StringUtil.isEmpty(clanMiAllEntity.getCustomer())){
                    ClanMemberActivity.this.listData.add(JSON.parseObject(clanMiAllEntity.getCustomer(), ClanFindApprovaEntity.class));
                }
                ClanMemberActivity.this.listData.addAll(JSON.parseArray(clanMiAllEntity.getMemberList(), ClanFindApprovaEntity.class));
                ClanMemberActivity.this.listDatas.add(JSON.parseObject(clanMiAllEntity.getCustomer(), ClanFindApprovaEntity.class));
                ClanMemberActivity.this.listDatas.addAll(JSON.parseArray(clanMiAllEntity.getMemberList(), ClanFindApprovaEntity.class));
                ClanMemberActivity.this.postSearch();
            }
        });
    }
}
