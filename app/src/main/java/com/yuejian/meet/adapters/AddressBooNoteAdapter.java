package com.yuejian.meet.adapters;

import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.BindFamilyActivity;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BookJson;
import com.yuejian.meet.bean.Contact;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 通讯录adpter
 */

public class AddressBooNoteAdapter extends FKAdapter<BookJson> {
    private AdapterHolder mHelper;
    List<BookJson> list;


    public AddressBooNoteAdapter(AbsListView view, List<BookJson> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.list=mDatas;
    }
    public void setListDate(List<BookJson> mDatas){
        this.list=mDatas;
    }

    public void convert(AdapterHolder helper, BookJson item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final BookJson item, final int position) {
        this.mHelper = helper;
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.getView(R.id.contact_letter).setVisibility(View.VISIBLE);
            helper.setText(R.id.contact_letter,item.getSortLetters());
            helper.getView(R.id.itemt_wire).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.itemt_wire).setVisibility(View.VISIBLE);
            helper.getView(R.id.contact_letter).setVisibility(View.GONE);
        }
        helper.setText(R.id.text_member_phone,item.getMobile());
        helper.setText(R.id.text_member_name,item.getByname());
        final CheckBox checkBox=helper.getView(R.id.member_check_box);
        checkBox.setChecked(item.getSelect());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setSelect(checkBox.isChecked());
            }
        });
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setSelect(!item.getSelect());
                checkBox.setChecked(item.getSelect());
            }
        });
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mDatas.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    private int selectCount = 0;
}
