package com.yuejian.meet.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachmentType;
import com.netease.nim.uikit.app.extension.GroupHeaderHintAttachment;
import com.netease.nim.uikit.app.extension.MyTiptAttachment;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.yuejian.meet.session.viewholder.MsgViewHolderMyTip;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = object.getInteger(KEY_TYPE);
            JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
                case CustomAttachmentType.Guess:
                    attachment = new GuessAttachment();
                    break;
                case CustomAttachmentType.SnapChat:
                    return new SnapChatAttachment(data);
                case CustomAttachmentType.Sticker:
                    attachment = new StickerAttachment();
                    break;
                case CustomAttachmentType.RTS:
                    attachment = new RTSAttachment();
                    break;
                case CustomAttachmentType.RED_ENVELOPE:
                    attachment = new RedEnvelopeAttachment();
                    break;
                case CustomAttachmentType.TYPE_TIP:
                    attachment = new MyTiptAttachment();
                    break;
                case CustomAttachmentType.TYPE_GIFT:
                    attachment = new ChatGiftAttachment();
                    break;
                case CustomAttachmentType.TypeInviteIntoGroup:
                    attachment = new InviteJoinAttachment();
                    break;
                case CustomAttachmentType.TypeGroupHint:
                    attachment = new GroupHeaderHintAttachment();
                    break;
                case CustomAttachmentType.TypeInviteIntoClan:
                    attachment = new InviteJoinClanAttachment();
                    break;
                case CustomAttachmentType.TypeGathering:
                    attachment = new InviteJoinGatheringAttachment();
                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
