package com.yuejian.meet.bean;

/**
 * @author : g000gle
 * @time : 2019/6/27 20:02
 * @desc : 评论回复列表实体类
 */
public class CommentReplyListEntity {
    public String article_comment_content;
    public String op_customer_id;
    public String surname;
    public int article_object_id;
    public String name;
    public String op_surname;
    public String op_name;
    public int customer_id;
    public int article_comment_id;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getArticle_object_id() {
        return article_object_id;
    }

    public void setArticle_object_id(int article_object_id) {
        this.article_object_id = article_object_id;
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

    public String getOp_name() {
        return op_name;
    }

    public void setOp_name(String op_name) {
        this.op_name = op_name;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getArticle_comment_id() {
        return article_comment_id;
    }

    public void setArticle_comment_id(int article_comment_id) {
        this.article_comment_id = article_comment_id;
    }
}
