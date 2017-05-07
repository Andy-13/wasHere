package com.example.yang.washere.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.washere.HomePageActivity;
import com.example.yang.washere.R;

public class LoginActivity extends AppCompatActivity {

    private EditText accountEt;
    private EditText passwordEt;
    private Button loginButton;
    private TextView forgetPasswordTv;
    private TextView signupTv;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = this.getSharedPreferences("personalInfo", MODE_PRIVATE);

        initViews();

        String account = sp.getString("account", "");
        String password = sp.getString("password", "");
        if(!account.isEmpty())
            accountEt.setText(account);
        if (!password.isEmpty())
            passwordEt.setText(password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEt.getText().toString();
                String password = passwordEt.getText().toString();
                sp.edit().putString("account", account).putString("password", password).apply();
                Log.i("account", sp.getString("account", "18826075040"));
                Log.i("password", sp.getString("password", ""));
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
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
