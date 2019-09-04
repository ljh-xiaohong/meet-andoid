package com.netease.nim.uikit.app.entity;

/**
 * 礼物发送返回的实体类
 * Created by zh03 on 2017/7/18.
 */

public class ActionGiftEntity {
    public String count;//": 32,
    public String myBal;//": {"recharge_bal": 988361, "gains_bal": 748078.54}

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMyBal() {
        return myBal;
    }

    public void setMyBal(String myBal) {
        this.myBal = myBal;
    }
}
