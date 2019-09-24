package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ReportActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ActivityLabEntity;
import com.yuejian.meet.bean.ArticleContentEntity;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.bean.VideoAndContentEntiy;
import com.yuejian.meet.dialogs.MoreDialog;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.TextUtil;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.load.GlideImageEngine;
import com.zhy.view.flowlayout.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ArticleActivity extends BaseActivity {

    private String contentId = null;
    private String customerId = null;
    private VideoAndContentEntiy info;

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

    private MoreDialog moreDialog;

    private List<String> moreData;

    private List<ArticleContentEntity> contentEntities;

    private ArrayList<String> pics = new ArrayList<String>();

    int index;

    private String[] labelName, labelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        if (!getData()) return;
        getDataFromNet();
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

    private boolean getData() {
        contentId = getIntent().getStringExtra("ArticleActivity.contentId");
        customerId = getIntent().getStringExtra("ArticleActivity.customerId");
        return contentId != null && customerId != null;
    }

    private void initData() {
        Glide.with(mContext).load(info.getContentDetail().getUserPhoto()).into(head);
        name.setText(info.getContentDetail().getUserName());
        vip.setVisibility(info.getContentDetail().getUserVipType().equals("0") ? View.INVISIBLE : View.VISIBLE);
        follow.setText(info.getContentDetail().getRelationType().equals("0") ? "关注" : "已关注");
        follow.setBackgroundResource(info.getContentDetail().getRelationType().equals("0") ? R.drawable.shape_article_follow : R.drawable.shape_article_follow_w);
        title.setText(info.getContentDetail().getContentTitle());
        date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(Long.valueOf(info.getContentDetail().getCreateTime()))));
        discuss.setText(String.format("共%s条评论", info.getContentDetail().getCommentNum()));
        discuss_b.setText(String.format("共%s条评论", info.getContentDetail().getCommentNum()));
        read.setText(String.format("阅读 %s", info.getContentDetail().getViewNum()));
        if (!TextUtils.isEmpty(info.getContentDetail().getLabelName()) && info.getContentDetail().getLabelName().contains("#")) {
            labelName = info.getContentDetail().getLabelName().trim().substring(1, info.getContentDetail().getLabelName().length()).split("#");
            labelId = info.getContentDetail().getLabelId().split(",");
            if (labelName == null) return;
            for (int i = 0; i < labelName.length; i++) {
                TextView item = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_article_tag, null);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                item.setLayoutParams(params);
                item.setText(labelName[i]);
                item.setTag(labelId[i]);
                item.setOnClickListener(view -> {
                    AcitivityLabActivity.startActivity(this, view.getTag() + "", AppConfig.CustomerId);
                });
                flowLayout.addView(item);
            }


        }

        if (info.getContentDetail().getShopList() != null) {
            shop_layout.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(info.getContentDetail().getShopList().getGPhoto()).into(shop_img);
            shop_name.setText(info.getContentDetail().getShopList().getShopName());
            shop_disct.setText(info.getContentDetail().getShopList().getGDes());
            shop_discount.setText(info.getContentDetail().getShopList().getGPriceVip());
            shop_price.setText(info.getContentDetail().getShopList().getGPriceOriginal());
        }

        contentEntities = JSON.parseArray(info.getContentDetail().getCrContent(), ArticleContentEntity.class);
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
                parseJSON(data);
                if (info == null) return;
                initData();
                if (AppConfig.CustomerId != null && AppConfig.CustomerId.length() > 0) {
                    initDialog();
                    initListener();
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
            moreData.add("编辑");
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
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                moreDialog.dismiss();
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
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                moreDialog.dismiss();
                finish();
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

    private void parseJSON(String data) {

        try {
            if (TextUtils.isEmpty(data)) return;
            JSONObject jo = JSON.parseObject(data);
            if (null == jo && !jo.getString("code").equals("0")) return;
            JSONObject in = jo.getJSONObject("data");
            info = new VideoAndContentEntiy();

            if (!TextUtils.isEmpty(in.getString("commentList")) && !in.getString("commentList").equals("null")) {
                info.setCommentList(JSON.parseArray(in.getString("commentList"), VideoAndContentEntiy.CommentList.class));
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
                info.setContentDetail(contentDetail);
            }

        } catch (NullPointerException e) {

        }

    }

    private void initListener() {
        more.setOnClickListener(view -> {

            moreDialog.show(getSupportFragmentManager(), "ArticleActivity.moreDialog");
        });

        follow.setOnClickListener(view -> {
            AddFollow();
        });
    }
}
