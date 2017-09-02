package com.example.yang.washere.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

public class SetNameActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SetNameActivity.class.getSimpleName();

    private TextView tv_cancel;
    private TextView tv_finish;
    private EditText et_name;
    String oldName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        oldName = getIntent().getStringExtra("oldName");
        LogUtils.d(TAG,"oldName: " + oldName);
        initViews();
        setListeners();
    }

    private void initViews(){
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setText(oldName);
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
                updateUserName();
                break;
            default:
                break;
        }
    }

    /**
     * 根据输入框的内容执行操作
     */
    private void updateUserName() {
        final String newName = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(newName)){
            shortToast("请输入有效的昵称!");
            return;
        }
        if (oldName.equals(newName)){
            //do nothing
            finish();
        }
        String userId = PreferenceUtil.getString(this,PreferenceUtil.USERID);
        OkHttpUtils.post()
                .url(Constant.URL.URL_EDIT_USER_NAME)
                .addParams("u_id",userId)
                .addParams("u_name",newName)
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
                                shortToast("昵称修改成功");
                                EventBus.getDefault().post(new EditInfoEvent(EditInfoEvent.TYPE_EDIT_NAME,newName));
                                SetNameActivity.this.finish();
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
