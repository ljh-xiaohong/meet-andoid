package com.yuejian.meet.bean;

import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/18 10:59
 * @desc : 商圈活动列表实体类
 */
public class BusinessActivityListEntity {

    List<Resources_url> Resources_url;
    String join_way;
    String create_time;
    String city;
    List<JoinList> joinList;
    String photo;
    String userName;
    String content;
    String apply_num_yet;
    String activity_state;
    String read_num;
    String province;
    String is_concern;
    String district;
    String company_name;
    String id;
    String customer_id;
    String is_pass;
    String headline;

    public String getIs_concern() {
        return is_concern;
    }

    public void setIs_concern(String is_concern) {
        this.is_concern = is_concern;
    }

    public List<BusinessActivityListEntity.Resources_url> getResources_url() {
        return Resources_url;
    }

    public void setResources_url(List<BusinessActivityListEntity.Resources_url> resources_url) {
        Resources_url = resources_url;
    }

    public String getJoin_way() {
        return join_way;
    }

    public void setJoin_way(String join_way) {
        this.join_way = join_way;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<JoinList> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<JoinList> joinList) {
        this.joinList = joinList;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApply_num_yet() {
        return apply_num_yet;
    }

    public void setApply_num_yet(String apply_num_yet) {
        this.apply_num_yet = apply_num_yet;
    }

    public String getActivity_state() {
        return activity_state;
    }

    public void setActivity_state(String activity_state) {
        this.activity_state = activity_state;
    }

    public String getRead_num() {
        return read_num;
    }

    public void setRead_num(String read_num) {
        this.read_num = read_num;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public static class Resources_url {
        private String delete_id;
        private String src;
        private String type;

        public String getDelete_id() {
            return delete_id;
        }

        public String getSrc() {
            return src;
        }

        public String getType() {
            return type;
        }

        public void setDelete_id(String delete_id) {
            this.delete_id = delete_id;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class JoinList {

        private String number;
        private String create_time;
        private String photo;
        private String userName;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
