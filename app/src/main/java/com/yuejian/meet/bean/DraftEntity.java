package com.yuejian.meet.bean;

import java.io.Serializable;

public class DraftEntity implements Serializable {


    /**
     * labelTitle : 测试标签   //标题标题
     * contentTitle : 123     //草稿箱内容标题
     * id : 1                 //草稿箱内容id
     * content : ??          //草稿箱内容
     * photoAndVideoUrl :    //封面图url
     * coverSizeType : 0     //封面图尺寸类型：0竖屏1横屏
     */

    private String labelTitle;
    private String contentTitle;
    private int id;
    private String content;
    private String photoAndVideoUrl;
    private int coverSizeType;

    public String getLabelTitle() {
        return labelTitle;
    }

    public void setLabelTitle(String labelTitle) {
        this.labelTitle = labelTitle;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoAndVideoUrl() {
        return photoAndVideoUrl;
    }

    public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
        this.photoAndVideoUrl = photoAndVideoUrl;
    }

    public int getCoverSizeType() {
        return coverSizeType;
    }

    public void setCoverSizeType(int coverSizeType) {
        this.coverSizeType = coverSizeType;
    }
}
