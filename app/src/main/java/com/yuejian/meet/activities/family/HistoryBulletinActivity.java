package com.yuejian.meet.activities.family;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.HistoryToday;

import java.util.ArrayList;
import java.util.List;

public class HistoryBulletinActivity extends BaseActivity {
    private List<HistoryToday> dataSource = new ArrayList();

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_history_bulletin);
        setTitleText("历史快报");
        initBackButton(true);
        ListView listView = (ListView) findViewById(R.id.history_list);
        this.dataSource = JSON.parseArray(getIntent().getStringExtra("dataSource"), HistoryToday.class);
        listView.setAdapter(new ListAdapter());
    }

    public class ListAdapter
            extends BaseAdapter {
        public ListAdapter() {
        }

        public int getCount() {
            return HistoryBulletinActivity.this.dataSource.size();
        }

        public Object getItem(int paramInt) {
            return HistoryBulletinActivity.this.dataSource.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(HistoryBulletinActivity.this.getBaseContext(), R.layout.item_essay, null);
                holder.title = ((TextView) paramView.findViewById(R.id.essay_title));
                holder.createTime = ((TextView) paramView.findViewById(R.id.essay_create_time));
                holder.pic = ((ImageView) paramView.findViewById(R.id.essay_pic));
                holder.rootView = paramView;
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            HistoryToday historyToday = HistoryBulletinActivity.this.dataSource.get(paramInt);
            holder.createTime.setText(String.valueOf(historyToday.year + "-" + historyToday.month + "-" + historyToday.day));
            holder.title.setText(historyToday.title);
            Glide.with(HistoryBulletinActivity.this.getBaseContext()).load(historyToday.pic).into(holder.pic);
            return paramView;
        }

        private class ViewHolder {
            TextView createTime;
            ImageView pic;
            View rootView;
            TextView title;

            private ViewHolder() {
            }
        }
    }
}
