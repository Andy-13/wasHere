package com.example.yang.washere.FindMsssage;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Aria on 2017/9/2.
 */

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient okHttpClient;

    private static void init(){
        okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(2000,TimeUnit.MILLISECONDS)
                            .build();
    }

    public static Call doPost(String url, JSONObject object) throws IOException {
        Log.d("MainActivity","object:"+object.toString());
        RequestBody body = RequestBody.create(JSON,object.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        if (okHttpClient == null)
            init();
        return okHttpClient.newCall(request);

    }

    public static Call doPost(String url, String object) throws IOException {
        Log.d(TAG,"object:"+object);
        RequestBody body = RequestBody.create(JSON,object);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        if (okHttpClient == null)
            init();
        return okHttpClient.newCall(request);
    }

    public static Call doGet(String url){
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        return httpClient.newCall(request);
    }
}
