package com.yuejian.meet.bean;

/**
 * 我的信息实体类
 * update 2019.06.20
 */
public class mine2Entity {
    public String perfectRatio;//": 0,家谱完成度
    public String friendCount;//": 0,好友总数
    public String visitCount;//": 0,访家总数
    public String promotionCount;//": 0,推广总数
    public int    inheritorFlag; // 传承人状态 0-审核未通过 1-审核通过 2-审核中 3-非传承人
    public boolean isYear; //是否是年费会员
    public String customer;//": { }用户信息

    public String getPerfectRatio() {
        return perfectRatio;
    }

    public void setPerfectRatio(String perfectRatio) {
        this.perfectRatio = perfectRatio;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getPromotionCount() {
        return promotionCount;
    }

    public void setPromotionCount(String promotionCount) {
        this.promotionCount = promotionCount;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getInheritorFlag() {
        return inheritorFlag;
    }

    public void setInheritorFlag(int inheritorFlag) {
        this.inheritorFlag = inheritorFlag;
    }

    public boolean isYear() {
        return isYear;
    }

    public void setYear(boolean year) {
        isYear = year;
    }
}
