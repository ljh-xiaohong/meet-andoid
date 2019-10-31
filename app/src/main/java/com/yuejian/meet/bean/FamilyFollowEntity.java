package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/5 20:34
 * @desc : 家圈 - 关注 - 实体类
 */
public class FamilyFollowEntity implements Serializable{


    /**
     * code : 0
     * data : [{"commentNum":0,"createTime":1567416592,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99276,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567416540,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99275,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567138203,"vipType":0,"fabulousNum":1,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99268,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":9,"createTime":1567134219,"vipType":0,"fabulousNum":-3,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[{"articleCommentContent":"测试动态评论1","articleCommentId":55,"articleCommentPraiseCnt":0,"articleCommentTime":1567222128000,"articleCommentType":4,"articleObjectId":99266,"articleReplyCnt":0,"customerId":500102,"opCustomerId":500103,"replyCommentId":49}],"id":99266,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567066881,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99265,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567063782,"vipType":0,"fabulousNum":0,"name":"测试女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99264,"title":"测试动态","content":"测试动态发布测试动态发布测试动态发布测试动态发布"},{"commentNum":0,"createTime":1567063665,"vipType":0,"fabulousNum":1,"name":"测试女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99263,"title":"测试动态","content":"测试动态发布测试动态发布测试动态发布测试动态发布"},{"commentNum":0,"createTime":1566958540,"vipType":null,"fabulousNum":0,"name":null,"photo":null,"commentMap":[],"id":26,"title":"大家一起看","content":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/ios_video_audios/1561616407500101_shuping.mp4"},{"commentNum":0,"createTime":1566958540,"vipType":null,"fabulousNum":0,"name":null,"photo":null,"commentMap":[{"articleCommentContent":"2323423","articleCommentId":62,"articleCommentPraiseCnt":0,"articleCommentTime":1567512525000,"articleCommentType":3,"articleObjectId":27,"articleReplyCnt":0,"customerId":500103,"opCustomerId":0,"replyCommentId":0}],"id":27,"title":"我","content":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/ios_video_audios/1561616531500101_shuping.mp4"},{"commentNum":0,"createTime":1566958540,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":29,"title":"title","content":"视频内容label_id=0"}]
     * message : 操作成功
     * result : true
     */

    private int code;
    private String message;
    private boolean result;
    private List<DataBean> data;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        /**
         * commentNum : 0
         * photoAndVideoUrl :
         * createTime : 1568012456
         * fabulousNum : 0
         * customerId : 722583
         * name :
         * photo :
         * vipType :
         * commentMap : []
         * id : 99461
         * type : 1
         * content : 只能看书睡觉睡觉睡觉睡觉睡觉玩手机上看手机的请加点链接在一起的时候可以看到这个世界上的东西了，宝贝我爱你自己问的问题。你在一起我也不想你的话就这样对他有一个想法就是说的话我不知道只能看书睡觉睡觉睡觉睡觉睡觉玩手机上看手机的请加点链接在一起的时候可以看到这个世界上的东西了，宝贝我爱你自己问的问题。你在一起我也不想你的话就这样对他有一个想法就是说的话我不知道
         */

        private String commentNum;
        private String photoAndVideoUrl;
        private String createTime;
        private String fabulousNum;
        private String customerId;
        private String name;
        private String photo;
        private String vipType;
        private int id;
        private String type;
        private String content;
        private String title;
        private String isPraise;
        private int CoveSizeType;
        private boolean isMe;

        public int getCoveSizeType() {
            return CoveSizeType;
        }

        public void setCoveSizeType(int coveSizeType) {
            CoveSizeType = coveSizeType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(String isPraise) {
            this.isPraise = isPraise;
        }

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }

        private List<CommentBean> commentMap;

        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getPhotoAndVideoUrl() {
            return photoAndVideoUrl;
        }

        public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
            this.photoAndVideoUrl = photoAndVideoUrl;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getVipType() {
            return vipType;
        }

        public void setVipType(String vipType) {
            this.vipType = vipType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<CommentBean> getCommentMap() {
            return commentMap;
        }

        public void setCommentMap(List<CommentBean> commentMap) {
            this.commentMap = commentMap;
        }

        public static class CommentBean implements Serializable {

            /**
             * articleCommentContent : 😄
             * opName :
             * userName : 凉凉
             * articleCommentType : 3
             */

            private String articleCommentContent;
            private String opName;
            private String userName;
            private String articleCommentType;

            public String getArticleCommentContent() {
                return articleCommentContent;
            }

            public void setArticleCommentContent(String articleCommentContent) {
                this.articleCommentContent = articleCommentContent;
            }

            public String getOpName() {
                return opName;
            }

            public void setOpName(String opName) {
                this.opName = opName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getArticleCommentType() {
                return articleCommentType;
            }

            public void setArticleCommentType(String articleCommentType) {
                this.articleCommentType = articleCommentType;
            }
        }
    }
}
