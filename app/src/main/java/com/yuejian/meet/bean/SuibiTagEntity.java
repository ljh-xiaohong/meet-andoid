package com.yuejian.meet.bean;

/**
 * @author : g000gle
 * @time : 2019/5/24 17:50
 * @desc : 随笔 Tag
 */
public class SuibiTagEntity {
    Long createTime;
    int id;
    int sort;
    String title;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
