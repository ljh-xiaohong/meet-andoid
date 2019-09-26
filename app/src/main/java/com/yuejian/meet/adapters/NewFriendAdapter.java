package com.yuejian.meet.adapters;

/**
 * @author : ljh
 * @time : 2019/9/8 12:55
 * @desc :
 */
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
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.List;


public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<NewFriendBean.DataBean> list;
    private boolean isNew;
    private onClickListener mOnClickListener;
    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public NewFriendAdapter(Context context, List<NewFriendBean.DataBean> list, boolean isNew) {
        this.context = context;
        this.list = list;
        this.isNew = isNew;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.new_friend_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NewFriendBean.DataBean dataBean= list.get(position);
        if (!TextUtils.isEmpty(dataBean.getPhoto())) {
            Glide.with(context).load(dataBean.getPhoto()).into(holder.iv_icon);
        }
        if (!TextUtils.isEmpty(dataBean.getName())) {
            holder.name.setText(dataBean.getName());
        }else {
            holder.name.setText("");
        }
        if (!CommonUtil.isNull(dataBean.getVipType())&&Integer.parseInt(dataBean.getVipType())==1) {
            holder.vip_img.setVisibility(View.VISIBLE);
        }else {
            holder.vip_img.setVisibility(View.GONE);
        }
        if (dataBean.getRelationType()==1){
            holder.attention.setText("关注");
            holder.attention.setBackgroundResource(R.drawable.black11);
        }else {
            holder.attention.setText("已关注");
            holder.attention.setBackgroundResource(R.drawable.gray11);
        }
        holder.attention.setOnClickListener(v -> mOnClickListener.onClick(position));
        if (isNew){
            holder.attention.setVisibility(View.VISIBLE);
        }else {
            holder.attention.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_icon;
        ImageView vip_img;
        TextView name,attention;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            vip_img = itemView.findViewById(R.id.vip_img);
            name = itemView.findViewById(R.id.name);
            attention = itemView.findViewById(R.id.attention);
        }
    }
}

