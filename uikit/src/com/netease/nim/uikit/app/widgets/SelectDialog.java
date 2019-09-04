package com.netease.nim.uikit.app.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by zh03 on 2017/12/28.
 */

public class SelectDialog extends AlertDialog {
    protected SelectDialog(@NonNull Context context) {
        super(context);
    }

    public SelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
