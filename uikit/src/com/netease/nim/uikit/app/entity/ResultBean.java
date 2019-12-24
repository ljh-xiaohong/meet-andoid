package com.netease.nim.uikit.app.entity;


/**
 * @author : ljh
 * @time : 2019/9/2 16:21
 * @desc :
 */
public class ResultBean {


    /**
     * code : -10001
     * data : {"customer_id":500171}
     * message : 请先绑定手机
     * result : false
     * totals : 0
     */

    private int code;
    private Object data;
    private String message;
    private boolean result;
    private int totals;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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

}
