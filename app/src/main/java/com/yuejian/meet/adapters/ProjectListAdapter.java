package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.yuejian.meet.bean.ProjectBean;
import com.yuejian.meet.bean.VideoAndArticleBean;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder> {

    private List<ProjectBean.DataBean> mFollowEntities;
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
    public ProjectListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, Activity activity) {
        mContext = context;
        mActivity =activity;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.precise_push_content_type1_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProjectBean.DataBean entity = mFollowEntities.get(position);
        if (!TextUtils.isEmpty(entity.getPhoto())) {
            Glide.with(mContext).load(entity.getPhoto()).into(holder.iv_icon);
        }
        if (!TextUtils.isEmpty(entity.getCoverUrl())) {
            Glide.with(mContext).load(entity.getCoverUrl()).into(holder.msg_img);
        }
        holder.title.setText(entity.getTitle());
        if (entity.getVipType()==0){
            holder.vip_img.setVisibility(View.GONE);
        }else {
            holder.vip_img.setVisibility(View.VISIBLE);
        }
        holder.name.setText(entity.getUserName());
        holder.content.setText(entity.getContent());
    }

    @Override
    public int getItemCount() {
        if (mFollowEntities == null) return 0;
        return mFollowEntities.size();
    }

    public void refresh(List<ProjectBean.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
        this.mFollowEntities = followEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<ProjectBean.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
            notifyDataSetChanged();
    }

    public interface OnFollowListItemClickListener {
        void onListItemClick(int type, int id);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView iv_icon;
        ImageView vip_img,msg_img;
        TextView title,content,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            vip_img = itemView.findViewById(R.id.vip_img);
            msg_img = itemView.findViewById(R.id.msg_img);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
        }
    }
}
