package com.yuejian.meet.framents.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yuejian.meet.R;
import com.yuejian.meet.adapters.PrecisePushAdapter;
import com.yuejian.meet.adapters.PrecisePushContentAdapter;
import com.yuejian.meet.adapters.ServiceAdapter;
import com.yuejian.meet.widgets.SecretaryTitleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : ljh
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class HundredSecretariesFragment extends Fragment implements SecretaryTitleView.OnTitleViewClickListener{
    SecretaryTitleView mSecretaryTitleView;
    RecyclerView precisePushList;
    @Bind(R.id.precise_push_content_list)
    RecyclerView precisePushContentList;
    @Bind(R.id.service_list)
    RecyclerView serviceList;
    private PrecisePushAdapter mPrecisePushAdapter;
    private PrecisePushContentAdapter mPrecisePushContentAdapter;
    private ServiceAdapter mServiceAdapter;


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
        }
    }

    private View view;// 需要返回的布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.hundred_secretaries_fragment, null);
            isInit = true;
            setParam();
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    View header;
    private void initView() {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.myheadview, null);
        precisePushList=header.findViewById(R.id.precise_push_list);
        mSecretaryTitleView=header.findViewById(R.id.family_circle_title_view);
        mSecretaryTitleView.setOnTitleViewClickListener(this);
        mSecretaryTitleView.setSelectedTitle(0);
        precisePushContentList.setVisibility(View.GONE);
        precisePushList.setVisibility(View.GONE);
        serviceList.setVisibility(View.VISIBLE);

        precisePushList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mPrecisePushAdapter = new PrecisePushAdapter(getActivity());
        precisePushList.setAdapter(mPrecisePushAdapter);


        precisePushContentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPrecisePushContentAdapter = new PrecisePushContentAdapter(getActivity());
        precisePushContentList.setAdapter(mPrecisePushContentAdapter);

        serviceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mServiceAdapter = new ServiceAdapter(getActivity());
        serviceList.setAdapter(mServiceAdapter);
        mServiceAdapter.setHeaderView(header);
    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                mSecretaryTitleView.setSelectedTitle(position);
                precisePushContentList.setVisibility(View.GONE);
                precisePushList.setVisibility(View.GONE);
                serviceList.setVisibility(View.VISIBLE);
                mServiceAdapter.setHeaderView(header);
                break;
            case 1:
                mSecretaryTitleView.setSelectedTitle(position);
                precisePushContentList.setVisibility(View.VISIBLE);
                precisePushList.setVisibility(View.VISIBLE);
                serviceList.setVisibility(View.GONE);
                mPrecisePushContentAdapter.setHeaderView(header);
                break;
            default:
                break;
        }
    }
}
