package com.yuejian.meet.bean;

import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/18 14:38
 * @desc : 商圈 - 需求列表实体类
 */
public class BusinessDemandListEntity {
    public int comment_num;
    public long create_time;
    public int fabulous_num;
    public String name;
    public String photo;
    public int id;
    public String relation_type;
    public String title;
    public int customer_id;
    public String job;
    public String label_name;
    public String content;
    public String view_count;
    private boolean is_praise;
    private boolean is_bind;
    private List<Resources_url> resources_url;

    public boolean isIs_praise() {
        return is_praise;
    }

    public boolean isIs_bind() {
        return is_bind;
    }

    public List<Resources_url> getResources_url() {
        return resources_url;
    }

    public void setResources_url(List<Resources_url> resources_url) {
        this.resources_url = resources_url;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public boolean getIs_praise() {
        return is_praise;
    }

    public boolean getIs_bind() {
        return is_bind;
    }

    public void setIs_praise(boolean is_praise) {
        this.is_praise = is_praise;
    }

    public void setIs_bind(boolean is_bind) {
        this.is_bind = is_bind;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getFabulous_num() {
        return fabulous_num;
    }

    public void setFabulous_num(int fabulous_num) {
        this.fabulous_num = fabulous_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class Resources_url {
        String delete_id;
        String src;
        String type;

        public String getDelete_id() {
            return delete_id;
        }

        public void setDelete_id(String delete_id) {
            this.delete_id = delete_id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
