package com.yuejian.meet.bean;

/**
 * 城市认领
 * Created by zh03 on 2017/9/4.
 */

public class ClaimPactEntity {
    public int apply_status;//": 0,  0-申请中,1-申请通过,2-申请未通过
    public String apply_list;//": []

    public int getApply_status() {
        return apply_status;
    }

    public void setApply_status(int apply_status) {
        this.apply_status = apply_status;
    }

    public String getApply_list() {
        return apply_list;
    }

    public void setApply_list(String apply_list) {
        this.apply_list = apply_list;
    }
}
