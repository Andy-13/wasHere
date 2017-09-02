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
    }

    public static class FOLDER{
        public static final String APP_BASE_FOLDER = "/wasHere";
        public static final String TASK_PAD_PICTURES = APP_BASE_FOLDER +"/Post/Pictures/";
    }
}