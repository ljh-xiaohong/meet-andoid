package com.yuejian.meet.bean;

/**
 * 视频通话时长级消费实体类
 * Created by zh03 on 2017/7/15.
 */

public class AvChatConsumptionEntity {
    public String duration;//通话时长
    public String cost;//通话扣费
    public String video_id;//评价id
    public String bal;//金额

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
