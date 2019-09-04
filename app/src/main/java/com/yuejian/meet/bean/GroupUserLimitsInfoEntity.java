package com.yuejian.meet.bean;

/**
 * 群聊用户权限
 * Created by zh03 on 2017/8/29.
 */

public class GroupUserLimitsInfoEntity {
    public String fans_cnt;//": 0,粉丝数
    public String distance;//": 0,
    public String city;//": "未知",
    public String sex;//": 0,
    public String thumb_cnt;//": 0,被赞数
    public String job;
    public String photo;//": "http://wx.qlogo.cn/mmopen/UK9RSMOAw5ibvHGGPeTd1rBQILKg1EzlRA8icia5mD1gauNib38MRpvRYOpf3R0CCibgicL1t0B8K2ugGCoIqbmutEST4J8cJIF3GM/0",
    public String is_weixin_certified;//": 1,
    public String attention_cnt;//": 0,关注数
    public String relation_type;//": 1,0游客，1为成员，2管理员，3城市发起人，9客服)
    public String we_relation;//": 0,关系
    public String is_idcard_certified;//": 0,
    public String is_business_license_certified;//": 0,
    public String surname;//": "梁",
    public String unlock_date;//": "",
    public String name;//": "东波",
    public String customer_id;//": 300295,
    public String group_permit;//": 0,（0正常，1禁言3天，2禁言7天，3禁言15天）
    public String is_mobile_certified;//": 0,
    public String age;//": 27,
    public String my_relation_type;//": 1(0游客，1为成员，2管理员，3城市发起人，9客服)
    public String master_area_name;//认领发起人家族城市简称
    public Long is_family_master=Long.parseLong("0");//认领发起人id
    public Long is_super=0l;

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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFans_cnt() {
        return fans_cnt;
    }

    public void setFans_cnt(String fans_cnt) {
        this.fans_cnt = fans_cnt;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getThumb_cnt() {
        return thumb_cnt;
    }

    public void setThumb_cnt(String thumb_cnt) {
        this.thumb_cnt = thumb_cnt;
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

    public String getAttention_cnt() {
        return attention_cnt;
    }

    public void setAttention_cnt(String attention_cnt) {
        this.attention_cnt = attention_cnt;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getWe_relation() {
        return we_relation;
    }

    public void setWe_relation(String we_relation) {
        this.we_relation = we_relation;
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

    public String getUnlock_date() {
        return unlock_date;
    }

    public void setUnlock_date(String unlock_date) {
        this.unlock_date = unlock_date;
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

    public String getGroup_permit() {
        return group_permit;
    }

    public void setGroup_permit(String group_permit) {
        this.group_permit = group_permit;
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

    public String getMy_relation_type() {
        return my_relation_type;
    }

    public void setMy_relation_type(String my_relation_type) {
        this.my_relation_type = my_relation_type;
    }
}
