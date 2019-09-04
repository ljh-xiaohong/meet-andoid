package com.yuejian.meet.bean;

import android.databinding.BaseObservable;

import com.yuejian.meet.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zh02 on 2017/8/13.
 */

public class CustomerInfo extends BaseObservable implements Serializable {

    public String birthday;//": 697219200000,
    public String fans_cnt;//": 0,
    public String area_name;//": "香洲区",
    public String specialty;//": "",
    public String distance;//": -1,
    public String signature;//": "",
    public String thumb_cnt;//": 8,
    public String attention_cnt;//": 0,
    public String industry;//": "",
    public String relation_type;//": 0,
    public String is_idcard_certified;//": 1,
    public String city_name;//": "珠海市",
    public String family_area_name;//": "中山",
    public String interest;//": "",
    public String surname;//": "陈",
    public String bg_url;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/2000061504697439840.jpg",
    public String origin_place;//": "",
    public String sex;//": 1,
    public String photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/2000061504697482145.jpg",
    public String is_weixin_certified;//": 1,
    public String video_price;//": 128,
    public String home;//": "",
    public String province_name;//": "广东省",
    public String province;///发起省
    public String is_business_license_certified;//": -1,
    public String name;//": "嘉俊",
    public String chat_group_tid;//": 111374027,
    public String invite_code;//": "74AC0006",
    public String customer_id;//": 200006,
    public String job;//": "",
    public String is_mobile_certified;//": 1,
    public String age;//": 25
    public int is_family_master;
    public String full_area_name;
    public String master_area_name;
    public String surname_master;
    public String perfect_ratio;
    public Long is_super=0l;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getPerfect_ratio() {
        return perfect_ratio;
    }

    public void setPerfect_ratio(String perfect_ratio) {
        this.perfect_ratio = perfect_ratio;
    }

    public String getSurname_master() {
        return surname_master;
    }

    public void setSurname_master(String surname_master) {
        this.surname_master = surname_master;
    }

    public String getMaster_area_name() {
        return master_area_name;
    }

    public void setMaster_area_name(String master_area_name) {
        this.master_area_name = master_area_name;
    }

    public String getFull_area_name() {
        return full_area_name;
    }

    public void setFull_area_name(String full_area_name) {
        this.full_area_name = full_area_name;
    }

    public int getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(int is_family_master) {
        this.is_family_master = is_family_master;
    }

    public String getVideo_price() {
        return video_price;
    }

    public void setVideo_price(String video_price) {
        this.video_price = video_price;
    }

    public String getChat_group_tid() {
        return chat_group_tid;
    }

    public void setChat_group_tid(String chat_group_tid) {
        this.chat_group_tid = chat_group_tid;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getBirthday() {
        return StringUtils.formatDate(new Date(Long.valueOf(birthday)), "yyyy/MM/dd");
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFans_cnt() {
        return fans_cnt;
    }

    public void setFans_cnt(String fans_cnt) {
        this.fans_cnt = fans_cnt;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getThumb_cnt() {
        return thumb_cnt;
    }

    public void setThumb_cnt(String thumb_cnt) {
        this.thumb_cnt = thumb_cnt;
    }

    public String getAttention_cnt() {
        return attention_cnt;
    }

    public void setAttention_cnt(String attention_cnt) {
        this.attention_cnt = attention_cnt;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getIs_idcard_certified() {
        return is_idcard_certified;
    }

    public void setIs_idcard_certified(String is_idcard_certified) {
        this.is_idcard_certified = is_idcard_certified;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getFamily_area_name() {
        return family_area_name;
    }

    public void setFamily_area_name(String family_area_name) {
        this.family_area_name = family_area_name;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public String getOrigin_place() {
        return origin_place;
    }

    public void setOrigin_place(String origin_place) {
        this.origin_place = origin_place;
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

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getIs_business_license_certified() {
        return is_business_license_certified;
    }

    public void setIs_business_license_certified(String is_business_license_certified) {
        this.is_business_license_certified = is_business_license_certified;
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
