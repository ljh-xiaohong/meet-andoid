package com.yuejian.meet.bean;

public class Gift
{
  public String count = "1";
  public String gift_benediction = "��������,��������";
  public Long gift_count;
  public Long gift_id;
  public String gift_image;
  public String gift_name;
  public Long gift_price;
  public String id;
  public boolean isSelected = false;
  public String status;
  
  public String getCount()
  {
    return this.count;
  }
  
  public String getGift_benediction()
  {
    return this.gift_benediction;
  }
  
  public Long getGift_count()
  {
    return this.gift_count;
  }
  
  public Long getGift_id()
  {
    return this.gift_id;
  }
  
  public String getGift_image()
  {
    return this.gift_image;
  }
  
  public String getGift_name()
  {
    return this.gift_name;
  }
  
  public Long getGift_price()
  {
    return this.gift_price;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public boolean isSelected()
  {
    return this.isSelected;
  }
  
  public void setCount(String paramString)
  {
    this.count = paramString;
  }
  
  public void setGift_benediction(String paramString)
  {
    this.gift_benediction = paramString;
  }
  
  public void setGift_count(Long paramLong)
  {
    this.gift_count = paramLong;
  }
  
  public void setGift_id(Long paramLong)
  {
    this.gift_id = paramLong;
  }
  
  public void setGift_image(String paramString)
  {
    this.gift_image = paramString;
  }
  
  public void setGift_name(String paramString)
  {
    this.gift_name = paramString;
  }
  
  public void setGift_price(Long paramLong)
  {
    this.gift_price = paramLong;
  }
  
  public void setId(String paramString)
  {
    this.id = paramString;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    this.isSelected = paramBoolean;
  }
  
  public void setStatus(String paramString)
  {
    this.status = paramString;
  }
}
