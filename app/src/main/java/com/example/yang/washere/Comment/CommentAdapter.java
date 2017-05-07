package com.example.yang.washere.Comment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yang.washere.R;
import com.example.yang.washere.UI.MLRoundedImageView;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by joe on 17-5-3.
 */

public class CommentAdapter extends BaseRecyclerAdapter<CommentItem> {
    public CommentAdapter(Context context, int layoutResId, List<CommentItem> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentItem item) {
        CommentHolder CommentHolder = new CommentHolder(holder);
        if (item != null) {
            CommentHolder.bindData(item);
        }
    }

    class CommentHolder{
        private MLRoundedImageView ml_user_image;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;

        public CommentHolder(BaseViewHolder holder){
            ml_user_image = holder.getView(R.id.ml_user_image);
            tv_name = holder.getView(R.id.tv_name);
            tv_time = holder.getView(R.id.tv_time);
            tv_content= holder.getView(R.id.tv_content);
        }

        public void bindData(CommentItem item){
            ml_user_image.setImageDrawable(item.getMl_user_head());
            tv_name.setText(item.getUser_name());
            tv_time.setText(item.getPublish_time());
            tv_content.setText(item.getComment_content());
        }
    }
}
