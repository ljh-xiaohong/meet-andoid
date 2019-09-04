package com.yuejian.meet.utils;

import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.api.http.UrlConstant;
import java.util.Date;

/**
 * 阿里Oss utils类
 * Created by zh03 on 2017/6/29.
 */

public class OssUtils {
    public static int ossIndex=0;

    /**
     * 获取以time命名的视频名
     * @return
     */
    public static  String getTimeNmaeVideo(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName=( new Date().getTime()+ossIndex )+ ".mp4";
        }else{
            imgName=AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+ ".mp4";
        }
        ossIndex++;
        return "android_video_audios/"+imgName;
    }
    /**
     * 获取以time命名的视频名
     * @return
     */
    public static  String getTimeNmaeVideoHorizontal(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName=( new Date().getTime()+ossIndex )+ ".mp4";
        }else{
            imgName=AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+"_hengping"+ ".mp4";
        }
        ossIndex++;
        return "android_video_audios/"+imgName;
    }
    /**
     * 获取以time命名的jpg图片名
     * @return
     */
    public static  String getTimeNmaeJpg(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName=( new Date().getTime()+ossIndex )+ ".jpg";
        }else{
            imgName=AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+ ".jpg";
        }
        ossIndex++;
        return "photoalbum/"+imgName;
    }
    /**
     *保存图片图片名
     * @return
     */
    public static  String saveJpg(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName=( new Date().getTime()+ossIndex )+ ".jpg";
        }else{
            imgName=AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+ ".jpg";
        }
        ossIndex++;
        return imgName;
    }
    /**
     * 获取以time命名的jpg图片名
     * @return
     */
    public static  String getTimeNmaeJpgHorizontal(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName=( new Date().getTime()+ossIndex )+ ".jpg";
        }else{
            imgName=AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+"_hengping"+ ".jpg";
        }
        ossIndex++;
        return "photoalbum/"+imgName;
    }
    /**
     * 获取oss上传名加跟根目录
     * @return
     */
    public static String getNewOssUploadingName(){
        String imgName="";
        if ( AppConfig.userEntity==null){
            imgName="photoalbum/"+( new Date().getTime()+ossIndex )+ ".jpg";
        }else{
            imgName="photoalbum/"+AppConfig.userEntity.customer_id + (new Date().getTime()+ossIndex )+ ".jpg";
        }
        ossIndex++;
        return imgName;
    }

    /**
     * 获取oss上传的url
     * @param ossUploadingName   上传oss的名
     * @return
     */
    public static String getOssUploadingUrl(String ossUploadingName){
        return UrlConstant.downloadObject+ossUploadingName;
    }
}
