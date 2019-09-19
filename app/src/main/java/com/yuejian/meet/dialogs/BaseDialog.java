package com.yuejian.meet.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

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
