package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentEntity;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 评论列表适配器
 */
public class CommentListViewAdapter extends RecyclerView.Adapter<CommentListViewAdapter.CommentListViewHolder> {

    private List<CommentEntity> mCommentEntityList;
    private Context mContext;
    private OnClick mClick;
    //接口回调
    public interface OnClick{
        void click(View view, int id, int postion);

        void click(View v, int position);
    }
    public void setClick(OnClick mClick) {
        this.mClick = mClick;
    }
    public CommentListViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_dialog_list, parent, false);
        return new CommentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListViewHolder holder, int position) {
        CommentEntity entity = mCommentEntityList.get(position);
        String headUrl = entity.photo;
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.nameTextView.setText(String.format("%s", entity.name));
        String createdTime = TimeUtils.formatDateTime(new Date(entity.article_comment_time));
        holder.createdTimeView.setText(createdTime);
        holder.contentTextView.setText(entity.article_comment_content);
        holder.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mClick.click(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCommentEntityList == null) return 0;
        return mCommentEntityList.size();
    }
    public void refresh(List<CommentEntity> commentEntities) {
        if (this.mCommentEntityList == null) {
            this.mCommentEntityList = new ArrayList<>();
        }
        this.mCommentEntityList = commentEntities;
        notifyDataSetChanged();
    }

    class CommentListViewHolder extends RecyclerView.ViewHolder {

        TextView contentTextView;
        TextView nameTextView;
        TextView createdTimeView;
        CircleImageView headImageView;

        public CommentListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_comment_list_item_head);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_comment_list_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_comment_list_item_name);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_comment_list_item_time);
        }
    }
}
