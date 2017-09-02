package com.example.yang.washere.UI;

import android.util.Log;

import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.callback.ResultStringCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Yang on 2017/9/2.
 */

public class GetPositionUtil {
    private static String address = "";
    public static String getPosition(double latitude,double longitude) {
        String QUERYADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=zh_cn";
        String url = String.format(QUERYADDRESS, latitude, longitude);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new ResultStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("GetPosition", e.toString());
                        address = "";
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("GetPosition", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            JSONObject currentAddress ;
                            for(int i = 0;i<1;i++) {
                                currentAddress = jsonArray.getJSONObject(i);
                                address = currentAddress.getString("formatted_address");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            address = "";
                        }
                    }
                });
        //
        LogUtils.i("GetPosition", address + "");
        return address;
    }
}
