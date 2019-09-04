package com.netease.nim.uikit.app.entity;

/**
 *钱的实体类
 * Created by zh03 on 2017/7/8.
 */

public class MonyEntity {
    public String recharge_bal;//": 999976," +一充值的钱
    public String gains_bal;//": 0          收益的钱
    public String total_bal;//总金额

    public String getTotal_bal() {
        return total_bal;
    }

    public void setTotal_bal(String total_bal) {
        this.total_bal = total_bal;
    }

    public String getRecharge_bal() {
        return recharge_bal;
    }

    public void setRecharge_bal(String recharge_bal) {
        this.recharge_bal = recharge_bal;
    }

    public String getGains_bal() {
        return gains_bal;
    }

    public void setGains_bal(String gains_bal) {
        this.gains_bal = gains_bal;
    }
}
