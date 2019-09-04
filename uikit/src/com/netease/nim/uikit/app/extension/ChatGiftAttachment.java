package com.netease.nim.uikit.app.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * 礼物---type=7
 * Created by zhoujianghua on 2015/4/9.
 */
public class ChatGiftAttachment extends CustomAttachment{
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";
    protected int type;
    protected JSONObject data;

    public ChatGiftAttachment() {
        super(CustomAttachmentType.TYPE_GIFT);
    }

    @Override
    public String toJson(boolean send) {
        return packData(CustomAttachmentType.TYPE_GIFT, data);
    }

    public int getType() {
        return type;
    }

    protected void parseData(JSONObject data) {
        this.data=data;
    }

    protected JSONObject packData() {
        return data;
    }
    public void setData(JSONObject data){
        this.data=data;
    }
    public String getData(){
        return data.toJSONString();
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
