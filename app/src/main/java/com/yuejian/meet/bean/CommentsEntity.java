package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * 动态--评论实体类
 * Created by zh03 on 2017/8/17.
 */

public class CommentsEntity implements Serializable {
    public String comment_time;//": 1502284650000,
    public String sex;//": 1,
    public String photo;//": "",
    public String is_praise;//": 0,
    public String comment_id;//": 1,
    public String action_cnts;//{"praise_cnt": 1,"gift_cnt": 2,"comment_cnt": 8}
    public String reply_list;//": [{"op_customer_id": "300247","surname": "张","name": "喜枫","op_surname": "111","op_name": "111","comment_content": "测试评论内评论的评论","comment_id": 5,"customer_id": 300248}],
    public String surname;//": "杨",
    public String name;//": "锦超",
    public String comment_content;//": "测试动态第一级评论内容",
    public String customer_id;//": 300241,
    public String job;//": "",
    public String family_area;///": "珠海",
    public String age;//": 17,
    public String comment_praise_cnt;//": 1,
    public String reply_cnt;//": 2
    public String article_comment_time;
    public String article_comment_content;
    public String article_comment_praise_cnt;
    public String article_comment_id;
    public String article_reply_cnt;
    public String is_family_master;
    public long is_super=0l;

    public String getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(String is_family_master) {
        this.is_family_master = is_family_master;
    }

    public long getIs_super() {
        return is_super;
    }

    public void setIs_super(long is_super) {
        this.is_super = is_super;
    }

    public boolean isArticleComment = false;

    public String getArticle_reply_cnt() {
        return article_reply_cnt;
    }

    public void setArticle_reply_cnt(String article_reply_cnt) {
        this.article_reply_cnt = article_reply_cnt;
    }

    public String getArticle_comment_time() {
        return article_comment_time;
    }

    public void setArticle_comment_time(String article_comment_time) {
        this.article_comment_time = article_comment_time;
    }

    public String getArticle_comment_content() {
        return article_comment_content;
    }

    public void setArticle_comment_content(String article_comment_content) {
        this.article_comment_content = article_comment_content;
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

    public String getAction_cnts() {
        return action_cnts;
    }

    public void setAction_cnts(String action_cnts) {
        this.action_cnts = action_cnts;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getReply_list() {
        return reply_list;
    }

    public void setReply_list(String reply_list) {
        this.reply_list = reply_list;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getComment_praise_cnt() {
        return comment_praise_cnt;
    }

    public void setComment_praise_cnt(String comment_praise_cnt) {
        this.comment_praise_cnt = comment_praise_cnt;
    }

    public String getReply_cnt() {
        return reply_cnt;
    }

    public void setReply_cnt(String reply_cnt) {
        this.reply_cnt = reply_cnt;
    }
}
