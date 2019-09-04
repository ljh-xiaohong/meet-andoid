# meet_demo 开发记录

author: g000gle
date  : 2019.05.08 - 2019.06.23



git branch:

 - master : 初始版本代码
 - dev    : 开发分支
 - modifyMobileLogin : 修改手机登录流程(已合并到dev, 暂留)
 - changeWallUI      : 修改钱包入口为旧版
 - UpdateGlideV4     : 修改项目构建工具和Android支持库，配合阿里云SDK升级到Glide v4


modules:

 - uikit                 // 网易云信
 - VerticalBannerView    // 垂直滚动广告条
 - AliyunSVideoLibrary   // 阿里云短视频SDK专业版
 - AlivcCore             // 阿里云短视频sdk核心组件
 - AliyunSVideoBase      // 阿里云短视频基础包
 - AliyunFileDownLoader  // 阿里云资源数据库和下载模块
 - AliyunSvideoMusic     // 阿里云短视频音乐控件
 - AliyunRecorder        // 阿里云短视频录制模块
 - AliyunEditor          // 阿里云编辑模块ui和功能实现模块
 - AliyunCrop            // 阿里云裁剪ui模块
 - AlivcMedia            // 阿里云媒体资源选择库
 - AliyunVideoSdk        // 阿里云短视频sdk编辑核心功能
 - AliyunVideoCommon     // 阿里云公共模块，主要为工具类
 - thirdparty-lib        // 阿里云短视频的gradle配置文件
 - PictureSelectorLib    // 多媒体选择库，原项目Glide版本为v4，这里改为v3
 

details:

 - 代码没有严格的架构分层，God Activity / God Fragment 模式..
 - 新建 Activity/Fragment 继承自 BaseActivity/BaseFragment
 - Activity/Fragment按功能模块分类存放在com.yuejian.meet.activities和com.yuejian.meet.fragments包下
 - BaseActivity/BaseFragment 中主要的成员变量有 UserEntity 和 ApiImp
 - BaseActivity/BaseFragment 中UserEntity在登陆/注册流程中初始化，用于获取用户基本信息
 - BaseActivity/BaseFragment 中ApiImp是网络请求封装类，在该类中定义后端接口。HttpRequstDB类基于OkHttp封装post和get请求，同时处理Response最外层数据
 - com.yuejian.meet.utils包下包含许多开发常用工具类
 - com.yuejian.meet.ui包下定义一些控件的通用样式配置
 - com.yuejian.meet.widgets包下是自定义控件
 - 以上三个包代码含有重复定义问题，需要新定义相关类时应查看是否已有现成的代码可用，减少冗余
 - com.yuejian.meet.session多是网易云信相关代码
 - com.yuejian.meet.bean定义实体类，由于后端代码变更，会有很多废弃不使用但没有删除的代码
 - 支付功能参考代码：com.yuejian.meet.activities.mine.BuyAllVipPermissionActivity
 - 接入某平台需要指定的应用签名，因此不要换签名文件
    * 应用签名：66ab0dee5723031373c4783b037bc761
 - Tinker 导出的debug包位置 meet_demo/app/bakApk
 - 主项目buildToolsVersion以及个别依赖库的版本(如Glide)修改涉及到大量旧版本代码和第三方依赖库，修改需谨慎，每步修改应使用gradle命令查清各第三方库的依赖关系和生效版本
 - 项目中使用OkHttp、EventBus的非官方封装库，相关问题需要查看非官方封装库对应版本的解决方案
 - 项目代码庞大，IDE设置了编译缓存以提高编译速度，当修改第三方库无效时，检查一下是否是AndroidStudio的缓存问题
 - 代码修改记录可用gitk查看详情

