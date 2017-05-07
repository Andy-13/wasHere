package com.example.yang.washere.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yang.washere.R;

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


    private static final int SEX_MAN = 0;
    private static final int SEX_WOMAN = 1;

    private int sex = SEX_MAN;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sex);

        initViews();
        setListeners();

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
                this.finish();
                break;

            case R.id.rl_man:
                sex = SEX_MAN;
                notifySexChanged();
                break;

            case R.id.rl_woman:
                sex = SEX_WOMAN;
                notifySexChanged();
                break;

            default:
                break;

        }
    }

    /**
     *  0；男 1：女
     */
    private void notifySexChanged(){
        if(sex == SEX_MAN){
            right_arrow_man.setVisibility(View.VISIBLE);
            right_arrow_woman.setVisibility(View.GONE);
        }else{
            right_arrow_man.setVisibility(View.GONE);
            right_arrow_woman.setVisibility(View.VISIBLE);
        }
    }
}
