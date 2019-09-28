package com.yuejian.meet.framents.message;

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
import com.yuejian.meet.activities.message.NewFriendActivity;
import com.yuejian.meet.adapters.FriendListAdapter;
import com.yuejian.meet.adapters.NewFriendAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * @author : ljh
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class FansFragment extends Fragment implements FriendListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.list)
    RecyclerView fansList;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    private FriendListAdapter mFansAdapter;

    public FansFragment() {
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
    List<NewFriendBean.DataBean> mList = new ArrayList<>();

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        if (getArguments().getInt("type") == 0) {
            params.put("type", 0);
        } else if (getArguments().getInt("type") == 1) {
            params.put("type", 1);
        } else {
            params.put("type", 2);
        }
        apiImp.getRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mList.clear();
                NewFriendBean bean = new Gson().fromJson(data, NewFriendBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                mList.addAll(bean.getData());
                if (mList.size() > 0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                mFansAdapter.refresh(mList);
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

    private void initView() {
        boolean isNew;
        if (getArguments().getInt("type") == 0) {
            isNew = true;
        } else {
            isNew = false;
        }
        mFansAdapter = new FriendListAdapter(getActivity(), this, apiImp,false);
        fansList.setAdapter(mFansAdapter);
        mFansAdapter.setOnClickListener(new FriendListAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                //关注
                getAttention(position);
            }
        });
        fansList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    private void getAttention(int position) {
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onListItemClick(int type, int id) {

    }
}
