package com.yuejian.meet.bean;


import java.io.Serializable;
import java.util.List;

public class RecommendEntity implements Serializable {


    /**
     * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561973539721537_0_shuping.jpg
     * labelId : 1,2
     * fabulousNum : 0
     * isPraise : false
     * id : 173
     * title : 我们的生活
     * labelName :
     * type : 1
     * useNumber : 0
     * customerPhoto : ["http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/default1.jpg","http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/default2.jpg","http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/default3.jpg","http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/default4.jpg","http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/default5.jpg"]
     * joinnumTrue : 0
     * coveSizeType: 竖屏-0 横屏-1
     * joinnumFalse : 0
     * sex:1  0:女，1：男
     * name:姓名
     * photo: "https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/default.png",
     */

    private String coverUrl;
    private String labelId;
    private int fabulousNum;
    private boolean isPraise;
    private int id;
    private String title;
    private String labelName;
    private int type;
    private int useNumber;
    private int joinnumTrue;
    private int joinnumFalse;
    private int coveSizeType;
    private List<String> customerPhoto;
    private int sex;
    private String photo;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public int getCoveSizeType() {
        return coveSizeType;
    }

    public void setCoveSizeType(int coveSizeType) {
        this.coveSizeType = coveSizeType;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public int getFabulousNum() {
        return fabulousNum;
    }

    public void setFabulousNum(int fabulousNum) {
        this.fabulousNum = fabulousNum;
    }

    public boolean isIsPraise() {
        return isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUseNumber() {
        return useNumber;
    }

    public void setUseNumber(int useNumber) {
        this.useNumber = useNumber;
    }

    public int getJoinnumTrue() {
        return joinnumTrue;
    }

    public void setJoinnumTrue(int joinnumTrue) {
        this.joinnumTrue = joinnumTrue;
    }

    public int getJoinnumFalse() {
        return joinnumFalse;
    }

    public void setJoinnumFalse(int joinnumFalse) {
        this.joinnumFalse = joinnumFalse;
    }

    public List<String> getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(List<String> customerPhoto) {
        this.customerPhoto = customerPhoto;
    }

}
