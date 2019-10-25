package com.yuejian.meet.api.http;


import com.netease.nim.uikit.api.UrlApi;

/**
 * 请求的URL
 *
 * @author lizhixin
 * @date 2016/4/15
 */


public class UrlConstant {


    public static String endpoint = "http://oss-cn-shenzhen.aliyuncs.com/";
    public static String accessKeyId = "LTAIGFRcss1RgkMF";
    public static String accessKeySecret = "4EWFKaVv6EFZZqkCiPVclq0tAqcTHs";
    public static String downloadObject = "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/";
    public static String bucket = "yuejian-app";//oss正式环境


    public static String apiUrl() {
        return UrlApi.mHttpUrl;
    }


    /**
     * 用户登录
     */
    public static String LOGIN() {
        return apiUrl() + "api/user/login";
    }


    /**
     * 根据手机号,绑定手机
     */
    public final static String POST_BANGDIN_MOBILE = apiUrl() + "api/user/bindingMobile";


    /**
     * 注册
     */
    public static String POST_REGISTER2() {
        return apiUrl() + "api/user/infosPersos";
    }

    //首页 动态列表
    public static String ATTENTIONFAMILYCRICLE = apiUrl() + "api/dynamic/getDynamicList";

    //首页 动态 发布
    public static String POSTCREATEACTION = apiUrl() + "api/contentRelease/pushContentRelease";

    //首页 动态 删除动态
    public static String POSTDELECTACTION = apiUrl() + "api/contentRelease/delContentRelease";

    //首页 动态 不感兴趣
    public static String POSTLOSEINTEREST = apiUrl() + "api/contentRelease/loseInterest";

    //首页 动态 举报
    public static String POSTDOREPORT = apiUrl() + "api/dynamic/doReport";

    //首页 动态 搜索
    public static String GETDOSEARCH = apiUrl() + "api/dynamic/doSearch";

    //分享链接
    public static String SHARE = apiUrl() + "release/blank.html";

    /**
     * 我的 - 传承人 - 收益列表
     * customer_id
     *
     * @return
     */
    public static String POST_INCOME_LIST() {
        return apiUrl() + "api/customer/getMyInheritorEarningsDetail";
    }

    /**
     * 商圈 - 需求 - 点赞
     * customer_id
     * demand_id
     *
     * @return
     */
    public static String POST_DEMAND_PRAISE() {

        return apiUrl() + "api/demand/giveALike";

    }

    /**
     * 家圈 - 点赞
     *
     * @return
     */
    public static String POST_DISCUSS_PRAISE() {
        return apiUrl() + "api/demand/giveALikeComment";
    }

    /**
     * 需求详情
     *
     * @return
     */
    public static String DEMANDDETAIL() {
        return apiUrl() + "release/details-of-requirements.html";
    }

    /**
     * 易赚宝
     *
     * @return
     */
    public static String yiZhuanBao() {
        return "http://shangyu.yuejianchina.com/yuejian/h5/yiZhuanBaoShop";
    }

    /**
     * 优品购
     *
     * @return
     */
    public static String youPinGou() {
        return "http://shangyu.yuejianchina.com/yuejian/h5/index";
    }

    /**
     * 获取热门社群
     */
    public static String GETCHATGROUP() {
        return apiUrl() + "api/customChatGroup/getTop5ChatGroup";
    }

    /**
     * VIP协议
     *
     * @return
     */
    public static String VIPACCEPCT() {
        return apiUrl() + "agreement/vip_membership.html";
    }

    /**
     * 获取热门名帖
     *
     * @return
     */
    public static String GETHOTTAG() {
        return apiUrl() + "api/customer/getTop10HotCard";
    }


    /**
     * 发送手机验证码
     */
    public static String SEND_CODE() {
        return apiUrl() + "api/user/sendSmsCode";
    }

    /**
     * 发送手机验证码
     */
    public static String SEND_CODE4() {
        return apiUrl() + "api/customer/sendSmsCode4";
    }


    /**
     * 获取姓接口
     */
    public static String GET_SURNAME_GETALL() {
        return apiUrl() + "api/surname/getAllSurname";
    }

    /**
     * 获取省
     */
    public static String GET_PROVINCE_GETALLPROVINCE() {
        return apiUrl() + "api/provinceCityArea/getAllProvince";
    }

    /**
     * 获取市
     */
    public static String GET_CIYT_GETALLCITY() {
        return apiUrl() + "api/provinceCityArea/getAllCity";
    }

    /**
     * 获取区
     */
    public static String GET_AREA_GETALLAREA() {
        return apiUrl() + "api/provinceCityArea/getAllArea";
    }

    /**
     * 注册
     */
    public static String POST_REGISTER() {
        return apiUrl() + "api/customer/register";
    }


    /**
     * 判断第三方登录是否注册
     */
    public static String POST_CUSTOMER_ISEXISTENCE() {
        return apiUrl() + "api/customer/checkOpenidIsExistence";
    }

    /**
     * 第三方登录
     */
    public static String POST_LOGIN_OTHER() {
        return apiUrl() + "api/customer/loginOther";
    }

    /**
     * 获取家族成员
     */
    public static String POST_MEMBER_GETFAMILYMEMBER() {
        return apiUrl() + "api/family/getFamilyMember";
    }

    //查询我的信息
    public static String POST_FIND_MY_INFO = apiUrl() + "api/customer/findMyInfo";
    public static String POST_SEND_SMS_CODE_TO_MODIFY_PHONE = apiUrl() + "api/customer/sendSmsCode";
    public static String POST_LOGOUT = apiUrl() + "api/customer/logout";
    //绑定手机
    public static String POST_BIND_MOBILE = apiUrl() + "api/customer/bindMobile";

    //绑定手机获取验证码
    public static String POST_BIND_MOBILE_GET_CODE = apiUrl() + "api/customer/sendSmsCode";

    //IdCard验证
    public static String POST_ID_CARD_VERIFY = apiUrl() + "api/certification/idCardCertification";

    //设置视频上线状态
    public static String POST_SET_SERVICE_STATUS = apiUrl() + "api/customer/setServiceStatus";

    //验证营业执照
    public static String POST_BUSINESS_CERTIFICATION = apiUrl() + "api/certification/businessCertification";

    //微信认证
    public static String POST_WX_CERTIFICATION = apiUrl() + "api/certification/wxCertification";

    //设置视频价格
    public static String POST_SET_VIDEO_PRICE = apiUrl() + "api/customer/setVideoPrice";

