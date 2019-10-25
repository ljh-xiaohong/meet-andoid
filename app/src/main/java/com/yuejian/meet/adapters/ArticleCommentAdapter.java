package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.InnerListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh02 on 2017/8/26.
 */

public class ArticleCommentAdapter extends FKAdapter<CommentsEntity> {

    private AdapterHolder mHelper;
    private Context context;
    ApiImp api = new ApiImp();


    public ArticleCommentAdapter(AbsListView view, List<CommentsEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, CommentsEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final CommentsEntity item, final int position) {
        this.mHelper = helper;
        mHelper.setText(R.id.txt_action_time, StringUtils.friendlyTime(item.article_comment_time));
        mHelper.setText(R.id.txt_action_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.txt_action_city, item.getFamily_area());
        mHelper.setText(R.id.txt_action_age, " " + item.getAge());
        mHelper.getView(R.id.txt_action_age).setSelected("1".equals(item.getSex()));
        int sexIcon = 0;
        if ("1".equals(item.getSex())) {
            sexIcon = R.mipmap.ic_man;
        } else {
            sexIcon = R.mipmap.ic_woman;
        }
        TextView age = mHelper.getView(R.id.txt_action_age);
        Drawable sexDrawable = context.getResources().getDrawable(sexIcon);
        sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
        age.setCompoundDrawables(sexDrawable, null, null, null);
        mHelper.setText(R.id.txt_action_job, item.getJob());
        mHelper.setText(R.id.txt_item_actioninfo_content, item.article_comment_content);
        mHelper.setText(R.id.actioninfo_like_praise_count, " " + item.article_comment_praise_cnt);
        mHelper.getView(R.id.actioninfo_like_praise_count).setSelected(!"0".equals(item.getIs_praise()));
        Glide.with(context).load(item.photo).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_action_header));
        ImageView fqr = mHelper.getView(R.id.img_action_sponsor);
        fqr.setVisibility(View.GONE);
        if (item.getIs_super()>0){
            fqr.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(fqr);
        }
        mHelper.getView(R.id.rely_cnt).setVisibility(View.GONE);
        if (Integer.parseInt(item.article_reply_cnt) > 2) {
            mHelper.getView(R.id.rely_cnt).setVisibility(View.VISIBLE);
            mHelper.setText(R.id.rely_cnt, "共" + item.article_reply_cnt + "条回复 >");
        }
        mHelper.getView(R.id.rely_cnt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isArticleComment = true;
                Intent intent = new Intent(context, ReplyMoreActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("commentsEntity", item);
                mBundle.putString("titlName", item.article_reply_cnt);
                mBundle.putString("action_id", item.article_comment_id);
                intent.putExtras(mBundle);
                ((ArticleInfoActivity)context).startActivityForResult(intent,12);

            }
        });
        mHelper.getView(R.id.actioninfo_like_praise_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                Map<String, Object> params = new HashMap<>();
                params.put("article_comment_id", item.article_comment_id);
                params.put("customer_id", AppConfig.CustomerId);
                api.getArticleCommentReplyPraise(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        PraiseState state = JSON.parseObject(data, PraiseState.class);
                        if (state != null) {
                            item.article_comment_praise_cnt = state.praise_cnt;
                            item.setIs_praise(state.is_praise);
                            helper.getView(R.id.actioninfo_like_praise_count).setSelected("1".equals(state.is_praise));
                            helper.setText(R.id.actioninfo_like_praise_count, item.article_comment_praise_cnt);
                        }
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
            }
        });

        mHelper.setClickListener(R.id.img_action_header, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });
        mHelper.getView(R.id.action_info_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ArticleInfoActivity) context).showBottomPopupWindow(item.getArticle_comment_content(),item.getCustomer_id(),item.getArticle_comment_id(), item.getCustomer_id().equals(AppConfig.CustomerId), item.getSurname()+item.getName(), position);
            }
        });

        mHelper.getView(R.id.actioninfo_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ArticleInfoActivity) context).showBottomPopupWindow(item.getArticle_comment_content(),item.getCustomer_id(),item.getArticle_comment_id(), item.getCustomer_id().equals(AppConfig.CustomerId), item.getSurname()+item.getName(), position);
            }
        });
        final List<ReplyListEntity> replyListEntities = JSON.parseArray(item.getReply_list(), ReplyListEntity.class);
        InnerListView listView = mHelper.getView(R.id.lt_action_info_list);
        listView.setVisibility(View.GONE);
        if (replyListEntities.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            Reply mAdapter = new Reply(listView, replyListEntities, R.layout.item_action_info_message_layout);
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    ((ArticleInfoActivity) context).showBottomPopupWindow(replyListEntities.get(position2).getArticle_comment_content(),replyListEntities.get(position2).getCustomer_id(),replyListEntities.get(position2).getArticle_comment_id(), false, replyListEntities.get(position2).getSurname() + replyListEntities.get(position2).getName(), position);
//                    ((ArticleInfoActivity) context).commentMsg(replyListEntities.get(position2).article_comment_id, false, replyListEntities.get(position2).getSurname() + replyListEntities.get(position2).getName(), position);
                }
            });
        }
    }

    //评论
    public class Reply extends FKAdapter<ReplyListEntity> {

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
                Spannable string = new SpannableString(replyer +  "：" + item.article_comment_content);
                string.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), 0, replyer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.meg_me_name, string);
            }
        }
    }
}
