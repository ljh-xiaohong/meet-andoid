package com.yuejian.meet.adapters;


import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.SuranameRankEntity;

import java.util.List;

/**
 * @author :
 * @time : 2018/11/17 11:45
 * @desc :
 * @version: V1.0
 * @update : 2018/11/17 11:45
 */

public class SurnameRankAdapter extends BaseQuickAdapter<SuranameRankEntity.SurnameListBean, BaseViewHolder> {

  private Context mContext;

  public SurnameRankAdapter(Context context, @Nullable List<SuranameRankEntity.SurnameListBean> data) {
    super(R.layout.item_surname_rank_act_rv_list, data);
    this.mContext = context;
  }

  @Override
  protected void convert(BaseViewHolder helper, SuranameRankEntity.SurnameListBean item) {
    helper.setText(R.id.item_surname_rank_tv_ranknum,item.getRank() + "")
    .setText(R.id.item_surname_rank_tv_name,item.getSurname())
    .setText(R.id.item_surname_rank_tv_Pname,item.getPinyin());


  }
}
