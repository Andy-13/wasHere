package com.example.yang.washere.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;



import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Yang on 2017/2/28.
 */

public class TakePhotoPickPhotoUtils {
    private Context mContext;
    private int size;

    public Uri getImageCaptureUri() {
        return imageCaptureUri;
    }

    public Uri getImageCropUri() {
        return imageCropUri;
    }

    private Uri imageCaptureUri;
    private Uri imageCropUri;
    public TakePhotoPickPhotoUtils(Context context, int size) {
        mContext = context;
        this.size = size;
    }
    /**
     * 调用系统直接拍照
     */
   public Intent takePhoto() {
        PermissionGen.needPermission(((Activity) mContext), 100,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }
        );
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;


       String dir_name ="/wasHere/photos/";
       File appDir = new File(Environment.getExternalStorageDirectory()+dir_name);
       if (!appDir.exists()) {
           appDir.mkdirs();
       }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(appDir.getPath(),
                "time_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");

        if (currentapiVersion < 24) {
            imageCaptureUri = Uri.fromFile(file);
            LogUtils.i("147258369", "11111");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    imageCaptureUri);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            imageCaptureUri = ((Activity) mContext)
                    .getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
//            imageCaptureUri = FileProvider.getUriForFile(mContext, "com.simalee.gulidaka.system.fileprovider", file);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            imageCaptureUri = FileProvider.getUriForFile(mContext.getApplicationContext(), "com.simalee.gulidaka.fileprovider", file);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        }

//        if (checkIntentAndSd(intent))
            return intent;

    }

    /**
     * 选择图片
     */
   public Intent pickPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
//        if (checkIntentAndSd(intent))
            return intent;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public Intent startPhotoZoom(Uri uri) {
        PermissionGen.needPermission(((Activity) mContext), 100,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }
        );
        String dir_name = "/wasHere/photos/";
        File appDir = new File(Environment.getExternalStorageDirectory()+dir_name);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        File file = new File(appDir.getPath(),
                "time_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");
        LogUtils.i("147258369", "666");

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT < 24) {
            imageCropUri = Uri.fromFile(file);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        } else {
            intent.putExtra("return-data", false);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageCropUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
            LogUtils.i("123456789", "imageCropUri" + imageCropUri);
        }
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", size);
        intent.putExtra("aspectY", size);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 检查SD卡是否存在
     * 检查隐式intent是否存在
     *
     * @param intent
     * @return
     */
    private boolean checkIntentAndSd(Intent intent) {
        //检查
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(((Activity) mContext),
                    "请先插入SD卡", Toast.LENGTH_SHORT).show();
            return false;
        }
        ComponentName componentName = intent.resolveActivity(((Activity) mContext).getPackageManager());
        if (componentName == null) {
            Toast.makeText(mContext,
                    "找不到对应的应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
