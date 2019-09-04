package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.UserFeedbackActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BugTypeEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * Bug选择adpter
 */

public class BugTypSelAdapter extends FKAdapter<BugTypeEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public BugTypSelAdapter(AbsListView view, List<BugTypeEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, BugTypeEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final BugTypeEntity item, final int position) {
        this.mHelper = helper;
        helper.setText(R.id.bug_type_name,item.getName());
        helper.getView(R.id.bug_type_name).setSelected(item.isSelect);
        helper.getView(R.id.bug_type_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserFeedbackActivity)mCxt).selBugType(item,position);
            }
        });
    }
}
