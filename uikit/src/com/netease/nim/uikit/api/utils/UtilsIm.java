package com.netease.nim.uikit.api.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.MonyEntity;

/**
 * Created by zh03 on 2017/7/10.
 */

public class UtilsIm {
    //获取通话时长
    public static Long getTimeLong(String moneySum,Long price){
        Long timeLong = null;
        Long moneyToInt=(new Double(moneySum)).longValue();
        Long ss=(moneyToInt/price);
//        Toast.makeText(AppConfig.context,ss+"分钟",Toast.LENGTH_SHORT).show();
        timeLong=Long.parseLong(((ss*60*1000)-(10*1000)-(AppConfig.mAvchattime*1000))+"");
        return timeLong;
    }
    //设置钱并保存
    public static void setMyMoney(Context context, MonyEntity entity){
        AppConfig.gainsSum=Double.parseDouble(entity.getGains_bal());
        AppConfig.rechargeSum=Double.parseDouble(entity.getRecharge_bal());
        AppConfig.moneySun=AppConfig.gainsSum+AppConfig.rechargeSum;
        PreferencesIm.put(context,PreferencesIm.moneySum,AppConfig.moneySun+"");
    }
}
