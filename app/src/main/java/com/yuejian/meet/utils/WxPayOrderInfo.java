package com.yuejian.meet.utils;

/**
 * Created by zh02 on 2017/7/24.
 */

public class WxPayOrderInfo {
    public String timeStamp;//": "1500881783",
    public String paySign;//": "F5CFDCA5189F662C6F53CF86FC1966DD",
    public String appId;//": "wx457b71ba948f85c0",
    public String signType;//": "MD5",
    public String result_code;//": "SUCCESS",
    public String nonceStr;//": "lSP7Bw7kUG0lxZwp",
    public String prepay_id;//": "wx20170724153625aae1bfbaf80303909748"


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }
}