    public static String POST_FIND_CUSTOMER_INFO = apiUrl() + "api/customer/findCustomerInfo";

    public static String POST_SET_SEE_PERSON = apiUrl() + "api/actionPrivacy/setSeePerson";

    public static String POST_GET_PRIVACY = apiUrl() + "api/actionPrivacy/getPrivacy";

    public static String POST_GET_MY_ACTION_LIST = apiUrl() + "api/action/getMyActionList";
    //家族--动态
    public static String POST_ACTION_GETACTION = apiUrl() + "api/action/getActionList";
    //家族--发动态
    public static String POST_ACTION_CREATEACTION = apiUrl() + "api/action/createAction";
    //家族--动态点赞
    public static String POST_ACTION_PRAISE = apiUrl() + "api/action/praise";

    public static String POST_GET_RELATION_LIST = apiUrl() + "api/customerRelation/getRelationList";
    //关注用户
    public static String POST_BIND_RELATION = apiUrl() + "api/customerRelation/bindRelation";
    //传承人服务用户API
    public static String POST_UPDATESERVICE = apiUrl() + "api/msg/updateService";
    //获取单条动态信息
    public static String POST_ACTION_GETACTION_ONE = apiUrl() + "api/action/getAction";
    //获取动态评论记录
    public static String POST_ACTION_GETCOMMENTS = apiUrl() + "api/action/getComments";
    //获取单条动态评论记录
    public static String POST_ACTION_ONE_GETCOMMENT = apiUrl() + "api/action/getComment";

    public static String POST_UPDATE_CUSTOMER_INFO = apiUrl() + "api/customer/updateCustomerInfo";

    public static String GET_GET_DICT_DATA = apiUrl() + "api/customer/getDictData";
    //回复评论
    public static String GET_ACTION_REPLYCOMMENT = apiUrl() + "api/action/replyComment";
    //评论动态
    public static String GET_ACTION_COMMENT = apiUrl() + "api/action/comment";
    //点赞评论
    public static String GET_ACTION_COMMENTPRAISE = apiUrl() + "api/action/commentPraise";
    //获取单个人的单条所有评论
    public static String POST_ACTION_GETREPLYS = apiUrl() + "api/action/getReplys";

    public static String GET_GET_MY_WALLET = apiUrl() + "api/customer/getMyWallet";

    public static String POST_GET_MY_BILL = apiUrl() + "api/bill/getBill";

    public static String POST_GET_UNTYPE_MSG_COUNT = apiUrl() + "api/msg/getUnTypeMsgCount";

    public static String POST_GET_MY_UNREAD_LIST = apiUrl() + "api/msg/getMyUnReadList";
    //获取礼物
    public static String GET_GIFT_GETGIFTALL = apiUrl() + "api/gift/getGiftAll";
    //一对一发礼物
    public static String POST_GIFT_SENDGIFT = apiUrl() + "api/gift/sendGift";

    public static String POST_BIND_OUTCASH_PASSWORD = apiUrl() + "api/account/bindOutCashPassword";

    public static String POST_UPDATE_OUTCASH_PASSWORD = apiUrl() + "api/account/updateOutCashPassword";

    public static String POST_SEND_SMS_CODE_3 = apiUrl() + "api/customer/sendSmsCode3";

    public static String POST_CHECK_SMS_CODE_2 = apiUrl() + "api/customer/checkSmsCode2";

    public static String POST_CHECK_SMS_CODE_ = apiUrl() + "api/customer/checkSmsCode";

    /**
     * 获取家族所有的群列表
     */
    public static String POST_FAMESURNAME_GETGROUP_ALLNAME = apiUrl() + "api/family/sameSurnameCityFamilyList";
    /**
     * 获取家族城市群
     */
    public static String POST_FAMILY_SAMESURNAME_CICYGROPLIST = apiUrl() + "api/familyChatGroup/sameSurnameCityGroupList";

    public static String POST_IN_CASH = apiUrl() + "api/inCash/doInCash";
    public static String PCREATESHOPORDERPAY = apiUrl() + "api/shopOrder/createShopOrderPay";
    //充值贡献值API
    public static String INCONTRIBUTION = apiUrl() + "api/userPay/inContribution";
    //升级VIP API
    public static String UPGRADEVIP = apiUrl() + "api/userPay/upgradeVip";

    /**
     * 购买海报模板api
     */
    public static String BUY_POSTER_TEMPLATE = apiUrl() + "api/userPay/buyPosterTemplate";

    /**
     * 升级VIP API
     */
    public static String UPGRADE_VIP = apiUrl() + "api/userPay/upgradeVip";

    /**
     * 创建商品支付订单
     */
    public static String CREATE_SHOP_ORDER_PAY = apiUrl() + "api/shopOrder/createShopOrderPay";

    //开通VIP
    public static String POST_IN_CASH_VIP = apiUrl() + "api/inCash/doInCashVip";
    //获取VIP详情列表
    public static String POST_FIND_INTRODUCE_VO = apiUrl() + "/api/vipIntroduce/findIntroduceVo";

    public final static String POST_DO_IN_CASH_H5_ALIPAY = apiUrl() + "api/inCash/doInCashH5";

    public final static String POST_APPLY_OUT_CASH = apiUrl() + "api/outCash/applyOutCash";

    public final static String POST_MY_ARTICLE = apiUrl() + "api/article/myArticle";
    /**
     * 进群
     */
    public final static String POST_FAMILYCHAT_INTOGROUP = apiUrl() + "api/familyChatGroup/intoGroup";

    public final static String POST_GET_ARTICLE_LIST = apiUrl() + "api/article/getArticleList";

    public final static String POST_GET_ARTICLE_COMMENT_LIST = apiUrl() + "api/article/getComments";

    public final static String POST_ARTICLE_COMMENT = apiUrl() + "api/article/comment";

    public final static String POST_ARTICLE_REPLY_COMMENT = apiUrl() + "api/article/replyComment";

    public final static String POST_GET_ARTICLE = apiUrl() + "api/article/article";
    //非本聊天群内的全国同姓用户列表
    public final static String POST_NO_TEAM_SAMESURNAMELIST = apiUrl() + "api/familyChatGroup/nonThisGroupSameSurnameList";
    //邀请多人进群
    public final static String POST_INVITER_INTOGROUP = apiUrl() + "api/familyChatGroup/inviterIntoGroup";

