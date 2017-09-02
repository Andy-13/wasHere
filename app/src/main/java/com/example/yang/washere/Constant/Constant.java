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


        //查看我的帖子
        public static final String CHECK_MY_POST_URL = BASE_URL + "checkMyPost.action";
        //发布贴子
        public static final String PUBLISH_POST_URL = BASE_URL + "pubPost.action";
        //上传帖子图片
        public static final String UPLOAD_POST_PIC_URL = BASE_URL + "uploadPostPic.action";
        //查看帖子
        public static final String CHECK_POST = BASE_URL + "checkPost.action";





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
        //获取验证码
        public static final String URL_GET_VERIFY_CODE = BASE_URL + "getVerifyCode.action";
        //注册
        public static final String URL_REGISTER = BASE_URL + "register.action";
    }

    public static class FOLDER{
        public static final String APP_BASE_FOLDER = "/wasHere";
        public static final String TASK_PAD_PICTURES = APP_BASE_FOLDER +"/Post/Pictures/";
    }
}