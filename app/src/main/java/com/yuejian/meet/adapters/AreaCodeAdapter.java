package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.activities.mine.AreaCodeActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.AreaCodeEntity;
import com.yuejian.meet.bean.Contact;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 国家地区adpter
 */

public class AreaCodeAdapter extends FKAdapter<AreaCodeEntity> {
    private AdapterHolder mHelper;
    List<AreaCodeEntity> list;


    public AreaCodeAdapter(AbsListView view, List<AreaCodeEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.list=mDatas;
    }
    public void setListDate(List<AreaCodeEntity> mDatas){
        this.list=mDatas;
    }

    public void convert(AdapterHolder helper, AreaCodeEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final AreaCodeEntity item, final int position) {
        this.mHelper = helper;
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.getView(R.id.area_letter).setVisibility(View.VISIBLE);
            helper.setText(R.id.area_letter,item.getSortLetters());
            helper.getView(R.id.itemt_area_wire).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.itemt_area_wire).setVisibility(View.VISIBLE);
            helper.getView(R.id.area_letter).setVisibility(View.GONE);
        }
        mHelper.setText(R.id.item_nation_name,item.getNation());
        mHelper.setText(R.id.item_area_code,"+"+item.getNationCode());
        mHelper.getView(R.id.item_nation_area_code_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("nationCode",item.getNationCode());
                intent.putExtra("nationAreaName",item.getNation());
                ((AreaCodeActivity)mCxt).setResult(((AreaCodeActivity) mCxt).RESULT_OK,intent);
                ((AreaCodeActivity) mCxt).finish();
            }
        });
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    private int selectCount = 0;
}
