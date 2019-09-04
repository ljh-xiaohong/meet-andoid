package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/9/3 20:38
 * @desc :
 */
public class ZanBean implements Serializable {

    /**
     * code : 0
     * data : {"isPraise":1,"praiseCnt":1}
     * message : 操作成功
     * result : true
     * totals : 0
     */

    private int code;
    private DataBean data;
    private String message;
    private boolean result;
    private int totals;

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

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public static class DataBean implements Serializable {
        /**
         * isPraise : 1
         * praiseCnt : 1
         */

        private int isPraise;
        private int praiseCnt;

        public int getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(int isPraise) {
            this.isPraise = isPraise;
        }

        public int getPraiseCnt() {
            return praiseCnt;
        }

        public void setPraiseCnt(int praiseCnt) {
            this.praiseCnt = praiseCnt;
        }
    }
}
