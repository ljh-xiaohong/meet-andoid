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
import com.yuejian.meet.adapters.CustomerServiceAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MessageBean;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : ljh
 * @time : 2019/12/16 16:47
 * @desc :
 */
public class NotificationActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    private CustomerServiceAdapter adapter;
    private int mNextPageIndex = 1;
    private int pageCount = 10;
    private boolean isUdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifi_activity);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    List<MessageBean.DataBean> mList = new ArrayList<>();

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
//        params.put("customerId", "725322");
        params.put("msgType", "4");
        params.put("pageIndex", mNextPageIndex);
        params.put("pageItemCount", pageCount);
        apiImp.getMessageList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                isUdate = true;
                if (llFamilyFollowListEmpty == null) return;
                MessageBean bean = new Gson().fromJson(data, MessageBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(NotificationActivity.this, bean.getMessage());
                    return;
                }
                mList.addAll(bean.getData());
                if (mList.size() > 0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                if (mNextPageIndex <= 1) {
                    //上拉最新
                    adapter.refresh(mList);
                } else {
                    //下拉更多
                    if (bean.getData().size() != pageCount) {
                        ViewInject.shortToast(NotificationActivity.this, "已经是最后一页");
                    } else {
                        adapter.Loadmore(mList);
                    }
                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
                isUdate = true;
            }
        });
    }

    private void delect(int id) {
        Map<String, Object> params = new HashMap<>();
//        params.put("customerId", AppConfig.CustomerId);
        params.put("id", id);
        apiImp.getDelMessage(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MessageCommentBean bean = new Gson().fromJson(data, MessageCommentBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(NotificationActivity.this, bean.getMessage());
                    return;
                }
                onRefresh();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(NotificationActivity.this, errMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        mList.clear();
        mNextPageIndex = 1;
        initData();
    }

    @Override
    public void onLoadmore() {
        ++mNextPageIndex;
        initData();
    }

    private void initView() {
        title.setText("通知");
        back.setOnClickListener(v -> finish());
        adapter = new CustomerServiceAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new CustomerServiceAdapter.onClickListener() {
            @Override
            public void onDelect(int position) {
                delect(mList.get(position).getId());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSpringView.setFooter(new DefaultFooter(this));
        mSpringView.setHeader(new DefaultHeader(this));
        mSpringView.setListener(this);
//        mSpringView.callFresh();
    }
}
