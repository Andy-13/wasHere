package com.example.yang.washere.Constant;

import java.net.URI;

/**
 * Created by Yang on 2017/9/2.
 */

public class Constant {
    public static class URL{
        public static String BASE_URL = "http://119.29.191.103:8080/here/";

        //登陆
        public static final String LOGIN_URL = BASE_URL + "login.action";





        //修改用户名
        public static final String URL_EDIT_USER_NAME = BASE_URL + "editUserName.action";
        //修改头像
        public static final String URL_EDIT_USER_LOGO = BASE_URL + "editUserLogo.action";
        //修改性别
        public static final String URL_EDIT_USER_GENDER = BASE_URL + "editUserGender.action";
        //修改生日
        public static final String URL_EDIT_USER_BIRTHDAY = BASE_URL + "editUserBirthday.action";
        //修改个人签名
        public static final String URL_EDIT_USER_SIGN = BASE_URL + "editUserSign.action";
        //获取个人信息
        public static final String URL_GET_USER_INFO = BASE_URL + "checkUserInf.action";
    }
}
