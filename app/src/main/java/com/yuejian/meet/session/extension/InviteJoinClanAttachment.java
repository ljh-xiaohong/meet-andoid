package com.yuejian.meet.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.extension.CustomAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachmentType;

public class InviteJoinClanAttachment
        extends CustomAttachment
{
    private JSONObject data;

    public InviteJoinClanAttachment() {
        super(CustomAttachmentType.TypeInviteIntoClan);
    }

    public String getData()
    {
        return this.data.toJSONString();
    }

    protected JSONObject packData()
    {
        return this.data;
    }

    protected void parseData(JSONObject paramJSONObject)
    {
        this.data = paramJSONObject;
    }

    public void setData(JSONObject paramJSONObject)
    {
        this.data = paramJSONObject;
    }
}
