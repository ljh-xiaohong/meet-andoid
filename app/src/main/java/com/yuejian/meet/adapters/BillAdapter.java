package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.Bill;
import com.yuejian.meet.bean.DateHead;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.PinnedSectionListView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zh02 on 2017/8/22.
 */

public class BillAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context context;
    private List<Object> dataSource = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.00");

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_BILL = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd", Locale.getDefault());

    public BillAdapter(Context context, List<Object> ds) {
        this.context = context;
        if (ds != null) {
            dataSource = ds;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_HEAD;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (getItemViewType(position) == TYPE_HEAD) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_bill_head, null);
                holder = new ViewHolder();
                holder.header = (TextView) convertView.findViewById(R.id.head_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Object obj = dataSource.get(position);
            if (obj instanceof DateHead) {
                DateHead head = (DateHead) obj;
                Calendar calendar = Calendar.getInstance();
                calendar.set(head.year, head.month, head.day);

                Calendar current = Calendar.getInstance();
                int currentYear = current.get(Calendar.YEAR);
                int currentMonth = current.get(Calendar.MONTH) + 1;
                int currentDay = current.get(Calendar.DAY_OF_MONTH);

                if (head.year == currentYear && head.month == currentMonth) {
                    holder.header.setText("本月");
                } else if (head.year == currentYear && head.month != currentMonth) {
                    holder.header.setText(head.month + "月");
                } else {
                    holder.header.setText(head.year + "年" + head.month + "月");
                }
            }


        } else if (TYPE_BILL == getItemViewType(position)) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_bill, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.bill_icon);
                holder.account = (TextView) convertView.findViewById(R.id.bill_account);
                holder.weekDay = (TextView) convertView.findViewById(R.id.weekday);
                holder.date = (TextView) convertView.findViewById(R.id.bill_date);
                holder.content = (TextView) convertView.findViewById(R.id.bill_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Object object = dataSource.get(position);
            if (object instanceof Bill) {
                final Bill bill = (Bill) object;
                String account = (bill.amount > 0 ? "+" : "") + df.format(bill.amount);
                holder.account.setText(account);
                Glide.with(context).load(bill.photo).into(holder.icon);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(bill.create_time);

                Calendar current = Calendar.getInstance();
                int currentYear = current.get(Calendar.YEAR);
                int currentMonth = current.get(Calendar.MONTH);
                int currentDay = current.get(Calendar.DAY_OF_MONTH);

                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                if (currentYear == y && currentMonth == m) {
                    if (currentDay == d) {
                        holder.weekDay.setText("今天");
                    } else if (currentDay - d == 1) {
                        holder.weekDay.setText("昨天");
                    } else {
                        holder.weekDay.setText(getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
                    }
                } else {
                    holder.weekDay.setText(getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
                }
                holder.date.setText(dateFormat.format(new Date(bill.create_time)));
                holder.content.setText(bill.content);
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtils.isNotEmpty(bill.op_customer_id) && !"0".equals(bill.op_customer_id)) {
                            AppUitls.goToPersonHome(context,  bill.op_customer_id);
                        }
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = dataSource.get(position);
        if (obj instanceof DateHead) {
            return TYPE_HEAD;
        } else {
            return TYPE_BILL;
        }
    }

    private class ViewHolder {
        TextView weekDay;
        TextView date;
        TextView account;
        ImageView icon;
        TextView content;
        TextView header;
    }

    private String getWeekDay(int weekDay) {
        switch (weekDay) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";

        }
        return "";
    }
}
