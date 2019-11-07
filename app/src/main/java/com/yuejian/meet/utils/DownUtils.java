package com.yuejian.meet.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;


/**
 * 文件操作工具类
 * Created by fs-ljh on 2017/5/26.
 */

public class DownUtils {
    private static long cachSize=0;
    private static RandomAccessFile rwd;
    /**
     * Download Signature
     *
     * @param downloadUrl
     */
    private static final int UPDATE = 0X2;
    public static File downloaddir, downloadfile, downloadfiletemp;
    public static String fileName = "";//文件名
    public static String fileNametemp = "";//临时文件
    public static final String fileRootPath = Environment.getExternalStorageDirectory() + File.separator;
    public static final String fileDownloadPath = "yuejian/";
    public static int fileSzie;//文件大小
    public static void DownloadFile(String downloadUrl, final Context context, final ProgressBar progressBar,
                                    final TextView tvNewest, final ImageView imgNew, final AutoRelativeLayout layCheckVersion, final TextView title, final TextView positiveButton) {
        /*文件名*/
        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        /*缓存文件*/
        fileNametemp = "download.tmp";
        /*下载目录*/
        downloaddir = new File(fileRootPath + fileDownloadPath);
        downloadfile = new File(fileRootPath + fileDownloadPath + fileName);
        downloadfiletemp = new File(fileRootPath + fileDownloadPath + fileNametemp);

        if (!downloaddir.exists()) {
            downloaddir.mkdirs();
        }
             /*是否有缓存*/
        if (downloadfiletemp.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(downloadfiletemp);
//                cachSize = fis.available();
                cachSize = getFileLength(context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
//            DadanPreference.getInstance(context).setInt("bytes",0);
            //保存下载的位置到SharedPreferences,下次下载的时候拿值写入设置字符编码
            saveFileLength(context , (long) 0);
            cachSize = getFileLength(context);
            progressBar.setProgress(0);
            positiveButton.setText("下载");
        }
        /*如何文件存在 这安装文件*/
        if (downloadfile.exists()) {
            installApp(context, fileRootPath + fileDownloadPath + fileName);
        }
        /*否则下载文件*/
        else {
            new AsyncTask<String, Integer, String>() {
                @Override
                protected void onPreExecute() {
//                    mBuilder.setTicker("下载富商美食").setProgress(100, 0, false);
//                    mNotifyManager.notify(notifiID, mBuilder.build());
                    super.onPreExecute();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
//                    Log.e(TAG, "---下载缓存" + values[0] + "---");
                    super.onProgressUpdate(values);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(values[0]);
                    //文件大小形式
//                            String i=bytes2kb(progressBar.getProgress())+"/"+bytes2kb(fileSzie);
                    //百分比形式
//                            double b= (double)progressBar.getProgress()*(double)100/(double)progressBar.getMax();
                    title.setText("下载进度为："+values[0]+"%");
                }

                @Override
                protected String doInBackground(String... params) {
                    fileName = params[0].substring(params[0].lastIndexOf("/") + 1);
                    getLength(context,params[0]);
                    try {
                        //获取文件名
                        URL myURL = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(3000);
                        conn.setRequestProperty("Range" , "bytes=" + cachSize + "-");
//                        conn.setRequestProperty("Range", "bytes=" + cachSize + "-" +fileSzie);//设置下载范围
                        InputStream is = conn.getInputStream();
                        if (is == null) throw new RuntimeException("stream is null");
                        /*下载目录*/
                        if (!downloaddir.exists()) {
                            downloaddir.mkdirs();
                        }
                        //把数据存入 路径+文件名
//                        FileOutputStream fos = new FileOutputStream(downloadfiletemp);
                        rwd = new RandomAccessFile(downloadfiletemp, "rwd");
                        //从文件的某一位置开始写入
                        rwd.seek(cachSize);
                        byte buf[] = new byte[1024*4];
                        do {
                            //循环读取
                            int numread = is.read(buf);
                            if (numread == -1) {
                                break;
                            }
                            rwd.write(buf, 0, numread);
                            cachSize += numread;
                            int progress = (int) (cachSize * 100.0 / fileSzie);
                            this.publishProgress(progress);
                            //保存下载的位置到SharedPreferences,下次下载的时候拿值写入设置字符编码
                            saveFileLength(context , cachSize);
                        } while (true);
                        try {
                            is.close();
                        } catch (Exception ex) {
                            Log.e("tag", "error: " + ex.getMessage());
                        }
                        return "下载成功";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    /*下载成功后*/
                    if (!CommonUtil.isNull(s)) {
                        if (downloadfiletemp.exists()) {
                            downloadfiletemp.renameTo(downloadfile);
                        }
                        if (positiveButton!=null) {
                            positiveButton.setEnabled(true);
                            positiveButton.setText("点击安装");
                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (tvNewest!=null) {
                            tvNewest.setVisibility(View.VISIBLE);
                        }
                        installApp(context, fileRootPath + fileDownloadPath + fileName);
                        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                        super.onPostExecute(s);
                    }else {
                        if (positiveButton == null) {
                            progressBar.setVisibility(View.GONE);
                        }else {
                            positiveButton.setEnabled(true);
                        }
                        if (imgNew!=null) {
                            imgNew.setVisibility(View.VISIBLE);
                        }
                        if (layCheckVersion!=null) {
                            layCheckVersion.setClickable(true);
                        }
                        Toast.makeText(context,"下载失败，检查网络后请重新下载",Toast.LENGTH_LONG).show();
                    }
                }
            }.execute(downloadUrl);

        }
    }

    /**
     * 保存文件长度
     * @param context
     * @param fileLength
     */
    private static void saveFileLength(Context context ,Long fileLength ){
        SharedPreferences sp = context.getSharedPreferences("My_SP" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("File_startOffset" , fileLength);
        editor.commit();
    }
    /**
     * 获取文件长度
     * @param context
     * @return
     */
    private static Long getFileLength(Context context){
        SharedPreferences sp = context.getSharedPreferences("My_SP" , Context.MODE_PRIVATE);
        return sp.getLong("File_startOffset" , 0);
    }

    private static void getLength(Context context,String fileName) {
        URL myURL = null;
        HttpURLConnection conn = null;
        try {
            myURL = new URL(fileName);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            if (conn.getResponseCode() == 200) {//网络连接成功
                fileSzie = conn.getContentLength();//根据响应获取文件大小
            }
            if (fileSzie <= 0) {
                throw new RuntimeException("无法获知文件大小 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }

    }
    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }
    /**
     * install Apk
     * @param filePath
     */
    public static void installApp(Context pContext, String filePath) {
        File pFile= new File(filePath);
        if (null == pFile)
            return;
        if (!pFile.exists())
            return;
        Intent _Intent = new Intent();
        _Intent.setAction(Intent.ACTION_VIEW);
        Uri _uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _Intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _uri = FileProvider.getUriForFile(pContext, pContext.getPackageName() + ".fileProvider", pFile);
        }else {
            _uri = Uri.fromFile(pFile);
            _Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        _Intent.setDataAndType(_uri, "application/vnd.android.package-archive");
        pContext.startActivity(_Intent);
    }

}