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

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.MyMessageCommentDialogActivity;
import com.yuejian.meet.activities.mine.MyDialogActivity;
import com.yuejian.meet.adapters.CommtentZanAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.bean.MessageZanBean;
import com.yuejian.meet.utils.ViewInject;

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
public class CommentZanFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;

    public CommentZanFragment() {
    }

    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        setParam();
    }

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            //加载数据
            initData();
        }
    }

    public ApiImp apiImp = new ApiImp();
    List<MessageCommentBean.DataBean> mCommentMapBeansList = new ArrayList<>();
    List<MessageZanBean.DataBean> mPraiseMapBeansList = new ArrayList<>();

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", "500103");
        params.put("type",1);
        apiImp.getCommentPraiseList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (getArguments().getInt("type")==1){
                    MessageCommentBean bean = new Gson().fromJson(data, MessageCommentBean.class);
                    if (bean.getCode() != 0) {
                        ViewInject.shortToast(getActivity(), bean.getMessage());
                        return;
                    }
                    mCommentMapBeansList.clear();
                    mCommentMapBeansList.addAll(bean.getData());
                    if (mCommentMapBeansList.size() > 0) {
                        llFamilyFollowListEmpty.setVisibility(View.GONE);
                    } else {
                        llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                    }
                }else {
                    MessageZanBean bean = new Gson().fromJson(data, MessageZanBean.class);
                    if (bean.getCode() != 0) {
                        ViewInject.shortToast(getActivity(), bean.getMessage());
                        return;
                    }
                    mPraiseMapBeansList.clear();
                    mPraiseMapBeansList.addAll(bean.getData());
                    if (mPraiseMapBeansList.size() > 0) {
                        llFamilyFollowListEmpty.setVisibility(View.GONE);
                    } else {
                        llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }

    private View view;// 需要返回的布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.fans_fragment, null);
            isInit = true;
            setParam();
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private CommtentZanAdapter adapter;
    private void initView() {
        adapter = new CommtentZanAdapter(getActivity(),getArguments().getInt("type"),mCommentMapBeansList,mPraiseMapBeansList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new CommtentZanAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getActivity(), MyMessageCommentDialogActivity.class);
                intent.putExtra("crId",mCommentMapBeansList.get(position).getType());
                intent.putExtra("replyCommentId",mCommentMapBeansList.get(position).getReplyCommentId());
                startActivityForResult(intent,1);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
