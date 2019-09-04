package com.yuejian.meet.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.activities.home.ReplyMoreActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentsEntity;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.bean.ReplyListEntity;
import com.yuejian.meet.widgets.InnerListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/29/029.
 */

public class ArticleCommentFactory {

    public static View createItem(final Context context, final CommentsEntity item, final int position) {
        View containView = View.inflate(context, R.layout.item_action_info_layout, null);
        TextView articleTime = (TextView) containView.findViewById(R.id.txt_action_time);
        articleTime.setText(StringUtils.friendlyTime(item.article_comment_time));
        TextView articleName = (TextView) containView.findViewById(R.id.txt_action_name);
        articleName.setText(item.getSurname() + item.getName());
        TextView articleCity = (TextView) containView.findViewById(R.id.txt_action_city);
        articleCity.setText(item.getFamily_area());
        TextView articleAge = (TextView) containView.findViewById(R.id.txt_action_age);
        articleAge.setText(" " + item.getAge());
        articleAge.setSelected("1".equals(item.getSex()));
        int sexIcon = 0;
        if ("1".equals(item.getSex())) {
            sexIcon = R.mipmap.ic_man;
        } else {
            sexIcon = R.mipmap.ic_woman;
        }
        Drawable sexDrawable = context.getResources().getDrawable(sexIcon);
        sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
        articleAge.setCompoundDrawables(sexDrawable, null, null, null);

        TextView articleJob = (TextView) containView.findViewById(R.id.txt_action_job);
        articleJob.setText(item.getJob());

        TextView commentContent = (TextView) containView.findViewById(R.id.txt_item_actioninfo_content);
        commentContent.setText(item.article_comment_content);

        final TextView commentPraiseCnt = (TextView) containView.findViewById(R.id.actioninfo_like_praise_count);
        commentPraiseCnt.setText(" " + item.article_comment_praise_cnt);
        commentPraiseCnt.setSelected(!"0".equals(item.getIs_praise()));

        Glide.with(context).load(item.photo).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) containView.findViewById(R.id.img_action_header));
        ImageView fqr = (ImageView) containView.findViewById(R.id.img_action_sponsor);
        fqr.setVisibility(View.GONE);
        if (item.getIs_super() > 0) {
            fqr.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : item.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(fqr);
        }
        TextView more = (TextView) containView.findViewById(R.id.rely_cnt);
        more.setVisibility(View.GONE);
        if (Integer.parseInt(item.article_reply_cnt) > 2) {
            more.setVisibility(View.VISIBLE);
            more.setText("共" + item.article_reply_cnt + "条回复 >");
        }
        more.setClickable(true);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isArticleComment = true;
                Intent intent = new Intent(context, ReplyMoreActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("commentsEntity", item);
                mBundle.putString("titlName", item.article_reply_cnt);
                mBundle.putString("action_id", item.article_comment_id);
                intent.putExtras(mBundle);
                ((ArticleInfoActivity) context).startActivityForResult(intent, 12);

            }
        });
        commentPraiseCnt.setClickable(true);
        commentPraiseCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                Map<String, Object> params = new HashMap<>();
                params.put("article_comment_id", item.article_comment_id);
                params.put("customer_id", AppConfig.CustomerId);
                new ApiImp().getArticleCommentReplyPraise(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        PraiseState state = JSON.parseObject(data, PraiseState.class);
                        if (state != null) {
                            item.article_comment_praise_cnt = state.praise_cnt;
                            item.setIs_praise(state.is_praise);
                            commentPraiseCnt.setSelected("1".equals(state.is_praise));
                            commentPraiseCnt.setText(item.article_comment_praise_cnt);
                        }
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
            }
        });

        ImageView headerIcon = (ImageView) containView.findViewById(R.id.img_action_header);
        headerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(context, item.customer_id);
            }
        });
        containView.findViewById(R.id.action_info_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ArticleInfoActivity) context).showBottomPopupWindow(item.getArticle_comment_content(), item.getCustomer_id(), item.getArticle_comment_id(), item.getCustomer_id().equals(AppConfig.CustomerId), "", position);
            }
        });

        containView.findViewById(R.id.actioninfo_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ArticleInfoActivity) context).showBottomPopupWindow(item.getArticle_comment_content(), item.getCustomer_id(), item.getArticle_comment_id(), item.getCustomer_id().equals(AppConfig.CustomerId), "", position);
            }
        });
        final List<ReplyListEntity> replyListEntities = JSON.parseArray(item.getReply_list(), ReplyListEntity.class);
        InnerListView listView = (InnerListView) containView.findViewById(R.id.lt_action_info_list);
        listView.setVisibility(View.GONE);
        if (replyListEntities.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            Reply mAdapter = new Reply(listView, replyListEntities, R.layout.item_action_info_message_layout);
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    ((ArticleInfoActivity) context).showBottomPopupWindow(replyListEntities.get(position2).getArticle_comment_content(), replyListEntities.get(position2).getCustomer_id(), replyListEntities.get(position2).getArticle_comment_id(), false, replyListEntities.get(position2).getSurname() + replyListEntities.get(position2).getName(), position);
                }
            });
        }
        return containView;
    }


    //评论
    public static class Reply extends FKAdapter<ReplyListEntity> {

        public Reply(AbsListView view, List<ReplyListEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        @Override
        public void convert(AdapterHolder helper, ReplyListEntity item, boolean isScrolling, int position) {
            super.convert(helper, item, isScrolling, position);
            String replyer = item.getSurname() + item.getName();
            String opName = item.getOp_surname() + item.getOp_name();
            if (StringUtils.isNotEmpty(opName)) {
                Spannable string = new SpannableString(replyer + "回复" + opName + "：" + item.article_comment_content);
                string.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), 0, replyer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                string.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), replyer.length() + 2, replyer.length() + 2 + opName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.meg_me_name, string);
            } else {
                Spannable string = new SpannableString(replyer + "：" + item.article_comment_content);
                string.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), 0, replyer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.meg_me_name, string);
            }
        }
    }

}
