package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.netease.nim.uikit.app.entity.MessageExtendEntity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.framents.message.MessageFragment;
import com.yuejian.meet.session.util.NimTypeUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.HashMap;
import java.util.List;


/**
 * Created by zh03 on 2017/6/13.
 * 私信adpter
 */

public class MessagePrivateChatAdapter extends FKAdapter<RecentContact> {
    private AdapterHolder mHelper;
    private Context context;
    private Boolean isEdit=false;
    MessageFragment messageFragment;


    public MessagePrivateChatAdapter(AbsListView view, List<RecentContact> mDatas, int itemLayoutId,MessageFragment messageFragment) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
        this.messageFragment=messageFragment;
    }

    public void convert(AdapterHolder helper, RecentContact item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    public void setIsEdit(boolean isEdit){
        this.isEdit=isEdit;
    }

    private void initNearByData(final AdapterHolder helper, final RecentContact item, final int position) {
        this.mHelper = helper;
        helper.setText(R.id.txt_message_private_chat_content, NimTypeUtils.descOfMsg(item));
        helper.setText(R.id.txt_message_private_chat_tiem, Utils.s2tOrT2s(StringUtils.friendlyTime(item.getTime() + "")));
        helper.getView(R.id.txt_message_private_age).setVisibility(View.VISIBLE);
        final ImageView headimag = mHelper.getView(R.id.img_michat_new_live_head);
         if (item.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(item.getContactId());
             Glide.with(context).load(team.getIcon()).placeholder(R.drawable.nim_avatar_group).error(R.drawable.nim_avatar_group).into(headimag);
             helper.setText(R.id.txt_message_private_chat_name, Utils.s2tOrT2s(team.getName()));
             helper.getView(R.id.txt_message_private_age).setVisibility(View.GONE);
//            imgHead.loadTeamIconByTeam(team);
        }
        NimUserInfo user = NimUserInfoCache.getInstance().getUserInfo(item.getContactId());
        if (user != null) {
            if (user.getName() != null)
                helper.setText(R.id.txt_message_private_chat_name, user.getName());
            helper.getView(R.id.txt_message_private_age).setSelected(user.getGenderEnum() == GenderEnum.FEMALE?false:true);
            String avatar = user.getAvatar();
            Glide.with(context).load(avatar).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(headimag);
            MessageExtendEntity entity = JSON.parseObject(user.getExtension(), MessageExtendEntity.class);
            if (entity!=null){
                helper.setText(R.id.txt_message_private_age," "+ entity.getAge()==null?"18":entity.getAge());
            }
        }
        mHelper.getView(R.id.chat_notice_tip).setVisibility(View.GONE);
        if (item.getUnreadCount() > 0) {
            helper.getView(R.id.chat_notice_tip).setVisibility(View.VISIBLE);
            helper.setText(R.id.chat_notice_tip, item.getUnreadCount() + "");
        }
        helper.getView(R.id.message_top_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getSessionType() == SessionTypeEnum.P2P){
                    ImUtils.toP2PCaht(mCxt, item.getContactId());
                }else if (item.getSessionType() == SessionTypeEnum.Team){
                    groupType(item.getContactId());
//                    ImUtils.toTeamSession(mCxt,item.getContactId(),"0");
                }
            }
        });
        helper.getView(R.id.btnTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getTag()==1){
                    item.setTag(0);
                    helper.setText(R.id.btnTop,context.getString(R.string.but_top));
                }else {
                    item.setTag(1);
                    helper.setText(R.id.btnTop,context.getString(R.string.Cancel_the_top));
                }
                messageFragment.settinTop(position,item);
                ((SwipeMenuLayout)helper.getConvertView()).quickClose();
            }
        });
        helper.getView(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFragment.deleteMessage(item);
                ((SwipeMenuLayout)helper.getConvertView()).quickClose();
            }
        });

    }
    public void groupType(final String paramString) {
        if (messageFragment.dialog != null) {
            messageFragment.dialog.show(messageFragment.getActivity().getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("t_id", paramString);
        new ApiImp().groupType(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (messageFragment.dialog != null) {
                    messageFragment.dialog.dismiss();
                }
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                if (messageFragment.dialog != null) {
                    messageFragment.dialog.dismiss();
                }
                if (data.equals("0")) {
                    ImUtils.toGroupChat(mCxt, paramString, ChatEnum.defaults, "0", Boolean.valueOf(false));
                }else if (data.equals("1")){
                    ImUtils.toGroupChat(mCxt, paramString, ChatEnum.FoundGroup, "0", Boolean.valueOf(false));
                }else if (data.equals("2")){
                    ImUtils.toGroupChat(mCxt, paramString, ChatEnum.CLANGROUP, "0", Boolean.valueOf(true));
                }
            }
        });
    }
}
