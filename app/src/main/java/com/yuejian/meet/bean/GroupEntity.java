package com.yuejian.meet.bean;

/**
 *  群列表实体类
 * Created by zh03 on 2017/8/24.
 */

public class GroupEntity {
    public String member_cnt;//": 1,
    public String province;//": "广东省",
    public String name;//"我的家族群",
    public Integer inviter_gift_cnt=0;//礼物数
    public String family_name;
    public String city_family_list;//": [{"member_cnt": 1,"city_family": "珠海家族","city": "珠海市"}]
    public String list;//
    public String chat_group_list;
    public Boolean isOpen;
    public int count=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getInviter_gift_cnt() {
        return inviter_gift_cnt;
    }

    public void setInviter_gift_cnt(Integer inviter_gift_cnt) {
        this.inviter_gift_cnt = inviter_gift_cnt;
    }

    public String getChat_group_list() {
        return chat_group_list;
    }

    public void setChat_group_list(String chat_group_list) {
        this.chat_group_list = chat_group_list;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getMember_cnt() {
        return member_cnt;
    }

    public void setMember_cnt(String member_cnt) {
        this.member_cnt = member_cnt;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity_family_list() {
        return city_family_list;
    }

    public void setCity_family_list(String city_family_list) {
        this.city_family_list = city_family_list;
    }

    public Boolean getOpen() {
        return isOpen==null?false:isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }
}
