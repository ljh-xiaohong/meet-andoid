package com.yuejian.meet.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.utils.CommonUtil;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NormalHolder> {
    private final Activity context;
    private LayoutInflater mInflater;
    private List<FamilyFollowEntity.DataBean.CommentBean> mData;
    private int what;
    public CommentAdapter(Activity context, List<FamilyFollowEntity.DataBean.CommentBean> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.context=context;
        this.what=what;
    }
    private OnChange onChange;
    //接口回调
    public interface OnChange{
        public void click(View view, int postion);
    }
    public void setChange(OnChange onChange) {
        this.onChange = onChange;
    }
    View view;
    @Override
    public NormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.activity_comment_item, parent, false);
        NormalHolder viewHolder = new NormalHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NormalHolder holder, final int position) {
        FamilyFollowEntity.DataBean.CommentBean listBean=mData.get(position);
        holder.name.setText(listBean.getUserName()+"：");
        if (!CommonUtil.isNull(listBean.getOpName())){
            holder.reply_name.setText(listBean.getOpName());
            holder.reply.setVisibility(View.VISIBLE);
            holder.reply_name.setVisibility(View.VISIBLE);
        }else {
            holder.reply.setVisibility(View.GONE);
            holder.reply_name.setVisibility(View.GONE);
        }
        holder.content.setText(listBean.getArticleCommentContent());
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    class NormalHolder extends RecyclerView.ViewHolder
    {
        private TextView name,content,reply,reply_name;
        public NormalHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            reply = (TextView) view.findViewById(R.id.reply);
            reply_name = (TextView) view.findViewById(R.id.reply_name);
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
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