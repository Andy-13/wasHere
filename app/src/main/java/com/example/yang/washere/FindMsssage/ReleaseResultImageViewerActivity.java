package com.example.yang.washere.FindMsssage;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;


import com.example.yang.washere.R;
import com.example.yang.washere.UI.PinchImageView;
import com.example.yang.washere.event.DeletePhotoEvent;

import java.io.File;

import de.greenrobot.event.EventBus;


/**
 * Created by Yang on 2017/3/1.
 */

public class ReleaseResultImageViewerActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv_delete;
    private PinchImageView pv_photo;
    private File file;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_photo_viewer);
        pv_photo = (PinchImageView) findViewById(R.id.pv_photo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        Bundle bundle = getIntent().getExtras();
        file = (File)bundle.getSerializable("file");
        pv_photo.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        iv_delete.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        pv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_delete:
                EventBus.getDefault().post(new DeletePhotoEvent());
                finish();
                break;
        }
    }
}
