package com.yuejian.meet.adapters;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.FriendBlacklistActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.AddressListEntity;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 黑名单adpter
 */

public class FriendBlackListAdapter extends FKAdapter<AddressListEntity> {
    private AdapterHolder mHelper;
    List<AddressListEntity> list;


    public FriendBlackListAdapter(AbsListView view, List<AddressListEntity> mDatas, int itemLayoutId) {
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
        helper.setText(R.id.item_name,item.getFullname());
        helper.setText(R.id.item_role,item.getSex()==0?"她":"他"+"是我的："+item.getRole());
        helper.getView(R.id.item_txt_age).setSelected(item.getSex()==1);
        helper.setText(R.id.item_txt_age," "+item.getAge());
        Glide.with(mCxt).load(item.getPhoto()).into((ImageView) helper.getView(R.id.item_photo));
        helper.getView(R.id.txt_del).setSelected(true);
        helper.getView(R.id.txt_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FriendBlacklistActivity)mCxt).postBalck(item.getOp_customer_id());
            }
        });
    }
}
