package com.yuejian.meet.bean;

/**
 * Created by zh03 on 2017/8/15.
 */

public class ActionAllEntity {
    public String action_list;
    public String  un_read_msg;
    public String feedback;
    public String replyList;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getReplyList() {
        return replyList;
    }

    public void setReplyList(String replyList) {
        this.replyList = replyList;
    }

    public String getAction_list() {
        return action_list;
    }

    public void setAction_list(String action_list) {
        this.action_list = action_list;
    }

    public String getUn_read_msg() {
        return un_read_msg;
    }

    public void setUn_read_msg(String un_read_msg) {
        this.un_read_msg = un_read_msg;
    }
}
