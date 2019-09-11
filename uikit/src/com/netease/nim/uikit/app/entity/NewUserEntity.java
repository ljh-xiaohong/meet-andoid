package com.netease.nim.uikit.app.entity;

import java.io.Serializable;

/**
 *用户基本信息
 * Created by zh03 on 2017/8/8.
 */

public class NewUserEntity {


    /**
     * code : 19998
     * data : {"age":0,"area":"","area_code":"+86","area_name":"","bg_url":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/bg.png","birthday":null,"chat_group_tid":0,"city":"","clan_hall_footprint_switch":1,"company_address":"","company_link":"","company_name":"","customer_id":500188,"customer_status":0,"email":"","family_area":"","family_id":0,"frozen_id":0,"frozen_time":null,"home":"","industry":"","inheritor_id":0,"interest":"","invite_code":"","is_business_license_certified":-1,"is_default_photo":0,"is_family_master":0,"is_hot_card":0,"is_idcard_certified":-1,"is_kf":0,"is_online":0,"is_super":0,"job":"","last_login_city":"珠海市","last_login_time":1567579169000,"market_token":"","master_area_name":"","mobile":"13168328807","name":"凉","origin_place":"","other_job":"","password":"","perfect_ratio":0,"photo":"https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/default.png","province":"","qq":"","qrcode_img":"","rank":0,"referral_mobile":"","referrer_id":0,"referrer_id2":0,"referrer_id3":0,"register_date":1567579169000,"residue":0,"service_status":1,"sex":0,"signature":"弘扬姓氏文化，凝聚家族力量","specialty":"","super_area":"","surname":"凉","third_nick_name":"","third_photo":"","token":"698d53d27e0afa658ebae6fd40ad58b6","unionid":"","video_price":0,"vip_expiration_time":null,"vip_type":0,"weixin":"","weixin_bound":""}
     * message : 登录成功
     * result : true
     */

