package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.SurnameRankFmEntity;

import java.util.List;

/**
 * @author :
 * @time : 2018/11/19 14:56
 * @desc :
 * @version: V1.0
 * @update : 2018/11/19 14:56
 */

public class SurnameRankFmAdapter extends BaseQuickAdapter<SurnameRankFmEntity, BaseViewHolder> {
  private Context mContext;
  public SurnameRankFmAdapter(Context context,@Nullable List<SurnameRankFmEntity> data) {
    super(R.layout.item_surname_rank_act_rv, data);
    this.mContext = context;
  }

  @Override
  protected void convert(BaseViewHolder helper, SurnameRankFmEntity item) {
    helper.setText(R.id.item_surname_rank_tv_name,item.getSurname())
    .setText(R.id.item_surname_rank_tv_Pname,item.getPinyin())
    .setText(R.id.item_surname_rank_tv_meritnum,String.valueOf(item.getPractice_total()));

    TextView textView = (TextView) helper.getView(R.id.item_surname_rank_tv_ranknum);
    ImageView imageView = (ImageView) helper.getView(R.id.item_surname_rank_iv_medal);
    if (item.getRank() == 1) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.one_shouye));
    } else if (item.getRank() == 2) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.two_shouye));
    } else if (item.getRank() == 3) {
      textView.setVisibility(View.GONE);
      imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.three_shouye));
    } else {
      imageView.setVisibility(View.GONE);
      textView.setText(item.getRank() + "");
    }
  }
}
