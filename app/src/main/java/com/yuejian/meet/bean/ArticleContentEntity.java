package com.yuejian.meet.bean;

/**
 * @author : g000gle
 * @time : 2019/6/26 22:35
 * @desc : 文章内容item实体类
 */
public class ArticleContentEntity {
    public int index;
    public String type;
    public String content;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
