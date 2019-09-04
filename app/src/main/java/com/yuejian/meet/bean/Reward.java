package com.yuejian.meet.bean;

public class Reward {
    public String photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1503111775_0_hengping.jpg",
    public String customer_id;//": 300283
    public String create_time;//": 1503904723000,
    public String surname;//": "杨",
    public String name;//": "锦超",
    public String cnt;//
    public String gift_name;

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }
    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
