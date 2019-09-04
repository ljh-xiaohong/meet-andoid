package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ActionInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ActionMessageEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 动态被评论(1, "动态被评论"), 动态被点赞(2, "动态被点赞"), 评论被点赞(3, "评论被点赞"), 评论被回复(4, "评论被回复"),动态被打赏(5, "动态被打赏")
 * Created by zh03 on 2017/6/13.
 * 动态未读消息adpter
 */

public class ActionMessageAdapter extends FKAdapter<ActionMessageEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public ActionMessageAdapter(AbsListView view, List<ActionMessageEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, ActionMessageEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final ActionMessageEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.action_mes_name,item.getSurname()+item.getName());
        mHelper.getView(R.id.action_msg_content_photo).setVisibility(StringUtils.isEmpty(item.getFirst_figure_url())?View.GONE:View.VISIBLE);
        mHelper.getView(R.id.action_msg_content).setVisibility(StringUtils.isEmpty(item.getFirst_figure_url())?View.VISIBLE:View.GONE);
        mHelper.setText(R.id.action_msg_content,item.getFirst_content());
        mHelper.getView(R.id.action_msg_comment).setVisibility(View.GONE);
        mHelper.getView(R.id.action_msg_like).setVisibility(View.GONE);
        if (item.getMsg_type().equals("1")||item.getMsg_type().equals("4")){//评论
            mHelper.getView(R.id.action_msg_comment).setVisibility(View.VISIBLE);
        }else if (item.getMsg_type().equals("2")||item.getMsg_type().equals("3")){//点赞
            mHelper.setText(R.id.action_mes_name,item.getSurname()+item.getName()+item.getTitle());
            mHelper.setText(R.id.action_mes_name,item.getSurname()+item.getName());
            mHelper.getView(R.id.action_msg_like).setVisibility(View.VISIBLE);
            ((ImageView)mHelper.getView(R.id.action_msg_like)).setImageResource(R.mipmap.ic_like_sel);
        }else if (item.getMsg_type().equals("5")){//礼物
            mHelper.setText(R.id.action_mes_name,item.getSurname()+item.getName()+item.getTitle());
            mHelper.getView(R.id.action_msg_like).setVisibility(View.VISIBLE);
            ((ImageView)mHelper.getView(R.id.action_msg_like)).setImageResource(R.mipmap.ic_gift_sel);
        }
        mHelper.getView(R.id.action_msg_comment).setVisibility((item.getMsg_type().equals("1")||item.getMsg_type().equals("4"))?View.VISIBLE:View.GONE);
        mHelper.getView(R.id.action_msg_like).setVisibility((item.getMsg_type().equals("1")||item.getMsg_type().equals("4"))?View.GONE:View.VISIBLE);
        mHelper.setText(R.id.action_msg_comment,item.getRemark());

        long time=Long.parseLong(item.getCreate_time());
        SimpleDateFormat format=new SimpleDateFormat("MM.dd HH:mm");
        Date d1=new Date(time);
        mHelper.setText(R.id.action_msg_time,format.format(d1));

        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.action_msg_photo));
        Glide.with(context).load(item.getFirst_figure_url()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.action_msg_content_photo));
        helper.getView(R.id.action_msg_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });
        helper.getView(R.id.action_message_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActionInfoActivity.class);
                intent.putExtra("action_id", item.getAction_id());
                context.startActivity(intent);
            }
        });
    }
}
