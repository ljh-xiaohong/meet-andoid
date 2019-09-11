package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FamilyCircleFollowListViewHolder> {

    private List<FamilyFollowEntity.DataBean> mFollowEntities;
    private Context mContext;
    private Activity mActivity;
    private OnFollowListItemClickListener mListItemClickListener;
    private ApiImp apiImp;
    private boolean isCheckeds;
    private onClickListener mOnClickListener;

    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public FriendListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, Activity activity) {
        mContext = context;
        mActivity =activity;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
    }

    @Override
    public FamilyCircleFollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FamilyCircleFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleFollowListViewHolder holder, int position) {
        FamilyFollowEntity.DataBean entity = mFollowEntities.get(position);
        String headUrl = entity.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.shop_img);
        }
        if (!CommonUtil.isNull(entity.getVipType())&&Integer.parseInt(entity.getVipType())==1) {
            holder.name_type.setVisibility(View.VISIBLE);
        }else {
            holder.name_type.setVisibility(View.GONE);
        }
        holder.name.setText(entity.getName());
//        if ()
        holder.attention.setText("关注");
    }

    @Override
    public int getItemCount() {
        if (mFollowEntities == null) return 0;
        return mFollowEntities.size();
    }

    public void refresh(List<FamilyFollowEntity.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
        this.mFollowEntities = followEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<FamilyFollowEntity.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
            notifyDataSetChanged();
    }

    public interface OnFollowListItemClickListener {
        void onListItemClick(int type, int id);
    }

    class FamilyCircleFollowListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shop_img;
        ImageView name_type;
        TextView name,attention;



        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            shop_img = (CircleImageView) itemView.findViewById(R.id.shop_img);
            name = (TextView) itemView.findViewById(R.id.name);
            name_type = (ImageView) itemView.findViewById(R.id.name_type);
            attention = (TextView) itemView.findViewById(R.id.attention);
        }
    }
}
