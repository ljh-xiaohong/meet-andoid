package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ljh
 * @time : 2019/9/16 09:43
 * @desc :
 */
public class ChooseIndustryBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * isSelect : true
         * job : IT|通信|电子|互联网
         * jobs : [{"isSelect":false,"job":"计算机软件"},{"isSelect":false,"job":"IT服务(系统/数据/维护)"},{"isSelect":false,"job":"电子技术/半导体/集成电路"},{"isSelect":false,"job":"计算机硬件"},{"isSelect":false,"job":"通信/电信/网络设备"},{"isSelect":false,"job":"通信/电信运营、增值服务"},{"isSelect":false,"job":"网络游戏"}]
         */

        private boolean isSelect;
        private String job;
        private int pId;
        private List<JobsBean> jobs;

        public int getpId() {
            return pId;
        }

        public void setpId(int pId) {
            this.pId = pId;
        }

        public boolean isIsSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public List<JobsBean> getJobs() {
            return jobs;
        }

        public void setJobs(List<JobsBean> jobs) {
            this.jobs = jobs;
        }

        public static class JobsBean implements Serializable {
            /**
             * isSelect : false
             * job : 计算机软件
             */

            private boolean isSelect;
            private String job;
            private int cId;

            public int getcId() {
                return cId;
            }

            public void setcId(int cId) {
                this.cId = cId;
            }

            public boolean isIsSelect() {
                return isSelect;
            }

            public void setIsSelect(boolean isSelect) {
                this.isSelect = isSelect;
            }

            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }
        }
    }
    /**
     * job : IT|通信|电子|互联网
     * jobs : ["计算机软件","IT服务(系统/数据/维护)","电子技术/半导体/集成电路","计算机硬件","通信/电信/网络设备","通信/电信运营、增值服务","网络游戏"]
     */

}
