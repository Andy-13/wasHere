package com.example.yang.washere.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Lee Sima on 2017/5/3.
 */

public class SetSexActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG = SetSexActivity.class.getSimpleName();

    private TextView tv_cancel;
    private TextView tv_finish;

    private RelativeLayout rl_man;
    private RelativeLayout rl_woman;

    private ImageView right_arrow_man;
    private ImageView right_arrow_woman;


    private static final int GENDER_WOMAN = 0;
    private static final int GENDER_MAN = 1;

    private int sex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sex);

        sex = getIntent().getIntExtra("sex",GENDER_MAN);

        initViews();
        setListeners();
        notifySexChanged();

    }

    private void initViews(){

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        rl_man = (RelativeLayout) findViewById(R.id.rl_man);
        rl_woman = (RelativeLayout) findViewById(R.id.rl_woman);

        right_arrow_man = (ImageView) findViewById(R.id.right_arrow1);
        right_arrow_woman = (ImageView) findViewById(R.id.right_arrow2);

    }

    private void setListeners(){
        tv_cancel.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        rl_man.setOnClickListener(this);
        rl_woman.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                this.finish();
                break;

            case R.id.tv_finish:
                updateUserGender();
                break;

            case R.id.rl_man:
                sex = GENDER_MAN;
                notifySexChanged();
                break;

            case R.id.rl_woman:
                sex = GENDER_WOMAN;
                notifySexChanged();
                break;

            default:
                break;

        }
    }

    /**
     * 发送
     */
    private void updateUserGender() {

        String userId = PreferenceUtil.getString(this,PreferenceUtil.USERID);
        OkHttpUtils.post()
                .url(Constant.URL.URL_EDIT_USER_GENDER)
                .addParams("u_id",userId)
                .addParams("gender",sex+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG,"error: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " +response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if ("0".equals(msg)){
                                shortToast("性别修改成功");
                                EventBus.getDefault().post(new EditInfoEvent(EditInfoEvent.TYPE_EDIT_SEX,getSexDescription(sex)));
                                SetSexActivity.this.finish();
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
     *  0；男 1：女
     */
    private void notifySexChanged(){
        if(sex == GENDER_MAN){
            right_arrow_man.setVisibility(View.VISIBLE);
            right_arrow_woman.setVisibility(View.GONE);
        }else{
            right_arrow_man.setVisibility(View.GONE);
            right_arrow_woman.setVisibility(View.VISIBLE);
        }
    }

    private String getSexDescription(int sex){
        String res = "男";

        if (GENDER_MAN == sex){
            res =  "男";
        } else if (GENDER_WOMAN == sex){
           res =  "女";
        }
        return res;
    }
    private void shortToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
