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
     */

    private String photoAndVideoUrl;
    private int fabulousNum;
    private int isPraise;
    private int id;
    private int coveSizeType;
    private int type;
    private String title;

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
