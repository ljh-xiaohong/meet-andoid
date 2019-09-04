package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.EnjoyEntity;
import com.yuejian.meet.bean.NewsActionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 宗享会adpter
 */

public class EnjoyActityAdapter extends FKAdapter<EnjoyEntity> {
    private AdapterHolder mHelper;


    public EnjoyActityAdapter(AbsListView view, List<EnjoyEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, EnjoyEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final EnjoyEntity item, final int position){
        this.mHelper=helper;
        helper.getView(R.id.item_top).setVisibility(position==0?View.VISIBLE:View.GONE);
        helper.getView(R.id.item_bottom).setVisibility(position==(mDatas.size()-1)?View.VISIBLE:View.GONE);
        helper.setText(R.id.item_enjoy_title,item.getTitle());
        Glide.with(mCxt).load(item.getPic_url()).into((ImageView) helper.getView(R.id.item_enjoy_photo));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mCxt,WebActivity.class);
                intent.putExtra(Constants.URL,item.getLink());
                mCxt.startActivity(intent);
            }
        });
    }
}
