package com.example.yang.washere.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.washere.R;

/**
 * Created by Lee Sima on 2017/5/3.
 */

public class SetNameActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SetNameActivity.class.getSimpleName();

    private TextView tv_cancel;
    private TextView tv_finish;
    private EditText et_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        initViews();
        setListeners();
    }

    private void initViews(){
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        et_name = (EditText) findViewById(R.id.et_name);
    }

    private void setListeners(){
        tv_cancel.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                this.finish();
                break;

            case R.id.tv_finish:
                Log.d(TAG,"finish");
                Toast.makeText(this,et_name.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
