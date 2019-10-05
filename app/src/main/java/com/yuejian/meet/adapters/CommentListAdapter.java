package com.yuejian.meet.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.utils.Log;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.MyDialogActivity;
import com.yuejian.meet.bean.CommentBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.NormalHolder> {
    private final Activity context;
    private LayoutInflater mInflater;
    private List<CommentBean.DataBean> mData;
    private MyDialogActivity.StyleType styleType;

    public CommentListAdapter(Activity context, List<CommentBean.DataBean> data, MyDialogActivity.StyleType styleType) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.styleType = styleType;
        this.context = context;
    }

    public void refreshData(List<CommentBean.DataBean> dataBeans) {
        if (dataBeans == null) return;
        if (mData == null) mData = new ArrayList<>();
        this.mData.clear();
        this.mData.addAll(dataBeans);
        this.notifyDataSetChanged();
    }

    public void loadMoreData(List<CommentBean.DataBean> dataBeans) {
        if (dataBeans == null) return;
        if (mData == null) mData = new ArrayList<>();
        this.mData.addAll(dataBeans);
        this.notifyDataSetChanged();
    }

    public void setData(List<CommentBean.DataBean> dataBeans) {
        this.mData = dataBeans;
    }

    public List<CommentBean.DataBean> getData() {
        return this.mData;
    }

    private OnChange onChange;

    //接口回调
    public interface OnChange {
        public void click(int postion);
    }

    public void setChange(OnChange onChange) {
        this.onChange = onChange;
    }

    View view;

    @Override
    public NormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (this.styleType) {
            case NORMAL:
                view = mInflater.inflate(R.layout.activity_comment_list_item, parent, false);
                break;

            case BLACK:
                view = mInflater.inflate(R.layout.activity_comment_list_item_two, parent, false);
                break;

            default:
                view = mInflater.inflate(R.layout.activity_comment_list_item, parent, false);
                break;
        }

        NormalHolder viewHolder = new NormalHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NormalHolder holder, final int position) {
        CommentBean.DataBean listBean = mData.get(position);
        holder.name.setText(listBean.getName());
        holder.content.setText(listBean.getComment());
        if (!CommonUtil.isNull(listBean.getReplyCommentId()) && Integer.parseInt(listBean.getReplyCommentId()) != 0) {
            holder.reply.setVisibility(View.VISIBLE);
            holder.reply_name.setVisibility(View.VISIBLE);
            holder.reply_name.setText(listBean.getOpName());
        } else {
            holder.reply.setVisibility(View.GONE);
            holder.reply_name.setVisibility(View.GONE);
            holder.reply_name.setText("");
        }
        Date date = new Date(listBean.getCreateTime() * 1000);
        String createdTime = TimeUtils.formatDateTime(date);
        holder.time.setText(createdTime);
        String headUrl = listBean.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(context).load(headUrl).into(holder.head_img);
        }
        holder.head_img.setOnClickListener(v -> Log.e("asdasd", "跳转个人主页"));
        holder.content.setOnClickListener(v -> onChange.click(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class NormalHolder extends RecyclerView.ViewHolder {
        private CircleImageView head_img;
        private TextView name, content, reply, reply_name, time;

        public NormalHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            reply = (TextView) view.findViewById(R.id.reply);
            reply_name = (TextView) view.findViewById(R.id.reply_name);
            time = (TextView) view.findViewById(R.id.time);
            head_img = (CircleImageView) view.findViewById(R.id.head_img);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

}