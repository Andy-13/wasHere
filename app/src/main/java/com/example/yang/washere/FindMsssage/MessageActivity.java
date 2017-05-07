package com.example.yang.washere.FindMsssage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.washere.Comment.CommentItem;
import com.example.yang.washere.Comment.CommentAdapter;
import com.example.yang.washere.MyMessage.Messageitem;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by joe on 17-5-1.
 */

public class MessageActivity extends AppCompatActivity {
    private static final String DIALOG_IMAGE = "DialogImage";

    private PullRecyclerView mPullRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentItem> commentList = new ArrayList<>();

    private MLRoundedImageView ml_user_image;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;
    private ImageView iv_message_image;
    private Messageitem item;
    private CommentItem[] commentItems = new CommentItem[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        CommentItem commentItem = new CommentItem(getResources().getDrawable(R.mipmap.hai),"东野圭吾","2014-10-10 20:20",
                "这是一条有趣的评论");
        for(int i =0; i <4; i++){
            commentItems[i] = commentItem;
        }
        item = new Messageitem(getResources().getDrawable(R.mipmap.hai),"东野圭吾","2014-10-10 20:20",
                "这是一个很有趣的信息",getResources().getDrawable(R.mipmap.hai),commentItems);
        mPullRecyclerView = (PullRecyclerView)findViewById(R.id.pull_recycler_view);
        init(commentItem);
        mPullRecyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                Toast.makeText(getBaseContext(),"点击下拉刷新",Toast.LENGTH_SHORT).show();
                mPullRecyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
//                Messageitem item = new Messageitem(getResources().getDrawable(R.mipmap.ic_launcher),"东野圭吾","2014-10-10 20:20",
//                        "To use a DrawerLayout, position your primary content view as the first child with width and height of match_parent and no layout_gravity>. " +
//                                "Add drawers as child views after the main content view and set the layout_gravity appropriately. " +
//                                "Drawers commonly use match_parent for height with a fixed width.",getResources().getDrawable(R.mipmap.ic_launcher));
//                messageList.add(item);

                mPullRecyclerView.stopLoadMore();

            }
        });
        mPullRecyclerView.refreshNoPull();


        ml_user_image = (MLRoundedImageView)findViewById(R.id.ml_user_image);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_content= (TextView)findViewById(R.id.tv_content);
        iv_message_image = (ImageView)findViewById(R.id.iv_message_image);

        ml_user_image.setImageDrawable(item.getMl_user_head());
        tv_name.setText(item.getUser_name());
        tv_time.setText(item.getPublish_time());
        tv_content.setText(item.getContent());
        iv_message_image.setImageDrawable(item.getPublish_image());

        iv_message_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] commentContents = new String[commentItems.length];
                for(int i = 0; i < commentItems.length; i++){
                    commentContents[i] = commentItems[i].getComment_content();
                }
                android.app.FragmentManager fragmentManager = getFragmentManager();
                final ImageDanmuDialog  imageDialog = ImageDanmuDialog.newInstance("lq",commentContents);
                imageDialog.show(fragmentManager,DIALOG_IMAGE);
                java.util.Timer timer = new java.util.Timer(true);

                TimerTask task = new TimerTask() {
                    public void run() {
                        imageDialog.equipment();
                    }
                };
                timer.schedule(task, 1000);

            }
        });
    }
    private void init(CommentItem commentItem) {

        mPullRecyclerView.setLayoutManager(new XLinearLayoutManager(getBaseContext()));
        commentAdapter = new CommentAdapter(getBaseContext(), R.layout.item_comment, commentList);
        mPullRecyclerView.setAdapter(commentAdapter);
        mPullRecyclerView.setColorSchemeResources(R.color.colorPrimary); // 设置下拉刷新的旋转圆圈的颜色
        mPullRecyclerView.enablePullRefresh(true); // 开启下拉刷新，默认即为true，可不用设置
        mPullRecyclerView.enableLoadMore(true);
        mPullRecyclerView.enableLoadDoneTip(true, R.string.load_done_tip); // 开启数据全部加载完成时的底部提示，默认为false

        commentList.add(commentItems[0]);
        commentList.add(commentItems[1]);
        commentList.add(commentItems[2]);
        commentList.add(commentItems[3]);
    }
}
