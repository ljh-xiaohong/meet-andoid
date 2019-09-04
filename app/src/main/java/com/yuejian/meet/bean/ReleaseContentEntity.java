package com.yuejian.meet.bean;

import java.util.List;

/**
 * @author : g000gle
 * @desc : 首页内容详情类
 */
public class ReleaseContentEntity {

    public List<Comments> comments;
    public Advertising advertising;
    public Creation creation;

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Advertising getAdvertising() {
        return advertising;
    }

    public void setAdvertising(Advertising advertising) {
        this.advertising = advertising;
    }

    public Creation getCreation() {
        return creation;
    }

    public void setCreation(Creation creation) {
        this.creation = creation;
    }

    public static class Comments {
        public String article_comment_content;
        public String article_reply_cnt;
        public String name;
        public String article_comment_time;
        public String photo;
        public String is_praise;
        public String customer_id;
        public String job;
        public String family_area;
        public String article_comment_praise_cnt;
        public String article_comment_id;
        public List<Reply_list> reply_list;

        public String getArticle_comment_content() {
            return article_comment_content;
        }

        public void setArticle_comment_content(String article_comment_content) {
            this.article_comment_content = article_comment_content;
        }

        public String getArticle_reply_cnt() {
            return article_reply_cnt;
        }

        public void setArticle_reply_cnt(String article_reply_cnt) {
            this.article_reply_cnt = article_reply_cnt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArticle_comment_time() {
            return article_comment_time;
        }

        public void setArticle_comment_time(String article_comment_time) {
            this.article_comment_time = article_comment_time;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getIs_praise() {
            return is_praise;
        }

        public void setIs_praise(String is_praise) {
            this.is_praise = is_praise;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getFamily_area() {
            return family_area;
        }

        public void setFamily_area(String family_area) {
            this.family_area = family_area;
        }

        public String getArticle_comment_praise_cnt() {
            return article_comment_praise_cnt;
        }

        public void setArticle_comment_praise_cnt(String article_comment_praise_cnt) {
            this.article_comment_praise_cnt = article_comment_praise_cnt;
        }

        public String getArticle_comment_id() {
            return article_comment_id;
        }

        public void setArticle_comment_id(String article_comment_id) {
            this.article_comment_id = article_comment_id;
        }

        public List<Reply_list> getReply_list() {
            return reply_list;
        }

        public void setReply_list(List<Reply_list> reply_list) {
            this.reply_list = reply_list;
        }
    }

    public static class Reply_list {
        String article_comment_content;
        String op_customer_id;
        String article_object_id;
        String name;
        String op_name;
        String customer_id;
        String article_comment_id;

        public String getArticle_comment_content() {
            return article_comment_content;
        }

        public void setArticle_comment_content(String article_comment_content) {
            this.article_comment_content = article_comment_content;
        }

        public String getOp_customer_id() {
            return op_customer_id;
        }

        public void setOp_customer_id(String op_customer_id) {
            this.op_customer_id = op_customer_id;
        }

        public String getArticle_object_id() {
            return article_object_id;
        }

        public void setArticle_object_id(String article_object_id) {
            this.article_object_id = article_object_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOp_name() {
            return op_name;
        }

        public void setOp_name(String op_name) {
            this.op_name = op_name;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getArticle_comment_id() {
            return article_comment_id;
        }

        public void setArticle_comment_id(String article_comment_id) {
            this.article_comment_id = article_comment_id;
        }
    }

    public static class Advertising {
        public String image;
        public String title;
        public String content;


        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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
    }

    public static class Creation {
        public int comment_num;
        public long create_time;
        public boolean is_bind;
        public int is_reprint;
        public String photo;
        public int is_praise;
        public String title;
        public String vip_deploy_id;
        public String content;
        public String music_url;
        public String vip_type;
        public String name;
        public int is_comment;
        public int fabulous_num;
        public int id;
        public int customer_id;
        public String job;
        public String label_name;
        public int label_id;
        public String photo_and_video_url;

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public boolean isIs_bind() {
            return is_bind;
        }

        public void setIs_bind(boolean is_bind) {
            this.is_bind = is_bind;
        }

        public int getIs_reprint() {
            return is_reprint;
        }

        public void setIs_reprint(int is_reprint) {
            this.is_reprint = is_reprint;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int isIs_praise() {
            return is_praise;
        }

        public void setIs_praise(int is_praise) {
            this.is_praise = is_praise;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVip_deploy_id() {
            return vip_deploy_id;
        }

        public void setVip_deploy_id(String vip_deploy_id) {
            this.vip_deploy_id = vip_deploy_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMusic_url() {
            return music_url;
        }

        public void setMusic_url(String music_url) {
            this.music_url = music_url;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_comment() {
            return is_comment;
        }

        public void setIs_comment(int is_comment) {
            this.is_comment = is_comment;
        }

        public int getFabulous_num() {
            return fabulous_num;
        }

        public void setFabulous_num(int fabulous_num) {
            this.fabulous_num = fabulous_num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }

        public int getLabel_id() {
            return label_id;
        }

        public void setLabel_id(int label_id) {
            this.label_id = label_id;
        }

        public String getPhoto_and_video_url() {
            return photo_and_video_url;
        }

        public void setPhoto_and_video_url(String photo_and_video_url) {
            this.photo_and_video_url = photo_and_video_url;
        }
    }
}
