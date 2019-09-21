package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/21 20:47
 * @desc :
 */
public class CommodityBean {

    /**
     * code : 0
     * data : [{"gPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101510551055974218.jpg","gName":"测试商品1111","gId":1,"gPriceVip":25,"gPriceOriginal":20}]
     * message : 搜索成功
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
         * gPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101510551055974218.jpg
         * gName : 测试商品1111
         * gId : 1
         * gPriceVip : 25.0
         * gPriceOriginal : 20.0
         */

        private String gPhoto;
        private String gName;
        private int gId;
        private double gPriceVip;
        private double gPriceOriginal;

        public String getGPhoto() {
            return gPhoto;
        }

        public void setGPhoto(String gPhoto) {
            this.gPhoto = gPhoto;
        }

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
    }
}
