package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.BotInfoActivity;
import com.yuejian.meet.activities.home.EnjoyActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.EnjoyEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TimeUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 宗享会项目adpter
 */

public class BotActityAdapter extends FKAdapter<BotEntity> {
    private AdapterHolder mHelper;


    public BotActityAdapter(AbsListView view, List<BotEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, BotEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final BotEntity item, final int position){
        this.mHelper=helper;
        helper.getView(R.id.item_bottom).setVisibility(position==(mDatas.size()-1)?View.VISIBLE:View.GONE);
        Glide.with(mCxt).load(item.getPhoto()).error(R.mipmap.ic_default).into((ImageView) helper.getView(R.id.item_bot_photo));
        Glide.with(mCxt).load(item.getLogo()).error(R.mipmap.ic_default).into((ImageView) helper.getView(R.id.item_bot_logo));
        helper.setText(R.id.bot_name,item.getTitle());
        helper.setText(R.id.bot_job,item.getIndustry());
        helper.setText(R.id.itme_user_name,item.getSurname()+item.getName());
        helper.setText(R.id.item_bot_problems, item.getFacing_problems());
        helper.setText(R.id.itme_bot_time,TimeUtils.getBugTimeTwo(Long.parseLong(item.getGmt_create())));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(AppConfig.CustomerId)){
                    mCxt.startActivity(new Intent(mCxt, LoginActivity.class));
                }else {
                    Intent intent= new Intent(mCxt,BotInfoActivity.class);
                    intent.putExtra("id",item.getId());
                    intent.putExtra("customer_id",item.getCustomer_id());
                    ((BaseActivity)mCxt).startActivityIfNeeded(intent,100);
                }
            }
        });
    }
}
