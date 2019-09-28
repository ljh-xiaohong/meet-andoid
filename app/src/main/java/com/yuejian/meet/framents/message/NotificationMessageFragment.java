package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.CommentZanActivity;
import com.yuejian.meet.activities.message.NewFriendActivity;
import com.yuejian.meet.adapters.CustomerServiceAdapter;
import com.yuejian.meet.adapters.ProjectListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MessageBean;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DadanPreference;
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
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class NotificationMessageFragment extends BaseFragment implements SpringView.OnFreshListener {

    @Bind(R.id.new_firent)
    TextView newFirent;
    @Bind(R.id.comment_and_zan)
    TextView commentAndZan;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    private CustomerServiceAdapter adapter;
    public ApiImp apiImp = new ApiImp();
    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;
    private View view;


    private int mNextPageIndex = 1;
    private int pageCount = 10;
    List<MessageBean.DataBean> mList =new ArrayList<>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.notification_message_fragment, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        initView();
    }
    private void delect(int id) {
        Map<String, Object> params = new HashMap<>();
//        params.put("customerId", AppConfig.CustomerId);
        params.put("id",id);
        apiImp.getDelMessage(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MessageCommentBean bean = new Gson().fromJson(data, MessageCommentBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                onRefresh();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }
    public void initDatas() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("msgType", "4");
        params.put("pageIndex", mNextPageIndex);
        params.put("pageItemCount", pageCount);
        apiImp.getMessageList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
             MessageBean bean=new Gson().fromJson(data,MessageBean.class);
                if (bean.getCode()!=0) {
                    ViewInject.shortToast(getActivity(),bean.getMessage());
                    return;
                }
                mList.addAll(bean.getData());
                if (mList.size() > 0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                }else{
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                if (mNextPageIndex <= 1) {
                    //上拉最新
                    adapter.refresh(mList);
                } else {
                    //下拉更多
                    if (bean.getData().size()!=pageCount){
                        ViewInject.shortToast(getActivity(),"已经是最后一页");
                    }else {
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
            }
        });
    }


    private void initView() {
        adapter = new CustomerServiceAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new CustomerServiceAdapter.onClickListener() {
            @Override
            public void onDelect(int position) {
                delect(mList.get(position).getId());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newFirent.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewFriendActivity.class)));
        commentAndZan.setOnClickListener(v -> startActivity(new Intent(getActivity(), CommentZanActivity.class)));
        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);
//        mSpringView.callFresh();
    }

    @Override
    public void onRefresh() {
        mList.clear();
        mNextPageIndex = 1;
        initDatas();
    }

    @Override
    public void onLoadmore() {
         ++mNextPageIndex;
        initDatas();
    }
    public void update() {
        mList.clear();
        mNextPageIndex = 1;
        initDatas();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
