package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.ShopEntity;
import com.yuejian.meet.widgets.ShopView;

import java.util.List;

public class ShopAdapter extends BaseAdapter<ShopAdapter.ViewHolder, ShopEntity> {


    public ShopAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
    public void refresh(List<ShopEntity> shopEntities) {
        if (shopEntities == null) return;
        this.data = shopEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<ShopEntity> shopEntities) {
        if (shopEntities == null) return;
        this.data.addAll(shopEntities);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new ShopView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShopEntity entity = data.get(position);
        if (!(holder.itemView instanceof ShopView)) return;
        ShopView item = (ShopView) holder.itemView;
        ViewGroup.LayoutParams lp = item.img.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(itemWidth, itemWidth);
        } else {
            lp.width = itemWidth;
            lp.height = itemWidth;
        }
        item.img.setLayoutParams(lp);

        Glide.with(context).load(entity.getGPhoto()).into(item.img);
        item.tv_title.setText(entity.getGName());
        item.tv_discount.setText(String.format("%s", entity.getGPriceVip()));
        item.tv_fullPrice.setText(String.format("%s", entity.getGPriceOriginal()));
        item.click_img.setImageResource(entity.isClick() ? R.mipmap.icon_peizhi_content_sel : R.mipmap.icon_peizhi_content_nor);
        item.setTag(item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(itemView, getAdapterPosition());
                }
            });

        }
    }
}
