/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.aliyun.demo.recorder.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.aliyun.apsaravideo.music.utils.NotchScreenUtil;
import com.aliyun.demo.R;

public class BaseChooser extends DialogFragment {

    private DialogVisibleListener dismissListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();

        if (window != null) {
            //设置dialog动画
            window.getAttributes().windowAnimations = R.style.record_bottom_dialog_animation;
        }

    }

    @Override
    public void onResume() {

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        super.onResume();

        DisplayMetrics dpMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        WindowManager.LayoutParams p = window.getAttributes();

        p.width = dpMetrics.widthPixels;
        // 适配传音CF8手机
        p.height = dpMetrics.heightPixels - NotchScreenUtil.getTECNOCF8NotchAndNaviHeight();

        window.setAttributes(p);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        /*
            解决crash:java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
            原因:after onSaveInstanceState invoke commit,而 show 会触发 commit 操作
            fragment is added and its state has already been saved，
            Any operations that would change saved state should not be performed if this method returns true
         */
//        if (isStateSaved()) {
//            return ;
//        }
        super.show(manager, tag);
        if (dismissListener != null) {
            dismissListener.onDialogShow();
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (dismissListener != null) {
            dismissListener.onDialogShow();
        }
        return super.show(transaction, tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDialogDismiss();
        }
    }

    public void setDismissListener(DialogVisibleListener dismissListener) {
        this.dismissListener = dismissListener;
    }
}
