package com.yuejian.meet.bean;

/**
 * Created by zh02 on 2017/8/16.
 */

public class Contact {

    public String surname;//": "廖",
    public String company_name;//": "廖",
    public String sex;//": 1,
    public String name;//": "建鸿",
    public String photo;//": "http://q.qlogo.cn/qqapp/1106149025/2D0316DC726C2C9BC3DA78AD519F1E55/100",
    public String registration_id;//": "1517bfd3f7f396c575f",
    public String customer_id;//": 300259,
    public String job;//": "苦力，随便打个代码",
    public String age;//": 23,
    public String token;//": "BE88AA8244626EE036467F1B7EA2A8ADE96FD1BA"
    public Long is_family_master;
    public Long is_super=0l;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Integer is_exist;//": 0,;
    public Boolean isSelect=false;
    public String sortLetters;

    public Integer getIs_exist() {
        return is_exist;
    }

    public void setIs_exist(Integer is_exist) {
        this.is_exist = is_exist;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public Long getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(Long is_family_master) {
        this.is_family_master = is_family_master;
    }


    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
