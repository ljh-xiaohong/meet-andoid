package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.GlideUtils;
import com.zhy.view.flowlayout.FlowLayout;

import org.xmlpull.v1.XmlPullParser;

public class RecommendView extends LinearLayout {

    public View article, article_person_info, video_vertical, video_vertical_person_info, video_horizontal, video_horizontal_person_info, mould, activity, poster, draft;

    public ImageView article_img, article_head_img, video_vertical_img, video_vertical_head_img, video_horizontal_blur, video_horizontal_head_img, video_horizontal_video, mould_img, activity_img_blur, poster_img;

    public TextView article_content, article_tag, article_like, article_name,
            video_vertical_content, video_vertical_tag, video_vertical_like, video_vertical_name,
            video_horizontal_content, video_horizontal_tag, video_horizontal_like, video_horizontal_name,
            mould_content,
            activity_title,
            poster_content;


    public LinearLayout mould_tag_layout;

    public FlowLayout poster_tag;

    public PictureLayout mould_pictureLayout, activity_picturelayout;

    public AttributeSet attributes;

    public RecommendView(Context context) {
        this(context, null);
    }

    public RecommendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecommendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initViews();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_recommendview, this);
        XmlPullParser parser = getResources().getLayout(R.layout.textview_tag);
        attributes = Xml.asAttributeSet(parser);

    }


    private void initViews() {
        article = this.findViewById(R.id.item_recommend_article_layout);
        article_person_info = this.findViewById(R.id.item_recommend_article_person_info);
        video_vertical = this.findViewById(R.id.item_recommend_videos_vertical_layout);
        video_vertical_person_info = this.findViewById(R.id.item_recommend_videos_vertical_person_info);
        video_horizontal = this.findViewById(R.id.item_recommend_video_horizontal_layout);
        video_horizontal_person_info = this.findViewById(R.id.item_recommend_videos_horizontal_person_info);
        mould = this.findViewById(R.id.item_recommend_mould_layout);
        activity = this.findViewById(R.id.item_recommend_activity_layout);
        poster = this.findViewById(R.id.item_recommend_poster_layout);

        //文章
        article_img = this.findViewById(R.id.item_recommend_article_img);
        article_content = this.findViewById(R.id.item_recommend_article_content);
        article_tag = this.findViewById(R.id.item_recommend_article_tag);
        article_like = this.findViewById(R.id.item_recommend_article_like);
        article_head_img = this.findViewById(R.id.item_recommend_article_head_img);
        article_name = this.findViewById(R.id.item_recommend_article_name_tv);

        //视频（竖屏）
        video_vertical_img = this.findViewById(R.id.item_recommend_video_vertical_img);
        video_vertical_content = this.findViewById(R.id.item_recommend_video_vertical_content);
        video_vertical_tag = this.findViewById(R.id.item_recommend_video_vertical_tag);
        video_vertical_like = this.findViewById(R.id.item_recommend_video_vertical_like);
        video_vertical_head_img = this.findViewById(R.id.item_recommend_videos_vertical_head_img);
        video_vertical_name = this.findViewById(R.id.item_recommend_videos_vertical_name_tv);

        //视频（横屏）
        //设置背景
        video_horizontal_blur = this.findViewById(R.id.item_recommend_video_horizontal_blur);
        video_horizontal_video = this.findViewById(R.id.item_recommend_video_horizontal_video);
        video_horizontal_content = this.findViewById(R.id.item_recommend_video_horizontal_content);
        video_horizontal_tag = this.findViewById(R.id.item_recommend_video_horizontal_tag);
        video_horizontal_like = this.findViewById(R.id.item_recommend_video_horizontal_like);
        video_horizontal_head_img = this.findViewById(R.id.item_recommend_video_horizontal_head_img);
        video_horizontal_name = this.findViewById(R.id.item_recommend_video_horizontal_name_tv);

        //模板
        mould_img = this.findViewById(R.id.item_recommend_mould_img);
        mould_content = this.findViewById(R.id.item_recommend_mould_content);
        mould_tag_layout = this.findViewById(R.id.item_recommend_mould_tag_layout);
        mould_pictureLayout = this.findViewById(R.id.item_recommend_mould_picturelayout);

        //活动
        activity_title = this.findViewById(R.id.item_recommend_activity_title);
        activity_picturelayout = this.findViewById(R.id.item_recommend_activity_picturelayout);
        activity_img_blur = this.findViewById(R.id.item_recommend_activity_img_blur);

        //海报
        poster_img = this.findViewById(R.id.item_recommend_poster_img);
        poster_content = this.findViewById(R.id.item_recommend_poster_content);
        poster_tag = this.findViewById(R.id.item_recommend_poster_flowLayout);

        //草稿
        draft = this.findViewById(R.id.item_recommend_draft_layout);

    }

    public enum ViewType {
        NONE, ARTICLE, VIDEO_VERTICAL, VIDEO_HORIZONTAL, MOULD, ACTIVITY, POSTER, DRAFT
    }


    private void setVisibilityStatus(View view, int VISIBLE_or_Not) {

        if (view.getVisibility() != VISIBLE_or_Not) {
            view.setVisibility(VISIBLE_or_Not);
        }

    }

    public void setPersonInfo(ViewType type, int sex, String head, String name) {
        if (name.length() > 5) {
            name = name.substring(0, 5);
        }

        switch (type) {

            case VIDEO_HORIZONTAL:
//                if (!isShow) video_horizontal_person_info.setVisibility(GONE);
                video_horizontal_person_info.setVisibility(VISIBLE);
                GlideUtils.display(video_horizontal_head_img,head);
//                Glide.with(getContext()).load(head).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).fallback(R.mipmap.ic_default).into(article_head_img);
                video_horizontal_name.setText(name);
                video_horizontal_name.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(sex == 0 ? R.mipmap.icon_tab_woman : R.mipmap.icon_tab_man), null);
                break;

            case VIDEO_VERTICAL:
//                if (!isShow) video_vertical_person_info.setVisibility(GONE);
                video_vertical_person_info.setVisibility(VISIBLE);
                GlideUtils.display(video_vertical_head_img,head);
//                Glide.with(getContext()).load(head).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).fallback(R.mipmap.ic_default).into(article_head_img);
                video_vertical_name.setText(name);
                video_vertical_name.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(sex == 0 ? R.mipmap.icon_tab_woman : R.mipmap.icon_tab_man), null);
                break;

            case ARTICLE:
