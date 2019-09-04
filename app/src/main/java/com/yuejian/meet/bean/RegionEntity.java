package com.yuejian.meet.bean;

/**
 * 省--市--区
 * Created by zh03 on 2017/8/7.
 */

public class RegionEntity {
    public String province;//": "安徽省"
    public String provincn_city_area_id;//id
    public String city;//": "安庆市"
    public String area;//": "青阳县"
    public String area_name;//'"广东 珠海"
    public String sublist;///["昌平区","朝阳区","大兴区","东城区",]

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getSublist() {
        return sublist;
    }

    public void setSublist(String sublist) {
        this.sublist = sublist;
    }

    public String getProvincn_city_area_id() {
        return provincn_city_area_id;
    }

    public void setProvincn_city_area_id(String provincn_city_area_id) {
        this.provincn_city_area_id = provincn_city_area_id;
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
}
