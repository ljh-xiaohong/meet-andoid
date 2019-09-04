package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Reward;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/28.
 */

public class RewardListActivity extends BaseActivity implements SpringView.OnFreshListener {

    @Bind(R.id.reward_list)
    ListView listView;
    @Bind(R.id.spring_view)
    SpringView springView;

    private List<Reward> rewards = new ArrayList<>();
    private RewordAdapter adapter = null;
    private int pageIndex = 1;
    private String articleId = "";
    private String customerId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_list);
        articleId = getIntent().getStringExtra("articleId");
        customerId = getIntent().getStringExtra("customer_id");
        setTitle("打赏详情");
        adapter = new RewordAdapter();
        listView.setAdapter(adapter);
        springView.setListener(this);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        getRewardList(articleId);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reward reward = rewards.get(position);
                AppUitls.goToPersonHome(mContext, reward.customer_id);
            }
        });
    }

    private void getRewardList(String articleId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_id", articleId);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(16));
        apiImp.getRewardList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Reward> temp = JSON.parseArray(data, Reward.class);
                if (pageIndex == 1) {
                    rewards.clear();
                }
                if (temp == null || temp.isEmpty()) {
                    pageIndex--;
                } else {
                    rewards.addAll(temp);
                }
                adapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getRewardList(articleId);
    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        getRewardList(articleId);
    }

    private class RewordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rewards.size();
        }

        @Override
        public Object getItem(int position) {
            return rewards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getBaseContext(), R.layout.item_reward, null);
            TextView name = (TextView) convertView.findViewById(R.id.reward_name);
            TextView createTime = (TextView) convertView.findViewById(R.id.reward_create_time);
            ImageView photo = (ImageView) convertView.findViewById(R.id.reward_photo);
            TextView gift_name = (TextView) convertView.findViewById(R.id.gift_name);
            Reward r = rewards.get(position);
            Glide.with(getBaseContext()).load(r.photo).into(photo);
            name.setText(r.surname + r.name);
            boolean isShow = true;
            if (StringUtils.isNotEmpty(r.cnt)) {
                if ("0".equals(r.cnt) || !StringUtils.isNumber(r.cnt)) {
                    isShow = false;
                }
            } else {
                isShow = false;
            }
            convertView.findViewById(R.id.gift_cnt_layout).setVisibility(isShow ? View.VISIBLE : View.GONE);
            TextView cntTextView = (TextView) convertView.findViewById(R.id.gift_cnt);

            if (AppConfig.CustomerId.equals(customerId) && StringUtils.isNotEmpty(r.gift_name)) {
                gift_name.setVisibility(View.VISIBLE);
                gift_name.setText(r.gift_name);
                convertView.findViewById(R.id.shang).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.shang).setVisibility(View.VISIBLE);
                gift_name.setVisibility(View.GONE);
            }

            cntTextView.setText(" x " + r.cnt);
            createTime.setText(StringUtils.friendlyTime(r.create_time));
            return convertView;
        }
    }
}
