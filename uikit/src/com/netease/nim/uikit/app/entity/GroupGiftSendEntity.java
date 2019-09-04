package com.netease.nim.uikit.app.entity;

/**群发红包实体类
 * Created by zh03 on 2017/8/26.
 */

public class GroupGiftSendEntity {
    public String content;
    public Long gift_id;
    public Long bag_id;
    public Long gift_count;//数量
    public String gift_name;
    public Long giftMoney;//价格
    public Boolean state;//是否可抢
    public GroupGiftSendEntity(){}

    public GroupGiftSendEntity(String content, Long gift_id, Long gift_count, String gift_name, Long giftMoney, Boolean state) {
        this.content = content;
        this.gift_id = gift_id;
        this.gift_count = gift_count;
        this.gift_name = gift_name;
        this.giftMoney = giftMoney;
        this.state = state;
    }

    public Long getBag_id() {
        return bag_id;
    }

    public void setBag_id(Long bag_id) {
        this.bag_id = bag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getGift_id() {
        return gift_id;
    }

    public void setGift_id(Long gift_id) {
        this.gift_id = gift_id;
    }

    public Long getGift_count() {
        return gift_count;
    }

    public void setGift_count(Long gift_count) {
        this.gift_count = gift_count;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public Long getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(Long giftMoney) {
        this.giftMoney = giftMoney;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
