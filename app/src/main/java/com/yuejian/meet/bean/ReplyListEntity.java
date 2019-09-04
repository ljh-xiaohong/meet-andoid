package com.yuejian.meet.bean;

/**
 * 查看的用户动态实体类
 * Created by zh03 on 2017/8/17.
 */

public class ReplyListEntity {
    public String comment_time;//":1503048079000,
    public String sex;//":1,
    public String photo;//":"http:\/\/wx.qlogo.cn\/mmopen\/UK9RSMOAw5ibvHGGPeTd1rBQILKg1EzlRA8icia5mD1gauNib38MRpvRYOpf3R0CCibgicL1t0B8K2ugGCoIqbmutEST4J8cJIF3GM\/0",
    public String is_praise;//":0,
    public String comment_id;//":59,
    public String comment_content;//":"uuu",
    public String job;//":"鸡鸡",
    public String family_area;//":"珠海",
    public String age;//":25,
    public String comment_praise_cnt;//":0
    public String article_comment_content;//": "吉林",
    public String op_customer_id;//": "",
    public String surname;//": "陈",
    public String name;//": "嘉俊",
    public String op_surname;//": "",
    public String op_name;//": "",
    public String customer_id;//": 300258,
    public String article_comment_id;//": 9
    public boolean isArticleReply = false;
    public String article_comment_time;//": 1503822872000,
    public String article_comment_praise_cnt;//": 0,
    public Long is_super=0l;

    public boolean isArticleReply() {
        return isArticleReply;
    }

    public void setArticleReply(boolean articleReply) {
        isArticleReply = articleReply;
    }

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getArticle_comment_time() {
        return article_comment_time;
    }

    public void setArticle_comment_time(String article_comment_time) {
        this.article_comment_time = article_comment_time;
    }

    public String getArticle_comment_praise_cnt() {
        return article_comment_praise_cnt;
    }

    public void setArticle_comment_praise_cnt(String article_comment_praise_cnt) {
        this.article_comment_praise_cnt = article_comment_praise_cnt;
    }

    public String getArticle_comment_content() {
        return article_comment_content;
    }

    public void setArticle_comment_content(String article_comment_content) {
        this.article_comment_content = article_comment_content;
    }

    public String getArticle_comment_id() {
        return article_comment_id;
    }

    public void setArticle_comment_id(String article_comment_id) {
        this.article_comment_id = article_comment_id;
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

    public String getOp_name() {
        return op_name;
    }

    public void setOp_name(String op_name) {
        this.op_name = op_name;
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

    public String getOp_customer_id() {
        return op_customer_id;
    }

    public void setOp_customer_id(String op_customer_id) {
        this.op_customer_id = op_customer_id;
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

    public String getOp_surname() {
        return op_surname;
    }

    public void setOp_surname(String op_surname) {
        this.op_surname = op_surname;
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
}
