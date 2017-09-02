package com.example.yang.washere.FindMsssage;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Yang on 2017/4/28.
 */

public class FindMessageFragment extends Fragment {

    private static final String TAG = "FindMessageFragment";
    private static final int MSG_UPDATE_MARKER = 1;
    private static final int HANDLER_DELAY_TIME = 1000;

    public static final int MAP_MIN_ZOOM_SIZE = 18;
    public static final int MAP_MAX_ZOOM_SIZE = 15;
    public static final float MAP_CIRCLE_RANGE = 120; //单位是米
    public static final float MAP_MESSAGE_STROKE = 0;
    private static final float markerZIndex  = 30;

    private MapView mapView;
    private AMap aMap;
    private Context context;
    private Circle centerCircle;
    private Circle messageCircle;
    private List<Marker> markerList;

    private int filter_time_position = 0;
    private int filter_num_position = 0;
    private static final int[]filter_time_values = new int[]{3,7,30,90,365,999};
    private static final int[]filter_num_values = new int[]{5,10,15};
    private List<TextView> timeOptions;
    private List<TextView> numOptions;
    private String[] filter_time_entries;
    private String[] filter_num_entries;


    private ImageButton btnRefresh;
    private Animation rotateAnim;
    private ImageButton btnSendMessage;
    private ImageButton btnSetRange;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_MARKER:
                    btnRefresh.clearAnimation();
                    btnRefresh.setClickable(true);
            }
        }
    };


    public static FindMessageFragment newInstance(Context context) {

        Bundle args = new Bundle();

        FindMessageFragment fragment = new FindMessageFragment();

        fragment.setArguments(args);

        fragment.setContext(context);

        return fragment;
    }

    private Location mLocation;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        aMap = mapView.getMap();
        mapView.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(getContext(),"到此一游","正在载入地图……");

        initData();
        initView(view);
        initListener();
        askForPermission();

        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    private void initData(){
        markerList = new ArrayList<Marker>();
        builder = new AlertDialog.Builder(getContext());

        filter_time_entries = getResources().getStringArray(R.array.map_filter_time_name_array);
        filter_num_entries = getResources().getStringArray(R.array.map_filter_num_name_array);
    }

    private void initView(View v){

        btnRefresh = (ImageButton) v.findViewById(R.id.btnRefresh);
        btnSendMessage = (ImageButton) v.findViewById(R.id.btnSendMessage);
        btnSetRange = (ImageButton) v.findViewById(R.id.btnSetRange);
        initLocationStyle();
    }

    private void initLocationStyle(){
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000);//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeWidth(0); //设置边框宽度
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.colorTransParent));
        BitmapDescriptor locationBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_black_48dp_2x);
        myLocationStyle.myLocationIcon(locationBitmap);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMinZoomLevel(aMap.getMaxZoomLevel() -1);
        aMap.setMaxZoomLevel(aMap.getMaxZoomLevel() -1);
    }

    private void initListener(){
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null){

                    LatLng curLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    if (centerCircle != null){

                        if (curLatLng.latitude == centerCircle.getCenter().latitude && curLatLng.longitude == centerCircle.getCenter().longitude){
                            return;
                        }
                        centerCircle.remove();
                    }

                    centerCircle = mapView.getMap().addCircle(new CircleOptions().center(new LatLng(location.getLatitude(),location.getLongitude()))
                            .fillColor(Color.argb(110,74,144,226))
                            .radius(MAP_CIRCLE_RANGE)
                            .strokeColor(R.color.colorTransParent)
                            .strokeWidth(0)); //单位是米

                    if (messageCircle != null){
                        if (AMapUtils.calculateLineDistance(curLatLng,messageCircle.getCenter()) < MAP_CIRCLE_RANGE){
                            return;
                        }
                        messageCircle.remove();
                    }
                }


            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //TODO
                //Toast.makeText(getContext(),"被点击的是第"+(index + 1)+"个marker",Toast.LENGTH_SHORT).show();
                PostItem item = (PostItem) marker.getObject();
                Log.d(TAG,"item:"+item.p_id);
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                intent.putExtra("p_id",item.p_id);
                intent.putExtra("p_type",item.p_type);
                intent.putExtra("latitude",item.latitude);
                intent.putExtra("longitude",item.longitude);
                startActivity(intent);
                return false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                progressDialog.dismiss();
                btnRefresh.performClick();
            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置不可点击和启动动画
                btnRefresh.setClickable(false);
                rotateAnim = AnimationUtils.loadAnimation(getContext(),R.anim.map_refresh_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                rotateAnim.setInterpolator(lin);
                v.startAnimation(rotateAnim);

                Location location = aMap.getMyLocation();

                PostForm postForm = new PostForm();
                postForm.num = getCurNum();
                postForm.time = getCurTime();
                postForm.longitude = location.getLongitude();
                postForm.latitude = location.getLatitude();
                postForm.radius = MAP_CIRCLE_RANGE;
                    StringBuilder url = new StringBuilder(Constant.URL.BASE_URL + "checkPostAround.action");
                    url.append("?longitude="+postForm.longitude)
                            .append("&latitude="+postForm.latitude)
                            .append("&radius="+postForm.radius)
                            .append("&num="+postForm.num)
                            .append("&time="+postForm.time);
                    NetworkUtils.doGet(url.toString()).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG,e.getMessage());
                            Toast.makeText(getContext(),"请检查网络连接情况后尝试！",Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessageDelayed(MSG_UPDATE_MARKER,HANDLER_DELAY_TIME);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d(TAG,"call:"+call.request().url().toString());
                            int code = response.code();
                            String body = response.body().string();
                            if (code == 200){
                                Log.d(TAG,"body:"+body);
                                Gson gson = new GsonBuilder().create();
                                ResultForm resultForm = gson.fromJson(body,new TypeToken<ResultForm>(){}.getType());
                                Log.d(TAG,resultForm.msg);
                                if ("0".equals(resultForm.msg)){
                                    updateMarker(resultForm.posts);
                                }else {
                                    Toast.makeText(getContext(),"数据获取失败！请重新尝试！",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(),"数据获取失败！请重新尝试！",Toast.LENGTH_SHORT).show();
                            }

                            handler.sendEmptyMessageDelayed(MSG_UPDATE_MARKER,HANDLER_DELAY_TIME);
                        }
                    });
            }
        });


        btnSetRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_map_filter,null,false);
                initDialog(view);
                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publicMessage = new Intent(context,publicMessageActivity.class);
                double longitude = aMap.getMyLocation().getLongitude();
                double latitude = aMap.getMyLocation().getLatitude();
                publicMessage.putExtra("longitude",longitude);
                publicMessage.putExtra("latitude",latitude);
                startActivity(publicMessage);

            }
        });
    }

    private int getCurTime(){
        switch (filter_time_position){
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 30;
            case 3:
                return 90;
            case 4:
                return 365;
            default:
                return 0;
        }
    }

    private int getCurNum(){
        switch (filter_num_position){
            case 0:
                return 5;
            case 1:
                return 10;
            default:
                return 15;
        }
    }

    private void updateMarker(List<PostItem> postItemList){

        Location curLocation = aMap.getMyLocation();

        if (messageCircle != null)
            messageCircle.remove();
        messageCircle = mapView.getMap().addCircle(new CircleOptions().center(new LatLng(curLocation.getLatitude(),curLocation.getLongitude()))
                .fillColor(Color.argb(0,0,0,0))
                .strokeWidth(MAP_MESSAGE_STROKE)
                .strokeColor(Color.argb(110,99,160,244))
                .radius(MAP_CIRCLE_RANGE));//判断是否更新圈的位置

        if (markerList == null){
            markerList = new ArrayList<Marker>();
        }
        while (markerList.size() > 0){
            Marker marker = markerList.remove(0);
            marker.remove();
        } //移除当前地图上的所有标记

        //测试数据
        postItemList.add(new PostItem("1","0",aMap.getMyLocation().getLongitude()+0.0001,aMap.getMyLocation().getLatitude()+0.0001));
        postItemList.add(new PostItem("2","0",aMap.getMyLocation().getLongitude()+0.0002,aMap.getMyLocation().getLatitude()+0.0002));
        postItemList.add(new PostItem("3","0",aMap.getMyLocation().getLongitude()+0.0003,aMap.getMyLocation().getLatitude()+0.0003));


        for (int i = 0;i<postItemList.size();i++) {
            PostItem item = postItemList.get(i);
            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.urgent_message))
                    .position(new LatLng(item.latitude,item.longitude))
                    .setFlat(true)
                    .zIndex(markerZIndex + i);
            Marker marker = aMap.addMarker(options);
            marker.setObject(item);
            markerList.add(marker);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"数据已更新！",Toast.LENGTH_SHORT).show();
            }
        });
