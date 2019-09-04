package com.yuejian.meet.bean;

/**
 * 关系标签
 */
public class FriendTreeTagEntity {
    public String friend_name;//": "同窗",
    public int friend_type;//": 1,
    public int id;//": 2
    public boolean isSelect=false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public int getFriend_type() {
        return friend_type;
    }

    public void setFriend_type(int friend_type) {
        this.friend_type = friend_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
