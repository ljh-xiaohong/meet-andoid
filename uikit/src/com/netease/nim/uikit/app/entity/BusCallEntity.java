package com.netease.nim.uikit.app.entity;

import android.content.Intent;

import com.netease.nim.uikit.app.myenum.BusEnum;

/**
 * bug回调实体类
 * Created by zh03 on 2017/8/7.
 */
public class BusCallEntity {
    public BusEnum callType;//类型状态
    public Boolean isUsable;//是否可用
    public Intent intent;
    public String data;//数据
    public String id = "0";//数据id
    public String stateType;
    public String areaName;
    public Boolean selProvince=false;//是否是选择省份
    public Object object;

    public Boolean getSelProvince() {
        return selProvince;
    }

    public void setSelProvince(Boolean selProvince) {
        this.selProvince = selProvince;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public BusCallEntity() {
    }

    public BusCallEntity(BusEnum callType, Boolean isUsable, String data, String id) {
        this.callType = callType;
        this.isUsable = isUsable;
        this.data = data;
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUsable() {
        return isUsable;
    }

    public void setUsable(Boolean usable) {
        isUsable = usable;
    }

    public BusEnum getCallType() {
        return callType;
    }

    public void setCallType(BusEnum callType) {
        this.callType = callType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
