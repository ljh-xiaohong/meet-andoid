package com.yuejian.meet.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yuejian.meet.R;

public class BaseDialog extends DialogFragment {
    private DialogVisibleListener dismissListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();

        if (window != null) {
            //设置dialog动画
            window.getAttributes().windowAnimations = R.style.bottom_dialog_animation;
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        Dialog dialog = getDialog();
//        window.setGravity(Gravity.BOTTOM);
//        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        DisplayMetrics dpMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        WindowManager.LayoutParams p = window.getAttributes();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        p.dimAmount = 0.2f;
        p.gravity = Gravity.BOTTOM;
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        window.setAttributes(p);


        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true);
//            /*dpm获取的高度不准确，不要使用这种方式*/
////            dialog.getWindow().setLayout((int) (dpMetrics.widthPixels), (int) (dpMetrics.heightPixels));
//            // 适配传音CF8手机
//            if (Build.MODEL.toUpperCase().contains("TECNO CF8")) {
//
//                p.height = dpMetrics.heightPixels -getTECNOCF8NotchAndNaviHeight();
//                dialog.getWindow().setLayout((int) (dpMetrics.widthPixels), p.height);
//
//            }
        }


    }


//    public void show(FragmentManager manager, String tag) {
//
//        if (isStateSaved()) {
//            return;
//        }
//        super.show(manager, tag);
//        if (dismissListener != null) {
//            dismissListener.onDialogShow();
//        }
//    }
//
//
//    public int show(FragmentTransaction transaction, String tag) {
//        if (dismissListener != null) {
//            dismissListener.onDialogShow();
//        }
//        return super.show(transaction, tag);
//    }

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
