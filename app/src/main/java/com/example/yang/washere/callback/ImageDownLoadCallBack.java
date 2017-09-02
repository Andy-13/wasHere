package com.example.yang.washere.callback;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Yang on 2017/3/1.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
