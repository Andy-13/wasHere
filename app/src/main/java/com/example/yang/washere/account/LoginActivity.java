package com.example.yang.washere.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.HomePageActivity;
import com.example.yang.washere.R;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.example.yang.washere.callback.ResultStringCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    private EditText accountEt;
    private EditText passwordEt;
    private Button loginButton;
    private TextView forgetPasswordTv;
    private TextView signupTv;
//    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        sp = this.getSharedPreferences("personalInfo", MODE_PRIVATE);

        initViews();

        String isLogin = PreferenceUtil.getString(LoginActivity.this, PreferenceUtil.ISLOGIN);
        if (isLogin.equals("1")) {
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
//        String account = sp.getString("account", "");
//        String password = sp.getString("password", "");
//        if(!account.isEmpty())
//            accountEt.setText(account);
//        if (!password.isEmpty())
//            passwordEt.setText(password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEt.getText().toString();
                String password = passwordEt.getText().toString();
//                sp.edit().putString("account", account).putString("password", password).apply();
//                Log.i("account", sp.getString("account", "18826075040"));
//                Log.i("password", sp.getString("password", ""));
                OkHttpUtils.post()
                        .url(Constant.URL.LOGIN_URL)
                        .addParams("phone",account)
                        .addParams("pwd",password)
                        .build()
                        .execute(new ResultStringCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.i("login fail reason----->", e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    LogUtils.i("LoginActivity -----" + response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    String userId = jsonObject.getString("id");

                                    if (msg.equals("0")) {
                                        PreferenceUtil.setString(LoginActivity.this, PreferenceUtil.ISLOGIN, "1");
                                        PreferenceUtil.setString(LoginActivity.this, PreferenceUtil.USERID, userId);
                                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }else if (msg.equals("1")) {
                                        Toast.makeText(LoginActivity.this, "账号密码不匹配", Toast.LENGTH_SHORT).show();
                                    } else if (msg.equals("2")) {
                                        Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                                    }else if (msg.equals("3")) {
                                        Toast.makeText(LoginActivity.this, "由于系统原因，登陆失败", Toast.LENGTH_SHORT).show();
                                    }
                                    accountEt.setText("");
                                    passwordEt.setText("");
                                } catch (JSONException je) {
                                    Toast.makeText(LoginActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        accountEt = (EditText) findViewById(R.id.login_account);
        passwordEt = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login);
        forgetPasswordTv = (TextView) findViewById(R.id.forget_password);
        signupTv = (TextView) findViewById(R.id.signup);
    }
}
