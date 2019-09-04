package com.yuejian.meet.bean;

import java.util.List;

public class ClanPhotoArrlyEntity
{
    public List<ClanPhotoEntity> listPhoto;
    public String month;
    public String time;
    public String year;

    public List<ClanPhotoEntity> getListPhoto()
    {
        return this.listPhoto;
    }

    public String getMonth()
    {
        return this.month;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getYear()
    {
        return this.year;
    }

    public void setListPhoto(List<ClanPhotoEntity> paramList)
    {
        this.listPhoto = paramList;
    }

    public void setMonth(String paramString)
    {
        this.month = paramString;
    }

    public void setTime(String paramString)
    {
        this.time = paramString;
    }

    public void setYear(String paramString)
    {
        this.year = paramString;
    }
}
