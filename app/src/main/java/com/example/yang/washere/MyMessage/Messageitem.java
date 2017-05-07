package com.example.yang.washere.MyMessage;

import android.graphics.drawable.Drawable;

import com.example.yang.washere.Comment.CommentItem;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;

/**
 * Created by Yang on 2017/4/28.
 */

public class Messageitem {
    private Drawable ml_user_head;
    private String user_name;
    private String publish_time;
    private String content;
    private Drawable publish_image;
    private CommentItem[] comment_items;

    public Drawable getMl_user_head() {
        return ml_user_head;
    }

    public void setMl_user_head(Drawable ml_user_head) {
        this.ml_user_head = ml_user_head;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Drawable getPublish_image() {
        return publish_image;
    }

    public void setPublish_image(Drawable publish_image) {
        this.publish_image = publish_image;
    }

    public CommentItem[] getComment_item() {
        return comment_items;
    }

    public void setComment_item(CommentItem[] comment_items) {
        this.comment_items = comment_items;
    }

    public Messageitem(Drawable a, String name, String time, String content, Drawable b,CommentItem[] comment_items){
        ml_user_head = a;
        this.user_name = name;
        publish_time = time;
        this.content = content;
        publish_image = b;
        this.comment_items = comment_items;
    }


}
