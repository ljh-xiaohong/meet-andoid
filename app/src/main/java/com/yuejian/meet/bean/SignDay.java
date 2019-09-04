package com.yuejian.meet.bean;

import java.io.Serializable;

public class SignDay
  implements Serializable
{
  public String customer_id;
  public String days;
  public String id;
  public String signin_date;
  
  public String getCustomer_id()
  {
    return this.customer_id;
  }
  
  public String getDays()
  {
    return this.days;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public String getSignin_date()
  {
    return this.signin_date;
  }
  
  public void setCustomer_id(String paramString)
  {
    this.customer_id = paramString;
  }
  
  public void setDays(String paramString)
  {
    this.days = paramString;
  }
  
  public void setId(String paramString)
  {
    this.id = paramString;
  }
  
  public void setSignin_date(String paramString)
  {
    this.signin_date = paramString;
  }
}
