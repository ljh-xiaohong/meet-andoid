package com.yuejian.meet.bean;

import android.databinding.BaseObservable;

/**
 * Created by zh02 on 2017/8/19.
 */

public class MyWallet extends BaseObservable {

    public String recharge_bal; //充值的金币
    public String gains_bal;  //VIP收益的金币
    public String total_bal;  //可提现金额
    public String jf;  //购物积分
    public String inheritor_bal;  //传承人收益
    public String coupon_unread_cnt;
    public int    inheritorFlag;  //传承人状态   0-审核未通过 1-审核通过 2-审核中 3-非传承人
    public boolean isYear;  //是否是年费会员

    public String getJf() {
        return jf;
    }

    public void setJf(String jf) {
        this.jf = jf;
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

    public String getTotal_bal() {
        return total_bal;
    }

    public void setTotal_bal(String total_bal) {
        this.total_bal = total_bal;
    }

    public String getInheritor_bal() {
        return inheritor_bal;
    }

    public void setInheritor_bal(String inheritor_bal) {
        this.inheritor_bal = inheritor_bal;
    }

    public String getCoupon_unread_cnt() {
        return coupon_unread_cnt;
    }

    public void setCoupon_unread_cnt(String coupon_unread_cnt) {
        this.coupon_unread_cnt = coupon_unread_cnt;
    }

    public int getInheritorFlag() {
        return inheritorFlag;
    }

    public void setInheritorFlag(int inheritorFlag) {
        this.inheritorFlag = inheritorFlag;
    }

    public boolean isYear() {
        return isYear;
    }

    public void setYear(boolean year) {
        isYear = year;
    }
}
