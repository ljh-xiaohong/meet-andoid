package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 16:33
 * @desc :
 */
public class PushProjectBean {

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
         * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201910101047034734721.jpg
         * userPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1505099409300670_0_shuping.jpg
         * vipType : 1
         * id : 113
         * userName : 颜琦
         * title : 爱的广告全套VI设计
         * content : VI系统设计vi设计企业VI餐饮VIvi应用系统VI全套设\n行业：餐饮行业 \n色系：彩色系 黑白系 红色系 \n类型：VI设计（全套）}]
         */

        private String coverUrl;
        private String userPhoto;
        private String vipType;
        private int id;
        private String userName;
        private String title;
        private String content;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
