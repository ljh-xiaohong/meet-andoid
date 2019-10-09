package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.video.common.utils.FastClickUtil;
import com.bumptech.glide.Glide;
import com.yuejian.meet.bean.PosterModelEntity;
import com.yuejian.meet.bean.RecommendPosterEntity;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐 海报
 */
public class PosterListAdapter extends BaseAdapter<PosterListAdapter.ViewHolder, RecommendPosterEntity> {

    private OnItemClickListener listener;

    public PosterListAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new RecommendView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;
        rv.setViewStatus(RecommendView.ViewType.POSTER, itemHeight);


        RecommendPosterEntity RecommendEntity = data.get(position);
        Glide.with(context).load(RecommendEntity.getPreviewUrl()).into(rv.poster_img);
        rv.poster_content.setText(RecommendEntity.getPostersTitle());
//        rv.poster_tag.setText(RecommendEntity.getPostersTitle());

//        rv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onItemClick(view, position);
//            }
//        });

    }


    @Override
    public void refresh(List<RecommendPosterEntity> recommendPosterEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data = recommendPosterEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<RecommendPosterEntity> recommendPosterEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(recommendPosterEntities);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return super.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if(listener!=null)listener.onItemClick(view,getAdapterPosition());
            });

        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
