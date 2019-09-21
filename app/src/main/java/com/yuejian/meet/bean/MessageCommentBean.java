package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 20:20
 * @desc :
 */
public class MessageCommentBean {


    /**
     * code : 0
     * data : [{"articleCommentId":92,"userPhoto":"","photoAndVideoUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101512521252515460.jpg","articleCommentContent":"操","replyCommentId":0,"articleCommentTime":1568971931,"articleCommentType":3,"userName":"","type":2,"articleObjectId":99731}]
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
         * articleCommentId : 92
         * userPhoto :
         * photoAndVideoUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101512521252515460.jpg
         * articleCommentContent : 操
         * replyCommentId : 0
         * articleCommentTime : 1568971931
         * articleCommentType : 3
         * userName :
         * type : 2
         * articleObjectId : 99731
         */

        private int articleCommentId;
        private String userPhoto;
        private String photoAndVideoUrl;
        private String articleCommentContent;
        private int replyCommentId;
        private int articleCommentTime;
        private int articleCommentType;
        private String userName;
        private int type;
        private int articleObjectId;

        public int getArticleCommentId() {
            return articleCommentId;
        }

        public void setArticleCommentId(int articleCommentId) {
            this.articleCommentId = articleCommentId;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getPhotoAndVideoUrl() {
            return photoAndVideoUrl;
        }

        public void setPhotoAndVideoUrl(String photoAndVideoUrl) {
            this.photoAndVideoUrl = photoAndVideoUrl;
        }

        public String getArticleCommentContent() {
            return articleCommentContent;
        }

        public void setArticleCommentContent(String articleCommentContent) {
            this.articleCommentContent = articleCommentContent;
        }

        public int getReplyCommentId() {
            return replyCommentId;
        }

        public void setReplyCommentId(int replyCommentId) {
            this.replyCommentId = replyCommentId;
        }

        public int getArticleCommentTime() {
            return articleCommentTime;
        }

        public void setArticleCommentTime(int articleCommentTime) {
            this.articleCommentTime = articleCommentTime;
        }

        public int getArticleCommentType() {
            return articleCommentType;
        }

        public void setArticleCommentType(int articleCommentType) {
            this.articleCommentType = articleCommentType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getArticleObjectId() {
            return articleObjectId;
        }

        public void setArticleObjectId(int articleObjectId) {
            this.articleObjectId = articleObjectId;
        }
    }
}
