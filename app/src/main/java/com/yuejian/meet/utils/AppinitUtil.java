package com.yuejian.meet.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yuejian.meet.R;

import org.apache.lucene.util.Constants;

import java.util.List;

/**
 * Created by idea on 2016/7/19.
 */
public class AppinitUtil {
    public static String NET_ERROR_TIPS = "";

    public static String mwxCircle = "";//getString(R.string.wx_space);
    public static String mwxFriends = "";// getString(R.string.wx_friends);
    public static String mqqCircle = "";//getString(R.string.qq_space);
    public static String mqqFriends = "";// getString(R.string.qq_friends);
    public static String msinaSpace = "";// getString(R.string.sina_space);
    public static String mfacebookSpace = "";                     //    getString(R.string.facebook_space);
    public static String mtwitterSpace = "";  //            getString(R.string.twitter_space
    public static String mqrCode = "";  //            getString(R.string.twitter_space
    public static String homeMine = "";
    public static String homeFirstPager = "";
    public static String sms = "";
//    <string name="oto_minute_ago">minutes ago</string>
//    <string name="oto_hour_ago">hours ago</string>
//    <string name="oto_yesterday">yesterday</string>
//    <string name="oto_the_day_before_yesterday">day before yesterday</string>
//    <string name="oto_day_ago">days ago</string>
//    <string name="one_month_age">one month ago</string>
//    <string name="two_month_age">two months ago</string>
//    <string name="three_month_age">three months ago</string>

    public static String minuteAgo = "";
    public static String hourAgo = "";
    public static String yesterdayAgo = "";
    public static String beforeYesterdayAgo = "";
    public static String dayAgo = "";
    public static String oneMonthAgo = "";
    public static String twoMonthAgo = "";
    public static String threeMonthAgo = "";
    public static String message ="";
    public static String discover = "";
    public static String contact = "";
//    public static List<ShortBean> mShorBean;

//    public static void init(Context context, String channel) {
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context, "571dbc86e0f55abf950005da", channel));
//        PlatformConfig.setWeixin("wx1b08e67d219ab7eb", "7f2bc7600a2d4f50671808f16742ea52"); //微信 appid appsecret
//        PlatformConfig.setSinaWeibo("396099387", "935a9adaa1810b2cd1172b765b53d7bd"); //新浪微博 appkey appsecret
//        PlatformConfig.setQQZone("1105622681", "ciIN4XIoeYPxtZYM");// QQ和Qzone appid appkey
//        PlatformConfig.setTwitter("s373b06HpF2sq2TL9fHvYOVpC", "KNSgcP3iiJHmmmoBNGd6qoW8L4Ah2VmVPoyC4KjAL2h62eWjp3");
//        initLanguage(context);
//        UMShareAPI.get(context);
//    }

    public static void initLanguage(Context context) {
        NET_ERROR_TIPS = context.getString(R.string.net_error_tips);
        mwxCircle = context.getString(R.string.wx_space);
        mwxFriends = context.getString(R.string.wx_friends);
        mqqCircle = context.getString(R.string.qq_space);
        mqqFriends = context.getString(R.string.qq_friends);
        msinaSpace = context.getString(R.string.sina_space);
        sms = context.getString(R.string.txt_sms);
        homeMine = context.getString(R.string.tab_rbtn_mine);
        mfacebookSpace = context.getString(R.string.facebook_space);
        mtwitterSpace = context.getString(R.string.twitter_space);
        minuteAgo = context.getString(R.string.oto_minute_ago);
        hourAgo = context.getString(R.string.oto_hour_ago);
        yesterdayAgo = context.getString(R.string.oto_yesterday);
        beforeYesterdayAgo = context.getString(R.string.oto_the_day_before_yesterday);
        dayAgo = context.getString(R.string.oto_day_ago);
        oneMonthAgo = context.getString(R.string.one_month_age);
        twoMonthAgo = context.getString(R.string.two_month_age);
        threeMonthAgo = context.getString(R.string.three_month_age);
        message = context.getString(R.string.tab_rbtn_msg);
        discover = context.getString(R.string.txt_discover);
        mqrCode = context.getString(R.string.text_qr_code_to_praise);
        contact = context.getString(R.string.tab_rbtn_contact_list);
    }

//    public static boolean checkToken(Context mContext) {
//        try {
//            final String packname = mContext.getPackageName();
//            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packname, PackageManager.GET_SIGNATURES);
//            android.content.pm.Signature[] signs = packageInfo.signatures;
//            android.content.pm.Signature sign = signs[0];
//            int code = sign.hashCode();
//            String codes = Utils.md5(code + packname);
////            FCLoger.debug("codes------------  "+codes);
//            StringBuilder builder = new StringBuilder();
//            //3d543aae20120393c995bfa735516f1a
//            builder.append(mContext.getResources().getString(R.string.md5_token)).append(Constants.TOKEN);
//            String token = builder.toString();
//            return codes.equals(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
////        return true;
//
//    }


}