//

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        askForPermission();
    }

    private void askForPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }


    private void initDialog(View view){
        timeOptions = new ArrayList<TextView>(6);
        timeOptions.add((TextView) view.findViewById(R.id.option_time_1));
        timeOptions.add((TextView) view.findViewById(R.id.option_time_2));
        timeOptions.add((TextView) view.findViewById(R.id.option_time_3));
        timeOptions.add((TextView) view.findViewById(R.id.option_time_4));
        timeOptions.add((TextView) view.findViewById(R.id.option_time_5));
        timeOptions.add((TextView) view.findViewById(R.id.option_time_6));

        timeOptions.get(filter_time_position).setTextColor(Color.WHITE);
        timeOptions.get(filter_time_position).setBackgroundResource(R.drawable.dialog_option_background);

        numOptions = new ArrayList<TextView>(3);
        numOptions.add((TextView) view.findViewById(R.id.option_num_1));
        numOptions.add((TextView) view.findViewById(R.id.option_num_2));
        numOptions.add((TextView) view.findViewById(R.id.option_num_3));
        numOptions.get(filter_num_position).setTextColor(Color.WHITE);
        numOptions.get(filter_num_position).setBackgroundResource(R.drawable.dialog_option_background);

        for (int i=0;i<6;i++){
            final  int pos = i;
            timeOptions.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j=0;j<timeOptions.size();j++){
                        if (pos == j) {
                            timeOptions.get(j).setTextColor(Color.WHITE);
                            timeOptions.get(j).setBackgroundResource(R.drawable.dialog_option_background);
                            filter_time_position = pos;
                        }
                        else {
                            timeOptions.get(j).setTextColor(getResources().getColor(R.color.colorPrimary));
                            timeOptions.get(j).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });
        }

        for (int i=0;i<3;i++){
            final int pos = i;
            numOptions.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j=0;j<numOptions.size();j++){
                        if (pos == j) {
                            numOptions.get(j).setTextColor(Color.WHITE);
                            numOptions.get(j).setBackgroundResource(R.drawable.dialog_option_background);
                            filter_num_position = pos;
                        }
                        else {
                            numOptions.get(j).setTextColor(getResources().getColor(R.color.colorPrimary));
                            numOptions.get(j).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });
        }
    }




    private class PostForm{
        double radius;
        double longitude;
        double latitude;
        int time;
        int num;
        int type;

        public PostForm(double radius,double longitude,double latitude,int time,int num,int type){
            this.radius = radius;
            this.longitude = longitude;
            this.latitude = latitude;
            this.time = time;
            this.num = num;
            this.type = type;
        }

        public PostForm(){}
    }

    private class ResultForm{
        String msg;
        List<PostItem> posts;
        public ResultForm(){
            posts = new ArrayList<>();
        }
    }

    private class PostItem{
        String p_id;
        String p_type;
        double longitude;
        double latitude;

        public PostItem(String p_id, String p_type, double longitude, double latitude){
            this.p_id = p_id;
            this.p_type = p_type;
            this.longitude = (float) longitude;
            this.latitude = (float) latitude;
        }

        public PostItem(String p_id,String p_type,float longitude,float latitude){
            this.p_id = p_id;
            this.p_type = p_type;
            this.longitude = longitude;
            this.latitude = latitude;
        }

    }

}
