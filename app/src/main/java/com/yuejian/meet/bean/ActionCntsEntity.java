package com.yuejian.meet.bean;

/**
 * 动态 ---数据统计
 * Created by zh03 on 2017/8/18.
 */

public class ActionCntsEntity {
    public String praise_cnt;//": 1,
    public String gift_cnt;//": 2,
    public String comment_cnt;//": 8

    public String getPraise_cnt() {
        return praise_cnt;
    }

    public void setPraise_cnt(String praise_cnt) {
        this.praise_cnt = praise_cnt;
    }

    public String getGift_cnt() {
        return gift_cnt;
    }

    public void setGift_cnt(String gift_cnt) {
        this.gift_cnt = gift_cnt;
    }

    public String getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(String comment_cnt) {
        this.comment_cnt = comment_cnt;
    }
}
