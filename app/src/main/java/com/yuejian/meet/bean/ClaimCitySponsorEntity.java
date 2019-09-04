package com.yuejian.meet.bean;

/**
 * 城市认领人实体类
 * Created by zh03 on 2017/9/3.
 */

public class ClaimCitySponsorEntity {
    public String sex;//": 1,
    public String apply_status;//": 0,0-申请中,1-申请通过,2-申请未通过
    public String photo;//": "http://wx.qlogo.cn/mmopen/vi_32/xGAicesoia4lj3icqInK5DaTU0y2FibIIaibqTHlZaJg95s4LqibWuiaVvoiaNBKZM8d2RbFoYrPibeLGI1N9JjUiaSIElsg/0",
    public String is_weixin_certified;//": 1,
    public String industry;//": "",
    public String is_idcard_certified;//": 0,
    public String is_business_license_certified;//": 0,
    public String surname;//": "梁",
    public String name;//": "东波",
    public String customer_id;//": 200021,
    public String job;//": "",
    public String is_mobile_certified;//": 0,
    public String age;//": 27

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getApply_status() {
        return apply_status;
    }

    public void setApply_status(String apply_status) {
        this.apply_status = apply_status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIs_weixin_certified() {
        return is_weixin_certified;
    }

    public void setIs_weixin_certified(String is_weixin_certified) {
        this.is_weixin_certified = is_weixin_certified;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIs_idcard_certified() {
        return is_idcard_certified;
    }

    public void setIs_idcard_certified(String is_idcard_certified) {
        this.is_idcard_certified = is_idcard_certified;
    }

    public String getIs_business_license_certified() {
        return is_business_license_certified;
    }

    public void setIs_business_license_certified(String is_business_license_certified) {
        this.is_business_license_certified = is_business_license_certified;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIs_mobile_certified() {
        return is_mobile_certified;
    }

    public void setIs_mobile_certified(String is_mobile_certified) {
        this.is_mobile_certified = is_mobile_certified;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
