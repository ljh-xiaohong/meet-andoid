package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.NotificationMsgBean;
import com.yuejian.meet.bean.UnreadMessage;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 通知中心
 * Created by zh02 on 2017/8/18.
 */

public class NotificationCenterActivity extends BaseActivity {

    private NotificationMsgBean msgBean = null;
    @Bind(R.id.fans_count)
    TextView fansCount;
    @Bind(R.id.video_comment_cnt)
    TextView videoCommentCnt;
    @Bind(R.id.essay_message_cnt)
    TextView essayMsgCnt;
    @Bind(R.id.system_message_cnt)
    TextView sysMsgCnt;
    @Bind(R.id.examine_result_cnt)
    TextView examineResultCnt;
    @Bind(R.id.report_detail_cnt)
    TextView reportDetailCnt;
    @Bind(R.id.shop_message_cnt)
    TextView shopMsgCnt;
    @Bind(R.id.clan_msg_cnt)
    TextView clanMsgCnt;
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_notification_center);
        boolean mallSwitch = PreferencesUtil.readBoolean(getApplicationContext(), Constants.MALL_SWITCH);
        findViewById(R.id.syxx).setVisibility(mallSwitch ? View.VISIBLE : View.GONE);
        setTitleText(getString(R.string.notification_center));
        ACTIVITY_NAME = getString(R.string.notification_center);
    }

    @OnClick({
            R.id.xdfs,
            R.id.spjg,
            R.id.wzxx,
            R.id.xttz,
            R.id.shjg,
            R.id.jbxq,
            R.id.syxx,
            R.id.zpxx,
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xdfs:
                type = 1;
                getUnReadList();
                break;
            case R.id.spjg:
                type = 2;
                getUnReadList();
                break;
            case R.id.wzxx:
                type = 3;
                getUnReadList();
                break;
            case R.id.xttz:
                type = 4;
                getUnReadList();
                break;
            case R.id.shjg:
                type = 5;
                getUnReadList();
                break;
            case R.id.jbxq:
                type = 6;
                getUnReadList();
                break;
            case R.id.syxx:
                type = 7;
                getUnReadList();
                break;
            case R.id.zpxx:
                type = 8;
                getUnReadList();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnReadMsgCount();
    }

    private ArrayList<UnreadMessage> unreadMessages = new ArrayList<>();

    private void getUnReadList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("type", String.valueOf(type));
        params.put("pageItemCount", String.valueOf(1000));
        apiImp.getMyUnReadList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                unreadMessages.clear();
                ArrayList<UnreadMessage> temp = (ArrayList<UnreadMessage>) JSON.parseArray(data, UnreadMessage.class);
                if (temp != null && !temp.isEmpty()) {
                    unreadMessages.addAll(temp);
                }
                Intent intent = new Intent(getBaseContext(), NotificationDetailActivity.class);
                if (type == 1) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_FANS);
                } else if (type == 2) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_VIDEO);
                } else if (type == 3) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_ESSAY);
                } else if (type == 4) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_SYSTEM);
                } else if (type == 5) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_EXAMINE);
                } else if (type == 6) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_REPORT);
                } else if (type == 7) {
                    intent = new Intent(getBaseContext(), StoreWebActivity.class);
                    intent.putExtra(Constants.URL, "http://shangyu.yuejianchina.com/yuejian/h5/mymessage?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app");
                    startActivity(intent);
                    return;
                } else if (type == 8) {
                    intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_CLAN);
                }
                intent.putParcelableArrayListExtra(Constants.NOTIFICATION_LIST, unreadMessages);
                startActivity(intent);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void getUnReadMsgCount() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        apiImp.getUnReadMsgCount(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    JSONObject dataJson = new JSONObject(data);
                    Iterator<String> keys = dataJson.keys();
//                    1, "粉丝消息",2,"视频评价"3, "文章消息",4,"系统消息"5,"审核结果"6,"举报详情"
                    msgBean = new NotificationMsgBean();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        int count = dataJson.getInt(key);
                        String countStr = String.valueOf(count);
                        if ("1".equals(key)) {
                            msgBean.fansMsgCnt = countStr;
                        } else if ("2".equals(key)) {
                            msgBean.videoCommentCnt = countStr;
                        } else if ("3".equals(key)) {
                            msgBean.essayMsgCnt = countStr;
                        } else if ("4".equals(key)) {
                            msgBean.systemMsgCnt = countStr;
                        } else if ("5".equals(key)) {
                            msgBean.examineResultMsgCnt = countStr;
                        } else if ("6".equals(key)) {
                            msgBean.reportDetailMsgCnt = countStr;
                        } else if ("7".equals(key)) {
                            msgBean.shopMsgCnt = countStr;
                        } else if ("8".equals(key)) {
                            msgBean.clanMsgCnt = countStr;
                        }
                    }
                    if (Integer.parseInt(msgBean.fansMsgCnt) > 0) {
                        fansCount.setVisibility(View.VISIBLE);
                        fansCount.setText(msgBean.fansMsgCnt);
                    } else {
                        fansCount.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.videoCommentCnt) > 0) {
                        videoCommentCnt.setVisibility(View.VISIBLE);
                        videoCommentCnt.setText(msgBean.videoCommentCnt);
                    } else {
                        videoCommentCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.essayMsgCnt) > 0) {
                        essayMsgCnt.setVisibility(View.VISIBLE);
                        essayMsgCnt.setText(msgBean.essayMsgCnt);
                    } else {
                        essayMsgCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.systemMsgCnt) > 0) {
                        sysMsgCnt.setVisibility(View.VISIBLE);
                        sysMsgCnt.setText(msgBean.systemMsgCnt);
                    } else {
                        sysMsgCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.examineResultMsgCnt) > 0) {
                        examineResultCnt.setVisibility(View.VISIBLE);
                        examineResultCnt.setText(msgBean.examineResultMsgCnt);
                    } else {
                        examineResultCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.reportDetailMsgCnt) > 0) {
                        reportDetailCnt.setVisibility(View.VISIBLE);
                        reportDetailCnt.setText(msgBean.reportDetailMsgCnt);
                    } else {
                        reportDetailCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.shopMsgCnt) > 0) {
                        shopMsgCnt.setVisibility(View.VISIBLE);
                        shopMsgCnt.setText(msgBean.shopMsgCnt);
                    } else {
                        shopMsgCnt.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(msgBean.clanMsgCnt) > 0) {
                        clanMsgCnt.setVisibility(View.VISIBLE);
                        clanMsgCnt.setText(msgBean.clanMsgCnt);
                    } else {
                        clanMsgCnt.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
}
