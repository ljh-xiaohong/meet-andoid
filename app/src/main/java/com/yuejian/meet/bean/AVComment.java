package com.yuejian.meet.bean;

import java.util.List;

public class AVComment
{
  public String age;
  public String av_comment_content;
  public String av_comment_createtime;
  public String av_comment_id;
  public String av_comment_praise_cnt;
  public String av_comment_type;
  public String av_id;
  public String av_reply_cnt;
  public String customer_id;
  public String family_area;
  public String job;
  public String name;
  public String op_customer_id;
  public String photo;
  public String praise_status;
  public List<Rely> rely_list;
  public String sex;
  public String surname;
  
  public String getAge()
  {
    return this.age;
  }
  
  public String getAv_comment_content()
  {
    return this.av_comment_content;
  }
  
  public String getAv_comment_createtime()
  {
    return this.av_comment_createtime;
  }
  
  public String getAv_comment_id()
  {
    return this.av_comment_id;
  }
  
  public String getAv_comment_praise_cnt()
  {
    return this.av_comment_praise_cnt;
  }
  
  public String getAv_comment_type()
  {
    return this.av_comment_type;
  }
  
  public String getAv_id()
  {
    return this.av_id;
  }
  
  public String getAv_reply_cnt()
  {
    return this.av_reply_cnt;
  }
  
  public String getCustomer_id()
  {
    return this.customer_id;
  }
  
  public String getFamily_area()
  {
    return this.family_area;
  }
  
  public String getJob()
  {
    return this.job;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getOp_customer_id()
  {
    return this.op_customer_id;
  }
  
  public String getPhoto()
  {
    return this.photo;
  }
  
  public String getPraise_status()
  {
    return this.praise_status;
  }
  
  public List<Rely> getRely_list()
  {
    return this.rely_list;
  }
  
  public String getSex()
  {
    return this.sex;
  }
  
  public String getSurname()
  {
    return this.surname;
  }
  
  public void setAge(String paramString)
  {
    this.age = paramString;
  }
  
  public void setAv_comment_content(String paramString)
  {
    this.av_comment_content = paramString;
  }
  
  public void setAv_comment_createtime(String paramString)
  {
    this.av_comment_createtime = paramString;
  }
  
  public void setAv_comment_id(String paramString)
  {
    this.av_comment_id = paramString;
  }
  
  public void setAv_comment_praise_cnt(String paramString)
  {
    this.av_comment_praise_cnt = paramString;
  }
  
  public void setAv_comment_type(String paramString)
  {
    this.av_comment_type = paramString;
  }
  
  public void setAv_id(String paramString)
  {
    this.av_id = paramString;
  }
  
  public void setAv_reply_cnt(String paramString)
  {
    this.av_reply_cnt = paramString;
  }
  
  public void setCustomer_id(String paramString)
  {
    this.customer_id = paramString;
  }
  
  public void setFamily_area(String paramString)
  {
    this.family_area = paramString;
  }
  
  public void setJob(String paramString)
  {
    this.job = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setOp_customer_id(String paramString)
  {
    this.op_customer_id = paramString;
  }
  
  public void setPhoto(String paramString)
  {
    this.photo = paramString;
  }
  
  public void setPraise_status(String paramString)
  {
    this.praise_status = paramString;
  }
  
  public void setRely_list(List<Rely> paramList)
  {
    this.rely_list = paramList;
  }
  
  public void setSex(String paramString)
  {
    this.sex = paramString;
  }
  
  public void setSurname(String paramString)
  {
    this.surname = paramString;
  }
  
  public class Rely
  {
    public String av_comment_content;
    public String av_comment_id;
    public String customer_id;
    public String father_comment_id;
    public String name;
    public String op_customer_id;
    public String op_name;
    public String op_surname;
    public String surname;
    
    public Rely() {}
    
    public String getAv_comment_content()
    {
      return this.av_comment_content;
    }
    
    public String getAv_comment_id()
    {
      return this.av_comment_id;
    }
    
    public String getCustomer_id()
    {
      return this.customer_id;
    }
    
    public String getFather_comment_id()
    {
      return this.father_comment_id;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getOp_customer_id()
    {
      return this.op_customer_id;
    }
    
    public String getOp_name()
    {
      return this.op_name;
    }
    
    public String getOp_surname()
    {
      return this.op_surname;
    }
    
    public String getSurname()
    {
      return this.surname;
    }
    
    public void setAv_comment_content(String paramString)
    {
      this.av_comment_content = paramString;
    }
    
    public void setAv_comment_id(String paramString)
    {
      this.av_comment_id = paramString;
    }
    
    public void setCustomer_id(String paramString)
    {
      this.customer_id = paramString;
    }
    
    public void setFather_comment_id(String paramString)
    {
      this.father_comment_id = paramString;
    }
    
    public void setName(String paramString)
    {
      this.name = paramString;
    }
    
    public void setOp_customer_id(String paramString)
    {
      this.op_customer_id = paramString;
    }
    
    public void setOp_name(String paramString)
    {
      this.op_name = paramString;
    }
    
    public void setOp_surname(String paramString)
    {
      this.op_surname = paramString;
    }
    
    public void setSurname(String paramString)
    {
      this.surname = paramString;
    }
  }
}
