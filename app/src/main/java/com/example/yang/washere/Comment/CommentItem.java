package com.example.yang.washere.Comment;

import android.graphics.drawable.Drawable;

/**
 * Created by joe on 17-5-3.
 */

public class CommentItem {
    private Drawable ml_user_head;
    private String user_name;
    private String publish_time;
    private String comment_content;

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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public CommentItem(Drawable user_head, String name, String time, String content) {
        this.ml_user_head = user_head;
        this.user_name = name;
        this.publish_time = time;
        this.comment_content = content;
    }
}
