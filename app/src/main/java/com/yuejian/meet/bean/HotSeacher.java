package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * Created by fs-ljh on 2017/11/15.
 */

public class HotSeacher implements Serializable {

    /**
     * Count : 热词搜索次数
     * ConsumerKeyword : 搜索的关键字
     */

    private boolean IsCompany;
    private String title;



    public boolean isCompany() {
        return IsCompany;
    }

    public void setCompany(boolean company) {
        IsCompany = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
