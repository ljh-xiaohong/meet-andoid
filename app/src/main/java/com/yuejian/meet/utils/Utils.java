package com.yuejian.meet.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.mcxiaoke.bus.Bus;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.decode.QRDecode;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.util.C;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.emoji.EmojiAdapter;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanPhotoActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.BusMessage;
import com.yuejian.meet.bean.VersionEntity;
import com.yuejian.meet.widgets.gallery.ViewPagerGallery;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import taobe.tec.jcc.JChineseConvertor;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.provider.Settings.Global.putInt;

public class Utils {
    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

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

    /**
     * 分享有奖链接
     *
     * @param targetCustomerId
     * @param shareCode        分享码
     * @return
     */
//    public static String shareUrl(String targetCustomerId, String shareCode) {
//        targetCustomerId = TextUtils.isEmpty(targetCustomerId) ? "" : targetCustomerId;
//        shareCode = TextUtils.isEmpty(shareCode) ? "" : shareCode;
//        return UrlConstant.shareUrl() + "view/share.html?targetCustomerId=" + targetCustomerId + "&code=" + shareCode;
//    }

    /**
     * 点击“获取验证码”之后进行倒计时
     */
    public static void countDown(final TextView tv) {
        tv.setClickable(false);
        tv.setText("120s");
//        tv.setBackgroundResource(R.drawable.shape_code_bg);
        new Handler() {
            private int i = 119;

            @Override
            public void handleMessage(Message msg) {
                if (i > 0) {
                    tv.setText("再次发送" + i + "秒");
                    sendEmptyMessageDelayed(0, 1000);
                    i--;
                } else {
                    tv.setClickable(true);
                    tv.setText(R.string.text_cxhq);
//                    tv.setBackgroundResource(R.drawable.shape_code_select_bg);
                }
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    /**
     * @param seconds        倒数秒数
     * @param tv             显示TextView
     * @param timeoutContent 显示内容
     *                       <p>
     *                       timeoutContent为倒数结束时所显示的内容，若此值为null，则显示原始数据
     */
    public static void countDown(int seconds, TextView tv, String timeoutContent, @DrawableRes int unpress, @DrawableRes int press) {
        if (null != tv) {
            tv.setClickable(false);
            tv.setBackgroundResource(unpress);
            String org = tv.getText().toString();
            tv.setText(seconds + "s");
            new Handler() {
                private int index = seconds;

                @Override
                public void handleMessage(Message msg) {

                    if (index > 0) {
                        tv.setText(index + "s");
                        sendEmptyMessageDelayed(0, 1000);
                        index--;
                    } else {
                        tv.setClickable(true);
                        tv.setBackgroundResource(press);
                        tv.setText(timeoutContent != null ? timeoutContent : org);
                    }

                }
            }.sendEmptyMessageDelayed(0, 1000);
        }
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
//    public static void print(String result) {
//        if (!AppConfig.isLOG) {
//            return;
//        }
//        String json = "" + result;
//        try {
//            if (result.startsWith("{")) {
//                json = new JSONObject(result).toString(4);
//            } else if (result.startsWith("[")) {
//                json = new JSONArray(result).toString(4);
//            }
//        } catch (Exception e) {
//        }
//
//        int parts = json.length() / 4000;
//        int start = 0;
//        for (int i = 0; i < parts; i++) {
//            int end = start + 4000;
//            FCLoger.debug(json.substring(start, end));
//            start = end;
//        }
//        FCLoger.debug(json.substring(start));
//    }

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

    public static List<Map<String, Object>> parseJsonStr3(String json) {
        try {
            return new Gson().fromJson(json, List.class);
        } catch (Exception e) {
            return new ArrayList<Map<String, Object>>();
        }
    }


    @SuppressWarnings("rawtypes")
    public static String map2Json(Map map) {
        return new Gson().toJson(map, Map.class);
    }


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
        String telRegex = "^[1][345789]\\d{9}$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
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
    public static boolean isNetLink() {
        Context c = MyApplication.context;
        // 连网管理
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(c.CONNECTIVITY_SERVICE);
        // 网络工作信息
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) { // 当前无可用网络
            ViewInject.toast(c, AppinitUtil.NET_ERROR_TIPS);
            return false;
        } else { // 当前有可用网络
            return true;
        }
    }

    /**
     * 隐藏键盘
     *
     * @param mcontext
     * @param v
     */
    public static void hideSystemKeyBoard(Context mcontext, View v) {
        InputMethodManager imm = (InputMethodManager) (mcontext)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (v.getWindowToken() == null) return;
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

    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    //此方法只是关闭软键盘
    public static void hintKbTwo(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * API19 之后选择视频
     */
    public void chooseVideoFromLocalKitKat(Activity activity, int requstCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(intent, requstCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, com.netease.nim.uikit.R.string.gallery_invalid, Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {

        }
    }

    /**
     * API19 之前选择视频
     */
    public void chooseVideoFromLocalBeforeKitKat(Activity activity, int requstCode) {
        Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mIntent.setType(C.MimeType.MIME_VIDEO_ALL);
        mIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(mIntent, requstCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, com.netease.nim.uikit.R.string.gallery_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isMIUI() {
        String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    // 检测Flyme
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void setListViewHeightBasedOnChildren(Context context, ListView listView, int bottom) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            listItem.measure(w, h);
            int itemHeight = listItem.getMeasuredHeight();
            Log.d("itemHeight", "i: " + itemHeight);
            totalHeight += itemHeight;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int bottomHeight = 0;
        if (listView.getChildCount() > 0) {
            bottomHeight = DensityUtils.dip2px(context, bottom);
        }
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + bottomHeight;
        listView.setLayoutParams(params);
        listView.invalidate();
    }

    public static void showKB(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    public static void umengShareForPhatForm(SHARE_MEDIA share_media, Activity activity, Bitmap shareLogo, String title, String description, String shareUrl) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, shareLogo));  //缩略图
        web.setDescription(description);//描述
        new ShareAction(activity).withMedia(web).setCallback(shareListener).setPlatform(share_media).share();


    }

    public static void shareAllType(Activity activity, Bitmap shareLogo, SHARE_MEDIA platform) {
        UMImage image = new UMImage(activity, shareLogo);//资源文件
        new ShareAction(activity)
                .setPlatform(platform)//传入平台
                // .withText("hello")//分享内容
                .withMedia(image)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    public static void umengShareByList(Activity activity, Bitmap shareLogo, String title, String description, String shareUrl) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, shareLogo));  //缩略图
        web.setDescription(description);//描述

        new ShareAction(activity).withMedia(web)
                .setCallback(shareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "icon_wh_tousu", "icon_wh_tousu")
                .setShareboardclickCallback(new ShareBoardlistener() {

                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == null) {
                            //根据key来区分自定义按钮的类型，并进行对应的操作
                            if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")) {
                                ClipboardManager clipManager = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);//获取剪切板管理对象
                                ClipData clipData = ClipData.newPlainText("copy text", shareUrl);//将数据放到clip对象
                                clipManager.setPrimaryClip(clipData);//将clip对象放到剪切板

                                //判断剪贴板里是否有内容
                                if (!clipManager.hasPrimaryClip()) {
                                    return;
                                }
                                ClipData clip = clipManager.getPrimaryClip();
                                //获取 ClipDescription
                                ClipDescription description = clip.getDescription();
                                //获取 label
                                String label = description.getLabel().toString();
                                //获取 text
                                String copyText = clip.getItemAt(0).getText().toString();
                                Toast.makeText(activity, "复制成功", Toast.LENGTH_LONG).show();
                            }

                        } else {//社交平台的分享行为
                            new ShareAction(activity).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(shareListener)
                                    //.withText("多平台分享")
                                    .share();
                        }
                    }
                }).open();
        //SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
    }

