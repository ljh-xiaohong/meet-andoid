package com.yuejian.meet.common;

import com.netease.nim.uikit.api.UrlApi;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.utils.StringUtils;

public interface Constants {
    /**
     * 每次获取20条数据
     */
    String pageItemCount = "20";
    /**
     * 客服qq
     */
    String userServiseQq = "3022476185";

    /**
     * 微信商户ID
     */
    String WX_APP_ID = "wx457b71ba948f85c0";
    /**
     * 应用签名
     */
    String APP_SIGN = "6b6ff70682ba627b03e0b95e5db9c08d";

    /**
     * 微信商户号
     */
    String WX_PARTNER_ID = "1486212932";

    /**
     * 加密字符串
     * 上版本后不能变更!
     */
    String NIMTOKEN = "RABLIVETK";

    String serverId = "66666";

    /**
     * 初始化Token
     */
    String TOKEN = "93c995bfa735516f1a";
    int REQUEST_CODE_SELECT_ALBUM = 10;
    int RESULT_CODE_SELECT_ALBUM = 11;
    int RESULT_CODE_SELECT_PICTURE = 8;
    int RESULT_CODE_DELETE_PICTURE = 14;
    int RESULT_CODE_BROWSER_PICTURE = 13;
    int RESULT_CODE_FIT_GIRL = 0x16;
    String POSITION = "position";
    int REQUEST_CODE_BROWSER_PICTURE = 12;
    String MsgIDs = "msgids";
    String PICPATHS = "picpaths";
    String OPEN_INSTALL_ID = "bmhy9f";
    String SHARE_CODE = "share_code";

    String ISJCHAT = "isjchat";
    String LOCAL_PATH = "local_path";
    int BEGINDOWNLOAD = 30;
    int COMPLETEDOWNLOAD = 31;
    int DOWNLOADINGPROGGRESS = 32;
    String FLAG_BIND_MOBILE = "2";
    String FLAG = "flag";
    String Mission_Mobile = "mission_mobile";


    int REQUEST_CODE_MSG = 17;
    String SELF_STATUS = "self_status";
    String PERSON_INFO = "person_info";
    String PHOTO_PATH = "photo_path";
    String ALBUM_LIST = "album_list";
    String CHANGE_INFO = "change_info";

    //好友关系常量
    interface RELATIONTYPE {
        String SEARCHTYPE = "searchType";
        String RELATIONTYPE = "relationType";
        int FANS = 3;
        int FOCUS = 1;
        int FRIENDS = 2;
        int BLACK = 4;
        int ALL = 0;
        int NEAR = 5;

    }

    String APP_KEY = "app_key";
    String TARGET_ID = "target_id";
    String TARGET_NAME = "target_name";
    String TARGET_PHOTO = "target_photo";
    String TARGET_SEX = "target_sex";
    String TARGET_AGE = "target_age";
    String IS_PAY = "is_pay";
    String MSG_TYPE = "msg_type";
    String MSG_CONTENT = "msg_content";
    String MSG_TIME = "msg_time";
    String TYPE_IMAGE = "type_image";
    String TYPE_VOICE = "type_voice";
    String TYPE_TXT = "type_txt";
    String MAX_SELECT = "max_select";
    String TARGET_ISMALE = "target_ismale";

