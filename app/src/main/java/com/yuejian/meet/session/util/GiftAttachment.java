package com.yuejian.meet.session.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.extension.CustomAttachment;


/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class GiftAttachment extends CustomAttachment {

    private final String KEY_CATALOG = "catalog";
    private final String KEY_CHARTLET = "chartlet";

    private String catalog;
    private String chartlet;

    public GiftAttachment() {
        super(CustomAttachmentType.Gift);
    }

    public GiftAttachment(String catalog, String id) {
        this();
        this.catalog = catalog;
        this.chartlet = id;
    }

    @Override
    protected void parseData(com.alibaba.fastjson.JSONObject data) {
        try {
            this.catalog = data.getString(KEY_CATALOG);
            this.chartlet = data.getString(KEY_CHARTLET);
        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_CATALOG, catalog);
            data.put(KEY_CHARTLET, chartlet);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getChartlet() {
        return chartlet;
    }
}
