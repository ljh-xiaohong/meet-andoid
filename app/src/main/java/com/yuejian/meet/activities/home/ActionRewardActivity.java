package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ActionRewardAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Reward;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 动态打赏详情
 */
public class ActionRewardActivity extends BaseActivity implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {

    @Bind(R.id.spring_view)
    SpringView springView;
    @Bind(R.id.action_reward_list)
    ListView listView;
    List<Reward> listData = new ArrayList<>();
    ActionRewardAdapter mAdapter;
    String actionId;
    int pageIndex = 1;
    private boolean isPraise = false;
    TextView subtitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_reward);
        actionId = getIntent().getStringExtra("actionId");
        isPraise = getIntent().getBooleanExtra("is_praise", false);
        initData();
    }

    public void initData() {
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        listView.setOnItemClickListener(this);
        mAdapter = new ActionRewardAdapter(listView, listData, R.layout.item_reward);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.isMySelfReword = AppConfig.CustomerId.equals(getIntent().getStringExtra("customer_id"));
        subtitle = (TextView) findViewById(R.id.subtitle);
        if (isPraise) {
            setTitleText("点赞详情");
            requestPraiseList(actionId);
            subtitle.setText("被以下用户点赞");
        } else {
            setTitleText("打赏详情");
            requestRewordList(actionId);
            subtitle.setText("被以下用户打赏");
        }
    }

    public void requestRewordList(String actionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("action_id", actionId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        apiImp.getActionReward(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) listData.clear();
                listData.addAll(JSON.parseArray(data, Reward.class));
                mAdapter.refresh(listData);
                pageIndex++;
                if (springView != null) springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) springView.onFinishFreshAndLoad();
            }
        });
    }

    public void requestPraiseList(String actionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("action_id", actionId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        apiImp.getActionPraiseList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) listData.clear();
                listData.addAll(JSON.parseArray(data, Reward.class));
                mAdapter.refresh(listData);
                pageIndex++;
                if (springView != null) springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) springView.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        if (isPraise) {
            requestPraiseList(actionId);
        } else {
            requestRewordList(actionId);
        }
    }

    @Override
    public void onLoadmore() {
        if (isPraise) {
            requestPraiseList(actionId);
        } else {
            requestRewordList(actionId);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppUitls.goToPersonHome(mContext, listData.get(position).getCustomer_id());
    }
}
