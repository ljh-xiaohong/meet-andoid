package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ReportActivity;
import com.yuejian.meet.activities.message.MessageSettingActivity;
import com.yuejian.meet.activities.mine.InputActivity;
import com.yuejian.meet.activities.mine.MyDialogActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.CommentListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.ArticleContentEntity;
import com.yuejian.meet.bean.CommentBean;
import com.yuejian.meet.bean.MessageCommentBean;
import com.yuejian.meet.bean.PraiseEntity;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.bean.VideoAndContentEntiy;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.MoreDialog;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.load.GlideImageEngine;
import com.yuejian.meet.widgets.MyNestedScrollView;
import com.zhy.view.flowlayout.FlowLayout;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ArticleActivity extends BaseActivity {

    private String contentId = null;
    private String customerId = null;
    private VideoAndContentEntiy info;
    private CommentListAdapter commentAdapter;

    private boolean discussData = false;

    Intent intent;

    @Bind(R.id.activity_article_back)
    View back;

    @Bind(R.id.activity_article_more)
    View more;

    @Bind(R.id.activity_article_head_img)
    ImageView head;

    @Bind(R.id.activity_article_name_tv)
    TextView name;

    @Bind(R.id.activity_article_name_vip)
    ImageView vip;

    @Bind(R.id.activity_article_name_follow)
    TextView follow;

    @Bind(R.id.activity_article_discuss)
    TextView discuss;

    @Bind(R.id.activity_article_content)
    LinearLayout ll;

    @Bind(R.id.activity_article_recycleview)
    RecyclerView recyclerView;

    @Bind(R.id.activity_article_discuss_b)
    TextView discuss_b;

    @Bind(R.id.activity_activity_title)
    TextView title;

    @Bind(R.id.activity_activity_date)
    TextView date;

    @Bind(R.id.activity_activity_read)
    TextView read;

    @Bind(R.id.activity_article_shop_img)
    ImageView shop_img;

    @Bind(R.id.activity_article_shop_name)
    TextView shop_name;

    @Bind(R.id.activity_article_shop_disct)
    TextView shop_disct;

    @Bind(R.id.activity_article_shop_price_discount)
    TextView shop_discount;

    @Bind(R.id.activity_article_shop_price_full)
    TextView shop_price;

    @Bind(R.id.activity_article_shop_layout)
    View shop_layout;

    @Bind(R.id.activity_article_flowlayout)
    FlowLayout flowLayout;

    @Bind(R.id.activity_article_like)
    ImageView like_img;

    @Bind(R.id.activity_article_like_count)
    TextView like_count;

    @Bind(R.id.activity_article_share)
    ImageView share_img;

    @Bind(R.id.activity_article_myscrollview)
    MyNestedScrollView scrollView;

    @Bind(R.id.activity_article_entry)
    TextView entry;

    @Bind(R.id.activity_article_showMore)
    TextView showMoe;


    private MoreDialog moreDialog;

    private List<String> moreData;

    private List<ArticleContentEntity> contentEntities;

    private ArrayList<String> pics = new ArrayList<String>();

    int index;

    int mNextPageIndex = 1, pageCount = 10;

    private static final int DISCUSS = 120;

    private static final int DISCUSS_DISCUSS = 110;


    private String[] labelName, labelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        if (!getData()) return;
        initDiscussAdapter();
        getDataFromNet();
        scrollView.setonScrollChanged((v, l, t, oldl, oldt) -> {
            int[] local = new int[2];
            discuss.getLocationInWindow(local);

            if (local[1] - ScreenUtils.getStatusBarHeight(getBaseContext()) - discuss.getHeight() <= 0) {
                discuss_b.setVisibility(View.VISIBLE);
            } else {
                discuss_b.setVisibility(View.GONE);
            }
        });


    }

    /**
     * @param context
     * @param contentId  内容ID
     * @param customerId 用户ID
     */
    public static void startActivity(Context context, String contentId, String customerId) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("ArticleActivity.contentId", contentId);
        intent.putExtra("ArticleActivity.customerId", customerId);
        context.startActivity(intent);
    }

    /**
     * 主要用于删除不感兴趣，及删除视频
     *
     * @param context
     * @param contentId
     * @param customerId
     * @param position
     */
    public static void startActivityForResult(Activity context, String contentId, String customerId, int position, int requestCode) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("ArticleActivity.contentId", contentId);
        intent.putExtra("ArticleActivity.customerId", customerId);
        intent.putExtra("ArticleActivity.position", position);
        context.startActivityForResult(intent, requestCode);
    }

    private boolean getData() {
        contentId = getIntent().getStringExtra("ArticleActivity.contentId");
        customerId = getIntent().getStringExtra("ArticleActivity.customerId");
        return contentId != null && customerId != null;
    }

    private void initData() {
        if (info == null || info.getContentDetail() == null) return;

        VideoAndContentEntiy.ContentDetail detail = info.getContentDetail();
        Glide.with(mContext).load(checkData(detail.getUserPhoto())).into(head);
        name.setText(checkData(detail.getUserName()));
        vip.setVisibility(checkData(detail.getUserVipType()).equals("0") ? View.INVISIBLE : View.VISIBLE);
        like_img.setImageResource(checkData(detail.getIsPraise()).equals("0") ? R.mipmap.icon_home_zan_nor : R.mipmap.icon_home_zan_sel);
        like_count.setText(detail.getFabulousNum());
        follow.setText(checkData(detail.getRelationType()).equals("0") ? "关注" : "已关注");
        follow.setBackgroundResource(checkData(detail.getRelationType()).equals("0") ? R.drawable.shape_article_follow : R.drawable.shape_article_follow_w);
        title.setText(checkData(detail.getContentTitle()));
        date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(Long.valueOf(detail.getCreateTime())*1000)));
        discuss.setText(String.format("共%s条评论", checkData(detail.getCommentNum())));
        discuss_b.setText(String.format("共%s条评论", checkData(detail.getCommentNum())));
        read.setText(String.format("阅读 %s", checkData(detail.getViewNum())));
        if (!TextUtils.isEmpty(detail.getLabelName()) && detail.getLabelName().contains("#")) {
            labelName = detail.getLabelName().trim().substring(1, detail.getLabelName().length()).split("#");
            labelId = detail.getLabelId().split(",");
            if (labelName == null) return;
            for (int i = 0; i < labelName.length; i++) {
                TextView item = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_article_tag, null);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                item.setLayoutParams(params);
                item.setText(labelName[i]);
                item.setTag(labelId[i]);
                item.setOnClickListener(view -> {
                    ActivityLabActivity.startActivity(this, view.getTag() + "", AppConfig.CustomerId);
                });
                flowLayout.addView(item);
            }


        }

        if (detail.getShopList() != null) {
            VideoAndContentEntiy.shopList shopList = detail.getShopList();
            shop_layout.setVisibility(shopList.getShopId().length() > 0 ? View.VISIBLE : View.GONE);
            Glide.with(mContext).load(checkData(shopList.getGPhoto())).into(shop_img);
            shop_name.setText(checkData(shopList.getShopName()));
            shop_disct.setText(checkData(shopList.getGDes()));
            shop_discount.setText(checkData(shopList.getGPriceVip()));
            shop_price.setText(checkData(shopList.getGPriceOriginal()));
        }

        contentEntities = JSON.parseArray(checkData(detail.getCrContent()), ArticleContentEntity.class);
        if (contentEntities != null && contentEntities.size() > 0) {

            for (ArticleContentEntity entity : contentEntities) {
                switch (entity.getType()) {
                    case "image":
                        ll.addView(creatPictureView(entity.getContent(), mContext));
                        break;

                    case "text":
                        ll.addView(creatContentView(entity.getContent(), mContext));
                        break;

                    case "video":

                        break;
                }


            }
        }

        //评论
        if (info == null || info.getCommentList() == null) return;
        List<CommentBean.DataBean> discusses = info.getCommentList();
        if (discusses.size() >= pageCount) showMoe.setVisibility(View.VISIBLE);
        commentAdapter.refreshData(discusses);

    }
    /**
     * 初始化 评论列表 adapter
     */
    private void initDiscussAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        commentAdapter = new CommentListAdapter(this, new ArrayList<>(), MyDialogActivity.StyleType.NORMAL);
        commentAdapter.setChange(new CommentListAdapter.OnChange() {
            @Override
            public void click(View view,int postion,boolean isClick) {
                if (isClick){
                if (info.getCommentList() == null) return;
                InputActivity.startActivityForResult(ArticleActivity.this, info.getContentDetail().getId(), commentAdapter.getData().get(postion).getId() + "", commentAdapter.getData().get(postion).getName(), DISCUSS_DISCUSS);
                }else {
                    initPopwindow(view,postion);
                }
             }
        });
        recyclerView.setAdapter(commentAdapter);
    }

    private View popupView;
    private PopupWindow window;
    //复制删除弹窗
    private void initPopwindow(View view, int postion) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        popupView = inflater.inflate(R.layout.copy_and_delect_popupwindow, null);
        TextView copy=popupView.findViewById(R.id.copy);
        TextView delect=popupView.findViewById(R.id.delect);
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
//        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = popupView.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        window.showAtLocation(view, Gravity.NO_GRAVITY, location[0]/2, location[1] - measuredHeight);
        if (info.getCommentList().get(postion).getCustomerId()==Integer.parseInt(AppConfig.CustomerId)){
            delect.setVisibility(View.VISIBLE);
        }else {
            delect.setVisibility(View.GONE);
        }
        copy.setOnClickListener(v -> {
            ClipboardManager cmb = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(info.getCommentList().get(postion).getComment());
            Toast.makeText(this,"已复制",Toast.LENGTH_LONG).show();
        });
        delect.setOnClickListener(v -> delectComment(info.getCommentList().get(postion).getId()+"",postion));
    }
    //删除评论
    private void delectComment(String id,int position) {
            Map<String, Object> params = new HashMap<>();
            params.put("customerId", AppConfig.CustomerId);
            params.put("id",id);
            apiImp.getDelContentComment(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    MessageCommentBean bean = new Gson().fromJson(data, MessageCommentBean.class);
                    if (bean.getCode() != 0) {
                        ViewInject.shortToast(ArticleActivity.this, bean.getMessage());
                        return;
                    }
                    info.getCommentList().remove(position);
                    commentAdapter.refreshData(info.getCommentList());
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {
                    ViewInject.shortToast(ArticleActivity.this, errMsg);
                }
            });
    }

    private String checkData(String data) {
        if (TextUtils.isEmpty(data)) data = "";
        return data;

    }


    /**
     * 图片处理
     *
     * @param url
     * @param activity
     * @return
     */
    private View creatPictureView(String url, Context activity) {
        CardView cardView = new CardView(this);
        cardView.setRadius(ScreenUtil.dip2px(4));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = ScreenUtil.dip2px(28);
        cardView.setLayoutParams(lp);
        pics.add(url);
        int position = index++;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MNImageBrowser
                        .with(activity)
                        .setCurrentPosition(position)
                        .setImageEngine(new GlideImageEngine())
                        .setImageList(pics)
                        .show(view);
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int width = ScreenUtils.getScreenWidth(this) - (ScreenUtil.dip2px(12) * 2);
        int height = width / 5 * 3;
        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(width, height);
        imageView.setLayoutParams(ll);
        cardView.addView(imageView);

        Glide.with(this).load(url).into(imageView);

        return cardView;
    }


    /**
     * 文字处理
     *
     * @param content
     * @return
     */
    private View creatContentView(String content, Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(15);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setText(content);
        textView.setLineSpacing(0, 1.5f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = ScreenUtil.dip2px(28);
//        layoutParams.leftMargin = ScreenUtil.dip2px(12);
//        layoutParams.rightMargin = ScreenUtil.dip2px(12);
        textView.setLayoutParams(layoutParams);


        return textView;

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
                    initDialog();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
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
                        like_img.setImageResource(info.getContentDetail().getIsPraise().equals("1") ? R.mipmap.icon_home_zan_sel : R.mipmap.icon_home_zan_nor);
                        like_count.setText(count + "");
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
     * 收藏
     */
    public void Collection() {
        Map<String, Object> params = new HashMap<>();
        //type：5:文章, 6:视频, 7:视频模板, 8:项目, 9:商品，10:海报模板
        params.put("type", 5);
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
                if (checkIsLife()) return;
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
                int position = getIntent().getIntExtra("ArticleActivity.position", -1);
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
                int position = getIntent().getIntExtra("ArticleActivity.position", -1);
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
                        follow.setText(info.getContentDetail().getRelationType().equals("0") ? "关注" : "已关注");
                        follow.setBackgroundResource(info.getContentDetail().getRelationType().equals("0") ? R.drawable.shape_article_follow : R.drawable.shape_article_follow_w);
                        break;

                    case 19983:
                    case 19981:
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

    @OnClick({R.id.activity_article_like, R.id.activity_article_share, R.id.activity_article_name_follow, R.id.activity_article_more, R.id.activity_article_entry, R.id.activity_article_showMore, R.id.activity_article_back, R.id.activity_article_head_img, R.id.activity_article_name_tv, R.id.activity_article_shop_layout})
    @Override
    public void onClick(View v) {
        if (AppConfig.CustomerId == null && AppConfig.CustomerId.length() < 0) return;

        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_article_like:
                like();
                break;
            case R.id.activity_article_share:
                share();
                break;
            case R.id.activity_article_name_follow:
                AddFollow();
                break;
            case R.id.activity_article_more:
                moreDialog.show(getSupportFragmentManager(), "ArticleActivity.moreDialog");
                break;
            case R.id.activity_article_entry:
                InputActivity.startActivityForResult(this, info.getContentDetail().getId(), "", null, DISCUSS);
                break;
            case R.id.activity_article_showMore:
                switch (showMoe.getText().toString()) {

                    case "展开":

                        break;

                    case "收起":
                        mNextPageIndex = 1;
                        break;

                }
                getDiscuss();
                break;
            case R.id.activity_article_back:
                finish();
                break;
            case R.id.activity_article_head_img:
            case R.id.activity_article_name_tv:
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
                urlVip = String.format(urlVip + "?customerId=%s&opCustomerId=%s&phone=true", AppConfig.CustomerId, info.getContentDetail().getCustomerId());

                intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, urlVip);
                intent.putExtra("No_Title", true);
                startActivity(intent);
                break;
            case R.id.activity_article_shop_layout:
                if (info == null || info.getContentDetail() == null || info.getContentDetail().getShopList() == null)
                    return;
                String urlShop = "";
//                urlShop = String.format(UrlConstant.ExplainURL.SHOP_DETAIL + "?customerId=%s&gId=%s&phone=true", AppConfig.CustomerId, info.getContentDetail().getShopList().getShopId());
                urlShop = String.format("http://app2.yuejianchina.com/yuejian-app/personal_center/shop/item.html?customerId=%s&gId=%s&phone=true", AppConfig.CustomerId, info.getContentDetail().getShopList().getShopId());
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, urlShop);
                intent.putExtra("No_Title", true);
                startActivity(intent);
                break;
        }
    }

    private void getDiscuss() {
        Map<String, Object> params = new HashMap<>();
        params.put("myCustomerId", AppConfig.CustomerId);
        params.put("crId", contentId);
        params.put("pageIndex", String.valueOf(mNextPageIndex));
        params.put("pageItemCount", String.valueOf(pageCount));
        apiImp.getContentComments(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                CommentBean commentBean = JSON.parseObject(data, CommentBean.class);


                if (commentBean == null || commentBean.getCode() != 0) {
                    ViewInject.shortToast(mContext, commentBean.getMessage());
                    return;
                }
                //为空时，不处理
                if (commentBean.getData() == null) {
                    showMoe.setText("收起");
                    return;
                }

                //评论为空时，不显示
                if (commentBean.getData().size() < pageCount) {
                    showMoe.setText("收起");
                } else {
                    showMoe.setText("展开");
                }


                if (mNextPageIndex == 1) {
                    commentAdapter.refreshData(commentBean.getData());
                } else {
                    commentAdapter.loadMoreData(commentBean.getData());
                }
                if (discussData) {
                    int count = Integer.valueOf(info.getContentDetail().getCommentNum());

                    info.getContentDetail().setCommentNum(++count + "");

                    discuss.setText(String.format("共%s条评论", count));

                    discuss_b.setText(String.format("共%s条评论", count));

                    discussData = false;
                }

                mNextPageIndex++;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (checkIsLife()) return;
                ViewInject.shortToast(mContext, errMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //评论
            case DISCUSS:
                if (resultCode == RESULT_OK) {
                    mNextPageIndex = 1;
                    discussData = true;
                    getDiscuss();
                }


                break;

            //评论评论
            case DISCUSS_DISCUSS:
                if (resultCode == RESULT_OK) {
                    mNextPageIndex = 1;
                    discussData = true;
                    getDiscuss();
                }
                break;


        }

    }
}
