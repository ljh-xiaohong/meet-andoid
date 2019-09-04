package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.TreeAddActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.BotInfoActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/13.
 * 好友申请处理adpter
 */

public class TreeAddAdapter1 extends FKAdapter<FindMemberEntity> {
    private AdapterHolder mHelper;


    public TreeAddAdapter1(AbsListView view, List<FindMemberEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, FindMemberEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final FindMemberEntity item, final int position){
        this.mHelper=helper;
        Glide.with(mCxt).load(item.getPhoto()).into((ImageView) helper.getView(R.id.item_phone));
        helper.setText(R.id.item_user_name,(item.getSurname()==null?"":item.getSurname())+item.getName());
        helper.setText(R.id.company_name,item.getCompany_name()==null?"":item.getCompany_name());
        helper.setText(R.id.op_role,mCxt.getString(R.string.treeadd_name1)+item.getOp_role());
        helper.getView(R.id.status1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TreeAddActivity)mCxt).postData(item.getId(),"2");
            }
        });
        helper.getView(R.id.status2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TreeAddActivity)mCxt).postData(item.getId(),"1");
            }
        });
        helper.getView(R.id.item_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });
    }

}
