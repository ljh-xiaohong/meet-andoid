package com.netease.nim.uikit.api;

/**
 * 管理请求地址
 * Created by zh03 on 2017/7/4.
 */

public class UrlApi {
    //测试 http://192.168.1.103:8080/yuejian-app/


    //测试 http://192.168.1.7:8090/yuejian-app/

    //线上 https://app2.yuejianchina.com/yuejian-app/

//    商域测试 http://ha.yuejianchina.com/yuejian-app/

    //

//    public static String mHttpUrl = "https://app2.yuejianchina.com/yuejian-app/";
//  public static String mHttpUrl = "http://192.168.0.196:8089/yuejian_app_war/";
//  public static String mHttpUrl ="http://192.168.0.181:81/yuejian_app_war/";

  public static String mHttpUrl = "http://192.168.0.134:8087/yuejian-app/";
//  public static String mHttpUrl="http://192.168.0.188:8087/yuejian_app_war/";
//  public static String mHttpUrl = "http://192.168.0.178:81/yuejian-app/";

//  public static String mHttpUrl = "http://39.108.226.78:8080/yuejian-app/";
//    public static String mHttpUrl = "http://192.168.0.177:81/yuejian_app_war/";

    //  public static String mHttpUrl = "http://192.168.0.139:8087/yuejian-app/";
    public static String mWXhttpUrl = "http://wx.yuejianchina.com/";
//  public static String mHttpUrl = "http://192.168.0.145:8087/yuejian-app/";
    /**
     * 壹克 链接  测试版
     */

    public static String yiKeHttpUrl = "http://yj.18ek.com/app/";


    public static String GET_NEWS = mWXhttpUrl + "yuejian-minih5/weixin/genyuan.html";


    /**
     * 壹克 链接  测试版
     */
    //public static String yiKeCeShiHttpUrl = "http://yj.jixiancai.cn/app/";


    /**
     * 壹克 链接 正式版
     */
    // public static String yiKeHttpUrl = "http://yj.18ek.com/app/";


//    public static String mHttpUrl = "http://192.168.0.192:8080/yuejian-app/";
//    public static String mHttpUrl = "http://mpc.yuejianchina.com/yuejian-app/";
//    public static String mHttpUrl = "http://app2.yuejianchina.com/yuejian-app/";

    /**
     * 聊天请求扣费
     * customerId    自己的id
     * opCustomerId  对方的id
     * chatType '聊天类型（1:文字，2:语音，3:私照，4:小视频）
     *
     * @return
     */
    public static String GET_CONTROLLER_CHATEXPENSE() {
        return mHttpUrl + "api/im/chatExpense";
    }

    /**
     * 视频开始请求
     * customerId   自己的id
     * opCustomerId  对方的id
     * channelId   网易云id
     *
     * @return
     */
    public static String GET_CONTROLLER_ADDVIDEORECORD() {
        return mHttpUrl + "api/im/addVideoRecord";
    }

    /**
     * 视频结束请求
     * channelId   网易云id
     * customerId   自己的id
     *
     * @return
     */
    public static String GET_CONTROLLER_VIDEOENDCALL() {
        return mHttpUrl + "api/im/videoEndCall";
    }

    /**
     * 获取用户余额
     * customerId   自己的id
     *
     * @return
     */
    public static String GET_ACCOUNT_GETMYBAL() {
        return mHttpUrl + "api/customer/getMyWallet";
    }

    /**
     * 礼物扣费
     * customerId   自己的id
     * opCustomerId  对方的id
     * giftId          礼物id
     *
     * @return
     */
    public static String POST_GiFT_SENDGIFT() {
        return mHttpUrl + "api/gift/sendGift";
    }

    /**
     * 获取视频价格
     * customerId
     *
     * @return
     */
    public static String POST_POSTING_GETPOSTPRICE() {
        return mHttpUrl + "api/customer/isAllowVideo";
    }

    /**
     * 退群
     * customerId
     *
     * @return
     */
    public static String POST_LEAVE_TEMPGROUP() {
        return mHttpUrl + "api/familyChatGroup/leaveTempGroup";
    }

    /**
     * 群发红包
     * customerId
     *
     * @return
     */
    public static String POST_SENDBAG_TOGROUP() {
        return mHttpUrl + "api/giftBagChatGroup/sendBagToGroup";
    }

    /**
     * 抢红包
     * customerId
     *
     * @return
     */
    public static String POST_GIFT_FROMBAG() {
        return mHttpUrl + "api/giftBagChatGroup/getGiftFromBag";
    }

    /**
     * 是否允许聊天
     * customerId
     *
     * @return
     */
    public static String POST_IS_ALLOWCHAT() {
        return mHttpUrl + "api/familyChatGroup/isAllowChat";
    }

    /**
     * 获取我对他的关系
     * customerId
     *
     * @return
     */
    public static String POST_WE_RELATION() {
        return mHttpUrl + "api/friend/getWeRelation";
    }

    /**
     * 自建群发送礼物
     * customerId
     *
     * @return
     */
    public static String POST_SEND_BAGTOGROUP() {
        return mHttpUrl + "api/customChatGroup/sendBagToGroup";
    }

    /**
     * 自建群获取礼物详情
     * customerId
     *
     * @return
     */
    public static String POST_SHOWBAGINFO() {
        return mHttpUrl + "api/customChatGroup/showBagInfo";
    }

    /**
     * 自建群抢礼物
     * customerId
     *
     * @return
     */
    public static String POST_GIFTFROMBAG() {
        return mHttpUrl + "api/customChatGroup/getGiftFromBag";
    }

    /**
     * 自建群红色剩余数量
     * customerId
     *
     * @return
     */
    public static String GET_GIFT_COUNT_SHOWBAG() {
        return mHttpUrl + "api/customChatGroup/showBag";
    }

    /**
     * 家族群红色剩余数量
     * customerId
     *
     * @return
     */
    public static String GET_FAMILY_CHAT_GROU_COUNT_BAG() {
        return mHttpUrl + "api/familyChatGroup/showBag";
    }

    /**
     * 同步群成员和群的关系（我在群里，而发不出消息）
     * customerId
     *
     * @return
     */
    public static String GET_GROUP_SYNCHRO_RELATION() {
        return mHttpUrl + "api/customChatGroup/synchroRelation";
    }
}
