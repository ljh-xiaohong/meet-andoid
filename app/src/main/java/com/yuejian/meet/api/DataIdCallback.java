package com.yuejian.meet.api;

/**
 * <b>创建时间</b> 2017/1/3 <br>
 *
 */
public interface DataIdCallback<T> {
    void onSuccess(T data, int id);

    void onFailed(String errCode, String errMsg, int id);
}