    public final static String POST_GET_ARTICLE_COMMENT_REPLYS = apiUrl() + "api/article/getReplys";

    public final static String POST_GET_ARTICLE_COMMENT_PRAISE = apiUrl() + "api/article/commentPraise";

    public final static String POST_GET_ARTICLE_PRAISE = apiUrl() + "api/article/praise";

    public final static String POST_GET_REWARD_LIST = apiUrl() + "api/article/rewardList";
    //显示群成员信息
    public final static String POST_SHOW_MEMBERINFO = apiUrl() + "api/familyChatGroup/showMemberInfo";
    //群-禁言
    public final static String POST_FAMILYCHAT_GROUP_SILENCED = apiUrl() + "api/familyChatGroup/silenced";
    //群主——设置管理员
    public final static String POST_SET_GROUP_ADMIN = apiUrl() + "api/familyChatGroup/setGroupAdmin";
    //群主——成员信息
    public final static String POST_FAMILYCHAT_GETGROUPINFO = apiUrl() + "api/familyChatGroup/getGroupInfo";
    //群消息提醒设置
    public final static String POST_SWITC_NOTIFICATION = apiUrl() + "api/familyChatGroup/switchNotification";
    //指定类型所有群成员列表
    public final static String POST_ALL_MEMBER_LIST = apiUrl() + "api/familyChatGroup/allMemberList";
    //更新群公告
    public final static String POST_UPDATE_GROUP_NOTICE = apiUrl() + "api/familyChatGroup/updateGroupNotice";

    public final static String POST_CREATE_ARTICLE = apiUrl() + "api/article/createArticle";
    //视频评价
    public final static String POST_JUDGE_JUDGE = apiUrl() + "api/judge/judge";
    //我的视频记录
    public final static String POST_IM_GETMY_VIDEORECORD = apiUrl() + "api/im/getMyVideoRecord";
    //删除通话记录
    public final static String POST_IM_DELETE_VIDEORECORD = apiUrl() + "api/im/deleteVideoRecord";
    //视频通话记录详情
    public final static String POST_IM_GET_VIDEORECORD = apiUrl() + "api/im/getVideoRecord";

    public final static String POST_SET_PRIVACY_RANGE = apiUrl() + "api/actionPrivacy/setSeeRange";
    //更新用户坐标
    public final static String POST_INIT_CUSTOMER_POSITION = apiUrl() + "api/customer/initCustomerPosition";

    //通过网络获取当地地址
    public final static String GET_INIT_CUSTOMER_POSITION = apiUrl() + "api/coverPersonOrder/getAddressByIP";

    public final static String GET_GET_SURNAME_BY_SURNAME = apiUrl() + "api/surname/getSurnameBySurname";
    //获取城市代理人竞选列表
    public final static String POST_FAMILY_FAMILY_MASTER = apiUrl() + "api/family/getApplyFamilyMaster";
    //版本更新
    public final static String CHECK_VERSION_UPDATE = apiUrl() + "api/version/getLastVersion";
    //格力手机版本更新
    public final static String CHECK_VERSION_GREE_UPDATE = apiUrl() + "api/version/getGreeLastVersion";
    //动态未读所有的消息
    public final static String POST_GETUNRED_LIST = apiUrl() + "api/action/getUnreadList";

    public final static String POST_GET_ALL_MALL = apiUrl() + "api/android/getSwitch";
    //发送adk下载平台
    public final static String POST_INSERT_CHANNEL = apiUrl() + "api/downloadChannel/insertChannel";
    //群聊足迹
    public final static String POST_FOOTMRK_LIST = apiUrl() + "api/familyChatGroup/footmarkList";
    //查看动态打赏列表
    public final static String POST_ACTION_REWAR_LIST = apiUrl() + "api/action/rewardList";
    //删除动态
    public final static String POST_DELETE_ACTION = apiUrl() + "api/action/delAction";
    //根据地图传的省，市，区 返回 area_name
    public final static String GET_GETARENAME = apiUrl() + "api/provinceCityArea/getAreaName";
    //申请城市发起人
    public final static String APPLY_FAMILY_MASTER = apiUrl() + "api/family/applyFamilyMaster";

    public final static String SEARCH_CUSTOMER_BY_ID_OR_NAME = apiUrl() + "api/family/getFamilyMemberBySearch";

    public final static String GET_ACTION_PRAISE_LIST = apiUrl() + "api/action/praiseList";
    /**
     * 动态未读消息
     */
    public final static String GET_ACTION_UUREADREMInD = apiUrl() + "api/action/getUnreadRemind";
    /**
     * 获得可以加进群的好友列表
     */
    public final static String GET_CUSTIN_GROUP_FREIENDS = apiUrl() + "api/customChatGroup/getMyFriends";
    /**
     * 创建群聊
     */
    public final static String GET_FOUND_CHAT_GROUP = apiUrl() + "api/customChatGroup/foundChatGroup";
    /**
     * 获取创建的群
     */
    public final static String GET_CHAT_GROUP = apiUrl() + "api/customChatGroup/findMyChatGroups";
    /**
     * 获取创建的群成员详细信息
     */
    public final static String GET_GROUP_INFO = apiUrl() + "api/customChatGroup/showGroupInfo";
    /**
     * 自主创建  修改群名
     */
    public final static String POST_UPDATE_GROUPNAME = apiUrl() + "api/customChatGroup/updateGroupName";
    /**
     * 自主创建 群置顶
     */
    public final static String POST_UPDATE_GROUP_SETTOP = apiUrl() + "api/customChatGroup/setTop";
    /**
     * 退群  离群
     */
    public final static String POST_GROUP_OUTLIER_GROUP = apiUrl() + "api/customChatGroup/outlier";
    /**
     * 群主 踢人
     */
    public final static String POST_GROUP_KICK = apiUrl() + "api/customChatGroup/kick";
    /**
     * 邀请入群
     */
    public final static String POST_GROUP_INTOGROUP = apiUrl() + "api/customChatGroup/intoGroup";
    /**
     * 群主转让
     */
    public final static String POST_GROUP_ADBICATEGROUP = apiUrl() + "api/customChatGroup/abdicate";
    /**
     * 群消息提醒
     */
    public final static String POST_GROUP_UPDATE_NOTIFIACTION = apiUrl() + "api/customChatGroup/updateNotification";
    /**
     * 自主创建群 获取群所有成员列表
     */
    public final static String POST_GROUP_MEMBERLIST = apiUrl() + "api/customChatGroup/getMemberList";
    /**
     * 自主创建群 更新群公告
     */
    public final static String POST_GROUP_UPDATENOTECE = apiUrl() + "api/customChatGroup/updateNotice";
    /**
     * 保存电话簿
     */
    public final static String POST_CUSTOMER_SAVE_PHONEBOOK = apiUrl() + "api/customer/savePhoneBook";
    /**
     * 发现宗亲
     */
    public final static String POST_CUSTOMER_FINDCLAN = apiUrl() + "api/customer/findClan";
    /**
     * 批量关注
     */
    public final static String POST_ATTENTION_BATCH = apiUrl() + "api/customerRelation/attentionBatch";

