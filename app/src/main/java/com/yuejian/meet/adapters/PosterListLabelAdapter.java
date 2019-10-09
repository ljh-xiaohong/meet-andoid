package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.video.common.utils.FastClickUtil;
import com.bumptech.glide.Glide;
import com.yuejian.meet.bean.PosterModelEntity;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

public class PosterListLabelAdapter extends BaseAdapter<PosterListLabelAdapter.ViewHolder, PosterModelEntity> {


    private OnItemClickListener listener;


    public PosterListLabelAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
   public void refresh(List<PosterModelEntity> postersModelLists) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data = postersModelLists;
        notifyDataSetChanged();
    }



    @Override
    public void Loadmore(List<PosterModelEntity> postersModelLists) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(postersModelLists);
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterListLabelAdapter.ViewHolder(new RecommendView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;
        rv.setViewStatus(RecommendView.ViewType.POSTER, itemHeight);

        PosterModelEntity postersModelEntity = data.get(position);
        if (null != postersModelEntity) {
            Glide.with(context).load(postersModelEntity.getPreviewUrl()).into(rv.poster_img);
            rv.poster_content.setText(postersModelEntity.getPostersTitle());
//            rv.poster_tag.setText(entity.getPostersTitle());
//            rv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onItemClick(view, position);
//                }
//            });
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (listener!=null)listener.onItemClick(view,getAdapterPosition());

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
