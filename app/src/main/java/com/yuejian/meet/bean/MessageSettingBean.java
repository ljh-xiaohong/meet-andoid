package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/9/19 21:14
 * @desc :
 */
public class MessageSettingBean {

    /**
     * code : 0
     * data : {"attentionIndustry":"","customerId":500103,"dynamicFlag":true,"forbiddenFollow":0,"goodsFlag":0,"id":9,"pushAddress":"","showDynamic":0,"updateTime":1568706512,"userFlag":0}
     * message : 获取成功
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
         * attentionIndustry :
         * customerId : 500103
         * dynamicFlag : true
         * forbiddenFollow : 0
         * goodsFlag : 0
         * id : 9
         * pushAddress :
         * showDynamic : 0
         * updateTime : 1568706512
         * userFlag : 0
         */

        private String industryName;
        private String attentionIndustry;
        private int customerId;
        private boolean dynamicFlag;
        private int forbiddenFollow;
        private int goodsFlag;
        private int id;
        private String pushAddress;
        private int showDynamic;
        private int updateTime;
        private int userFlag;

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public String getAttentionIndustry() {
            return attentionIndustry;
        }

        public void setAttentionIndustry(String attentionIndustry) {
            this.attentionIndustry = attentionIndustry;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public boolean isDynamicFlag() {
            return dynamicFlag;
        }

        public void setDynamicFlag(boolean dynamicFlag) {
            this.dynamicFlag = dynamicFlag;
        }

        public int getForbiddenFollow() {
            return forbiddenFollow;
        }

        public void setForbiddenFollow(int forbiddenFollow) {
            this.forbiddenFollow = forbiddenFollow;
        }

        public int getGoodsFlag() {
            return goodsFlag;
        }

        public void setGoodsFlag(int goodsFlag) {
            this.goodsFlag = goodsFlag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPushAddress() {
            return pushAddress;
        }

        public void setPushAddress(String pushAddress) {
            this.pushAddress = pushAddress;
        }

        public int getShowDynamic() {
            return showDynamic;
        }

        public void setShowDynamic(int showDynamic) {
            this.showDynamic = showDynamic;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserFlag() {
            return userFlag;
        }

        public void setUserFlag(int userFlag) {
            this.userFlag = userFlag;
        }
    }
}
