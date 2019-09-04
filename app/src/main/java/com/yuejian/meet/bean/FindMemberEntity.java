package com.yuejian.meet.bean;

/**
 * Created by zh03 on 2017/12/27.
 */

/**
 * 发现宗亲的结果类
 */
public class FindMemberEntity {
    public String byname;//": "梁四",
    public String surname;//": "梁",
    public Long is_family_master;//": 0,
    public String name;//": "四海",
    public String photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/zupu/201710201923572357.png",
    public String master_area_name;//": "",
    public String customer_id;///": 276845
    public String distance;
    public Boolean isSelect=true;
    public String company_name;
    public String job;
    public String id;
    public String other_job;
    public String age;
    public int sex;
    public String op_role;
    public String role;

    public String getOp_role() {
        return op_role;
    }

    public void setOp_role(String op_role) {
        this.op_role = op_role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOther_job() {
        return other_job;
    }

    public void setOther_job(String other_job) {
        this.other_job = other_job;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(Long is_family_master) {
        this.is_family_master = is_family_master;
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

    public String getMaster_area_name() {
        return master_area_name;
    }

    public void setMaster_area_name(String master_area_name) {
        this.master_area_name = master_area_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
