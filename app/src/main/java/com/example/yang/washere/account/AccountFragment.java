package com.example.yang.washere.account;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.HomePageActivity;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.example.yang.washere.Utils.TakePhotoPickPhotoUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Yang on 2017/4/28.
 */

public class AccountFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = AccountFragment.class.getSimpleName();

    private final static int GENDER_WOMAN = 0;
    private final static int GENDER_MAN = 1;

    private static Context mContext;

    private MLRoundedImageView roundedImageView;
    private BirthdayPickerFragment mBirthdayPickerFragment;
    private RelativeLayout rl_set_name;
    private RelativeLayout rl_set_sex;
    private RelativeLayout rl_set_birthday;
    private RelativeLayout rl_set_sign;

    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_birthday;
    private TextView tv_sign;

    private TextView btn_logout;

    private TakePhotoPickPhotoUtils mTakePhotoPickPhotoUtils;

    private UserInfo mUserInfo;

    public static final int CODE_PICK_FROM_CAMERA = 0;
    public static final int CODE_PICK_FROM_FILE = 1;
    public static final int CODE_ACTION_CROP = 2;

    public static AccountFragment newInstance(Context context) {
        Bundle args = new Bundle();
        mContext = context;
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        EventBus.getDefault().register(this);

        initViews(view);
        mTakePhotoPickPhotoUtils = new TakePhotoPickPhotoUtils(mContext, 500);
        setListeners();
        loadUserInfo();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 加载用户数据
     */
    private void loadUserInfo() {
        mUserInfo = new UserInfo();

        final String userId = PreferenceUtil.getString(mContext,PreferenceUtil.USERID);
        OkHttpUtils.get()
                .url(Constant.URL.URL_GET_USER_INFO)
                .addParams("u_id",userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG,"error: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
//                            0：成功
//                            1：其他
                            if ("0".equals(msg)){
                                JSONObject info = jsonObject.getJSONObject("userInf");

                                mUserInfo.setName(info.getString("u_name"));
                                mUserInfo.setPhone(info.getString("phone"));
                                mUserInfo.setHeadUrl(info.getString("head_logo"));
                                mUserInfo.setGender(info.getInt("gender"));
                                mUserInfo.setBirthday(info.getString("birthday"));
                                mUserInfo.setSignature(info.getString("signature"));

                                LogUtils.d(TAG,"userInfo: " + mUserInfo.toString());

                                updateUserInfo(mUserInfo);

                            }else if ("1".equals(msg)){
                                Toast.makeText(mContext,"获取用户信息失败!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    /**
     * 设置用户信息
     * @param userInfo
     */
    private void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null ){
            return;
        }
        Glide.with(mContext.getApplicationContext())
                .load(Constant.URL.BASE_URL + userInfo.getHeadUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_menu_camera)
                .into(roundedImageView);

        if (isTextEmpty(userInfo.getName())){
            tv_name.setText("未设置");
        }else{
            tv_name.setText(userInfo.getName());
        }

        if (isTextEmpty(userInfo.getSignature())){
            tv_sign.setText("未设置");
        }else{
            tv_sign.setText(userInfo.getSignature());
        }
        if (userInfo.getGender() == GENDER_WOMAN ){
            tv_sex.setText("女");
        }else if (userInfo.getGender() == GENDER_MAN ){
            tv_sex.setText("男");
        }
        if (isTextEmpty(userInfo.getBirthday())){
            tv_birthday.setText("未设置");
        }else{
            tv_birthday.setText(userInfo.getBirthday().substring(0,11));
        }

    }

    private void initViews(View view){
        roundedImageView = (MLRoundedImageView) view.findViewById(R.id.img_user);
        rl_set_name = (RelativeLayout) view.findViewById(R.id.rl_set_name);
        rl_set_sex = (RelativeLayout) view.findViewById(R.id.rl_set_sex);
        rl_set_birthday = (RelativeLayout) view.findViewById(R.id.rl_set_birthday);
        rl_set_sign = (RelativeLayout) view.findViewById(R.id.rl_set_sign);

        tv_name = (TextView) view.findViewById(R.id.tv_set_name);
        tv_sex = (TextView) view.findViewById(R.id.tv_set_sex);
        tv_birthday = (TextView) view.findViewById(R.id.tv_set_birthday);
        tv_sign = (TextView) view.findViewById(R.id.tv_set_sign);

        btn_logout = (TextView) view.findViewById(R.id.tv_logout);
    }

    /**
     *  add listeners
     */
    private void setListeners(){
        roundedImageView.setOnClickListener(this);
        rl_set_name.setOnClickListener(this);
        rl_set_sex.setOnClickListener(this);
        rl_set_birthday.setOnClickListener(this);
        rl_set_sign.setOnClickListener(this);

        btn_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_user:
                showPopUpWindow();
                break;

            case R.id.rl_set_name:
                redirectToSetName();
                break;

            case R.id.rl_set_sex:
                redirectToSetSex();
                break;

            case R.id.rl_set_birthday:
                showBirthdayDialog();
                break;
            case R.id.rl_set_sign:
                redirectToSetSign();
                break;

            case R.id.tv_logout:
                redirectToLogin();
                break;

            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            LogUtils.i("147258369", 333 + "");
            return;
        }
        switch (requestCode){

            case CODE_PICK_FROM_CAMERA:
                Uri pickImageUri = mTakePhotoPickPhotoUtils.getImageCaptureUri();
                Intent cropIntent1 = mTakePhotoPickPhotoUtils.startPhotoZoom(pickImageUri);
                startActivityForResult(cropIntent1,CODE_ACTION_CROP);
                break;

            case CODE_PICK_FROM_FILE:
                Uri selectedImageUri = data.getData();
                Intent cropIntent = mTakePhotoPickPhotoUtils.startPhotoZoom(selectedImageUri);
                startActivityForResult(cropIntent,CODE_ACTION_CROP);
                break;

            case CODE_ACTION_CROP:
                Uri cropImageUri = mTakePhotoPickPhotoUtils.getImageCropUri();
                updateHead(cropImageUri);
                break;

            default:
                break;
        }
    }


    /**
     * 更新用户头像
     * @param cropImageUri
     */
    private void updateHead(Uri cropImageUri){
        //roundedImageView.setImageURI(cropImageUri);
        File targetFile = new File(cropImageUri.getPath().toString());
        if (!targetFile.exists()){
            return;
        }
        String userId = PreferenceUtil.getString(mContext,PreferenceUtil.USERID);
        OkHttpUtils.post()
                .url(Constant.URL.URL_EDIT_USER_LOGO)
                .addParams("u_id",userId)
                .addFile("file",targetFile.getName(),targetFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "error: " + e.toString());
                        shortToast("修改头像失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " + response);
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("0")) {
                                String head_url = jsonObject.getString("id");
                                loadUserHead(head_url);
                                EventBus.getDefault().post(new EditInfoEvent(EditInfoEvent.TYPE_EDIT_LOGO,head_url));
                            } else if (msg.equals("1")) {
                                LogUtils.d(TAG, "上传图片文件失败");
                            }
                        } catch (JSONException je) {
                            LogUtils.d(TAG, "Execption:" + je.toString());

                        }
                    }

                });
    }

    /**
     * 加载用户头像
     * @param head_url
     *
     */
    private void loadUserHead(String head_url) {
        Glide.with(mContext)
                .load(Constant.URL.BASE_URL + head_url)
                .asBitmap()
                .into(roundedImageView);
    }
    /**
     *  打开修改头像选择界面
     */
    private void showPopUpWindow() {
        View contentView = LayoutInflater.from(mContext)
                .inflate(R.layout.popupwindow_took_photo_account, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置点击空白地方消失
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置空白地方的背景色
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        getActivity().getWindow().setAttributes(lp);
        //设置popupWindow消失的时候做的事情 即把背景色恢复
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        final Button bt_took_photo = (Button) contentView.findViewById(R.id.bt_take_photo);
        Button bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        Button bt_pick_photo = (Button) contentView.findViewById(R.id.bt_select_photo);
        bt_took_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
                startActivityForResult(mTakePhotoPickPhotoUtils.takePhoto(), CODE_PICK_FROM_CAMERA);
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
                startActivityForResult(mTakePhotoPickPhotoUtils.pickPhoto(), CODE_PICK_FROM_FILE);

            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.fragment_account,null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    /**
     *  显示日期选择器
     */
    private void showBirthdayDialog() {

        mBirthdayPickerFragment = new BirthdayPickerFragment();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = "2017-05-07";
        try {
            Date date = dateFormat.parse(birthday);
            mBirthdayPickerFragment.setSelectedDate(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        FragmentManager fragmentManager = ((HomePageActivity)mContext).getSupportFragmentManager();
        mBirthdayPickerFragment.show(fragmentManager,"date");
        mBirthdayPickerFragment.setOnDatePickerClickListener(new BirthdayPickerFragment.OnDatePickerClickListener() {
            @Override
            public void onCancelClick() {
                mBirthdayPickerFragment.dismiss();
            }

            @Override
            public void onAcceptClick(Date date) {
                setBirthday(date);
            }
        });
    }


    /**
     * @param date 设置的日期
     */
    private void setBirthday(Date date){

        SimpleDateFormat sendFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String sendDateString = sendFormat.format(date);
        mBirthdayPickerFragment.dismiss();
        String userId = PreferenceUtil.getString(mContext,PreferenceUtil.USERID);
        LogUtils.d(TAG,"userId: " + userId +",date: " + sendDateString);
        OkHttpUtils.post()
                .url(Constant.URL.URL_EDIT_USER_BIRTHDAY)
                .addParams("u_id",userId)
                .addParams("birthday",sendDateString)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG,"error:" + e.toString());
                        shortToast("修改生日失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
//                            0：修改成功
//                            1：其他
                            if ("0".equals(msg)){
                                mUserInfo.setBirthday(sendDateString);
                                tv_birthday.setText(sendDateString);
                                shortToast("修改成功");
                            }else if ("1".equals(msg)){
                                shortToast("由于系统原因修改失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * @param event
     * eventbus 2.0
     */
    public void onEventMainThread(EditInfoEvent event) {
        LogUtils.d(TAG,"onEvent:" + event.toString());
       switch (event.getType()){
           case EditInfoEvent.TYPE_EDIT_NAME:
               tv_name.setText(event.getData());
               mUserInfo.setName(event.getData());
               break;
           case EditInfoEvent.TYPE_EDIT_SEX:
               tv_sex.setText(event.getData());
               if ("男".equals(event.getData())){
                   mUserInfo.setGender(GENDER_MAN);
               }else if ("女".equals(event.getData())){
                   mUserInfo.setGender(GENDER_WOMAN);
               }
               break;
           case EditInfoEvent.TYPE_EDIT_SIGN:
               tv_sign.setText(event.getData());
               mUserInfo.setSignature(event.getData());
               break;
           default:
               break;
       }
    }

    /*
    *  重定向
    * */
    private void redirectToSetName(){
        Intent intent = new Intent(mContext,SetNameActivity.class);
        String name = mUserInfo.getName();
        if (isTextEmpty(name)){
            intent.putExtra("oldName","未设置");
        }else{
            intent.putExtra("oldName",name);
        }
        startActivity(intent);
    }

    private void redirectToSetSex(){
        Intent intent = new Intent(mContext,SetSexActivity.class);
        intent.putExtra("sex",mUserInfo.getGender());
        startActivity(intent);
    }

    private void redirectToSetSign(){
        Intent intent = new Intent(mContext,SetSignActivity.class);
        String sign = mUserInfo.getSignature();
        if (isTextEmpty(sign)){
            intent.putExtra("sign","未设置");
        }else{
            intent.putExtra("sign",sign);
        }
        startActivity(intent);
    }

    private void redirectToLogin(){
        Intent intent = new Intent(mContext,LoginActivity.class);
        PreferenceUtil.setString(mContext, PreferenceUtil.ISLOGIN, "0");
        startActivity(intent);
        ((HomePageActivity)mContext).finish();
    }

    /**
     * @param text
     * @return if text==null or text.equals("null")
     */
    private boolean isTextEmpty(String text){
        return TextUtils.isEmpty(text) || "null".equals(text);
    }

    /**
     * short toast
     * @param msg
     */
    private void shortToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
}
