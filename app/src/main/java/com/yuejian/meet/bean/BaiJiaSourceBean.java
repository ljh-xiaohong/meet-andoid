package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/10/8 15:08
 * @desc :
 */
public class BaiJiaSourceBean {

    /**
     * code : 0
     * data : {"image":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062722471247128483.png","friendCount":3,"fansCount":4,"attentionCount":0}
     * message : 操作成功
     * result : true
     */

    private int code;
    private DataBean data;
    private String message;
    private boolean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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
         * image : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062722471247128483.png
         * friendCount : 3
         * fansCount : 4
         * attentionCount : 0
         */

        private String image;
        private int friendCount;
        private int fansCount;
        private int attentionCount;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getFriendCount() {
            return friendCount;
        }

        public void setFriendCount(int friendCount) {
            this.friendCount = friendCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public int getAttentionCount() {
            return attentionCount;
        }

        public void setAttentionCount(int attentionCount) {
            this.attentionCount = attentionCount;
        }
    }
}
