package com.example.yang.washere.FindMsssage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.HomePageActivity;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.GetPositionUtil;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.example.yang.washere.Utils.TakePhotoPickPhotoUtils;
import com.example.yang.washere.event.DeletePhotoEvent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Yang on 2017/5/6.
 */

public class publicMessageActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "PublishMessageActivity";
    public static final int PICK_FROM_CAMERA = 0;
    public static final int PICK_FROM_FILE = 1;
    public static final int ACTION_CROP = 2;


    private TextView tv_cancel;
    private ImageView add_picture;
    private ImageView picture;
    private TextView tv_publish_result;
    private EditText et_comments;
    private Bitmap bitmap;
    private TakePhotoPickPhotoUtils mTakePhotoPickPhotoUtils;
    private TextView tv_tips;
    private TextView tv_position;

    private Uri imageUri;
    private String task_result_id = "";
    private int type = 0;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_message);

        Intent intent = getIntent();
        if (intent != null) {
            longitude = intent.getDoubleExtra("longitude", 0.0);
            latitude = intent.getDoubleExtra("latitude", 0.0);
            LogUtils.i(TAG, "Longitude ----------"+longitude + "");
            LogUtils.i(TAG, "Latitude ----------"+latitude + "");

        }
        EventBus.getDefault().register(this);
        mTakePhotoPickPhotoUtils = new TakePhotoPickPhotoUtils(publicMessageActivity.this, 1000);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        add_picture = (ImageView) findViewById(R.id.add_picture);
        picture = (ImageView) findViewById(R.id.picture);
        et_comments = (EditText) findViewById(R.id.et_idea);
        tv_publish_result = (TextView) findViewById(R.id.tv_publish_result);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_tips.setText(et_comments.length() + "/500");
        tv_cancel.setOnClickListener(this);
        add_picture.setOnClickListener(this);
        picture.setOnClickListener(this);
        tv_publish_result.setOnClickListener(this);
        et_comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_tips.setText(editable.length() + "/500");
            }
        });

//        tv_position.setText("(" + longitude + "," + latitude + ")");
        tv_position.setText(GetPositionUtil.getPosition(latitude, longitude));

    }
    private void showPopUpWindow() {
        View contentView = LayoutInflater.from(publicMessageActivity.this).inflate(R.layout.popupwindow_took_photo, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置点击空白地方消失
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置空白地方的背景色
        WindowManager.LayoutParams lp = publicMessageActivity.this.getWindow().getAttributes();
        lp.alpha = 0.6f;
        publicMessageActivity.this.getWindow().setAttributes(lp);
        //设置popupWindow消失的时候做的事情 即把背景色恢复
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp =  publicMessageActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                publicMessageActivity.this.getWindow().setAttributes(lp);
            }
        });

