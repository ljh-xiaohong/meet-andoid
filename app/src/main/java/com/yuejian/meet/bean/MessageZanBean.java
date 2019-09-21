package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 20:20
 * @desc :
 */
public class MessageZanBean {

    /**
     * code : 0
     * data : {"praiseMap":[{"userPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3005411504837504687.jpg","createTime":0,"praiseType":1,"userName":"第五个","objectId":118},{"userPhoto":"http://wx.qlogo.cn/mmopen/vi_32/xGAicesoia4lj3icqInK5DaTU0y2FibIIaibqTHlZaJg95s4LqibWuiaVvoiaNBKZM8d2RbFoYrPibeLGI1N9JjUiaSIElsg/0","createTime":0,"praiseType":1,"userName":"梁东波","objectId":121},{"userPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201809121438383838392405.png","createTime":0,"praiseType":1,"userName":"第五维钱","objectId":122}],"commentMap":[{"userPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","articleCommentContent":"12312","articleCommentTime":1568205653000,"articleCommentType":4,"userName":"测试女子","articleObjectId":26},{"userPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","articleCommentContent":"测试","articleCommentTime":1567737001000,"articleCommentType":3,"userName":"测试女子","articleObjectId":26},{"userPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","articleCommentContent":"测试","articleCommentTime":1567736980000,"articleCommentType":3,"userName":"测试女子","articleObjectId":27},{"userPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","articleCommentContent":"测试","articleCommentTime":1567734772000,"articleCommentType":3,"userName":"测试女子","articleObjectId":28}]}
     * message : 操作成功
     * result : true
     */

    private int code;
    private List<DataBean> data;
    private String message;
    private boolean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
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

    public static class DataBean implements Serializable {
        /**
         * userPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3005411504837504687.jpg
         * createTime : 0
         * praiseType : 1
         * userName : 第五个
         * objectId : 118
         */

        private String userPhoto;
        private String photoAndVideoUrl;
        private int createTime;
        private int praiseType;
        private String userName;
        private int objectId;

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

        public int getPraiseType() {
            return praiseType;
        }

        public void setPraiseType(int praiseType) {
            this.praiseType = praiseType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }

    }
}
