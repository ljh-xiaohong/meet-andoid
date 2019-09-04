package com.yuejian.meet.bean;

import java.io.Serializable;

public class ClanBulletinEntity
        implements Serializable
{
    public String association_id;
    public String bulletin_createtime;
    public String bulletin_id;
    public String bulletin_img;
    public String bulletin_info;
    public String bulletin_status;
    public String bulletin_title;
    public String customer_id;

    public String getAssociation_id()
    {
        return this.association_id;
    }

    public String getBulletin_createtime()
    {
        return this.bulletin_createtime;
    }

    public String getBulletin_id()
    {
        return this.bulletin_id;
    }

    public String getBulletin_img()
    {
        return this.bulletin_img;
    }

    public String getBulletin_info()
    {
        return this.bulletin_info;
    }

    public String getBulletin_status()
    {
        return this.bulletin_status;
    }

    public String getBulletin_title()
    {
        return this.bulletin_title;
    }

    public String getCustomer_id()
    {
        return this.customer_id;
    }

    public void setAssociation_id(String paramString)
    {
        this.association_id = paramString;
    }

    public void setBulletin_createtime(String paramString)
    {
        this.bulletin_createtime = paramString;
    }

    public void setBulletin_id(String paramString)
    {
        this.bulletin_id = paramString;
    }

    public void setBulletin_img(String paramString)
    {
        this.bulletin_img = paramString;
    }

    public void setBulletin_info(String paramString)
    {
        this.bulletin_info = paramString;
    }

    public void setBulletin_status(String paramString)
    {
        this.bulletin_status = paramString;
    }

    public void setBulletin_title(String paramString)
    {
        this.bulletin_title = paramString;
    }

    public void setCustomer_id(String paramString)
    {
        this.customer_id = paramString;
    }
}
