package com.yuejian.meet.utils;

import android.util.Log;

/**
 * Created by zh03 on 2017/6/9.
 */

public class FCLoger {
    public static void debug(String msg){
        Log.i("clssname====", msg);
    }
    public static void error(String msg){
        Log.e("error-------", ""+msg );
    }
}
