package com.yuejian.meet.dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * <b>创建时间</b> 2016/4/21 <br>
 *
 * @author zhouwenjun
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public boolean isShowing = false;
    protected View fragmentRootView;
    //获取权限会添加到这个map中
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();

    protected abstract View inflaterView(LayoutInflater inflater,
                                         ViewGroup container, Bundle bundle);

    /**
     * 初始化组建
     *
     * @param parentView
     */
    protected void initWidget(View parentView) {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 绑定事件
     */
    protected void bindEvent() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fragmentRootView = inflaterView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, fragmentRootView);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(true);
        initData();
        initWidget(fragmentRootView);
        bindEvent();
        return fragmentRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if (!this.isAdded() && !this.isVisible() && !this.isRemoving()) {
                super.show(manager, tag);
                isShowing = true;
            }
        } catch (Exception ignore) {

        }
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
        isShowing = false;
    }

    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    protected void requestPermission(int id, String[] permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
//            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            //弹出对话框接收权限
            ActivityCompat.requestPermissions(getActivity(), permission, id);
            return;
//            } else {
//                allowableRunnable.run();
//            }
        } else {
            allowableRunnable.run();
        }
    }

    protected boolean hasPermission(String permission) {

        if (canMakeSmores()) {
            return (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            allowRun.run();
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            disallowRun.run();
        }
    }


    /**
     * 检查系统版本
     *
     * @return
     */
    private boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }
}
