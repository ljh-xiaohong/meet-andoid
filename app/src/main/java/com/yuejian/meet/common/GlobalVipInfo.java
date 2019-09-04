package com.yuejian.meet.common;

import android.app.Activity;
import android.content.Context;

import com.yuejian.meet.bean.SingleVipDetailEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/25 14:52
 * @desc : VIP 信息单例
 */
public final class GlobalVipInfo {

    private Context mContext;
    private volatile static GlobalVipInfo mInstance;
    private static final Object LOCK = new Object();

    private List<SingleVipDetailEntity> mSingleVipDetailEntities = new ArrayList<>();
    private int mPayAllVipTime = 0;
    private int mPayAllVipPrice = 0;
    private int mPayAllVipPriceNot = 0;
    private int mPayAllVipId = -1;

    private GlobalVipInfo(Context context) {
        mContext = context;
    }

    public static GlobalVipInfo getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (mInstance == null) {
                    mInstance = new GlobalVipInfo(context);
                }
            }
        }
        return mInstance;
    }

    public List<SingleVipDetailEntity> getSingleVipDetailEntities() {
        return mSingleVipDetailEntities;
    }

    public void setSingleVipDetailEntities(List<SingleVipDetailEntity> singleVipDetailEntities) {
        mSingleVipDetailEntities = singleVipDetailEntities;
    }

    public int getPayAllVipId() {
        return mPayAllVipId;
    }

    public void setPayAllVipId(int payAllVipId) {
        mPayAllVipId = payAllVipId;
    }

    public int getPayAllVipTime() {
        return mPayAllVipTime;
    }

    public void setPayAllVipTime(int payAllVipTime) {
        mPayAllVipTime = payAllVipTime;
    }

    public int getPayAllVipPrice() {
        return mPayAllVipPrice;
    }

    public void setPayAllVipPrice(int payAllVipPrice) {
        mPayAllVipPrice = payAllVipPrice;
    }

    public int getPayAllVipPriceNot() {
        return mPayAllVipPriceNot;
    }

    public void setPayAllVipPriceNot(int payAllVipPriceNot) {
        mPayAllVipPriceNot = payAllVipPriceNot;
    }
}
