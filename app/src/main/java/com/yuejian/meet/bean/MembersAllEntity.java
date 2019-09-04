package com.yuejian.meet.bean;

/**
 * 所有成员（发起人、成员总数等）
 * Created by zh03 on 2017/8/9.
 */

public class MembersAllEntity {
    public String familyMasterCount;//是否有发起人
    public String members;//成员
    public String cnt;//成员数
    public String familyMaster;//发起人数据
    public String family_id;//家族id
    public String surnameMaster;///全国合伙人数据
    public String provinceMaster="";//省发起人

    public String getProvinceMaster() {
        return provinceMaster;
    }

    public void setProvinceMaster(String provinceMaster) {
        this.provinceMaster = provinceMaster;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getSurnameMaster() {
        return surnameMaster;
    }

    public void setSurnameMaster(String surnameMaster) {
        this.surnameMaster = surnameMaster;
    }

    public String getFamilyMasterCount() {
        return familyMasterCount;
    }

    public void setFamilyMasterCount(String familyMasterCount) {
        this.familyMasterCount = familyMasterCount;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getFamilyMaster() {
        return familyMaster;
    }

    public void setFamilyMaster(String familyMaster) {
        this.familyMaster = familyMaster;
    }
}
