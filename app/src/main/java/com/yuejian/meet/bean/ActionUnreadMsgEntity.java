package com.yuejian.meet.bean;

/**
 * 动态未读消息实体类
 * Created by zh03 on 2017/8/16.
 */

public class ActionUnreadMsgEntity {
    public String count;//": 0,
    public String photo;//": ""

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
