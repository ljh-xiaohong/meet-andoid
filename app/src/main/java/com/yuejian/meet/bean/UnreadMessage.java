package com.yuejian.meet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zh02 on 2017/8/21.
 */

public class UnreadMessage implements Parcelable {
    public String create_time;//": 1503139246000,
    public String op_customer_id;//": "300241",
    public String surname;//": "杨",
    public String msg_photo;//": "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/bg/head.png",
    public String msg_remark;//": "",
    public String name;//": "锦超",
    public String msg_type;//": 1,
    public String id;//": 340,
    public String customer_id;//": 300258,
    public String title;//": "关注了您",
    public String object_id;//": "1111"
    public String msg_remark2;

    public UnreadMessage() {
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOp_customer_id() {
        return op_customer_id;
    }

    public void setOp_customer_id(String op_customer_id) {
        this.op_customer_id = op_customer_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMsg_photo() {
        return msg_photo;
    }

    public void setMsg_photo(String msg_photo) {
        this.msg_photo = msg_photo;
    }

    public String getMsg_remark() {
        return msg_remark;
    }

    public void setMsg_remark(String msg_remark) {
        this.msg_remark = msg_remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getMsg_remark2() {
        return msg_remark2;
    }

    public void setMsg_remark2(String msg_remark2) {
        this.msg_remark2 = msg_remark2;
    }

    public static Creator<UnreadMessage> getCREATOR() {
        return CREATOR;
    }

    protected UnreadMessage(Parcel in) {
        create_time = in.readString();
        op_customer_id = in.readString();
        surname = in.readString();
        msg_photo = in.readString();
        msg_remark = in.readString();
        name = in.readString();
        msg_type = in.readString();
        id = in.readString();
        customer_id = in.readString();
        title = in.readString();
        object_id = in.readString();
        msg_remark2 = in.readString();
    }

    public static final Creator<UnreadMessage> CREATOR = new Creator<UnreadMessage>() {
        @Override
        public UnreadMessage createFromParcel(Parcel in) {
            return new UnreadMessage(in);
        }

        @Override
        public UnreadMessage[] newArray(int size) {
            return new UnreadMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(create_time);
        dest.writeString(op_customer_id);
        dest.writeString(surname);
        dest.writeString(msg_photo);
        dest.writeString(msg_remark);
        dest.writeString(name);
        dest.writeString(msg_type);
        dest.writeString(id);
        dest.writeString(customer_id);
        dest.writeString(title);
        dest.writeString(object_id);
        dest.writeString(msg_remark2);
    }
}
