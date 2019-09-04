package com.yuejian.meet.utils;

import android.content.Context;
import android.view.WindowManager;


/**
 * Created by Jue on 2016/3/29.
 * 常用小工具类
 */
public class EasyUtil {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


}
