package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.BuySingleVipPermissionActivity;
import com.yuejian.meet.bean.SingleVipDetailEntity;
import com.yuejian.meet.common.GlobalVipInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/17 17:59
 * @desc : 点亮VIP单项权限列表适配器
 */
public class LightUpSingleVipPermissionListAdapter extends RecyclerView.Adapter<LightUpSingleVipPermissionListAdapter.SingleVipPermissionViewHolder> {

    private Context mContext;
    List<SingleVipDetailEntity> mSingleVipDetailEntities;

    public LightUpSingleVipPermissionListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SingleVipPermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_vip_permission, parent, false);
        return new LightUpSingleVipPermissionListAdapter.SingleVipPermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleVipPermissionViewHolder holder, int position) {
        SingleVipDetailEntity entity = mSingleVipDetailEntities.get(position);
        boolean isOpen = entity.getRemaining_days() > 0;
        holder.mIconView.setImageDrawable(mContext.getResources().getDrawable(getLocalResourceID(entity.getEnglish_name(), isOpen)));
        holder.mTextView.setText(entity.getVip_name());
        int days = entity.getRemaining_days();
        holder.mTimeView.setVisibility(days > 0 ? View.VISIBLE : View.INVISIBLE);
        if (days > 0) {
            holder.mTimeView.setText(String.format("剩余%s天", days));
            holder.mTimeView.setTextColor(entity.isTag_red() ? Color.parseColor("#BE3232") : Color.parseColor("#D09B21"));
        }
    }

    private int getLocalResourceID(String name, boolean isOpen) {
        switch (name) {
            case "faqi":
                return isOpen ? R.mipmap.icon_faqi_sel : R.mipmap.icon_faqi_nor;
            case "fabu":
                return isOpen ? R.mipmap.icon_fabu_sel : R.mipmap.icon_fabu_nor;
            case "youpin":
                return isOpen ? R.mipmap.icon_youpin_sel : R.mipmap.icon_youpin_nor;
            case "peizhi":
                return isOpen ? R.mipmap.icon_peizhi_sel : R.mipmap.icon_peizhi_nor;
            case "ziyuan":
                return isOpen ? R.mipmap.icon_ziyuan_sel : R.mipmap.icon_ziyuan_nor;
            case "xiangmu":
                return isOpen ? R.mipmap.icon_xiangmu_sel : R.mipmap.icon_xiangmu_nor;
            case "yizhuanbao":
                return isOpen ? R.mipmap.icon_yizhuanbao_sel : R.mipmap.icon_yizhuanbao_nor;
            case "poster":
                return isOpen ? R.mipmap.icon_poster_sel : R.mipmap.icon_poster_nor;
            case "ship":
                return isOpen ? R.mipmap.icon_ship_sel : R.mipmap.icon_ship_nor;
            case "xiangc":
                return isOpen ? R.mipmap.icon_xiangc_sel : R.mipmap.icon_xiangc_nor;
            default:
                return isOpen ? R.mipmap.icon_poster_sel : R.mipmap.icon_poster_nor;
        }
    }

    @Override
    public int getItemCount() {
        if (mSingleVipDetailEntities == null) return 0;
        return mSingleVipDetailEntities.size();
    }

    public void updateListData(List<SingleVipDetailEntity> singleVipDetailEntities) {
        if (mSingleVipDetailEntities == null) {
            mSingleVipDetailEntities = new ArrayList<>();
        }
        mSingleVipDetailEntities = singleVipDetailEntities;
        notifyDataSetChanged();
    }

    class SingleVipPermissionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIconView;
        TextView mTextView;
        TextView mTimeView;

        public SingleVipPermissionViewHolder(View itemView) {
            super(itemView);
            mIconView = (ImageView) itemView.findViewById(R.id.iv_vip_permission_icon);
            mTextView = (TextView) itemView.findViewById(R.id.tv_vip_permission_text);
            mTimeView = (TextView) itemView.findViewById(R.id.tv_vip_permission_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, BuySingleVipPermissionActivity.class);
            intent.putExtra("index", getAdapterPosition());
            mContext.startActivity(intent);
        }
    }
}
