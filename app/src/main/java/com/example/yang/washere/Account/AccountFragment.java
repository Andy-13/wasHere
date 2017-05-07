package com.example.yang.washere.Account;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yang.washere.HomePageActivity;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.TakePhotoPickPhotoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yang on 2017/4/28.
 */

public class AccountFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = AccountFragment.class.getSimpleName();

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        initViews(view);
        mTakePhotoPickPhotoUtils = new TakePhotoPickPhotoUtils(mContext, 500);
        setListeners();
        return view;
    }

    /**
     *
     */
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
                //TODO 拍照
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


    private void updateHead(Uri cropImageUri){
        roundedImageView.setImageURI(cropImageUri);
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = format.format(date);
        tv_birthday.setText(dateString);
        mBirthdayPickerFragment.dismiss();
    }

    /*
    *  重定向
    * */
    private void redirectToSetName(){
        Intent intent = new Intent(mContext,SetNameActivity.class);
        startActivity(intent);
    }

    private void redirectToSetSex(){
        Intent intent = new Intent(mContext,SetSexActivity.class);
        startActivity(intent);
    }

    private void redirectToSetSign(){
        Intent intent = new Intent(mContext,SetSignActivity.class);
        startActivity(intent);
    }

    private void redirectToLogin(){
        Intent intent = new Intent(mContext,LoginActivity.class);
        startActivity(intent);
        ((HomePageActivity)mContext).finish();
    }
}
