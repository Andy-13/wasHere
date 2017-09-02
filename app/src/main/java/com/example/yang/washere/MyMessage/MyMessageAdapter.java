package com.example.yang.washere.MyMessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yang.washere.Constant.Constant;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.GetPositionUtil;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Yang on 2017/4/28.
 */

public class MyMessageAdapter  extends BaseRecyclerAdapter<Messageitem> {
    public MyMessageAdapter(Context context, int layoutResId, List<Messageitem> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Messageitem item) {
        MyMessageHolder myMessageHolder = new MyMessageHolder(holder);
        if (item != null) {
            myMessageHolder.bindData(item);
        }
    }

    class MyMessageHolder{
        private MLRoundedImageView ml_user_image;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;
        private ImageView iv_message_image;
        private TextView tv_position;

        public MyMessageHolder(BaseViewHolder holder){
            ml_user_image = holder.getView(R.id.ml_user_image);
            tv_name = holder.getView(R.id.tv_name);
            tv_time = holder.getView(R.id.tv_time);
            tv_content= holder.getView(R.id.tv_content);
            iv_message_image = holder.getView(R.id.iv_message_image);
            tv_position = holder.getView(R.id.tv_position);
        }

        public void bindData(Messageitem item){
            String head_logo = item.getHead_logo();
            final String image = item.getMulti_media();
            Glide.with(mContext)
                    .load(Constant.URL.BASE_URL + head_logo)
                    .asBitmap()
                    .into(ml_user_image);
            Glide.with(mContext)
                    .load(Constant.URL.BASE_URL + image)
                    .asBitmap()
                    .into(iv_message_image);
            tv_name.setText(item.getUser_name());
            tv_time.setText(item.getPublish_time());
            tv_content.setText(item.getContent());
//            tv_position.setText("( "+ item.getLongitude()+" , " + item.getLatitude() +" )");
            tv_position.setText(GetPositionUtil.getPosition(item.getLatitude(), item.getLongitude()));
            iv_message_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Intent a = new Intent(mContext, ImageViewActivity.class);
                    bundle.putString("photo_url", image);
                    a.putExtras(bundle);
                    mContext.startActivity(a);
                }
            });
        }
    }
}
