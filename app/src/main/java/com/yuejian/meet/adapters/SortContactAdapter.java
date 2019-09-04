package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.BindFamilyActivity;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.bean.SurnameEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 姓氏adpter
 */

public class SortContactAdapter extends FKAdapter<Contact> {
    private AdapterHolder mHelper;
    List<Contact> list;


    public SortContactAdapter(AbsListView view, List<Contact> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.list=mDatas;
    }
    public void setListDate(List<Contact> mDatas){
        this.list=mDatas;
    }

    public void convert(AdapterHolder helper, Contact item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final Contact item, final int position) {
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
        mHelper.setText(R.id.member_age," "+item.getAge());
        mHelper.getView(R.id.member_age).setSelected(item.getSex().equals("0")?false:true);
        mHelper.setText(R.id.text_member_name,item.getSurname()+item.getName());
        helper.getView(R.id.member_check_box).setSelected(item.getIs_exist()==1?true:false);
        mHelper.getView(R.id.member_faqr).setVisibility(View.GONE);
        if (item.getIs_family_master()>0){
            mHelper.getView(R.id.member_faqr).setVisibility(View.VISIBLE);
        }
        ((CheckBox)helper.getView(R.id.member_check_box)).setChecked(item.getSelect());
        Glide.with(mCxt.getApplicationContext()).load(item.getPhoto()).override(Utils.dp2px(mCxt, 33),Utils.dp2px(mCxt, 33)).into((ImageView) mHelper.getView(R.id.img_member_icon));
        mHelper.getView(R.id.contact_all_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_exist()==0){
                    item.setSelect(!item.getSelect());
                    ((CheckBox)helper.getView(R.id.member_check_box)).setChecked(item.getSelect());
                    if(mCxt instanceof SelectContactActivity){
                        ((SelectContactActivity)mCxt).isSelectContact(item);
                    } else if(mCxt instanceof BindFamilyActivity) {
                        ((BindFamilyActivity)mCxt).isSelectContact(item);
                    }
                }
            }
        });
        mHelper.getView(R.id.member_check_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_exist()==0){
                    item.setSelect(!item.getSelect());
                    ((CheckBox)helper.getView(R.id.member_check_box)).setChecked(item.getSelect());
                    if(mCxt instanceof SelectContactActivity){
                        ((SelectContactActivity)mCxt).isSelectContact(item);
                    } else if(mCxt instanceof BindFamilyActivity) {
                        ((BindFamilyActivity)mCxt).isSelectContact(item);
                    }
                }else {
                    ((CheckBox)helper.getView(R.id.member_check_box)).setChecked(false);
                }
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
