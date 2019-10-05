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
         * articleCommentId :
         * articleCommentContent :
         * createTime : 1504838842
         * messageId : 641
         * msgPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1504749630371.jpg
         * photo : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1514277141300542_0_shuping.jpg
         * userName : 第五太阳
         * articleObjectId :
         */

        private String articleCommentId;
        private String articleCommentContent;
        private String createTime;
        private String messageId;
        private String msgPhoto;
        private String photo;
        private String userName;
        private String articleObjectId;

        public String getArticleCommentId() {
            return articleCommentId;
        }

        public void setArticleCommentId(String articleCommentId) {
            this.articleCommentId = articleCommentId;
        }

        public String getArticleCommentContent() {
            return articleCommentContent;
        }

        public void setArticleCommentContent(String articleCommentContent) {
            this.articleCommentContent = articleCommentContent;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getMsgPhoto() {
            return msgPhoto;
        }

        public void setMsgPhoto(String msgPhoto) {
            this.msgPhoto = msgPhoto;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getArticleObjectId() {
            return articleObjectId;
        }

        public void setArticleObjectId(String articleObjectId) {
            this.articleObjectId = articleObjectId;
        }
    }
}
