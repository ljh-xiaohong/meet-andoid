package com.yuejian.meet.bean;

import java.util.List;

/**
 * 群聊第一层数据
 * Created by zh03 on 2017/12/5.
 */

public class GroupChatEntity {
    public String groupName;
    public List<GroupEntity> groupList;
    public int numbers=0;//对应的群数
    public Boolean isOpen=true;//

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<GroupEntity> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupEntity> groupList) {
        this.groupList = groupList;
    }

}