    private static UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppConfig.context, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };

    public static void umengShareByList(Activity activity, Bitmap shareLogo, String title, String description, String shareUrl, UMShareListener listener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, shareLogo));  //缩略图
        web.setDescription(description);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setCallback(listener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).open();

        //, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
    }


    public static PopupWindow displayImageWindow = null;

    public static void displayImages(final Activity activity, final List<String> urls, final int position, ViewPagerGallery.GalleryOnClickListener listener) {
        initDisplayImageWindow(activity);
        ViewPagerGallery gallery = (ViewPagerGallery) displayImageWindow.getContentView().findViewById(R.id.vp_gallery);
        displayImageWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
        final View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
        if (listener == null) {
            listener = new ViewPagerGallery.GalleryOnClickListener() {
                @Override
                public void onClick(int position) {
                    if (saveImageLayout != null && saveImageLayout.getVisibility() != View.VISIBLE) {
                        displayImageWindow.dismiss();
                    }
                }
            };
        }
        gallery.setOnClickListener(listener);
        gallery.setGalleryOnLongClickListener(new ViewPagerGallery.GalleryOnLongClickListener() {
            @Override
            public void onLongClick(View view) {
                final View target = view;
                saveImageLayout.setVisibility(View.VISIBLE);
                saveImageLayout.findViewById(R.id.save_in_local).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (target instanceof ImageView) {
                            ImageView img = (ImageView) target;
                            img.setDrawingCacheEnabled(true);
                            Bitmap bitmap = img.getDrawingCache();
                            Utils.saveImageToGallery(activity, bitmap, String.valueOf(System.currentTimeMillis()));
                            img.setDrawingCacheEnabled(false);
                        } else {
                            Utils.saveImageToGallery(activity, getViewBitmap(target), String.valueOf(System.currentTimeMillis()));
                        }
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });
                saveImageLayout.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });

