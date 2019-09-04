package com.yuejian.meet.bean;

/**
 * Created by zh03 on 2017/12/27.
 */
/*
发现宗亲的父类
 */
public class FindClanEntity {
    public String clan_list;//通讯录里面的宗亲
    public String near_list;//附近的人

    public String getClan_list() {
        return clan_list;
    }

    public void setClan_list(String clan_list) {
        this.clan_list = clan_list;
    }

    public String getNear_list() {
        return near_list;
    }

    public void setNear_list(String near_list) {
        this.near_list = near_list;
    }
}
