package com.netease.nim.uikit.app.entity;

/**
 * 视频价格--实体类
 * Created by zh03 on 2017/7/10.
 */

public class PriceEnitty {
    public String recharge_bal;//": 0,
    public String gains_bal;//": 7,
    public Long video_price;//": 0视频价格

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

    public Long getVideo_price() {
        return video_price;
    }

    public void setVideo_price(Long video_price) {
        this.video_price = video_price;
    }

}
