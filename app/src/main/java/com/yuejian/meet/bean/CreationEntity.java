package com.yuejian.meet.bean;

import java.io.Serializable;

public class CreationEntity implements Serializable {


    /**
     * previewUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909072205405403593.jpg
     * labelId : 1,2
     * id : 29
     * contentTitle : 八月秋
     * photoAndVideoUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101022232223377349.png
     * fabulousNum : 0
     * contentId : 99477
     * draftsId : 4
     * content :
     */

    private String previewUrl;
    private String labelId;
    private int id;
    private String contentTitle;
    private String photoAndVideoUrl;
    private int fabulousNum;
    private int contentId;
    private String labelName;
    private int draftsId;
    private String content;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getPhotoAndVideoUrl() {
        return photoAndVideoUrl;
    }

    public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
        this.photoAndVideoUrl = photoAndVideoUrl;
    }

    public int getFabulousNum() {
        return fabulousNum;
    }

    public void setFabulousNum(int fabulousNum) {
        this.fabulousNum = fabulousNum;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getDraftsId() {
        return draftsId;
    }

    public void setDraftsId(int draftsId) {
        this.draftsId = draftsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
