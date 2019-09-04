package com.netease.nim.uikit.api;
import android.content.Context;
import android.util.Log;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zh03 on 2017/6/9.
 */

public class Requstdb {
    public void postRequest(String url,Map<String ,String> params,final Object tag, final DataCallback<String> scall){
        if(!AppConfig.CustomerId.equals("")&&!AppConfig.Token.equals("")){
            params.put("token", AppConfig.Token);
            params.put("token_customer_id", AppConfig.CustomerId);
            params.put("my_customer_id",AppConfig.CustomerId);
        }
        OkHttpUtils.post().url(url).params(params).tag(tag).build().connTimeOut(5000).writeTimeOut(6000).readTimeOut(6000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                scall.onFailed(call.hashCode()+"",e.getMessage());
                Bus.getDefault().post("网络连接异常"+ AppConfig.Toasshow);
            }

            @Override
            public void onResponse(String response, int i) {
                try {
                    Log.d("money",response);
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");

                    if ("0".equals(code)) {
                        if (object.has("data")) {
                            String data= object.getString("data");
                            scall.onSuccess(data.toString());
                        } else {
                            scall.onSuccess(msg);
//                            Bus.getDefault().post(msg+ AppConfig.Toasshow);
                        }
                    } else {
                        scall.onFailed(code, msg);
                        Bus.getDefault().post(msg+AppConfig.Toasshow);
                    }

                } catch (JSONException e) {
                    scall.onFailed("", "");
                }
            }
        });
    }
    public void getRequest(String url,Map<String ,String > params,final Object tag, final DataCallback<String> scall){
        if(!AppConfig.CustomerId.equals("")&&!AppConfig.Token.equals("")){
            params.put("token", AppConfig.Token);
            params.put("token_customer_id", AppConfig.CustomerId);
            params.put("my_customer_id",AppConfig.CustomerId);
        }
        OkHttpUtils.get().url(url).params(params).tag(tag).build().connTimeOut(5000).writeTimeOut(6000).readTimeOut(6000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                scall.onFailed(call.hashCode()+"",e.getMessage());
                Bus.getDefault().post("网络连接异常"+ AppConfig.Toasshow);
            }

            @Override
            public void onResponse(String response, int i) {
                try {
                    Log.d("money",response);
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");

                    if ("0".equals(code)) {
                        if (object.has("data")) {
                            String data= object.getString("data");
                            scall.onSuccess(data.toString());
                        } else {
                            scall.onSuccess(msg);
                            Bus.getDefault().post(msg+ AppConfig.Toasshow);
                        }
                    } else {
                        scall.onFailed(code, msg);
                        Bus.getDefault().post(msg+ AppConfig.Toasshow);
                    }

                } catch (JSONException e) {
                    scall.onFailed("", "");
                }
            }
        });
    }
}
