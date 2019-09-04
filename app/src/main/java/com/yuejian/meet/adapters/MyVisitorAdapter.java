package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.FamilyTree.AddressListActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.PersonHomeActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.AddressListEntity;
import com.yuejian.meet.bean.VisitListEntity;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 我的访客adpter
 */

public class MyVisitorAdapter extends FKAdapter<VisitListEntity> {
    private AdapterHolder mHelper;
    List<VisitListEntity> list;


    public MyVisitorAdapter(AbsListView view, List<VisitListEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.list=mDatas;
    }
    public void setListDate(List<VisitListEntity> mDatas){
        this.list=mDatas;
    }

    public void convert(AdapterHolder helper, VisitListEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final VisitListEntity item, final int position) {
        this.mHelper = helper;
        //根据position获取分类的首字母的char ascii值
        String section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.getView(R.id.contact_letter).setVisibility(View.VISIBLE);
            helper.setText(R.id.contact_letter,TimeUtils.getDateDay_s(item.getVisit_info_create_time()));
        } else {
            helper.getView(R.id.contact_letter).setVisibility(View.GONE);
        }
        helper.setText(R.id.item_name,item.getSurname()+item.getName());
        Glide.with(mCxt).load(item.getPhoto()).into((ImageView) helper.getView(R.id.item_photo));
        helper.getView(R.id.item_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCxt, PersonHomeActivity.class);
                intent.putExtra("customer_id", item.customer_id);
                mCxt.startActivity(intent);
            }
        });
        helper.setText(R.id.item_role,TimeUtils.getTimeHH(item.getVisit_info_create_time())+" 访问了您的主页");
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public String getSectionForPosition(int position) {
        return TimeUtils.getDateDay_s(mDatas.get(position).getVisit_info_create_time());
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = TimeUtils.getDateDay_s(mDatas.get(i).getVisit_info_create_time());
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (sortStr.equals(section)) {
                return i;
            }
        }
        return -1;
    }

    private int selectCount = 0;
}
