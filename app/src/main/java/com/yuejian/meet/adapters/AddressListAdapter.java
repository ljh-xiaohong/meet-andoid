package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.FamilyTree.AddressListActivity;
import com.yuejian.meet.activities.FamilyTree.TreeTagActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.PersonHomeActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.AddressListEntity;
import com.yuejian.meet.bean.BookJson;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 通讯录adpter
 */

public class AddressListAdapter extends FKAdapter<AddressListEntity> {
    private AdapterHolder mHelper;
    List<AddressListEntity> list;


    public AddressListAdapter(AbsListView view, List<AddressListEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.list=mDatas;
    }
    public void setListDate(List<AddressListEntity> mDatas){
        this.list=mDatas;
    }

    public void convert(AdapterHolder helper, AddressListEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final AddressListEntity item, final int position) {
        this.mHelper = helper;
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.getView(R.id.contact_letter).setVisibility(View.VISIBLE);
            helper.setText(R.id.contact_letter,item.getSortLetters());
        } else {
            helper.getView(R.id.contact_letter).setVisibility(View.GONE);
        }
        helper.setText(R.id.item_name,item.getFullname());
        helper.setText(R.id.item_role,(item.getSex()==0?"她":"他")+"是我的："+item.getRole());
        helper.getView(R.id.item_txt_age).setSelected(item.getSex()==1);
        helper.setText(R.id.item_txt_age," "+item.getAge());
        Glide.with(mCxt).load(item.getPhoto()).into((ImageView) helper.getView(R.id.item_photo));
        helper.getView(R.id.item_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt, AddRelationActivity.class);
                intent.putExtra("userName",item.getFullname());
                intent.putExtra("op_customer_id",item.getOp_customer_id());
                intent.putExtra("update_relation",true);
                ((BaseActivity)mCxt).startActivityForResult(intent,20);
            }
        });
        helper.getView(R.id.btnUnRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SwipeMenuLayout)helper.getView(R.id.my_swipe_menulayout)).quickClose();
                ((AddressListActivity)mCxt).postBalck(item.getOp_customer_id());
            }
        });
        helper.getView(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SwipeMenuLayout)helper.getView(R.id.my_swipe_menulayout)).quickClose();
                ((AddressListActivity)mCxt).delFriend(item.getOp_customer_id());
            }
        });

        helper.getView(R.id.item_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCxt, PersonHomeActivity.class);
                intent.putExtra("customer_id", item.customer_id);
                mCxt.startActivity(intent);
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
