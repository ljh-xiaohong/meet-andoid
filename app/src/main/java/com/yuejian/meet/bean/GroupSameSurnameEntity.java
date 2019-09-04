package com.yuejian.meet.bean;

import butterknife.Bind;

/**
 * 邀请群聊同姓实体类
 * Created by zh03 on 2017/8/28.
 */

public class GroupSameSurnameEntity {
    public String surname;//": "梁",
    public String sex;//": 1,
    public String name;//": "山伯",
    public String photo;//": "http://q.qlogo.cn/qqapp/1106149025/6CD77DAF99EC8B4A3B8CDD4E7870FDE3/100",
    public String customer_id;//": 300298,
    public String family_area;///": "珠海",
    public String age;///": 25
    public Boolean isChoose=false;
    public Long is_praise=0l;//":0,

    public Long getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(Long is_praise) {
        this.is_praise = is_praise;
    }

    public Boolean getChoose() {
        return isChoose;
    }

    public void setChoose(Boolean choose) {
        isChoose = choose;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFamily_area() {
        return family_area;
    }

    public void setFamily_area(String family_area) {
        this.family_area = family_area;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
