package com.yuejian.meet.bean;

/**
 * 我的访问量实体类
 */
public class VisitorAcEntity {
    public String visitCount;//": 0,
    public String todayVisitCount;//": 0,
    public String visitList;//":

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getTodayVisitCount() {
        return todayVisitCount;
    }

    public void setTodayVisitCount(String todayVisitCount) {
        this.todayVisitCount = todayVisitCount;
    }

    public String getVisitList() {
        return visitList;
    }

    public void setVisitList(String visitList) {
        this.visitList = visitList;
    }
}
