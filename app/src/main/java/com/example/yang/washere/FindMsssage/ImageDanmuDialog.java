package com.example.yang.washere.FindMsssage;

/**
 * Created by Idoit on 2017/5/4.
 */


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yang.washere.R;

import java.util.HashMap;
import java.util.Random;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by Idoit on 2017/4/29.
 */

public class ImageDanmuDialog extends DialogFragment {
    private static final String ARG_IMAGE = "photo";
    private static final String ARG_COMMENTS = "comments";

    private DanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private ImageView mBackgournd;
    private int commentNums;
    private String[] comments;

    private AcFunDanmakuParser mParser;

    public static ImageDanmuDialog newInstance(String photoFile,String[] comments)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, photoFile);
        args.putSerializable(ARG_COMMENTS,comments);
        ImageDanmuDialog fragment = new ImageDanmuDialog();
        fragment.setArguments(args);
        Log.i("123","instance");
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        Log.i("123","create dialogImage");
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        String photoFile = (String) getArguments().getSerializable(ARG_IMAGE);
        comments = (String[]) getArguments().getSerializable(ARG_COMMENTS);
        commentNums = comments.length;

        View v =  LayoutInflater.from(getActivity()).inflate(R.layout.danmu_diaglog, null);

        mDanmakuView = (DanmakuView) v.findViewById(R.id.danmakuView);
        mBackgournd = (ImageView)v.findViewById(R.id.backGround);
        Resources resources = getResources();
        Drawable btnDrawable = resources.getDrawable(R.mipmap.hai);
        //mDanmakuView.setBackgroundDrawable(btnDrawable);
        mBackgournd.setImageDrawable(btnDrawable);

        Log.i("123","setImageDone");


        //Button show = (Button) v.findViewById(R.id.show);
        //Button hide = (Button) v.findViewById(R.id.hide);
        //Button sendText = (Button) v.findViewById(R.id.sendText);
        //Button pause = (Button) v.findViewById(R.id.pause);
        //Button resume = (Button) v.findViewById(R.id.resume);
        for(int i = 0; i < commentNums; i++){
            Log.i("123",comments[i]);
        }


//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDanmakuView.show();
//            }
//        });
//
//        hide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDanmakuView.hide();
//            }
//        });

//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //每点击一次按钮发送一条弹幕
//                equipment();
//            }
//        });
//
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDanmakuView.pause();
//            }
//        });
//
//        resume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDanmakuView.resume();
//            }
//        });


        init();
        for(int i = 0; i < commentNums; i++){
            addDanmaku(true, comments[i]);
        }
        return new AlertDialog.Builder(getActivity()).setView(v).create();

    }

    public void equipment() {
        for(int i = 0; i < commentNums; i++){
            addDanmaku(true, comments[i]);
        }
    }

    private void addDanmaku(boolean islive,String commentContent) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Log.i("123","here2");
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        Log.i("123","here");
        danmaku.text = commentContent;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        Random random = new Random();
        int delayTime = (int)(random.nextDouble() * commentNums * 3000);
        Log.i("123",String.valueOf(delayTime));
        danmaku.setTime(mDanmakuView.getCurrentTime() + delayTime);
        danmaku.textSize = 20f * (mParser.getDisplayer().getDensity() - 0.6f); //文本弹幕字体大小
        danmaku.textColor = getRandomColor(); //文本的颜色
        danmaku.textShadowColor = getRandomColor(); //文本弹幕描边的颜色
        //danmaku.underlineColor = Color.DKGRAY; //文本弹幕下划线的颜色
        //danmaku.borderColor = getRandomColor(); //边框的颜色

        mDanmakuView.addDanmaku(danmaku);
    }

    private void init() {
        mContext = DanmakuContext.create();

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 8); // 滚动弹幕最大显示8行

        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 5) //描边的厚度
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.6f) //弹幕的速度。注意！此值越小，速度越快！值越大，速度越慢。// by phil
                .setScaleTextSize(1.2f)  //缩放的值
                //.setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
                //.setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        mParser = new AcFunDanmakuParser();
        mDanmakuView.prepare(mParser, mContext);

        //mDanmakuView.showFPS(true);
        mDanmakuView.enableDanmakuDrawingCache(true);

        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                    Log.d("弹幕文本", "danmakuShown text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
    /**
     * 从一系列颜色中随机选择一种颜色
     *
     * @return
     */
    private int getRandomColor() {
        int[] colors = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.CYAN, Color.BLACK, Color.DKGRAY};
        int i = ((int) (Math.random() * 10)) % colors.length;
        return colors[i];
    }
}