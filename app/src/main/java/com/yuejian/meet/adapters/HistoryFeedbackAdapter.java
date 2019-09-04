package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.HistoryFeedbackActivity;
import com.yuejian.meet.activities.mine.UserHistoryFeedbackInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.bean.UserFeedbackEntity;
import com.yuejian.meet.utils.TimeUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 反馈列表adpter
 */

public class HistoryFeedbackAdapter extends FKAdapter<UserFeedbackEntity> {
    private AdapterHolder mHelper;
    int status=0;


    public HistoryFeedbackAdapter(AbsListView view, List<UserFeedbackEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void setStatus(int status){
        this.status=status;
    }
    public void convert(AdapterHolder helper, UserFeedbackEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final UserFeedbackEntity item, final int position){
        this.mHelper=helper;
        helper.setText(R.id.item_bug_content,item.getFeedback_question());
        helper.setText(R.id.item_bug_date,mCxt.getString(R.string.user_feedback_bug6)+"   "+ TimeUtils.getBugTime(item.getFeedback_create_time()));
        helper.getView(R.id.item_bug_type_name).setSelected(true);
        helper.getView(R.id.feedback_solve).setVisibility(status==0?View.GONE:View.VISIBLE);
        //1:反馈BUG, 2:功能建议, 3:吐槽, 4:其他
        helper.setText(R.id.item_bug_type_name,item.getFeedback_type().equals("1")?mCxt.getString(R.string.user_feedback_bug1):item.getFeedback_type().equals("2")?mCxt.getString(R.string.user_feedback_bug2):item.getFeedback_type().equals("3")?mCxt.getString(R.string.user_feedback_bug3):mCxt.getString(R.string.user_feedback_bug4));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt,UserHistoryFeedbackInfoActivity.class);
                intent.putExtra("id",item.getFeedback_id());
                intent.putExtra("status",item.getFeedback_status());
                if (mCxt instanceof HistoryFeedbackActivity)
                    ((HistoryFeedbackActivity)mCxt).startActivityIfNeeded(intent,23);
            }
        });
    }
}
