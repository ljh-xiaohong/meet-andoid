package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.UserFeedbackActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BugTypeEntity;
import com.yuejian.meet.bean.FeebackCommentEntity;
import com.yuejian.meet.utils.TimeUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * Bug回复选择adpter
 */

public class FeedbackCommentAdapter extends FKAdapter<FeebackCommentEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public FeedbackCommentAdapter(AbsListView view, List<FeebackCommentEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, FeebackCommentEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final FeebackCommentEntity item, final int position) {
        this.mHelper = helper;
        helper.setText(R.id.comment_conten,item.getComment_content());
        helper.setText(R.id.comment_time, TimeUtils.getBugTimeTwo(item.getComment_create_time()));
    }
}
