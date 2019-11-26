package com.yuejian.meet.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoRelativeLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class DownLoadUtils {
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
    public static void DownloadUrlIMG(Context context, Bitmap bmp){
        try {
            downloaddir = new File(fileRootPath + fileDownloadPath);
            if (!downloaddir.exists()) {
                downloaddir.mkdirs();
            }
            fileName="wx_code.jpg";
            downloadfile = new File(fileRootPath + fileDownloadPath + fileName);
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            try {
                // 如果文件存在则删除
                if (downloadfile.exists()) {
                    downloadfile.delete();
                }
                // 在文件系统中根据路径创建一个新的空文件
                downloadfile.createNewFile();
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(downloadfile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    downloadfile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{downloadfile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            context.sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = downloadfile.getParent();
            File file1 = new File(relationDir);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
        }
    }
    public static void DownloadCode(Context context,String code){
        try {
            byte[] bitmapArray = Base64.decode(code.split(",")[1], Base64.DEFAULT);
            downloaddir = new File(fileRootPath + fileDownloadPath);
            if (!downloaddir.exists()) {
                downloaddir.mkdirs();
            }
            fileName="code.jpg";
            downloadfile = new File(fileRootPath + fileDownloadPath + fileName);
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            // 创建BufferedOutputStream对象
            BufferedOutputStream bufferedOutputStream = null;
            try {
                // 如果文件存在则删除
                if (downloadfile.exists()) {
                    downloadfile.delete();
                }
                // 在文件系统中根据路径创建一个新的空文件
                downloadfile.createNewFile();
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(downloadfile);
                // 获取BufferedOutputStream对象
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                // 往文件所在的缓冲输出流中写byte数据
                bufferedOutputStream.write(bitmapArray);
                // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                bufferedOutputStream.flush();
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    downloadfile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{downloadfile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            context.sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = downloadfile.getParent();
            File file1 = new File(relationDir);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
        }
    }

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
//            DadanPreference.getInstance(context).setInt("bytes",0);
            progressBar.setProgress(0);
            positiveButton.setText("下载");
        }
        /*如何文件存在 这安装文件*/
        if (downloadfile.exists()) {
            installApp(context, fileRootPath + fileDownloadPath + fileName);
        }
        /*否则下载文件*/
        else {
            //downloadfile
            new AsyncTask<String, Integer, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    switch (values[0]){
                        case UPDATE:
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(values[1]);
                            //文件大小形式
//                            String i=bytes2kb(progressBar.getProgress())+"/"+bytes2kb(fileSzie);
                            //百分比形式
//                            double b= (double)progressBar.getProgress()*(double)100/(double)progressBar.getMax();
                            title.setText("下载进度为："+values[1]+"%");
                            break;
                    }
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        RandomAccessFile accessFile = new RandomAccessFile(downloadfiletemp, "rwd");
                        int bytes = (int) accessFile.getFilePointer();
//                        int bytes = DadanPreference.getInstance(context).getInt("bytes");
                        if (bytes <= 0) {                                                    //第一次下载
                            URL url = new URL(params[0]);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoInput(true);
                            urlConnection.setRequestProperty("Accept-Encoding", "identity"); //设置请求参数
                            urlConnection.setConnectTimeout(1000*10);
                            Log.e("code", urlConnection.getResponseCode() + "");
                            if (urlConnection.getResponseCode() == 200) {
                                long total = urlConnection.getContentLength();                //获取网络资源大小
                                accessFile.setLength(total);
                                long lastIndex = (int) downloadfiletemp.length();
                                InputStream inputStream = urlConnection.getInputStream();
                                long i, count = 0;
                                byte buffer[] = new byte[1024];
                                while ((i = inputStream.read(buffer)) != -1) {
                                    count += i;
                                    int progress = (int) (count * 100.0 / total);
                                    accessFile.write(buffer, 0, (int) i);
                                    publishProgress(UPDATE,progress);
//                                    DadanPreference.getInstance(context).setInt("bytes",(int) accessFile.getFilePointer());
                                }
                                if (count == lastIndex){           //删除计数文件
                                    File file3= new File("/data/data/" + getPackageName() + "/shared_prefs","index.xml");
                                    if (file3.exists())
                                        file3.delete();
                                }
                            }
                        } else {                                                             //断点下载
                            // Log.e("index内容", index_str);
                            // int index = Integer.parseInt(index_str);
                            long lastIndex = (int) downloadfiletemp.length();                             //获取原始资源长度
                            accessFile.seek(bytes);
                            URL url = new URL(params[0]);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoInput(true);
                            urlConnection.setRequestProperty("Accept-Encoding", "identity"); //设置请求参数
                            urlConnection.setRequestProperty("Range", "bytes=" + bytes + "-" + lastIndex);  //设置分段下载的头信息。  Range:做分段数据请求用的。
                            urlConnection.setConnectTimeout(1000*10);
                            Log.e("code---", urlConnection.getResponseCode() + "");
                            if (urlConnection.getResponseCode() == 206) {                         //200：请求全部资源成功， 206代表部分资源请求成功
                                InputStream inputStream = urlConnection.getInputStream();
                                long i, count = bytes;
                                byte buffer[] = new byte[1024];
                                while ((i = inputStream.read(buffer)) != -1) {
                                    accessFile.write(buffer, 0, (int) i);
                                    count += i;
                                    int progress = (int) (count * 100.0 / lastIndex);
                                    publishProgress(UPDATE,progress);
//                                    DadanPreference.getInstance(context).setInt("bytes",(int) accessFile.getFilePointer());
                                }
                                if (count == lastIndex){           //删除计数文件
                                    File file3= new File("/data/data/" + getPackageName() + "/shared_prefs","index.xml");
                                    if (file3.exists())
                                        file3.delete();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                    return "下载成功";
                }

                @Override
                protected void onPostExecute(String s) {
                    /*下载成功后*/
                    if (!CommonUtil.isNull(s)) {
                        if (downloadfiletemp.exists()) {
                            downloadfiletemp.renameTo(downloadfile);
                        }
                        if (positiveButton!=null) {
                            title.setText("下载完成");
                            progressBar.setProgress(100);
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