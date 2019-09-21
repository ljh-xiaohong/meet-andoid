package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/21 15:08
 * @desc :
 */
public class VideoAndArticleBean {

    /**
     * code : 0
     * data : [{"coverUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20190911142704274851469.png","labelId":"19","fabulousNum":0,"isPraise":false,"id":99512,"type":2,"title":"标签一","labelName":"#故宫"}]
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
         * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/20190911142704274851469.png
         * labelId : 19
         * fabulousNum : 0
         * isPraise : false
         * id : 99512
         * type : 2
         * title : 标签一
         * labelName : #故宫
         */

        private String coverUrl;
        private String labelId;
        private int fabulousNum;
        private boolean isPraise;
        private int id;
        private int type;
        private String title;
        private String labelName;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
    }
}
