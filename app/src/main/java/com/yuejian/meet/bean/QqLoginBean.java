package com.yuejian.meet.bean;

import android.os.Parcel;
import android.os.Parcelable;

//{
//        is_yellow_vip=0,
//        yellow_vip_level=0,
//        profile_image_url=http: //q.qlogo.cn/qqapp/1105359504/6315F5AE85713DC5CE33E6A594085656/100,
//        screen_name=Joe,
//        msg=,
//        vip=0,
//        city=洛杉矶,
//        gender=男,
//        province=加利福尼亚,
//        level=0,
//        is_yellow_year_vip=0,
//        openid=6315F5AE85713DC5CE33E6A594085656
//        }

/**
 * 账单信息Bean
 *
 * @author lizhixin
 * @date 2016/4/20
 */
public class QqLoginBean implements Parcelable {
	public String is_yellow_vip;
	public String yellow_vip_level;
	public String profile_image_url;
	public String screen_name;
	public String msg;
	public String vip;
	public String city;
	public String gender;
	public String province;
	public String level;
	public String is_yellow_year_vip;
	public String openid;

	protected QqLoginBean(Parcel in) {
		is_yellow_vip = in.readString();
		yellow_vip_level = in.readString();
		profile_image_url = in.readString();
		screen_name = in.readString();
		msg = in.readString();
		vip = in.readString();
		city = in.readString();
		gender = in.readString();
		province = in.readString();
		level = in.readString();
		is_yellow_year_vip = in.readString();
		openid = in.readString();
	}

	public static final Creator<QqLoginBean> CREATOR = new Creator<QqLoginBean>() {
		@Override
		public QqLoginBean createFromParcel(Parcel in) {
			return new QqLoginBean(in);
		}

		@Override
		public QqLoginBean[] newArray(int size) {
			return new QqLoginBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(is_yellow_vip);
		dest.writeString(yellow_vip_level);
		dest.writeString(profile_image_url);
		dest.writeString(screen_name);
		dest.writeString(msg);
		dest.writeString(vip);
		dest.writeString(city);
		dest.writeString(gender);
		dest.writeString(province);
		dest.writeString(level);
		dest.writeString(is_yellow_year_vip);
		dest.writeString(openid);
	}
}
