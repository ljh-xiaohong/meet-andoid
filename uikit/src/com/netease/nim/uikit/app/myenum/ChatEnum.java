package com.netease.nim.uikit.app.myenum;

/**
 * Created by zh03 on 2017/11/28.
 */

public enum ChatEnum {
    /**
     * 商店
     */
    shop(2),
    /**
     * 默认正常家族群聊
     */
    defaults(1),
    /**
     * 创建群
     */
    FoundGroup(3),
    CLANGROUP(4);

    final private int value;
    ChatEnum(int valus){
        this.value=valus;
    }
    public int getValue() {
        return value;
    }
}
