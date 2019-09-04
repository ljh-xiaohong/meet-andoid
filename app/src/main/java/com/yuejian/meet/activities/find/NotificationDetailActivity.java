package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.VideoCallInfoActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.bean.UnreadMessage;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/21.
 */

public class NotificationDetailActivity extends BaseActivity {
    private String notificationType = "";
    private ArrayList<UnreadMessage> notificationList;

    @Bind(R.id.notification_list)
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        notificationType = getIntent().getStringExtra(Constants.NOTIFICATION_TYPE);
        ACTIVITY_NAME = getString(R.string.essay_text_2);
        if (Constants.NOTIFICATION_FANS.equals(notificationType)) {
            setTitleText(getString(R.string.The_new_fan));
        } else if (Constants.NOTIFICATION_VIDEO.equals(notificationType)) {
            setTitleText(getString(R.string.Video_evaluation));
        } else if (Constants.NOTIFICATION_ESSAY.equals(notificationType)) {
            setTitleText(getString(R.string.Post_a_message));
        } else if (Constants.NOTIFICATION_SYSTEM.equals(notificationType)) {
            setTitleText(getString(R.string.tex_about_notice));
        } else if (Constants.NOTIFICATION_EXAMINE.equals(notificationType)) {
            setTitleText(getString(R.string.audit_result));
        } else if (Constants.NOTIFICATION_REPORT.equals(notificationType)) {
            setTitleText(getString(R.string.To_report_the_details));
        } else if (Constants.NOTIFICATION_CLAN.equals(notificationType)) {
            setTitleText(getString(R.string.Family_news));
        }
        ArrayList<UnreadMessage> list = getIntent().getParcelableArrayListExtra(Constants.NOTIFICATION_LIST);
        notificationList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            notificationList.addAll(list);
        }
        listView.setAdapter(new Adapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnreadMessage message = notificationList.get(position);
                if (message == null) return;
                if ("2".equals(message.msg_type)) {
                    Intent intent = new Intent(getBaseContext(), VideoCallInfoActivity.class);
                    intent.putExtra("video_id", message.getObject_id());
                    startActivity(intent);
                } else if ("8".equals(message.msg_type)) {
                    Intent intent = new Intent(getBaseContext(), WebActivity.class);
                    intent.putExtra("No_Title", true);
                    intent.putExtra(Constants.URL, message.msg_remark2);
                    startActivity(intent);
                } else if (!"5".equals(message.msg_type) && !"4".equals(message.msg_type)) {
                    AppUitls.goToPersonHome(mContext, message.op_customer_id);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notificationList.size();
        }

        @Override
        public Object getItem(int position) {
            return notificationList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getBaseContext(), R.layout.item_notification, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.commentIcon = (ImageView) convertView.findViewById(R.id.comment_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            消息类型(1, 粉丝消息,2,视频评价3, 文章消息,4,系统消息5,审核结果6,举报详情;)

            holder.commentIcon.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);
            UnreadMessage message = notificationList.get(position);
            holder.title.setText(message.title);
            holder.time.setText(StringUtils.friendlyTime(message.create_time));
            View view = (View) holder.icon.getParent();
            view.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.VISIBLE);
            if (StringUtils.isNotEmpty(message.msg_photo)) {
                Glide.with(getBaseContext()).load(message.msg_photo).asBitmap().into(holder.icon);
            }
            String msgType = message.msg_type.trim();
            if ("4".equals(msgType)) {
                holder.name.setVisibility(View.GONE);
                if (StringUtils.isEmpty(message.msg_photo)) {
                    view.setVisibility(View.INVISIBLE);
                }
            } else if ("2".equals(msgType)) {
                //视频评价
                holder.title.setVisibility(View.GONE);
                holder.name.setText(message.title);
                holder.commentIcon.setVisibility(View.VISIBLE);
                if ("3".equals(message.msg_remark)) {
                    holder.commentIcon.setImageDrawable(getResources().getDrawable(R.mipmap.chaping));
                } else if ("2".equals(message.msg_remark)) {
                    holder.commentIcon.setImageDrawable(getResources().getDrawable(R.mipmap.zhongping));
                } else {
                    holder.commentIcon.setImageDrawable(getResources().getDrawable(R.mipmap.haoping));
                }
            } else if ("5".equals(msgType)) {
                //审核结果
                holder.name.setVisibility(View.GONE);
                if ("1".equals(message.msg_remark)) {
                    view.setVisibility(View.VISIBLE);
                    holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.shtg));
                } else if ("0".equals(message.msg_remark)) {
                    view.setVisibility(View.VISIBLE);
                    holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.shwtg));
                }
            } else if ("6".equals(msgType)) {
                holder.name.setVisibility(View.GONE);
            } else if ("8".equals(msgType)) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setText(message.surname + message.name);
                if ("1".equals(msgType)) {
                    holder.title.setTextColor(Color.parseColor("#54abe6"));
                }
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            ImageView commentIcon;
            TextView name;
            TextView title;
            TextView time;
        }
    }
}
