package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.FriendListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewFriendActivity extends BaseActivity implements FriendListAdapter.OnFollowListItemClickListener{
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend_activity);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    List<NewFriendBean.DataBean> mList =new ArrayList<>();
    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getAttentionAndFriend(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mList.clear();
                NewFriendBean bean=new Gson().fromJson(data,NewFriendBean.class);
                if (bean.getCode()!=0) {
                    ViewInject.shortToast(NewFriendActivity.this,bean.getMessage());
                    return;
                }
                mList.addAll(bean.getData());
                if (mList.size() > 0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                }else{
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                adapter.refresh(mList);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(NewFriendActivity.this,errMsg);
            }
        });
    }

    private void initView() {
        title.setText("新朋友");
        back.setOnClickListener(v -> finish());
        adapter = new FriendListAdapter(this, this, apiImp, 2);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new FriendListAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", AppConfig.CustomerId);
                map.put("opCustomerId", mList.get(position).getCustomerId());
                map.put("type", "1");
                apiImp.bindRelation(map, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        ResultBean loginBean=new Gson().fromJson(data, ResultBean.class);
                        ViewInject.shortToast(getApplication(), loginBean.getMessage());
                        initData();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClick(int type, int id) {

    }
}
