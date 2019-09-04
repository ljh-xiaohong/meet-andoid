package com.yuejian.meet.bean;

/**
 * Created by Administrator on 2018/1/22/022.
 */

public class BusMessage {
    public static final  String QRCODE_MESSAGE = "qr_code_message";
    public String type;
    public Object message;

    public BusMessage(String type, Object message) {
        this.type = type;
        this.message = message;
    }
}
