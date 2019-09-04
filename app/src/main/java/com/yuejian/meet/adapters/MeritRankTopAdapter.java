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
import com.yuejian.meet.bean.MeritRankTOPEntity;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.List;

/**
 * @author :
 * @time : 2018/11/17 16:46
 * @desc :
 * @version: V1.0
 * @update : 2018/11/17 16:46
 */

public class MeritRankTopAdapter extends BaseQuickAdapter<MeritRankTOPEntity, BaseViewHolder> {

  private Context mContext;

  public MeritRankTopAdapter(Context context, @Nullable List<MeritRankTOPEntity> data) {
    super(R.layout.item_surname_rank_act_rv_my, data);
    this.mContext = context;
  }

  @Override
  protected void convert(BaseViewHolder helper, MeritRankTOPEntity item) {
    helper.setText(R.id.item_surname_rank_tv_name, item.getName())
            .setText(R.id.item_surname_rank_tv_meritnum, item.getIntegral().substring(0,item.getIntegral().lastIndexOf(".")));

    Glide.with(mContext).load(item.getIcon()).asBitmap().placeholder(R.mipmap.ic_default)
            .into((CircleImageView) helper.getView(R.id.item_surname_rank_iv_icon));

    TextView textView = (TextView) helper.getView(R.id.item_surname_rank_tv_ranknum);
    ImageView imageView = (ImageView) helper.getView(R.id.item_surname_rank_iv_medal);

    if (item.getNo() == 1) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.one_shouye));
    } else if (item.getNo() == 2) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.two_shouye));
    } else if (item.getNo() == 3) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.three_shouye));
    } else {
      imageView.setVisibility(View.GONE);
      textView.setText(item.getNo() + "");
    }


  }
}
