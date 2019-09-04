package com.netease.nim.uikit.app.myenum;

/**
 * 性别枚举
 * * Created by zh03 on 2017/7/14.
 */

public enum  SexEnum {
    woman(0),///女
    man(1);//男
    final private int value;
    SexEnum(int valus){
        this.value=valus;
    }
}
