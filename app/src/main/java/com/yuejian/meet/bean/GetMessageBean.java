package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/9/28 15:19
 * @desc :
 */
public class GetMessageBean {

    /**
     * code : 0
     * data : {"readFlag":false,"firendFlag":true}
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
         * readFlag : false
         * firendFlag : true
         */

        private boolean readFlag;
        private boolean firendFlag;

        public boolean isReadFlag() {
            return readFlag;
        }

        public void setReadFlag(boolean readFlag) {
            this.readFlag = readFlag;
        }

        public boolean isFirendFlag() {
            return firendFlag;
        }

        public void setFirendFlag(boolean firendFlag) {
            this.firendFlag = firendFlag;
        }
    }
}
