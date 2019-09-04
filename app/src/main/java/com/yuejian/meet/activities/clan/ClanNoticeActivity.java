package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.ClanNoticeAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanBulletinEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanNoticeActivity extends BaseActivity implements OnItemClickListener {
    String clanId;
    LoadingDialogFragment dialog;
    Intent intent;
    List<ClanBulletinEntity> listData;
    @Bind(R.id.clan_notice_list)
    ListView listView;
    ClanNoticeAdapter mAdapter;
    @Bind(R.id.titlebar_create_essy)
    ImageView write_clan_notice;

    public void initData()
    {
        this.write_clan_notice.setVisibility(View.VISIBLE);
        this.mAdapter = new ClanNoticeAdapter(this.listView, this.listData, R.layout.item_clan_notice_list_layout);
        this.listView.setAdapter(this.mAdapter);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt2 == -1) {
            requstData();
        }
    }

    @OnClick({R.id.titlebar_create_essy})
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.titlebar_create_essy:
                this.intent = new Intent(this, ClanNoticeReleaseActivity.class);
                this.intent.putExtra("clanId", this.clanId);
                startActivityIfNeeded(this.intent, 11);
                return;
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_notice);
        this.dialog = LoadingDialogFragment.newInstance("正在加载..");
        this.clanId = getIntent().getStringExtra("clanId");
        setTitleText("公告");
        initData();
        requstData();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        startActivity(new Intent(this, NoticeInfoActivity.class));
    }

    public void requstData()
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        this.apiImp.clanBulletin(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ClanNoticeActivity.this.listData = JSON.parseArray(paramAnonymousString, ClanBulletinEntity.class);
                ClanNoticeActivity.this.mAdapter.refresh(ClanNoticeActivity.this.listData);
            }
        });
    }
}
