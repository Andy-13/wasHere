package com.example.yang.washere.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.washere.R;

/**
 * Created by Lee Sima on 2017/5/3.
 */

public class SetSignActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SetSignActivity.class.getSimpleName();

    private TextView tv_cancel;
    private TextView tv_finish;
    private EditText et_sign;
    private TextView tv_tips;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sign);

        initViews();
        setListeners();

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
                this.finish();
                break;

            default:
                break;
        }
    }
}
