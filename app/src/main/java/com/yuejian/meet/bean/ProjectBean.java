package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/21 21:56
 * @desc :
 */
public class ProjectBean {

    /**
     * code : 0
     * data : [{"coverUrl":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019091511153315336316.jpg","createTime":1568605770,"photo":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1533781688_0_shuping.jpg","vipType":0,"id":114,"title":"测试项目","userName":"关羽","content":"脆香米999"}]
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
         * coverUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019091511153315336316.jpg
         * createTime : 1568605770
         * photo : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1533781688_0_shuping.jpg
         * vipType : 0
         * id : 114
         * title : 测试项目
         * userName : 关羽
         * content : 脆香米999
         */

        private String coverUrl;
        private int createTime;
        private String photo;
        private int vipType;
        private int id;
        private String title;
        private String userName;
        private String content;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
            this.vipType = vipType;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