    /**
     * 姓氏百科
     */
    public final static String POST_GET_OTHER_LIST = apiUrl() + "api/article/getOtherList";
    /**
     * 获取资料完善度
     */
    public final static String GET_FINDPERFACT_RATIO = apiUrl() + "api/customer/findPerfectRatio";
    /**
     * 删除评论动态
     */
    public final static String POST_ACTION_DEL_COMMENT = apiUrl() + "api/action/delComment";
    /**
     * 删除评论文章
     */
    public final static String POST_ARTICLE_DEL_COMMENT = apiUrl() + "api/article/delComment";

    public final static String POST_GET_COUPON_UN_READ = apiUrl() + "api/coupon/getUnreadCnt";

    public final static String POST_LOOK_ACTION_MESSAGE_LIST = apiUrl() + "api/action/lookMessageList";
    /**
     * 检查是否可以认领传承使者
     */
    public final static String POST_DEAL_CHECK_FAMILY_MASTER = apiUrl() + "api/deal/checkFamilyMaster";

    /**
     * 检查是否可以认领传承使者
     */
    public final static String GET_CLAN_TEAMID_CLANID = apiUrl() + "api/clanAssociation/findClanAssociationByWy_team_id";
    /**
     * 检查是否可以认领传承使者
     */
    public final static String POST_RELATION_FRIND = apiUrl() + "api/customerRelation/getRelationAndSurnameList";
    /**
     * 检查是否可以认领传承使者
     */
    public final static String POST_INVITE_CLAN_MEMBER = apiUrl() + "api/clanAssociation/inviteClanAssociationMember";
    /**
     * 检查是否可以认领传承使者
     */
    public final static String POST_RELEASE_CLAN_BULLETIN = apiUrl() + "api/clanAssociation/saveClanAssociationBulletin";
    /**
     * 检查是否可以认领传承使者
     */
    public final static String GET_SRARCH_GLOBAL = apiUrl() + "api/search/global";

    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_ASSOCIATION = apiUrl() + "api/clanAssociation/clanAssociation";
    /**
     * 获取宗亲会
     */
    public final static String POST_CLAN_FIND_APPROVA = apiUrl() + "api/clanAssociation/findApprova";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_FIND_APPROVA_PASS = apiUrl() + "api/clanAssociation/approvaPass";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_FIND_APPROVA_NO_PASS = apiUrl() + "api/clanAssociation/approvaNoPass";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_MEMBER = apiUrl() + "api/clanAssociation/findClanAssociationMember";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_MEMBER_REMOE = apiUrl() + "api/clanAssociation/deleteClanAssociationMember";
    /**
     * 获取宗亲会
     */
    public final static String POST_CLAN_MEMBER_CHARGE = apiUrl() + "api/clanAssociation/saveClanAssociationCharge";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_FIND_CHARGE = apiUrl() + "api/clanAssociation/findClanAssociationCharge";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_CHARGE_PAYINFO = apiUrl() + "api/clanAssociation/findClanAssociationPayInfo";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_PAYMENT = apiUrl() + "api/clanAssociation/payment";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_DELETE = apiUrl() + "api/clanAssociation/deleteClanAssociation";
    /**
     * 获取宗亲会
     */
    public final static String POST_UPDATE_CLANASSOCIATION = apiUrl() + "api/clanAssociation/updateClanAssociation";
    /**
     * 获取宗亲会
     */
    public final static String POST_UPLOADING_CLAN_PHOTO = apiUrl() + "api/clanAssociation/saveClanAssociationPhoto";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_PHOTO = apiUrl() + "api/clanAssociation/findClanAssociationPhoto";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_PHOTO_DELETE = apiUrl() + "api/clanAssociation/deleteClanAssociationPhoto";
    /**
     * 获取宗亲会
     */
    public final static String GET_CAREATE_CLAN_FIND_BULLETIN = apiUrl() + "api/clanAssociation/findClanAssociationBulletin";
//    /**
//     * 获取宗亲会
//     */
//    public final static String CLAN_HALL_TOP_RECORD = apiUrl() + "api/clanHall/topRecord";
    /**
     * 获取宗亲会
     */
    public final static String POST_FIND_CLAN_ASSOCIATION = apiUrl() + "api/clanAssociation/findClanAssociation";
    /**
     * 获取宗亲会
     */
    public final static String GET_CAREATE_CLAN_ASSCIATION_BYID = apiUrl() + "api/clanAssociation/findClanAssociationById";
    /**
     * 获取宗亲会
     */
    public final static String GET_CLAN_ADD_MEMBER = apiUrl() + "api/clanAssociation/saveClanAssociationMember";
    /**
     * 获取宗亲会
     */
    public final static String GET_CHECK_CREATER_ASSOCIATION = apiUrl() + "api/clanAssociation/checkCreateAssociation";
    /**
     * 获取宗亲会
     */
    public final static String GET_FIND_MYCLAN_ASSOCIATION = apiUrl() + "api/clanAssociation/findMyClanAssociation";
    /**
     * 获取宗亲会
     */
    public final static String POST_CAREATE_CLAN_ASSCIATION = apiUrl() + "api/clanAssociation/saveClanAssociation";

