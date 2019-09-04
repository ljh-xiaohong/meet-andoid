package com.netease.nim.uikit.session.viewholder;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.MessageExtendEntity;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderText extends MsgViewHolderBase {
    TextView bodyTextView;
    ImageView gold_left,gold_right;


    public MsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
        layoutDirection();

        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
        if (message.getSessionType()== SessionTypeEnum.Team&&message.getFromAccount().equals(AppConfig.sponsorId)){
            NimUserInfo userInfo = getUserInfo();
            if (userInfo==null){
                setDefault();
            }else{
                MessageExtendEntity extendEntity= JSON.parseObject(userInfo.getExtension(),MessageExtendEntity.class);
                if (extendEntity!=null){
                    if (extendEntity.getIs_family_master()>0){
                        bodyTextView.setTextColor(Color.parseColor("#aa8245"));
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void layoutDirection() {
        bodyTextView = findViewById(R.id.nim_message_item_text_body);
        gold_left=findViewById(R.id.gold_left);
        gold_right=findViewById(R.id.gold_right);
        gold_left.setVisibility(View.GONE);
        gold_right.setVisibility(View.GONE);
        if (message.getSessionType()== SessionTypeEnum.Team&&message.getFromAccount().equals(AppConfig.sponsorId)){
//            UserInfoProvider.UserInfo userInfo = getUserInfo();
            NimUserInfo userInfo = getUserInfo();
            if (userInfo==null){
                setDefault();
            }else {
                MessageExtendEntity extendEntity= JSON.parseObject(userInfo.getExtension(),MessageExtendEntity.class);
                if (extendEntity!=null){
                    if (extendEntity.getIs_family_master()>0){
                        if (isReceivedMessage()) {
                            gold_left.setVisibility(View.VISIBLE);
                            bodyTextView.setBackgroundResource(R.drawable.nim_message_left_master_bg);
                            bodyTextView.setPadding(ScreenUtil.dip2px(25), ScreenUtil.dip2px(10), ScreenUtil.dip2px(10), ScreenUtil.dip2px(10));
                        } else {
                            gold_right.setVisibility(View.VISIBLE);
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//                            params.setMargins(3,0,0,0);
//                            bodyTextView.setLayoutParams(params);
                            bodyTextView.setBackgroundResource(R.drawable.nim_message_right_master_bg);
                            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(10), ScreenUtil.dip2px(25), ScreenUtil.dip2px(10));
                        }
                    }else {
                        setDefault();
                    }
                }else {
                    setDefault();
                }
            }
        }else {
            setDefault();
        }


    }
    public void setDefault(){
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_left_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
//            bodyTextView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_right_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }
}
