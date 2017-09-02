package com.example.yang.washere.MyMessage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.PinchImageView;
import com.example.yang.washere.Utils.DownLoadImageService;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.callback.ImageDownLoadCallBack;


import java.io.File;

/**
 * Created by Yang on 2017/3/1.
 */

public class ImageViewActivity extends AppCompatActivity {
    public static final String TAG = "ImageViewActivity";
    private PinchImageView pv_photo;
    private String photo_url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        pv_photo = (PinchImageView) findViewById(R.id.iv_photo);
        Bundle bundle = getIntent().getExtras();
         photo_url = bundle.getString("photo_url");
        LogUtils.i(TAG, photo_url);
        pv_photo.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(this).load(Constant.URL.BASE_URL + photo_url)
                .into(pv_photo);

        pv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pv_photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopUpWindow();
                return false;
            }
        });
    }

    private void showPopUpWindow() {
        View contentView = LayoutInflater.from(ImageViewActivity.this)
                .inflate(R.layout.popupwindow_save_photo, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置点击空白地方消失
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置空白地方的背景色
        WindowManager.LayoutParams lp =ImageViewActivity.this
                .getWindow().getAttributes();
        lp.alpha = 0.6f;
       ImageViewActivity.this.getWindow().setAttributes(lp);
        //设置popupWindow消失的时候做的事情 即把背景色恢复
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ImageViewActivity.this
                        .getWindow().getAttributes();
                lp.alpha = 1f;
                ImageViewActivity.this.getWindow().setAttributes(lp);
            }
        });
//        mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        Button bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        Button bt_save_photo = (Button) contentView.findViewById(R.id.bt_save_photo);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
        bt_save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile(Constant.URL.BASE_URL + photo_url);
                mPopWindow.dismiss();

            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(ImageViewActivity.this)
                .inflate(R.layout.activity_imageviewer, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(ImageViewActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ImageViewActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void saveFile(String url) {
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(),
                url,
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(File file) {
                    }
                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        // 在这里执行图片保存方法
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);

                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }


}