    /**
     * 祖词
     */
    public final static String POST_ZUCI_MY_COLLECTION = apiUrl() + "api/clanHall/myCollections";
//    /**
//     * 祖词
//     */
//    public final static String COLLECTION = apiUrl() + "api/collection/collection";
//    /**
//     * 祖词
//     */
//    public final static String POST_ZUCI_THUMB = apiUrl() + "api/clanHall/thumb";
    /**
     * 祖词
     */
    public final static String POST_ZUCI_DETAILED = apiUrl() + "api/clanHall/detailed";
    /**
     * 祖词
     */
    public final static String POST_ZUCI_FOOTPRINTS = apiUrl() + "api/clanHall/getFootprints";
    /**
     * 祖词
     */
    public final static String POST_ZUCI_NEARBY = apiUrl() + "api/clanHall/nearby";
    /**
     * 祖词
     */
    public final static String POST_COLLECTION_TOP_OR_DROP = apiUrl() + "api/clanHall/topOrDrop";
    /**
     * 祖词
     */
    public final static String POST_ZUCI_SAVE = apiUrl() + "api/clanHall/save";
    public final static String POST_CHECK_GROUP_TYPE = apiUrl() + "api/chatGroupUtil/checkGroupType";
    public final static String GET_CLAN_ISCLANASSOCIAION = apiUrl() + "api/clanAssociation/getClanAssociationByIdAndExists";
    /**
     * 获取新闻动态
     */
    public final static String POST_NEW_ACTION = apiUrl() + "api/newsAction/getLastNewsAction";
    /**
     * 获取新闻动态列表
     */
    public final static String POST_NEW_ACTION_LIST = apiUrl() + "api/newsAction/newsActionList";
    /**
     * 新增用户反馈
     */
    public final static String POST_SAVE_CUSTOMER_FEEDACK = apiUrl() + "api/customerFeedback/saveCustomerFeedback";
    /**
     * 获取用户反馈列表
     */
    public final static String GET_HISTORY_FEEDBACK = apiUrl() + "api/customerFeedback/findCustomerFeedback";
    /**
     * 获取用户反馈详情
     */
    public final static String GET_HISTORY_FEEDBACK_INFO = apiUrl() + "api/customerFeedback/findCustomerFeedbackInfo";
    /**
     * 提交问题已解决
     */
    public final static String POST_FEEDBACK_SOLVING = apiUrl() + "api/customerFeedback/customerFeedbackSolving";
    /**
     * 祖祠查询宗亲会
     */
    public final static String GET_HALLFIND_CLAN_ASSOCIATiON = apiUrl() + "api/clanAssociation/hallFindClanAssociation";
    /**
     * 查询指定姓氏的总人数
     */
    public final static String GET_CUSTOMER_COUNTBY_SURNAME = apiUrl() + "api/customer/getCustomerCountBySurname";
    /**
     * 检查微信openId是否已注册
     */
    public final static String GET_CUSTOMEr_CHECK_WEIXIN_OPENID = apiUrl() + "api/customer/checkWeixinOpenId";

    /**
     * 修改用户所属家族
     */
    public final static String POST_UPDATE_FAMILY = apiUrl() + "api/customer/updateFamily";
    /**
     * 发起宗享会会议
     */
    public final static String POST_SAVEENJOY = apiUrl() + "api/clanEnjoyAssociation/saveClanEnjoyAssociationMeeting";
    /**
     * 查询宗享会会议动态
     */
    public final static String POST_GETENJOY = apiUrl() + "api/clanEnjoyAssociation/findClanEnjoyAssociationMeetingAction";
    /**
     * 查询宗享会项目
     */
    public final static String GET_BOT_ALL = apiUrl() + "api/clanEnjoyAssociation/findClanEnjoyAssociationProject";
    /**
     * 查询宗享会项目详情(我的)
     */
    public final static String GET_My_BOT_INFO = apiUrl() + "api/clanEnjoyAssociation/getClanEnjoyAssociationProjectInfo";
    /**
     * 查询我的宗享会项目
     */
    public final static String GET_BOT_TA_BOT_INFO = apiUrl() + "api/clanEnjoyAssociation/findClanEnjoyAssociationProjectByCustomerID";
    /**
     * 删除宗享会项目
     */
    public final static String POST_DEL_BOT = apiUrl() + "api/clanEnjoyAssociation/deleteClanEnjoyAssociationProjectByID";
    /**
     * 发起宗享会项目
     */
    public final static String POST_ADD_BOT = apiUrl() + "api/clanEnjoyAssociation/saveClanEnjoyAssociationProject";
    /**
     * 编辑宗享会项目
     */
    public final static String POST_UPDATE_BOT = apiUrl() + "api/clanEnjoyAssociation/updateClanEnjoyAssociationProjectByID";
    /**
     * 获取标签
     */
    public final static String POST_GET_TAG = apiUrl() + "api/tag/getTag";
    /**
     * 查询所有朋友，亲戚关系类型
     */
    public final static String POST_FRIEND_TREE = apiUrl() + "api/friend/getFrindEum";
    /**
     * 短信邀请好友
     */
    public final static String POST_INVITE_FRIEND = apiUrl() + "api/customer/inviteFriend";
    /**
     * 获得朋友关系申请
     */
    public final static String POST_FRIEND_GETREQUES_FRIEND = apiUrl() + "api/friend/getRequestFriend";
    /**
     * 朋友关系申请通过或忽略
     */
    public final static String POST_APPLY_REQUEST = apiUrl() + "api/friend/ApplyRequestFriend";
    /**
     * 获得某关系的对应关系
     */
    public final static String POST_OP_FRIEND_NAME = apiUrl() + "api/friend/getOp_friend_name";
    /**
     * 提交朋友关系申请
     */
    public final static String POST_REQEST_FRIEND = apiUrl() + "api/friend/requestFriend";
    /**
     * 获得我的朋友关系
     */
    public final static String POST_GET_MYFRIEND = apiUrl() + "api/friend/getMyFriend";
    /**
     * 获得我的黑名单
     */
    public final static String POST_BLACK_LIST = apiUrl() + "api/friend/getMyBlackList";
    /**
     * 黑名单操作
     */
    public final static String POST_DO_BLACK = apiUrl() + "api/friend/doBlack";
    /**
     * 删除关系
     */
    public final static String POST_DEL_FRIEND = apiUrl() + "api/friend/delFriend";
    /**
     * 你可能认识的人
     */
    public final static String POST_MAY_KNOWLIST = apiUrl() + "api/friend/mayKnowList";
    /**
     * 修改关系
     */
    public final static String POST_UPDATE_FRIEND = apiUrl() + "api/friend/updateFriend";
    /**
     * 通过邀请码获取用户信息
     */
    public final static String POST_GET_INFORMATION = apiUrl() + "api/friend/getInformationByInviteCode";
    /**
     * 获得申请个数
     */
    public final static String POST_GET_REQUESTCOUNT = apiUrl() + "api/friend/getRequestCount";
    /**
     * 小程序json数据
     */
    public final static String POST_MINI_JSON = apiUrl() + "mini/mini.json";
    /**
     * 我的v3
     */
    public final static String GET_FINDMY_DATA = apiUrl() + "api/customer/v3/findMy";
    /**
     * 我的v4
     */
    public final static String FIND_MY_FOUR_VERSION = apiUrl() + "api/customer/findMyFourVersion";
    /**
     * 查询用户未读动态数量
     */
    public final static String POST_ACTION_READACTION_NUM = apiUrl() + "api/action/getUnReadActionNum";
    /**
     * 我的访客(版本:3)
     */
    public final static String GET_FIND_VISITINFO = apiUrl() + "api/customer/v3/findCustomerVisitInfo";
    /**
     * 用户详细信息(版本:3)
     */
    public final static String GET_FIND_CustomerInfo = apiUrl() + "api/customer/v3/findCustomerInfo";
    /**
     * 用户详细信息(版本:3)
     */
    public final static String GET_FIND_CustomerBaseInfo = apiUrl() + "api/customer/v3/findCustomerBaseInfo";
    /**
     * 更新用户信息(版本:3)
     */
    public final static String POST_UPDATA_CUSTOMER_INFO = apiUrl() + "api/customer/v3/updateCustomerInfo";

