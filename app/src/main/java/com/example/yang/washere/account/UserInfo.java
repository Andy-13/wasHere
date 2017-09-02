package com.example.yang.washere.account;

/**
 * Created by Lee Sima on 2017/7/2.
 */

public class UserInfo {
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UserInfo() {
    }

    //    {msg,userInf:{u_name,phone,head_logo,gender,birthday,signature}
    private String name;
    private String phone;
    private String headUrl;
    /**
     * 0为女，1为男
     */
    private int gender;
    private String birthday;
    private String signature;

    @Override
    public String toString() {
        return "UserInfo{" +
                "birthday='" + birthday + '\'' +
                ", oldName='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", gender=" + gender +
                ", signature='" + signature + '\'' +
                '}';
    }
}
