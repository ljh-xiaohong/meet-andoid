package com.yuejian.meet.bean;

import java.io.Serializable;

public class VideoAndContentEntiy implements Serializable {


    /**
     * photoAndVideoUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201908241519131913598158.jpg
     * userPhoto : http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A / 132
     * createTime : 1567581282    //创建时间
     * contentVipType : 0        //vip配置类型：0募捐1活动2商品3专属
     * crContent :
     * customerId  : 500102      //详情用户id
     * labelTitle : 测试标签     //标签名称
     * contentTitle : 草稿文章测试标题  //标题
     * vipDeployId : 0                 //vip配置广告id
     * userName : 小女子               //用户名
     * userVipType : 0                //会员类型（0：普通用户，1：VIP会员）
     * isRelation : 0                 //是否关注      0-否  1-是
     */

    private String photoAndVideoUrl;
    private String userPhoto;
    private int createTime;
    private int contentVipType;
    private String crContent;
    private int customerId;
    private String labelTitle;
    private String contentTitle;
    private int vipDeployId;
    private String userName;
    private int userVipType;
    private int isRelation;

    public String getPhotoAndVideoUrl() {
        return photoAndVideoUrl;
    }

    public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
        this.photoAndVideoUrl = photoAndVideoUrl;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getContentVipType() {
        return contentVipType;
    }

    public void setContentVipType(int contentVipType) {
        this.contentVipType = contentVipType;
    }

    public String getCrContent() {
        return crContent;
    }

    public void setCrContent(String crContent) {
        this.crContent = crContent;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

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

    public int getVipDeployId() {
        return vipDeployId;
    }

    public void setVipDeployId(int vipDeployId) {
        this.vipDeployId = vipDeployId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserVipType() {
        return userVipType;
    }

    public void setUserVipType(int userVipType) {
        this.userVipType = userVipType;
    }

    public int getIsRelation() {
        return isRelation;
    }

    public void setIsRelation(int isRelation) {
        this.isRelation = isRelation;
    }
}
