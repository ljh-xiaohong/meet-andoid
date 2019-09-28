package com.yuejian.meet.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GetMessageBean;
import com.yuejian.meet.framents.message.NewMessageFragment;
import com.yuejian.meet.widgets.MessageTitleView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.view.DisplayCutout;
//import com.fsms.consumer.R;
//import com.fsms.consumer.activity.PhoneLoginActivity;
//import com.fsms.consumer.wxapi.WXEntryActivity;
//import com.google.gson.JsonObject;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

//import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;
//import cn.bingoogolapple.badgeview.BGABadgeImageView;
//import cn.bingoogolapple.badgeview.BGABadgeTextView;
//import cn.bingoogolapple.badgeview.BGABadgeViewHelper;
//
//import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;


/**
 * Copyright (c) 2014 All rights reserved
 * 名称：CommonUtil.java
 * 描述：工具类
 * Author: ljh
 * Date： 2015/1/19 13:19
 * Version：1.0
 */
public class CommonUtil {

    /*判断字符串是否为空*/
    public static boolean isNull(String text) {
        if (text == null || "".equals(text.trim()) || "null".equals(text))
            return true;
        return false;
    }

    /*检查手机号码用户名*/
    public static Boolean checkUserName(String userName) {
        if (userName.length() != 11 || !checkPhoneNum(userName)) {
            return false;
        }
        return true;
    }

    /*检查手机号码用户名*/
    public static Boolean checTelPhone(String userName) {
        if (userName.length() >= 7&&userName.length() <= 11 ) {
            return true;
        }
        return false;
    }

    /*检查用户密码*/
    public static Boolean checkPwd(String pwd) {
        if (pwd.length() != 6) {
            return false;
        }
        return true;
    }
    /*检查用户密码*/
    public static Boolean checkPwds(String pwd) {
        if (pwd.length() < 6||pwd.length()>10) {
            return false;
        }
        return true;
    }

