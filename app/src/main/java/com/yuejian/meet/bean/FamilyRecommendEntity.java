package com.yuejian.meet.bean;

import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/5 20:34
 * @desc : 家圈 - 推荐 - 实体类
 */
public class FamilyRecommendEntity {

    private String comment_num;
    private String create_time;
    private String article_comment_time;
    private String is_reprint;
    private String photo;
    private String is_praise;
    private String title;
    private String type;
    private String vip_deploy_id;
    private String content;
    public List<Content> contents;
    private String is_delete;
    private String third_photo;
    private String article_comment_content;
    private String music_url;
    private String is_audit;
    private String heart_num;
    private String vip_type;
    private String is_comment;
    private String fabulous_num;
    private String name;
    private String id;
    private String customer_id;
    private String photo_and_video_url;
    private String praise_type;

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getArticle_comment_time() {
        return article_comment_time;
    }

    public void setArticle_comment_time(String article_comment_time) {
        this.article_comment_time = article_comment_time;
    }

    public String getIs_reprint() {
        return is_reprint;
    }

    public void setIs_reprint(String is_reprint) {
        this.is_reprint = is_reprint;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getThird_photo() {
        return third_photo;
    }

    public void setThird_photo(String third_photo) {
        this.third_photo = third_photo;
    }

    public String getArticle_comment_content() {
        return article_comment_content;
    }

    public void setArticle_comment_content(String article_comment_content) {
        this.article_comment_content = article_comment_content;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public String getHeart_num() {
        return heart_num;
    }

    public void setHeart_num(String heart_num) {
        this.heart_num = heart_num;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public String getFabulous_num() {
        return fabulous_num;
    }

    public void setFabulous_num(String fabulous_num) {
        this.fabulous_num = fabulous_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhoto_and_video_url() {
        return photo_and_video_url;
    }

    public void setPhoto_and_video_url(String photo_and_video_url) {
        this.photo_and_video_url = photo_and_video_url;
    }

    public String getPraise_type() {
        return praise_type;
    }

    public void setPraise_type(String praise_type) {
        this.praise_type = praise_type;
    }

    public static class Content {
        String content;
        String type;
        String index;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }
}
