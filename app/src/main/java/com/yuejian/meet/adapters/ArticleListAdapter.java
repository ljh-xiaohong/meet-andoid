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

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.FamilyCircleFollowListViewHolder> {

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
    public ArticleListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, Activity activity) {
        mContext = context;
        mActivity =activity;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
    }

    @Override
    public FamilyCircleFollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_article, parent, false);
        return new FamilyCircleFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleFollowListViewHolder holder, int position) {
        FamilyFollowEntity.DataBean entity = mFollowEntities.get(position);
        String headUrl = entity.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.shop_img);
        }
        holder.title.setText(entity.getName());

        holder.zan_check_num.setText("99");
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

        ImageView shop_img;
        TextView title,zan_check_num;



        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            shop_img = (ImageView) itemView.findViewById(R.id.shop_img);
            title = (TextView) itemView.findViewById(R.id.title);
            zan_check_num = (TextView) itemView.findViewById(R.id.zan_check_num);
        }
    }
}
