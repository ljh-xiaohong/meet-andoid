package com.yuejian.meet.bean;

import android.support.annotation.NonNull;

import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.letterList.FirstLetterUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 16:33
 * @desc :
 */
public class NewFriendBean {

    /**
     * code : 0
     * data : [{"relationType":1,"photo":"http://wx.qlogo.cn/mmopen/vi_32/CwRqACnfJtCoibFjDXax57558KVWdXiaiaEEMibayrbyujiaHQfz05ricUKZJ4n5IDdJFL8bVJuZV6QNQdrVciaOOxicfg/0","name":"张国良"},{"relationType":1,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1512699981300866_0_shuping.jpg","name":"罗安琪"},{"relationType":1,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3008211506050674219.jpg","name":"郑红"},{"relationType":1,"photo":null,"name":null},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1514277141300542_0_shuping.jpg","name":"第五太阳"},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/3005541505179653823.jpg","name":"第五天"},{"relationType":2,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201809121438383838392405.png","NAME":"第五维钱"}]
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

    public static class DataBean implements Comparable<DataBean> {
        /**
         * relationType : 1
         * photo : http://wx.qlogo.cn/mmopen/vi_32/CwRqACnfJtCoibFjDXax57558KVWdXiaiaEEMibayrbyujiaHQfz05ricUKZJ4n5IDdJFL8bVJuZV6QNQdrVciaOOxicfg/0
         * name : 张国良
         */

        private int relationType;
        private String vipType;
        private int customerId;
        private String photo;
        private String name;



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

        public int getRelationType() {
            return relationType;
        }

        public void setRelationType(int relationType) {
            this.relationType = relationType;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(@NonNull DataBean another) {
            String firstLetter = FirstLetterUtil.getFirstLetter(getName()); // 获取拼音首字母并转成大写
            String anotherFirstLetter=FirstLetterUtil.getFirstLetter(another.getName());
            if (firstLetter.equals("#") && !anotherFirstLetter.equals("#")) {
                return 1;
            } else if (!firstLetter.equals("#") && anotherFirstLetter.equals("#")){
                return -1;
            } else {
                return firstLetter.compareToIgnoreCase(anotherFirstLetter);
            }
        }
    }
}
