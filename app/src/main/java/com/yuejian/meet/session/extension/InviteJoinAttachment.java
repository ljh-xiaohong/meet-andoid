package com.yuejian.meet.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.extension.CustomAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachmentType;
import com.netease.nim.uikit.common.util.file.FileUtil;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class InviteJoinAttachment extends CustomAttachment {


    private JSONObject data;

    public InviteJoinAttachment() {
        super(CustomAttachmentType.TypeInviteIntoGroup);
    }
    @Override
    protected void parseData(JSONObject data) {
        this.data=data;
    }

    @Override
    protected JSONObject packData() {
        return this.data;
    }

    public String getData() {
        return data.toJSONString();
    }

    public void setData(JSONObject data) {
        this.data=data;
    }
}