    private int code;
    private DataBean data;
    private String message;
    private boolean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public static class DataBean implements Serializable {
        /**
         * age : 0
         * area :
         * area_code : +86
         * area_name :
         * bg_url : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/bg.png
         * birthday : null
         * chat_group_tid : 0
         * city :
         * clan_hall_footprint_switch : 1
         * company_address :
         * company_link :
         * company_name :
         * customer_id : 500188
         * customer_status : 0
         * email :
         * family_area :
         * family_id : 0
         * frozen_id : 0
         * frozen_time : null
         * home :
         * industry :
         * inheritor_id : 0
         * interest :
         * invite_code :
         * is_business_license_certified : -1
         * is_default_photo : 0
         * is_family_master : 0
         * is_hot_card : 0
         * is_idcard_certified : -1
         * is_kf : 0
         * is_online : 0
         * is_super : 0
         * job :
         * last_login_city : 珠海市
         * last_login_time : 1567579169000
         * market_token :
         * master_area_name :
         * mobile : 13168328807
         * name : 凉
         * origin_place :
         * other_job :
         * password :
         * perfect_ratio : 0.0
         * photo : https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/default.png
         * province :
         * qq :
         * qrcode_img :
         * rank : 0
         * referral_mobile :
         * referrer_id : 0
         * referrer_id2 : 0
         * referrer_id3 : 0
         * register_date : 1567579169000
         * residue : 0
         * service_status : 1
         * sex : 0
         * signature : 弘扬姓氏文化，凝聚家族力量
         * specialty :
         * super_area :
         * surname : 凉
         * third_nick_name :
         * third_photo :
         * token : 698d53d27e0afa658ebae6fd40ad58b6
         * unionid :
         * video_price : 0
         * vip_expiration_time : null
         * vip_type : 0
         * weixin :
         * weixin_bound :
         */

        private int age;
        private String area;
        private String area_code;
        private String area_name;
        private String bg_url;
        private Object birthday;
        private int chat_group_tid;
        private String city;
        private int clan_hall_footprint_switch;
        private String company_address;
        private String company_link;
        private String company_name;
        private int customer_id;
        private int customerId;
        private int customer_status;
        private String email;
        private String family_area;
        private int family_id;
        private int frozen_id;
        private Object frozen_time;
        private String home;
        private String industry;
        private int inheritor_id;
        private String interest;
        private String invite_code;
        private int is_business_license_certified;
        private int is_default_photo;
        private int is_family_master;
        private int is_hot_card;
        private int is_idcard_certified;
        private int is_kf;
        private int is_online;
        private int is_super;
        private String job;
        private String last_login_city;
        private long last_login_time;
        private String market_token;
        private String master_area_name;
        private String mobile;
        private String name;
        private String origin_place;
        private String other_job;
        private String password;
        private double perfect_ratio;
        private String photo;
        private String province;
        private String qq;
        private String qrcode_img;
        private int rank;
        private String referral_mobile;
        private int referrer_id;
        private int referrer_id2;
        private int referrer_id3;
        private long register_date;
        private int residue;
        private int service_status;
        private int sex;
        private String signature;
        private String specialty;
        private String super_area;
        private String surname;
        private String third_nick_name;
        private String third_photo;
        private String token;
        private String unionid;
        private int video_price;
        private Object vip_expiration_time;
        private int vip_type;
        private String weixin;
        private String weixin_bound;

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getBg_url() {
            return bg_url;
        }

        public void setBg_url(String bg_url) {
            this.bg_url = bg_url;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public int getChat_group_tid() {
            return chat_group_tid;
        }

        public void setChat_group_tid(int chat_group_tid) {
            this.chat_group_tid = chat_group_tid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getClan_hall_footprint_switch() {
            return clan_hall_footprint_switch;
        }

        public void setClan_hall_footprint_switch(int clan_hall_footprint_switch) {
            this.clan_hall_footprint_switch = clan_hall_footprint_switch;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

        public String getCompany_link() {
            return company_link;
        }

        public void setCompany_link(String company_link) {
            this.company_link = company_link;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public int getCustomer_status() {
            return customer_status;
        }

        public void setCustomer_status(int customer_status) {
            this.customer_status = customer_status;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFamily_area() {
            return family_area;
        }

        public void setFamily_area(String family_area) {
            this.family_area = family_area;
        }

        public int getFamily_id() {
            return family_id;
        }

        public void setFamily_id(int family_id) {
            this.family_id = family_id;
        }

        public int getFrozen_id() {
            return frozen_id;
        }

        public void setFrozen_id(int frozen_id) {
            this.frozen_id = frozen_id;
        }

        public Object getFrozen_time() {
            return frozen_time;
        }

        public void setFrozen_time(Object frozen_time) {
            this.frozen_time = frozen_time;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public int getInheritor_id() {
            return inheritor_id;
        }

        public void setInheritor_id(int inheritor_id) {
            this.inheritor_id = inheritor_id;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public int getIs_business_license_certified() {
            return is_business_license_certified;
        }

        public void setIs_business_license_certified(int is_business_license_certified) {
            this.is_business_license_certified = is_business_license_certified;
        }

        public int getIs_default_photo() {
            return is_default_photo;
        }

        public void setIs_default_photo(int is_default_photo) {
            this.is_default_photo = is_default_photo;
        }

        public int getIs_family_master() {
            return is_family_master;
        }

        public void setIs_family_master(int is_family_master) {
            this.is_family_master = is_family_master;
        }

        public int getIs_hot_card() {
            return is_hot_card;
        }

        public void setIs_hot_card(int is_hot_card) {
            this.is_hot_card = is_hot_card;
        }

        public int getIs_idcard_certified() {
            return is_idcard_certified;
        }

        public void setIs_idcard_certified(int is_idcard_certified) {
            this.is_idcard_certified = is_idcard_certified;
        }

        public int getIs_kf() {
            return is_kf;
        }

        public void setIs_kf(int is_kf) {
            this.is_kf = is_kf;
        }

        public int getIs_online() {
            return is_online;
        }

        public void setIs_online(int is_online) {
            this.is_online = is_online;
        }

        public int getIs_super() {
            return is_super;
        }

        public void setIs_super(int is_super) {
            this.is_super = is_super;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getLast_login_city() {
            return last_login_city;
        }

        public void setLast_login_city(String last_login_city) {
            this.last_login_city = last_login_city;
        }

        public long getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(long last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getMarket_token() {
            return market_token;
        }

        public void setMarket_token(String market_token) {
            this.market_token = market_token;
        }

        public String getMaster_area_name() {
            return master_area_name;
        }

        public void setMaster_area_name(String master_area_name) {
            this.master_area_name = master_area_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrigin_place() {
            return origin_place;
        }

        public void setOrigin_place(String origin_place) {
            this.origin_place = origin_place;
        }

        public String getOther_job() {
            return other_job;
        }

        public void setOther_job(String other_job) {
            this.other_job = other_job;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public double getPerfect_ratio() {
            return perfect_ratio;
        }

        public void setPerfect_ratio(double perfect_ratio) {
            this.perfect_ratio = perfect_ratio;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getQrcode_img() {
            return qrcode_img;
        }

        public void setQrcode_img(String qrcode_img) {
            this.qrcode_img = qrcode_img;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getReferral_mobile() {
            return referral_mobile;
        }

        public void setReferral_mobile(String referral_mobile) {
            this.referral_mobile = referral_mobile;
        }

        public int getReferrer_id() {
            return referrer_id;
        }

        public void setReferrer_id(int referrer_id) {
            this.referrer_id = referrer_id;
        }

        public int getReferrer_id2() {
            return referrer_id2;
        }

        public void setReferrer_id2(int referrer_id2) {
            this.referrer_id2 = referrer_id2;
        }

        public int getReferrer_id3() {
            return referrer_id3;
        }

        public void setReferrer_id3(int referrer_id3) {
            this.referrer_id3 = referrer_id3;
        }

        public long getRegister_date() {
            return register_date;
        }

        public void setRegister_date(long register_date) {
            this.register_date = register_date;
        }

        public int getResidue() {
            return residue;
        }

        public void setResidue(int residue) {
            this.residue = residue;
        }

        public int getService_status() {
            return service_status;
        }

        public void setService_status(int service_status) {
            this.service_status = service_status;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getSuper_area() {
            return super_area;
        }

        public void setSuper_area(String super_area) {
            this.super_area = super_area;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getThird_nick_name() {
            return third_nick_name;
        }

        public void setThird_nick_name(String third_nick_name) {
            this.third_nick_name = third_nick_name;
        }

        public String getThird_photo() {
            return third_photo;
        }

        public void setThird_photo(String third_photo) {
            this.third_photo = third_photo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public int getVideo_price() {
            return video_price;
        }

        public void setVideo_price(int video_price) {
            this.video_price = video_price;
        }

        public Object getVip_expiration_time() {
            return vip_expiration_time;
        }

        public void setVip_expiration_time(Object vip_expiration_time) {
            this.vip_expiration_time = vip_expiration_time;
        }

        public int getVip_type() {
            return vip_type;
        }

        public void setVip_type(int vip_type) {
            this.vip_type = vip_type;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getWeixin_bound() {
            return weixin_bound;
        }

        public void setWeixin_bound(String weixin_bound) {
            this.weixin_bound = weixin_bound;
        }

        @Override
        public String toString() {
            return "{" +
                    "age=" + age +
                    ", area='" + area + '\'' +
                    ", area_code='" + area_code + '\'' +
                    ", area_name='" + area_name + '\'' +
                    ", bg_url='" + bg_url + '\'' +
                    ", birthday=" + birthday +
                    ", chat_group_tid=" + chat_group_tid +
                    ", city='" + city + '\'' +
                    ", clan_hall_footprint_switch=" + clan_hall_footprint_switch +
                    ", company_address='" + company_address + '\'' +
                    ", company_link='" + company_link + '\'' +
                    ", company_name='" + company_name + '\'' +
                    ", customer_id=" + customer_id +
                    ", customer_status=" + customer_status +
                    ", email='" + email + '\'' +
                    ", family_area='" + family_area + '\'' +
                    ", family_id=" + family_id +
                    ", frozen_id=" + frozen_id +
                    ", frozen_time=" + frozen_time +
                    ", home='" + home + '\'' +
                    ", industry='" + industry + '\'' +
                    ", inheritor_id=" + inheritor_id +
                    ", interest='" + interest + '\'' +
                    ", invite_code='" + invite_code + '\'' +
                    ", is_business_license_certified=" + is_business_license_certified +
                    ", is_default_photo=" + is_default_photo +
                    ", is_family_master=" + is_family_master +
                    ", is_hot_card=" + is_hot_card +
                    ", is_idcard_certified=" + is_idcard_certified +
                    ", is_kf=" + is_kf +
                    ", is_online=" + is_online +
                    ", is_super=" + is_super +
                    ", job='" + job + '\'' +
                    ", last_login_city='" + last_login_city + '\'' +
                    ", last_login_time=" + last_login_time +
                    ", market_token='" + market_token + '\'' +
                    ", master_area_name='" + master_area_name + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", name='" + name + '\'' +
                    ", origin_place='" + origin_place + '\'' +
                    ", other_job='" + other_job + '\'' +
                    ", password='" + password + '\'' +
                    ", perfect_ratio=" + perfect_ratio +
                    ", photo='" + photo + '\'' +
                    ", province='" + province + '\'' +
                    ", qq='" + qq + '\'' +
                    ", qrcode_img='" + qrcode_img + '\'' +
                    ", rank=" + rank +
                    ", referral_mobile='" + referral_mobile + '\'' +
                    ", referrer_id=" + referrer_id +
                    ", referrer_id2=" + referrer_id2 +
                    ", referrer_id3=" + referrer_id3 +
                    ", register_date=" + register_date +
                    ", residue=" + residue +
                    ", service_status=" + service_status +
                    ", sex=" + sex +
                    ", signature='" + signature + '\'' +
                    ", specialty='" + specialty + '\'' +
                    ", super_area='" + super_area + '\'' +
                    ", surname='" + surname + '\'' +
                    ", third_nick_name='" + third_nick_name + '\'' +
                    ", third_photo='" + third_photo + '\'' +
                    ", token='" + token + '\'' +
                    ", unionid='" + unionid + '\'' +
                    ", video_price=" + video_price +
                    ", vip_expiration_time=" + vip_expiration_time +
                    ", vip_type=" + vip_type +
                    ", weixin='" + weixin + '\'' +
                    ", weixin_bound='" + weixin_bound + '\'' +
                    '}';
        }
    }
}
