package com.netease.nim.uikit.api;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zh03 on 2017/7/3.
 */

public class NetApi {
    Requstdb requst=new Requstdb();
    /**
     * 回调扣钱
     * customerId    自己的id
     * opCustomerId  对方的id
     * chatType '聊天类型（1://文字、语音、图片 聊天，2:阅后即焚聊天，3:小视频聊天，4:小视频）'
     * @param scall
     */
    public void deductionMoney(String MycustomerId,String toCustomerId,String type, final Object tag, final DataCallback<String> scall){
        Map<String ,String > params=new HashMap<>();
        params.put("customerId",MycustomerId);
        params.put("opCustomerId",toCustomerId);
        params.put("chatType",type);
        requst.postRequest(UrlApi.GET_CONTROLLER_CHATEXPENSE(),params,tag,scall);
    }

    /**
     * 开始视频
     *  视频开始请求
     * customerId   自己的id
     * opCustomerId  对方的id
     * channelId   网易云id
     * @param params
     * @param tag
     * @param scall
     */
    public void startVideo(Map<String ,String > params, final Object tag, final DataCallback<String> scall){
        requst.postRequest(UrlApi.GET_CONTROLLER_ADDVIDEORECORD(),params,tag,scall);
    }

    /**
     * 结束视频
     */
    public void endVideo(String channelId, final Object tag, final DataCallback<String> scall){
        Map<String,String> params=new HashMap<>();
        params.put("channel_id",channelId);
        params.put("customer_id", AppConfig.CustomerId);
        requst.postRequest(UrlApi.GET_CONTROLLER_VIDEOENDCALL(),params,tag,scall);
    }

    /**
     * 查询余额
     * @param tag
     * @param scall
     */
    public void getBal(String customerId,final Object tag, final DataCallback<String> scall){
        Map<String ,String> params=new HashMap<>();
        params.put("customer_id",customerId);
        requst.getRequest(UrlApi.GET_ACCOUNT_GETMYBAL(),params,tag,scall);
    }
    /**
     * 获取视频价格---多少钱一分钟
     * @param tag
     * @param scall
     */
    public void getPrice(String customerId,final Object tag, final DataCallback<String> scall){
        Map<String ,String> params=new HashMap<>();
        params.put("customer_id",customerId);
        requst.postRequest(UrlApi.POST_POSTING_GETPOSTPRICE(),params,tag,scall);
    }

    /**
     *发送礼物
     * @param tag
     * @param scall
     */
    public void sendGift(Map<String ,String> params, Object tag, final DataCallback<String> scall){
        requst.postRequest(UrlApi.POST_GiFT_SENDGIFT(),params,tag,scall);
    }
    /**
     * 退群
     * @param tag
     * @param scall
     */
    public void quitGroup(Object tag, final DataCallback<String> scall){
        if(StringUtil.isEmpty(AppConfig.CustomerId)){
            return;
        }
        Map<String ,String> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.POST_LEAVE_TEMPGROUP(),params,tag,scall);
    }
    /**
     * 群发红包
     * @param tag
     * @param scall
     */
    public void sendGroupRedPacket(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(AppConfig.chatEnum== ChatEnum.defaults?UrlApi.POST_SENDBAG_TOGROUP():UrlApi.POST_SEND_BAGTOGROUP(),params,tag,scall);
    }
    /**
     * 群抢红包
     * @param tag
     * @param scall
     */
    public void bagGroupRedPacket(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(AppConfig.chatEnum==ChatEnum.defaults?UrlApi.POST_GIFT_FROMBAG():UrlApi.POST_GIFTFROMBAG(),params,tag,scall);
    }
    /**
     * 是否允许聊天
     * @param tag
     * @param scall
     */
    public void isGroupCaht(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.POST_IS_ALLOWCHAT(),params,tag,scall);
    }
    /**
     * 获取我对他的关系
     * @param tag
     * @param scall
     */
    public void getWeRelation(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.POST_WE_RELATION(),params,tag,scall);
    }
    /**
     * 自建群获取礼物详情
     * @param tag
     * @param scall
     */
    public void getBagInfo(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("my_customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.POST_SHOWBAGINFO(),params,tag,scall);
    }
    /**
     * 自建群抢礼物
     * @param tag
     * @param scall
     */
    public void getGiftFromBag(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.POST_GIFTFROMBAG(),params,tag,scall);
    }
    /**
     * 红包剩余数量
     * @param tag
     * @param scall
     */
    public void getGiftCountBag(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(AppConfig.chatEnum== ChatEnum.defaults?UrlApi.GET_FAMILY_CHAT_GROU_COUNT_BAG():UrlApi.GET_GIFT_COUNT_SHOWBAG(),params,tag,scall);
    }
    /**
     * 不在群里面同步群关系
     * @param tag
     * @param scall
     */
    public void postSynchroGroup(Map<String ,String> params,Object tag, final DataCallback<String> scall){
        params.put("customer_id",AppConfig.CustomerId);
        requst.postRequest(UrlApi.GET_GROUP_SYNCHRO_RELATION(),params,tag,scall);
    }

    public void getSendImMessage(Map<String, Object> params, Object tag, DataCallback<String> call) {
        requst.postRequst(UrlApi.GET_SENDIMMESSAGE(), params, tag, call);
    }
}
