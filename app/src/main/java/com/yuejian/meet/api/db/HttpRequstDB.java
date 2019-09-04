package com.yuejian.meet.api.db;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alipay.android.phone.mrpc.core.HttpException;
import com.aliyun.common.utils.MD5Util;
import com.google.gson.Gson;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.NoNetworkActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import taobe.tec.jcc.JChineseConvertor;

/**
 * Created by zh03 on 2017/6/10.
 */

public class HttpRequstDB {

    private Map<String, Object> paramsIn;

    /**
     * post请求方法
     *
     * @param url    请求地址
     * @param params 方法参数Map方式
     * @param tag    tag（一般传上下文）
     * @param scall  回调自己定义的接口
     */
    public void postRequst(String url, Map<String, Object> params, final Object tag, final DataIdCallback<String> scall) {
        if (!Utils.isNetLink()) {
            scall.onFailed("", "网络错误", 0);
            Bus.getDefault().post("网络错误，请设置网络" + AppConfig.Toasshow);
            NoNetworkActivity.starter(MyApplication.context);
            return;
        }
        paramsIn = params;
        params = mapT2s(params);
        if (AppConfig.userEntity != null) {
            params.put("token", AppConfig.userEntity.getToken());
            params.put("my_customer_id", AppConfig.CustomerId);
            params.put("token_customer_id", AppConfig.CustomerId);
        }
        Map<String,String> map=new HashMap<>();
        map.put("param",JSON.toJSONString(params));
        map.put("sign", MD5Util.getMD5(JSON.toJSONString(params)+MD5Util.getMD5("###yuejian!***")));
        OkHttpUtils.post().url(url).params(map).tag(tag).build().connTimeOut(10000).writeTimeOut(11000).readTimeOut(11000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                String errorName = "";
                if (e instanceof HttpException) {
                    HttpException hE = (HttpException) e;
                    switch (hE.getCode()) {
                        case 504:
                            errorName = "网关超时";
                            break;
                        case 408:
                            errorName = "网络连接较差";
                            break;
                        case 505:
                            errorName = "HTTP版本不受支持";
                            break;
                        case 404:
                            errorName = "找不到对象";
                            break;
                        case 400:
                            errorName = "参数请求错误";
                            break;
                        case 405:
                            errorName = "请求方法有有误";
                            break;
                        case 500:
                            errorName = "连接超时";
                            break;
                    }
                } else if (e instanceof SocketTimeoutException) {
                    errorName = "网络连接较差";
                } else if (e instanceof UnknownHostException) {
                    errorName = "当前网络不可用，请检查网络";
                } else if (e instanceof ConnectException) {
                    errorName = "网络连接错误";
                }
                e.printStackTrace();
                scall.onFailed("", errorName, id);
                if (!StringUtil.isEmpty(errorName))
                    Bus.getDefault().post(errorName + AppConfig.Toasshow);
            }

            @Override
            public void onResponse(String result, int id) {
                try {
                    result = s2t(result);
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    String pas = "?";

                    for (String s : paramsIn.keySet()) {
                        pas += s + "=" + paramsIn.get(s) + "&";
                    }
                    Log.e("LogURL", url + "---" + pas + "---" + result);
                    scall.onSuccess(result, id);
//                    if ("0".equals(code)) {
////                        if (object.has("data")) {
////                            String data = object.getString("data");
////                            // data = data.replaceAll("\\\"\\[\\{", "[{").replaceAll("\\}\\]\\\"", "}]").replaceAll("\\\\n", "").replaceAll("\\\\", "");
////                            data = data.replaceAll(":null", ":\"\" ");
////                            try {
////                                scall.onSuccess(data, id);
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
////                        } else {
////                            try {
////                                scall.onSuccess("", id);
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
////                        }
//                    } else if ("-3".equals(code)) {//重新登录
//                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
//                        ImUtils.logout();
//                        Bus.getDefault().post(AppConfig.userKick);
//                    } else if ("-4".equals(code)) {///冻结
//                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
//                        ImUtils.logout();
//                        Bus.getDefault().post(AppConfig.user_freeze);
//                    } else {
//                        scall.onFailed(code, msg, id);
//                        Bus.getDefault().post(msg + AppConfig.Toasshow);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Bus.getDefault().post("数据异常" + AppConfig.Toasshow);
                    scall.onFailed("", "数据异常", id);
                }
            }
        });
    }

    /**
     * get请求方法
     *
     * @param url    请求地址
     * @param params 方法参数Map方式
     * @param tag    tag（一般传上下文）
     * @param scall  回调自己定义的接口
     */
    public void getRequst(String url, Map<String, Object> params, final Object tag, final DataIdCallback<String> scall) {
        if (!Utils.isNetLink()) {
            scall.onFailed("", "网络错误", 0);
            Bus.getDefault().post("网络错误，请设置网络" + AppConfig.Toasshow);
            NoNetworkActivity.starter(MyApplication.context);
            return;
        }
        paramsIn = params;
        params = mapT2s(params);
        if (AppConfig.userEntity != null) {
            params.put("token", AppConfig.userEntity.getToken());
            params.put("my_customer_id", AppConfig.CustomerId);
            params.put("token_customer_id", AppConfig.CustomerId);
        }
        Map<String,String> map=new HashMap<>();
        map.put("param",JSON.toJSONString(params));
        map.put("sign", MD5Util.getMD5(JSON.toJSONString(params)+MD5Util.getMD5("###yuejian!***")));
        OkHttpUtils.get().url(url).params(map).tag(tag).build().connTimeOut(10000).writeTimeOut(10000).readTimeOut(10000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String errorName = "";
                if (e instanceof HttpException) {
                    HttpException hE = (HttpException) e;
                    switch (hE.getCode()) {
                        case 504:
                            errorName = "网关超时";
                            break;
                        case 408:
                            errorName = "网络连接较差";
                            break;
                        case 505:
                            errorName = "HTTP版本不受支持";
                            break;
                        case 400:
                            errorName = "参数请求错误";
                            break;
                        case 405:
                            errorName = "请求方法有有误";
                            break;
                        case 500:
                            errorName = "连接超时";
                            break;
                    }
                } else if (e instanceof SocketTimeoutException) {
                    errorName = "网络连接较差";
                } else if (e instanceof UnknownHostException) {
                    errorName = "当前网络不可用，请检查网络";
                } else if (e instanceof ConnectException) {
                    errorName = "网络连接错误";
                }
                errorName = s2t(errorName);
                FCLoger.error("" + call.request().toString() + "====" + e.getMessage());
                scall.onFailed("", errorName, id);
                if (!StringUtil.isEmpty(errorName))
                    Bus.getDefault().post(errorName + AppConfig.Toasshow);
            }

            @Override
            public void onResponse(String result, int id) {
                try {
                    result = s2t(result);
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    scall.onSuccess(result, id);
//                    if ("0".equals(code)) {
//
//                    } else if ("-3".equals(code)) {//重新登录
//                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
//                        ImUtils.logout();
//                        Bus.getDefault().post(AppConfig.userKick);
//                    } else if ("-4".equals(code)) {///冻结
//                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
//                        ImUtils.logout();
//                        Bus.getDefault().post(AppConfig.user_freeze);
//                    } else {
//                        scall.onFailed(code, msg, id);
//                        Bus.getDefault().post(msg + AppConfig.Toasshow);
//                    }

                } catch (JSONException e) {
                    Bus.getDefault().post("数据异常" + AppConfig.Toasshow);
                }
            }
        });
    }

    public Map<String, Object> mapT2s(Map<String, Object> map) {
        if (AppConfig.language.equals("1"))
            return map;
        Map<String, Object> params = new HashMap<>();
        if (null == map)
            return params;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value = "";
            try {
                JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
                value = jChineseConvertor.t2s(entry.getValue()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            params.put(entry.getKey(), value);
        }
        return params;
    }

    public String s2t(String s) {
        if (AppConfig.language.equals("1"))
            return s;
        String data = "";
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            data = jChineseConvertor.s2t(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void postRequst(String url, final Object tag, final DataIdCallback<String> scall) {
        if (!Utils.isNetLink()) {
            scall.onFailed("", "网络错误", 0);
            Bus.getDefault().post("网络错误，请设置网络" + AppConfig.Toasshow);
            NoNetworkActivity.starter(MyApplication.context);
            return;
        }

        OkHttpUtils.post().url(url).tag(tag).build().connTimeOut(10000).writeTimeOut(11000).readTimeOut(11000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                String errorName = "";
                if (e instanceof HttpException) {
                    HttpException hE = (HttpException) e;
                    switch (hE.getCode()) {
                        case 504:
                            errorName = "网关超时";
                            break;
                        case 408:
                            errorName = "网络连接较差";
                            break;
                        case 505:
                            errorName = "HTTP版本不受支持";
                            break;
                        case 404:
                            errorName = "找不到对象";
                            break;
                        case 400:
                            errorName = "参数请求错误";
                            break;
                        case 405:
                            errorName = "请求方法有有误";
                            break;
                        case 500:
                            errorName = "连接超时";
                            break;
                    }
                } else if (e instanceof SocketTimeoutException) {
                    errorName = "网络连接较差";
                } else if (e instanceof UnknownHostException) {
                    errorName = "当前网络不可用，请检查网络";
                } else if (e instanceof ConnectException) {
                    errorName = "网络连接错误";
                }
                e.printStackTrace();
                scall.onFailed("", errorName, id);
                if (!StringUtil.isEmpty(errorName))
                    Bus.getDefault().post(errorName + AppConfig.Toasshow);
            }

            @Override
            public void onResponse(String result, int id) {
                try {
                    result = s2t(result);
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    if ("0".equals(code)) {
                        scall.onSuccess(result, id);
                    } else if ("-3".equals(code)) {//重新登录
                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
                        ImUtils.logout();
                        Bus.getDefault().post(AppConfig.userKick);
                    } else if ("-4".equals(code)) {///冻结
                        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
                        ImUtils.logout();
                        Bus.getDefault().post(AppConfig.user_freeze);
                    } else {
                        scall.onFailed(code, msg, id);
                        Bus.getDefault().post(msg + AppConfig.Toasshow);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Bus.getDefault().post("数据异常" + AppConfig.Toasshow);
                    scall.onFailed("", "数据异常", id);
                }
            }
        });
    }


}
