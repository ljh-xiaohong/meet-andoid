package com.netease.nim.uikit.app.entity;

/**
 *消息扩展字段类
 * Created by zh03 on 2017/7/17.
 */

public class MessageExtendEntity {

    public String job;//":"警察",
    public String age;//":14
    public Long is_family_master=Long.parseLong("0");//大于0表示是城市发起人
    public String master_area_name;//
    public String family_area;//
    public Long is_super=0l;

    public Long getIs_super() {
        return is_super;
    }

    public void setIs_super(Long is_super) {
        this.is_super = is_super;
    }

    public String getFamily_area() {
        return family_area;
    }

    public void setFamily_area(String family_area) {
        this.family_area = family_area;
    }

    public Long getIs_family_master() {
        return is_family_master;
    }

    public void setIs_family_master(Long is_family_master) {
        this.is_family_master = is_family_master;
    }

    public String getMaster_area_name() {
        return master_area_name;
    }

    public void setMaster_area_name(String master_area_name) {
        this.master_area_name = master_area_name;
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
}
