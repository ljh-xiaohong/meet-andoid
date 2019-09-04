package com.yuejian.meet.bean;

import android.os.Parcel;
import android.os.Parcelable;

//{
//    public String     public String unionid=ouwDNwjg3hi_BJUQYpvPxxhy_0xA,
//    public String     public String country=,
//    public String     public String nickname=null,
//    public String     public String city=,
//    public String     public String province=,
//    public String     public String language=zh_CN,
//    public String     public String headimgurl=http: //wx.qlogo.cn/mmopen/PiajxSqBRaEKNG73CvYU8M8iaA3j2iaiaaeWH8HiauMTNAeDjdxwkH2PqadRUWGVuPvm4ek7N96vrHokF7eW0SB5TNA/0,
//    public String     public String sex=1,
//    public String     public String openid=o-Ve6wVc7Yn288dQBg8H_W7D-cds
//}

/**
 * 账单信息Bean
 *
 * @author lizhixin
 * @date 2016/4/20
 */
public class WxLoginBean implements Parcelable {


            public String access_token;// "--",
            public String city;// ",
            public String country;// ",
            public String expires_in;// "1487045399436",
            public String gender;// ",
            public String iconurl;// "http://wx..cn///0",
            public String language;// "zh_CN",
            public String name;//
            public String openid;// "oyV4sw5E8W0hFzel630_ZdVkKQeM",
            public String profile_image_url;// "http://wx..cn/mmopen/,
            public String province;// ",
            public String refresh_token;// "-",
            public String screen_name;// "",
            public String uid;// "ouwDNwmZJ8QIsPTDsFtpY02lSSFw",
            public String unionid;// ""


    protected WxLoginBean(Parcel in) {
        access_token = in.readString();
        city = in.readString();
        country = in.readString();
        expires_in = in.readString();
        gender = in.readString();
        iconurl = in.readString();
        language = in.readString();
        name = in.readString();
        openid = in.readString();
        profile_image_url = in.readString();
        province = in.readString();
        refresh_token = in.readString();
        screen_name = in.readString();
        uid = in.readString();
        unionid = in.readString();
    }

    public static final Creator<WxLoginBean> CREATOR = new Creator<WxLoginBean>() {
        @Override
        public WxLoginBean createFromParcel(Parcel in) {
            return new WxLoginBean(in);
        }

        @Override
        public WxLoginBean[] newArray(int size) {
            return new WxLoginBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(expires_in);
        dest.writeString(gender);
        dest.writeString(iconurl);
        dest.writeString(language);
        dest.writeString(name);
        dest.writeString(openid);
        dest.writeString(profile_image_url);
        dest.writeString(province);
        dest.writeString(refresh_token);
        dest.writeString(screen_name);
        dest.writeString(uid);
        dest.writeString(unionid);
    }
}
