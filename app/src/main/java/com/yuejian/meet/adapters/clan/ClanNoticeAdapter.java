package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.NoticeInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanBulletinEntity;
import com.yuejian.meet.utils.TimeUtils;

import java.util.List;

public class ClanNoticeAdapter
        extends FKAdapter<ClanBulletinEntity>
{
    private Context context;
    private AdapterHolder mHelper;

    public ClanNoticeAdapter(AbsListView paramAbsListView, List<ClanBulletinEntity> paramList, int paramInt)
    {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ClanBulletinEntity paramClanBulletinEntity, int paramInt)
    {
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.item_clan_notice_conten, paramClanBulletinEntity.getBulletin_info());
        paramAdapterHolder.setText(R.id.item_clan_notice_time, TimeUtils.secondsToDate(Long.parseLong(paramClanBulletinEntity.getBulletin_createtime())));
        this.mHelper.getConvertView().setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent intent = new Intent(ClanNoticeAdapter.this.context, NoticeInfoActivity.class);
                intent.putExtra("clanNoticeEntity", paramClanBulletinEntity);
                ClanNoticeAdapter.this.context.startActivity(intent);
            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanBulletinEntity paramClanBulletinEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanBulletinEntity, paramInt);
    }
}
