package com.yuejian.meet.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.Gift;

import java.util.List;

public class GiftDetailAdapter extends BaseAdapter {
    private final AbsListView abslistview;
    private final Context context;
    private final List<Gift> giftBeanList;

    public GiftDetailAdapter(AbsListView paramAbsListView, Context paramContext, List<Gift> paramList) {
        this.context = paramContext;
        this.giftBeanList = paramList;
        this.abslistview = paramAbsListView;
    }

    public int getCount() {
        return this.giftBeanList.size();
    }

    public Gift getItem(int paramInt) {
        return this.giftBeanList.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder holder;
        if (paramView == null) {
            holder = new ViewHolder();
            paramView = View.inflate(this.context, R.layout.item_gift_image, null);
            int i = (int) (AppConfig.width * 0.7D) / 5;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, i);
            holder.mIvImage = ((ImageView) paramView.findViewById(R.id.gift_iv_image));
            holder.mIvImage.setLayoutParams(layoutParams);
            holder.mTvPrice = ((TextView) paramView.findViewById(R.id.gift_tv_price));
            holder.mTvName = ((TextView) paramView.findViewById(R.id.gift_tv_name));
            holder.gift_ll_container = ((LinearLayout) paramView.findViewById(R.id.gift_item_layotu));
            paramView.setTag(holder);
        } else {
            holder = (ViewHolder) paramView.getTag();
        }
        Gift gift = this.giftBeanList.get(paramInt);
        holder.gift_ll_container.setSelected(gift.isSelected);
        holder.mTvPrice.setText(String.valueOf(gift.gift_price + "金币"));
        holder.mTvName.setText(gift.gift_name);
        Glide.with(this.context).load(gift.gift_image).into(holder.mIvImage);
        return paramView;

    }

    class ViewHolder {
        public LinearLayout gift_ll_container;
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvPrice;
    }
}
