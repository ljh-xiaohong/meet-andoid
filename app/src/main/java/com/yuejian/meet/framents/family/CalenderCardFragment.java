package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.PerpetualCalendar;
import com.yuejian.meet.bean.Weather;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

public class CalenderCardFragment extends BaseFragment {
    private static final int UPDATE_CALENDER = 1110;
    private TextView avoidTv = null;
    private PerpetualCalendar calendar = null;
    private TextView dateTv = null;
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message paramAnonymousMessage) {
            if ((UPDATE_CALENDER == paramAnonymousMessage.what) && (calendar != null)) {
                String suitDoString = calendar.suit.replace(".", " ");
                suitDo.setText(suitDoString);
                String date = calendar.date;
                date = date.substring(date.indexOf('-') + 1, date.length()).replace("-", "月");
                date = date + "号";
                dateTv.setText(date);
                String avoid = calendar.avoid.replace(".", " ");
                avoidTv.setText(avoid);
                lunarDateTv.setText(String.valueOf("农历 " + calendar.lunar + " " + calendar.weekday));
            } else if (paramAnonymousMessage.what == 2) {
//                ((ImageView) rootView.findViewById(R.id.sign_btn)).setImageResource(2130903414);
            }
            return false;
        }
    });
    private TextView lunarDateTv = null;
    private View rootView;
    private TextView suitDo = null;

    private void getCalender() {
        if (getArguments() != null) {
            long l = getArguments().getLong("date");
            if (new Date(l).getDate() != new Date(System.currentTimeMillis()).getDate()) {
                this.rootView.findViewById(R.id.qiandao).setVisibility(View.GONE);
            }
            new ApiImp().getCalender(getDate(l), this, new StringCallback() {
                public void onError(Call paramAnonymousCall, Exception paramAnonymousException, int paramAnonymousInt) {
                    Log.d("error", paramAnonymousException.getMessage());
                }

                public void onResponse(String paramAnonymousString, int paramAnonymousInt) {
                    try {
                        JSONObject localJSONObject = new JSONObject(new JSONObject(paramAnonymousString).getString("result")).getJSONObject("data");
                        calendar = new PerpetualCalendar();
                        calendar.animalsYear = localJSONObject.getString("animalsYear");
                        calendar.avoid = localJSONObject.getString("avoid");
                        calendar.date = localJSONObject.getString("date");
                        calendar.suit = localJSONObject.getString("suit");
                        calendar.lunar = localJSONObject.getString("lunar");
                        calendar.lunarYear = localJSONObject.getString("lunarYear");
                        calendar.weekday = localJSONObject.getString("weekday");
                        calendar.year_month = localJSONObject.getString("year-month");
                        handler.sendEmptyMessage(1110);
                        Log.d("calender", paramAnonymousString);
                    } catch (JSONException error) {
                        error.printStackTrace();
                    }
                }
            });
        }
    }

    private String getDate(long paramLong) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(paramLong);
        int i = localCalendar.get(Calendar.YEAR);
        int j = localCalendar.get(Calendar.MONTH);
        int k = localCalendar.get(Calendar.DATE);
        return i + "-" + (j + 1) + "-" + k;
    }

    private void init() {
        this.suitDo = ((TextView) this.rootView.findViewById(R.id.suit_do));
        this.dateTv = ((TextView) this.rootView.findViewById(R.id.date));
        this.avoidTv = ((TextView) this.rootView.findViewById(R.id.avoid));
        this.lunarDateTv = ((TextView) this.rootView.findViewById(R.id.lunar_date));

        ImageView signImg = (ImageView) rootView.findViewById(R.id.qiandao);

        if ((getArguments() != null) && (getArguments().getBoolean("isSigned"))) {
            signImg.setImageResource(R.mipmap.qiandao2);
            signImg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("sign_every_day");
                }
            });
        } else {
            signImg.setImageResource(R.mipmap.qiandao1);
            signImg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    sign();
                }
            });
        }
    }

    private void sign() {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.customerSignIn(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                Bus.getDefault().post("sign_window");
                ((ImageView) rootView.findViewById(R.id.qiandao)).setImageResource(R.mipmap.qiandao2);
            }
        });
    }

    protected View inflaterView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.rootView = paramLayoutInflater.inflate(R.layout.layout_peroetual_calender, null);
        init();
        getCalender();
        String weatherString = PreferencesUtil.get(MyApplication.context, "weather", "");
        if (StringUtils.isNotEmpty(weatherString)) {
            List<Weather> weathers = JSON.parseArray(weatherString, Weather.class);
            long l = getArguments().getLong("date");
            String date = new SimpleDateFormat("yyyMMdd", Locale.getDefault()).format(new Date(l));
            for (Weather weather : weathers) {
                if (date.equals(weather.date)) {
                    ((TextView) this.rootView.findViewById(R.id.weather)).setText(String.valueOf(weather.weather + " " + weather.temperature));
                }
            }
        }
        return this.rootView;
    }
}
