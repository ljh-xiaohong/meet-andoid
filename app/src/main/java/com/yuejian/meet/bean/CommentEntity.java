package com.yuejian.meet.bean;

import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/27 19:59
 * @desc : 评论实体类
 */
public class CommentEntity {
    public long article_comment_time;  //评论时间
    public String photo;  //评论用户头像
    public int is_praise;   //评论用户是否点过赞
    public String article_comment_content;   //评论内容
    public int article_reply_cnt;  //该评论回复条数
    public List<CommentReplyListEntity> reply_list;  //评论回复List
    public String name;   //评论用户姓名
    public int customer_id;  //评论用户id
    public String job;  //评论用户的工作
    public String family_area;  //评论用户家族地域名
    public int article_comment_praise_cnt;  //该评论的被点赞数
    public int article_comment_id;  //该评论的评论id
}
