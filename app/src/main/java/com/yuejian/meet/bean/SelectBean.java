package com.yuejian.meet.bean;

import java.io.Serializable;

/**
 * @author : ljh
 * @time : 2019/9/16 14:37
 * @desc :
 */
public class SelectBean implements Serializable {
    private String name;
    private int id;
    private int firstPosition;
    private int secondPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }

    public int getSecondPosition() {
        return secondPosition;
    }

    public void setSecondPosition(int secondPosition) {
        this.secondPosition = secondPosition;
    }
}
