package com.yuejian.meet.activities.clan;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.NoticeInfoAdapter;
import com.yuejian.meet.bean.ClanBulletinEntity;
import com.yuejian.meet.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class NoticeInfoActivity
        extends BaseActivity
{
    ClanBulletinEntity entity;
    @Bind(R.id.notice_grid)
    GridView gridView;
    List<String> listData = new ArrayList();
    NoticeInfoAdapter mAdapter;
    @Bind(R.id.notice_info_content)
    TextView notice_info_content;
    @Bind(R.id.notice_tiem)
    TextView notice_tiem;

    public void initData()
    {
        this.notice_info_content.setText(this.entity.getBulletin_info());
        this.notice_tiem.setText(TimeUtils.secondsToDate(Long.parseLong(this.entity.getBulletin_createtime())));
        if (!StringUtil.isEmpty(this.entity.getBulletin_img()))
        {
            String[] arrayOfString = this.entity.getBulletin_img().split(",");
            int i = 0;
            while (i < arrayOfString.length)
            {
                this.listData.add(arrayOfString[i]);
                i += 1;
            }
        }
        this.mAdapter = new NoticeInfoAdapter(this.gridView, this.listData, R.layout.item_notice_info_img_layout);
        this.gridView.setAdapter(this.mAdapter);
        this.mAdapter.refresh(this.listData);
    }

    public void onClick(View paramView) {}

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_notice_info);
        this.entity = ((ClanBulletinEntity)getIntent().getSerializableExtra("clanNoticeEntity"));
        setTitleText(getString(R.string.notice_info_text_1));
        initData();
    }
}
