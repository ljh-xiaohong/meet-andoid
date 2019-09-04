package com.yuejian.meet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 文件管理器
 * 
 * @author zhixin.li
 * 
 */
public class PreferencesUtil {

	/** 存储的SharedPreferences名称 */
	public static final String SHARED_PREFERENCES_NAME = "meet_android";
	/** 是否是第一次运行程序，true是第一次运行，false不是第一次运行。 */
	public static final String KEY_IS_FIRST_RUN = "key_is_first_run";
	public static final String KEY_NEAR_BY_IS_FIRST_FILTER = "KEY_NEAR_BY_IS_FIRST_FILTER";
	public static final String KEY_HOT_IS_FIRST_FILTER = "KEY_HOT_IS_FIRST_FILTER";
	public static final String KEY_NEW_IS_FIRST_FILTER = "KEY_NEW_IS_FIRST_FILTER";
	public static final String KEY_CONTACT_IS_FIRST_FILTER = "KEY_CONTACT_IS_FIRST_FILTER";
	/** 登录之后的信息 */
	public static final String KEY_USER_INFO = "key_user_info";
	public static final String KEY_OPEN_ID = "openid";
	public static final String KEY_UNION_ID = "unionid";
	public static final String KEY_FLAG = "flag";
	/** 记住登录手机号 */
	public static final String KEY_USER_MOBILE = "key_user_mobile";
	public static final String KEY_USER_MOBILE_NEW = "key_user_mobile_new";
	/** 记住登录密码 */
	public static final String KEY_USER_MD5 = "key_user_MD5";
	public static final String KEY_USER_PWD = "key_user_pwd";
	public static final String KEY_USER_PWD_FLAG = "key_user_pwd_flag";
	public static final String ONLY_WIFI = "only_wifi";
	public static final String KEY_GIFT_INFO = "key_gift_info";//保存礼物状态
	public static final String KEY_SET_PWD = "key_set_pwd";//保存礼物状态
	public static final String KEY_PERSON_INFO = "key_person_info";//保存个人资料数据
	public static final String KEY_MUSIC_SWITCH = "key_music_switch";//保存控制声音的布尔值
	public static final String KEY_VIBRATE_SWITCH = "key_vibrate_switch";//保存控制振动的布尔值
	public static final String KEY_LOAD_FRIEND= "key_load_friend";
	public static final String KEY_CUSTOMERID = "key_customerid";
	public static final String KEY_TOKEN = "key_token";
	public static final String KEY_ENJOY= "key_Enjoy";//共享会
	/** 保存总资产 */
	public static final String KEY_TOTAL_BAL= "key_total_bal";
	public static final String KEY_FIND_RATIO= "key_find_material_ratio";//用户资料的%值


	public static final String INVITATION_HELP_TAG = "INVITATION_HELP_KEY";
	public static final String REQUEST_HELP_TAG = "REQUEST_HELP_KEY";
	public static final String INVITATION_CAPTURE_HELP_KEY = "INVITATION_CAPTURE_HELP_KEY";
	public static final String INVITATION_RENDER_HELP_KEY = "INVITATION_RENDER_HELP_KEY";
	public static final String REQUEST_CAPTURE_HELP_KEY = "REQUEST_CAPTURE_HELP_KEY";
	public static final String REQUEST_RENDER_HELP_KEY = "REQUEST_RENDER_HELP_KEY";
	public static final String SHAREUPDATE = "SHARE_UPDATE";
	public static final String SHARE_IMEI = "SHARE_IMEI";
	public static final String IS_ONLINE = "is_online";
	public static final String isReportLocation = "isReportLocation";
	public static final String CITY = "CITY";
	public static final String longitude = "longitude";
	public static final String latitude = "latitude";
	public static final String bug_userName ="bug_userName" ;
	public static final String bug_userPhone ="bug_userPhone" ;
	public static final String KEY_SDK_UPDATE ="key_sdk_update";

	////位置
	public static final String LATITUDE="LATITUDE";
	public static final String LONGITUDE="LONGITUDE";
	//	public static final String CITY="CITY";
	public static final String DISTRICT="DISTRICT";
	public static final String PROVINCE="PROVINCE";
	public static final String isGiftDownload="isGiftDownload";//礼物是否已经下载本地
	public static final String siginDate="siginDate";//签到日期
	public static final String isPhone="isPhone";//是否绑定手机
	public static final String Langguage="Langguage";//语言
	public static final String Company_name="company_name";//

	/**
	 * 保存数据到文件
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void put(Context context, String key, String value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}

	/**
	 * 保存数据到文件
	 * 
	 * @param context
	 * @param map
	 */
	public static void put(Context context, HashMap<String, String> map) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();

		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}

	/**
	 * 从文件中读取数据
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            默认值，如果当前获取不到数据就返回它
	 * @return
	 */
	public static String get(Context context, String key, String defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE).getString(key, defValue);
	}

	/**
	 * 删除单个字段
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE).edit().remove(key).commit();
	}

	/**
	 * 删除全部字段
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE).edit().clear().commit();
	}

	/**
	 * 反射机制存储数据
	 *  @param context
	 * @param classz
	 */
	public static void put(Context context, Object classz) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		try {
			Field[] fields = Object.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if (!"CREATOR".equals(name)) {
					editor.putString(name, (String) field.get(classz));
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		editor.commit();
	}




	public static void write(Context context, String k, int v) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(k, v);
		editor.commit();
	}

	public static void write(Context context, String k,
							 boolean v) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putBoolean(k, v);
		editor.commit();
	}

	public static void write(Context context, String k,
							 String v) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(k, v);
		editor.commit();
	}

	public static int readInt(Context context,  String k) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getInt(k, 0);
	}

	public static int readInt(Context context, String k,
							  int defv) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getInt(k, defv);
	}

	public static boolean readBoolean(Context context, String k) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, false);
	}

	public static boolean readBoolean(Context context,
									  String k, boolean defBool) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, defBool);
	}

	public static String readString(Context context,  String k) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getString(k, null);
	}

	public static String readString(Context context,  String k,
									String defV) {
		SharedPreferences preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return preference.getString(k, defV);
	}


	public static void clean(Context cxt, String fileName) {
		SharedPreferences preference = cxt.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.clear();
		editor.commit();
	}
}
