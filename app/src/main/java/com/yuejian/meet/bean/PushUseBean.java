package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 16:33
 * @desc :
 */
public class PushUseBean {

    /**
     * code : 0
     * data : [{"relationType":1,"photo":"http://wx.qlogo.cn/mmopen/vi_32/CwRqACnfJtCoibFjDXax57558KVWdXiaiaEEMibayrbyujiaHQfz05ricUKZJ4n5IDdJFL8bVJuZV6QNQdrVciaOOxicfg/0","NAME":"张国良"},{"relationType":1,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1512699981300866_0_shuping.jpg","NAME":"罗安琪"},{"relationType":1,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3008211506050674219.jpg","NAME":"郑红"},{"relationType":1,"photo":null,"NAME":null},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1514277141300542_0_shuping.jpg","NAME":"第五太阳"},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3005541505179653823.jpg","NAME":"第五天"},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201809121438383838392405.png","NAME":"第五维钱"}]
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
         * userPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3005411504837504687.jpg
         * lastLoginCity : null
         * isConcern : 0
         * customerId : 300541
         * vipType : 0
         * userName : 第五个
         */

        private String userPhoto;
        private Object lastLoginCity;
        private String isConcern;
        private int customerId;
        private String vipType;
        private String userName;

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public Object getLastLoginCity() {
            return lastLoginCity;
        }

        public void setLastLoginCity(Object lastLoginCity) {
            this.lastLoginCity = lastLoginCity;
        }

        public String getIsConcern() {
            return isConcern;
        }

        public void setIsConcern(String isConcern) {
            this.isConcern = isConcern;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getVipType() {
            return vipType;
        }

        public void setVipType(String vipType) {
            this.vipType = vipType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