    /**
     * 查询我的修行功德数
     */
    public final static String POST_MERITS = getWebUrl() + "api/practice/getPractice";

    public static String getWebUrl() {
        return apiUrl().replace("https", "http");
    }


  /*public static String getYikeWebUrl() {
    return UrlApi.yiKeHttpUrl;
  }*/

    public static String getYikeCeshiWebUrl() {
        return UrlApi.yiKeHttpUrl;
    }


    public static class ExplainURL {
        //        分享二维码链接-------- share.html
        //        城市发起人认领说明---- familyMaster.html
        //        约见群聊准则-----chatGroup.html
        //        用户充值协议----inCash.html
        //        用户提现协议----outCash.html
        //        用户使用协议----userGuide.html
        public static final String STAR_ZPU = getWebUrl() + "star_zupu/index.html";
        public static final String QRCODE_SHARE = getWebUrl() + "person/index.html";//个人主页分享链接
        public static final String QRCODE_SHARE2 = getWebUrl() + "share2.html";//邀请好友分享链接
        public static final String QRCODE_SHARE3 = getWebUrl() + "share3.html";//邀请城市发起人
        public static final String FAMILYMASTER = getWebUrl() + "familyMaster.html";
        public static final String CHATGROUP = getWebUrl() + "chatGroup.html";
        public static final String INCASH = getWebUrl() + "inCash.html";
        public static final String OUTCASH = getWebUrl() + "outCash.html";
        public static final String USERGUIDE = getWebUrl() + "userGuide.html";
        public static final String FAXIAN = getWebUrl() + "faxian/game_worship.html";
        public static final String FAXIAN_SCHOOL = getWebUrl() + "faxian/school.html";
        public static final String FIND_XIUXING = getWebUrl() + "faxian/xiuxing.html";

        /**
         * 开通VIP
         */
        public static final String OPEN_VIP = getWebUrl() + "personal_center/shop/pages/vip/vip.html";

        /**
         * 商品详情
         */
        public static final String SHOP_DETAIL = getWebUrl() + "personal_center/shop/item.html";

        /**
         * 个人主页（非VIP）
         */
        public static final String PERSON_INFORMATION_UNVIP = getWebUrl() + "personal_center/userHome3.html";

        /**
         * 个人主页（VIP）
         */
        public static final String PERSON_INFORMATION_VIP = getWebUrl() + "personal_center/personHome2.html";

        /**
         * 发现 优选商城 亿课商城   正式地址getYikeWebUrl()
         */
        public static final String YIKE_SHANGCHENG = getYikeCeshiWebUrl() + "shop/home";

        /**
         * 我的 推广 customerId	int	是	会员ID
         */
        public static final String YIKE_TUIGUANG = getYikeCeshiWebUrl() + "link/generalize";

        /**
         * 我的 待付款 待发货 待收货 待评价
         * customerId	int	是	会员ID
         * tabFlag	Int	是	标识(见5.5.订单标识码)
         */
        public static final String YIKE_SHOUHUO = getYikeCeshiWebUrl() + "link/orderCenter";


        /**
         * 我的 我的钱包 customerId	int	是	会员ID
         */
        public static final String YIKE_QIANBAO = getYikeCeshiWebUrl() + "link/wallet";
        /**
         * 功德地址 customerId	int	是	会员ID
         */
        public static final String YIKE_GONDE = getYikeCeshiWebUrl() + "link/bonusPonits";
        /**
         * 福缘 customerId	int	是	会员ID
         */
        public static final String YIKE_FUYUAN = getYikeCeshiWebUrl() + "link/fate";
        /**
         * 金元 customerId	int	是	会员ID
         */
        public static final String YIKE_JINYUAN = getYikeCeshiWebUrl() + "link/gold";
        /**
         * 会员权益 customerId	int	是	会员ID
         */
        public static final String
                YIKE_HUIYUAN = getYikeCeshiWebUrl() + "link/memberBenefits";


        //用户隐私政策
        public static final String PRIVACY = getWebUrl() + "privacy.html";

//        认领要求---apply_require.html
//        认领价值---apply_worth.html
//        发起特权---apply_power.html
//        认领须知--apply_notice.html

        public static final String APPLY_REQUIRE = getWebUrl() + "apply_require.html";
        public static final String APPLY_WORTH = getWebUrl() + "apply_worth.html";
        public static final String APPLY_POWER = getWebUrl() + "apply_power.html";
        public static final String APPLY_NOTICE = getWebUrl() + "apply_notice.html";

//        钱包类问题----  wallet/bill_question.html
//        支付类密码问题 --- wallet/pay_password_question.html
//        转入/转出失败类问题 ---wallet/inCash_OutCash_question.html
//        钱包转出类规则说明--/wallet/wallet-out-cash_question.html

        public static final String BILL_QUESTION = getWebUrl() + "wallet/bill_question.html"; // 钱包类问题
        public static final String PAY_PASSWORD_QUESTION = getWebUrl() + "wallet/pay_password_question.html"; //支付类密码问题
        public static final String INCASH_OUTCASH_QUESTION = getWebUrl() + "wallet/inCash_OutCash_question.html";//转入/转出失败类问题
        public static final String WALLET_OUT_CASH_QUESTION = getWebUrl() + "wallet/wallet_out_cash_question.html"; //钱包转出类规则说明

//        充值钱包余额----- wallet/inCash.html
//        转出钱包余额---- wallet/outCash.html

