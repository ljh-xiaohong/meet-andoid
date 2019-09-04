package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanMemberChargeSelActivity;
import com.yuejian.meet.bean.ClanFindApprovaEntity;

import java.util.List;

public class ClanMemberchargeSelAdapter extends Adapter<ClanMemberchargeSelAdapter.ViewHolder> {
    Context context;
    List<ClanFindApprovaEntity> listData;

    public int getItemCount()
    {
        if (this.listData == null) {
            return 0;
        }
        return this.listData.size();
    }

    public void onBindViewHolder(ViewHolder paramViewHolder, final int paramInt)
    {
        Glide.with(this.context).load(((ClanFindApprovaEntity)this.listData.get(paramInt)).getPhoto()).override(Utils.dp2px(this.context, 33.0F), Utils.dp2px(this.context, 33.0F)).into(paramViewHolder.mImageView);
        paramViewHolder.mImageView.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ((ClanFindApprovaEntity)ClanMemberchargeSelAdapter.this.listData.get(paramInt)).setSelect(Boolean.valueOf(false));
                ((ClanMemberChargeSelActivity)ClanMemberchargeSelAdapter.this.context).delectSelectData((ClanFindApprovaEntity)ClanMemberchargeSelAdapter.this.listData.get(paramInt));
            }
        });
    }

    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        this.context = paramViewGroup.getContext();
        return new ViewHolder(View.inflate(this.context, R.layout.invite_group_member_layout, null));
    }

    public void setListData(List<ClanFindApprovaEntity> paramList)
    {
        this.listData = paramList;
        notifyDataSetChanged();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;

        public ViewHolder(View paramView)
        {
            super(paramView);
            this.mImageView = ((ImageView)paramView.findViewById(R.id.group_member_img));
        }
    }
}
