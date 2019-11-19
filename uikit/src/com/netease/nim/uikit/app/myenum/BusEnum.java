package com.netease.nim.uikit.app.myenum;

/**
 * Created by zh03 on 2017/8/7.
 */

public enum BusEnum {
    /**
     * 未定义
     */
    INVALID(0),
    /**
     * 用户更新
     */
    USER_UPDATE(1),
    /**
     * 更新位置
     */
    LOCATION_UPDATE(2),
    /**
     * 选择所属地区
     */
    FINISH_SELECTCITY(3),
    /**
     * 选择市
     */
    CITY(4),
    /**
     * 选择省
     */
    PROVINCE(5),
    /**
     * 登录成功更新
     */
    LOGIN_UPDATE(6),

    //退出登录
    LOGOUT(7),
    /**
     * 选择姓氏更新
     */
    SURNAME_UPDATE(55),
    /**
     * 选择成员所属地区
     */
    district(56),
    /**
     * 选择职业
     */
    JOBS(57),
    /**
     * 网易私信更新
     */
    IM_USERCHAT_UPDATE(58),
    /**
     * 邀请入群回调activity
     */
    INVITE_JOIN_GROUP(59),
    /**
     * 群资料activity
     */
    GROUP_USERINFO(60),
    /**
     * 打开用户资料activity
     */
    OPEN_USERINFO(61),
    /**
     * 信息编辑
     */
    OPEN_compile(62),
    /**
     * 信息编辑是否全选
     */
    IS_SELECTLAA(63),
    /**
     * 信息删除
     */
    MESSGE_DEL(64),
    /**
     * 调支付页面
     */
    In_CashActivity(65),
    /**
     * Im登录成功
     */
    IM_LOGIN(66),
    /**
     * 落地页
     */
    START_PAGE(67),
    /**
     * 消息删完
     */
    IS_DELETE_ALL(68),
    /**
     * 跳转聊天
     */
    INTEN_CHAT(69),

    UPDATE_CUSTOMER_INFO(70),
    /**
     * 删除 通话记录后回调
     */
    DELETE_VIDEO_RECORD(71),
    /**
     * 删除 通话记录后回调
     */
    ACTION_UNREAD(72),
    /**
     * 刷新群列表
     */
    GROUP_UPDATE(73),
    /**
     * 群未读消息
     */
    GROUP_UNREAD_COUNT(74),

    Message_RECEIVER(80), MESSAGE_RECEIVER_DELETE(82), CLAN_LOCATION(83),
    /**
     * 网络连接
     */
    NETWOR_CONNECTION(75),
    ETWOR_CONNECTION(75),
    CLAN_WEIXIN_PAY(85),
    CLAN_UPDATE(86),
    ZUCI_SELECT(87),
    /**
     * 绑定家族
     */
    Bangding_Family(88),
    /**
     * 语言切换
     */
    Language(89), payment_success(90),
    toback(95),toproject(96),
    /**
     * 家圈-推荐-点赞
     */
    FAMILY_RECOMMEND_ZAN(91),

    NOT_POINT(92),

    /**
     * 推荐视频 — 删除
     */
    RECOMMEND_CANCEL(93),

    /**
     * 推荐视频 - 不感兴趣
     */
    RECOMMEND_NOT_INTERESTED(94);

    private int value;

    BusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
