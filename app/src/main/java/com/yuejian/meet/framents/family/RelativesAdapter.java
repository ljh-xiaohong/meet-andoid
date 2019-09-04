package com.yuejian.meet.framents.family;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.bean.Relative;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.Utils;

import java.util.List;

public class RelativesAdapter extends RecyclerView.Adapter<RelativesAdapter.ViewHolder> {
    private Context context = null;
    private List<Relative> dataSource;

    public RelativesAdapter(Context paramContext, List<Relative> paramList) {
        this.context = paramContext;
        this.dataSource = paramList;
    }

    public int getItemCount() {
        return this.dataSource.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        CardView cardView = new CardView(this.context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setElevation(Utils.dp2px(this.context, 2.0F));
        }
        cardView.setRadius(Utils.dp2px(this.context, 25.0F));
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-2, -2);
        paramInt = Utils.dp2px(this.context, 10.0F);
        layoutParams.leftMargin = paramInt;
        layoutParams.rightMargin = paramInt;
        layoutParams.topMargin = Utils.dp2px(this.context, 2.0F);
        layoutParams.bottomMargin = Utils.dp2px(this.context, 2.0F);
        cardView.setLayoutParams(layoutParams);
        ImageView imageView = new ImageView(this.context);
        paramInt = Utils.dp2px(this.context, 50.0F);
        cardView.addView(imageView, new FrameLayout.LayoutParams(paramInt, paramInt));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Relative localRelative = this.dataSource.get(position);
        CardView localCardView = (CardView) holder.photo.getParent();
        if ("-2".equals(localRelative.id)) {
            Glide.with(this.context).load(AppConfig.userEntity.photo).into(holder.photo);
            localCardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(RelativesAdapter.this.context, WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + AppConfig.CustomerId);
                    RelativesAdapter.this.context.startActivity(intent);
                }
            });
        } else if ("-1".equals(localRelative.id)) {
            localCardView.setCardBackgroundColor(Color.parseColor("#D8D8D8"));
            Glide.with(this.context).load(R.mipmap.zengjia).into(holder.photo);
            localCardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("add_family_member");
                }
            });
        } else {
            Glide.with(this.context).load(localRelative.photo).into(holder.photo);
            localCardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("bind_relative_" + position);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo = null;
        public ViewHolder(View paramView) {
            super(paramView);
            this.photo = ((ImageView) ((CardView) paramView).getChildAt(0));
        }
    }
}
