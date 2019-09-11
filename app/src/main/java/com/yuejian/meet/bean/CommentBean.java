package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/27 19:59
 * @desc : 评论实体类
 */
public class CommentBean {

    /**
     * code : 0
     * data : [{"createTime":1567222128000,"replyCommentId":49,"opName":"测试女子","customerId":500102,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","comment":"测试动态评论1","id":55,"opPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","type":4,"opCustomerId":500103},{"createTime":1567221345000,"replyCommentId":47,"opName":"小女子","customerId":500103,"name":"测试女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","comment":"测试动态评论回复内容","id":48,"opPhoto":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","type":4,"opCustomerId":500102},{"createTime":1567221142000,"replyCommentId":"","opName":"","customerId":500102,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","comment":"测试动态评论","id":47,"opPhoto":"","type":3,"opCustomerId":""}]
     * message : 操作成功
     * result : true
     * totals : 0
     */

    private int code;
    private String message;
    private boolean result;
    private int totals;
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

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * createTime : 1567222128000
         * replyCommentId : 49
         * opName : 测试女子
         * customerId : 500102
         * name : 小女子
         * photo : http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132
         * comment : 测试动态评论1
         * id : 55
         * opPhoto : http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132
         * type : 4
         * opCustomerId : 500103
         */

        private long createTime;
        private int replyCommentId;
        private String opName;
        private int customerId;
        private String name;
        private String photo;
        private String comment;
        private int id;
        private String opPhoto;
        private int type;
        private int opCustomerId;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getReplyCommentId() {
            return replyCommentId;
        }

        public void setReplyCommentId(int replyCommentId) {
            this.replyCommentId = replyCommentId;
        }

        public String getOpName() {
            return opName;
        }

        public void setOpName(String opName) {
            this.opName = opName;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOpPhoto() {
            return opPhoto;
        }

        public void setOpPhoto(String opPhoto) {
            this.opPhoto = opPhoto;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOpCustomerId() {
            return opCustomerId;
        }

        public void setOpCustomerId(int opCustomerId) {
            this.opCustomerId = opCustomerId;
        }
    }
}
