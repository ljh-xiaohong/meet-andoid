package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/10/11 11:35
 * @desc :
 */
public class MessageDetailBean {

    /**
     * code : 0
     * data : {"msgRemark2":null,"msgType":5,"createTime":1504751041000,"customerId":723495,"msgPhoto":null,"msgRemark":"1","title":"您提交的营业执照认证已通过审核","opCustomerId":null,"objectId":8}
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
         * msgRemark2 : null
         * msgType : 5
         * createTime : 1504751041000
         * customerId : 723495
         * msgPhoto : null
         * msgRemark : 1
         * title : 您提交的营业执照认证已通过审核
         * opCustomerId : null
         * objectId : 8
         */

        private String msgRemark2;
        private int msgType;
        private long createTime;
        private int customerId;
        private String msgPhoto;
        private String msgRemark;
        private String title;
        private String opCustomerId;
        private int objectId;

        public String getMsgRemark2() {
            return msgRemark2;
        }

        public void setMsgRemark2(String msgRemark2) {
            this.msgRemark2 = msgRemark2;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getMsgPhoto() {
            return msgPhoto;
        }

        public void setMsgPhoto(String msgPhoto) {
            this.msgPhoto = msgPhoto;
        }

        public String getMsgRemark() {
            return msgRemark;
        }

        public void setMsgRemark(String msgRemark) {
            this.msgRemark = msgRemark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOpCustomerId() {
            return opCustomerId;
        }

        public void setOpCustomerId(String opCustomerId) {
            this.opCustomerId = opCustomerId;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }
    }
}
