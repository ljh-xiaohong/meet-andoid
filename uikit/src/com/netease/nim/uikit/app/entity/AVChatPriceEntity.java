package com.netease.nim.uikit.app.entity;

/**
 * 视频聊天返回的价格及用户余额
 * Created by zh03 on 2017/7/25.
 */

public class AVChatPriceEntity {
    public String recharge_bal;//": -122,
    public String gains_bal;//": 0,
    public String video_price;//": 100

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

    public String getVideo_price() {
        return video_price;
    }

    public void setVideo_price(String video_price) {
        this.video_price = video_price;
    }
}
