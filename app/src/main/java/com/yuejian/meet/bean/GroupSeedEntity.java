package com.yuejian.meet.bean;

/**
 * 群列表 子类
 * Created by zh03 on 2017/8/24.
 */

public class GroupSeedEntity {
    public String member_cnt;//": 1,
    public String city_family;//": "珠海家族",
    public String city;//": "珠海市"
    public String t_id;
    public String group_id;
    public String group_photo;
    public String group_name;//": "珠海1群",
    public Integer inviter_gift_cnt=0;//礼物数
    public int sort=0;//置顶

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getInviter_gift_cnt() {
        return inviter_gift_cnt;
    }

    public void setInviter_gift_cnt(Integer inviter_gift_cnt) {
        this.inviter_gift_cnt = inviter_gift_cnt;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getGroup_photo() {
        return group_photo==null?"":group_photo;
    }

    public void setGroup_photo(String group_photo) {
        this.group_photo = group_photo;
    }

    public String getMember_cnt() {
        return member_cnt;
    }

    public void setMember_cnt(String member_cnt) {
        this.member_cnt = member_cnt;
    }

    public String getCity_family() {
        return city_family;
    }

    public void setCity_family(String city_family) {
        this.city_family = city_family;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
