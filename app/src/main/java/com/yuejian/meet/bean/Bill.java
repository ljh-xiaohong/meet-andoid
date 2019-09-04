package com.yuejian.meet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zh02 on 2017/8/19.
 */

public class Bill implements Parcelable {
    public double amount;//": 7,
    public long create_time;//": 1503364756000,
    public String op_customer_id;//": "",
    public String photo;//": "http://wx.qlogo.cn/mmopen/UK9RSMOAw5ibvHGGPeTd1rBQILKg1EzlRA8icia5mD1gauNib38MRpvRYOpf3R0CCibgicL1t0B8K2ugGCoIqbmutEST4J8cJIF3GM/0",
    public String id;//": 20,
    public String customer_id;//": 300258,
    public int type;//": 3,
    public String object_id;//": 2726,
    public String content;//": "收到梁东波的棒棒糖"
    public String type_name;//": "收到礼物"

    public Bill(){}

    protected Bill(Parcel in) {
        amount = in.readInt();
        create_time = in.readLong();
        op_customer_id = in.readString();
        photo = in.readString();
        id = in.readString();
        customer_id = in.readString();
        type = in.readInt();
        object_id = in.readString();
        content = in.readString();
        type_name = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getOp_customer_id() {
        return op_customer_id;
    }

    public void setOp_customer_id(String op_customer_id) {
        this.op_customer_id = op_customer_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeLong(create_time);
        dest.writeString(op_customer_id);
        dest.writeString(photo);
        dest.writeString(id);
        dest.writeString(customer_id);
        dest.writeInt(type);
        dest.writeString(object_id);
        dest.writeString(content);
    }
}

