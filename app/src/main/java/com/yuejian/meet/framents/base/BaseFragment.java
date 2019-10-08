package com.yuejian.meet.framents.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.umeng.analytics.MobclickAgent;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View fragmentRootView;
    protected Context mContext;
    protected boolean mIsPrepared = false;
    public boolean mIsFirstResume = true;
    protected boolean mIsFirstVisible = true;
    protected boolean mIsFirstInvisible = true;
    public UserEntity user;
    public LoadingDialogFragment dialog;
    public ApiImp apiImp = new ApiImp();
    public static final String PAGE_ITEM_COUNT = String.valueOf(10);
    protected boolean isInitView = false;
    protected WeakReference<BaseFragment> reference;

    protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle);

    /**
     * 初始化组建
     *
     * @param parentView
     */
    protected void initWidget(View parentView) {

    }

    protected boolean checkIsLife() {
        return reference == null || reference.get() == null;
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 组建单击事件
     */
    protected void widgetClick(View v) {
    }

    protected void bindEvent() {

    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    protected <T extends View> T bindView(int id) {
        return (T) fragmentRootView.findViewById(id);
    }

    protected <T extends View> T bindView(int id, boolean click) {
        T view = (T) fragmentRootView.findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = new WeakReference(this);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentRootView = inflaterView(inflater, container, savedInstanceState);
        mContext = getActivity();
        user = AppConfig.userEntity;
        ButterKnife.bind(this, fragmentRootView);
        isInitView = true;
        initWidget(fragmentRootView);
        bindEvent();
        initData();
        Bus.getDefault().register(this);
        MobclickAgent.openActivityDurationTrack(false);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        return fragmentRootView;

    }

    public View getRootView() {
        return fragmentRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkIsLife()) return;
    }

    protected void initListener() {
    }


    @Override
    public void onResume() {
        super.onResume();
        user = AppConfig.userEntity;
        if (mIsFirstResume) {
            mIsFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(getContext());
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        user = AppConfig.userEntity;
        if (isVisibleToUser) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (mIsFirstInvisible) {
                mIsFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }

    }

    public synchronized void initPrepare() {
//        if (mIsPrepared) {
        onFirstUserVisible();
//        } else {
//            mIsPrepared = true;
//        }
    }

    /**
     * 第一次对用户可见时会调用该方法
     */

    protected void onFirstUserVisible() {
    }

    ;

    /**
     * 对用户可见时会调用该方法，除了第一次
     */
    public void onUserVisible() {
//        user = MyApplication.mUserEntity;
    }

    /**
     * 第一次对用户不可见时会调用该方法
     */
    public void onFirstUserInvisible() {
    }

    /**
     * 对用户不可见时会调用该方法，除了第一次
     */
    public void onUserInvisible() {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkHttpUtils.getInstance().cancelTag(this);
        Bus.getDefault().unregister(this);
        isInitView = false;
        reference = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference = null;
    }

    public void onSelectSure(Map<String, String> params) {
    }


    public Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        return fragment;
    }

    /**
     * Bus回调
     *
     * @param event
     */
    @BusReceiver
    public void onSomeEvent(BusCallEntity event) {
        if (checkIsLife())
            return;
        if (event.getCallType() == BusEnum.LOGIN_UPDATE || event.getCallType() == BusEnum.LOGOUT) {
            user = AppConfig.userEntity;
        } else if (event.getCallType() == BusEnum.Language) {
            getActivity().recreate();
        }
        // 这里处理事件
        onBusCallback(event);
    }

    public void onBusCallback(BusCallEntity event) {

    }

    @BusReceiver
    public void onStringEvent(String event) {
        receiverBus(event);
    }

    public void receiverBus(String event) {
    }

    public void onWindowFocusChanged(boolean hasChanged) {

    }

    public void getPosition(DataIdCallback<PositionInfo> callback) {
        apiImp.getPosition(mContext, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PositionInfo info = JSON.parseObject(data, PositionInfo.class);
                AppConfig.city = info.getCity();
                AppConfig.province = info.getProvince();
                if (null != callback) callback.onSuccess(info, id);

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (null != callback) callback.onFailed(errCode, errMsg, id);
            }
        });
    }

    public boolean onBackPressed() {
        return false;
    }
}
