package com.yuejian.meet.bean;

public class BugTypeEntity {
    public String name;
    public int type;//问题分类(1:反馈BUG, 2:功能建议, 3:吐槽, 4:其他)
    public Boolean isSelect;
    public BugTypeEntity(String name,int type,Boolean isSelect){
        this.name=name;
        this.type=type;
        this.isSelect=isSelect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
