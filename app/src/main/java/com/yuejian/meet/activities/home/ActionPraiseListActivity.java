package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.ActionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24/024.
 */

public class ActionPraiseListActivity extends BaseActivity {

    private ArrayList<ActionInfo.PraiseInfo> dataSource = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_praise_list);
        ListView listView = (ListView) findViewById(R.id.action_praise_list);
        ActionInfo info = (ActionInfo) getIntent().getSerializableExtra("actionInfo");
        dataSource.addAll(info.praise_list);
    }

    @Override
    public void onClick(View v) {

    }

    private class PraiseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getBaseContext(), R.layout.item_reward, null);
            ImageView photo = (ImageView) convertView.findViewById(R.id.reward_photo);
            TextView name = (TextView) convertView.findViewById(R.id.reward_name);
            Glide.with(getBaseContext()).load(dataSource.get(position).photo).placeholder(R.mipmap.ic_default).into(photo);
            return convertView;
        }
    }
}
