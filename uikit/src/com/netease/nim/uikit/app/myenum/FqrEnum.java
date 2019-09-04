package com.netease.nim.uikit.app.myenum;

/**
 * Created by zh03 on 2018/1/23.
 */

public enum FqrEnum {
    /**
     * 全国
     */
    nationwide(1l),
    /**
     * 市
     */
    city(2l),
    /**
     * 省
     */
    province(3l);

    final private Long value;
    FqrEnum(Long valus){
        this.value=valus;
    }
    public Long getValue() {
        return value;
    }
}
