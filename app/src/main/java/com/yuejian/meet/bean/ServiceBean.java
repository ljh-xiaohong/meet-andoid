package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/10/7 14:12
 * @desc :
 */
public class ServiceBean {

    /**
     * code : 0
     * data : [{"incomeFlag":0,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/fy8WZDEHsX.png","id":3,"userName":"刘志维","opCustomerId":10052},{"incomeFlag":1,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/fy8WZDEHsX.png","id":2,"userName":"刘志维","opCustomerId":10051}]
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
         * incomeFlag : 0
         * photo : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/fy8WZDEHsX.png
         * id : 3
         * userName : 刘志维
         * opCustomerId : 10052
         */

        private int incomeFlag;
        private String photo;
        private int id;
        private String userName;
        private int opCustomerId;

        public int getIncomeFlag() {
            return incomeFlag;
        }

        public void setIncomeFlag(int incomeFlag) {
            this.incomeFlag = incomeFlag;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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

        public int getOpCustomerId() {
            return opCustomerId;
        }

        public void setOpCustomerId(int opCustomerId) {
            this.opCustomerId = opCustomerId;
        }
    }
}
