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
import com.yuejian.meet.adapters.FriendListAdapter;
import com.yuejian.meet.adapters.PrecisePushAdapter;
import com.yuejian.meet.adapters.PrecisePushCommodityAdapter;
import com.yuejian.meet.adapters.PrecisePushContentAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.PushCommodityBean;
import com.yuejian.meet.bean.PushListBean;
import com.yuejian.meet.bean.PushProjectBean;
import com.yuejian.meet.bean.PushUseBean;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.HeadAndFootRecyclerView;

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
public class PrecisePushFragment extends Fragment implements FriendListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    @Bind(R.id.precise_push_content_list)
    HeadAndFootRecyclerView precisePushContentList;
    @Bind(R.id.precise_push_list)
    RecyclerView precisePushList;
    @Bind(R.id.precise_push_commodity_list)
    RecyclerView precisePushCommodityList;

    public PrecisePushFragment() {
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
                initUserData();
                //加载数据
                initListData();
                //加载数据
                initListCommodityData();
        }
    }

    public ApiImp apiImp = new ApiImp();
    List<PushUseBean.DataBean> mList = new ArrayList<>();
    List<PushProjectBean.DataBean> list = new ArrayList<>();
    List<PushCommodityBean.DataBean> listCommodity = new ArrayList<>();
    //获取项目列表
    private void initListData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getPushProject(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                list.clear();
                PushProjectBean bean = new Gson().fromJson(data, PushProjectBean.class);
                if (bean.getCode() != 0&&bean.getCode()!=19986) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                list.addAll(bean.getData());
                if (list.size()>0){
                    precisePushContentList.setVisibility(View.VISIBLE);
                }else {
                    precisePushContentList.setVisibility(View.GONE);
                }
                if (list.size() > 0 || mList.size() > 0||listCommodity.size()>0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                mPrecisePushContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }
    //获取商品列表
    private void initListCommodityData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getThreeProject(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listCommodity.clear();
                PushCommodityBean bean = new Gson().fromJson(data, PushCommodityBean.class);
                if (bean.getCode() != 0&&bean.getCode()!=19986) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                listCommodity.addAll(bean.getData());
                if (listCommodity.size()>0){
                    precisePushCommodityList.setVisibility(View.VISIBLE);
                }else {
                    precisePushCommodityList.setVisibility(View.GONE);
                }
                if (list.size() > 0 || mList.size() > 0||listCommodity.size()>0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                mPrecisePushCommodityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }
    //获取用户列表
    private void initUserData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getPushList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mList.clear();
                PushUseBean bean = new Gson().fromJson(data, PushUseBean.class);
                if (bean.getCode() != 0&&bean.getCode()!=19986) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                mList.addAll(bean.getData());
                if (mList.size()>0){
                    precisePushList.setVisibility(View.VISIBLE);
                }else {
                    precisePushList.setVisibility(View.GONE);
                }
                if (list.size() > 0 || mList.size() > 0||listCommodity.size()>0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                mPrecisePushAdapter.notifyDataSetChanged();
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
            view = inflater.inflate(R.layout.precise_push_fragment, null);
            isInit = true;
            setParam();
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    PrecisePushAdapter mPrecisePushAdapter;
    PrecisePushContentAdapter mPrecisePushContentAdapter;
    PrecisePushCommodityAdapter mPrecisePushCommodityAdapter;

    private void initView() {
        precisePushList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mPrecisePushAdapter = new PrecisePushAdapter(getActivity(), mList);
        precisePushList.setAdapter(mPrecisePushAdapter);
        mPrecisePushAdapter.setOnClickListener(new PrecisePushAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                //关注
                getAttention(position);
            }
        });

        precisePushCommodityList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mPrecisePushCommodityAdapter = new PrecisePushCommodityAdapter(getActivity(),listCommodity);
        precisePushCommodityList.setAdapter(mPrecisePushCommodityAdapter);



        precisePushContentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPrecisePushContentAdapter = new PrecisePushContentAdapter(getActivity(),list);
        precisePushContentList.setAdapter(mPrecisePushContentAdapter);

//        precisePushContentList.setHasFixedSize(true);
        precisePushContentList.setNestedScrollingEnabled(false);
//
//        precisePushContentList.addHeaderView(precisePushList);
//        precisePushContentList.addFooterView(precisePushCommodityList);
    }

    private void getAttention(int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("opCustomerId", mList.get(position).getCustomerId());
        if (mList.get(position).getIsConcern().equals("0")) {
            map.put("type", "1");
        } else {
            map.put("type", "2");
        }
        apiImp.bindRelation(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                if (mList.get(position).getIsConcern().equals("0")) {
                    mList.get(position).setIsConcern("1");
                } else {
                    mList.get(position).setIsConcern("0");
                }
                mPrecisePushAdapter.notifyItemChanged(position);
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
