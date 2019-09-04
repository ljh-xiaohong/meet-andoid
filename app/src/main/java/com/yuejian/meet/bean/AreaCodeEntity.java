package com.yuejian.meet.bean;

/**
 * 国家区号
 * Created by zh03 on 2018/1/17.
 */

public class AreaCodeEntity {
    public String nation;//国家名
    public String englishNation;//英文国家
    public String nationCode;//区号
    public String price;//价格
    public String sortLetters;

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getEnglishNation() {
        return englishNation;
    }

    public void setEnglishNation(String englishNation) {
        this.englishNation = englishNation;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