    interface COMMONDIALOGTYPE {
        /**
         * 创建邀请帖子dialog提示框
         */
        String INVITATION_FEED_TYPE = "0";
        /**
         * 创建请求帖子dialog提示框
         */
        String REQUEST_FEED_TYPE = "1";
        /**
         * 两个单子在线
         */
        String REQUEST_TWO_FEED_TYPE = "5";
        /**
         * 发布两个单子在线
         */
        String REQUEST_TWO_FEED_SUBMIT_TYPE = "6";
        /**
         * 普通确认提示框
         */
        String COMMON_ALERT_DIALOG_TYPE = "2";
        /**
         * 普通充值提示框
         */
        String COMMON_RECHARGE_TYPE = "3";
        /**
         * 权限设置
         */
        String COMMON_PERMISSION_TYPE = "4";
        /**
         * 完善资料
         */
        String COMMON_PERFECT_DATA_TYPE = "7";
        /**
         * 修改资料
         */
        String COMMON_UPDATE_DATA_TYPE = "8";
        /**
         * 立即自拍认证
         */
        String goToAuthentication = "1001";
        /**
         * 重新自拍认证
         */
        String reAuthentication = "1002";
        /**
         * 审核通过
         */
        String COMMON_PASS_TYPE = "9";
        /**
         * 两次未接被下线
         */
        String COMMON_OFFLINE_TYPE = "10";
        /**
         * 审核不通过
         */
        String COMMON_UNPASS_TYPE = "11";
        /**
         * 照片审核不通过
         */
        String COMMON_UNPASSPHOTO_TYPE = "12";
        String COMMON_NIMTIPS_TYPE = "13";
        /**
         * 消费提示
         */
        String COMMON_NIMPAY_TYPE = "14";
        /**
         * 主页抢视频提示
         */
        String COMMON_VIDEO_NOTIFY = "15";
        /**
         * 重新获取位置
         */
        String COMMON_LOCATION_TYPE = "16";
    }

    interface FEEDS_TYPE {
        /**
         * 创建帖子
         */
        String CREATE_FEEDS_TYPE = "0";
        /**
         * 编辑帖子
         */
        String EDIT_FEEDS_TYPE = "1";
        /**
         * 搜索邀请贴
         */
        String SEARCH_INVITATION_FEEDS = "2";
        /**
         * 搜索请求贴
         */
        String SEARCH_REQUEST_FEEDS = "3";
    }

    interface DIRECT_SEEDING_TYPE {
        /**
         * 一对一直播采集端
         */
        int DIRECT_ONE_CAPTURE_TYPE = 0;
        /**
         * 一对一直播播放端
         */
        int DIRECT_ONE_RENDER_TYPE = 1;
        /**
         * 千里眼直播采集端
         */
        int DIRECT_EYE_CAPTURE_TYPE = 2;
        /**
         * 千里眼直播播放端
         */
        int DIRECT_EYE_RENDER_TYPE = 3;
    }

    interface EVALUATE_FEEDS_TYPE {
        /**
         * 邀请者帖子评价
         */
        int EVALUATE_FEED_INVITATION_TYPE = 0;
        /**
         * 抢看者帖子评价
         */
        int EVALUATE_FEED_RUSH_SEE_TYPE = 1;
        /**
         * 请求者帖子评价
         */
        int EVALUATE_FEED_REQUEST_TYPE = 2;
        /**
         * 抢单者帖子评价
         */
        int EVALUATE_FEED_RUSH_ORDER_TYPE = 3;
    }

    interface SHARE_TYPE {
        /**
         * 一对一邀请帖分享
         */
        String OTO_INVITATION_SHARE = "0";
        /**
         * 一对一请求贴分享
         */
        String EYE_REQUEST_SHARE = "1";
        /**
         * 活动分享
         */
        String GIFT_SHARE = "2";
    }

    interface SHARE_INTER_TYPE {
        /**
         * 个人主页进入
         */
        String HOME_PAGER_INTER = "0";
        /**
         * 发现页面进入
         */
        String DISCOVER_PAGER_INTER = "1";
    }

    interface PRAISE_RANK_TYPE {
        String CREDITS_RANK_TYPE = "0";
        String PEOPLE_RANK_TYPE = "1";
    }

