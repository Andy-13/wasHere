package com.example.yang.washere.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.callback.BitmapCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText accountEt;
    private EditText passwordEt;
    private EditText verificationEt;
    private ImageView pCodeImageView;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        initOkhttp();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegister();
            }
        });
        pCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPCode();
            }
        });

        loadPCode();
    }

    private void initOkhttp(){
        //配置OkHttp
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 注册
     */
    private void performRegister() {
        if (!checkInput()){
            return;
        }

        String account = accountEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String pCode = verificationEt.getText().toString().trim();

        OkHttpUtils.post()
                .url(Constant.URL.URL_REGISTER)
                .addParams("phone",account)
                .addParams("pwd",password)
                .addParams("pic_code",pCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG,"onError: " + e.toString());
                        shortToast("注册失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if ("0".equals(msg)){
                                shortToast("注册成功");
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(intent);
                                SignUpActivity.this.finish();
                            }else if ("1".equals(msg)){
                                shortToast("图片验证码错误");
                            }else if ("2".equals(msg)){
                                shortToast("该账号已存在!");
                            }else if ("3".equals(msg)){
                                shortToast("其他错误");
                            }else if ("4".equals(msg)){
                                shortToast("session 无验证码");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            shortToast("注册失败");
                        }
                    }
                });

    }

    /**
     * @return return true if input is validate;
     */
    private boolean checkInput() {
        if (accountEt.getText().toString().trim().length() != 11 ){
            shortToast("请输入正确的注册账号");
            return false;
        }
        if (passwordEt.getText().toString().trim().length() < 6){
            shortToast("密码至少为6位");
            return false;
        }
        if (verificationEt.getText().toString().trim().equals("")){
            shortToast("请输入验证码");
            return false;
        }
        return true;
    }

    /**
     * 加载图片验证码
     */
    private void loadPCode() {
        OkHttpUtils.get()
                .url(Constant.URL.URL_GET_VERIFY_CODE)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG,"error:" + e.toString());
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        pCodeImageView.setImageBitmap(response);
                    }
                });
    }

    private void initViews() {
        accountEt = (EditText) findViewById(R.id.signup_account);
        passwordEt = (EditText) findViewById(R.id.signup_password);
        verificationEt = (EditText) findViewById(R.id.verification);
        pCodeImageView = (ImageView) findViewById(R.id.send_verification);
        signUpButton = (Button) findViewById(R.id.signup);
    }

    private void shortToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
