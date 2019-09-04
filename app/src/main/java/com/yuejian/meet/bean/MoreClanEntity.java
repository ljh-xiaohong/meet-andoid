package com.yuejian.meet.bean;

import java.io.Serializable;

public class MoreClanEntity implements Serializable {
    public String association_about;
    public String association_address;
    public String association_area;
    public String association_city;
    public String association_cnt;
    public String association_createtime;
    public String association_found_date;
    public String association_hall_name;//祖词名
    public String association_id;
    public String association_img;
    public String association_intrant_about;
    public String association_lat;
    public String association_lng;
    public String association_name;
    public String association_province;
    public String association_reason;
    public String association_status="";//0筹建中，1 筹建成功，-1筹建失败
    public String association_surname;
    public String customer_id;
    public int customer_type;///0 游客，1 创始要，2 成员
    public String distance;
    public String hall_id;
    public String hall_name;
    public String is_MiClan = "N";
    public String km = "-1";
    public String wy_team_id;
    public int is_exist;
    public int is_join=0;

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
    }

    public int getIs_exist() {
        return is_exist;
    }

    public void setIs_exist(int is_exist) {
        this.is_exist = is_exist;
    }

    public String getHall_name() {
        return hall_name;
    }

    public void setHall_name(String hall_name) {
        this.hall_name = hall_name;
    }

    public String getAssociation_about()
    {
        return this.association_about;
    }

    public String getAssociation_address()
    {
        return this.association_address;
    }

    public String getAssociation_area()
    {
        return this.association_area;
    }

    public String getAssociation_city()
    {
        return this.association_city;
    }

    public String getAssociation_cnt()
    {
        return this.association_cnt;
    }

    public String getAssociation_createtime()
    {
        return this.association_createtime;
    }

    public String getAssociation_found_date()
    {
        return this.association_found_date;
    }

    public String getAssociation_hall_name()
    {
        return this.association_hall_name;
    }

    public String getAssociation_id()
    {
        return this.association_id;
    }

    public String getAssociation_img()
    {
        return this.association_img;
    }

    public String getAssociation_intrant_about()
    {
        return this.association_intrant_about;
    }

    public String getAssociation_lat()
    {
        return this.association_lat;
    }

    public String getAssociation_lng()
    {
        return this.association_lng;
    }

    public String getAssociation_name()
    {
        return this.association_name;
    }

    public String getAssociation_province()
    {
        return this.association_province;
    }

    public String getAssociation_reason()
    {
        return this.association_reason;
    }

    public String getAssociation_status()
    {
        return this.association_status;
    }

    public String getAssociation_surname()
    {
        return this.association_surname;
    }

    public String getCustomer_id()
    {
        return this.customer_id;
    }

    public int getCustomer_type()
    {
        return this.customer_type;
    }

    public String getDistance()
    {
        return this.distance;
    }

    public String getHall_id()
    {
        return this.hall_id;
    }

    public String getIs_MiClan()
    {
        return this.is_MiClan;
    }

    public String getKm()
    {
        return this.km;
    }

    public String getWy_team_id()
    {
        return this.wy_team_id;
    }

    public void setAssociation_about(String paramString)
    {
        this.association_about = paramString;
    }

    public void setAssociation_address(String paramString)
    {
        this.association_address = paramString;
    }

    public void setAssociation_area(String paramString)
    {
        this.association_area = paramString;
    }

    public void setAssociation_city(String paramString)
    {
        this.association_city = paramString;
    }

    public void setAssociation_cnt(String paramString)
    {
        this.association_cnt = paramString;
    }

    public void setAssociation_createtime(String paramString)
    {
        this.association_createtime = paramString;
    }

    public void setAssociation_found_date(String paramString)
    {
        this.association_found_date = paramString;
    }

    public void setAssociation_hall_name(String paramString)
    {
        this.association_hall_name = paramString;
    }

    public void setAssociation_id(String paramString)
    {
        this.association_id = paramString;
    }

    public void setAssociation_img(String paramString)
    {
        this.association_img = paramString;
    }

    public void setAssociation_intrant_about(String paramString)
    {
        this.association_intrant_about = paramString;
    }

    public void setAssociation_lat(String paramString)
    {
        this.association_lat = paramString;
    }

    public void setAssociation_lng(String paramString)
    {
        this.association_lng = paramString;
    }

    public void setAssociation_name(String paramString)
    {
        this.association_name = paramString;
    }

    public void setAssociation_province(String paramString)
    {
        this.association_province = paramString;
    }

    public void setAssociation_reason(String paramString)
    {
        this.association_reason = paramString;
    }

    public void setAssociation_status(String paramString)
    {
        this.association_status = paramString;
    }

    public void setAssociation_surname(String paramString)
    {
        this.association_surname = paramString;
    }

    public void setCustomer_id(String paramString)
    {
        this.customer_id = paramString;
    }

    public void setCustomer_type(int paramInt)
    {
        this.customer_type = paramInt;
    }

    public void setDistance(String paramString)
    {
        this.distance = paramString;
    }

    public void setHall_id(String paramString)
    {
        this.hall_id = paramString;
    }

    public void setIs_MiClan(String paramString)
    {
        this.is_MiClan = paramString;
    }

    public void setKm(String paramString)
    {
        this.km = paramString;
    }

    public void setWy_team_id(String paramString)
    {
        this.wy_team_id = paramString;
    }
}
