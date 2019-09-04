package com.yuejian.meet.bean;

/**
 * 反馈问题回复实体类
 */
public class FeebackCommentEntity {
    public String comment_content;
    public long comment_create_time;
    public  String comment_id;
    public  String comment_status;
    public  String comment_type;
    public  long comment_update_time;
    public  String mg_user_id;
    public  String op_customer_id;

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getComment_create_time() {
        return comment_create_time;
    }

    public void setComment_create_time(long comment_create_time) {
        this.comment_create_time = comment_create_time;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public String getComment_type() {
        return comment_type;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
    }

    public long getComment_update_time() {
        return comment_update_time;
    }

    public void setComment_update_time(long comment_update_time) {
        this.comment_update_time = comment_update_time;
    }

    public String getMg_user_id() {
        return mg_user_id;
    }

    public void setMg_user_id(String mg_user_id) {
        this.mg_user_id = mg_user_id;
    }

    public String getOp_customer_id() {
        return op_customer_id;
    }

    public void setOp_customer_id(String op_customer_id) {
        this.op_customer_id = op_customer_id;
    }
}
