package com.example.yang.washere.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class SetSignActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SetSignActivity.class.getSimpleName();

    private TextView tv_cancel;
    private TextView tv_finish;
    private EditText et_sign;
    private TextView tv_tips;

    private String oldSignature;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sign);
        oldSignature = getIntent().getStringExtra("sign");
        LogUtils.d(TAG,"get sign: " + oldSignature);

        initViews();
        setListeners();
        et_sign.setText(oldSignature);

    }

    private void initViews(){
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        et_sign = (EditText) findViewById(R.id.et_signature);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
    }

    private void setListeners(){
        tv_cancel.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_tips.setText(et_sign.length() + "/25");
        et_sign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = et_sign.length();
                tv_tips.setText(length+"/25");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                this.finish();
                break;
            case R.id.tv_finish:
                updateUserSignature();
                break;
            default:
                break;
        }
    }

    /**
     * 更新个性签名
     */
    private void updateUserSignature() {
        final String newSignature = et_sign.getText().toString().trim();
        if (TextUtils.isEmpty(newSignature)){
            shortToast("请输入有效的个性签名!");
            return;
        }
        if (oldSignature.equals(newSignature)){
            //do nothing
            finish();
        }

        String userId = PreferenceUtil.getString(this,PreferenceUtil.USERID);
        OkHttpUtils.post()
                .url(Constant.URL.URL_EDIT_USER_SIGN)
                .addParams("u_id",userId)
                .addParams("sign",newSignature)
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
                                shortToast("个性签名修改成功");
                                EventBus.getDefault().post(new EditInfoEvent(EditInfoEvent.TYPE_EDIT_SIGN,newSignature));
                                SetSignActivity.this.finish();
                            }else if ("1".equals(msg)){
                                shortToast("由于系统原因修改失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void shortToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
