package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * Created by jw on 2016/10/28.
 */

public class DynamicPrivatePicBean implements Serializable{
    public String actionPhoto;
    public String flag;
     public DynamicPrivatePicBean(String actionPhoto, String flag){
        this.actionPhoto=actionPhoto;
        this.flag=flag;
    }
}
