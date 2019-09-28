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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.bean.MessageZanBean;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.Date;
import java.util.List;


public class CommtentZanAdapter extends RecyclerView.Adapter<CommtentZanAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private int type;
    private List<MessageCommentBean.DataBean> commentMapBeansList;
    private List<MessageZanBean.DataBean> praiseMapBeansList;

    private onClickListener mOnClickListener;
    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
        void onDelect(int position);
    }

    public CommtentZanAdapter(Context context, int type, List<MessageCommentBean.DataBean> commentMapBeansList, List<MessageZanBean.DataBean> praiseMapBeansList) {
        this.context = context;
        this.type = type;
        this.commentMapBeansList = commentMapBeansList;
        this.praiseMapBeansList = praiseMapBeansList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.comment_zan_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (type==2) {
            holder.swipeMenuLayout.setIos(false);//设置是否开启IOS阻塞式交互
            holder.swipeMenuLayout.setLeftSwipe(true);//true往左滑动,false为往右侧滑动
            holder.swipeMenuLayout.setSwipeEnable(true);//设置侧滑功能开关
            holder.btnDelete.setOnClickListener(v -> mOnClickListener.onDelect(position));
            MessageCommentBean.DataBean commentMapBean=commentMapBeansList.get(position);
            if (!TextUtils.isEmpty(commentMapBean.getMsgPhoto())) {
                Glide.with(context).load(commentMapBean.getMsgPhoto()).into(holder.iv_icon);
            }
            if (!TextUtils.isEmpty(commentMapBean.getPhoto())) {
                Glide.with(context).load(commentMapBean.getPhoto()).into(holder.content_img);
                holder.content_img.setVisibility(View.VISIBLE);
            }else {
                holder.content_img.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(commentMapBean.getUserName())) {
                holder.title.setText(commentMapBean.getUserName());
            }else {
                holder.title.setText("");
            }
            if (!TextUtils.isEmpty(commentMapBean.getArticleCommentContent())) {
                holder.comment_content.setText(commentMapBean.getArticleCommentContent());
            }else {
                holder.comment_content.setText("");
            }
            holder.zan_img.setVisibility(View.GONE);
            holder.comment_content.setVisibility(View.VISIBLE);
            Date date = new Date(commentMapBean.getCreateTime()*1000);
            String createdTime = TimeUtils.formatDateTime(date);
            holder.time.setText(createdTime);
            holder.rlItem.setOnClickListener(v -> mOnClickListener.onClick(position));
        }else {
            holder.swipeMenuLayout.setSwipeEnable(false);//设置侧滑功能开关
            MessageZanBean.DataBean commentMapBean=praiseMapBeansList.get(position);
            if (!TextUtils.isEmpty(commentMapBean.getPhoto())) {
                Glide.with(context).load(commentMapBean.getPhoto()).into(holder.iv_icon);
            }
            if (!TextUtils.isEmpty(commentMapBean.getPhotoAndVideoUrl())) {
                Glide.with(context).load(commentMapBean.getPhotoAndVideoUrl()).into(holder.content_img);
                holder.content_img.setVisibility(View.VISIBLE);
            }else {
                holder.content_img.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(commentMapBean.getUserName())) {
                    holder.title.setText(commentMapBean.getUserName()+commentMapBean.getTitle());
            }else {
                holder.title.setText("");
            }
            holder.zan_img.setVisibility(View.VISIBLE);
            holder.comment_content.setVisibility(View.GONE);
            Date date = new Date(commentMapBean.getCreateTime()*1000);
            String createdTime = TimeUtils.formatDateTime(date);
            holder.time.setText(createdTime);
        }
    }

    @Override
    public int getItemCount() {
        if (type==2){
            return commentMapBeansList.size();
        }else {
            return praiseMapBeansList.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout swipeMenuLayout;
        Button btnDelete;
        CircleImageView iv_icon;
        ImageView content_img,zan_img;
        RelativeLayout rlItem;
        TextView time,title,comment_content;
        public MyViewHolder(View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rlItem);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            zan_img = itemView.findViewById(R.id.zan_img);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            comment_content = itemView.findViewById(R.id.comment_content);
            content_img = itemView.findViewById(R.id.content_img);
            swipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

