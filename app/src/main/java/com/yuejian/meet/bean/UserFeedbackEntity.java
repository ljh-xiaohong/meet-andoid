package com.yuejian.meet.bean;

/**
 * 用户反馈实体类
 */
public class UserFeedbackEntity {
    public String customer_id;//": 708258,
    public String customer_name;//": "那个谁",
    public Long feedback_create_time;//": 1528251337000,
    public String feedback_id;//": 19,
    public String feedback_imgs;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251334805.jpg,http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251335334_hengping.jpg,http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251335907.jpg,http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251336073.jpg,http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251336603.jpg,http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/7082581528251336611.jpg",
    public String feedback_question;//": "有问题",
    public String feedback_status;//": 0,
    public String feedback_tel;//": "hfhjjfnjjj@qq.com",
    public String feedback_type;//": 1,
    public String feedback_update_time;//": null
    public String replyList;

    public String getReplyList() {
        return replyList;
    }

    public void setReplyList(String replyList) {
        this.replyList = replyList;
    }

    public Long getFeedback_create_time() {
        return feedback_create_time;
    }

    public void setFeedback_create_time(Long feedback_create_time) {
        this.feedback_create_time = feedback_create_time;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFeedback_imgs() {
        return feedback_imgs;
    }

    public void setFeedback_imgs(String feedback_imgs) {
        this.feedback_imgs = feedback_imgs;
    }

    public String getFeedback_question() {
        return feedback_question;
    }

    public void setFeedback_question(String feedback_question) {
        this.feedback_question = feedback_question;
    }

    public String getFeedback_status() {
        return feedback_status;
    }

    public void setFeedback_status(String feedback_status) {
        this.feedback_status = feedback_status;
    }

    public String getFeedback_tel() {
        return feedback_tel;
    }

    public void setFeedback_tel(String feedback_tel) {
        this.feedback_tel = feedback_tel;
    }

    public String getFeedback_type() {
        return feedback_type;
    }

    public void setFeedback_type(String feedback_type) {
        this.feedback_type = feedback_type;
    }

    public String getFeedback_update_time() {
        return feedback_update_time;
    }

    public void setFeedback_update_time(String feedback_update_time) {
        this.feedback_update_time = feedback_update_time;
    }
}
