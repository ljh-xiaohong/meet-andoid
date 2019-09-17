package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

public class PosterDetailEntity implements Serializable {


    /**
     * contentLabelList : [{"coverUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg","createTime":1566547185,"des":"","endTime":1566547855,"id":1,"isDelete":false,"joinnumFalse":28,"joinnumTrue":28,"recFlag":false,"sort":3,"startTime":1566547114,"title":"测试标签","type":0},{"coverUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg","createTime":1566547805,"des":"","endTime":1566547800,"id":2,"isDelete":false,"joinnumFalse":28,"joinnumTrue":28,"recFlag":false,"sort":2,"startTime":1566547080,"title":"测试活动标签2","type":0}]
     * createTime : 1567601679
     * discountPrice : 0.0
     * id : 18
     * isDelete : 0
     * labelId : 1,2
     * postersJson : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg
     * postersPrice : 99.0
     * postersTitle : 测99999
     * previewUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909031930523052522330.jpg
     * recFlag : 1
     * templateCollection: true,
     * updateTime : 1567601679
     * usenumFalse : 1
     * usenumTrue : 1
     */

    private int createTime;
    private double discountPrice;
    private int id;
    private int isDelete;
    private String labelId;
    private String postersJson;
    private double postersPrice;
    private String postersTitle;
    private String previewUrl;
    private int recFlag;
    private int updateTime;
    private int usenumFalse;
    private int usenumTrue;
    private boolean templateCollection;
    private List<ContentLabelList> contentLabelList;

    public boolean isTemplateCollection() {
        return templateCollection;
    }

    public void setTemplateCollection(boolean templateCollection) {
        this.templateCollection = templateCollection;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getPostersJson() {
        return postersJson;
    }

    public void setPostersJson(String postersJson) {
        this.postersJson = postersJson;
    }

    public double getPostersPrice() {
        return postersPrice;
    }

    public void setPostersPrice(double postersPrice) {
        this.postersPrice = postersPrice;
    }

    public String getPostersTitle() {
        return postersTitle;
    }

    public void setPostersTitle(String postersTitle) {
        this.postersTitle = postersTitle;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getRecFlag() {
        return recFlag;
    }

    public void setRecFlag(int recFlag) {
        this.recFlag = recFlag;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getUsenumFalse() {
        return usenumFalse;
    }

    public void setUsenumFalse(int usenumFalse) {
        this.usenumFalse = usenumFalse;
    }

    public int getUsenumTrue() {
        return usenumTrue;
    }

    public void setUsenumTrue(int usenumTrue) {
        this.usenumTrue = usenumTrue;
    }

    public List<ContentLabelList> getContentLabelList() {
        return contentLabelList;
    }

    public void setContentLabelList(List<ContentLabelList> contentLabelList) {
        this.contentLabelList = contentLabelList;
    }

    public static class ContentLabelList {
        /**
         * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg
         * createTime : 1566547185
         * des :
         * endTime : 1566547855
         * id : 1
         * isDelete : false
         * joinnumFalse : 28
         * joinnumTrue : 28
         * recFlag : false
         * sort : 3
         * startTime : 1566547114
         * title : 测试标签
         * type : 0
         */

        private String coverUrl;
        private int createTime;
        private String des;
        private int endTime;
        private int id;
        private boolean isDelete;
        private int joinnumFalse;
        private int joinnumTrue;
        private boolean recFlag;
        private int sort;
        private int startTime;
        private String title;
        private int type;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public void setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
        }

        public int getJoinnumFalse() {
            return joinnumFalse;
        }

        public void setJoinnumFalse(int joinnumFalse) {
            this.joinnumFalse = joinnumFalse;
        }

        public int getJoinnumTrue() {
            return joinnumTrue;
        }

        public void setJoinnumTrue(int joinnumTrue) {
            this.joinnumTrue = joinnumTrue;
        }

        public boolean isRecFlag() {
            return recFlag;
        }

        public void setRecFlag(boolean recFlag) {
            this.recFlag = recFlag;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
