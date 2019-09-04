package com.netease.nim.uikit.app.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * 群头部提示---type=101
 * Created by zhoujianghua on 2015/4/9.
 */
public class GroupHeaderHintAttachment extends CustomAttachment{
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";
    protected int type;
    protected JSONObject data;

    public GroupHeaderHintAttachment() {
        super(CustomAttachmentType.TypeGroupHint);
    }

    @Override
    public String toJson(boolean send) {
        return packData(CustomAttachmentType.TypeGroupHint, data);
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
