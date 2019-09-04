package com.yuejian.meet.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yuejian.meet.R;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author : ljh
 * @time : 2019/8/15 11:06
 * @desc :
 */
public class StatusBarUtils {

    /**
     * @param activity 上下文
     * @param statusBg 状态栏颜色
     * @param darkMode true：字体为黑  false：字体为白
     */
    public static void setYtfStatusBar(Activity activity, int statusBg, boolean darkMode) {

        final int gray = ContextCompat.getColor(activity, R.color.write);

        /**
         * 2019/2/21
         * style的windowTranslucentNavigation设置为false后，状态栏无法达到沉浸效果
         * 设置UI FLAG 让布局能占据状态栏的空间，达到沉浸效果
         */
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(option);

        if (isMiUIV7OrAbove()) {
            //设置状态栏文字黑色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
                if (darkMode) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                activity.getWindow().getDecorView().setSystemUiVisibility(vis);
                //设置状态栏颜色
                setStatusBarBackground_V21(activity, statusBg);
            } else {
                //浅背景黑字
                MIUISetStatusBarLightMode(activity, darkMode);
                setStatusBarBackground_V19(activity, statusBg);
            }

            return;
        } else if (isMiUIV6OrAbove()) {
            //白背景黑字
            MIUISetStatusBarLightMode(activity, darkMode);
            setStatusBarBackground_V19(activity, statusBg);

            return;
        }

        if (isFlymeV4OrAbove()) {
            FlymeSetStatusBarLightMode(activity, darkMode);
            setStatusBarBackground_V19(activity, statusBg);

            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //api23+ 6.0以上的
            //设置状态栏文字黑色
            int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (darkMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(vis);
            //设置状态栏颜色
            setStatusBarBackground_V21(activity, statusBg);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //api21-23   5.0~6.0
            //字体不能变色，所以statusBar颜色改成灰的
            if (darkMode) {
                setStatusBarBackground_V21(activity, gray);
            } else {
                setStatusBarBackground_V21(activity, statusBg);
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //api19-21
            if (darkMode) {
                setStatusBarBackground_V19(activity, gray);
            } else {
                setStatusBarBackground_V19(activity, statusBg);
            }
        }

    }


    public static void setYtfLightStatusBar(Activity activity) {
        final int white = ContextCompat.getColor(activity, R.color.write);

        final int testColor = ContextCompat.getColor(activity, R.color.black3);

        setYtfStatusBar(activity, white, true);

//        if (isMiUIV7OrAbove()) {
//            //设置状态栏文字黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                //设置状态栏颜色
//                setStatusBarBackground_V21(activity, white);
//            } else {
//                //浅背景黑字
//                MIUISetStatusBarLightMode(activity, true);
//                setStatusBarBackground_V19(activity, white);
//            }
//
//            return;
//        } else if (isMiUIV6OrAbove()) {
//            //白背景黑字
//            MIUISetStatusBarLightMode(activity, true);
//            setStatusBarBackground_V19(activity, white);
//
//            return;
//        }
//
//        if (isFlymeV4OrAbove()) {
//            FlymeSetStatusBarLightMode(activity, true);
//            setStatusBarBackground_V19(activity, white);
//
//            return;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //api23+ 6.0以上的
//            //设置状态栏文字黑色
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            //设置状态栏颜色
//            setStatusBarBackground_V21(activity, white);
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //api21-23   5.0~6.0
//            //字体不能变色，所以statusBar颜色改成灰的
//            setStatusBarBackground_V21(activity, gray);
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //api19-21
//            setStatusBarBackground_V19(activity, gray);
//        }
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarBackground_V21(Activity activity, int color) {
        Window window = activity.getWindow();
        //首先清除默认的FLAG_TRANSLUCENT_STATUS
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private static void setStatusBarBackground_V19(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);

        decorViewGroup.addView(statusBarView);

        //设置标题栏下移
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    private static boolean isMiUIV6OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 4;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
