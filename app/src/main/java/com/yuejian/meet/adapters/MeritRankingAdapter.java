package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.MeritRankingEntity;

import java.util.List;

/**
 * @author :
 * @time : 2018/11/16 9:58
 * @desc :
 * @version: V1.0
 * @update : 2018/11/16 9:58
 */

public class MeritRankingAdapter extends BaseQuickAdapter<MeritRankingEntity, BaseViewHolder> {
  private Context mContext;

  public MeritRankingAdapter(Context context, @Nullable List<MeritRankingEntity> data) {
    super(R.layout.item_surname_rank_act_rv_my, data);
    this.mContext = context;

  }

  @Override
  protected void convert(BaseViewHolder helper, MeritRankingEntity item) {
    helper.setText(R.id.item_surname_rank_tv_name, item.getTv())
            .setText(R.id.item_surname_rank_tv_meritnum, item.getTv());


    Glide.with(mContext).load(item.getTv()).into((ImageView) (helper.getView(R.id.item_surname_rank_iv_icon)));
    TextView textView = (TextView) helper.getView(R.id.item_surname_rank_tv_ranknum);
    ImageView imageView = (ImageView) helper.getView(R.id.item_surname_rank_iv_medal);
    if (item.getTv().equals("1")) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.one_shouye));
    } else if (item.getTv().equals("2")) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.two_shouye));
    } else if (item.getTv().equals("3")) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.three_shouye));
    }else {
      imageView.setVisibility(View.GONE);
      textView.setText(item.getTv());
    }
  }
}
