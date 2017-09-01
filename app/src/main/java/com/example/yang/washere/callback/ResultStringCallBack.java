package com.example.yang.washere.callback;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by Yang on 2017/1/14.
 */

public abstract class ResultStringCallBack extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String responseString = response.body().string();
        return responseString;
    }
}
