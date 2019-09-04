package com.yuejian.meet.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.extension.CustomAttachment;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

    private String content;

    public DefaultCustomAttachment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.toJSONString();
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = null;
        try {
            data = JSONObject.parseObject(content);
        } catch (Exception e) {

        }
        return data;
    }

    public String getContent() {
        return content;
    }
}
