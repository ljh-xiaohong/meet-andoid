package com.yuejian.meet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ReplyMoreActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.bean.ReplyListEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/13.
 * 所有的回复adpter
 */

public class ReplyMoreAdapter extends FKAdapter<ReplyListEntity> {
    private AdapterHolder mHelper;
    private Context context;
    ApiImp api = new ApiImp();


    public ReplyMoreAdapter(AbsListView view, List<ReplyListEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, ReplyListEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(final AdapterHolder helper, final ReplyListEntity item, final int position) {
        this.mHelper = helper;
        mHelper.setText(R.id.txt_action_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.txt_action_city, item.getFamily_area());
        mHelper.setText(R.id.txt_action_job, " " + item.getJob());
        mHelper.getView(R.id.txt_action_age).setSelected(!item.getSex().equals("0"));
        mHelper.setText(R.id.txt_action_age, " " + item.getAge());
        mHelper.setText(R.id.txt_action_time, StringUtils.friendlyTime(item.comment_time));
        mHelper.setText(R.id.messag_content, item.isArticleReply ? item.article_comment_content : item.comment_content);
        mHelper.getView(R.id.mes_reply).setVisibility(StringUtils.isEmpty(item.getOp_surname()) ? View.GONE : View.VISIBLE);
        mHelper.getView(R.id.mes_opposite_name).setVisibility(StringUtils.isEmpty(item.getOp_surname()) ? View.GONE : View.VISIBLE);
        mHelper.getView(R.id.txt_reply_partition).setVisibility(StringUtils.isEmpty(item.getOp_surname()) ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.mes_opposite_name, item.getOp_surname() + item.getOp_name());
        String praiseCnt = StringUtils.isNotEmpty(item.article_comment_praise_cnt)?item.article_comment_praise_cnt : item.comment_praise_cnt;
        mHelper.getView(R.id.actioninfo_like_praise_count).setSelected(!praiseCnt.equals("0"));
        mHelper.setText(R.id.actioninfo_like_praise_count, " " + praiseCnt);
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_action_header));
        ImageView imageView=(ImageView)mHelper.getView(R.id.img_action_reply_sponsor);
        imageView.setVisibility(View.GONE);
        if (item.getIs_super()>0){
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.getIs_super()==FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).into(imageView);
        }
        mHelper.getView(R.id.actioninfo_like_praise_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                Map<String, Object> params = new HashMap<>();
                if (item.isArticleReply) {
                    params.put("article_comment_id", item.article_comment_id);
                } else {
                    params.put("comment_id", item.getComment_id());
                }
                params.put("customer_id", AppConfig.CustomerId);
                DataIdCallback<String> dataIdCallback = new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        PraiseState state = JSON.parseObject(data, PraiseState.class);
                        if (state != null) {
                            if (item.isArticleReply) {
                                item.article_comment_praise_cnt = state.praise_cnt;
                                helper.setText(R.id.actioninfo_like_praise_count, " " + item.getArticle_comment_praise_cnt());
                            } else {
                                item.comment_praise_cnt = state.praise_cnt;
                                helper.setText(R.id.actioninfo_like_praise_count, item.getComment_praise_cnt());
                            }
                            item.setIs_praise(state.is_praise);
                            helper.getView(R.id.actioninfo_like_praise_count).setSelected("1".equals(state.is_praise));
                        }
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                };
                if (item.isArticleReply) {
                    api.getArticleCommentReplyPraise(params, this, dataIdCallback);
                } else {
                    api.commentPraise(params, this, dataIdCallback);
                }
            }
        });

        helper.setClickListener(R.id.reply_content_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ReplyMoreActivity) {
                    ReplyMoreActivity activity = (ReplyMoreActivity) context;
                    String commentId = item.isArticleReply ? item.article_comment_id : item.comment_id;
//                    activity.requestInput(false, "回复@" + item.surname + item.name, commentId);
                    activity.showBottomPopupWindow(StringUtils.isNotEmpty(item.getComment_content())?item.getComment_content():item.getArticle_comment_content(),item.getCustomer_id(),commentId, false, "回复@" + item.surname + item.name);
                }
            }
        });
    }
}
