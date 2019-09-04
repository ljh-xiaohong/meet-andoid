package com.yuejian.meet.bean;

/**
 * 发起人实体类
 * Created by zh03 on 2017/8/9.
 */

public class FamilyMasterEntity {
    public String distance;//": -1,
    public String sex;//": 0,
    public String photo;//": "http://wx.qlogo.cn/mmopen/VKpjEMWs1k0ORP6TyUwT6ictr6FCPmGygwtXgI2tg0A3A5E3Ad5dsm2k3eeLzgHljyiao4LYrhVm0OF6XBRuQnHgN06KuDleul/0
    public String is_weixin_certified;//": 1,
    public String industry;//": "",
    public String is_idcard_certified;//": -1,
    public String is_business_license_certified;//": -1,
    public String surname;//": "杨",
    public String name;//": "多多",
    public String customer_id;//": 300262,
    public String job;//": "",
    public String is_mobile_certified;//": -1,
    public String age;//": 27
    public long is_super=0l;
    public String company_name;
    public String other_job;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOther_job() {
        return other_job;
    }

    public void setOther_job(String other_job) {
        this.other_job = other_job;
    }

    public long getIs_super() {
        return is_super;
    }

    public void setIs_super(long is_super) {
        this.is_super = is_super;
    }

    public FamilyMasterEntity() {
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
