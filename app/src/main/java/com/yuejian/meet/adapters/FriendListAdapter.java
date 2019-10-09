package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.letterList.FirstLetterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FamilyCircleFollowListViewHolder> {

    private List<NewFriendBean.DataBean> mFollowEntities;
    private Context mContext;
    private Activity mActivity;
    private OnFollowListItemClickListener mListItemClickListener;
    private ApiImp apiImp;
    private int tyep;
    private onClickListener mOnClickListener;
    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public FriendListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, int tyep) {
        mContext = context;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
        this.tyep = tyep;
    }

    @Override
    public FamilyCircleFollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FamilyCircleFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleFollowListViewHolder holder, int position) {
        NewFriendBean.DataBean entity = mFollowEntities.get(position);
        String headUrl = entity.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.shop_img);
        }else {
            Glide.with(mContext).load(R.mipmap.user_account_pictures).into(holder.shop_img);
        }
        if (!CommonUtil.isNull(entity.getVipType())&&Integer.parseInt(entity.getVipType())==1) {
            holder.name_type.setVisibility(View.VISIBLE);
        }else {
            holder.name_type.setVisibility(View.GONE);
        }
        holder.name.setText(entity.getName());
        if (tyep==0){
            holder.tv_index.setVisibility(View.VISIBLE);
            String anotherFirstLetter = "";
            String firstLetter = FirstLetterUtil.getFirstLetter(entity.getName()); // 获取拼音首字母并转成大写
            if (position>0) {
                anotherFirstLetter = FirstLetterUtil.getFirstLetter(mFollowEntities.get(position - 1).getName());
            }
            if (position == 0 || !anotherFirstLetter.equals(firstLetter)) {
                holder.tv_index.setVisibility(View.VISIBLE);
                holder.tv_index.setText(firstLetter);
            } else {
                holder.tv_index.setVisibility(View.GONE);
            }
        }else {
            holder.tv_index.setVisibility(View.GONE);
        }
        if (tyep==1){
            if (entity.getRelationType()==1){
                holder.attention.setText("关注");
                holder.attention.setVisibility(View.VISIBLE);
                holder.attention.setBackgroundResource(R.drawable.black11);
            }else if (entity.getRelationType()==2){
                holder.attention.setText("已关注");
                holder.attention.setBackgroundResource(R.drawable.gray11);
                holder.attention.setVisibility(View.VISIBLE);
            }else if (entity.getRelationType()==3){
                holder.attention.setText("已拉黑");
                holder.attention.setBackgroundResource(R.drawable.gray11);
                holder.attention.setVisibility(View.VISIBLE);
            }
        }else {
            if (entity.getRelationType()==1){
                holder.attention.setText("关注");
                holder.attention.setVisibility(View.VISIBLE);
                holder.attention.setBackgroundResource(R.drawable.black11);
            }else {
                holder.attention.setVisibility(View.GONE);
            }
        }

        holder.attention.setOnClickListener(v -> mOnClickListener.onClick(position));

        holder.shop_img.setOnClickListener(v -> {
            String urlVip = "";
            if (entity.getVipType().equals("0")) {
                //非VIP
                urlVip = "http://app2.yuejianchina.com/yuejian-app/personal_center/userHome3.html";
            } else {
                //VIP
                urlVip = "http://app2.yuejianchina.com/yuejian-app/personal_center/personHome2.html";
            }
            urlVip = String.format(urlVip + "?customerId=%s&opCustomerId=%s", AppConfig.CustomerId, entity.getCustomerId());
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra(Constants.URL, urlVip);
            intent.putExtra("No_Title", true);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mFollowEntities == null) return 0;
        return mFollowEntities.size();
    }

    public void refresh(List<NewFriendBean.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
        this.mFollowEntities = followEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<NewFriendBean.DataBean> followEntities) {
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
        TextView name,attention,tv_index;



        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            shop_img = (CircleImageView) itemView.findViewById(R.id.shop_img);
            name = (TextView) itemView.findViewById(R.id.name);
            name_type = (ImageView) itemView.findViewById(R.id.name_type);
            attention = (TextView) itemView.findViewById(R.id.attention);
            tv_index = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }
}
