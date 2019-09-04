package com.yuejian.meet.framents.mine;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CustomerInfo;
import com.yuejian.meet.bean.NotificationMsgBean;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zh03 on 2017/12/26.
 */

public class MineFragmentBC {
    public static int totalCount = 0;
    private NotificationMsgBean msgBean = null;
    ApiImp apiImp=new ApiImp();
    String typeKey;
    TextView msgCount,message_content;
    public MineFragmentBC(TextView content,TextView msgCount,NotificationMsgBean msgBean){
        this.msgCount=msgCount;
        this.msgBean=msgBean;
        this.message_content=content;
    }


    public void getUnReadMsgCount() {
        if (StringUtil.isEmpty(AppConfig.CustomerId )) {
            if (null != msgCount)
                msgCount.setVisibility(View.GONE);
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.getUnReadMsgCount(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    MineFragmentBC.totalCount = 0;
                    JSONObject dataJson = new JSONObject(data);
                    Iterator<String> keys = dataJson.keys();
                    int totalCount = 0;
//                    1, "粉丝消息",2,"视频评价"3, "文章消息",4,"系统消息"5,"审核结果"6,"举报详情"
                    while (keys.hasNext()) {
                        String key = keys.next();
                        int count = dataJson.getInt(key);
                        typeKey=key;
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
                        }
                        totalCount += count;
                        MineFragmentBC.totalCount += count;
                    }
                    if (null != msgCount){
                        if (totalCount > 0) {
                            msgCount.setVisibility(View.VISIBLE);
                            msgCount.setText(String.valueOf(totalCount));
                            message_content.setText(getCotent());
                        } else {
                            msgCount.setVisibility(View.GONE);
                            message_content.setText("暂无消息");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (null != msgCount)
                    msgCount.setVisibility(View.GONE);
            }
        });
    }
    public String getCotent(){
        String content="";
//        1, "粉丝消息",2,"视频评价"3, "文章消息",4,"商域消息"5,"审核结果"6,"举报详情"
        if ("1".equals(typeKey)){
            content="粉丝消息";
        }else if ("2".equals(typeKey)){
            content="视频评价";
        }else if ("3".equals(typeKey)){
            content="文章消息";
        }else if ("4".equals(typeKey)){
            content="商域消息";
        }else if ("5".equals(typeKey)){
            content="审核结果";
        }else if ("6".equals(typeKey)){
            content="举报详情";
        }
        return content;
    }

    /**
     * 用户不是完善资料
     * @param context
     * @param material_layout
     */
    public void getFindRatio(Context context, final LinearLayout material_layout){
        if (PreferencesUtil.readBoolean(context,PreferencesUtil.KEY_FIND_RATIO,false) || StringUtil.isEmpty(AppConfig.CustomerId)){
            material_layout.setVisibility(View.GONE);
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        apiImp.getFindPerfactRatio(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (Double.parseDouble(data)>=100){
                    material_layout.setVisibility(View.GONE);
                }else {
                    material_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    /**
     * 跳到资料编辑页面
     * @param context
     */
    public void findCustomerInfo(final Context context) {
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("customer_id", AppConfig.CustomerId);
            params.put("latitude", String.valueOf(AppConfig.slatitude));
            params.put("longitude", String.valueOf(AppConfig.slongitude));
            if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                params.put("my_customer_id", AppConfig.CustomerId);
            }
            apiImp.findCustomerInfo(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    CustomerInfo customerInfo = JSON.parseObject(data, CustomerInfo.class);
                    if (customerInfo == null) {
                        Toast.makeText(context, "该用户不存在", Toast.LENGTH_SHORT).show();
                    }
//                    Intent intent = new Intent(context, PersonInfoEditActivity.class);
//                    intent.putExtra(Constants.KEY_CUSTOMER_INFO, customerInfo);
//                    context.startActivity(intent);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {
                }
            });
        }
    }
}
