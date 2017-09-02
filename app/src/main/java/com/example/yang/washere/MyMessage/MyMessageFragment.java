package com.example.yang.washere.MyMessage;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;


/**
 * Created by Yang on 2017/4/28.
 */

public class MyMessageFragment extends Fragment {
    private static final String TAG = "MyMessageFragment";
    private PullRecyclerView mPullRecyclerView;
    private MyMessageAdapter mMessageAdapter;
    private ArrayList<Messageitem> messageList = new ArrayList<>();
    private static Context mContext;

    public static MyMessageFragment newInstance(Context context) {

        Bundle args = new Bundle();
        mContext = context;

        MyMessageFragment fragment = new MyMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_message,container,false);
        mPullRecyclerView = (PullRecyclerView)view.findViewById(R.id.pull_recycler_view);
        init();
        mPullRecyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                getData(0);
                Toast.makeText(mContext,"点击下拉刷新",Toast.LENGTH_SHORT).show();
                mPullRecyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                getData(1);
                mPullRecyclerView.stopLoadMore();
                mPullRecyclerView.enableLoadMore(false);
            }
        });
        mPullRecyclerView.refreshNoPull();
        return view;
    }
    private void init() {
        mPullRecyclerView.setLayoutManager(new XLinearLayoutManager(mContext));
        mMessageAdapter = new MyMessageAdapter(mContext, R.layout.item_my_message, messageList);
        mPullRecyclerView.setAdapter(mMessageAdapter);
        mPullRecyclerView.setColorSchemeResources(R.color.colorPrimary); // 设置下拉刷新的旋转圆圈的颜色
        mPullRecyclerView.enablePullRefresh(true); // 开启下拉刷新，默认即为true，可不用设置
        mPullRecyclerView.enableLoadMore(true);
        mPullRecyclerView.enableLoadDoneTip(true, R.string.load_done_tip); // 开启数据全部加载完成时的底部提示，默认为false
    }


    /**
     * type 0代表下拉刷新
     *       1代表上拉加载*/
    private void getData(final int type) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk-mm-ss");
        String last_time = "";
        if (type == 0) {
            last_time = dateFormat.format(new Date());
        }else{
            if(messageList.size()!=0)
                last_time = messageList.get(messageList.size() - 1).getPublish_time();
            else {
                mPullRecyclerView.stopLoadMore();
                return;
            }
        }

        LogUtils.i("123456789", last_time);
        String date = last_time.substring(0, 10);
        String hour = last_time.substring(11, 13);
        String minute_sec = last_time.substring(13, 18);
        LogUtils.i("123456789", date);
        LogUtils.i("123456789", hour);
        LogUtils.i("123456789", minute_sec);
        if (hour.equals("24")) {
            last_time = date + " " + "00" + minute_sec;
        }
        OkHttpUtils.post()
                .url(Constant.URL.CHECK_MY_POST_URL)
                .addParams("u_id", PreferenceUtil.getString(mContext, PreferenceUtil.USERID))
                .addParams("last_time",last_time)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("getTaskPad", e.toString());
                        if (type == 0) {
                            mPullRecyclerView.stopRefresh();
                        }else{
                            mPullRecyclerView.stopLoadMore();
                            mPullRecyclerView.enableLoadMore(true);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i(TAG, response);
                        try {
                            if (type == 0) {
                                messageList.clear();//清除所有TaskPadItems;
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("posts");
                            JSONObject currentPost = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                currentPost = (JSONObject) jsonArray.get(i);
                                Messageitem messageItem = new Messageitem();
                                String p_id = currentPost.getString("p_id");
                                String p_title = currentPost.getString("p_title");
                                String p_type = currentPost.getString("p_type");
                                String u_id = currentPost.getString("u_id");
                                String u_name = currentPost.getString("u_name");
                                String head_logo = currentPost.getString("head_logo").replace("thumbnail","original");
                                String content = currentPost.getString("content");
                                String multi_media = currentPost.getString("multi_media").replace("thumbnail","original");
                                Double longitude = currentPost.getDouble("longitude");
                                Double latitude = currentPost.getDouble("latitude");
                                String time =  currentPost.getString("time");
                                int level = currentPost.getInt("level");
                                int page_view = currentPost.getInt("page_view");
                                messageItem.setPost_id(p_id);
                                messageItem.setP_title(p_title);
                                messageItem.setP_type(p_type);
                                messageItem.setU_id(u_id);
                                messageItem.setUser_name(u_name);
                                messageItem.setHead_logo(head_logo);
                                messageItem.setContent(content);
                                messageItem.setMulti_media(multi_media);
                                messageItem.setLongitude(longitude);
                                messageItem.setLatitude(latitude);
                                messageItem.setPublish_time(time);
                                messageItem.setLevel(level);
                                messageItem.setPage_view(page_view);
                                messageList.add(messageItem);
                            }
                            mMessageAdapter.notifyDataSetChanged();
                            if (type == 0) {
//                                checkNewResult();
                                mPullRecyclerView.stopRefresh();
                            }else{
                                mPullRecyclerView.stopLoadMore();
                                mPullRecyclerView.enableLoadMore(true);
                            }

                        } catch (JSONException je) {
                            je.printStackTrace();
                            if (type == 0) {
                                mPullRecyclerView.stopRefresh();
                            }else{
                                mPullRecyclerView.stopLoadMore();
                                mPullRecyclerView.enableLoadMore(true);
                            }
                        }
                    }
                });
    }

}
