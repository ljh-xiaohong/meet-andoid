package com.yuejian.meet.bean;

import java.io.Serializable;

public class LifeEntity implements Serializable {


    /**
     * photoAndVideoUrl : /storage/emulated/0/baidu/searchbox/downloads/timg-4.jpeg
     * fabulousNum : 0
     * isPraise : 0
     * id : 99311
     * type : 2
     * coveSizeType:
     * title :
     * sex:1  0:女，1：男
     * name:姓名
     * photo: "https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/default.png",
     */

    private String photoAndVideoUrl;
    private int fabulousNum;
    private int isPraise;
    private int id;
    private int coveSizeType;
    private int type;
    private String title;
    private int sex;
    private String photo;
    private String name;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoAndVideoUrl() {
        return photoAndVideoUrl;
    }

    public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
        this.photoAndVideoUrl = photoAndVideoUrl;
    }

    public int getCoveSizeType() {
        return coveSizeType;
    }

    public void setCoveSizeType(int coveSizeType) {
        this.coveSizeType = coveSizeType;
    }

    public int getFabulousNum() {
        return fabulousNum;
    }

    public void setFabulousNum(int fabulousNum) {
        this.fabulousNum = fabulousNum;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
