package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.FriendListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.CommonUtil;
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

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * @author : g000gle
 * @time : 2019/5/23 09:42
 * @desc : 搜索 好友
 */
public class FriendFragment extends BaseFragment
        implements SpringView.OnFreshListener, FriendListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.rv_family_circle_follow_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout mEmptyList;
    private FriendListAdapter mFollowListAdapter;
    private int mNextPageIndex = 1;
    private int pageCount = 20;
    private boolean firstLoad = true;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFollowListAdapter = new FriendListAdapter(getActivity(), this, apiImp, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFollowListAdapter);
        mFollowListAdapter.setOnClickListener(new FriendListAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                if (!DadanPreference.getInstance(getActivity()).getBoolean("isLogin")) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                 //关注
                getAttention(position);
            }
        });
        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);
//        mSpringView.callFresh();
    }

    private void getAttention(int position) {
        if (mEmptyList==null)return;
        if (dialog != null&&!dialog.isShowing)
            dialog.show(getActivity().getFragmentManager(), "");
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("opCustomerId", followEntities.get(position).getCustomerId());
        if (followEntities.get(position).getRelationType()==1){
            map.put("type", "1");
        }else if (followEntities.get(position).getRelationType()==2){
            map.put("type", "2");
        }else if (followEntities.get(position).getRelationType()==3){
            map.put("type", "4");
        }

        apiImp.bindRelation(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                ResultBean loginBean=new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
//                //未关注
//                if (followEntities.get(position).getRelationType()==1){
//                    followEntities.get(position).setRelationType(2);
//                    //已关注
//                }else if (followEntities.get(position).getRelationType()==2){
//                    followEntities.get(position).setRelationType(1);
//                    //拉黑
//                }else if (followEntities.get(position).getRelationType()==3){
//                    followEntities.get(position).setRelationType(1);
//                }
//                mFollowListAdapter.notifyItemChanged(position);
                followEntities.clear();
                mNextPageIndex = 1;
                loadDataFromNet("0",title);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    //加载数据
    List<NewFriendBean.DataBean> followEntities =new ArrayList<>();
    NewFriendBean followEntitie;
    private void loadDataFromNet(String type,String title) {
        if (dialog==null||getActivity()==null){

        }else{
            if (dialog != null&&!dialog.isShowing)
                dialog.show(getActivity().getFragmentManager(), "");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("title", title);
        map.put("type", type);
        map.put("pageIndex", String.valueOf(mNextPageIndex));
        map.put("pageItemCount", String.valueOf(pageCount));
        apiImp.getDoSearch(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                isUdate=true;
                if (mEmptyList==null)return;
                if (dialog==null){

                }else{
                    dialog.dismiss();
                }
                 followEntitie=new Gson().fromJson(data,NewFriendBean.class);
                 if (followEntitie.getCode()!=0) {
                     ViewInject.shortToast(getActivity(),followEntitie.getMessage());
                     return;
                 }
                followEntities.addAll(followEntitie.getData());
                if (followEntities.size() > 0) {
                    mEmptyList.setVisibility(View.GONE);
                }else {
                    mEmptyList.setVisibility(View.VISIBLE);
                }
                if (mNextPageIndex <= 1) {
                    //上拉最新
                    mFollowListAdapter.refresh(followEntities);
                } else {
                    //下拉更多
                    if (followEntitie.getData().size()!=pageCount){
                        ViewInject.shortToast(getActivity(),"已经是最后一页");
                    }else {
                        mFollowListAdapter.Loadmore(followEntities);
                    }
                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (CommonUtil.isNull(title)) return;
        followEntities.clear();
        mNextPageIndex = 1;
        loadDataFromNet("0",title);
    }

    @Override
    public void onLoadmore() {
        if (CommonUtil.isNull(title)) return;
        ++mNextPageIndex;
        loadDataFromNet("0",title);
    }

    @Override
    public void onListItemClick(int type, int id) {
        //类型：1-随笔，2-文章，3-相册，4-视频
        Intent intent = null;
        if (type == 4) {
            intent = new Intent(getActivity(), VideoDetailsActivity.class);
        } else {
            intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        }
        intent.putExtra("id", id);
        startActivity(intent);
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
    private boolean isUdate=true;
    String title="";
    public void update(String titles) {
        if (isUdate) {
            title=titles;
            followEntities.clear();
            mNextPageIndex = 1;
            loadDataFromNet("0",titles);
        }
        isUdate=false;
    }
}
