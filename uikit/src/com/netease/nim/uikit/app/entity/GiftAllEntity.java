package com.netease.nim.uikit.app.entity;

import java.io.Serializable;

/**
 * 礼物
 * Created by zh03 on 2017/7/4.
 */

public class GiftAllEntity implements Serializable {
    public String gift_image;//": "http://mchatoss.oss-cn-shenzhen.aliyuncs.com/gift/sAsrYfhs5p.png",
    public String gift_name;//": "棒棒糖",
    public Long gift_price;//": 10,
    public Long gift_id;
    public Long gift_count;
    public String id;//": 21,
    public String status;//": 1
    public String count="1";//发送的礼物计数
    public String gift_benediction="恭喜发财,万事如意";//祝福语
    public boolean isSelected=false;

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

    public String getGift_benediction() {
        return gift_benediction;
    }

    public void setGift_benediction(String gift_benediction) {
        this.gift_benediction = gift_benediction;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public Long getGift_price() {
        return gift_price;
    }

    public void setGift_price(Long gift_price) {
        this.gift_price = gift_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
