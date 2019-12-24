package com.yuejian.meet.session.util;

import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.app.extension.MyTiptAttachment;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.yuejian.meet.R;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.session.extension.InviteJoinAttachment;
import com.yuejian.meet.session.extension.SnapChatAttachment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by idea on 2016/12/6.
 */

public class NimTypeUtils {

    public static String descOfMsg(RecentContact recent) {
        if (recent.getMsgType() == MsgTypeEnum.text) {
            return recent.getContent();
        } else if (recent.getMsgType() == MsgTypeEnum.tip) {
//            String digest = null;
//            digest = getDigestOfAttachment(recent.getAttachment());
//
//            if (digest == null) {
//                digest = getDefaultDigest(null);
//            }

            return MyApplication.context.getString(R.string.txt_notify);
        } else if(recent.getMsgType() == MsgTypeEnum.video||recent.getMsgType() == MsgTypeEnum.audio){
            return recent.getContent();
        }else if(MsgTypeEnum.avchat==recent.getMsgType()){
            return MyApplication.context.getString(R.string.txt_video_chat);
        }else if (recent.getAttachment() != null) {
            String digest = null;
            digest = getDigestOfAttachment(recent.getAttachment());
            if (digest == null) {
                digest = getDefaultDigest(recent);
            }

            return digest;
        }
        return recent.getContent();
    }

    public static  String getDigestOfAttachment(MsgAttachment attachment) {
        // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
        // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
        if (attachment instanceof SnapChatAttachment) {
            return MyApplication.context.getString(R.string.txt_secret);
        } else if (attachment instanceof ImageAttachment) {
            try{
                String textContent=((FileAttachment)attachment).getDisplayName();
                if (textContent.contains(MyApplication.context.getString(R.string.gift_pic)))
                    return textContent;
            }catch (Exception e){}
            return MyApplication.context.getString(R.string.txt_pic);
        }else if (attachment instanceof VideoAttachment){
            return MyApplication.context.getString(R.string.txt_video_chat);
        }else if (attachment instanceof RedEnvelopeAttachment){
            return MyApplication.context.getString(R.string.red_packet);
        }else if (attachment instanceof ChatGiftAttachment){
            return MyApplication.context.getString(R.string.gift_pic);
        }else if (attachment instanceof MyTiptAttachment){
            return MyApplication.context.getString(R.string.txt_notify);
        }else if (attachment instanceof InviteJoinAttachment){
            return MyApplication.context.getString(R.string.txt_The_group_of);
        }
        try{
            String textContent=((FileAttachment)attachment).getDisplayName();
            return textContent;
        }catch (Exception e){}

        return null;
    }

    // SDK本身只记录原始数据，第三方APP可根据自己实际需求，在最近联系人列表上显示缩略消息
    // 以下为一些常见消息类型的示例。
    public static String getDefaultDigest(RecentContact recent) {
        MsgAttachment attachment = recent.getAttachment();
        switch (recent.getMsgType()) {
            case text:
                return recent.getContent();
            case image:
                return MyApplication.context.getString(R.string.txt_pic);
            case video:
                return MyApplication.context.getString(R.string.txt_video);
            case audio:
                return MyApplication.context.getString(R.string.txt_audio);
            case location:
                return MyApplication.context.getString(R.string.txt_location);
            case file:
                return MyApplication.context.getString(R.string.txt_file);
            case tip:
                List<String> uuids = new ArrayList<>();
                uuids.add(recent.getRecentMessageId());
                List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (messages != null && messages.size() > 0) {
                    return messages.get(0).getContent();
                }
                return MyApplication.context.getString(R.string.txt_notify);
            case notification:
                return MyApplication.context.getString(R.string.txt_group_hint);
            case avchat:
                return "";
//                AVChatAttachment avchat = (AVChatAttachment) attachment;
//                if (avchat.getState() == AVChatRecordState.Missed && !recent.getFromAccount().equals(FolkApplication.tokenCustomerId)) {
//                    // 未接通话请求
//                    StringBuilder sb = new StringBuilder("[未接");
//                    if (avchat.getType() == AVChatType.VIDEO) {
//                        sb.append("视频电话]");
//                    } else {
//                        sb.append("[音频电话]");
//                    }
//                    return sb.toString();
//                } else if (avchat.getState() == AVChatRecordState.Success) {
//                    StringBuilder sb = new StringBuilder();
//                    if (avchat.getType() == AVChatType.VIDEO) {
//                        sb.append("[视频电话]: ");
//                    } else {
//                        sb.append("[音频电话]: ");
//                    }
//                    sb.append(TimeUtils.secToTime(avchat.getDuration()));
//                    return sb.toString();
//                } else {
//                    if (avchat.getType() == AVChatType.VIDEO) {
//                        return ("[视频电话]");
//                    } else {
//                        return ("[音频电话]");
//                    }
//                }
            default:
                return "[自定义消息]";
        }
    }



}