    /*验证手机号码*/
    public static boolean checkPhoneNum(String userPhone) {
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(19[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(userPhone);
        return m.matches();
    }


    /*验证网址*/
    public static boolean checkUrl(String url) {
        String regExp = "^((https?|ftp|news):\\/\\/)?([a-z]([a-z0-9\\-]*[\\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\\/[a-z0-9_\\-\\.~]+)*(\\/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(url);
        return m.matches();
    }

    /*验证邮箱*/
    public static boolean checkEmail(String email) {
        String regExp ="^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /*隐藏手机号中间位数，用*号显示*/
    public static String compilePhone(String mobile) {
        return mobile.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*");
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /*验证身份证号是否符合规则*/
    public static boolean personIdValidation(String text) {
        if (CommonUtil.isNull(text)){
            return false;
        }
        String regx = "[0-9]{17}x";
        String regX = "[0-9]{17}X";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) ||text.matches(regX) || text.matches(reg1) || text.matches(regex);
    }
   /*保留小数*/
    public static String keepTheDecimal(String regular, double num){
        //regular--正则表达式
        DecimalFormat format = new DecimalFormat(regular);
        return format.format(num);
    }
//    /*数量圆圈标记*/
//    public static void changeBadgeIMG(BGABadgeImageView badgeImageView, String textBadge, int textSizeSp,
//                                      int textColor, int bgColor, boolean dragable, int padding, BGABadgeViewHelper.BadgeGravity gravity, int verticalMargin, int horizontalMargin){
//        if (!textBadge.equals("0")) {
//            badgeImageView.showCirclePointBadge();
//            //数值
//            badgeImageView.showTextBadge(textBadge);
//            //数字大小
//            badgeImageView.getBadgeViewHelper().setBadgeTextSizeSp(textSizeSp);
//            //数字颜色
//            badgeImageView.getBadgeViewHelper().setBadgeTextColorInt(textColor);
//            //圆点背景
//            badgeImageView.getBadgeViewHelper().setBadgeBgColorInt(bgColor);
//            //是否可拖动
//            badgeImageView.getBadgeViewHelper().setDragable(dragable);
//            //圆点大小
//            badgeImageView.getBadgeViewHelper().setBadgePaddingDp(padding);
//            //圆点位置
//            badgeImageView.getBadgeViewHelper().setBadgeGravity(gravity);
//            //圆点纵距
//            badgeImageView.getBadgeViewHelper().setBadgeVerticalMarginDp(verticalMargin);
//            //圆点横距
//            badgeImageView.getBadgeViewHelper().setBadgeHorizontalMarginDp(horizontalMargin);
//        } else {
//            badgeImageView.hiddenBadge();
//        }
//    }
//    /*数量圆圈标记*/
//    public static void changeBadgeFrameLayout(BGABadgeFrameLayout badgeImageView, String textBadge, int textSizeSp,
//                                              int textColor, int bgColor, boolean dragable, int padding, BGABadgeViewHelper.BadgeGravity gravity, int verticalMargin, int horizontalMargin){
//        if (!textBadge.equals("0")) {
//            badgeImageView.showCirclePointBadge();
//            //数值
//            badgeImageView.showTextBadge(textBadge);
//            //数字大小
//            badgeImageView.getBadgeViewHelper().setBadgeTextSizeSp(textSizeSp);
//            //数字颜色
//            badgeImageView.getBadgeViewHelper().setBadgeTextColorInt(textColor);
//            //圆点背景
//            badgeImageView.getBadgeViewHelper().setBadgeBgColorInt(bgColor);
//            //是否可拖动
//            badgeImageView.getBadgeViewHelper().setDragable(dragable);
//            //圆点大小
//            badgeImageView.getBadgeViewHelper().setBadgePaddingDp(padding);
//            //圆点位置
//            badgeImageView.getBadgeViewHelper().setBadgeGravity(gravity);
//            //圆点纵距
//            badgeImageView.getBadgeViewHelper().setBadgeVerticalMarginDp(verticalMargin);
//            //圆点横距
//            badgeImageView.getBadgeViewHelper().setBadgeHorizontalMarginDp(horizontalMargin);
//        } else {
//            badgeImageView.hiddenBadge();
//        }
//    }
//    /*数量圆圈标记*/
//    public static void changeBadgeTV(BGABadgeTextView badgeTextView, String textBadge, int textSizeSp,
//                                     int textColor, int bgColor, boolean dragable, int padding, BGABadgeViewHelper.BadgeGravity gravity, int verticalMargin, int horizontalMargin){
//        if (!textBadge.equals("0")) {
//            badgeTextView.showCirclePointBadge();
//            //数值
//            badgeTextView.showTextBadge(textBadge);
//            //数字大小
//            badgeTextView.getBadgeViewHelper().setBadgeTextSizeSp(textSizeSp);
//            //数字颜色
//            badgeTextView.getBadgeViewHelper().setBadgeTextColorInt(textColor);
//            //圆点背景
//            badgeTextView.getBadgeViewHelper().setBadgeBgColorInt(bgColor);
//            //是否可拖动
//            badgeTextView.getBadgeViewHelper().setDragable(dragable);
//            //圆点大小
//            badgeTextView.getBadgeViewHelper().setBadgePaddingDp(padding);
//            //圆点位置
//            badgeTextView.getBadgeViewHelper().setBadgeGravity(gravity);
//            //圆点纵距
//            badgeTextView.getBadgeViewHelper().setBadgeVerticalMarginDp(verticalMargin);
//            //圆点横距
//            badgeTextView.getBadgeViewHelper().setBadgeHorizontalMarginDp(horizontalMargin);
//        } else {
//            badgeTextView.hiddenBadge();
//        }
//    }
    /**
     * 对指定字符设置颜色
     *
     * @param str   字符串
     * @param ch1   切换颜色开始的字符
     * @param ch2   切换颜色停止的字符
     * @param color 设置的颜色
     * @param tv    TextView控件
     */
    public static void setTVColor(String str, char ch1, char ch2, int color, TextView tv) {
        int a = str.indexOf(ch1);
        int b = str.indexOf(ch2);
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(color), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }
    /**

     *  以最省内存的方式读取本地资源的图片

     *  @param context

     *  @param resId

     *  @return

     */

    public  static Bitmap readBitMap(Context context, int resId){

        BitmapFactory.Options opt = new  BitmapFactory.Options();

        opt.inPreferredConfig =  Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        //  获取资源图片

        InputStream is =  context.getResources().openRawResource(resId);

        return  BitmapFactory.decodeStream(is, null, opt);

    }
//    //微信登录
//    public static void wxLogin(Context context) {
//        if (!Constants.wx_api.isWXAppInstalled()) {
//            CustomToast.showToast(context,"您还未安装微信客户端");
//            return;
//        }
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wx_login";
//        Constants.wx_api.sendReq(req);
//        DadanPreference.getInstance(context).setString("state","wx_login");
//    }
//    //微信支付签名
//    public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
//        StringBuffer sb = new StringBuffer();
//        Set es = parameters.entrySet();
//        Iterator it = es.iterator();
//        while(it.hasNext()) {
//            Map.Entry entry = (Map.Entry)it.next();
//            String k = (String)entry.getKey();
//            Object v = entry.getValue();
//            if(null != v && !"".equals(v)
//                    && !"sign".equals(k) && !"key".equals(k)) {
//                sb.append(k + "=" + v + "&");
//            }
//        }
//        sb.append("key=" + Constants.SHOP_KEY);
//        System.out.println("签名："+sb.toString());
//        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
//        return sign;
//    }
    //length用户要求产生字符串的长度。

    public static String getRandomString(int length){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random=new Random();

        StringBuffer sb=new StringBuffer();

        for(int i=0;i<length;i++){

            int number=random.nextInt(62);

            sb.append(str.charAt(number));

        }

        return sb.toString();

    }
    //调用电话
    public static void call(Activity context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }
//    public static void loginErr(JSONObject result, Activity context){
//        if (result.optString(Constants.CODE_KEY).equals("1")) {
//            DadanPreference.getInstance(context).setTicket(result.optString(Constants.DATAOBJ_KEY));
//        } else if (result.optString(Constants.CODE_KEY).equals("-1")) {
//            Intent intent = new Intent(context, PhoneLoginActivity.class);
//            intent.putExtra("LogionCode", "-1");
//            context.startActivity(intent);
//            ActivityManager.getInstance(context).finishAllActivity();
//        }
//    }
   //价钱去0（如传入1.00->1）
    public static String price(Object phone) {
        String price = String.valueOf(phone);
        String[] prices = price.split("\\.");
        if (prices.length == 2) {
            if (prices[1].length() == 2) {
                if (prices[1].charAt(0) != '0') {
                    if (prices[1].charAt(1) != '0') {
                        return price;
                    } else {
                        return prices[0] + "." + prices[1].charAt(0);
                    }
                } else if (prices[1].charAt(1) != '0') {
                    return price;
                } else {
                    return prices[0];
                }
            } else {
                if (prices[1].charAt(0) != '0') {
                    return price;
                } else {
                    return prices[0];
                }
            }
        }else {
            return price;
        }
    }
    //好友
//    public static void shareRed(Context context,String url,String title,String description,int imgID,int what){
//        WXWebpageObject wxWebpageObject=new WXWebpageObject();
//        wxWebpageObject.webpageUrl=url;
//        WXMediaMessage wxMediaMessage=new WXMediaMessage(wxWebpageObject);
//        wxMediaMessage.title=title;
//        wxMediaMessage.description=description;
//        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),imgID);
//        wxMediaMessage.thumbData=bmpToByteArray(bitmap,true);
//        SendMessageToWX.Req req=new SendMessageToWX.Req();
//        req.transaction=buildTransaction("share");
//        req.message=wxMediaMessage;
//        req.scene=what;
//        Constants.wx_api.sendReq(req);
//    }
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
     //时间对比  "yyyy.MM.dd HH:mm"
     public static boolean compareDateTime(String selectMaxDate, String selectMinDate, String rex) {
        SimpleDateFormat sdf = new SimpleDateFormat(rex);//小写的mm表示的是分钟
        try {
            Date maxDate = sdf.parse(selectMaxDate);
            Date minDate = sdf.parse(selectMinDate);
            if (minDate.getTime() > maxDate.getTime()) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    //获取Imei
    public static String getDeviceId(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
//		if (mTelephony.getDeviceId() != null) {
//			id = mTelephony.getDeviceId();
//		} else {
        //android.provider.Settings;
        id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//		}
        return id;
    }



    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }
    /**
     * Android P 刘海屏判断
     * @param activity
     * @return
     */
//    public static DisplayCutout isAndroidP(Activity activity){
//        View decorView = activity.getWindow().getDecorView();
//        if (decorView != null && Build.VERSION.SDK_INT >= 28){
//            WindowInsets windowInsets = decorView.getRootWindowInsets();
//            if (windowInsets != null)
//                return windowInsets.getDisplayCutout();
//        }
//        return null;
//    }

    /**
     * 小米刘海屏判断.
     * @return 0 if it is not notch ; return 1 means notch
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key, Activity activity) {
        int result = 0;
        if (isXiaomi()){
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Integer(0);
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 华为刘海屏判断
     * @return
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException","hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("NoSuchMethodException","hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e( "Exception","hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    /**
     * VIVO刘海屏判断
     * @return
     */
    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("NoSuchMethodException", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Exception", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }
    /**
     * OPPO刘海屏判断
     * @return
     */
    public static boolean hasNotchAtOPPO(Context context) {
        return  context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * 透明度转换
     * @return
     */
    public static int changBg(float alpha) {
        return (int) (alpha*255);
    }

    /**
     * MD5 32位小写加密
     * @param encryptStr
     * @return
     */
    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }
}
