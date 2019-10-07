package com.yuejian.meet.bean;

/**
 * @author : ljh
 * @time : 2019/9/26 22:13
 * @desc :
 */
public class PayBean {

    /**
     * code : 0
     * data : alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2017063007611861&biz_content=%7B%22body%22%3A%22%E7%99%BE%E5%AE%B6%E5%A7%93%E6%B0%8F%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%22201909261524302430%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E7%99%BE%E5%AE%B6%E5%A7%93%E6%B0%8F%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%227.00%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=%22http%3A%2F%2F2681f7j945.qicp.vip%3A41793%2Fyuejian-app%2Fapi%2FshopOrder%2FaliPayNotify%22%3B&sign=XBs4Yx%2FmbLtUk2XsI00DxdgUHfu2GgC1XbXWk38DufDnYJKQCkI52VzoGER6fCqQUB6c%2BFvfkrJRi76%2B1GvRYIS5D8Ku5vr4FmSHfyJEinyN2%2FO36yf8tkZX5O7S%2FV53WOxdCAxXz1LV0aI1atKBeQthEU7BukrFWEgP8O3%2BxO8I2fiTcfNvB06uf0F%2BMk9gAN25BBjQ92RRLcCq9Nob4I2rCDLBEH9Vz9XDq4uYm5XSVDnCPC%2FI8ljGcU3NjeuHAnmPXRWVC%2BkqP1N%2FmVEU41GGWz1t2sPLwqE8yuvV%2B%2BydH0GnuJXys5qjXCe7xePqaFnqHQSyGxmdPF%2BNY01P6w%3D%3D&sign_type=RSA2&timestamp=2019-09-26+15%3A24%3A30&version=1.0
     * message : 操作成功
     * result : true
     */

    private int code;
    private String data;
    private String message;
    private boolean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
