package com.yuejian.meet.bean;

import java.util.ArrayList;
import java.util.List;

public class PhotoItem {
    public long create_time = 0L;
    public String photo = "";
    public List<String> photos = new ArrayList<>();

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}