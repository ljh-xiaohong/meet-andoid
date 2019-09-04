package com.yuejian.meet.bean;

public class VisitListEntity {
    public String surname;//": "第五",
    public String sex;//": 0,
    public Long visit_info_create_time;//": 1541820405000,
    public String name;//": "维钱",
    public String photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201809121438383838392405.png",
    public String customer_id;//": 300555

    public Long getVisit_info_create_time() {
        return visit_info_create_time;
    }

    public void setVisit_info_create_time(Long visit_info_create_time) {
        this.visit_info_create_time = visit_info_create_time;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
