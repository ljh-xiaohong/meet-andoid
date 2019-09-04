package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zh02 on 2017/8/25.
 */

public class Article implements Serializable {
    public String article_comment_cnt;//": 0,
    public String article_type;//": 1,
    public String create_time;//": 1503624495000,
    public String article_gift_cnt;//": 0,
    public String article_from;//": "百度百科",
    public String c_surname;//": "陈",
    public String article_url;//": "https://wapbaike.baidu.com/item/%E8%B5%B5%E5%A7%93/280326?bk_fr=chain_bottom&fromtitle=%E8%B5%B5&fromid=1278582",
    public String article_photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/pMtaS8B6ZE.png",
    public String article_id;//": 26,
    public String article_title;//": "赵姓起源",
    public String article_status;//": 1,
    public String surname;//": "赵",
    public String article_praise_cnt;//": 0,
    public String c_name;//": "嘉俊",
    public String customer_id;//": 300258
    public String relation_type;
    public String photo;
    public Long is_super=0l;

    public List<Reward> reward_list;//": [],
    public String sex;//": 1,
    public String is_family_master;//": 0,
    public String is_praise;//": 1,
    public String family_area_name;//": "中山",
    public String job;//": "歌手",
    public String age;//": 25

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public List<Reward> getReward_list() {
        return reward_list;
    }

    public void setReward_list(List<Reward> reward_list) {
        this.reward_list = reward_list;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(String is_family_master) {
        this.is_family_master = is_family_master;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getFamily_area_name() {
        return family_area_name;
    }

    public void setFamily_area_name(String family_area_name) {
        this.family_area_name = family_area_name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getArticle_comment_cnt() {
        return article_comment_cnt;
    }

    public void setArticle_comment_cnt(String article_comment_cnt) {
        this.article_comment_cnt = article_comment_cnt;
    }

    public String getArticle_type() {
        return article_type;
    }

    public void setArticle_type(String article_type) {
        this.article_type = article_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getArticle_gift_cnt() {
        return article_gift_cnt;
    }

    public void setArticle_gift_cnt(String article_gift_cnt) {
        this.article_gift_cnt = article_gift_cnt;
    }

    public String getArticle_from() {
        return article_from;
    }

    public void setArticle_from(String article_from) {
        this.article_from = article_from;
    }

    public String getC_surname() {
        return c_surname;
    }

    public void setC_surname(String c_surname) {
        this.c_surname = c_surname;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public String getArticle_photo() {
        return article_photo;
    }

    public void setArticle_photo(String article_photo) {
        this.article_photo = article_photo;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_status() {
        return article_status;
    }

    public void setArticle_status(String article_status) {
        this.article_status = article_status;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getArticle_praise_cnt() {
        return article_praise_cnt;
    }

    public void setArticle_praise_cnt(String article_praise_cnt) {
        this.article_praise_cnt = article_praise_cnt;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

}

