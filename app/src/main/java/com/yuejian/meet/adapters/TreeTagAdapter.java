package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity2;
import com.yuejian.meet.activities.FamilyTree.TreeTagActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.BotInfoActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.FriendTreeTagEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TimeUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 星级族谱adpter
 */

public class TreeTagAdapter extends FKAdapter<FriendTreeTagEntity> {
    private AdapterHolder mHelper;


    public TreeTagAdapter(AbsListView view, List<FriendTreeTagEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, FriendTreeTagEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final FriendTreeTagEntity item, final int position){
        this.mHelper=helper;
        helper.setText(R.id.txt_name,item.getFriend_name());
        helper.getView(R.id.txt_name).setSelected(item.isSelect);
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCxt instanceof TreeTagActivity)
                    ((TreeTagActivity)mCxt).setSel(item);
                else if (mCxt instanceof AddRelationActivity){
                    ((AddRelationActivity)mCxt).setSel(item);
                } else if (mCxt instanceof AddRelationActivity2){
                    ((AddRelationActivity2)mCxt).setSel(item);
                }
            }
        });
    }
}
