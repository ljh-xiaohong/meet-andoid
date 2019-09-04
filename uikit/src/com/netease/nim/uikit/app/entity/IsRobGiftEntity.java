package com.netease.nim.uikit.app.entity;

/**
 * 是否抢红包
 * Created by zh03 on 2017/12/28.
 */
public class IsRobGiftEntity {
    public String gift_image;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/yRhGatsGxa.png",
    public String surname;//": "梁",
    public Long gift_remaining_cnt;//": 1,
    public String name;//": "冷冷",
    public Long is_family_master;//": 0,
    public String photo;///": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/zupu/201710201923572357.png",
    public String gift_name;//": "茉莉",
    public String master_area_name;//": "",
    public String customer_id;//": 277487,
    public String content;//": "恭喜发财,万事如意"
    public Long is_super=0l;

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getGift_remaining_cnt() {
        return gift_remaining_cnt;
    }

    public void setGift_remaining_cnt(Long gift_remaining_cnt) {
        this.gift_remaining_cnt = gift_remaining_cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(Long is_family_master) {
        this.is_family_master = is_family_master;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getMaster_area_name() {
        return master_area_name;
    }

    public void setMaster_area_name(String master_area_name) {
        this.master_area_name = master_area_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
