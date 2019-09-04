package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.graphics.Color;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanMemberChargeEntity;

import java.util.List;

public class ClanProceedsInfoAdapter extends FKAdapter<ClanMemberChargeEntity> {
    private Context context;
    private AdapterHolder mHelper;

    public ClanProceedsInfoAdapter(AbsListView paramAbsListView, List<ClanMemberChargeEntity> paramList, int paramInt)
    {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, ClanMemberChargeEntity paramClanMemberChargeEntity, int paramInt) {
        this.mHelper = paramAdapterHolder;
        TextView textView=mHelper.getView(R.id.item_clan_member_remoe);
        if (paramClanMemberChargeEntity.getPay_status().equals("0")){
            textView.setText("未交款");
            textView.setTextColor(Color.parseColor("#ebb156"));
        }else {
            textView.setText("已交款");
            textView.setTextColor(Color.parseColor("#999999"));
        }
        paramAdapterHolder.setText(R.id.item_clan_member_name, paramClanMemberChargeEntity.getSurname() + paramClanMemberChargeEntity.getName());
        paramAdapterHolder.setText(R.id.item_clan_member_age, " " + paramClanMemberChargeEntity.getAge());
        Glide.with(this.context).load(paramClanMemberChargeEntity.getPhoto()).asBitmap().into((ImageView)paramAdapterHolder.getView(R.id.item_clan_member_photo));
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanMemberChargeEntity paramClanMemberChargeEntity, boolean paramBoolean, int paramInt) {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanMemberChargeEntity, paramInt);
    }
}
