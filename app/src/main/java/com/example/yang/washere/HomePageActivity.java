package com.example.yang.washere;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.yang.washere.About.AboutAppFragment;
import com.example.yang.washere.account.AccountFragment;
import com.example.yang.washere.account.LoginActivity;
import com.example.yang.washere.FindMsssage.FindMessageFragment;
import com.example.yang.washere.Guide.GuideFragment;
import com.example.yang.washere.MyMessage.MyMessageFragment;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RelativeLayout rl_account;
    private SharedPreferences sp;
    private int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        sp = this.getSharedPreferences("personalInfo", MODE_PRIVATE);
        if(sp.getString("account", "").equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        rl_account = (RelativeLayout)navigationView.getHeaderView(0).findViewById(R.id.rl_account);
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

}