//                QRDecode.decodeQR(urls.get(position), new OnScannerCompletionListener() {
//                    @Override
//                    public void onScannerCompletion(final Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
//                        if (barcode != null && StringUtils.isNotEmpty(rawResult.getText())) {
//                            final View scanQRCode = saveImageLayout.findViewById(R.id.scan_qr_code);
//                            scanQRCode.setVisibility(View.VISIBLE);
//                            saveImageLayout.findViewById(R.id.scan_qr_code).setVisibility(View.VISIBLE);
//                            scanQRCode.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
                //                                    Bus.getDefault().post(new BusMessage(BusMessage.QRCODE_MESSAGE, rawResult.getText()));
//                                    saveImageLayout.setVisibility(View.GONE);
//                                    scanQRCode.setVisibility(View.GONE);
//                                }
//                            });
//                        }
//                    }
//                });
            }
        });
        displayImageWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
                if (saveImageLayout != null) {
                    saveImageLayout.setVisibility(View.GONE);
                }
            }
        });
        gallery.setImgUrls(urls, position);

    }

    public static void displayImages(final Activity activity, final ArrayList<Bitmap> bitmaps, final int position, ViewPagerGallery.GalleryOnClickListener listener) {
        initDisplayImageWindow(activity);
        ViewPagerGallery gallery = (ViewPagerGallery) displayImageWindow.getContentView().findViewById(R.id.vp_gallery);
        final View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
        if (listener == null) {
            listener = new ViewPagerGallery.GalleryOnClickListener() {
                @Override
                public void onClick(int position) {
                    if (saveImageLayout != null && saveImageLayout.getVisibility() != View.VISIBLE) {
                        displayImageWindow.dismiss();
                    }
                }
            };
        }
        gallery.setOnClickListener(listener);
        gallery.setGalleryOnLongClickListener(new ViewPagerGallery.GalleryOnLongClickListener() {
            @Override
            public void onLongClick(View view) {
                final View target = view;
                saveImageLayout.setVisibility(View.VISIBLE);
                saveImageLayout.findViewById(R.id.save_in_local).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (target instanceof ImageView) {
                            ImageView img = (ImageView) target;
                            img.setDrawingCacheEnabled(true);
                            Bitmap bitmap = convertViewToBitmap(img);
                            img.setDrawingCacheEnabled(false);
                            Utils.saveImageToGallery(activity, bitmap, String.valueOf(System.currentTimeMillis()));
                        } else {
                            Utils.saveImageToGallery(activity, convertViewToBitmap(target), String.valueOf(System.currentTimeMillis()));
                        }

                        saveImageLayout.setVisibility(View.GONE);
                    }
                });
                saveImageLayout.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });

