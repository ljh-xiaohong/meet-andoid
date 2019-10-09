package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ReportActivity;
import com.yuejian.meet.activities.mine.InputActivity;
import com.yuejian.meet.activities.mine.MyDialogActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentBean;
import com.yuejian.meet.bean.PraiseEntity;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.bean.VideoAndContentEntiy;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.MoreDialog;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.VideoPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class VideoActivity extends AppCompatActivity {

    private String contentId = null;
    private String customerId = null;
    private boolean full_screen;

    MoreDialog moreDialog;

    private Context mContext;

    private Intent intent;

    private static final int INPUT_STATE = 154;

    private VideoAndContentEntiy info;

    @Bind(R.id.activity_video_player)
    VideoPlayer player;

    private String[] labelName, labelId;

    private List<String> moreData;

    private static final int REQUEST_CODE = 200;

    WeakReference<VideoActivity> reference;

    private ApiImp apiImp = new ApiImp();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        if (!getData()) return;
        reference = new WeakReference<>(this);
        getDataFromNet();
    }

    private void initDialog() {
        //初始化dialog
        moreDialog = new MoreDialog();
        moreData = new ArrayList<>();
        //初始化内容
        if (info.getContentDetail().getCustomerId().equals(AppConfig.CustomerId)) {
//            moreData.add("编辑");
            moreData.add("删除");
        } else {
            moreData.add(info.getContentDetail().isCollection() ? "已收藏" : "收藏");
            moreData.add("不感兴趣");
            moreData.add("举报");
        }
        moreData.add("取消");
        //初始化adapter
        moreDialog.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return moreData.size();
            }

            @Override
            public Object getItem(int i) {
                return moreData.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View child = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_text_item, null);
                TextView textView = child.findViewById(R.id.dialog_text_item_text);
                textView.setText(moreData.get(i));
                if (i == moreData.size() - 1) {
                    textView.setBackgroundColor(Color.parseColor("#F2F5F7"));
                }

                return child;
            }
        });
        moreDialog.setOnclickItemListener(position -> {
            switch (moreData.get(position)) {

                case "编辑":

                    break;

                case "删除":
                    delectContent();
                    break;

                case "收藏":
                    Collection();
                    break;

                case "不感兴趣":
                    notInterested();
                    break;

                case "举报":
                    ReportActivity.startActivityForResult(mContext, 1, info.getContentDetail().getCustomerId(), info.getContentDetail().getId() + "", "3");
                    break;

                case "取消":
                    moreDialog.dismiss();
                    break;
            }
        });
    }

    /**
     * @param context
     * @param contentId    内容ID
     * @param customerId   用户ID
     * @param SCREEN_MATCH 是否全屏
     */
    public static void startActivity(Context context, String contentId, String customerId, boolean SCREEN_MATCH) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("VideoActivity.contentId", contentId);
        intent.putExtra("VideoActivity.customerId", customerId);
        intent.putExtra("VideoActivity.SCREEN_MATCH", SCREEN_MATCH);
        context.startActivity(intent);
    }

    /**
     * 主要用于删除不感兴趣，及删除视频
     *
     * @param context
     * @param contentId
     * @param customerId
     * @param position
     * @param SCREEN_MATCH
     */
    public static void startActivityForResult(Activity context, String contentId, String customerId, int position, int requestCode, boolean SCREEN_MATCH) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("VideoActivity.contentId", contentId);
        intent.putExtra("VideoActivity.customerId", customerId);
        intent.putExtra("VideoActivity.SCREEN_MATCH", SCREEN_MATCH);
        intent.putExtra("VideoActivity.position", position);
        context.startActivityForResult(intent, requestCode);
    }

    private boolean getData() {
        contentId = getIntent().getStringExtra("VideoActivity.contentId");
        customerId = getIntent().getStringExtra("VideoActivity.customerId");
        full_screen = getIntent().getBooleanExtra("VideoActivity.SCREEN_MATCH", full_screen);
        GSYVideoType.setShowType(full_screen ? GSYVideoType.SCREEN_MATCH_FULL : GSYVideoType.SCREEN_TYPE_DEFAULT);
        return contentId != null && customerId != null;
    }


    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", contentId);
        params.put("customerId", customerId);
        apiImp.getContentDetails(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                parseJSON(data);
                if (info == null) return;
                initData();
                if (AppConfig.CustomerId != null && AppConfig.CustomerId.length() > 0) {
                    initListener();
                    initDialog();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void initData() {
        if (info == null || info.getContentDetail() == null) return;
        VideoAndContentEntiy.ContentDetail detail = info.getContentDetail();
        player.setLooping(true);
        player.setUp(detail.getCrContent(), true, "");
        player.startPlayLogic();
        Glide.with(mContext).load(checkData(detail.getUserPhoto())).into(player.getHeadImagView());
        player.getNameText().setText(checkData(detail.getUserName()));
        player.getContenText().setText(checkData(detail.getContentTitle()));
        //关注
        player.getFollowText().setTextColor(Color.parseColor(checkData(detail.getRelationType()).equals("0") ? "#ffffffff" : "#66ffffff"));
        player.getFollowText().setText(checkData(detail.getRelationType()).equals("0") ? "加关注" : "已关注");
        //点赞数
        //是否点赞
        player.setLike(checkData(detail.getIsPraise()).equals("1") ? true : false, info.getContentDetail().getFabulousNum());
        //评论数量
        player.getDiscussButton().setText(checkData(detail.getCommentNum()));
        player.getContenText().setText(checkData(detail.getContentTitle()));
        if (detail.getShopList() != null && detail.getShopList().getShopId().length() > 0) {
            player.getGoodsButton().setText(checkData(detail.getShopList().getShopName()));
        }
        if (detail.getLabelName() != null && detail.getLabelName().contains("#")) {
            labelName = detail.getLabelName().trim().substring(1, detail.getLabelName().length()).split("#");
            labelId = detail.getLabelId().split(",");
        }

        //标签
        player.setTagItem(labelName, labelId, view -> {
            ActivityLabActivity.startActivity(mContext, (String) (view.getTag()), AppConfig.CustomerId);
        });


    }

    private void initListener() {
        player.getLikeButton().setOnClickListener(view -> {
            like();
        });
        player.getShareButton().setOnClickListener(view -> {
            share();
        });

        player.getFollowText().setOnClickListener(view -> {
            AddFollow();
        });
        player.getDiscussButton().setOnClickListener(view -> {
//            Intent intent = new Intent(mContext, MyDialogActivity.class);
//            intent.putExtra("crId", contentId + "");
//            startActivityForResult(intent, 1);

            MyDialogActivity.startActivityForResult(this, contentId, MyDialogActivity.StyleType.BLACK, REQUEST_CODE);


        });
        player.getDiscussEdittext().setOnClickListener(view -> {
            InputActivity.startActivityForResult(this, contentId, "", "", INPUT_STATE);
        });

        //更多
        player.getMoreButton().setOnClickListener(view -> {
            moreDialog.show(getSupportFragmentManager(), "VideoActivity.moreDialog");
        });

        player.getNameText().setOnClickListener(view -> {
            personDetail();
        });

        player.getHeadImagView().setOnClickListener(view -> {
            personDetail();
        });

        player.getGoodsButton().setOnClickListener(view -> {
            goodDetail();
        });
    }

    private void personDetail() {
        if (info == null || info.getContentDetail() == null) return;
        VideoAndContentEntiy.ContentDetail detail = info.getContentDetail();
        String urlVip = "";
        if (checkData(detail.getUserVipType()).equals("0")) {
            //非VIP
//                    url = UrlConstant.ExplainURL.PERSON_INFORMATION_UNVIP;
            urlVip = "http://app2.yuejianchina.com/yuejian-app/personal_center/userHome3.html";
        } else {
            //VIP
//                    url = UrlConstant.ExplainURL.PERSON_INFORMATION_VIP;
            urlVip = "http://app2.yuejianchina.com/yuejian-app/personal_center/personHome2.html";
        }
        urlVip = String.format(urlVip + "?customerId=%s&opCustomerId=%s", AppConfig.CustomerId, info.getContentDetail().getCustomerId());

        intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.URL, urlVip);
        intent.putExtra("No_Title", true);
        startActivity(intent);
    }

    private void goodDetail() {
        if (info == null || info.getContentDetail() == null || info.getContentDetail().getShopList() == null)
            return;
        String urlShop = "";
//                urlShop = String.format(UrlConstant.ExplainURL.SHOP_DETAIL + "?customerId=%s&gId=%s&phone=true", AppConfig.CustomerId, info.getContentDetail().getShopList().getShopId());
        urlShop = String.format("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/item.html?customerId=%s&gId=%s&phone=true", AppConfig.CustomerId, info.getContentDetail().getShopList().getShopId());
        intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.URL, urlShop);
        intent.putExtra("No_Title", true);
        startActivity(intent);
    }

    private String checkData(String data) {
        if (TextUtils.isEmpty(data)) data = "";

        return data;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkIsLife()) return;
        switch (requestCode) {
            //评论
            case INPUT_STATE:

                if (resultCode == RESULT_OK) {
                    int count = Integer.valueOf(info.getContentDetail().getCommentNum());
                    ;
                    info.getContentDetail().setCommentNum(++count + "");
                    player.getDiscussButton().setText(checkData(info.getContentDetail().getCommentNum()));

                }

                break;
            //
            case REQUEST_CODE:

                break;


        }

    }

    private void parseJSON(String data) {

        try {
            if (TextUtils.isEmpty(data)) return;
            JSONObject jo = JSON.parseObject(data);
            if (null == jo && !jo.getString("code").equals("0")) return;
            JSONObject in = jo.getJSONObject("data");
            info = new VideoAndContentEntiy();

            if (!TextUtils.isEmpty(in.getString("commentList")) && !in.getString("commentList").equals("null")) {
                info.setCommentList(JSON.parseArray(in.getString("commentList"), CommentBean.DataBean.class));
            }

            if (!TextUtils.isEmpty(in.getString("contentDetail")) && !in.getString("contentDetail").equals("null")) {
                VideoAndContentEntiy.ContentDetail contentDetail = new VideoAndContentEntiy.ContentDetail();
                JSONObject jsonObject = JSONObject.parseObject(in.getString("contentDetail"));
                if (null == jsonObject) return;
                contentDetail.setPhotoAndVideoUrl(jsonObject.getString("photoAndVideoUrl"));
                contentDetail.setUserPhoto(jsonObject.getString("userPhoto"));
                contentDetail.setCrContent(jsonObject.getString("crContent"));
                contentDetail.setContentTitle(jsonObject.getString("contentTitle"));
                contentDetail.setUserName(jsonObject.getString("userName"));
                contentDetail.setUserVipType(jsonObject.getString("userVipType"));
                contentDetail.setRelationType(jsonObject.getString("relationType"));
                contentDetail.setCommentNum(jsonObject.getString("commentNum"));
                contentDetail.setLabelId(jsonObject.getString("labelId"));
                contentDetail.setCreateTime(jsonObject.getString("createTime"));
                contentDetail.setFabulousNum(jsonObject.getString("fabulousNum"));
                contentDetail.setCustomerId(jsonObject.getString("customerId"));
                contentDetail.setIsPraise(jsonObject.getString("isPraise"));
                contentDetail.setId(jsonObject.getString("id"));
                contentDetail.setIsCollection(jsonObject.getBoolean("isCollection"));
                contentDetail.setLabelName(jsonObject.getString("labelName"));
                contentDetail.setViewNum(jsonObject.getString("viewNum"));
                contentDetail.setContentType(jsonObject.getString("contentType"));
                contentDetail.setShopList(JSON.parseObject(jsonObject.getString("shopList"), VideoAndContentEntiy.shopList.class));
                info.setContentDetail(contentDetail);
            }

        } catch (NullPointerException e) {

        }

    }

    /**
     * 点赞
     */
    //todo 点赞处理
    private void like() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("crId", contentId);
        apiImp.praiseContent(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                PraiseEntity praise = JSON.parseObject(data, PraiseEntity.class);
                if (praise == null) return;
                switch (praise.getCode()) {
                    //操作成功
                    case 0:
                        int count = Integer.valueOf(info.getContentDetail().getFabulousNum());
                        if (info.getContentDetail().getIsPraise().equals("1")) {
                            info.getContentDetail().setIsPraise("0");
                            count = count - 1;
                        } else {
                            info.getContentDetail().setIsPraise("1");
                            count = count + 1;
                        }
                        info.getContentDetail().setFabulousNum(count + "");
                        player.setLike(info.getContentDetail().getIsPraise().equals("1") ? true : false, count + "");

                        break;

                    //拉黑
                    case 10205:

                        break;

                    //系统异常
                    case 10001:

                        break;

                    //其他
                    case -1:

                        break;

                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 分享
     */
    private void share() {
        Glide.with(mContext).load(info.getContentDetail().getPhotoAndVideoUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (checkIsLife()) return;
                Utils.umengShareByList(
                        (Activity) mContext,
                        bitmap,
                        info.getContentDetail().getContentTitle(),
                        mContext.getResources().getString(R.string.share_description),
                        String.format("http://app2.yuejianchina.com/yuejian-app/release/blank.html?type=%s&id=%s", info.getContentDetail().getContentType(), info.getContentDetail().getId()));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onVideoPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onVideoPause();
        }

        ButterKnife.unbind(this);
    }

    /**
     * 关注
     */
    public void AddFollow() {
        Map<String, Object> params = new HashMap<>();
        params.put("opCustomerId", info.getContentDetail().getCustomerId());
        params.put("customerId", customerId);
        //2-取消关注 1-添加关注
        params.put("type", info.getContentDetail().getRelationType().equals("0") ? 1 : 2);
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                JSONObject jo = JSONObject.parseObject(data);
                if (jo == null) return;
                switch (jo.getInteger("code")) {

                    case 0:
                        info.getContentDetail().setRelationType(info.getContentDetail().getRelationType().equals("0") ? "1" : "0");
                        player.getFollowText().setTextColor(Color.parseColor(info.getContentDetail().getRelationType().equals("0") ? "#ffffffff" : "#66ffffff"));
                        player.getFollowText().setText(info.getContentDetail().getRelationType().equals("0") ? "加关注" : "已关注");
                        break;

                    case 19983:
                    case 19981:
                    case -1:
                        ViewInject.shortToast(mContext, jo.getString("message"));
                        break;
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 收藏
     */
    public void Collection() {
        Map<String, Object> params = new HashMap<>();
        //type：5:文章, 6:视频, 7:视频模板, 8:项目, 9:商品，10:海报模板
        params.put("type", 6);
        params.put("relationId", info.getContentDetail().getId());
        params.put("customerId", AppConfig.CustomerId);
        apiImp.doCollection(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                JSONObject jo = JSON.parseObject(data);
                if (jo == null && jo.getInteger("code") != 0) return;
                ViewInject.CollectionToast(mContext, "已收藏");
                info.getContentDetail().setCollection(true);
                moreDialog.dismiss();
                initDialog();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                moreDialog.dismiss();
            }
        });
    }


    /**
     * 不感兴趣
     */
    private void notInterested() {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("type", "1");
        map.put("id", info.getContentDetail().getId());
        apiImp.postLoseInterest(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                moreDialog.dismiss();
                int position = getIntent().getIntExtra("VideoActivity.position", -1);
                if (position == -1) {
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (checkIsLife()) return;
            }
        });
    }

    /**
     * 删除
     */
    private void delectContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("type", info.getContentDetail().getContentType());
        map.put("id", info.getContentDetail().getId());
        apiImp.postDelectAction(map, this, new DataIdCallback<String>() {

            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                moreDialog.dismiss();
                int position = getIntent().getIntExtra("VideoActivity.position", -1);
                if (position == -1) {
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    /**
     * 检测是否还在栈内
     *
     * @return true：不在栈内，false：还在栈内
     */
    protected boolean checkIsLife() {
        return reference == null || reference.get() == null || reference.get().isFinishing();
    }
}
