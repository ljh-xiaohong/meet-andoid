package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

public class ActivityLabEntity implements Serializable {


    /**
     * label : {"coverUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg","des":"测试活动标签描述10","id":15,"title":"测试活动标签10","isFocus":false}
     * content : [{"createTime":0,"fabulousNum":2,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1562060703500115_0_shuping.jpg","id":175,"title":"哈哈哈哈哈h","type":4,"isPraise":false,"labelName":"测试活动标签10"},{"createTime":0,"fabulousNum":1,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1562060889500115_0_shuping.jpg","id":176,"isPraise":false,"title":"巴拉巴拉小魔仙","type":4,"labelName":"测试活动标签10"},{"createTime":0,"fabulousNum":1,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1562138122500115_0_shuping.jpg","id":177,"isPraise":false,"title":"观后感烦烦烦","type":2,"labelName":"测试活动标签10"},{"createTime":0,"fabulousNum":0,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/5000781562153274081.jpg","id":178,"title":"个","isPraise":false,"type":4,"labelName":"测试活动标签10"},{"createTime":0,"fabulousNum":0,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/5000781562156694074.jpg","id":179,"title":"啊","type":4,"isPraise":false,"labelName":"测试活动标签10"},{"createTime":0,"fabulousNum":0,"coverPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1562394649500115_0_shuping.jpg","id":180,"title":"哥哥哥哥哥哥哥哥","type":4,"isPraise":false,"labelName":"测试活动标签10"}]
     */

    private Label label;
    private List<Content> content;

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public static class Label {
        /**
         * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1561550994500077_0_shuping.jpg
         * des : 测试活动标签描述10
         * id : 15
         * title : 测试活动标签10
         * isFocus : false
         */

        private String coverUrl;
        private String des;
        private int id;
        private String title;
        private boolean isFocus;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
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

        public boolean isIsFocus() {
            return isFocus;
        }

        public void setIsFocus(boolean isFocus) {
            this.isFocus = isFocus;
        }
    }

    public static class Content {
        /**
         * createTime : 0
         * fabulousNum : 2
         * coverPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1562060703500115_0_shuping.jpg
         * id : 175
         * title : 哈哈哈哈哈h
         * type : 4
         * coveSizeType: 竖屏-0 横屏-1
         * isPraise : false
         * labelName : 测试活动标签10
         */

        private int createTime;
        private int fabulousNum;
        private String coverPhoto;
        private int id;
        private String title;
        private int type;
        private boolean isPraise;
        private String labelName;
        private int coveSizeType;

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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getFabulousNum() {
            return fabulousNum;
        }

        public void setFabulousNum(int fabulousNum) {
            this.fabulousNum = fabulousNum;
        }

        public String getCoverPhoto() {
            return coverPhoto;
        }

        public void setCoverPhoto(String coverPhoto) {
            this.coverPhoto = coverPhoto;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isIsPraise() {
            return isPraise;
        }

        public void setIsPraise(boolean isPraise) {
            this.isPraise = isPraise;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }
    }
}
