package com.example.yang.washere.MyMessage;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yang.washere.Comment.CommentItem;
import com.example.yang.washere.R;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by Yang on 2017/4/28.
 */

public class MyMessageFragment extends Fragment {
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
                    Toast.makeText(mContext,"点击下拉刷新",Toast.LENGTH_SHORT).show();
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

        CommentItem commentItem = new CommentItem(getResources().getDrawable(R.mipmap.hai),"东野圭吾","2014-10-10 20:20",
                "这是一条有趣的评论");
        CommentItem[] commentItems = new CommentItem[4];
        for(int i =0; i <4; i++){
            commentItems[i] = commentItem;
        }
        Messageitem item = new Messageitem(getResources().getDrawable(R.mipmap.hai),"东野圭吾","2014-10-10 20:20",
                "这是一个很有趣的信息",getResources().getDrawable(R.mipmap.hai),commentItems);
        messageList.add(item);
        messageList.add(item);
        messageList.add(item);
        messageList.add(item);
        messageList.add(item);
        messageList.add(item);
    }
}
