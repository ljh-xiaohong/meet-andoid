package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SignDay;
import com.yuejian.meet.bean.SignPerson;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.LunarCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 每日签到
 */
public class EveryDaySignActivity extends BaseActivity {
    private SignRangerAdapter adapter = null;
    private LunarCalendarView lunarCalendarView;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private TextView signBtn = null;
    private List<SignPerson> signPeople = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_every_day_sign);
        initBackButton(true);
        setTitleText(getString(R.string.mine_daily_attendance));
        View headerView = View.inflate(this, R.layout.every_day_sign_header, null);
        this.lunarCalendarView = ((LunarCalendarView) headerView.findViewById(R.id.lunar_calendar));
        this.signBtn = ((TextView) headerView.findViewById(R.id.sign_btn));
        ((TextView) headerView.findViewById(R.id.month)).setText(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
        ((TextView) headerView.findViewById(R.id.year)).setText(TimeUtils.getYear(System.currentTimeMillis()));
        signBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (user==null){
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    return;
                }
                EveryDaySignActivity.this.sign();
            }
        });
        ListView localListView = (ListView) findViewById(R.id.sign_list);
        localListView.addHeaderView(headerView);
        this.adapter = new SignRangerAdapter();
        localListView.setAdapter(this.adapter);
        findSignDays();
        findSignInRanking();
        checkCustomerSignin();
    }


    private void checkCustomerSignin() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        this.apiImp.checkCustomerSignIn(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                if ((data != null) && ("1".equals(data))) {
                    EveryDaySignActivity.this.signBtn.setSelected(true);
                    EveryDaySignActivity.this.signBtn.setText(R.string.action_dialog_text6);
                }
            }
        });
    }


    private void findSignDays() {
        HashMap<String, Object> localHashMap = new HashMap<>();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("date", new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date()));
        this.apiImp.findCustomerSignIn(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                List<SignDay> tmp = JSON.parseArray(data, SignDay.class);
                ArrayList<Integer> signDays = new ArrayList<>();
                for (SignDay day : tmp) {
                    try {
                        signDays.add(sdf.parse(day.signin_date).getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                EveryDaySignActivity.this.lunarCalendarView.setSignDays(signDays);
            }
        });
    }

    private void findSignInRanking() {
        this.apiImp.findSignInRanking(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
            public void onFailed(String error, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                List<SignPerson> signPeople = JSON.parseArray(data, SignPerson.class);
                EveryDaySignActivity.this.signPeople.clear();
                EveryDaySignActivity.this.signPeople.addAll(signPeople);
                EveryDaySignActivity.this.adapter.notifyDataSetChanged();
            }
        });
    }

    private void sign() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        this.apiImp.customerSignIn(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                findSignDays();
                Bus.getDefault().post("sign_today");
                signBtn.setSelected(true);
                signBtn.setText(R.string.action_dialog_text6);
            }
        });
    }

    public void onClick(View paramView) {
    }

    private class SignRangerAdapter
            extends BaseAdapter {
        private SignRangerAdapter() {
        }

        public int getCount() {
            return EveryDaySignActivity.this.signPeople.size();
        }

        public Object getItem(int paramInt) {
            return EveryDaySignActivity.this.signPeople.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(EveryDaySignActivity.this.getBaseContext(), R.layout.item_sign_range, null);
                holder.name = ((TextView) paramView.findViewById(R.id.name));
                holder.range = ((TextView) paramView.findViewById(R.id.range));
                holder.signDays = ((TextView) paramView.findViewById(R.id.series_days));
                holder.photo = ((ImageView) paramView.findViewById(R.id.photo));
                holder.age = ((TextView) paramView.findViewById(R.id.age));
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            if (paramInt == 0) {
                paramView.findViewById(R.id.header_title).setVisibility(View.VISIBLE);
            } else {
                paramView.findViewById(R.id.header_title).setVisibility(View.GONE);
            }
            final SignPerson signPerson = EveryDaySignActivity.this.signPeople.get(paramInt);
            holder.age.setSelected(signPerson.getSex().equals("1"));
            holder.age.setText(" "+signPerson.getAge());
            Glide.with(EveryDaySignActivity.this.getBaseContext()).load(signPerson.photo).into(holder.photo);
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EveryDaySignActivity.this.getBaseContext(), WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + signPerson.customer_id);
                    EveryDaySignActivity.this.startActivity(intent);
                }
            });

            ImageView commend = (ImageView) paramView.findViewById(R.id.commend);
            holder.range.setVisibility(View.GONE);
            switch (paramInt) {
                case 0:
                    int color = Color.parseColor("#de4038");
                    commend.setImageResource(R.mipmap.n1);
                    commend.setVisibility(View.VISIBLE);
                    holder.signDays.setTextColor(color);
                    holder.signDays.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(getBaseContext(), 15.0F));
                    break;
                case 1:
                    color = Color.parseColor("#f89d21");
                    commend.setImageResource(R.mipmap.n2);
                    commend.setVisibility(View.VISIBLE);
                    holder.signDays.setTextColor(color);
                    holder.signDays.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(getBaseContext(), 15.0F));
                    break;
                case 2:
                    color = Color.parseColor("#cfa86e");
                    commend.setVisibility(View.VISIBLE);
                    commend.setImageResource(R.mipmap.n3);
                    holder.signDays.setTextColor(color);
                    holder.signDays.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(getBaseContext(), 15.0F));
                    break;
                default: {
                    commend.setVisibility(View.GONE);
                    holder.signDays.setTextColor(Color.parseColor("#232323"));
                    holder.signDays.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(getBaseContext(), 11f));
                    holder.range.setVisibility(View.VISIBLE);
                }
            }
            String text = (String) holder.signDays.getText();
            holder.signDays.setText(signPerson.days);
//            holder.signDays.setText(text.replace("##", String.valueOf(signPerson.days)));
            holder.range.setText(String.valueOf(paramInt + 1));
            holder.name.setText(signPerson.surname + signPerson.name);
            return paramView;
        }

        class ViewHolder {
            TextView age;
            TextView name;
            ImageView photo;
            TextView range;
            TextView signDays;
        }
    }
}
