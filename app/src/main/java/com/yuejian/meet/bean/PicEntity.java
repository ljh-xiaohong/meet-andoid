package com.yuejian.meet.bean;

/**
 * @author :
 * @time : 2018/11/17 17:45
 * @desc :
 * @version: V1.0
 * @update : 2018/11/17 17:45
 */

public class PicEntity {

  /**
   * id : 3
   * pic : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/rankpic/WdAsbQ5Jc5.png
   * url : http://www.yuejianchina.com
   */

  private int id;
  private String pic;
  private String url;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
