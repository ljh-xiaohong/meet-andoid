package com.yuejian.meet.bean;

public class ClanFindApprovaEntity
{
    public String age;
    public String bg_url;
    public String customer_id;
    public Boolean isSelect = Boolean.valueOf(false);
    public Integer is_exist = Integer.valueOf(0);
    public String member_id;
    public String name;
    public String photo;
    public String sex;
    public String sortLetters;
    public String surname;

    public String getAge()
    {
        return this.age;
    }

    public String getBg_url()
    {
        return this.bg_url;
    }

    public String getCustomer_id()
    {
        return this.customer_id;
    }

    public Integer getIs_exist()
    {
        return this.is_exist;
    }

    public String getMember_id()
    {
        return this.member_id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getPhoto()
    {
        return this.photo;
    }

    public Boolean getSelect()
    {
        return this.isSelect;
    }

    public String getSex()
    {
        return this.sex;
    }

    public String getSortLetters()
    {
        return this.sortLetters;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public void setAge(String paramString)
    {
        this.age = paramString;
    }

    public void setBg_url(String paramString)
    {
        this.bg_url = paramString;
    }

    public void setCustomer_id(String paramString)
    {
        this.customer_id = paramString;
    }

    public void setIs_exist(Integer paramInteger)
    {
        this.is_exist = paramInteger;
    }

    public void setMember_id(String paramString)
    {
        this.member_id = paramString;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }

    public void setPhoto(String paramString)
    {
        this.photo = paramString;
    }

    public void setSelect(Boolean paramBoolean)
    {
        this.isSelect = paramBoolean;
    }

    public void setSex(String paramString)
    {
        this.sex = paramString;
    }

    public void setSortLetters(String paramString)
    {
        this.sortLetters = paramString;
    }

    public void setSurname(String paramString)
    {
        this.surname = paramString;
    }
}
