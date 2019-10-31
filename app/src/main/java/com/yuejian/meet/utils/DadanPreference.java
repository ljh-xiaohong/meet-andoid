package com.yuejian.meet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 内部存储
 */
public class DadanPreference {
	private static DadanPreference preference = null;
	private SharedPreferences sharedPreference;
	private String packageName = "";
	private final String TICKET = "TICKET";
	private final String QI_NIU_TOKEN = "QI_NIU_TOKEN";
	private final String NEW_QI_NIU_TOKEN = "NEW_QI_NIU_TOKEN";
	private Context context;

	public static  DadanPreference getInstance(Context context) {
		if (preference == null) {
			synchronized (DadanPreference.class) {
				if (preference == null) {
					preference = new DadanPreference(context);
				}
			}
		}
		return preference;
	}

	public DadanPreference(Context context) {
		this.context = context.getApplicationContext();
		packageName = context.getPackageName() + "_preferences";
		sharedPreference = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
	}

	public void setFloat(String key, float value) {
		Editor editor = sharedPreference.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public float getFloat(String key) {
		return sharedPreference.getFloat(key, 0.0f);
	}
	public void setInt(String key, int value) {
		Editor editor = sharedPreference.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key) {
		return sharedPreference.getInt(key, 0);
	}
	public void setLong(String key, long value) {
		Editor editor = sharedPreference.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key) {
		return sharedPreference.getLong(key, 0);
	}

	public void setString(String key, String value) {
		Editor editor = sharedPreference.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return sharedPreference.getString(key, "");
	}

	public void setBoolean(String key, boolean value) {
		Editor editor = sharedPreference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
	 * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
	 *
	 * @param object 待加密的转换为String的对象
	 * @return String   加密后的String
	 */
//	private static String Object2String(Object object) {
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		ObjectOutputStream objectOutputStream = null;
//		try {
//			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//			objectOutputStream.writeObject(object);
//			String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
//			objectOutputStream.close();
//			return string;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * 使用Base64解密String，返回Object对象
	 *
	 * @param objectString 待解密的String
	 * @return object      解密后的object
	 */
//	private static Object String2Object(String objectString) {
//		byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
//		ObjectInputStream objectInputStream = null;
//		try {
//			objectInputStream = new ObjectInputStream(byteArrayInputStream);
//			Object object = objectInputStream.readObject();
//			objectInputStream.close();
//			return object;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * 使用SharedPreference保存对象
	 *
	 * @param fileKey    储存文件的key
	 * @param key        储存对象的key
	 * @param saveObject 储存的对象
	 */
//	public  void save(String fileKey, String key, Object saveObject) {
//		SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
//		SharedPreferences.Editor editor = sharedPreferences.edit();
//		String string = Object2String(saveObject);
//		editor.putString(key, string);
//		editor.commit();
//	}

	/**
	 * 获取SharedPreference保存的对象
	 *
	 * @param fileKey 储存文件的key
	 * @param key     储存对象的key
	 * @return object 返回根据key得到的对象
	 */
//	public Object get(String fileKey, String key) {
//		SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
//		String string = sharedPreferences.getString(key, null);
//		if (string != null) {
//			Object object = String2Object(string);
//			return object;
//		} else {
//			return null;
//		}
//	}
	/**
	 * 保存List
	 * @param tag
	 * @param datalist
	 */
	public <T> void setDataList(String tag, List<T> datalist) {
//		if (null == datalist || datalist.size() <= 0)
//			return;
		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
		Editor editor = sharedPreference.edit();
		editor.putString(tag, strJson);
		editor.commit();

	}
	/**
	 * 获取List
	 * @param tag
	 * @return
	 */
	public <T> List<T> getDataList(String tag, Class<T> clazz) {
		List<T> datalist=new ArrayList<T>();
		String strJson = sharedPreference.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
		for(final JsonElement elem : array){
			datalist.add(new Gson().fromJson(elem, clazz));
		}
		return datalist;

	}
	public boolean getBoolean(String key){
		return sharedPreference.getBoolean(key, false);
	}

	public void setQNToken(String id){
		Editor editor = sharedPreference.edit();
		editor.putString(QI_NIU_TOKEN, id);
		editor.commit();
	}

	public String getQNToken(){
		return sharedPreference.getString(QI_NIU_TOKEN, "-1");
	}
	public boolean hasQNToken(){
		return sharedPreference.contains(QI_NIU_TOKEN);
	}
	public void removeQNToken(){
		Editor editor = sharedPreference.edit();
		editor.putString(QI_NIU_TOKEN, "-1");
		editor.commit();
	}

	
	public void setTicket(String id){
		Editor editor = sharedPreference.edit();
		editor.putString(TICKET, id);
		editor.commit();
	}
	
	public String getTicket(){
		return sharedPreference.getString(TICKET, "-1");
	}
	public boolean hasTicket(){
		return sharedPreference.contains(TICKET);
	}
	public void removeTicket(){
		Editor editor = sharedPreference.edit();
		editor.putString(TICKET, "-1");
		editor.commit();
	}
	public String  serialize(Object obj){
		try {
			long startTime = System.currentTimeMillis();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			String serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
			objectOutputStream.close();
			byteArrayOutputStream.close();
			Log.d("serial", "serialize str =" + serStr);
			long endTime = System.currentTimeMillis();
			Log.d("serial", "序列化耗时为:" + (endTime - startTime));
			return serStr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 反序列化对象
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public <T>T diserialization(String str,Class<T> cls){
		long startTime = System.currentTimeMillis();
		try {
			String redStr = java.net.URLDecoder.decode(str, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			T t = (T) objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			long endTime = System.currentTimeMillis();
			Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
			return t;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
