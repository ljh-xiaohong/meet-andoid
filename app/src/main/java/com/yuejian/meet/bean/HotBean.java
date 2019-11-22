package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/11/18 11:58
 * @desc :
 */
public class HotBean implements Serializable {

    /**
     * code : 0
     * data : [{"createTime":"2019-11-15 16:47:30","id":5,"type":3,"title":"大保健"},{"createTime":"2019-11-15 16:29:10","id":6,"type":3,"title":"首页大保健"}]
     * message : 操作成功
     * result : true
     */

    private int code;
    private String message;
    private boolean result;
    private List<HotSeacher> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<HotSeacher> getData() {
        return data;
    }

    public void setData(List<HotSeacher> data) {
        this.data = data;
    }
}
