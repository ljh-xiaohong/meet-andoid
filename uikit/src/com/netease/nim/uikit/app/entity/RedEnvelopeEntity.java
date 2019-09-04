package com.netease.nim.uikit.app.entity;

/**
 * 聊天自己定义消息实体类
 * Created by zh03 on 2017/8/21.
 */

public class RedEnvelopeEntity {
    public String content;//
    public String giftMoney;//
    public String gift_id;//
    public String gift_name;//
    public String state;//
    public String type;///
    public String customer_id;//
    public String nick_name;//发红包人的名称
    public String op_customer_id;//
    public String op_nick_name;//抢红包人的名称
    public String bag_id;//红包id
    public String bag_gift_cnt;
    public String expense_id;//收费id
    public String group_name;//群名
    public String t_id;//群id
    public String association_id;
    public String charge;

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getAssociation_id() {
        return association_id;
    }

    public void setAssociation_id(String association_id) {
        this.association_id = association_id;
    }

    public String getBag_gift_cnt() {
        return bag_gift_cnt;
    }

    public void setBag_gift_cnt(String bag_gift_cnt) {
        this.bag_gift_cnt = bag_gift_cnt;
    }

    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getOp_customer_id() {
        return op_customer_id;
    }

    public void setOp_customer_id(String op_customer_id) {
        this.op_customer_id = op_customer_id;
    }

    public String getOp_nick_name() {
        return op_nick_name;
    }

    public void setOp_nick_name(String op_nick_name) {
        this.op_nick_name = op_nick_name;
    }

    public String getBag_id() {
        return bag_id;
    }

    public void setBag_id(String bag_id) {
        this.bag_id = bag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(String giftMoney) {
        this.giftMoney = giftMoney;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