        public static final String INCASH_RULE = getWebUrl() + "wallet/inCash.html";
        public static final String OUTCASH_RULE = getWebUrl() + "wallet/outCash.html";
        //认领发起人合同
        public static final String CONTRACT = apiUrl() + "api/contract/getContract";


        /**
         * 撞钟
         */
        public static final String ZHUANG_ZHONG = getWebUrl() + "faxian/game_zhuangzhong.html";

        /**
         * 放生
         */
        public static final String FANG_SHENG = getWebUrl() + "faxian/game_release.html";


        /**
         * 点灯
         */
        public static final String DIAN_DENG = getWebUrl() + "faxian/game_diandeng.html";
    }

    //1.3.4

    public static final String Find_All_member_list = apiUrl() + "api/familyChatGroup/allMemberList";

    public static final String GET_KF_LIST = apiUrl() + "api/customer/getKfList";

    public static final String GET_USER_RANK = apiUrl() + "api/customer/findRank";

    public static final String POST_GET_ACTION_PHOTO = apiUrl() + "api/action/getActionPhoto";

    public static final String COUPON_URL = getWebUrl() + "coupon/index.html";


    public static String FIND_AV = apiUrl() + "api/av/findAv";
    public static String FIND_AV_COMMENT_BY_ID = apiUrl() + "api/av/findAvCommentByAv_id";
    public static String FIND_BANNER = apiUrl() + "api/banner/findBanner";
    public static String FIND_NEAR_PERSON = apiUrl() + "api/customer/randomSearch";
    public static String PUT_IN_BASKET = apiUrl() + "api/giftBasket/putInBasket";
    public static String LUCK_DRAW = apiUrl() + "api/giftBasket/luckDraw";
    public static String MY_RELATIVES = apiUrl() + "api/relative/myRelatives";
    public static String FIND_CUSTROMER_SIGN_IN = apiUrl() + "api/signin/findCustromerSignin";
    public static String CHECK_CUSTOMER_SIGNIN = apiUrl() + "api/signin/checkCustomerSignin";
    public static String FIND_SIGN_IN_RANKING = apiUrl() + "api/signin/findSigninRanking";
    public static String CUSTOMER_SIGN_IN = apiUrl() + "api/signin/customerSignin";
    public static String COMMENT_AV = apiUrl() + "api/av/comment";
    public static String REPLY_COMMENT = apiUrl() + "api/av/replyComment";
    public static String AV_PRAISE = apiUrl() + "api/av/praise";
    public static String COLLECTION = apiUrl() + "api/collection/collection";
    public static String FIND_AV_BY_ID = apiUrl() + "api/av/findAvById";
    public static String ADD_RELATIVE = apiUrl() + "api/relative/add";
    public static String BIND_RELATION = apiUrl() + "api/relative/bindRelation";
    public static String DELETE_RELATIVE = apiUrl() + "api/relative/delete";
    public static String INVITE_RANKING = apiUrl() + "api/customer/inviteRanking";
    public static String INVITE_RANKING_MY_RANK = apiUrl() + "api/customer/inviteRankingMyRank";
    public static String DELETE_AV_COMMENT = apiUrl() + "api/av/deleteComment";
    public static String DELETE_AV_REPLY_COMMENT = apiUrl() + "api/av/deleteReplyComment";
    public static String GET_MESSAGE_FOR_BOARD = apiUrl() + "api/clanHall/getComments";
    public static String INVITE_RANKING_MY_INVITE_LIST = apiUrl() + "api/customer/inviteRankingMyInviteList";
    public static String ADD_CLAN_HALL_COMMENT = apiUrl() + "api/clanHall/addComment";
    public static String CLAN_HALL_TOP_RECORD = apiUrl() + "api/clanHall/topRecord";
    public static String COMMENT_PRAISE = apiUrl() + "api/av/commentPraise";
    public static String POST_ZUCI_THUMB = apiUrl() + "api/clanHall/thumb";

    /**
     * 首页 家族 获取数据
     */
    public static String GET_FAMLIY_DATA = apiUrl() + "api/surname/getSurnameBySurname";
    /**
     * 姓氏排行
     */
    public static String FIND_SURNAME_RANK = apiUrl() + "api/surname/findSurnameRank";

    /**
     * 任务奖励
     */
    public static String QUEST_REWARD = apiUrl() + "api/task/findCustomerTask";
    /**
     * 任务奖励 领取
     */
    public static String POST_QUEST_REWARD = apiUrl() + "api/task/getTaskRewards";
    /**
     * 个人 功德排行
     */
    public static String MERIT_RANK = apiUrl() + "api/practice/praticeTop10";

    /**
     * 姓氏 功德排行
     */
    public static String SURNAME_RANK = apiUrl() + "api/surname/getPracticeBySurname";
    /**
     * 个人 功德排行 的背景图片
     */
    public static String RANKPIC = apiUrl() + "api/rankpic/getPic";


    public static String SURNAME_ORIGIN = apiUrl() + "api/family/getFamilyMemberv2";

    // 发布随笔 获取Tag列表
    public static String POST_FIND_LABEL = apiUrl() + "api/Essay/findLabel";

    // 发布随笔 发布
    public static String POST_INSERT_ESSAY = apiUrl() + "api/Essay/insertEssay";

    // 发布文章 发布
    public static String PUBLISHED_ARTICLES = apiUrl() + "api/newArticle/publishedArticles";

    public static String PUBLISHED_ARTICLES_NEW = apiUrl() + "api/contentRelease/pushContentRelease";

    //获取VIP配置商品列表API
    public static String GET_VIP_SHOP_GOODS_LIST = apiUrl() + "api/shopGoods/getVipShopGoodsList";

    //草稿箱列表
    public static String GET_DRAFTS_LIST = apiUrl() + "api/contentRelease/getDraftsList";

    //内容（动态，视频，文章）点赞(测试通过)
    public static String PRAISE_CONTENT = apiUrl() + "api/contentRelease/praiseContent";

    //用户添加取消关注活动标签接口
    public static String ADD_CONTENT_LABEL_CUSTOMER = apiUrl() + "api/contentLabelCustomer/addContentLabelCustomer";

