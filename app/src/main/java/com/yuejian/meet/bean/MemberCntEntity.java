package com.yuejian.meet.bean;

/**
 * 成员数---实体类
 * Created by zh03 on 2017/8/9.
 */

public class MemberCntEntity {
    public String certified_member_cnt;//": 0,认证
    public String family_id="0";//": 8,
    public String id;//": 4,
    public String member_cnt;//": 0

    public String getCertified_member_cnt() {
        return certified_member_cnt;
    }

    public void setCertified_member_cnt(String certified_member_cnt) {
        this.certified_member_cnt = certified_member_cnt;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_cnt() {
        return member_cnt;
    }

    public void setMember_cnt(String member_cnt) {
        this.member_cnt = member_cnt;
    }
}
