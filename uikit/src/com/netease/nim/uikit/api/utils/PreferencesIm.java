package com.netease.nim.uikit.api.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

/**
 * xml文件存
 * Created by zh03 on 2017/7/3.
 */

public class PreferencesIm {

    /** 存储的SharedPreferences名称 */
    public static final String SHARED_PREFERENCES_NAME = "folkcam_android_ismoney";
    public static final String isOpen="isOpen";
    public static final String isShapCharge="isShapCharge";//
    public static final String gift_image="gift_image";
    public static final String moneySum="moneySum";//金额总数
    public static final String giftDownload="giftDownload";//礼物是否下载本地key
    public static final String isFreeChat="isFreeChatMessage";/////是否提示过扣费
    public static final String freechatCntIm="freechatCntIm";/////剩余免费的次数



    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, String value) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 保存数据到文件
     *
     * @param context
     * @param map
     */
    public static void put(Context context, HashMap<String, String> map) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     *            默认值，如果当前获取不到数据就返回它
     * @return
     */
    public static String get(Context context, String key, String defValue) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 删除单个字段
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    /**
     * 删除全部字段
     *
     * @param context
     */
    public static void clear(Context context) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * 反射机制存储数据
     *  @param context
     * @param classz
     */
    public static void put(Context context, Object classz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            Field[] fields = Object.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String name = field.getName();
                if (!"CREATOR".equals(name)) {
                    editor.putString(name, (String) field.get(classz));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


    public static void write(Context context, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(k, v);
        editor.commit();
    }

    public static void write(Context context, String k,
                             boolean v) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(k, v);
        editor.commit();
    }

    public static void write(Context context, String k,
                             String v) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(k, v);
        editor.commit();
    }

    public static int readInt(Context context,  String k) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getInt(k, 0);
    }

    public static int readInt(Context context, String k,
                              int defv) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getInt(k, defv);
    }

    public static boolean readBoolean(Context context, String k) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getBoolean(k, false);
    }

    public static boolean readBoolean(Context context,
                                      String k, boolean defBool) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getBoolean(k, defBool);
    }

    public static String readString(Context context,  String k) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getString(k, null);
    }

    public static String readString(Context context,  String k,
                                    String defV) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return preference.getString(k, defV);
    }


    public static void clean(Context cxt, String fileName) {
        SharedPreferences preference = cxt.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.commit();
    }
}
