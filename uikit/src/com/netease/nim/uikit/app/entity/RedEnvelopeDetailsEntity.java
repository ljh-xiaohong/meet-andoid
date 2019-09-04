package com.netease.nim.uikit.app.entity;

/**抢红包详情
 * Created by zh03 on 2017/8/26.
 */

public class RedEnvelopeDetailsEntity {

    public String bag_id;//": 14,
    public String customer_list;//[]
    public String name;//": "梁东波",
    public String photo;//": "http://wx.qlogo.cn/mmopen/UK9RSMOAw5ibvHGGPeTd1rBQILKg1EzlRA8icia5mD1gauNib38MRpvRYOpf3R0CCibgicL1t0B8K2ugGCoIqbmutEST4J8cJIF3GM/0",
    public String content;//": "恭喜发财,万事如意"
    public String gift_remaining_cnt;//剩余礼物
    public String gift_cnt;//礼物总数
    public String gift_name;//礼物名
    public String is_rob;

    public String getIs_rob() {
        return is_rob;
    }

    public void setIs_rob(String is_rob) {
        this.is_rob = is_rob;
    }

    public String getGift_remaining_cnt() {
        return gift_remaining_cnt;
    }

    public void setGift_remaining_cnt(String gift_remaining_cnt) {
        this.gift_remaining_cnt = gift_remaining_cnt;
    }

    public String getGift_cnt() {
        return gift_cnt;
    }

    public void setGift_cnt(String gift_cnt) {
        this.gift_cnt = gift_cnt;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getBag_id() {
        return bag_id;
    }

    public void setBag_id(String bag_id) {
        this.bag_id = bag_id;
    }

    public String getCustomer_list() {
        return customer_list;
    }

    public void setCustomer_list(String customer_list) {
        this.customer_list = customer_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
