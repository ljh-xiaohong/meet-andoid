package com.yuejian.meet.api.http;

import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.db.HttpRequstDB;
import com.yuejian.meet.api.db.Logindb;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/9.
 */

public class ApiImp {
    Logindb logindb = new Logindb();
    HttpRequstDB mHttpRequst = new HttpRequstDB();

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     * @param tag
     * @param call
     */
    public void getCode(String areaCode, String mobile, Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        params.put("areaCode", areaCode);
        params.put("mobile", mobile);
        mHttpRequst.postRequst(UrlConstant.SEND_CODE(), params, tag, call);
    }

    /**
     * 登录接口
     *
     * @param params
     * @param tag
     * @param call
     */
    public void login(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        logindb.postRequst(UrlConstant.LOGIN(), params, tag, call);
    }

    /**
     * 根据手机号,绑定手机
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void bangdingMobile(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_BANGDIN_MOBILE, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 完善资料
     *
     * @param tag
     * @param call
     */
    public void postRegister2(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        logindb.postRequst(UrlConstant.POST_REGISTER2(), params, tag, call);
    }

    /**
     * 首页 动态
     */
    public void getAttentionFamilyCricleDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.ATTENTIONFAMILYCRICLE, paramMap, paramObject, callback);
    }

    /**
     * 首页 动态 发布动态
     */
    public void postCreateAction(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTCREATEACTION, paramMap, paramObject, callback);
    }

    /**
     * 首页 动态 删除动态
     */
    public void postDelectAction(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTDELECTACTION, paramMap, paramObject, callback);
    }

    /**
     * 首页 动态 不感兴趣
     */
    public void postLoseInterest(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTLOSEINTEREST, paramMap, paramObject, callback);
    }

    /**
     * 首页 动态 举报
     */
    public void postDoReport(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTDOREPORT, paramMap, paramObject, callback);
    }

    /**
     * 首页 动态 搜索
     */
    public void getDoSearch(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GETDOSEARCH, paramMap, paramObject, callback);
    }


    /**
     * 第三方是否注册
     *
     * @param tag
     * @param call
     */
    public void isRegister(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        logindb.getRequst(UrlConstant.POST_CUSTOMER_ISEXISTENCE(), params, tag, call);
    }

    /**
     * 第三方登录
     *
     * @param tag
     * @param call
     */
    public void loginother(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        logindb.postRequst(UrlConstant.POST_LOGIN_OTHER(), params, tag, call);
    }

    /**
     * 收益列表
     *
     * @param params
     * @param tag
     * @param call
     */
    public void doIncomeList(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        logindb.postRequst(UrlConstant.POST_INCOME_LIST(), params, tag, call);
    }

    /**
     * 获取姓
     *
     * @param tag
     * @param call
     */
    public void getSurname(Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        mHttpRequst.getRequst(UrlConstant.GET_SURNAME_GETALL(), params, tag, call);
    }

    /**
     * 省
     *
     * @param tag
     * @param call
     */
    public void getProvince(Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        mHttpRequst.getRequst(UrlConstant.GET_PROVINCE_GETALLPROVINCE(), params, tag, call);
    }

    /**
     * 市
     *
     * @param tag
     * @param call
     */
    public void getCity(String provinceName, Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        params.put("province", provinceName);
        mHttpRequst.getRequst(UrlConstant.GET_CIYT_GETALLCITY(), params, tag, call);
    }

    /**
     * 区
     *
     * @param tag
     * @param call
     */
    public void getArea(String cityName, Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        params.put("city", cityName);
        mHttpRequst.getRequst(UrlConstant.GET_AREA_GETALLAREA(), params, tag, call);
    }

    /**
     * 获取家族成员
     *
     * @param tag
     * @param call
     */
    public void getMember(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_MEMBER_GETFAMILYMEMBER(), params, tag, call);
    }


    /**
     * 查询我的信息
     */
    public void findMyInfo(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_FIND_MY_INFO, params, tag, call);
    }

    //获取验证码更改手机号
    public void sendSmsCodeToModifyPhone(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_SEND_SMS_CODE_TO_MODIFY_PHONE, params, tag, call);
    }

    //退出登录
    public void logout(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.getRequst(UrlConstant.POST_LOGOUT, params, tag, call);
    }

    public void bindMobile(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_BIND_MOBILE, params, tag, call);
    }

    public void sendSmsCodeToBindMobile(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_BIND_MOBILE_GET_CODE, params, tag, call);
    }

    public void verifyIdCard(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ID_CARD_VERIFY, params, tag, call);
    }

    public void setServiceStatus(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_SET_SERVICE_STATUS, params, tag, call);
    }

    public void businessCertification(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_BUSINESS_CERTIFICATION, params, tag, call);
    }

    public void wxCertification(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_WX_CERTIFICATION, params, tag, call);
    }

    public void setVideoPrice(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_SET_VIDEO_PRICE, params, tag, call);
    }

    public void findCustomerInfo(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_FIND_CUSTOMER_INFO, params, tag, call);
    }

    public void setSeePerson(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_SET_SEE_PERSON, params, tag, call);
    }

    public void getPrivacy(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_PRIVACY, params, tag, call);
    }

    public void getMyActionList(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_MY_ACTION_LIST, params, tag, call);
    }

    /**
     * 获取动态
     *
     * @param params
     * @param tag
     * @param call
     */
    public void getActionList(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_GETACTION, params, tag, call);
    }

    /**
     * 发动态
     *
     * @param params
     * @param tag
     * @param call
     */
    public void createAction(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_CREATEACTION, params, tag, call);
    }

    public void getRelationList(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_RELATION_LIST, params, tag, call);
    }


    /**
     * 动态点赞
     *
     * @param params
     * @param tag
     * @param call
     */
    public void actionPraise(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_PRAISE, params, tag, call);
    }

    /**
     * 获取单个动态
     *
     * @param params
     * @param tag
     * @param call
     */
    public void actionOne(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_GETACTION_ONE, params, tag, call);
    }

    /**
     * 获取动态评论
     *
     * @param params
     * @param tag
     * @param call
     */
    public void comments(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_GETCOMMENTS, params, tag, call);
    }

    /**
     * 获取单条动态评论
     *
     * @param params
     * @param tag
     * @param call
     */
    public void oneComment(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_ONE_GETCOMMENT, params, tag, call);
    }

    public void updateCustomerInfo(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_UPDATE_CUSTOMER_INFO, params, tag, call);
    }

    public void getDictData(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.getRequst(UrlConstant.GET_GET_DICT_DATA, params, tag, call);
    }

    /**
     * 回复评论
     *
     * @param params
     * @param tag
     * @param call
     */
    public void replyAction(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.GET_ACTION_REPLYCOMMENT, params, tag, call);
    }

    /**
     * 评论动态
     *
     * @param params
     * @param tag
     * @param call
     */
    public void postSendComment(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.GET_ACTION_COMMENT, params, tag, call);
    }

    /**
     * 评论点赞
     *
     * @param params
     * @param tag
     * @param call
     */
    public void commentPraise(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.GET_ACTION_COMMENTPRAISE, params, tag, call);
    }

    /**
     * 获取单条评论内的回复记录
     *
     * @param params
     * @param tag
     * @param call
     */
    public void getReplys(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_GETREPLYS, params, tag, call);
    }

    public void getMyWallet(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.GET_GET_MY_WALLET, params, tag, call);
    }

    public void getBill(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_MY_BILL, params, tag, call);
    }

    public void getUnReadMsgCount(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_UNTYPE_MSG_COUNT, params, tag, call);
    }

    public void getMyUnReadList(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_MY_UNREAD_LIST, params, tag, call);
    }

    /**
     * 获取礼物
     *
     * @param tag
     * @param call
     */
    public void getGiftAll(Object tag, DataIdCallback<String> call) {
        Map<String, Object> params = new HashMap<>();
        mHttpRequst.getRequst(UrlConstant.GET_GIFT_GETGIFTALL, params, tag, call);
    }

    public void bindOutCashPassword(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_BIND_OUTCASH_PASSWORD, params, tag, call);
    }

    public void updateOutCashPassword(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_UPDATE_OUTCASH_PASSWORD, params, tag, call);
    }

    public void sendSMSToResetOutCashPassword(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_SEND_SMS_CODE_3, params, tag, call);
    }

    public void validateSMSCode(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_CHECK_SMS_CODE_2, params, tag, call);
    }

    public void validateCode(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_CHECK_SMS_CODE_, params, tag, call);
    }

    /**
     * 获取所有的家族群名
     *
     * @param params
     * @param tag
     * @param call
     */
    public void getRoupAllName(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_FAMESURNAME_GETGROUP_ALLNAME, params, tag, call);
    }

    /**
     * 获取所有的家族群名
     *
     * @param params
     * @param tag
     * @param call
     */
    public void getCityGroup(Map<String, Object> params, Object tag, DataIdCallback<String> call) {
        mHttpRequst.postRequst(UrlConstant.POST_FAMILY_SAMESURNAME_CICYGROPLIST, params, tag, call);
    }

    //充值
    public void doInCash(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_IN_CASH, params, tag, callback);
    }
    //购买海报模板api
    public void buyPosterTemplate(Map<String, Object> params, Object tag, DataIdCallback<String> callback){
        mHttpRequst.postRequst(UrlConstant.BUY_POSTER_TEMPLATE, params, tag, callback);
    }

    //购买VIP api
    public void upgradeVip(Map<String, Object> params, Object tag, DataIdCallback<String> callback){
        mHttpRequst.postRequst(UrlConstant.UPGRADE_VIP, params, tag, callback);
    }

    //购买商品 api
    public void createShopOrderPay(Map<String, Object> params, Object tag, DataIdCallback<String> callback){
        mHttpRequst.postRequst(UrlConstant.CREATE_SHOP_ORDER_PAY, params, tag, callback);
    }

    //充值贡献值API
    public void inContribution(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.INCONTRIBUTION, params, tag, callback);
    }


    //提现
    public void applyOutCash(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_APPLY_OUT_CASH, params, tag, callback);
    }

    //开通VIP
    public void doInCashVip(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_IN_CASH_VIP, params, tag, callback);
    }

    //获取VIP详情列表
    public void findIntroduceVo(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_FIND_INTRODUCE_VO, params, tag, callback);
    }

    public void getMyArticle(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_MY_ARTICLE, params, tag, callback);
    }

    //进群
    public void intoGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_FAMILYCHAT_INTOGROUP, params, tag, callback);
    }

    public void getArticleList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE_LIST, params, tag, callback);
    }

    public void getArticleCommentsList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE_COMMENT_LIST, params, tag, callback);
    }

    //文章评论
    public void commentArticle(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ARTICLE_COMMENT, params, tag, callback);
    }

    //文章评论回复
    public void replyArticle(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ARTICLE_REPLY_COMMENT, params, tag, callback);
    }

    public void getArticle(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE, params, tag, callback);
    }

    //获取非本群的同姓列表
    public void getNoTeamSameSurname(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_NO_TEAM_SAMESURNAMELIST, params, tag, callback);
    }

    //邀请多人入群
    public void invtieIntoGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_INVITER_INTOGROUP, params, tag, callback);
    }


    public void getArticleCommentReplys(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE_COMMENT_REPLYS, params, tag, callback);
    }

    public void getArticleCommentReplyPraise(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE_COMMENT_PRAISE, params, tag, callback);
    }

    public void getArticlePraise(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ARTICLE_PRAISE, params, tag, callback);
    }

    public void getRewardList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_REWARD_LIST, params, tag, callback);
    }

    ///群成员信息
    public void getGroupMemberInfo(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_SHOW_MEMBERINFO, params, tag, callback);
    }

    ///群- 禁言
    public void setSilenced(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_FAMILYCHAT_GROUP_SILENCED, params, tag, callback);
    }

    ///群主- 设置管理员或撒消  0:剥夺管理员身份, 1:设置管理员身份
    public void setGroupAdmin(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_SET_GROUP_ADMIN, params, tag, callback);
    }

    ///群主——成员信息
    public void getGroupUserInfo(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_FAMILYCHAT_GETGROUPINFO, params, tag, callback);
    }

    ///群消息提醒设置
    public void setSwitcNotification(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_SWITC_NOTIFICATION, params, tag, callback);
    }

    ///获取指定所有成员
    public void getAllGroupMember(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_ALL_MEMBER_LIST, params, tag, callback);
    }

    ///更改群公告
    public void updateNotice(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_UPDATE_GROUP_NOTICE, params, tag, callback);
    }

    public void createArticle(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_CREATE_ARTICLE, params, tag, callback);
    }

    //视频评价
    public void postJudce(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_JUDGE_JUDGE, params, tag, callback);
    }

    //我的视频记录
    public void getVideoRecord(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_IM_GETMY_VIDEORECORD, params, tag, callback);
    }

    //删除通话记录
    public void delVideoRecord(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_IM_DELETE_VIDEORECORD, params, tag, callback);
    }

    //获取通话详情
    public void getVideoCallInfo(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_IM_GET_VIDEORECORD, params, tag, callback);
    }

    public void setPrivacyRange(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_SET_PRIVACY_RANGE, params, tag, callback);
    }

    public void getSurNameSource(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.getRequst(UrlConstant.GET_GET_SURNAME_BY_SURNAME, params, tag, callback);
    }

    ///更新用户坐标
    public void updatePosition(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_INIT_CUSTOMER_POSITION, params, tag, callback);
    }

    //获取用户当时地址
    public void getPosition(Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_INIT_CUSTOMER_POSITION, tag, callback);
    }

    ///获取城市代理人竞选列表
    public void getFamilyMaster(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_FAMILY_FAMILY_MASTER, params, tag, callback);
    }

    //版本更新
    public void getVersion(Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(AppConfig.isGeliPhone ? UrlConstant.CHECK_VERSION_GREE_UPDATE : UrlConstant.CHECK_VERSION_UPDATE, new HashMap<String, Object>(), tag, callback);
    }

    //动态的所有未读消息
    public void getActionUnredMgs(Object tag, DataIdCallback<String> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_GETUNRED_LIST, params, tag, callback);
    }

    //提交所在的下载平台
    public void postChannel(String name, Object tag, DataIdCallback<String> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("channel", name);
        mHttpRequst.postRequst(UrlConstant.POST_INSERT_CHANNEL, params, tag, callback);
    }

    public void getMallSwitch(HashMap<String, Object> params, DataIdCallback<String> callback) {
        mHttpRequst.getRequst(UrlConstant.POST_GET_ALL_MALL, params, null, callback);
    }

    //群聊足迹
    public void getFootmrk(DataIdCallback<String> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.POST_FOOTMRK_LIST, params, null, callback);
    }

    //查看动态打赏列表
    public void getActionReward(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_REWAR_LIST, params, tag, callback);
    }

    public void deleteAction(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_DELETE_ACTION, params, tag, callback);
    }

    //根据地图传的省，市，区 返回 area_name
    public void getAreaName(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.getRequst(UrlConstant.GET_GETARENAME, params, tag, callback);
    }

    public void orderFamilyMasterGoods(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.APPLY_FAMILY_MASTER, params, tag, callback);
    }

    public void searchCustomerByIdOrName(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.SEARCH_CUSTOMER_BY_ID_OR_NAME, params, tag, callback);
    }

    public void getActionPraiseList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_ACTION_PRAISE_LIST, params, tag, callback);
    }

    /**
     * 动态未读消息
     *
     * @param tag
     * @param callback
     */
    public void getActioUNreadRemind(Object tag, DataIdCallback<String> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        mHttpRequst.postRequst(UrlConstant.GET_ACTION_UUREADREMInD, params, tag, callback);
    }

    public void findAllMemberList(HashMap<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.Find_All_member_list, params, tag, callback);
    }

    public void getKfList(HashMap<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_KF_LIST, params, tag, callback);
    }

    /**
     * 获得可以加进群的好友列表
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getGroupFriends(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_CUSTIN_GROUP_FREIENDS, params, tag, callback);
    }

    /**
     * 创建群
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getFoundGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_FOUND_CHAT_GROUP, params, tag, callback);
    }

    /**
     * 获取聊天群
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getChatGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_CHAT_GROUP, params, tag, callback);
    }

    /**
     * 获取聊天群成员详细信息
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getGroupInfo(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_GROUP_INFO, params, tag, callback);
    }

    /**
     * 修改群名
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getUpdateGroupName(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_UPDATE_GROUPNAME, params, tag, callback);
    }

    /**
     * 群置顶
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void setGroupTop(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_UPDATE_GROUP_SETTOP, params, tag, callback);
    }

    /**
     * 离群
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void outLietGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_OUTLIER_GROUP, params, tag, callback);
    }

    /**
     * 群主  踢人
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getGroupKick(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_KICK, params, tag, callback);
    }

    /**
     * 邀请入群
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getIntoGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_INTOGROUP, params, tag, callback);
    }

    /**
     * 群主转让
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getAdbicatGroup(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_ADBICATEGROUP, params, tag, callback);
    }

    /**
     * 群消息提醒
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getUpdateGroupNotifinaction(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_UPDATE_NOTIFIACTION, params, tag, callback);
    }

    /**
     * 自主创建群   获取群成员
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getGroupMemberList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_MEMBERLIST, params, tag, callback);
    }

    /**
     * 自主创建群   群公告
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getGroupNotece(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GROUP_UPDATENOTECE, params, tag, callback);
    }

    /**
     * 保存电话簿
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void postSavePhoneBook(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_CUSTOMER_SAVE_PHONEBOOK, params, tag, callback);
    }

    /**
     * 获取发现宗亲
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getFinDclan(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_CUSTOMER_FINDCLAN, params, tag, callback);
    }

    /**
     * 批量关注
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void postAttentionBatch(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ATTENTION_BATCH, params, tag, callback);
    }

    /**
     * 姓氏百科
     */
    public void getSurnameWiKiList(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_OTHER_LIST, params, tag, callback);
    }

    public void getUserRank(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_USER_RANK, params, tag, callback);
    }

    /**
     * 获取用户资料完善度
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getFindPerfactRatio(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.GET_FINDPERFACT_RATIO, params, tag, callback);
    }

    /**
     * 获取动态全部相片
     */
    public void getActionPhotos(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_ACTION_PHOTO, params, tag, callback);
    }

    /**
     * 删除动态评论
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void postDelComment(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ACTION_DEL_COMMENT, params, tag, callback);
    }

    /**
     * 删除文章评论
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void postArticleDelComment(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_ARTICLE_DEL_COMMENT, params, tag, callback);
    }

    /**
     * 获取优惠券未读数
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getCouponUnReadCnt(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_GET_COUPON_UN_READ, params, tag, callback);
    }

    /**
     * 获取动态消息
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void getActionMessages(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_LOOK_ACTION_MESSAGE_LIST, params, tag, callback);
    }

    /**
     * 检查是否可以认领传承使者
     *
     * @param params
     * @param tag
     * @param callback
     */
    public void isFamilyMaster(Map<String, Object> params, Object tag, DataIdCallback<String> callback) {
        mHttpRequst.postRequst(UrlConstant.POST_DEAL_CHECK_FAMILY_MASTER, params, tag, callback);
    }

    public void getFutureWeather(String paramString, StringCallback paramStringCallback) {
        OkHttpUtils.get().url("http://v.juhe.cn/weather/index")
                .addParams("format", String.valueOf(2))
                .addParams("cityname", paramString)
                .addParams("key", "77fff43c4ef9e3649720df1faaffacbd")
                .build().execute(paramStringCallback);
    }

    //摇一摇
    public void luckDraw(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.LUCK_DRAW, paramMap, paramObject, paramDataIdCallback);
    }

    public void checkCustomerSignIn(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.CHECK_CUSTOMER_SIGNIN, paramMap, paramObject, paramDataIdCallback);
    }

    public void findCustomerSignIn(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_CUSTROMER_SIGN_IN, paramMap, paramObject, paramDataIdCallback);
    }

    public void customerSignIn(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.CUSTOMER_SIGN_IN, paramMap, paramObject, paramDataIdCallback);
    }

    public void findSignInRanking(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_SIGN_IN_RANKING, paramMap, paramObject, paramDataIdCallback);
    }

    public void bindRelative(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.BIND_RELATION, paramMap, paramObject, paramDataIdCallback);
    }

