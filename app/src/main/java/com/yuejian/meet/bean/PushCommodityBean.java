package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/28 21:01
 * @desc :
 */
public class PushCommodityBean {

    /**
     * code : 0
     * data : [{"gName":"人与人推特瑞特","gId":5,"price":6,"gPriceVip":6,"gPriceOriginal":6,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/goods/201909171057355735677636.png"},{"gName":"有65已4","gId":4,"price":5,"gPriceVip":5,"gPriceOriginal":5,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/goods/20190917160643643921932.jpg"},{"gName":"3外国人","gId":3,"price":5,"gPriceVip":6,"gPriceOriginal":4,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/goods/2019091618040343363412.png"}]
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
         * gName : 人与人推特瑞特
         * gId : 5
         * price : 6.0
         * gPriceVip : 6.0
         * gPriceOriginal : 6.0
         * photo : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/goods/201909171057355735677636.png
         */

        private String gName;
        private int gId;
        private double price;
        private double gPriceVip;
        private double gPriceOriginal;
        private String photo;

        public String getGName() {
            return gName;
        }

        public void setGName(String gName) {
            this.gName = gName;
        }

        public int getGId() {
            return gId;
        }

        public void setGId(int gId) {
            this.gId = gId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getGPriceVip() {
            return gPriceVip;
        }

        public void setGPriceVip(double gPriceVip) {
            this.gPriceVip = gPriceVip;
        }

        public double getGPriceOriginal() {
            return gPriceOriginal;
        }

        public void setGPriceOriginal(double gPriceOriginal) {
            this.gPriceOriginal = gPriceOriginal;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
