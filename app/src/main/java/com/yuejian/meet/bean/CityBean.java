package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/22 02:21
 * @desc :
 */
public class CityBean {

    /**
     * code : 0
     * data : [{"city":"广州市"},{"city":"韶关市"},{"city":"深圳市"},{"city":"珠海市"},{"city":"汕头市"},{"city":"佛山市"},{"city":"江门市"},{"city":"湛江市"},{"city":"茂名市"},{"city":"肇庆市"},{"city":"惠州市"},{"city":"梅州市"},{"city":"汕尾市"},{"city":"河源市"},{"city":"阳江市"},{"city":"清远市"},{"city":"东莞市"},{"city":"中山市"},{"city":"潮州市"},{"city":"揭阳市"},{"city":"云浮市"}]
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
         * city : 广州市
         */

        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
