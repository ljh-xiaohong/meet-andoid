package com.netease.nim.uikit.app.entity;

/**
 * 扣费返回结果实体类
 * Created by zh03 on 2017/7/8.
 */

public class    ExpenseEntity {
    public String freechatCnt;//": 6,
    public String cost;//": 0,
    public String bal;//": {"recharge_bal": 999976,"gains_bal": 0 }

    public String getFreechatCnt() {
        return freechatCnt;
    }

    public void setFreechatCnt(String freechatCnt) {
        this.freechatCnt = freechatCnt;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }
}
