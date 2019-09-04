package com.netease.nim.uikit.app;


import android.content.Context;

import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.ChatEnum;

/**
 * <b>创建时间</b> 2016/6/27 <br>
 *
 * @author zhouwenjun
 */
public class AppConfig {
    public static boolean isGeliPhone=false;///是否是格力手机包
    public static boolean isLogin=false;///是否是格力手机包
    public static UserEntity userEntity;
    public static String CustomerId="";//用户id
    public static String UserSex;////用户性别
    public static String Token="";//
    public static Double moneySun=0.0;//总金额
    public static Double rechargeSum=0.0;///充值的钱
    public static Double gainsSum=0.0;//收益的钱
    public static String getChatId="";
    public static String videoToCustomerId="";//对方视频customerId
    public static String chatFreeCnt="1";///聊天消息的乘余条数  0：表示要扣费
    public static Boolean isMessageFreeHint=false;///是否已经提示过扣扣费弹窗
    public static long mAvchattime=Long.parseLong("0");//通话时间
    public static String chatGiftPath;//本地礼物根目录
    public static float width;//手机屏目宽
    public static Boolean isGiftDownload=false;
    public static Boolean isFreechat=false;////聊天是否提示过扣费
    public static String freechatCnt="11";////还乘多少免费次数(0表示要开始扣费)
    public static String sdGiftPath="";//图片保存的sd卡地址
    public static Context context;///上下文
    public static Long avChatPrice=Long.parseLong("0");///咨询的视频价格

    public static Boolean isVideo=false;////对方是否可以视频
    public static String clipboard="";//////获取剪贴板的内容
    public static long OnleTime = 0;///推送时间
    public static String attention="0";//关注人的状态
    public static Boolean isGeLiGuide=false;///是否是格力引导页
    public static Boolean isShopTeam=false;//是否商店跳转到群聊
    public static ChatEnum chatEnum=ChatEnum.defaults;//群聊类型
    public static Boolean isShopP2PChat=false;//是否商店跳转到聊天客服
    public static Boolean isClanWeiXinPay=false;





    public static String userUpdate="userUpadte";//更新用户信息
    public static String nearbyUpadte="nearbyUpadte";//附近的人数据更新
    public static String Toasshow="Toasshow";//toas提示
    public static String updateMoney="updateMoney";//更新钱
    public static String userKick="userKick";///用户被踢下线
    public static String user_freeze="user_freeze";///用户被踢下线
    public static String finishAll="finishAllActivity";////关闭所有的页面通知
    public static String finishSelectCity="finishSelectCity";////关闭选择地区页面
    public static String sponsorId="0";///城市发起人id
    public static int pageItemCount=20;
    public static Boolean isAppOpen=false;
    public static boolean isShowPush = false;
    public static final String APK_DOWNLOAD_PATH = "APK_DOWNLOAD_PATH";
    public static String family_id = "";//申请城市发起人所属 family_id
    public static String AreaName = "";///申请城市发起人所属 （区名）
//    public static boolean isLOG = BuildInfo.BUILD_LOG;//控制是否log
    public static boolean is119P2P = false;//控制是不是119视频连接true
    public static boolean isOnLine = false;//女的是否上线的


    public static boolean isOPENED_CHATNOTIC = false;//是否弹出私聊提示框
    public static boolean isOPENED_CHATCOV = false;//是否打开会话详情
    public static boolean isOPENED_COVLIST = false;//是否打开会话列表
    public static boolean isForeground = true;//设置是否在前台
    public static boolean isClosedNim = false;//设置是否关闭私信提醒
    public static String customerPlace = "";//城市

    public static String province = "";//省

    public static String district = "";//区
    public static double longitude = 0;
    public static double latitude = 0;
    public static String slongitude = "0";//这个是最新的
    public static String slatitude = "0";//这个是最新的
    //判断视频中是否有关注操作
    public static boolean isVideoFollow = false;
    public static String constellation = "";//星座
    public static String job = "";//行业
    public static String language = "1";//语言


    public static boolean isChina = true;
    public static boolean isReply = false;
    public static String city = "";
    public static int godGirlCount = 0;
    public static int chatedCount = 0;


    public static int isFriendCount = 1;
    public static int isFansCount = 1;
    public static int isAttentionCount = 1;

    public static final String INVITE_CODE = "app_invite_code";
}