//        mPopWindow.setAnimationStyle(R.style.AnimationPreview);


        final Button bt_took_photo = (Button) contentView.findViewById(R.id.bt_delete_from_group);
        Button bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        Button bt_pick_photo = (Button) contentView.findViewById(R.id.bt_delete_student);


        bt_took_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
                startActivityForResult(mTakePhotoPickPhotoUtils.takePhoto(),PICK_FROM_CAMERA);

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });

        bt_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
                startActivityForResult(mTakePhotoPickPhotoUtils.pickPhoto(), PICK_FROM_FILE);
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from( publicMessageActivity.this).inflate(R.layout.activity_publish_message, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }


    private void publishResult() {
        File file = null;
        if(imageUri != null) {
            file = new File(imageUri.getPath().toString());
            tv_publish_result.setClickable(true);
            if (file.exists()) {
                if (et_comments.getText().toString().equals("")) {
                    Toast.makeText(publicMessageActivity.this, "请先将信息填写完整", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    sendPic(file);
                }
            } else {
                tv_publish_result.setClickable(true);
                Toast.makeText(publicMessageActivity.this, "图片文件不存在，请重新选择", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            Toast.makeText(publicMessageActivity.this,"请先选择一张图片",Toast.LENGTH_SHORT).show();
            tv_publish_result.setClickable(true);
        }
    }

    // Todo 将文件图片上传到服务器，然后将status信息传到后台
    private void sendPic(File file) {
        OkHttpUtils.post()
                .addFile("file", file.getName(), file)
                .url(Constant.URL.UPLOAD_POST_PIC_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i(TAG, e.toString());
                        Toast.makeText(publicMessageActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                        tv_publish_result.setClickable(true);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String url = jsonObject.getString("id");
                            if (msg.equals("0")) {
                                sendStatusMessage(url);
                            }else{
                                Toast.makeText(publicMessageActivity.this, "由于系统原因，发表状态失败", Toast.LENGTH_SHORT).show();
                                tv_publish_result.setClickable(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tv_publish_result.setClickable(true);
                        }
                    }
                });
    }

    private void sendStatusMessage(String url) {
        if (et_comments.getText().toString().equals("")) {
            Toast.makeText(publicMessageActivity.this, "请先将信息填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.post()
                .url(Constant.URL.PUBLISH_POST_URL)
                .addParams("u_id", PreferenceUtil.getString(publicMessageActivity.this, PreferenceUtil.USERID))
                .addParams("type", "0")
                .addParams("title", "标题")
                .addParams("content", et_comments.getText().toString())
                .addParams("media", url)
                .addParams("longitude", longitude + "")
                .addParams("latitude", latitude + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i(TAG, e.toString());
                        tv_publish_result.setClickable(true);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i(TAG, response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String msg = jsonObject1.getString("msg");
                            if (msg.equals("0")) {
                                Toast.makeText(publicMessageActivity.this, "发表帖子成功", Toast.LENGTH_SHORT).show();
//                                EventBus.getDefault().post(new TodayAchievementFinishEvent());
                                Intent a = new Intent(publicMessageActivity.this, HomePageActivity.class);
                                startActivity(a);
                                finish();
                            } else {
                                Toast.makeText(publicMessageActivity.this, "由于系统原因，发表失败", Toast.LENGTH_SHORT).show();
                                tv_publish_result.setClickable(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tv_publish_result.setClickable(true);
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
               publicMessageActivity.this.finish();
                break;
            case R.id.add_picture:
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_comments.getWindowToken(), 0);
                picture.setClickable(true);
                showPopUpWindow();
                break;
            case R.id.tv_publish_result:
                tv_publish_result.setClickable(false);
                publishResult();
                break;
            case R.id.picture:
                File sendfile = null;
                sendfile = new File(imageUri.getPath().toString());
                if (sendfile.exists()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("file", sendfile);
                    Intent a = new Intent(publicMessageActivity.this, ReleaseResultImageViewerActivity.class);
                    a.putExtras(bundle);
                    startActivity(a);
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode !=publicMessageActivity.RESULT_OK)
            return;
        switch (requestCode) {
            case PICK_FROM_FILE:
                Uri uriSelectInPhone = data.getData();
                Intent b = mTakePhotoPickPhotoUtils.startPhotoZoom(uriSelectInPhone);
                startActivityForResult(b, ACTION_CROP);
                break;

            case PICK_FROM_CAMERA:
                Intent a =  mTakePhotoPickPhotoUtils.startPhotoZoom(mTakePhotoPickPhotoUtils.getImageCaptureUri());
                startActivityForResult(a, ACTION_CROP);
                break;

            case ACTION_CROP:
                add_picture.setVisibility(View.GONE);
                picture.setVisibility(View.VISIBLE);
                type = 1;
                try {
                    imageUri = mTakePhotoPickPhotoUtils.getImageCropUri();
                    bitmap = BitmapFactory.decodeStream(publicMessageActivity.this.getContentResolver().openInputStream(imageUri));
                    picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                add_picture.setClickable(false);
                break;

        }
    }
    public void onEventMainThread(DeletePhotoEvent baseEvent) {
        add_picture.setVisibility(View.VISIBLE);
        add_picture.setClickable(true);
        type = 0;
        imageUri = null;
        picture.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
