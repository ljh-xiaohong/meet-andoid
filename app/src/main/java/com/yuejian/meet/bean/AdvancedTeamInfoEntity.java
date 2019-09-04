package com.yuejian.meet.bean;

/**
 * 群成员--信息
 * Created by zh03 on 2017/8/30.
 */

public class AdvancedTeamInfoEntity {
    public String area_name;
    public String notification;//": "1",
    public String family_id;//": 29,
    public String group_notice;//": "",
    public String surname;//": "梁",
    public String name;//": "东波",
    public String photo;//": "http://wx.qlogo.cn/mmopen/UK9RSMOAw5ibvHGGPeTd1rBQILKg1EzlRA8icia5mD1gauNib38MRpvRYOpf3R0CCibgicL1t0B8K2ugGCoIqbmutEST4J8cJIF3GM/0",
    public String group_member_cnt;//": 1,
    public String familyMemberList;//": [{"surname": "约见","name": "客服","photo": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/head.png","customer_id": 10002},}]
    public String relation_type;//": 3,
    public String customer_id;//": 300295,
    public String tempMemberList;//": [{"surname": "梁","name": "波波","customer_id": 300296}]
    public String temp_member_cnt;//": 1
    public Long is_super=0l;

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getGroup_notice() {
        return group_notice;
    }

    public void setGroup_notice(String group_notice) {
        this.group_notice = group_notice;
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

    public String getGroup_member_cnt() {
        return group_member_cnt;
    }

    public void setGroup_member_cnt(String group_member_cnt) {
        this.group_member_cnt = group_member_cnt;
    }

    public String getFamilyMemberList() {
        return familyMemberList;
    }

    public void setFamilyMemberList(String familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTempMemberList() {
        return tempMemberList;
    }

    public void setTempMemberList(String tempMemberList) {
        this.tempMemberList = tempMemberList;
    }

    public String getTemp_member_cnt() {
        return temp_member_cnt;
    }

    public void setTemp_member_cnt(String temp_member_cnt) {
        this.temp_member_cnt = temp_member_cnt;
    }
}
