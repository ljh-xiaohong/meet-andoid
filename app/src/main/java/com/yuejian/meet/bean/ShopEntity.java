package com.yuejian.meet.bean;

import java.io.Serializable;

public class ShopEntity implements Serializable {


    /**
     * gDes : 测试商品描述
     * gName : 测试商品1111
     * gPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909101510551055974218.jpg
     * gPrice : 30
     * gPriceOriginal : 20
     * gPriceVip : 25
     * gid : 1
     */

    private String gDes;
    private String gName;
    private String gPhoto;
    private double gPrice;
    private double gPriceOriginal;
    private double gPriceVip;
    private int gid;
    private boolean isClick;

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getGDes() {
        return gDes;
    }

    public void setGDes(String gDes) {
        this.gDes = gDes;
    }

    public String getGName() {
        return gName;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public String getGPhoto() {
        return gPhoto;
    }

    public void setGPhoto(String gPhoto) {
        this.gPhoto = gPhoto;
    }

    public double getGPrice() {
        return gPrice;
    }

    public void setGPrice(double gPrice) {
        this.gPrice = gPrice;
    }

    public double getGPriceOriginal() {
        return gPriceOriginal;
    }

    public void setGPriceOriginal(int gPriceOriginal) {
        this.gPriceOriginal = gPriceOriginal;
    }

    public double getGPriceVip() {
        return gPriceVip;
    }

    public void setGPriceVip(int gPriceVip) {
        this.gPriceVip = gPriceVip;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
}
