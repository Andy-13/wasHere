package com.example.yang.washere.FindMsssage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.washere.Comment.CommentAdapter;
import com.example.yang.washere.Comment.CommentItem;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.MyMessage.ImageViewActivity;
import com.example.yang.washere.MyMessage.Messageitem;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.GetPositionUtil;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by joe on 17-5-1.
 */

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    private PullRecyclerView mPullRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentItem> commentList = new ArrayList<>();

    private View header;
    private MLRoundedImageView ml_user_image;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_position;
    private ImageView iv_message_image;
    private String post_id;
    private Messageitem messageItem;

    private EditText edt_comment;
    private Button btn_comment;

    String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        header = View.inflate(this, R.layout.item_my_message, null);
        ml_user_image = (MLRoundedImageView) header.findViewById(R.id.ml_user_image);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_time = (TextView) header.findViewById(R.id.tv_time);
        tv_content= (TextView) header.findViewById(R.id.tv_content);
        tv_position = (TextView) header.findViewById(R.id.tv_position);
        iv_message_image = (ImageView) header.findViewById(R.id.iv_message_image);
        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.pull_recycler_view);

        edt_comment = (EditText) findViewById(R.id.comment_edt);
        btn_comment = (Button) findViewById(R.id.comment_btn);

        init();
        Intent intent = getIntent();
        if (intent != null) {
            post_id = intent.getStringExtra("p_id");
            Log.i(TAG, "post_id" + post_id);
        }
        if(!post_id.equals("")){
            getPostMessage(post_id);
            getCommentItems(post_id);
        }


        mPullRecyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                Toast.makeText(getBaseContext(),"刷新",Toast.LENGTH_SHORT).show();
                getCommentItems(post_id);
                mPullRecyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                mPullRecyclerView.stopLoadMore();

            }
        });
        mPullRecyclerView.refreshNoPull();

        iv_message_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] commentContents = new String[commentList.size()];
                for(int i = 0; i < commentList.size(); i++){
                    commentContents[i] = commentList.get(i).getComment_content();
                }
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                final ImageDanmuDialog  imageDialog = ImageDanmuDialog.newInstance("lq",commentContents);
//
//                imageDialog.show(fragmentManager,DIALOG_IMAGE);
                int location[] = new int[2] ;
                iv_message_image.getLocationOnScreen(location);
                Intent i = ImageDanmuActivity.newIntent(MessageActivity.this, picUrl ,commentContents
                        ,location[0],location[1],iv_message_image.getWidth(),iv_message_image.getHeight());


                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString();
                if(comment.equals("")){
                    Toast.makeText(MessageActivity.this, "沉默未必是金哦", Toast.LENGTH_SHORT).show();
                }else {

                    OkHttpUtils.post()
                            .url(Constant.URL.COMMENT_URL)
                            .addParams("u_id", PreferenceUtil.getString(MessageActivity.this,
                                    PreferenceUtil.USERID))
                            .addParams("f_id", post_id)
                            .addParams("content", comment)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    LogUtils.i(TAG, e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");

                                        Log.d("mygod", msg + "  " );

                                        if(msg.equals("0")){
                                            edt_comment.setText(" ");
                                            mPullRecyclerView.refreshNoPull();
                                        }
                                        else Toast.makeText(MessageActivity.this,
                                                "评论失败"+ msg, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException je){
                                        je.printStackTrace();
                                    }
                                }
                            });

                }
            }
        });
    }
    private void init() {

        mPullRecyclerView.setLayoutManager(new XLinearLayoutManager(getBaseContext()));
        commentAdapter = new CommentAdapter(getBaseContext(), R.layout.item_comment, commentList);
        mPullRecyclerView.setAdapter(commentAdapter);
        mPullRecyclerView.addHeaderView(header);
        mPullRecyclerView.setColorSchemeResources(R.color.colorPrimary); // 设置下拉刷新的旋转圆圈的颜色
        mPullRecyclerView.enablePullRefresh(true); // 开启下拉刷新，默认即为true，可不用设置
        mPullRecyclerView.enableLoadMore(true);
        mPullRecyclerView.enableLoadDoneTip(true, R.string.load_done_tip); // 开启数据全部加载完成时的底部提示，默认为false


    }

    private void getPostMessage(String post_id) {
        OkHttpUtils.post()
                .url(Constant.URL.CHECK_POST)
                .addParams("id", post_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("getTaskPad", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("posts");
                            JSONObject currentPost = null;
                            Log.i(TAG, "length: " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i(TAG, "run here");
                                currentPost = (JSONObject) jsonArray.get(i);
                                messageItem = new Messageitem();
                                String p_id = currentPost.getString("p_id");
                                String p_title = currentPost.getString("p_title");
                                String p_type = currentPost.getString("p_type");
                                String u_id = currentPost.getString("u_id");
                                String u_name = currentPost.getString("u_name");
                                String head_logo = currentPost.getString("head_logo").replace("thumbnail","original");
                                String content = currentPost.getString("content");
                                String multi_media = currentPost.getString("multi_media").replace("thumbnail","original");
                                picUrl = multi_media;
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
                            }
                            String head_logo = messageItem.getHead_logo();
                            final String image = messageItem.getMulti_media();
                            Glide.with(MessageActivity.this)
                                    .load(Constant.URL.BASE_URL + head_logo)
                                    .asBitmap()
                                    .into(ml_user_image);
                            Glide.with(MessageActivity.this)
                                    .load(Constant.URL.BASE_URL + image)
                                    .asBitmap()
                                    .into(iv_message_image);
                            tv_name.setText(messageItem.getUser_name());
                            tv_time.setText(messageItem.getPublish_time());
                            tv_content.setText(messageItem.getContent());
//                            tv_position.setText("( "+ messageItem.getLongitude()+" , " + messageItem.getLatitude() +" )");
                            tv_position.setText(new GetPositionUtil().getPosition(messageItem.getLatitude(), messageItem.getLongitude()));

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                });
    }

    private void getCommentItems(String post_id){
        Log.d("mygod", "getCommentItems运行了");
        OkHttpUtils.post()
                .url(Constant.URL.CHECK_POST_COMMENTS_URL)
                .addParams("id", post_id)
                .build()
                .execute(new StringCallback(){
                    @Override
                    public void onResponse(String response, int id) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("floors");
                            JSONObject currentPost = null;
                            Log.d("mygod", "length: "+ jsonArray.length());
                            Log.d("mygod", "response");
                            commentList.clear();
                            for(int i = 0; i < jsonArray.length(); i++){
                                CommentItem commentItem = new CommentItem();
                                currentPost = (JSONObject) jsonArray.get(i);
                                commentItem.setUser_name(currentPost.getString("u_name"));
                                commentItem.setHead_logo(currentPost.getString("head_logo"));
                                commentItem.setPublish_time(currentPost.getString("time"));
                                commentItem.setComment_content(currentPost.getString("content"));
                                commentList.add(commentItem);
                                Log.d("mygod", commentItem.getComment_content());
                            }
                            commentAdapter.notifyDataSetChanged();
                        } catch (JSONException je){
                            je.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("mygod", e.toString());
                    }
                });
    }
}
