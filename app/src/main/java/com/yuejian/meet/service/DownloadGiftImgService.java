package com.yuejian.meet.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.utils.FileUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;

/**
 * Created by idea on 2016/10/11.
 */

public class DownloadGiftImgService extends Service {

    private String loadingUrl;
    List<String> downloadList = new ArrayList<>();
    public static final int ADDURL = 0x111;
    private static final int REFRESH = 0x112;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ADDURL:
                    if (downloadList.size() > 0) {
                        downLoadFile(downloadList.get(0));
                    }
                    break;
                case REFRESH:
                    if (downloadList.size() > 0) {
                        downLoadFile(downloadList.get(0));
                    }
                    break;
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppConfig.isGiftDownload=true;
        PreferencesIm.write(this,PreferencesIm.giftDownload,true);
        if (intent != null) {
            ArrayList<String> giftImageList = intent.getStringArrayListExtra("giftImageList");
            if (giftImageList != null && giftImageList.size() > 0) {
                downloadList.addAll(giftImageList);
                Message message = Message.obtain();
                message.what = ADDURL;
                handler.handleMessage(message);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void downLoadFile(String url) {
        if (TextUtils.isEmpty(url) || url.equals(loadingUrl)) {
            return;
        }
        loadingUrl = url;
        final String fileName = StringUtils.getFileName(url);
        String tempName = fileName + ".tmp";
        final String chatGiftPath = FileUtils.getChatGiftPath(DownloadGiftImgService.this);
        File tempFile = new File(chatGiftPath, tempName);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        File imgfile = new File(chatGiftPath, fileName);
        if (imgfile.exists()) {
            downloadList.remove(0);
            Message message = Message.obtain();
            message.what = REFRESH;
            handler.handleMessage(message);
        }
        OkHttpUtils.get().tag(this).url(url).build().execute(
                new FileCallBack(chatGiftPath, tempName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        loadingUrl = "";
                        if (downloadList.size()>0) {
                        downloadList.remove(0);
                        }
                        Message message = Message.obtain();
                        message.what = REFRESH;
                        handler.handleMessage(message);
                    }

                    @Override
                    public void onResponse(File file, int i) {
                        file.renameTo(new File(chatGiftPath, fileName));
                        loadingUrl = "";
                        if (downloadList.size()>0) {
                            downloadList.remove(0);
                        }
                        Message message = Message.obtain();
                        message.what = REFRESH;
                        handler.handleMessage(message);
                    }
                });
    }
}
