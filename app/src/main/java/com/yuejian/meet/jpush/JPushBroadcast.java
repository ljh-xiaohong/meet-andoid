package com.yuejian.meet.jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.activities.mine.BillRecordActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;


/**
 * 极光推送广播
 * Created by zh03 on 2017/6/26.
 */

public class JPushBroadcast extends BroadcastReceiver {
    long time = 0;
    long newtworkTime=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (extras != null) {
                JSONObject jsonObject = JSON.parseObject(extras);
                if (jsonObject != null) {
                    try {
                        int type = jsonObject.getInteger("type");
                        switch (type) {
                            case 2:
                                if ((System.currentTimeMillis() - time) > 3000) {
                                    time = System.currentTimeMillis();
                                    if (AppConfig.userEntity != null) {
                                        Bus.getDefault().post("ACTION_UNREAD");
                                    }
                                }
                                String title = jsonObject.getString("title");
                                String content = jsonObject.getString("content");
                                String clazz = jsonObject.getString("class");
                                String id = jsonObject.getString("id");
                                if (StringUtils.isEmpty(id)) {
                                    return;
                                }
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setContentTitle(title)
                                        .setContentText(content)
                                        .setTicker("百家姓给您发来一条消息")
                                        .setSmallIcon(R.mipmap.app_logo)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                                        .setWhen(System.currentTimeMillis())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                                        .setOngoing(false);

                                Intent i;
                                if ("action".equals(clazz)) {
//                                    i = new Intent(context, ActionInfoActivity.class);
//                                    i.putExtra("action_id", id);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
//                                    builder.setContentIntent(pendingIntent);
//                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                                    if (manager != null && AppConfig.userEntity != null) {
//                                        manager.notify((int) System.currentTimeMillis() / 1000, builder.build());
//                                    }
                                } else {
                                    getArticle(id, context, title, content);
                                }
                                break;
                            case 3:
                                Bus.getDefault().post("store_has_un_read_message");
                                String url = jsonObject.getString("url");
                                Log.d("storePush", "url: " + url);
                                title = jsonObject.getString("title");
                                content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                                Log.d("storePush", "title : " + title);
                                Log.d("storePush", "content: " + content);
                                if (StringUtils.isEmpty(title)) {
                                    title = "百家姓商域";
                                }
                                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
                                        .setContentTitle(title)
                                        .setContentText(content)
                                        .setTicker("百家姓商域来了一条消息")
                                        .setSmallIcon(R.mipmap.app_logo)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                                        .setWhen(System.currentTimeMillis())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                                        .setOngoing(false);

                                if (StringUtils.isNotEmpty(url)) {
//                                    i = new Intent(context, StoreWebActivity.class);
//                                    i.putExtra(Constants.URL, url);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
//                                    builder1.setContentIntent(pendingIntent);
                                }

                                NotificationManager manager1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                if (manager1 != null && AppConfig.userEntity != null) {
                                    manager1.notify((int) System.currentTimeMillis() / 1000, builder1.build());
                                }
                                break;
                            case 4:
                                Bus.getDefault().post("web_store_reload");
                                break;

                            case 5:
//                                String function = jsonObject.getString("fun");
//                                intent = new Intent(context, StoreWebActivity.class);
//                                intent.putExtra("function", function);
//                                context.startActivity(intent);
                                break;
                            case 7:
                                title = jsonObject.getString("title");
                                content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context)
                                        .setContentTitle(title)
                                        .setContentText(content)
                                        .setTicker("好友绑定成功")
                                        .setSmallIcon(R.mipmap.app_logo)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                                        .setWhen(System.currentTimeMillis())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                                        .setOngoing(false);

//                                i = new Intent(context, MyInviteRangeActivity.class);
//                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
//                                builder2.setContentIntent(pendingIntent);
//                                NotificationManager manager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                                if (manager2 != null && AppConfig.userEntity != null) {
//                                    manager2.notify((int) System.currentTimeMillis() / 1000, builder2.build());
//                                }

                                break;
                            case 8:
                                title = jsonObject.getString("title");
                                content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                                NotificationCompat.Builder builder3 = new NotificationCompat.Builder(context)
                                        .setContentTitle(title)
                                        .setContentText(content)
                                        .setTicker("好友绑定成功")
                                        .setSmallIcon(R.mipmap.app_logo)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                                        .setWhen(System.currentTimeMillis())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                                        .setOngoing(false);

                                i = new Intent(context, BillRecordActivity.class);
                                PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder3.setContentIntent(pendingIntent1);
                                NotificationManager manager3 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                if (manager3 != null && AppConfig.userEntity != null) {
                                    manager3.notify((int) System.currentTimeMillis() / 1000, builder3.build());
                                }


                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if ("cn.jpush.android.intent.CONNECTION".equals(intent.getAction())){
            if (Utils.isNetLink()){
                if ((System.currentTimeMillis() - newtworkTime) > 3000) {
                    newtworkTime = System.currentTimeMillis();
                    BusCallEntity busCallEntity=new BusCallEntity();
                    busCallEntity.setCallType(BusEnum.NETWOR_CONNECTION);
                    Bus.getDefault().post(busCallEntity);
                }
            }else {
//                NoNetworkActivity.starter(context);
            }
        }
    }

    private void getArticle(String aId, final Context context, final String title, final String content) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_id", aId);
        if (AppConfig.userEntity != null) {
            params.put("my_customer_id", AppConfig.userEntity.customer_id);
        }
        new ApiImp().getArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setTicker("百家姓给您发来一条消息")
                        .setSmallIcon(R.mipmap.app_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                        .setWhen(System.currentTimeMillis())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setOngoing(false);
                Intent i;
                Article article = JSON.parseObject(data, Article.class);
                i = new Intent(context, ArticleInfoActivity.class);
                i.putExtra("article", article);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null && AppConfig.userEntity != null) {
                    manager.notify((int) System.currentTimeMillis() / 1000, builder.build());
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