//    public void clanHallTopRecord(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
//        this.mHttpRequst.postRequst(UrlConstant.CLAN_HALL_TOP_RECORD, paramMap, paramObject, paramDataIdCallback);
//    }

    public void findBanner(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_BANNER, paramMap, paramObject, paramDataIdCallback);
    }

    public void findAv(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_AV, paramMap, paramObject, paramDataIdCallback);
    }

    public void findNearPerson(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_NEAR_PERSON, paramMap, paramObject, paramDataIdCallback);
    }

    public void deleteRelative(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.DELETE_RELATIVE, paramMap, paramObject, paramDataIdCallback);
    }

    public void addRelatives(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.ADD_RELATIVE, paramMap, paramObject, paramDataIdCallback);
    }

    public void putInBasket(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.PUT_IN_BASKET, paramMap, paramObject, paramDataIdCallback);
    }

    public void avCommentPraise(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.COMMENT_PRAISE, paramMap, paramObject, paramDataIdCallback);
    }

    public void collection(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.COLLECTION, paramMap, paramObject, paramDataIdCallback);
    }

    public void commentAv(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.COMMENT_AV, paramMap, paramObject, paramDataIdCallback);
    }

    public void deleteAvComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.DELETE_AV_COMMENT, paramMap, paramObject, paramDataIdCallback);
    }

    public void deleteAvReplyComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.DELETE_AV_REPLY_COMMENT, paramMap, paramObject, paramDataIdCallback);
    }

    public void findAvComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_AV_COMMENT_BY_ID, paramMap, paramObject, paramDataIdCallback);
    }

    public void replyComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.REPLY_COMMENT, paramMap, paramObject, paramDataIdCallback);
    }

    public void findAvById(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_AV_BY_ID, paramMap, paramObject, paramDataIdCallback);
    }

    public void avPraise(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.AV_PRAISE, paramMap, paramObject, paramDataIdCallback);
    }

    public void zuciLike(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_THUMB, paramMap, paramObject, paramDataIdCallback);
    }

    public void myRelatives(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.MY_RELATIVES, paramMap, paramObject, paramDataIdCallback);
    }

    public void getMainClan(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_ASSOCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanBulletin(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CAREATE_CLAN_FIND_BULLETIN, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanChargeList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_FIND_CHARGE, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanChargePayInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_CHARGE_PAYINFO, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanDelete(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_DELETE, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanFindApprova(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_CLAN_FIND_APPROVA, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanFindApprovaNoPass(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_FIND_APPROVA_NO_PASS, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanFindApprovaPass(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_FIND_APPROVA_PASS, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanHallTopRecord(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.CLAN_HALL_TOP_RECORD, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanMember(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_MEMBER, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 商圈 - 需求- 点赞
     */
    public void postDemandPraise(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_DEMAND_PRAISE(), paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 家圈- 点赞
     */
    public void postDiscussPraise(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_DISCUSS_PRAISE(), paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 热门社群
     */
    public void getChatGroupListDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GETCHATGROUP(), paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 热门名帖
     */
    public void getTagListDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GETHOTTAG(), paramObject, callback);
    }


    public void clanMemberCharge(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_CLAN_MEMBER_CHARGE, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanMemberRemoe(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_MEMBER_REMOE, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanPayment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_PAYMENT, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanPhoto(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_PHOTO, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanPhotoDelete(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_PHOTO_DELETE, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanUpLoadingPhoto(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPLOADING_CLAN_PHOTO, paramMap, paramObject, paramDataIdCallback);
    }

    public void clanUpdate(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATE_CLANASSOCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void findClanMore(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_FIND_CLAN_ASSOCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void findClanInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CAREATE_CLAN_ASSCIATION_BYID, paramMap, paramObject, paramDataIdCallback);
    }

    public void addClanMember(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_ADD_MEMBER, paramMap, paramObject, paramDataIdCallback);
    }

    public void isCreateClan(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CHECK_CREATER_ASSOCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void MyClan(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_FIND_MYCLAN_ASSOCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void CreateClanAssciation(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_CAREATE_CLAN_ASSCIATION, paramMap, paramObject, paramDataIdCallback);
    }

    public void teaIdToClanId(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_TEAMID_CLANID, paramMap, paramObject, paramDataIdCallback);
    }

    public void SurnameFrind(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_RELATION_FRIND, paramMap, paramObject, paramDataIdCallback);
    }

    public void inviteClanMember(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_INVITE_CLAN_MEMBER, paramMap, paramObject, paramDataIdCallback);
    }

    public void releaseClanBulletin(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_RELEASE_CLAN_BULLETIN, paramMap, paramObject, paramDataIdCallback);
    }

    public void getSerch(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_SRARCH_GLOBAL, paramMap, paramObject, paramDataIdCallback);
    }

    public void ZuciCollection(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_MY_COLLECTION, paramMap, paramObject, paramDataIdCallback);
    }

    public void zuciInfoMessage(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_DETAILED, paramMap, paramObject, paramDataIdCallback);
    }

    public void zuciFootprints(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_FOOTPRINTS, paramMap, paramObject, paramDataIdCallback);
    }

    public void zuciNearby(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_NEARBY, paramMap, paramObject, paramDataIdCallback);
    }

    public void CollectionTopRoDrop(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_COLLECTION_TOP_OR_DROP, paramMap, paramObject, paramDataIdCallback);
    }

    public void ZuciSave(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ZUCI_SAVE, paramMap, paramObject, paramDataIdCallback);
    }

    public void getHistoryToday(int paramInt1, int paramInt2, StringCallback paramStringCallback) {
        OkHttpUtils.get().url("http://api.juheapi.com/japi/toh")
                .addParams("month", String.valueOf(paramInt1))
                .addParams("day", String.valueOf(paramInt2))
                .addParams("key", "62d3d608b5941d63e44fc2edab520d81")
                .addParams("v", "1.0")
                .build()
                .execute(paramStringCallback);
    }

    public void getCalender(String paramString, Object paramObject, StringCallback paramStringCallback) {
        OkHttpUtils.get().url("http://v.juhe.cn/calendar/day").addParams("date", paramString).addParams("key", "495af6a7f031e2b32535202bc8b3a3fe").build().execute(paramStringCallback);
    }

    public void getMessageForBoard(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_MESSAGE_FOR_BOARD, paramMap, paramObject, paramDataIdCallback);
    }

    public void addClanHallComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.ADD_CLAN_HALL_COMMENT, paramMap, paramObject, paramDataIdCallback);
    }

    public void inviteRanking(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.INVITE_RANKING, paramMap, paramObject, paramDataIdCallback);
    }

    public void inviteRankingMyRank(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.INVITE_RANKING_MY_RANK, paramMap, paramObject, paramDataIdCallback);
    }

    public void inviteRankingMyList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.INVITE_RANKING_MY_INVITE_LIST, paramMap, paramObject, paramDataIdCallback);
    }

    public void groupType(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_CHECK_GROUP_TYPE, paramMap, paramObject, paramDataIdCallback);
    }

    public void isClanDissolve(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CLAN_ISCLANASSOCIAION, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取新闻动态
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getNewAction(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_NEW_ACTION, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取新闻动态列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getNewActionList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_NEW_ACTION_LIST, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 添加用户反馈信息
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void SaveCustomerFeedack(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_SAVE_CUSTOMER_FEEDACK, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取用户反馈信息
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getFeedback(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_HISTORY_FEEDBACK, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取用户反馈信息详情
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getFeedbackInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_HISTORY_FEEDBACK_INFO, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 提交问题已解决
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void postFeedbackSolving(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_FEEDBACK_SOLVING, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 祖祠查询宗亲会
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void zucuIdQueryClan(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_HALLFIND_CLAN_ASSOCIATiON, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询指定姓氏的总人数
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getSurnameCount(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CUSTOMER_COUNTBY_SURNAME, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 检查微信openId是否已注册
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getWeixiCheckOpenid(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_CUSTOMEr_CHECK_WEIXIN_OPENID, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 修改用户所属家族
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void updateFamily(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATE_FAMILY, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 发起宗享会会议
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void saveEnjoy(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_SAVEENJOY, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询宗享会会议动态
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getEnjoy(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_GETENJOY, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询宗享会项目
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBotAll(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_BOT_ALL, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询宗享会项目详情(我的)
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBotMy(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_My_BOT_INFO, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询我的宗享会项目
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBotMyList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_BOT_TA_BOT_INFO, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 删除宗享会项目
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void delBot(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_DEL_BOT, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 发起宗享会项目
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void addBot(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ADD_BOT, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 编辑宗享会项目
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void updateBot(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATE_BOT, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取标签
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getTag(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_GET_TAG, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询所有朋友，亲戚关系类型
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getFriendTreeTag(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_FRIEND_TREE, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 邀请通讯录好友
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void noteInvite(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_INVITE_FRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得朋友关系申请
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getFriendRelation(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_FRIEND_GETREQUES_FRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 朋友关系申请通过或忽略
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getAddFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_APPLY_REQUEST, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得某关系的对应关系
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getOpName(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_OP_FRIEND_NAME, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 提交朋友关系申请
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void postRequstFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_REQEST_FRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得我的朋友关系
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getMyFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_GET_MYFRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得我的黑名单
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBalck(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_BLACK_LIST, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 黑名单操作
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBlack(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_DO_BLACK, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 删除关系
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void delFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_DEL_FRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 你可能认识的人
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getMayKnowList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_MAY_KNOWLIST, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 修改关系
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void postUpdaterFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATE_FRIEND, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 通过验证码获取用户信息
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getCodeInfoUserData(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_GET_INFORMATION, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得申请个数
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getRequstCount(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_GET_REQUESTCOUNT, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获得申请个数
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getMiniJson(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_MINI_JSON, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取我的页面数据
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getFindMyData(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.FIND_MY_FOUR_VERSION, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 查询用户未读动态数量
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getActionReadNum(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_ACTION_READACTION_NUM, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 获取消息通知列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getMessageList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETMESSAGELIST, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 新朋友
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getAttentionAndFriend(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETATTENTIONANDFRIEND, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取用户收到的评论和赞列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getCommentPraiseList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETCOMMENTPRAISELIST, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取用户收到的评论和赞列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getCommentAndPraise(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETCOMMENTANDPRAISE, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取用户服务信息
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getUsrServiceInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETUSRSERVICEINFO, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 设置用户服务信息
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void meessageSetting(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_MEESSAGESETTING, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取某省的所有市
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void acquireCity(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_ACQUIRECITY, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 通讯录
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getRelation(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETRELATION, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取百家秘书服务用户列表API
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBaiJiaService(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETBAIJIASERVICE, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 百家秘书运营数据图
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getBaiJiaSource(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETBAIJIASOURCE, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 推送用户列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getPushList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETPUSHLIST, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 推送用户列表
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getPushProject(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETPUSHPROJECT, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取精准推送商品列表API（
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getThreeProject(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETTHREEPROJECT, paramMap, paramObject, paramDataIdCallback);
    }
    /**
     * 获取消息通知详细
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getMessageDetail(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETMESSAGEDETAIL, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 我的访客(版本:3)
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getfindVisitInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_FIND_VISITINFO, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 用户详细信息(版本:3)
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getUserInfoV3(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_FIND_CustomerInfo, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 用户详细信息(版本:3)
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getUserBaseInfoV3(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_FIND_CustomerBaseInfo, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 更新用户信息(版本:3)
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void updataUserInfoData(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATA_CUSTOMER_INFO, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 姓氏族人 接口
     *
     * @param paramMap
     * @param paramObject
     * @param paramDataIdCallback
     */
    public void getSurnameOrigin(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.SURNAME_ORIGIN, paramMap, paramObject, paramDataIdCallback);
    }


    public void getPic(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.RANKPIC, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 个人 功德排行
     */
    public void postMeritRank(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.MERIT_RANK, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 姓氏 功德排行
     */
    public void getSurnameRankTop10(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.SURNAME_RANK, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 任务奖励
     */
    public void getQuestReward(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.QUEST_REWARD, paramMap, paramObject, paramDataIdCallback);
    }

    /**
     * 任务奖励 领取
     */

    public void postQuestReward(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_QUEST_REWARD, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 姓氏排行
     */
    public void getSurnameRank(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.FIND_SURNAME_RANK, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 首页 家族 获取数据
     */
    public void getFamliyData(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.getRequst(UrlConstant.GET_FAMLIY_DATA, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 为家 修行查询我的修行功德数
     */
    public void getMeritsData(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> paramDataIdCallback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_MERITS, paramMap, paramObject, paramDataIdCallback);
    }


    /**
     * 发布随笔 获取Tag列表
     */
    public void findLabel(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_FIND_LABEL, paramMap, paramObject, callback);
    }

    /**
     * 发布随笔 发布
     */
    public void insertEssay(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_INSERT_ESSAY, paramMap, paramObject, callback);
    }

    /**
     * 发布文章 发布
     */
    public void publishedArticles(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.PUBLISHED_ARTICLES, paramMap, paramObject, callback);
    }

    /**
     * 发布文章 发布 rvs1
     */
    public void publishedArticlesNew(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.PUBLISHED_ARTICLES_NEW, paramMap, paramObject, callback);
    }

    /**
     * 获取VIP配置商品列表API
     */
    public void getVipShopGoodsList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_VIP_SHOP_GOODS_LIST, paramMap, paramObject, callback);
    }

    /**
     * 草稿箱列表
     */
    public void getDraftsList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_DRAFTS_LIST, paramMap, paramObject, callback);
    }

    /**
     *内容（动态，视频，文章）点赞(测试通过)
     */
    public void praiseContent(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.PRAISE_CONTENT, paramMap, paramObject, callback);
    }

    /**
     * 用户添加取消关注活动标签接口
     */
    public void addContentLabelCustomer(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.ADD_CONTENT_LABEL_CUSTOMER, paramMap, paramObject, callback);
    }

    /**
     * 关注用户
     */
    public void bindRelation(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_BIND_RELATION, paramMap, paramObject, callback);
    }
    /**
     * 传承人服务用户API
     */
    public void updateService(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POST_UPDATESERVICE, paramMap, paramObject, callback);
    }


    /**
     * 获取活动标签下的视频文章内容 rvs1
     */
    public void findContentByLabel(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_CONTENT_BY_LABEL, paramMap, paramObject, callback);
    }

    /**
     * 内容详情 rvs1
     */
    public void getContentDetails(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.CONTENT_DETAILS, paramMap, paramObject, callback);
    }

    /**
     * 收藏内容 RVS1
     */
    public void doCollection(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.DO_COLLECTION, paramMap, paramObject, callback);
    }

    /**
     * 发布视频 发布
     */
    public void publishedVideo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.PUBLISHED_VIDEO, paramMap, paramObject, callback);
    }

    /**
     * 首页 推荐
     */
    public void getRecommendFamilyCricleDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.RECOMMEND_FIND, paramMap, paramObject, callback);
    }

    //我的创作 列表
    public void getContentList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.MY_CREAT_CONTENT_LIST, paramMap, paramObject, callback);
    }

    /**
     * 首页 推荐
     */
    public void findPostersModelById(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.FIND_POSTERS_MODEL_ID, paramMap, paramObject, callback);
    }

    /**
     * 推荐列表（poster）
     */
    public void getAppPostersModelDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTERMODEL_LIST, paramMap, paramObject, callback);
    }

    public void getAppPostersModelLabel(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.POSTERMODEL_LABEL, paramMap, paramObject, callback);
    }

    /**
     * 获取活动标签
     * {"type":1} type：类型:1文章视频2视频模板3海报模板
     */
    public void getContentLabel(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.CONTENTLABEL, paramMap, paramObject, callback);
    }

    /**
     * 首页 生活
     */
    public void getIcityFamilyCricleDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.RECOMMEND_LIFE, paramMap, paramObject, callback);
    }


    /**
     * 首页 内容详情
     */
    public void getContentRelease(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.CONTENTRELEASE, paramMap, paramObject, callback);
    }

    /**
     * 我的 - 我的创作
     */
    public void getMineFamilyCricleDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.MINEFAMILYCRICLEDO, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 活动
     */
    public void getActivityListDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.ACTIVITYLISTDO, paramMap, paramObject, callback);
    }

    /**
     * 参与活动
     */
    public void joinActivityDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.JOINACTIVITYDO, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 需求
     */
    public void getDemandList(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.DEMANDLIST, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 封面人物列表
     */
    public void getCoverPersonListDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.COVERPERSONLISTDO, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 申请成为封面人物
     */
    public void doRankedDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.DORANKEDDO, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 申请成为封面人物(再次)
     */
    public void doRankedDoAgain(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.DORANKEDDO_AGAIN, paramMap, paramObject, callback);
    }

    /**
     * 商圈 - 广场 - 支付成为封面人物金额
     */
    public void doPayRankDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.DOPAYRANKDO, paramMap, paramObject, callback);
    }

    /**
     * 钱包 - VIP收益
     */
    public void getMyInheritorEarnings(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.MYINHERITOREARNINGS, paramMap, paramObject, callback);
    }

    /**
     * 申请成为传承人
     */
    public void applyForInheritorDo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.APPLYFORINHERITORDO, paramMap, paramObject, callback);
    }

    //点赞
    public void praiseArticles(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.PRAISEARTICLES, paramMap, paramObject, callback);
    }

    //评论
    public void contentComent(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.CONTENTCOMENT, paramMap, paramObject, callback);
    }

    //回复评论
    public void replyComent(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.REPLYCOMENT, paramMap, paramObject, callback);
    }

    //获取评论列表
    public void getContentComments(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_CONTENT_COMMENTS, paramMap, paramObject, callback);
    }

    //获取新消息通知标识API
    public void getMessage(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETMESSAGE, paramMap, paramObject, callback);
    }

    //设置通知消息全部为已读API
    public void doSettingRead(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_DOSETTINGREAD, paramMap, paramObject, callback);
    }

    //消息删除
    public void getDelMessage(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETDELMESSAGE, paramMap, paramObject, callback);
    }

    //删除内容下评论
    public void getDelContentComment(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback) {
        this.mHttpRequst.postRequst(UrlConstant.GET_GETDELCONTENTCOMMENT, paramMap, paramObject, callback);
    }
    /**
     * 个人中心 - 获取用户基本信息API（测试通过）
     */
    public void findCustomerBaseInfo(Map<String, Object> paramMap, Object paramObject, DataIdCallback<String> callback){
        this.mHttpRequst.postRequst(UrlConstant.FIND_CUSTOMER_BASE_INFO, paramMap, paramObject, callback);
    }
}
