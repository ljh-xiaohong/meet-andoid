package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/5 20:34
 * @desc : 家圈 - 关注 - 实体类
 */
public class FamilyFollowEntity implements Serializable{


    /**
     * code : 0
     * data : [{"commentNum":0,"createTime":1567416592,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99276,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567416540,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99275,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567138203,"vipType":0,"fabulousNum":1,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99268,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":9,"createTime":1567134219,"vipType":0,"fabulousNum":-3,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[{"articleCommentContent":"测试动态评论1","articleCommentId":55,"articleCommentPraiseCnt":0,"articleCommentTime":1567222128000,"articleCommentType":4,"articleObjectId":99266,"articleReplyCnt":0,"customerId":500102,"opCustomerId":500103,"replyCommentId":49}],"id":99266,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567066881,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99265,"title":"动态测试标题","content":"这是一条动态"},{"commentNum":0,"createTime":1567063782,"vipType":0,"fabulousNum":0,"name":"测试女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99264,"title":"测试动态","content":"测试动态发布测试动态发布测试动态发布测试动态发布"},{"commentNum":0,"createTime":1567063665,"vipType":0,"fabulousNum":1,"name":"测试女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":99263,"title":"测试动态","content":"测试动态发布测试动态发布测试动态发布测试动态发布"},{"commentNum":0,"createTime":1566958540,"vipType":null,"fabulousNum":0,"name":null,"photo":null,"commentMap":[],"id":26,"title":"大家一起看","content":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/ios_video_audios/1561616407500101_shuping.mp4"},{"commentNum":0,"createTime":1566958540,"vipType":null,"fabulousNum":0,"name":null,"photo":null,"commentMap":[{"articleCommentContent":"2323423","articleCommentId":62,"articleCommentPraiseCnt":0,"articleCommentTime":1567512525000,"articleCommentType":3,"articleObjectId":27,"articleReplyCnt":0,"customerId":500103,"opCustomerId":0,"replyCommentId":0}],"id":27,"title":"我","content":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/ios_video_audios/1561616531500101_shuping.mp4"},{"commentNum":0,"createTime":1566958540,"vipType":0,"fabulousNum":0,"name":"小女子","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132","commentMap":[],"id":29,"title":"title","content":"视频内容label_id=0"}]
     * message : 操作成功
     * result : true
     */

    private int code;
    private String message;
    private boolean result;
    private List<DataBean> data;
    private int totals;
    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }
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
         * commentNum : 0
         * createTime : 1567416592
         * vipType : 0
         * fabulousNum : 0
         * name : 小女子
         * photo : http://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqK2bichxjbASCTNcnZb9hb2m8mvJcibTz4vXNYsXyLrO7FS6rGicPDqVzaBGpdibIUhGSkEZicUqZNxu7A/132
         * commentMap : []
         * id : 99276
         * title : 动态测试标题
         * content : 这是一条动态
         */

        private int commentNum;
        private int createTime;
        private int vipType;
        private int fabulousNum;
        private String name;
        private String photo;
        private int id;
        private int type;
        private String title;
        private String content;
        private List<CommentBean> commentMap;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
            this.vipType = vipType;
        }

        public int getFabulousNum() {
            return fabulousNum;
        }

        public void setFabulousNum(int fabulousNum) {
            this.fabulousNum = fabulousNum;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<CommentBean> getCommentMap() {
            return commentMap;
        }

        public void setCommentMap(List<CommentBean> commentMap) {
            this.commentMap = commentMap;
        }

        public static class CommentBean implements Serializable {

            /**
             * articleCommentContent : 2323423
             * articleCommentId : 62
             * articleCommentTime : 1567512525000
             * articleCommentType : 3
             * articleObjectId : 27
             * articleReplyCnt : 0
             * customerId : 500103
             * opCustomerId : 0
             * replyCommentId : 0
             */

            private String articleCommentContent;
            private int articleCommentId;
            private long articleCommentTime;
            private int articleCommentType;
            private int articleObjectId;
            private int articleReplyCnt;
            private int customerId;
            private int opCustomerId;
            private int replyCommentId;

            public String getArticleCommentContent() {
                return articleCommentContent;
            }

            public void setArticleCommentContent(String articleCommentContent) {
                this.articleCommentContent = articleCommentContent;
            }

            public int getArticleCommentId() {
                return articleCommentId;
            }

            public void setArticleCommentId(int articleCommentId) {
                this.articleCommentId = articleCommentId;
            }

            public long getArticleCommentTime() {
                return articleCommentTime;
            }

            public void setArticleCommentTime(long articleCommentTime) {
                this.articleCommentTime = articleCommentTime;
            }

            public int getArticleCommentType() {
                return articleCommentType;
            }

            public void setArticleCommentType(int articleCommentType) {
                this.articleCommentType = articleCommentType;
            }

            public int getArticleObjectId() {
                return articleObjectId;
            }

            public void setArticleObjectId(int articleObjectId) {
                this.articleObjectId = articleObjectId;
            }

            public int getArticleReplyCnt() {
                return articleReplyCnt;
            }

            public void setArticleReplyCnt(int articleReplyCnt) {
                this.articleReplyCnt = articleReplyCnt;
            }

            public int getCustomerId() {
                return customerId;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public int getOpCustomerId() {
                return opCustomerId;
            }

            public void setOpCustomerId(int opCustomerId) {
                this.opCustomerId = opCustomerId;
            }

            public int getReplyCommentId() {
                return replyCommentId;
            }

            public void setReplyCommentId(int replyCommentId) {
                this.replyCommentId = replyCommentId;
            }
        }
    }
}
