package com.example.yang.washere.MyMessage;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yang.washere.Comment.CommentItem;
import com.example.yang.washere.R;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;

import org.w3c.dom.Text;

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

        public MyMessageHolder(BaseViewHolder holder){
            ml_user_image = holder.getView(R.id.ml_user_image);
            tv_name = holder.getView(R.id.tv_name);
            tv_time = holder.getView(R.id.tv_time);
            tv_content= holder.getView(R.id.tv_content);
            iv_message_image = holder.getView(R.id.iv_message_image);
        }

        public void bindData(Messageitem item){
            ml_user_image.setImageDrawable(item.getMl_user_head());
            tv_name.setText(item.getUser_name());
            tv_time.setText(item.getPublish_time());
            tv_content.setText(item.getContent());
            iv_message_image.setImageDrawable(item.getPublish_image());
        }
    }
}