//                QRDecode.decodeQR(bitmaps.get(position), new OnScannerCompletionListener() {
//                    @Override
//                    public void onScannerCompletion(final Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
//                        if (barcode != null && StringUtils.isNotEmpty(rawResult.getText())) {
//                            final View scanQRCode = saveImageLayout.findViewById(R.id.scan_qr_code);
//                            scanQRCode.setVisibility(View.VISIBLE);
//                            saveImageLayout.findViewById(R.id.scan_qr_code).setVisibility(View.VISIBLE);
//                            scanQRCode.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Bus.getDefault().post(new BusMessage(BusMessage.QRCODE_MESSAGE, rawResult.getText()));
//                                    saveImageLayout.setVisibility(View.GONE);
//                                    scanQRCode.setVisibility(View.GONE);
//                                }
//                            });
//                        }
//                    }
//                });
            }
        });
        gallery.setImgBitmaps(bitmaps, position);
        displayImageWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
    }

    public static void displayImagesResources(final Activity activity, ArrayList<Integer> resources, int position, ViewPagerGallery.GalleryOnClickListener listener) {
        initDisplayImageWindow(activity);
        ViewPagerGallery gallery = (ViewPagerGallery) displayImageWindow.getContentView().findViewById(R.id.vp_gallery);
        final View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
        if (listener == null) {
            listener = new ViewPagerGallery.GalleryOnClickListener() {
                @Override
                public void onClick(int position) {
                    if (saveImageLayout != null && saveImageLayout.getVisibility() != View.VISIBLE) {
                        displayImageWindow.dismiss();
                    }
                }
            };
        }
        gallery.setOnClickListener(listener);
        gallery.setGalleryOnLongClickListener(new ViewPagerGallery.GalleryOnLongClickListener() {
            @Override
            public void onLongClick(View view) {
                final View target = view;
                saveImageLayout.setVisibility(View.VISIBLE);
                saveImageLayout.findViewById(R.id.save_in_local).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (target instanceof ImageView) {
                            ImageView img = (ImageView) target;
                            img.setDrawingCacheEnabled(true);
//                            Bitmap bitmap = img.getDrawingCache();
                            Bitmap bitmap = convertViewToBitmap(img);
                            Utils.saveImageToGallery(activity, bitmap, String.valueOf(System.currentTimeMillis()));
                            img.setDrawingCacheEnabled(false);
                        } else {
                            Utils.saveImageToGallery(activity, convertViewToBitmap(target), String.valueOf(System.currentTimeMillis()));
                        }
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });
                saveImageLayout.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
        gallery.setImgResources(resources, position);
        displayImageWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
    }

    public static void initDisplayImageWindow(final Activity activity) {
        if (displayImageWindow == null) {
            displayImageWindow = new PopupWindow(activity);
            View view = View.inflate(activity, R.layout.layout_gallery, null);
            displayImageWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            displayImageWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            displayImageWindow.setWidth(DensityUtils.getScreenW(activity));
            displayImageWindow.setContentView(view);
            fitPopupWindowOverStatusBar(activity, true);
        }
    }

    public static void fitPopupWindowOverStatusBar(Activity activity, boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                if (Utils.displayImageWindow == null) {
                    Utils.initDisplayImageWindow(activity);
                }
                mLayoutInScreen.set(Utils.displayImageWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 某些机型直接获取会为null,在这里处理一下防止国内某些机型返回null
     */
    public static Bitmap getViewBitmap(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public static void versionUpdate(final Context context) {
        // TODO MOLULU 2019.05.09 暂时屏蔽更新
//        new ApiImp().getVersion(null, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                List<VersionEntity> list = JSON.parseArray(data, VersionEntity.class);
//                if (list.size() > 1) {
//                    if (versionComparison(list.get(1).version_name, getVersionName(context)) == 1) {//强制更新
//                        download(context, list.get(1), true);
//                    } else if (versionComparison(list.get(0).version_name, getVersionName(context)) == 1) {//非强制更新
//                        String versionSkd = PreferencesUtil.get(context, PreferencesUtil.KEY_SDK_UPDATE, "");
//                        if (!data.equals(versionSkd)) {
//                            PreferencesUtil.put(context, PreferencesUtil.KEY_SDK_UPDATE, data);
//                            download(context, list.get(0), false);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//            }
//        });
    }

//    /**
//     * 本地本版号
//     *
//     * @param context
//     * @return
//     */
//    public String getVersionName(Context context) {
//        String version;
//        PackageManager pm = context.getPackageManager();//context为当前Activity上下文
//        PackageInfo pi = null;
//        try {
//            pi = pm.getPackageInfo(context.getPackageName(), 0);
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        version = pi.versionName;
//        return version;
//    }


    /**
     * 下载提示
     */
    public static void download(final Context context, final VersionEntity entity, final boolean isCancelable) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("下载提示");
        builder.setMessage(StringUtil.isEmpty(entity.getContent()) ? "有新版本更新" : entity.getContent());
        builder.setCancelable(false);
        if (!isCancelable) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (isCancelable) {
                        if (context instanceof Activity) {
                            Activity activity = (Activity) context;
                            activity.finish();
                        }
                    }
                }
            });
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final ProgressDialog pd = new ProgressDialog(context);
                pd.setTitle("正在下载");
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.getWindow().setGravity(Gravity.CENTER);
                pd.setMax(100);
                pd.setCancelable(false);
                pd.show();
                String douwloadUtl = FileUtils.getUpdateApk(context);
                String apkName = StringUtils.getFileabsName(entity.app_url);
//
                OkHttpUtils.get().tag(this).url(entity.app_url).build().execute(
                        new FileCallBack(douwloadUtl, apkName) {
                            @Override
                            public void inProgress(float progress, long total, int id) {
                                Log.d("-----------------", (int) ((progress % total) * 100) + "");
                                pd.setProgress((int) ((progress % total) * 100));
                            }

                            @Override
                            public void onError(Call call, Exception e, int i) {
                                pd.setMessage("下载失败");
                                pd.setButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(File file, int i) {
                                pd.dismiss();
                                openFile(file, context);
                            }
                        });
            }
        });
        builder.show();
    }


    /**
     * 本版号对比
     *
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    public static int versionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            int[] number2 = getValue(version2, index2);

            if (number1[0] < number2[0]) {
                return -1;
            } else if (number1[0] > number2[0]) {
                return 1;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if (index1 < version1.length())
            return 1;
        else
            return -1;
    }

    /**
     * @param version
     * @param index   the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    private static void openFile(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.yuejian.meet.fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void pickPhotos(final Activity activity, final int requestCode, final boolean isCupture, final int width, final int height) {
        final String[] items = {"手机拍照", "从相册里选择"};
//        final OptionPicker picker = new OptionPicker(activity, items) {
//            @Override
//            protected void onCancel() {
//                super.onCancel();
//            }
//        };
//        picker.setTitleText(R.string.title_choose);
//        picker.setTextSize(14);
//        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//            @Override
//            public void onOptionPicked(int position, String option) {
//                if ("手机拍照".equals(option)) {
//                    selectAlbums(activity, false, requestCode, isCupture, width, height);
//                } else if ("从相册里选择".equals(option)) {
//                    selectAlbums(activity, true, requestCode, isCupture, width, height);
//                }
//            }
//        });
//        picker.show();
        if (activity instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) activity;
            showSelector(a, items, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String option = items[position];
                    if ("手机拍照".equals(option)) {
                        selectAlbums(activity, false, requestCode, isCupture, width, height);
                    } else if ("从相册里选择".equals(option)) {
                        selectAlbums(activity, true, requestCode, isCupture, width, height);
                    }
                }
            }, null);
        }
    }

    public static boolean isSubmit = false;

    public static void pickPhotos(final Activity activity,
                                  final int requestCode,
                                  final boolean isCupture,
                                  final int width,
                                  final int height,
                                  final DialogInterface.OnDismissListener dismissListener) {
        final String[] items = {"手机拍照", "从相册里选择照片"};
        isSubmit = true;
//        final OptionPicker picker = new OptionPicker(activity, items) {
//            @Override
//            public void onSubmit() {
//                super.onSubmit();
//                isSubmit = true;
//            }
//
//            @Override
//            protected void onCancel() {
//                super.onCancel();
//                isSubmit = true;
//            }
//        };
//        picker.setTitleText(R.string.title_choose);
//        picker.setTextSize(14);
//        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//            @Override
//            public void onOptionPicked(int position, String option) {
//                if ("手机拍照".equals(option)) {
//                    selectAlbums(activity, false, requestCode, isCupture, width, height);
//                } else if ("从相册里选择".equals(option)) {
//                    selectAlbums(activity, true, requestCode, isCupture, width, height);
//                }
//            }
//        });
//        picker.setOnDismissListener(dismissListener);
//        picker.show();

        isSubmit = true;
        if (activity instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) activity;
            showSelector(a, items, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String option = items[position];
                    if ("手机拍照".equals(option)) {
                        selectAlbums(activity, false, requestCode, isCupture, width, height);
                    } else if ("从相册里选择照片".equals(option)) {
                        selectAlbums(activity, true, requestCode, isCupture, width, height);
                    }
                    isSubmit = true;
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dismissListener != null) {
                        isSubmit = false;
                        dismissListener.onDismiss(null);
                    }
                    isSubmit = true;
                }
            });
        }
    }

    public static void pickInfor(final Activity activity,
                                 final int requestCode,
                                 final boolean isCupture,
                                 final int width,
                                 final int height,
                                 final DialogInterface.OnDismissListener dismissListener) {
        final String[] items = {"手机拍照", "从相册里选择相片", "从相册里选择视频"};
        isSubmit = true;
//        final OptionPicker picker = new OptionPicker(activity, items) {
//            @Override
//            public void onSubmit() {
//                super.onSubmit();
//                isSubmit = true;
//            }
//
//            @Override
//            protected void onCancel() {
//                super.onCancel();
//                isSubmit = true;
//            }
//        };
//        picker.setTitleText(R.string.title_choose);
//        picker.setTextSize(14);
//        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//            @Override
//            public void onOptionPicked(int position, String option) {
//                if ("手机拍照".equals(option)) {
//                    selectAlbums(activity, false, requestCode, isCupture, width, height);
//                } else if ("从相册里选择".equals(option)) {
//                    selectAlbums(activity, true, requestCode, isCupture, width, height);
//                }
//            }
//        });
//        picker.setOnDismissListener(dismissListener);
//        picker.show();

        isSubmit = true;
        if (activity instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) activity;
            showSelector(a, items, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String option = items[position];
                    if ("手机拍照".equals(option)) {
                        selectAlbums(activity, false, requestCode, isCupture, width, height);
                    } else if ("从相册里选择相片".equals(option)) {
                        selectAlbums(activity, true, requestCode, isCupture, width, height);
                    } else if ("从相册里选择视频".equals(option)) {
                        //todo
                        PictureSelector.create(activity).openGallery(PictureMimeType.ofVideo()).isCamera(false).maxSelectNum(1).forResult(requestCode);
                    }
                    isSubmit = true;
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dismissListener != null) {
                        isSubmit = false;
                        dismissListener.onDismiss(null);
                    }
                    isSubmit = true;
                }
            });
        }
    }


    private static void selectAlbums(Activity activity, Boolean isLocal, int requestCode, boolean isCupture, int width, int height) {
        if (isLocal) {
            if (isCupture) {
                PickImageActivity.start(activity, requestCode, PickImageActivity.FROM_LOCAL, ImgUtils.imgTempFile(), false, 1, false, true, width, height);
            } else {
                PickImageActivity.start(activity, requestCode, PickImageActivity.FROM_LOCAL, ImgUtils.imgTempFile());
            }
        } else {
            if (isCupture) {
                PickImageActivity.start(activity, requestCode, PickImageActivity.FROM_CAMERA, ImgUtils.imgTempFile(), false, 1, false, true, width, height);
            } else {
                PickImageActivity.start(activity, requestCode, PickImageActivity.FROM_CAMERA, ImgUtils.imgTempFile());
            }
        }
    }


    private static PopupWindow sendGiftWindow = null;

    public static void showSendGiftWindow(Activity activity) {
        if (sendGiftWindow == null) {
            sendGiftWindow = new PopupWindow(activity);
            sendGiftWindow.setWidth(DensityUtils.getScreenW(activity));
            sendGiftWindow.setHeight(DensityUtils.getScreenH(activity));

            View contentView = View.inflate(activity, R.layout.gifts_layout, null);
            sendGiftWindow.setContentView(contentView);
        }
    }

    public static void copyText(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
        Toast.makeText(context, R.string.toast_text_1, Toast.LENGTH_SHORT).show();
    }

    private static PopupWindow noTitleDialog = null;

    public static void showNoTitleDialog(final Activity activity, String message, String postiveText, String cancelText, final View.OnClickListener positiveListener, final View.OnClickListener cancelListener) {
        if (noTitleDialog == null) {
            noTitleDialog = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            View contentView = View.inflate(activity, R.layout.no_titile_dialog, null);
            noTitleDialog.setContentView(contentView);
            contentView.findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noTitleDialog.dismiss();
                }
            });

            TextView messageText = (TextView) contentView.findViewById(R.id.no_title_message);
            messageText.setText(message);
            Button queding = (Button) contentView.findViewById(R.id.queding);
            if (positiveListener == null) {
                queding.setVisibility(View.GONE);
            } else {
                queding.setText(postiveText);
                queding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveListener.onClick(v);
                        noTitleDialog.dismiss();
                    }
                });
            }
            Button quxiao = (Button) contentView.findViewById(R.id.quxiao);
            if (cancelListener == null) {
                quxiao.setVisibility(View.GONE);
            } else {
                quxiao.setText(cancelText);
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelListener.onClick(v);
                        noTitleDialog.dismiss();
                    }
                });
            }
            backgroundAlpha(activity, 0.7f);
            noTitleDialog.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            noTitleDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(activity, 1f);
                    noTitleDialog = null;
                }
            });
        }
    }

    public static void displayImagesClan(final Activity paramActivity, final List<String> paramList, int paramInt, ViewPagerGallery.GalleryOnClickListener paramGalleryOnClickListener, final ViewPagerGallery.GalleryDelOnClickListener listenerDel) {
        initDisplayImageWindow(paramActivity);
        final ViewPagerGallery localViewPagerGallery = (ViewPagerGallery) displayImageWindow.getContentView().findViewById(R.id.vp_gallery);
        displayImageWindow.showAtLocation(paramActivity.getWindow().getDecorView(), 51, 0, 0);
        final View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
        View localView2 = displayImageWindow.getContentView().findViewById(R.id.delete_photo);
        localView2.setVisibility(View.GONE);
        if (ClanPhotoActivity.type == 1) {
            localView2.setVisibility(View.VISIBLE);
        }
        Object localObject = paramGalleryOnClickListener;
        if (paramGalleryOnClickListener == null) {
            localObject = new ViewPagerGallery.GalleryOnClickListener() {
                public void onClick(int paramAnonymousInt) {
                    if ((saveImageLayout != null) && (saveImageLayout.getVisibility() != View.VISIBLE)) {
                        Utils.displayImageWindow.dismiss();
                    }
                }
            };
        }
        localViewPagerGallery.setOnClickListener((ViewPagerGallery.GalleryOnClickListener) localObject);
        localViewPagerGallery.setGalleryOnLongClickListener(new ViewPagerGallery.GalleryOnLongClickListener() {
            public void onLongClick(final View paramAnonymousView) {
                saveImageLayout.setVisibility(View.VISIBLE);
                saveImageLayout.findViewById(R.id.save_in_local).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymous2View) {
                        if ((paramAnonymousView instanceof ImageView)) {
                            paramAnonymous2View = (ImageView) paramAnonymousView;
                            paramAnonymous2View.setDrawingCacheEnabled(true);
                            Bitmap localBitmap = paramAnonymous2View.getDrawingCache();
                            Utils.saveImageToGallery(paramActivity, localBitmap, String.valueOf(System.currentTimeMillis()));
                            paramAnonymous2View.setDrawingCacheEnabled(false);
                        }
                        saveImageLayout.setVisibility(View.GONE);
//                            return;
//                            Utils.saveImageToGallery(paramActivity, Utils.getViewBitmap(paramAnonymousView), String.valueOf(System.currentTimeMillis()));
                    }
                });
                saveImageLayout.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymous2View) {
                        saveImageLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
        displayImageWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                View saveImageLayout = displayImageWindow.getContentView().findViewById(R.id.save_img_layout);
                if (saveImageLayout != null) {
                    saveImageLayout.setVisibility(View.GONE);
                }
            }
        });
        localViewPagerGallery.setImgUrls(paramList, paramInt);
        localView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                listenerDel.onBack(localViewPagerGallery.getCurrentItem(), new ViewPagerGallery.GalleryOnClickListener() {
                    public void onClick(int paramAnonymous2Int) {
                        if (paramList.size() <= 1) {
                            if ((saveImageLayout != null) && (saveImageLayout.getVisibility() != View.VISIBLE)) {
                                Utils.displayImageWindow.dismiss();
                            }
                            return;
                        }
                        paramList.remove(paramAnonymous2Int);
                        if (paramList.size() - 1 == paramAnonymous2Int) {
                            localViewPagerGallery.setImgUrls(paramList, paramAnonymous2Int - 1);
                            return;
                        }
                        localViewPagerGallery.setImgUrls(paramList, paramAnonymous2Int);
                    }
                });
            }
        });
    }

    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static boolean isAnyWindowsIsShowing() {
        boolean isDisplayImageWindow = displayImageWindow != null && displayImageWindow.isShowing();
        boolean isNoTitleDialogShow = noTitleDialog != null && noTitleDialog.isShowing();
        return isDisplayImageWindow || isNoTitleDialogShow;
    }

    public static void dismissAnyWindows() {
        if (displayImageWindow != null) {
            displayImageWindow.dismiss();
        }
        if (noTitleDialog != null) {
            noTitleDialog.dismiss();
        }
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String imageFileName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "meet");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = imageFileName;
        if (!imageFileName.contains(".jpg")) {
            fileName = imageFileName + ".jpg";
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            Toast.makeText(context, "已保存图片:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 是否是华为
     */
    public static boolean isHUAWEI() {
        return android.os.Build.MANUFACTURER.equals("HUAWEI");
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            String key = str.substring(0, str.indexOf("="));
            if (key.equals(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param pkg
     * @param cls
     * @param context
     * @return
     */
    public static boolean isClsRunning(String pkg, String cls, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        if (task != null) {
            return TextUtils.equals(task.baseActivity.getPackageName(), pkg) && TextUtils.equals(task.baseActivity.getClassName(), cls);
        }
        return false;
    }

    /**
     * 设置格力home
     *
     * @param activity
     */
    public static void settingPutInt(Activity activity) {
        try {
            if (AppConfig.isGeLiGuide) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    putInt(activity.getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 0);
                }
                Settings.Secure.putInt(activity.getContentResolver(), "user_setup_complete", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置格力home
     *
     * @param activity
     */
    public static void settingOpenPutInt(Activity activity) {
        try {
            if (AppConfig.isGeLiGuide) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    putInt(activity.getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 1);
                }
                Settings.Secure.putInt(activity.getContentResolver(), "user_setup_complete", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSelector(FragmentActivity activity, String[] items, AdapterView.OnItemClickListener listener, View.OnClickListener negativeListener) {
        new CircleDialog.Builder(activity)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setItems(items, listener)
                .setNegative("取消", negativeListener)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                    }
                })
                .show();
    }

    /**
     * 从给定路径载入图片
     */
    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }


    /**
     * 从给定的路径载入图片，并指定是否自己主动旋转方向
     */
    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap bm = loadBitmap(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    public static File saveBitmap(Bitmap bitmap, String imageFileName) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "meet");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = imageFileName;
        if (!fileName.contains(".jpg")) {
            fileName = imageFileName + ".jpg";
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static long getTodayTime() {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(Calendar.HOUR, 0);
        localCalendar.set(Calendar.MINUTE, 0);
        localCalendar.set(Calendar.SECOND, 0);
        return localCalendar.getTimeInMillis() / 1000L;
    }

    public static void setAgeAndSexView(Context context, TextView textView, String sex, String age) {
        boolean isMale = "1".equals(sex);
        textView.setSelected(sex.equals("1"));
//        textView.setBackground(isMale ? context.getResources().getDrawable(R.drawable.shape_man) : context.getResources().getDrawable(R.drawable.shape_woman));
//        int sexIcon = isMale ? R.drawable.selector_man : R.drawable.selector_woman;
//        Drawable drawable = context.getResources().getDrawable(sexIcon);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setText(String.valueOf(" " + age));
    }

    public static String s2tOrT2s(String s) {
        String data = "";
        if (AppConfig.language.equals("1")) {
            try {
                JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
                data = jChineseConvertor.t2s(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            data = jChineseConvertor.s2t(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String t2s(String s) {
        String data = "";
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            data = jChineseConvertor.t2s(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 设置语言
     */
    public static void setlangage(Context context) {
        if (StringUtil.isEmpty(PreferencesUtil.get(context, PreferencesUtil.Langguage, ""))) {
//            String locale = Locale.getDefault().toString();
//            if (locale.equals("zh_TW")){
//                PreferencesUtil.put(context,PreferencesUtil.Langguage,"2");
//                Resources resources = context.getResources();
//                DisplayMetrics dm = resources.getDisplayMetrics();
//                Configuration config = resources.getConfiguration();
//                config.locale = Locale.TRADITIONAL_CHINESE;
//                resources.updateConfiguration(config, dm);
//                AppConfig.language="2";
//            }
        } else {
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            String name = PreferencesUtil.get(context, PreferencesUtil.Langguage, "1");
            AppConfig.language = name;
            if (name.contains("1")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                config.locale = Locale.TRADITIONAL_CHINESE;
            }
            resources.updateConfiguration(config, dm);
        }
    }

    /**
     * 将整数转换成汉字数字
     *
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
//                if ('0' == val[i - 1]) {
//                    continue;
//                } else {
                sb.append(numArray[n]);
//                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }
}
