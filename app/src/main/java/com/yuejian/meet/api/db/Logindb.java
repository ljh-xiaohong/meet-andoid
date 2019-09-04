package com.yuejian.meet.api.db;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alipay.android.phone.mrpc.core.HttpException;
import com.aliyun.common.utils.MD5Util;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.utils.AppinitUtil;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import taobe.tec.jcc.JChineseConvertor;

/**
 * Created by zh03 on 2017/6/9.
 */

public class Logindb {
    public void postRequst(String url, Map<String ,Object> params,final Object tag, final DataIdCallback<String> scall){
        if (!Utils.isNetLink()){
            ViewInject.shortToast(AppConfig.context,"网络错误，请设置网络");
            scall.onFailed("","网络错误",0);
            return;
        }
//        String url= UrlConstant.LOGIN();
        params=mapT2s(params);
        FCLoger.debug("POST请求debug"+url);
        Map<String,String> map=new HashMap<>();
        map.put("param",JSON.toJSONString(params));
        map.put("sign", MD5Util.getMD5(JSON.toJSONString(params)+MD5Util.getMD5("###yuejian!***")));
        OkHttpUtils.post().url(url).params(map).tag(tag).build().connTimeOut(10000).writeTimeOut(10000).readTimeOut(10000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String errorName="";
                if (e instanceof HttpException){
                    HttpException hE= (HttpException) e;
                    switch (hE.getCode()){
                        case 504:
                            errorName="网关超时";
                            break;
                        case 408:
                            errorName="网络连接较差";
                            break;
                        case 505:
                            errorName="HTTP版本不受支持";
                            break;
                        case 400:
                            errorName="参数请求错误";
                            break;
                        case 405:
                            errorName="请求方法有有误";
                            break;
                        case 500:
                            errorName="连接超时";
                            break;
                    }
                }else if (e instanceof SocketTimeoutException){
                    errorName="网络连接较差";
                }else if (e instanceof ConnectException){
                    errorName="网络连接错误";
                }
                e.printStackTrace();
                scall.onFailed("", errorName, id);
                if (!StringUtil.isEmpty(errorName))
                    Bus.getDefault().post(errorName + AppConfig.Toasshow);

            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    response=s2t(response);
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    scall.onSuccess(response,id);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Bus.getDefault().post("数据异常"+id+ AppConfig.Toasshow);
                    scall.onFailed("", AppinitUtil.NET_ERROR_TIPS,1);
                }
            }
        });
    }
    public void getRequst(String url,Map<String ,Object> params,final Object tag, final DataIdCallback<String> scall){
        if (!Utils.isNetLink()){
            ViewInject.shortToast(AppConfig.context,"网络错误，请设置网络");
            scall.onFailed("","网络错误",0);
            return;
        }
        params=mapT2s(params);
//        String url= UrlConstant.LOGIN();
        FCLoger.debug("POST请求debug"+url);
        Map<String,String> map=new HashMap<>();
        map.put("param",JSON.toJSONString(params));
        map.put("sign", MD5Util.getMD5(JSON.toJSONString(params)+MD5Util.getMD5("###yuejian!***")));
        OkHttpUtils.get().url(url).params(map).tag(tag).build().connTimeOut(10000).writeTimeOut(10000).readTimeOut(10000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String errorName="";
                if (e instanceof HttpException){
                    HttpException hE= (HttpException) e;
                    switch (hE.getCode()){
                        case 504:
                            errorName="网关超时";
                            break;
                        case 408:
                            errorName="网络连接较差";
                            break;
                        case 505:
                            errorName="HTTP版本不受支持";
                            break;
                        case 400:
                            errorName="参数请求错误";
                            break;
                        case 405:
                            errorName="请求方法有有误";
                            break;
                        case 500:
                            errorName="连接超时";
                            break;
                    }
                }else if (e instanceof SocketTimeoutException){
                    errorName="网络连接较差";
                }else if (e instanceof ConnectException){
                    errorName="网络连接错误";
                }
                e.printStackTrace();
                scall.onFailed("", errorName, id);
                if (!StringUtil.isEmpty(errorName))
                    Bus.getDefault().post(errorName + AppConfig.Toasshow);

            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    response=s2t(response);
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    scall.onSuccess(response,id);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Bus.getDefault().post("数据异常"+id+ AppConfig.Toasshow);
                    scall.onFailed("", AppinitUtil.NET_ERROR_TIPS,1);
                }
            }
        });
    }
    public Map<String,Object> mapT2s(Map<String,Object> map){
        if (AppConfig.language.equals("1"))
            return map;
        Map<String,Object> params=new HashMap<>();
        if (null == map)
            return params;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value="";
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            try {
                JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
                value=jChineseConvertor.t2s(entry.getValue()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            params.put(entry.getKey(),value);
        }
        return params;
    }
    public String s2t(String s){
        if (AppConfig.language.equals("1"))
            return s;
        String data="";
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            data=jChineseConvertor.s2t(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
