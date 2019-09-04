package com.yuejian.meet.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.SurnameOriginEntity;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.List;

/**
 * @author :
 * @time : 2018/11/19 17:53
 * @desc :
 * @version: V1.0
 * @update : 2018/11/19 17:53
 */

public class AddSurnameOriginAdapter extends BaseQuickAdapter<SurnameOriginEntity, BaseViewHolder> {

  private Context mContext;

  public AddSurnameOriginAdapter(Context context, @Nullable List<SurnameOriginEntity> data) {
    super(R.layout.item_add_surname_origin_act_rv, data);
    this.mContext = context;
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void convert(BaseViewHolder helper, SurnameOriginEntity item) {
    helper.setText(R.id.item_surname_rank_tv_name, item.getSurname() + item.getName())
            .setText(R.id.item_surname_rank_company, item.getJob())
            .addOnClickListener(R.id.item_surname_rank_iv_icon)
            .addOnClickListener(R.id.item_surname_rank_ll);

    Glide.with(mContext).load(item.getPhoto()).into((CircleImageView) helper.getView(R.id.item_surname_rank_iv_icon));

    ImageView imageView = (ImageView) helper.getView(R.id.item_surname_rank_iv_add);
    LinearLayout linearLayout = (LinearLayout) helper.getView(R.id.item_surname_rank_ll);
    TextView textView = (TextView) helper.getView(R.id.item_surname_rank_tv_meritnum);
    if (item.getIs_friend() == 1) {
      // 已经是好友
      imageView.setVisibility(View.GONE);
      textView.setText("已添加");
      textView.setTextColor(mContext.getResources().getColor(R.color.color_grey_999999));
      linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.add_bg_hui));

    } else {
      textView.setText("添加");
    }

  }


}
