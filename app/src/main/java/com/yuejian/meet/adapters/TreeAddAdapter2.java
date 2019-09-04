package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.FamilyTree.TreeAddActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.utils.AppUitls;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 好友申请处理adpter
 */

public class TreeAddAdapter2 extends FKAdapter<FindMemberEntity> {
    private AdapterHolder mHelper;


    public TreeAddAdapter2(AbsListView view, List<FindMemberEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, FindMemberEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final FindMemberEntity item, final int position){
        this.mHelper=helper;
        Glide.with(mCxt).load(item.getPhoto()).into((ImageView) helper.getView(R.id.item_photo));
        helper.setText(R.id.item_user_name,(item.getSurname()==null?"":item.getSurname())+item.getName());
        helper.setText(R.id.company_name,item.getCompany_name()==null?"":item.getCompany_name());
        helper.getView(R.id.add_tree).setSelected(true);
        helper.getView(R.id.add_tree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt, AddRelationActivity.class);
                intent.putExtra("userName",(item.getSurname()==null?"":item.getSurname())+item.getName());
                intent.putExtra("op_customer_id",item.getCustomer_id());
                mCxt.startActivity(intent);
            }
        });
        helper.getView(R.id.item_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });
    }

}
