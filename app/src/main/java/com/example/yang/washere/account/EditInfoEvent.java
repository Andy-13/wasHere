package com.example.yang.washere.account;

/**
 * Created by Lee Sima on 2017/9/2.
 */

public class EditInfoEvent {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public EditInfoEvent(int type,String data) {
        this.data = data;
        this.type = type;
    }

    @Override
    public String toString() {
        return "EditInfoEvent{" +
                "data='" + data + '\'' +
                ", type=" + type +
                '}';
    }

    int type;
    String data;

    /**
     * 修改昵称
     */
    public static final int TYPE_EDIT_NAME = 0;
    /**
     * 修改性别
     */
    public static final int TYPE_EDIT_SEX = 1;
    /**
     * 修改个性签名
     */
    public static final int TYPE_EDIT_SIGN = 2;
}
