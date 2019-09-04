package com.yuejian.meet.bean;

/**
 * 成员实体类
 * Created by zh03 on 2017/8/9.
 */

public class MembersEntity {
  public String distance;//": -1,
  public String sex;//": 1,
  public String photo;//": "",
  public String is_weixin_certified;//": -1,微信认证
  public String industry;//": "",
  public String is_idcard_certified;//": -1,身份认证
  public String is_business_license_certified;//": -1,企业认证
  public String surname;//": "杨",
  public String name;//": "锦超",
  public String customer_id;//": 300241,
  public String job;//": "",
  public String is_mobile_certified;//": 1,手机认证
  public String age;//": 17
  public Long is_family_master = 0L;//大于0表示是城市发起人
  public String master_area_name;//
  public Long is_super = 0l;
  public String family_area;
  public boolean isSelect = false;
  public String company_name;
  public String other_job;
  public int is_friend;

  public int getIs_friend() {
    return is_friend;
  }

  public void setIs_friend(int is_friend) {
    this.is_friend = is_friend;
  }

  public String getOther_job() {
    return other_job;
  }

  public void setOther_job(String other_job) {
    this.other_job = other_job;
  }

  public String getCompany_name() {
    return company_name;
  }

  public void setCompany_name(String company_name) {
    this.company_name = company_name;
  }

  public boolean isSelect() {
    return isSelect;
  }

  public void setSelect(boolean select) {
    isSelect = select;
  }

  public String getFamily_area() {
    return family_area;
  }

  public void setFamily_area(String family_area) {
    this.family_area = family_area;
  }

  public Long getIs_super() {
    return is_super;
  }

  public void setIs_super(Long is_super) {
    this.is_super = is_super;
  }

  public Long getIs_family_master() {
    return is_family_master;
  }

  public void setIs_family_master(Long is_family_master) {
    this.is_family_master = is_family_master;
  }

  public String getMaster_area_name() {
    return master_area_name;
  }

  public void setMaster_area_name(String master_area_name) {
    this.master_area_name = master_area_name;
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
