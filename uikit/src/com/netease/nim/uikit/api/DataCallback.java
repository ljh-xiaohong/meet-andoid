package com.netease.nim.uikit.api;

/**
 * <b>创建时间</b> 2016/4/14 <br>
 *
 * @author zhouwenjun
 */
public interface DataCallback<T> {
    void onSuccess(T data);

    void onSuccess(T data, int id);

    void onFailed(String errCode, String errMsg);

    void onFailed(String errCode, String errMsg, int id);
}
