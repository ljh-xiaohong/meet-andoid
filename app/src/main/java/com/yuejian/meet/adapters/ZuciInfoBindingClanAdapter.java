package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.bean.ZuciFootprintsEntity;
import com.yuejian.meet.utils.ImUtils;

import java.util.List;

public class ZuciInfoBindingClanAdapter extends Adapter<ZuciInfoBindingClanAdapter.ViewHolder> {
    Context context;
    List<MoreClanEntity> listData;

    public int getItemCount() {
        if (this.listData == null) {
            return 0;
        }
        return this.listData.size();
    }

    public void onBindViewHolder(ViewHolder holder, int paramInt) {
        final MoreClanEntity item = listData.get(paramInt);
        Glide.with(context).load(item.getAssociation_img()).into(holder.photo);
        holder.number.setText(" "+item.getAssociation_cnt());
        holder.name.setText(item.getAssociation_name());
        holder.banding_but.setSelected(item.getIs_exist()==0?true:false);
        holder.banding_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getIs_exist()==1){
                    ImUtils.toGroupChat(context, item.getWy_team_id(), ChatEnum.CLANGROUP, "0", Boolean.valueOf(true));
                }else {

                    Intent intent = new Intent(context, ClanInfoActivity.class);
                    intent.putExtra("clanEntity", item);
                    context.startActivity(intent);
                }
            }
        });
    }

    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        this.context = paramViewGroup.getContext();
        return new ViewHolder(View.inflate(this.context, R.layout.item_zuci_info_banding_clan_layout, null));
    }

    public void setListData(List<MoreClanEntity> paramList) {
        this.listData = paramList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView banding_but;
        public TextView name;
        public ImageView photo;

        public ViewHolder(View paramView) {
            super(paramView);
            this.photo = ((ImageView)paramView.findViewById(R.id.item_banding_clan_photo));
            this.name = ((TextView)paramView.findViewById(R.id.item_banding_clan_name));
            this.number = ((TextView)paramView.findViewById(R.id.item_banding_clan_name_number));
            this.banding_but = ((TextView)paramView.findViewById(R.id.banding_clan_but));
        }
    }
}