    //获取活动标签下的视频文章内容
    public static String FIND_CONTENT_BY_LABEL = apiUrl() + "api/contentLabelRecord/findContentByLabel";

    public static String CONTENT_DETAILS = apiUrl() + "api/contentRelease/getContentDetails";

    // 收藏内容
    public static String DO_COLLECTION = apiUrl() + "api/collection/doCollection";

    // 发布视频 发布
    public static String PUBLISHED_VIDEO = apiUrl() + "api/video/publishedVideo";

    // 首页 推荐
    public static String RECOMMEND_FAMILY_CRICLE = apiUrl() + "api/familyCircle/getRecommendFamilyCricle.do";

    // 首页推荐 RSV 1.0
    public static String RECOMMEND_FIND = apiUrl() + "api/homePage/findRecommend";

    //个人中心 - 获取用户基本信息API（测试通过）
    public static String FIND_CUSTOMER_BASE_INFO = apiUrl() + "api/customer/findCustomerBaseInfo";

    public static String MY_CREAT_CONTENT_LIST = apiUrl() + "api/contentRelease/getContentList";

    //获取海报模板基础资源详情API
    public static String FIND_POSTERS_MODEL_ID = apiUrl() + "api/appPostersModel/findPostersModelById";

    //推荐标签
    public static String POSTERMODEL_LIST = apiUrl() + "api/appPostersModel/queryList.do";

    //海报ID标签
    public static String POSTERMODEL_LABEL = apiUrl() + "api/appPostersModel/findPostersModelByLabelId";

    //活动标签获取
    public static String CONTENTLABEL = apiUrl() + "api/contentRelease/getContentLabel";

    //首页 同城
    public static String ICITYFAMILYCRICLE = apiUrl() + "api/familyCircle/getIcityFamilyCricle.do";

    public static String RECOMMEND_LIFE = apiUrl() + "api/contentRelease/getLiveList";

    //首页 内容详情
    public static String CONTENTRELEASE = apiUrl() + "api/contentRelease/getContentRelease";
    //我的 - 我的创作
    public static String MINEFAMILYCRICLEDO = apiUrl() + "api/familyCircle/getMineFamilyCricle.do";
    //商圈 - 活动
    public static String ACTIVITYLISTDO = apiUrl() + "api/activity/getActivityList.do";
    //商圈 - 活动 - 参与活动
    public static String JOINACTIVITYDO = apiUrl() + "api/activity/joinActivity.do";
    //商圈 - 广场 - 封面人物列表
    public static String COVERPERSONLISTDO = apiUrl() + "api/coverPerson/getCoverPersonList.do";
    //商圈 - 广场 - 申请成为封面人物
    public static String DORANKEDDO = apiUrl() + "api/coverPerson/doRanked.do";
    //商圈 - 广场 - 申请成为封面人物（再次）
    public static String DORANKEDDO_AGAIN = apiUrl() + "api/coverPerson/doNextPayRank.do";
    //商圈 - 广场 - 支付成为封面人物金额
    public static String DOPAYRANKDO = apiUrl() + "api/coverPerson/doPayRank.do";
    //商圈 - 需求
    public static String DEMANDLIST = apiUrl() + "api/demand/getDemandList";

    //钱包 - VIP收益
    public static String MYINHERITOREARNINGS = apiUrl() + "api/customer/getMyInheritorEarnings";
    //申请成为传承人
    public static String APPLYFORINHERITORDO = apiUrl() + "api/inheritor/applyForInheritor.do";
    //点赞
    public static String PRAISEARTICLES = apiUrl() + "api/contentRelease/praiseContent";
    //评论
    public static String CONTENTCOMENT = apiUrl() + "api/contentComment/contentComment";
    //回复评论
    public static String REPLYCOMENT = apiUrl() + "api/contentComment/replyContentComment";
    //获取评论列表
    public static String GET_CONTENT_COMMENTS = apiUrl() + "api/contentComment/getContentComments";
    //api/homePage/findSlideIds
    public static String FINDSLIDEIDS=apiUrl()+"api/homePage/findSlideIds";





    //获取消息通知列表
    public static String GET_GETMESSAGELIST = apiUrl() + "api/msg/getMessageList";
    //新朋友
    public static String GET_GETATTENTIONANDFRIEND = apiUrl() + "api/customerRelation/getAttentionAndFriend";
    //获取用户收到的评论和赞列表
    public static String GET_GETCOMMENTPRAISELIST = apiUrl() + "api/contentComment/getCommentPraiseList";
    //获取用户收到的评论和赞列表
    public static String GET_GETCOMMENTANDPRAISE = apiUrl() + "api/contentComment/getCommentAndPraise";
    //获取用户服务信息
    public static String GET_GETUSRSERVICEINFO = apiUrl() + "api/msg/getUsrServiceInfo";
    //设置用户服务信息
    public static String GET_MEESSAGESETTING = apiUrl() + "api/msg/meessageSetting";
    //获取某省的所有市
    public static String GET_ACQUIRECITY = apiUrl() + "api/provinceCityArea/acquireCity";
    //通讯录
    public static String GET_GETRELATION = apiUrl() + "api/customerRelation/getRelation";
    //获取百家秘书服务用户列表API
    public static String GET_GETBAIJIASERVICE = apiUrl() + "api/msg/getBaiJiaService";
    //百家秘书运营数据图
    public static String GET_GETBAIJIASOURCE = apiUrl() + "api/customerRelation/getBaiJiaSource";
    //推送用户列表
    public static String GET_GETPUSHLIST = apiUrl() + "api/msg/getPushList";
    //推送用户列表
    public static String GET_GETPUSHPROJECT = apiUrl() + "api/msg/getPushProject";
    //获取精准推送商品列表API
    public static String GET_GETTHREEPROJECT = apiUrl() + "api/msg/getThreeProject";
    //获取消息通知详细
    public static String GET_GETMESSAGEDETAIL = apiUrl() + "api/msg/getMessageDetail";
    //删除内容下评论
    public static String GET_GETDELCONTENTCOMMENT = apiUrl() + "api/contentComment/delContentComment";
    //消息删除
    public static String GET_GETDELMESSAGE = apiUrl() + "api/msg/delMessage";
    //获取新消息通知标识API
    public static String GET_GETMESSAGE = apiUrl() + "api/msg/getMessage";
    //设置通知消息全部为已读API
    public static String GET_DOSETTINGREAD = apiUrl() + "api/msg/doSettingRead";



}


