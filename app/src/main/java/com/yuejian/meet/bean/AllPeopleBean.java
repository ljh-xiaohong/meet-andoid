package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/12/20 14:34
 * @desc :
 */
public class AllPeopleBean implements Serializable {

    /**
     * code : 0
     * data : [{"customerId":727331,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20191010160208286987.png","vipType":1,"userName":"张健"},{"customerId":728304,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20191010160208286987.png","vipType":1,"userName":"许文琼"},{"customerId":728305,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20191010160208286987.png","vipType":0,"userName":null}]
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
         * customerId : 727331
         * photo : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20191010160208286987.png
         * vipType : 1
         * userName : 张健
         */

        private int customerId;
        private String photo;
        private int vipType;
        private String userName;

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
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
