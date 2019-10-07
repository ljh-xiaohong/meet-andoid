package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/19 15:31
 * @desc :
 */
public class MessageBean {

    /**
     * code : 0
     * data : [{"msgRemark2":null,"msgType":4,"createTime":1504939140000,"customerId":300541,"msgPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/APc4bWb2G3.png","msgRemark":"您在【佛山1群】发的'鞭炮'礼物包，剩余 1 个礼物失效已退回您的钱包，可去[钱包]->[账单明细]查看退款记录","title":"【佛山1群】礼物包过期退回","opCustomerId":null,"objectId":null},{"msgRemark2":null,"msgType":5,"createTime":1504777021000,"customerId":300541,"msgPhoto":null,"msgRemark":"0","title":"您的《俄罗斯惨败中国》文章审核不通过(asdasd)","opCustomerId":null,"objectId":90},{"msgRemark2":null,"msgType":5,"createTime":1504776986000,"customerId":300541,"msgPhoto":null,"msgRemark":"0","title":"您的《李白》文章审核不通过(sdd)","opCustomerId":null,"objectId":91},{"msgRemark2":null,"msgType":4,"createTime":1504754806000,"customerId":300541,"msgPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/system/icon_jinggao%403x.png","msgRemark":"2","title":"由于你的不良行为，收到一个警告，被禁言3天","opCustomerId":300542,"objectId":null},{"msgRemark2":null,"msgType":5,"createTime":1504751061000,"customerId":300541,"msgPhoto":null,"msgRemark":"1","title":"您的文章《俄罗斯惨败中国》已通过审核并发表于文化-起源模块，感谢您对百家姓文化的贡献","opCustomerId":null,"objectId":90},{"msgRemark2":null,"msgType":5,"createTime":1504751057000,"customerId":300541,"msgPhoto":null,"msgRemark":"1","title":"您的文章《李白》已通过审核并发表于文化-起源模块，感谢您对百家姓文化的贡献","opCustomerId":null,"objectId":91},{"msgRemark2":null,"msgType":5,"createTime":1504751041000,"customerId":300541,"msgPhoto":null,"msgRemark":"1","title":"您提交的营业执照认证已通过审核","opCustomerId":null,"objectId":8},{"msgRemark2":null,"msgType":5,"createTime":1504751002000,"customerId":300541,"msgPhoto":null,"msgRemark":"1","title":"您提交的身份认证已通过审核","opCustomerId":null,"objectId":8},{"msgRemark2":null,"msgType":4,"createTime":1504750890000,"customerId":300541,"msgPhoto":"http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/APc4bWb2G3.png","msgRemark":"您在【佛山1群】发的'茉莉'礼物包，剩余 3 个礼物失效已退回您的钱包，可去[钱包]->[账单明细]查看退款记录","title":"【佛山1群】礼物包过期退回","opCustomerId":null,"objectId":null}]
     * message : 操作成功
     * result : true
     */

    private int code;
    private String message;
    private boolean result;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * msgRemark2 : null
         * msgType : 4
         * createTime : 1504939140000
         * customerId : 300541
         * msgPhoto : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/gift/APc4bWb2G3.png
         * msgRemark : 您在【佛山1群】发的'鞭炮'礼物包，剩余 1 个礼物失效已退回您的钱包，可去[钱包]->[账单明细]查看退款记录
         * title : 【佛山1群】礼物包过期退回
         * opCustomerId : null
         * objectId : null
         */

        private Object msgRemark2;
        private int msgType;
        private int id;
        private long createTime;
        private int customerId;
        private String msgPhoto;
        private String msgRemark;
        private String title;
        private Object opCustomerId;
        private Object objectId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getMsgRemark2() {
            return msgRemark2;
        }

        public void setMsgRemark2(Object msgRemark2) {
            this.msgRemark2 = msgRemark2;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getMsgPhoto() {
            return msgPhoto;
        }

        public void setMsgPhoto(String msgPhoto) {
            this.msgPhoto = msgPhoto;
        }

        public String getMsgRemark() {
            return msgRemark;
        }

        public void setMsgRemark(String msgRemark) {
            this.msgRemark = msgRemark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getOpCustomerId() {
            return opCustomerId;
        }

        public void setOpCustomerId(Object opCustomerId) {
            this.opCustomerId = opCustomerId;
        }

        public Object getObjectId() {
            return objectId;
        }

        public void setObjectId(Object objectId) {
            this.objectId = objectId;
        }
    }
}
