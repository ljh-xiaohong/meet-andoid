package com.yuejian.meet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class ScreenUtils {
    public static int getNavigationBarHeight(Context paramContext) {
        int i = paramContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (i > 0) {
            i = paramContext.getResources().getDimensionPixelSize(i);
            return i;
        }
        return 0;
    }

    public static int getScreenHeight(Context paramContext) {
        return ((WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public static int getScreenWidth(Context paramContext) {
        return ((WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static int getStatusBarHeight(Context paramContext) {
        int i = 0;
        int j = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (j > 0) {
            i = paramContext.getResources().getDimensionPixelSize(j);
        }
        return i;
    }

    public static void setScreenAlpha(Activity paramActivity, float paramFloat) {
        WindowManager.LayoutParams localLayoutParams = paramActivity.getWindow().getAttributes();
        localLayoutParams.alpha = paramFloat;
        paramActivity.getWindow().addFlags(2);
        paramActivity.getWindow().setAttributes(localLayoutParams);
    }
}
