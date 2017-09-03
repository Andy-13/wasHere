package com.example.yang.washere;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.washere.About.AboutAppFragment;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.example.yang.washere.Utils.LogUtils;
import com.example.yang.washere.Utils.PreferenceUtil;
import com.example.yang.washere.account.AccountFragment;
import com.example.yang.washere.account.EditInfoEvent;
import com.example.yang.washere.FindMsssage.FindMessageFragment;
import com.example.yang.washere.Guide.GuideFragment;
import com.example.yang.washere.MyMessage.MyMessageFragment;
import com.example.yang.washere.account.UserInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomePageActivity.class.getSimpleName();

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RelativeLayout rl_account;
    private MLRoundedImageView imageView;
    private TextView userName;
    private UserInfo mUserInfo;
    private int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        EventBus.getDefault().register(this);
        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        rl_account = (RelativeLayout)navigationView.getHeaderView(0).findViewById(R.id.rl_account);
        imageView = (MLRoundedImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        loadUserInfo();
        rl_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
                AccountFragment accountFragment = AccountFragment.newInstance(HomePageActivity.this);
                mTransaction.replace(R.id.content_home_page,accountFragment,"account");
                mTransaction.commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        FindMessageFragment findMessageFragment = FindMessageFragment.newInstance(HomePageActivity.this);
        mTransaction.replace(R.id.content_home_page,findMessageFragment,"findMessageFragment");
        mTransaction.commit();

    }

    /**
     * 加载用户头像
     */
    private void loadUserInfo() {

        mUserInfo = new UserInfo();

        final String userId = PreferenceUtil.getString(this,PreferenceUtil.USERID);
        OkHttpUtils.get()
                .url(Constant.URL.URL_GET_USER_INFO)
                .addParams("u_id",userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG,"error: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG,"onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
//                            0：成功
//                            1：其他
                            if ("0".equals(msg)){
                                JSONObject info = jsonObject.getJSONObject("userInf");

                                mUserInfo.setName(info.getString("u_name"));
                                mUserInfo.setPhone(info.getString("phone"));
                                mUserInfo.setHeadUrl(info.getString("head_logo"));
                                mUserInfo.setGender(info.getInt("gender"));
                                mUserInfo.setBirthday(info.getString("birthday"));
                                mUserInfo.setSignature(info.getString("signature"));

                                LogUtils.d(TAG,"userInfo: " + mUserInfo.toString());
                                Glide.with(HomePageActivity.this)
                                        .load(Constant.URL.BASE_URL + mUserInfo.getHeadUrl())
                                        .asBitmap()
                                        .into(imageView);
                                userName.setText(mUserInfo.getName());

                            }else if ("1".equals(msg)){
                                Toast.makeText(HomePageActivity.this,"获取用户信息失败!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(type == 1){
                Toast.makeText(HomePageActivity.this,"您开启了通知服务",Toast.LENGTH_SHORT).show();
                type = 2;
            }else{
                Toast.makeText(HomePageActivity.this,"您关闭了通知服务",Toast.LENGTH_SHORT).show();
                type = 1;
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
        if(id==R.id.find_message){
            FindMessageFragment findMessageFragment = FindMessageFragment.newInstance(HomePageActivity.this);
            mTransaction.replace(R.id.content_home_page,findMessageFragment,"findMessageFragment");
        }else if (id == R.id.my_message) {
            MyMessageFragment myMessage = MyMessageFragment.newInstance(HomePageActivity.this);
            mTransaction.replace(R.id.content_home_page,myMessage,"Message");
        } else if (id == R.id.guide) {
            GuideFragment guideFragment = GuideFragment.newInstance(HomePageActivity.this);
            mTransaction.replace(R.id.content_home_page,guideFragment,"Guide");
        } else if (id == R.id.about) {
            AboutAppFragment aboutAppFragment= AboutAppFragment.newInstance(HomePageActivity.this);
            mTransaction.replace(R.id.content_home_page,aboutAppFragment,"About");
        }
        mTransaction.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @param event
     * eventbus 2.0
     */
    public void onEventMainThread(EditInfoEvent event){
        if (event.getType() == EditInfoEvent.TYPE_EDIT_LOGO){
            Glide.with(this)
                    .load(Constant.URL.BASE_URL + event.getData())
                    .asBitmap()
                    .into(imageView);
            return;
        }
        if (event.getType() == EditInfoEvent.TYPE_EDIT_NAME){
            userName.setText(event.getData());
            return;
        }
    }

}
