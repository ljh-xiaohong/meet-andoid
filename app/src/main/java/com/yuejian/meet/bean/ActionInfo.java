package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态单条实体类
 * Created by zh03 on 2017/8/17.
 */
public class ActionInfo implements Serializable {
    public String action_url;//": "",
    public String area;//": "",
    public String video_first_url;//": "",
    public String create_time;//": 1502284473000,
    public String city;//": "",
    public String reward_list;//": ["photo": "","customer_id": 300241}]
    public String url_type;//": 0,
    public String sex;//": 1,
    public String latitude;//": "",
    public String is_family_master;//": 1,
    public String photo;//": "http://michatapp-oss.oss-cn-shenzhen.aliyuncs.com/photoalbum/15016456213000810.jpg",
    public String is_praise;//": 0,
    public String comment_cnt;//": 1,
    public String praise_cnt;//": 1,
    public String province;//": "",
    public String family_area_name;//": "珠海",
    public String action_title;//": "测试测试测试",
    public String surname;//": "陈",
    public String name;//": "波波",
    public String customer_id;//": 300240,
    public String job;//": "",
    public String age;//": 17,
    public String gift_cnt;//": 2,
    public String longitude;//": ""
    public Long is_super;
    public ArrayList<PraiseInfo> praise_list;
    public String reward_cnt;

    public String getReward_cnt() {
        return reward_cnt;
    }

    public void setReward_cnt(String reward_cnt) {
        this.reward_cnt = reward_cnt;
    }

    public ArrayList<PraiseInfo> getPraise_list() {
        return praise_list;
    }

    public void setPraise_list(ArrayList<PraiseInfo> praise_list) {
        this.praise_list = praise_list;
    }

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getAction_url() {
        return action_url;
    }

    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getVideo_first_url() {
        return video_first_url;
    }

    public void setVideo_first_url(String video_first_url) {
        this.video_first_url = video_first_url;
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

    public String getReward_list() {
        return reward_list;
    }

    public void setReward_list(String reward_list) {
        this.reward_list = reward_list;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(String is_family_master) {
        this.is_family_master = is_family_master;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(String comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public String getPraise_cnt() {
        return praise_cnt;
    }

    public void setPraise_cnt(String praise_cnt) {
        this.praise_cnt = praise_cnt;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getFamily_area_name() {
        return family_area_name;
    }

    public void setFamily_area_name(String family_area_name) {
        this.family_area_name = family_area_name;
    }

    public String getAction_title() {
        return action_title;
    }

    public void setAction_title(String action_title) {
        this.action_title = action_title;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public String getGift_cnt() {
        return gift_cnt;
    }

    public void setGift_cnt(String gift_cnt) {
        this.gift_cnt = gift_cnt;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public class PraiseInfo {
        public String photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/photoalbum/1505730430200187_0_shuping.jpg",
        public String customer_id;//": 200187

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }
    }
}
