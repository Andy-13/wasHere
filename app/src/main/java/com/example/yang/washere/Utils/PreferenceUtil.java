package com.example.yang.washere.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yang on 2016/12/2.
 */
public class PreferenceUtil {
    //用户id
    public static final String USERID = "user_id";

    public static final String ISLOGIN = "is_login"; // 0代表不保存登录，1代表保存登录
    /**
     * 存储信息
     */
    public static void setString(Context context, String key, String value){
        // 得到SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取信息
     */
    public static String getString(Context context, String key)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        // 返回key值，key值默认值是false
        return preferences.getString(key,"");
    }
}
