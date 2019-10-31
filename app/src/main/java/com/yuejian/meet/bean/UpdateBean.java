package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/10/30 14:16
 * @desc :
 */
public class UpdateBean {

    /**
     * code : 0
     * data : {"phoneType":2,"size":"SDFGSDFG","issueTime":1572403307,"appUrl":"qwerqwer","id":29,"versionName":"6.0","content":"erqwerqwe","isForced":0,"isShow":1}
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
         * phoneType : 2
         * size : SDFGSDFG
         * issueTime : 1572403307
         * appUrl : qwerqwer
         * id : 29
         * versionName : 6.0
         * content : erqwerqwe
         * isForced : 0
         * isShow : 1
         */

        private int phoneType;
        private String size;
        private int issueTime;
        private String appUrl;
        private int id;
        private String versionName;
        private String content;
        private int isForced;
        private int isShow;

        public int getPhoneType() {
            return phoneType;
        }

        public void setPhoneType(int phoneType) {
            this.phoneType = phoneType;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getIssueTime() {
            return issueTime;
        }

        public void setIssueTime(int issueTime) {
            this.issueTime = issueTime;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsForced() {
            return isForced;
        }

        public void setIsForced(int isForced) {
            this.isForced = isForced;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }
}
