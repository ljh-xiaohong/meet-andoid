package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * Created by zh02 on 2017/8/19.
 */

public class NotificationMsgBean implements Serializable {
    //    1, "粉丝消息",2,"视频评价"3, "文章消息",4,"系统消息"5,"审核结果"6,"举报详情"
    public String fansMsgCnt;
    public String videoCommentCnt;
    public String essayMsgCnt;
    public String systemMsgCnt;
    public String examineResultMsgCnt;
    public String reportDetailMsgCnt;
    public String shopMsgCnt;
    public String clanMsgCnt;

    public String getClanMsgCnt() {
        return clanMsgCnt;
    }

    public void setClanMsgCnt(String clanMsgCnt) {
        this.clanMsgCnt = clanMsgCnt;
    }

    public String getShopMsgCnt() {
        return shopMsgCnt;
    }

    public void setShopMsgCnt(String shopMsgCnt) {
        this.shopMsgCnt = shopMsgCnt;
    }

    public String getFansMsgCnt() {
        return fansMsgCnt;
    }

    public void setFansMsgCnt(String fansMsgCnt) {
        this.fansMsgCnt = fansMsgCnt;
    }

    public String getVideoCommentCnt() {
        return videoCommentCnt;
    }

    public void setVideoCommentCnt(String videoCommentCnt) {
        this.videoCommentCnt = videoCommentCnt;
    }

    public String getEssayMsgCnt() {
        return essayMsgCnt;
    }

    public void setEssayMsgCnt(String essayMsgCnt) {
        this.essayMsgCnt = essayMsgCnt;
    }

    public String getSystemMsgCnt() {
        return systemMsgCnt;
    }

    public void setSystemMsgCnt(String systemMsgCnt) {
        this.systemMsgCnt = systemMsgCnt;
    }

    public String getExamineResultMsgCnt() {
        return examineResultMsgCnt;
    }

    public void setExamineResultMsgCnt(String examineResultMsgCnt) {
        this.examineResultMsgCnt = examineResultMsgCnt;
    }

    public String getReportDetailMsgCnt() {
        return reportDetailMsgCnt;
    }

    public void setReportDetailMsgCnt(String reportDetailMsgCnt) {
        this.reportDetailMsgCnt = reportDetailMsgCnt;
    }
}
