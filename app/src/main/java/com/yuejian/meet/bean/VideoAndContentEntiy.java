package com.yuejian.meet.bean;

import java.io.Serializable;

public class VideoAndContentEntiy implements Serializable {


    /**
     * photoAndVideoUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201908241519131913598158.jpg   //封面图url
     * userPhoto : http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132
     * crContent : //创作内容
     * shopName :  //商品名称
     * contentTitle : 草稿文章测试标题   //创作标题
     * userName : xiao女子   //用户名
     * userVipType : 0   //0：普通用户，1：VIP会员
     * isRelation : 0    //是否关注  0-否  1-是
     * commentNum : 0    //评论数量
     * labelId : 1,2,3,4   //标签id
     * createTime : 1567581282  //创建时间
     * fabulousNum : 0    //点赞数量
     * customerId : 500102  //用户id
     * isPraise : 0   //是否点赞    0-否1-是
     * id : 99309    //创作id
     * shopId :      //商品id
     * labelName : #测试标签#测试活动标签2#国庆节#中秋节   // 标签名称
     * isCollection:true            //收藏标识
     * gPhoto:""                //商品封面缩略图路径
     * gPriceVip:""             //商品VIP价格
     * gPriceOriginal:""        //商品原价
     * viewNum:1                //阅读数
     * contentType : 2    //创作类型：2-文章4-视频
     */

    private String photoAndVideoUrl;
    private String userPhoto;
    private String crContent;
    private String shopName;
    private String contentTitle;
    private String userName;
    private String userVipType;
    private int isRelation;
    private String commentNum;
    private String labelId;
    private String createTime;
    private String fabulousNum;
    private String customerId;
    private int isPraise;
    private int id;
    private String shopId;
    private String labelName;
    private String contentType;
    private boolean isCollection;
    private String gPhoto;
    private String gPriceVip;
    private String gPriceOriginal;
    private int viewNum;

    public String getgPhoto() {
        return gPhoto;
    }

    public void setgPhoto(String gPhoto) {
        this.gPhoto = gPhoto;
    }

    public String getgPriceVip() {
        return gPriceVip;
    }

    public void setgPriceVip(String gPriceVip) {
        this.gPriceVip = gPriceVip;
    }

    public String getgPriceOriginal() {
        return gPriceOriginal;
    }

    public void setgPriceOriginal(String gPriceOriginal) {
        this.gPriceOriginal = gPriceOriginal;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

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

    public String getCrContent() {
        return crContent;
    }

    public void setCrContent(String crContent) {
        this.crContent = crContent;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserVipType() {
        return userVipType;
    }

    public void setUserVipType(String userVipType) {
        this.userVipType = userVipType;
    }

    public int getIsRelation() {
        return isRelation;
    }

    public void setIsRelation(int isRelation) {
        this.isRelation = isRelation;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFabulousNum() {
        return fabulousNum;
    }

    public void setFabulousNum(String fabulousNum) {
        this.fabulousNum = fabulousNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
