package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

public class VideoAndContentEntiy implements Serializable {

    //详情
    private ContentDetail contentDetail;

    //评论列表
    private List<CommentBean.DataBean> CommentList;

    public ContentDetail getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(ContentDetail contentDetail) {
        this.contentDetail = contentDetail;
    }

    public List<CommentBean.DataBean> getCommentList() {
        return CommentList;
    }

    public void setCommentList(List<CommentBean.DataBean> commentList) {
        CommentList = commentList;
    }

    public static class ContentDetail implements Serializable {
        /**
         * photoAndVideoUrl :   //封面图url
         * userPhoto :    //用户头像
         * crContent :    //创作内容
         * contentTitle : 草稿文章测试标题  //创作标题
         * userName : xiao女子   //用户名
         * userVipType : 0    //0：普通用户，1：VIP会员
         * relationType : 0   //是否关注  0-否  1-是
         * commentNum : 0     //评论数量
         * labelId : 1,2,3,4  //标签id
         * createTime : 1567581282  //创建时间
         * fabulousNum : 0   //点赞数量
         * customerId : 500102  //用户id
         * isPraise : 0    //是否点赞    0-否1-是
         * id : 99309      //创作id
          * isCollection : false  //收藏标识
         * labelName : #测试标签#测试活动标签2#国庆节#中秋节  // 标签名称
         * viewNum : 1    //阅读数
         * contentType : 2  //创作类型：2-文章4-视频
         * shopList:       //商品详情
         */

        private shopList shopList;
        private String photoAndVideoUrl;
        private String userPhoto;
        private String crContent;
        private String contentTitle;
        private String userName;
        private String userVipType;
        private String relationType;
        private String commentNum;
        private String labelId;
        private String createTime;
        private String fabulousNum;
        private String customerId;
        private String isPraise;
        private String id;
        private boolean isCollection;
        private String labelName;
        private String viewNum;
        private String contentType;

        public VideoAndContentEntiy.shopList getShopList() {
            return shopList;
        }

        public void setShopList(VideoAndContentEntiy.shopList shopList) {
            this.shopList = shopList;
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

        public String getRelationType() {
            return relationType;
        }

        public void setRelationType(String relationType) {
            this.relationType = relationType;
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

        public String getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(String isPraise) {
            this.isPraise = isPraise;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsCollection() {
            return isCollection;
        }

        public void setIsCollection(boolean isCollection) {
            this.isCollection = isCollection;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getViewNum() {
            return viewNum;
        }

        public void setViewNum(String viewNum) {
            this.viewNum = viewNum;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

//    public static class CommentList implements Serializable {
//        /**
//         * replyCommentId : 0
//         * opName :
//         * customerId : 500193
//         * name : 张春杰
//         * photo : https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/default.png
//         * comment : 不仅不会拒绝
//         * id : 74
//         * opPhoto :
//         * type : 3
//         * opCustomerId :
//         * createTime:1568971931
//         */
//
//        private String createTime;
//        private String replyCommentId;
//        private String opName;
//        private String customerId;
//        private String name;
//        private String photo;
//        private String comment;
//        private String id;
//        private String opPhoto;
//        private String type;
//        private String opCustomerId;
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        public String getReplyCommentId() {
//            return replyCommentId;
//        }
//
//        public void setReplyCommentId(String replyCommentId) {
//            this.replyCommentId = replyCommentId;
//        }
//
//        public String getOpName() {
//            return opName;
//        }
//
//        public void setOpName(String opName) {
//            this.opName = opName;
//        }
//
//        public String getCustomerId() {
//            return customerId;
//        }
//
//        public void setCustomerId(String customerId) {
//            this.customerId = customerId;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPhoto() {
//            return photo;
//        }
//
//        public void setPhoto(String photo) {
//            this.photo = photo;
//        }
//
//        public String getComment() {
//            return comment;
//        }
//
//        public void setComment(String comment) {
//            this.comment = comment;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getOpPhoto() {
//            return opPhoto;
//        }
//
//        public void setOpPhoto(String opPhoto) {
//            this.opPhoto = opPhoto;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getOpCustomerId() {
//            return opCustomerId;
//        }
//
//        public void setOpCustomerId(String opCustomerId) {
//            this.opCustomerId = opCustomerId;
//        }
//
//    }

    public static class shopList implements Serializable {

        /**
         * gPhoto : //商品图
         * gDes :   //商品描述
         * gPriceVip :  //商品VIP价格
         * shopName :   //商品名称
         * gPriceOriginal :  //商品原价
         * shopId :     //商品id
         */

        private String gPhoto;
        private String gDes;
        private String gPriceVip;
        private String shopName;
        private String gPriceOriginal;
        private String shopId;

        public String getGPhoto() {
            return gPhoto;
        }

        public void setGPhoto(String gPhoto) {
            this.gPhoto = gPhoto;
        }

        public String getGDes() {
            return gDes;
        }

        public void setGDes(String gDes) {
            this.gDes = gDes;
        }

        public String getGPriceVip() {
            return gPriceVip;
        }

        public void setGPriceVip(String gPriceVip) {
            this.gPriceVip = gPriceVip;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getGPriceOriginal() {
            return gPriceOriginal;
        }

        public void setGPriceOriginal(String gPriceOriginal) {
            this.gPriceOriginal = gPriceOriginal;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
    }

}
