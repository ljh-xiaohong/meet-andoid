package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : g000gle
 * @time : 2019/5/21 11:28
 * @desc : 单项 VIP 详情 Entity
 */
public class SingleVipDetailEntity implements Serializable {
    long    id;                //单项VIP购买类型
    String  expiration_time;
    String  signred;
    int     remaining_days;    //剩余天数
    long    customer_id;       //用户ID
    int     order_number;      //排序序号
    String  english_name;      //英文名称
    int     residue_degree;    //剩余次数
    boolean tag_red;           //是否标记红色
    int     type;              //类型 0-单项 1-年费
    String  vip_introduce;     //单项VIP介绍
    String  vip_name;          //单项VIP名称
    String  single_price;      //单次购买价格
    String  monthly_price;     //包月购买价格
    String  yearly_price;      //包年购买价格

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(String expiration_time) {
        this.expiration_time = expiration_time;
    }

    public String getSignred() {
        return signred;
    }

    public void setSignred(String signred) {
        this.signred = signred;
    }

    public int getRemaining_days() {
        return remaining_days;
    }

    public void setRemaining_days(int remaining_days) {
        this.remaining_days = remaining_days;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public int getResidue_degree() {
        return residue_degree;
    }

    public void setResidue_degree(int residue_degree) {
        this.residue_degree = residue_degree;
    }

    public boolean isTag_red() {
        return tag_red;
    }

    public void setTag_red(boolean tag_red) {
        this.tag_red = tag_red;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVip_introduce() {
        return vip_introduce;
    }

    public void setVip_introduce(String vip_introduce) {
        this.vip_introduce = vip_introduce;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getMonthly_price() {
        return monthly_price;
    }

    public void setMonthly_price(String monthly_price) {
        this.monthly_price = monthly_price;
    }

    public String getYearly_price() {
        return yearly_price;
    }

    public void setYearly_price(String yearly_price) {
        this.yearly_price = yearly_price;
    }
}