//                if (!isShow) article_person_info.setVisibility(GONE);
                article_person_info.setVisibility(VISIBLE);
                GlideUtils.display(article_head_img,head);
//                Glide.with(getContext()).load(head).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).fallback(R.mipmap.ic_default).into(article_head_img);
                article_name.setText(name);
                article_name.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(sex == 0 ? R.mipmap.icon_tab_woman : R.mipmap.icon_tab_man), null);
                break;


        }
    }


    public void setLike(ViewType type, boolean like, String count) {
        switch (type) {

            case VIDEO_HORIZONTAL:
                setVideoHorizontalLike(like, count);
                break;

            case VIDEO_VERTICAL:
                setVideoVerticalLike(like, count);
                break;

            case ARTICLE:
                setArticleLike(like, count);
                break;


        }
    }

    public void setArticleLike(boolean like, String count) {
        article_like.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(like ? R.mipmap.icon_zan_sel : R.mipmap.icon_zan_nor),
                null,
                null,
                null);
        article_like.setText(CommonUtil.changeNum(count));
    }

    public void setVideoVerticalLike(boolean like, String count) {
        video_vertical_like.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(like ? R.mipmap.icon_zan_sel : R.mipmap.icon_zan_nor_w),
                null,
                null,
                null);
        video_vertical_like.setText(CommonUtil.changeNum(count));
    }

    public void setVideoHorizontalLike(boolean like, String count) {
        video_horizontal_like.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(like ? R.mipmap.icon_zan_sel : R.mipmap.icon_zan_nor_w),
                null,
                null,
                null);
        video_horizontal_like.setText(CommonUtil.changeNum(count));
    }

    /**
     * 设置显示的视图，并给与高度
     *
     * @param type
     * @param height
     */
    public void setViewStatus(ViewType type, int height) {

        switch (type) {
            //文章
            case ARTICLE:
                setVisibilityStatus(article, VISIBLE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                article.getLayoutParams().height = height;
                break;

            //视屏竖屏
            case VIDEO_VERTICAL:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, VISIBLE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                video_vertical.getLayoutParams().height = height;
                break;

            //视屏横屏
            case VIDEO_HORIZONTAL:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, VISIBLE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                video_horizontal.getLayoutParams().height = height;
                break;

            //模板
            case MOULD:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, VISIBLE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                mould.getLayoutParams().height = height;
                break;

            //活动
            case ACTIVITY:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, VISIBLE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                activity.getLayoutParams().height = height;
                break;

            case POSTER:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, VISIBLE);
                setVisibilityStatus(draft, GONE);
                poster.getLayoutParams().height = height;
                break;

            case DRAFT:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, VISIBLE);
                draft.getLayoutParams().height = height;
                break;

            case NONE:
                setVisibilityStatus(article, GONE);
                setVisibilityStatus(video_vertical, GONE);
                setVisibilityStatus(video_horizontal, GONE);
                setVisibilityStatus(mould, GONE);
                setVisibilityStatus(activity, GONE);
                setVisibilityStatus(poster, GONE);
                setVisibilityStatus(draft, GONE);
                break;

        }


    }

}

