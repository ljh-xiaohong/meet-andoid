package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/22 02:12
 * @desc :
 */
public class ProvinceBean {


    /**
     * code : 0
     * data : [{"province":"北京市"},{"province":"天津市"},{"province":"河北省"},{"province":"山西省"},{"province":"内蒙古自治区"},{"province":"辽宁省"},{"province":"吉林省"},{"province":"黑龙江省"},{"province":"上海市"},{"province":"江苏省"},{"province":"浙江省"},{"province":"安徽省"},{"province":"福建省"},{"province":"江西省"},{"province":"山东省"},{"province":"河南省"},{"province":"湖北省"},{"province":"湖南省"},{"province":"广东省"},{"province":"广西壮族自治区"},{"province":"海南省"},{"province":"重庆市"},{"province":"四川省"},{"province":"贵州省"},{"province":"云南省"},{"province":"西藏自治区"},{"province":"陕西省"},{"province":"甘肃省"},{"province":"青海省"},{"province":"宁夏回族自治区"},{"province":"新疆维吾尔自治区"},{"province":"香港特别行政区"},{"province":"澳门特别行政区"},{"province":"台湾省"}]
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
         * province : 北京市
         */

        private String province;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
