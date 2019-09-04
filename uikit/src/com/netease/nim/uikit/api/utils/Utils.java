package com.netease.nim.uikit.api.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import taobe.tec.jcc.JChineseConvertor;


public class Utils {


    public static final String EDAMS[] = {"zh", "en"};

    public static Locale getSystemLocale() {
        Locale l = Locale.getDefault();
        String def = "en";
        for (int i = 0; i < EDAMS.length; i++) {
            if (EDAMS[i].equals(l.getLanguage())) {
                def = EDAMS[i];
                break;
            }
        }
        Locale nLocale = null;
        if ("zh".equals(def)) {
            if ("CN".equals(l.getCountry())) {
                nLocale = Locale.SIMPLIFIED_CHINESE;
            } else {
                nLocale = Locale.TRADITIONAL_CHINESE;
            }
        } else {
            nLocale = new Locale(def);
        }
        return nLocale;
    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);

                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.00;
        }
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String getFromAssets(String fileName, Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNotNull(String str) {
        if (str == null || "null".equals(str.trim().toLowerCase())) {
            return "";
        }
        return str.trim();
    }


    public static String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 以JSON形式打印结果字符串
     */
    public static void print(String result) {
//        if (!AppConfig.isLOG) {
//            return;
//        }
        String json = "" + result;
        try {
            if (result.startsWith("{")) {
                json = new JSONObject(result).toString(4);
            } else if (result.startsWith("[")) {
                json = new JSONArray(result).toString(4);
            }
        } catch (Exception e) {
        }

        int parts = json.length() / 4000;
        int start = 0;
        for (int i = 0; i < parts; i++) {
            int end = start + 4000;
            start = end;
        }
    }

    /**
     * 返回一串文字是否含有数字和字母。
     *
     * @param str
     * @return
     */
    public static boolean isTextOrAlphabet(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) { //循环遍历字符串
            if (Character.isDigit(str.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        /*循环完毕以后
         *如果isDigit为true，则代表字符串中包含数字，否则不包含
         *如果isLetter为true，则代表字符串中包含字母，否则不包含
         */
        if (isDigit && isLetter) {
            return true;
        }
        return false;

    }

//    @SuppressWarnings("unchecked")
//    public static Map<String, Object> parseJsonStrObj(String json) {
//        try {
//            return new Gson().fromJson(json, Map.class);
//        } catch (Exception e) {
//            return new HashMap<String, Object>();
//        }
//    }

//    public static Map<String, String> parseJsonStr(String json) {
//        try {
//            return new Gson().fromJson(json, Map.class);
//        } catch (Exception e) {
//            return new HashMap<String, String>();
//        }
//    }


//    @SuppressWarnings("unchecked")
//    public static List<Map<String, String>> parseJsonStr2(String json) {
//        try {
//            return new Gson().fromJson(json, List.class);
//        } catch (Exception e) {
//            return new ArrayList<Map<String, String>>();
//        }
//    }


    /**
     * 设置Textview 下划线
     */
    public static void setUnderline(TextView view) {
        view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * 键盘弹出
     *
     * @param activity
     */
    public static void setInput(Activity activity) {
        final View rootView = ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        Rect rect = new Rect();
                        decorView.getWindowVisibleDisplayFrame(rect);
                        int screenHeight = decorView.getRootView().getHeight();
                        int heightDifferent = screenHeight - rect.bottom;
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView
                                .getLayoutParams();
                        lp.setMargins(0, 0, 0, heightDifferent);
                        rootView.requestLayout();
                    }
                });
    }

    /**
     * 字符串中是否为纯数字
     *
     * @param str 字符串
     * @return 纯数字型字符返回true，否则返回false。
     */
    public static boolean isOnlyNumber(String str) {
        if (str != null) {
            return str.matches("\\d+");
        }
        return false;
    }


    /**
     * SD卡是否可用
     *
     * @return 可用返回true，不可用返回false
     */
    public static boolean isSDCardEnabled() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;// SD卡可用
        }
        return false;
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            // String check =
            // "(?=^[\\w.@]{6,50}$)\\w+@\\w+(?:\\.[\\w]{2,3}){1,2}";
            String check = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {

            flag = false;
        }

        return flag;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }


    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        try {
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;

    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
//
//    public static String getMd5ByFile(File file) throws FileNotFoundException {
//        String value = null;
//        FileInputStream in = new FileInputStream(file);
//        try {
//            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = bi.toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != in) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        FCLoger.debug("file md5========" + value);
//        return value;
//    }


    /**
     * 计算给定字符串的总和
     *
     * @param strs 字符串数组。
     * @return 返回总和。
     */
    public static float add(String... strs) {
        if (strs == null) {
            return 0;
        }
        float f0 = toFloat(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            f0 = add(f0, toFloat(strs[i]));
        }
        return f0;
    }

    /**
     * 计算f1和f2之和
     */
    public static float add(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.add(b2).floatValue();
    }

    /**
     * 检查字符串是否为数字类型
     *
     * @param str 字符串
     * @return 为数字类型返回true，反之返回false
     */
    public static boolean isNumber(String str) {
        if (str != null) {
            return str.matches("-?\\d+\\.?\\d*");
        }
        return false;
    }

    /**
     * 将字符串str转为float类型，默认返回0
     *
     * @param str 字符串
     * @return 返回对应float值，如果str格式不对则返回0.0
     */
    public static float toFloat(String str) {
        float f = 0;
        if (isNumber(str)) {
            f = Float.valueOf(str);
        }
        return f;
    }

    /**
     * 格式化价格，保留小数点两位数。
     *
     * @param str 数字型字符串
     * @return 返回处理过的数字型字符串
     */
    @SuppressLint("DefaultLocale")
    public static String formatPrice(String str) {
        float f = toFloat(str);
        int i = (int) f;
        if (f == i) {
            return i + ".00";
        } else {
            return String.format("%.2f", f);
        }
    }

//    // 判断当前的网络状态
//    public static boolean isNetLink(Context c) {
//        // 连网管理
//        ConnectivityManager connectivityManager = (ConnectivityManager) c
//                .getSystemService(c.CONNECTIVITY_SERVICE);
//        // 网络工作信息
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo == null || !networkInfo.isAvailable()) { // 当前无可用网络
//            ViewInject.toast(c, AppinitUtil.NET_ERROR_TIPS);
//            return false;
//        } else { // 当前有可用网络
//            return true;
//        }
//    }

    public static void hideSystemKeyBoard(Context mcontext, View v) {
        InputMethodManager imm = (InputMethodManager) (mcontext)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */

    public static Long getLongDate() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String dateString = formatter.format(currentTime);

        long currentTimeLong = 0;
        Date date2 = null; // String类型转成date类型
        try {
            date2 = stringToDate(dateString, "yyyy-MM-dd");
            currentTimeLong = dateToLong(date2); // date类型转成long类型

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTimeLong;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        if (checkDeviceHasNavigationBar(aty)) {
            return dm.heightPixels - getNavigationBarHeight(aty);
        }
        return dm.heightPixels;
    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 是否是华为
     */
    public static boolean isHUAWEI() {
        return android.os.Build.MANUFACTURER.equals("HUAWEI");
    }
    public static String s2tOrT2s(String s){
        String data="";
        if (AppConfig.language.equals("1")) {
            try {
                JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
                data=jChineseConvertor.t2s(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            data=jChineseConvertor.s2t(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
