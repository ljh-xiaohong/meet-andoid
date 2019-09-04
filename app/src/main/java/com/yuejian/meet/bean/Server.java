package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * Created by pro on 2017/11/27.
 */

public class Server implements Serializable {
    public String surname;
    public String name;
    public String photo;
    public String is_online;
    public String customer_id;
    public String recentContent;
    public long recentTime;

    public int getUnreadMsgCnt() {
        return unreadMsgCnt;
    }

    public void setUnreadMsgCnt(int unreadMsgCnt) {
        this.unreadMsgCnt = unreadMsgCnt;
    }

    public int unreadMsgCnt;


    public long getRecentTime() {
        return recentTime;
    }

    public void setRecentTime(long recentTime) {
        this.recentTime = recentTime;
    }

    public String getRecentContent() {
        return recentContent;
    }

    public void setRecentContent(String recentContent) {
        this.recentContent = recentContent;
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

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
