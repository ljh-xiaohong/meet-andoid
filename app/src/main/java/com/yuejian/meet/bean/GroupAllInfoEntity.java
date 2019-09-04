package com.yuejian.meet.bean;

/**
 * 创建群的详细信息
 * Created by zh03 on 2017/11/28.
 */

public class GroupAllInfoEntity {
    public int notification;//": 1,
    public int sort;//": 0,
    public int member_count;//": 3
    public String chat_group;
    public String top18_members;//返回前18位成员

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public String getChat_group() {
        return chat_group;
    }

    public void setChat_group(String chat_group) {
        this.chat_group = chat_group;
    }

    public String getTop18_members() {
        return top18_members;
    }

    public void setTop18_members(String top18_members) {
        this.top18_members = top18_members;
    }
}
