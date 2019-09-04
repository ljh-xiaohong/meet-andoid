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
import com.yuejian.meet.bean.NewsActionEntity;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 姓氏adpter
 */

public class NewActionAdapter extends FKAdapter<String> {
    private AdapterHolder mHelper;


    public NewActionAdapter(AbsListView view, List<String> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, String item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, String item, final int position){
        this.mHelper=helper;
        List<NewsActionEntity> listData= JSON.parseArray(item,NewsActionEntity.class);
        final NewsActionEntity entity=listData.get(0);
        helper.setText(R.id.item_new_action_time, TimeUtils.getNewActionTime(entity.getCreate_time()));
        helper.setText(R.id.item_new_action_title,entity.getTitle());
        Glide.with(mCxt).load(entity.getPic_url()).into((ImageView) helper.getView(R.id.item_new_acton_photo));

        helper.getView(R.id.item_new_acton_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt, WebActivity.class);
                intent.putExtra(Constants.URL,entity.getUrl());
//                intent.putExtra("No_Title",true);
                mCxt.startActivity(intent);
            }
        });
        AbsListView listView=(AbsListView) helper.getView(R.id.item_new_action_list);
        if (listData.size()>1){
            List<NewsActionEntity> list=new ArrayList<>();
            for (int i=1;i<listData.size();i++){
                list.add(listData.get(i));
            }
            NewActionSonAdapter mAdapter=new NewActionSonAdapter(listView,list,R.layout.item_new_action_son_layout);
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.new_action_bottom).setVisibility(View.GONE);
        if ((getCount()-1)==position){
            helper.getView(R.id.new_action_bottom).setVisibility(View.VISIBLE);
        }
    }
    public class NewActionSonAdapter extends FKAdapter<NewsActionEntity>{

        public NewActionSonAdapter(AbsListView view, List<NewsActionEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        @Override
        public void convert(AdapterHolder helper, final NewsActionEntity item, boolean isScrolling, int position)  {
            super.convert(helper, item, isScrolling,position);
            helper.setText(R.id.item_new_action_son_title,item.getTitle());
            Glide.with(mCxt).load(item.getPic_url()).into((ImageView) helper.getView(R.id.item_new_action_son_photo));
            helper.getView(R.id.item_new_action_son_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mCxt, WebActivity.class);
                    intent.putExtra(Constants.URL,item.getUrl());
//                    intent.putExtra("No_Title",true);
                    mCxt.startActivity(intent);
                }
            });
        }
    }
}