    interface AVCHAT_KEYS {
        String KEY_IN_CALLING = "KEY_IN_CALLING";
        String KEY_ACCOUNT = "KEY_ACCOUNT";
        String KEY_CALL_TYPE = "KEY_CALL_TYPE";
        String KEY_SOURCE = "source";
        String KEY_CALL_CONFIG = "KEY_CALL_CONFIG";
        String KEY_PHOTO = "KEY_PHOTO";
        String KEY_POST_TITLE = "KEY_POST_TITLE";
        String KEY_TOTAL_WALLET = "KEY_TOTAL_WALLET";//订单ID
        String KEY_NETWORK_TYPE = "KEY_NETWORK_TYPE";//网络类型
        String KEY_INTENT_ACTION_AVCHAT = "KEY_INTENT_ACTION_AVCHAT";
        String KEY_POST_NAME = "KEY_POST_NAME";
        String KEY_POST_PRICE = "KEY_POST_PRICE";
        String KEY_CHAT_THEME = "KEY_CHAT_THEME";
    }

    public final static String REFRESH_USER_INFO = "refreshUserInfo";

    public final static String WX_PAY_RESULT_CODE_FAULT = "wx_pay_result_code_fault";


    int PORTRAIT_IMAGE_WIDTH = 720;
    int PORTRAIT_IMAGE_WIDTH_1280 = 1280;
    String KEY_CHAT_BACKGROUND_IMAGE = "key_chat_background_image";
    String KEY_VOICE_USE_EAR_CUP = "key_voice_use_ear_cup";
    String KEY_CUSTOMER_ID = "key_customer_id";
    String KEY_CUSTOMER_INFO = "key_customer_info";

    //1:关注;2:好友,3:拉黑,4:我的粉丝
    int GET_FOCUS = 1;
    int GET_FRIENDS = 2;
    int GET_BLACK_LIST = 3;
    int GET_FANS = 4;

    //1. 关注 2.取关 3.拉黑 4.取消拉黑
    int ADD_FOCUS = 1;
    int CANCEL_FOCUS = 2;
    int ADD_BLACK_LIST = 3;
    int CANCEL_BLACK_LIST = 4;

    String COMMEND_UPDATE_USER_INFO = "commend_update_user_info";

    String NOTIFICATION_TYPE = "notification_type";
    String NOTIFICATION_LIST = "notification_list";
    //新的粉丝
    String NOTIFICATION_FANS = "notification_fans";
    //视频评价
    String NOTIFICATION_VIDEO = "notification_video";
    //文章消息
    String NOTIFICATION_ESSAY = "notification_essay";
    //系统通知
    String NOTIFICATION_SYSTEM = "notification_system";
    //审核结果
    String NOTIFICATION_EXAMINE = "notification_examine";
    //举报详情
    String NOTIFICATION_REPORT = "notification_report";
    //族谱消息
    String NOTIFICATION_CLAN = "notification_clan";

    String URL = "url";
    String VIEW_TYPE = "view_type";
    String WEB_VIEW_TYPE = "web_view_type";
    String VIDEO_URL = "video_url";
    String NO_TITLE_BAR = "No_Title";
    String NEW_MESSAGE_NOTIFY_SWITCH = "new_message_notify_switch";
    String NEW_MESSAGE_CONTENT_SWITCH = "new_message_content_switch";
    String NEW_MESSAGE_VOICE_SWITCH = "new_message_voice_switch";
    String NEW_MESSAGE_VIBRATION_SWITCH = "new_message_vibration_switch";
    String HAVE_START_UP = "have_start_up";
    String HAVE_SHOW_UP_GUIDE = "have_show_up_guide";
    String ID = "id";

    String MALL_SWITCH = "market_switch";
    String UNDONE_SWITCH = "undone_switch";
    String STORE_URL = "http://shangyu.yuejianchina.com/yuejian/h5";
//        String STORE_URL = "http://shangyu.yuejianchina.com/yuejiantest/h5";
    String FIND_URL = UrlConstant.getWebUrl() + "faxian/index.html";
    String BAI_SHAN_JS = UrlConstant.getWebUrl() + "jz.html";
    public static final String PERSON_HOME = UrlConstant.getWebUrl() + "person_card/index.html?op_customer_id=" + (StringUtils.isNotEmpty(AppConfig.CustomerId) ? AppConfig.CustomerId : "");
}
