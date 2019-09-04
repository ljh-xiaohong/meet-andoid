package com.yuejian.meet.bean;

/**
 * Created by zh03 on 2017/12/27.
 */

/**
 * 通讯录电话
 */
public class BookJson {
    public String mobile;//电话号码
    public String  byname;//名字
    public Boolean isSelect=false;
    public String sortLetters;

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }
}

