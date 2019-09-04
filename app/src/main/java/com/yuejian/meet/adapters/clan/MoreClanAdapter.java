package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.utils.ImUtils;

import java.util.List;

//import com.yuejian.meet.activities.clan.ClanInfoActivity;

public class MoreClanAdapter extends FKAdapter<MoreClanEntity> {
    private Context context;
    private AdapterHolder mHelper;
    private String zuciId;

    public MoreClanAdapter(AbsListView paramAbsListView, List<MoreClanEntity> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }
    public void setZuciId(String zuciId){
        this.zuciId=zuciId;
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final MoreClanEntity paramMoreClanEntity, int paramInt) {
        final int i = 0;
        this.mHelper = paramAdapterHolder;
        mHelper.getView(R.id.item_association_status).setVisibility(paramMoreClanEntity.getAssociation_status().equals("1")?View.GONE:View.VISIBLE);
        paramAdapterHolder.setText(R.id.item_img_more_clan_name, paramMoreClanEntity.getAssociation_name());
        paramAdapterHolder.getView(R.id.item_img_more_clan_loation).setVisibility(paramMoreClanEntity.getKm().equals("-1")?View.GONE:View.VISIBLE);
        paramAdapterHolder.setText(R.id.item_img_more_clan_loation, paramMoreClanEntity.getKm() + "km");
        paramAdapterHolder.setText(R.id.item_img_more_clan_count, paramMoreClanEntity.getAssociation_cnt() + "人");
        Glide.with(this.context).load(paramMoreClanEntity.getAssociation_img()).asBitmap().into((ImageView)paramAdapterHolder.getView(R.id.item_img_more_clan_photo));
        this.mHelper.getConvertView().setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(MoreClanAdapter.this.context, ClanInfoActivity.class);
                    intent.putExtra("clanEntity",paramMoreClanEntity);
                    context.startActivity(intent);
            }
        });
        if (!StringUtil.isEmpty(zuciId)){
            mHelper.getView(R.id.banding_clan_but).setVisibility(View.VISIBLE);
            mHelper.getView(R.id.banding_clan_but).setSelected(paramMoreClanEntity.getIs_join()==1?false:true);
            mHelper.getView(R.id.banding_clan_but).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paramMoreClanEntity.getIs_join()==1){
                        ImUtils.toGroupChat(context, paramMoreClanEntity.getWy_team_id(), ChatEnum.CLANGROUP, "0", Boolean.valueOf(true));
                    }else {
                        Intent intent = new Intent(MoreClanAdapter.this.context, ClanInfoActivity.class);
                        intent.putExtra("clanEntity",paramMoreClanEntity);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void convert(AdapterHolder paramAdapterHolder, MoreClanEntity paramMoreClanEntity, boolean paramBoolean, int paramInt) {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramMoreClanEntity, paramInt);
    }
}
