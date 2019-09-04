package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ClanMasterApproveAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanFindApprovaEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

public class ClanMasterApproveActivity extends BaseActivity {
    String clanId;
    LoadingDialogFragment dialog;
    List<ClanFindApprovaEntity> listData;
    @Bind(R.id.approve_list)
    ListView listView;
    ClanMasterApproveAdapter mAdapter;

    public void approvaNoPass(String paramString)
    {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("member_id", paramString);
        this.apiImp.clanFindApprovaNoPass(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt)
            {
                if (ClanMasterApproveActivity.this.dialog != null) {
                    ClanMasterApproveActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ClanMasterApproveActivity.this.requstData();
            }
        });
    }

    public void approvaPass(String paramString)
    {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("member_id", paramString);
        this.apiImp.clanFindApprovaPass(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt)
            {
                if (ClanMasterApproveActivity.this.dialog != null) {
                    ClanMasterApproveActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ClanMasterApproveActivity.this.requstData();
            }
        });
    }

    public void initData()
    {
        setTitleText("加入申请");
        this.dialog = LoadingDialogFragment.newInstance("正在操作..");
        this.mAdapter = new ClanMasterApproveAdapter(this.listView, this.listData, R.layout.item_clan_approve_lsit_layout);
        this.listView.setAdapter(this.mAdapter);
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        requstData();
    }

    public void onClick(View paramView) {}

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_master_approve);
        this.clanId = getIntent().getStringExtra("clanId");
        initData();
        setResult(-1, new Intent());
    }

    public void requstData()
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        this.apiImp.clanFindApprova(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt)
            {
                ClanMasterApproveActivity.this.finish();
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ClanMasterApproveActivity.this.listData = JSON.parseArray(paramAnonymousString, ClanFindApprovaEntity.class);
                ClanMasterApproveActivity.this.mAdapter.refresh(ClanMasterApproveActivity.this.listData);
                if (ClanMasterApproveActivity.this.dialog != null) {
                    ClanMasterApproveActivity.this.dialog.dismiss();
                }
            }
        });
    }
}
