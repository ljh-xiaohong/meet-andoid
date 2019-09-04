package com.netease.nim.uikit.api;

/**
 * <b>创建时间</b> 2016/4/14 <br>
 *
 * @author zhouwenjun
 */
public interface DataCallback<T> {
    void onSuccess(T data);

    void onFailed(String errCode, String errMsg);
}
