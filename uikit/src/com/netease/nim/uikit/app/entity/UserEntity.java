package com.netease.nim.uikit.app.entity;

/**
 *用户基本信息
 * Created by zh03 on 2017/8/8.
 */

public class UserEntity {
    //用户编码
    public String customer_id;

    public String customerId;
    //用户状态
    public String customer_status;//0:正常 1:被冻结 2:冻结一天 3:永久冻结
    //最后登录时间
    public String  last_login_time;
    //用户注册时间
    public String register_date;
    //手机号
    public String  mobile;
    //家族id
    public String  family_id;
    //营业执照认证
    public String  is_business_license_certified;
    //省份证是否认证
    public String  is_idcard_certified;
    //是否是城市发起人
    public String  is_family_master;
    //姓
    public String surname;
    //名
    public String  name;
    //头像
    public String  photo;
    //性别 0 为女  1为男
    public String sex;
    //登录标志
    public String  token;//传给网易云
    //充值余额
    public String recharge_bal;
    //收益余额
    public String gains_bal;
    //省
    public String province;
    //城市
    public String city;
    //地区
    public String area;
    public String bg_url;
    public String birthday;
    public String chat_group_tid;

    public String  age;//年龄

    public String  video_price;//视频价格

    public String invite_code;//推荐码

    public String msg_cnt;//消息通知数量

    public String is_online;//视频在线状态

    public String is_set_pw;//是否设置过支付密码

    public String  is_certified;//是否已认证

    public String rank;

    public String company_name;
    public String other_job;
    public String job;
    public String email;
    public String position;
    public String origin_place;
    public String home;
    public String friendStatus;
    public String address;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getOrigin_place() {
        return origin_place;
    }

    public void setOrigin_place(String origin_place) {
        this.origin_place = origin_place;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOther_job() {
        return other_job;
    }

    public void setOther_job(String other_job) {
        this.other_job = other_job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getChat_group_tid() {
        return chat_group_tid;
    }

    public void setChat_group_tid(String chat_group_tid) {
        this.chat_group_tid = chat_group_tid;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getIs_business_license_certified() {
        return is_business_license_certified;
    }

    public void setIs_business_license_certified(String is_business_license_certified) {
        this.is_business_license_certified = is_business_license_certified;
    }

    public String getIs_idcard_certified() {
        return is_idcard_certified;
    }

    public void setIs_idcard_certified(String is_idcard_certified) {
        this.is_idcard_certified = is_idcard_certified;
    }

    public String getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(String is_family_master) {
        this.is_family_master = is_family_master;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRecharge_bal() {
        return recharge_bal;
    }

    public void setRecharge_bal(String recharge_bal) {
        this.recharge_bal = recharge_bal;
    }

    public String getGains_bal() {
        return gains_bal;
    }

    public void setGains_bal(String gains_bal) {
        this.gains_bal = gains_bal;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVideo_price() {
        return video_price;
    }

    public void setVideo_price(String video_price) {
        this.video_price = video_price;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getMsg_cnt() {
        return msg_cnt;
    }

    public void setMsg_cnt(String msg_cnt) {
        this.msg_cnt = msg_cnt;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getIs_set_pw() {
        return is_set_pw;
    }

    public void setIs_set_pw(String is_set_pw) {
        this.is_set_pw = is_set_pw;
    }

    public String getIs_certified() {
        return is_certified;
    }

    public void setIs_certified(String is_certified) {
        this.is_certified = is_certified;
    }
}
