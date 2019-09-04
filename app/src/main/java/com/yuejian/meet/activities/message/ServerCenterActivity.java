package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pro on 2017/11/27.
 */

public class ServerCenterActivity extends BaseActivity implements SpringView.OnFreshListener {
    private ArrayList<Server> dataSource = new ArrayList<>();
    private ServerAdapter adapter = null;
    private Observer<List<RecentContact>> messageObserver;
    private SpringView springView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_center);
        setTitleText("客服中心");
        springView = (SpringView) findViewById(R.id.spring_view);
        dataSource.addAll((Collection<? extends Server>) getIntent().getSerializableExtra("server_list"));
        adapter = new ServerAdapter();
        ListView list = (ListView) findViewById(R.id.server_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Server server = dataSource.get(i);
                server.unreadMsgCnt = 0;
                adapter.notifyDataSetChanged();
                ImUtils.toStoreP2PCaht(mContext, server.customer_id);
            }
        });

        messageObserver = new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> messages) {
                if (dataSource != null) {
                    for (Server server : dataSource) {
                        for (RecentContact contact : messages) {
                            if (server.getCustomer_id().equals(contact.getContactId())) {
                                server.recentContent = contact.getContent().trim();
                                server.recentTime = contact.getTime();
                                server.unreadMsgCnt = contact.getUnreadCount();
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };

        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);

        springView.setListener(this);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        getServerList();
    }

    @Override
    public void onLoadmore() {
        springView.postDelayed(new Runnable() {
            @Override
            public void run() {
                springView.onFinishFreshAndLoad();
            }
        }, 800);
    }

    private void getServerList() {
        HashMap<String, Object> params = new HashMap<>();
        apiImp.getKfList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Server> servers = JSON.parseArray(data, Server.class);
                List<Server> tmp = new ArrayList<>();
                tmp.addAll(dataSource);
                dataSource.clear();
                if(tmp.size() == 0) {
                    tmp.addAll(servers);
                } else {
                    for (Server server : servers) {
                        boolean isExist = false;
                        for (Server s : tmp) {
                            if (server.customer_id.equals(s.customer_id)) {
                                s.is_online = server.is_online;
                                isExist = true;
                                break;
                            } else {
                                isExist = false;
                            }
                        }
                        if (!isExist) {
                            dataSource.add(server);
                        }
                    }
                }
                dataSource.addAll(tmp);
                Collections.sort(dataSource, new Comparator<Server>() {
                    @Override
                    public int compare(Server o1, Server o2) {
                        if ("1".equals(o1.is_online)) {
                            if(o1.recentTime > o2.recentTime){
                                return -1;
                            } else {
                                return 0;
                            }
                        } else {
                            return 1;
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                springView.onFinishFreshAndLoad();
            }
        });
    }

    private class ServerAdapter extends BaseAdapter {
        private SimpleDateFormat dateFormat = null;

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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getBaseContext(), R.layout.item_server, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.server_icon);
                holder.name = (TextView) convertView.findViewById(R.id.server_name);
                holder.onlineState = (TextView) convertView.findViewById(R.id.online_state);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.contact_time);
                holder.badge = (TextView) convertView.findViewById(R.id.unreand_msg_cnt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Server server = dataSource.get(i);
            String name = server.surname + server.name;
            if (name.length() > 6) {
                name.replace(name.subSequence(6, name.length() - 1), "...");
            }
            holder.name.setText(name);
            Glide.with(getBaseContext()).load(server.photo).error(R.mipmap.ic_default).into(holder.icon);
            boolean isOnline = "1".equals(server.is_online);
            holder.onlineState.setText(isOnline ? "在线" : "离线");
            holder.onlineState.setEnabled(isOnline);
            holder.content.setText(server.recentContent);
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat();
            }
            if (server.recentTime == 0) {
                holder.time.setVisibility(View.INVISIBLE);
            } else {
                holder.time.setVisibility(View.VISIBLE);
                boolean today = DateUtils.isToday(server.recentTime);
                if (today) {
                    dateFormat.applyPattern("hh:mm");
                } else {
                    dateFormat.applyPattern("yyyy-MM-dd hh:mm");
                }
                String time = dateFormat.format(new Date(server.recentTime));
                holder.time.setText(time);
            }
            if (server.unreadMsgCnt <= 0) {
                holder.badge.setText("0");
                holder.badge.setVisibility(View.INVISIBLE);
            } else {
                holder.badge.setVisibility(View.VISIBLE);
                holder.badge.setText(String.valueOf(server.unreadMsgCnt));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView onlineState;
            TextView content;
            TextView time;
            TextView badge;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, false);
    }
}
