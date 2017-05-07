package com.example.yang.washere.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yang.washere.R;

public class SignupActivity extends AppCompatActivity {

    private EditText accountEt;
    private EditText passwordEt;
    private EditText phoneEt;
    private EditText verificationEt;
    private Button sendVerificationButton;
    private Button signupButton;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sp = this.getSharedPreferences("personalInfo", MODE_PRIVATE);
        initViews();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();
                String verification = verificationEt.getText().toString().trim();
                sp.edit().putString("account", account).putString("password", password).apply();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        sendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initViews() {
        accountEt = (EditText) findViewById(R.id.signup_account);
        passwordEt = (EditText) findViewById(R.id.signup_password);
        phoneEt = (EditText) findViewById(R.id.phone);
        verificationEt = (EditText) findViewById(R.id.verification);
        sendVerificationButton = (Button) findViewById(R.id.send_verification);
        signupButton = (Button) findViewById(R.id.signup);
    }
}
