package com.yuejian.meet.bean;

/**
 * 帖子图片文件路径
 * <b>创建时间</b> 2016/5/10 <br>
 *
 * @author zhouwenjun
 */
public class FeedsResourceBean {
    public String imageUrl;
    public String videoUrl;

    public FeedsResourceBean(){

    }

    public FeedsResourceBean(String imageUrl, String videoUrl) {
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
